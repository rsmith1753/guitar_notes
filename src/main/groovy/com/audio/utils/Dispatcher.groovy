package com.audio.utils

import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.AudioProcessor
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.UniversalAudioInputStream
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory
import be.tarsos.dsp.onsets.ComplexOnsetDetector
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchDetectionResult
import be.tarsos.dsp.pitch.PitchProcessor
import com.events.Event
import com.events.NoteSubscriber
import com.events.PubSub
import com.events.Subscriber

abstract class Dispatcher extends AudioDispatcherFactory {

    protected AudioDispatcher dispatcher
    protected int sampleRate
    protected int bufferSize
    protected int overlap
    protected HashMap<Double,String> pluckedTimes = new HashMap<>()
    protected double lastPluckedTime = -1.0

    //PubSub pubSub

    // WAV
    Dispatcher(String path, int sampleRate) {
        //pubSub = PubSub.getInstance()
        this.sampleRate = sampleRate
        this.bufferSize = DSPConfig.WAV_BUFFER_SIZE
        this.overlap = DSPConfig.WAV_OVERLAP
        this.dispatcher = fromPipe(path, sampleRate, bufferSize, overlap)
        this.setupWavAudioProcessors()
    }

    // LIVE
    Dispatcher(UniversalAudioInputStream tarsosStream) {
        this.sampleRate = DSPConfig.LIVE_SAMPLE_RATE
        this.bufferSize = DSPConfig.LIVE_BUFFER_SIZE
        this.overlap = DSPConfig.LIVE_OVERLAP
        this.dispatcher = new AudioDispatcher(tarsosStream, bufferSize, overlap)
        this.setupLiveAudioProcessors()
    }

    private void setupWavAudioProcessors() {
        // Onset Detector: Detects when a string is plucked
        def onsetDetector = new ComplexOnsetDetector(bufferSize, DSPConfig.WAV_PEAK_THRESHOLD, DSPConfig.WAV_MIN_INTER_ONSET_INTERVAL)
        onsetDetector.setHandler { double timestamp, double note ->
            pluckedTimes.put(timestamp, '')
            println "🎸 String plucked at ${timestamp.round(3)} sec"
        }
        // Pitch Detector: Detects the frequency of the played note
        PitchDetectionHandler pitchHandler = { PitchDetectionResult result, AudioEvent audioEvent ->
            double pitch = result.getPitch()
            if (pitch > 0) {
                String note = Utils.frequencyToNote(pitch)

                // Find the closest pluck event
                def closestPluck = pluckedTimes.keySet().max()

                if (pluckedTimes[closestPluck] == '') {
                    pluckedTimes[closestPluck] = note
                    //println "🎶 Note detected: ${note} (${pitch.round(2)} Hz) at ${closestPluck.round(3)} sec"
                    PubSub.instance.publish('Pluck', new Event(note: note, frequency: pitch.round(2)))
                }
            }
        }
        PitchProcessor pitchProcessor = new PitchProcessor(DSPConfig.WAV_PITCH_ALGO, sampleRate, bufferSize, pitchHandler)
        addAudioProcessor(onsetDetector)
        addAudioProcessor(pitchProcessor)
    }

    private void setupLiveAudioProcessors() {
        def onsetDetector = new ComplexOnsetDetector(bufferSize, DSPConfig.LIVE_PEAK_THRESHOLD)
        onsetDetector.setHandler { double timestamp, double note ->
            //println "🎸 String plucked at ${timestamp.round(3)} sec"
            lastPluckedTime = timestamp
            PubSub.instance.publish('Pluck', new Event(timestamp: timestamp))
        }
        PitchDetectionHandler pitchHandler = { PitchDetectionResult result, AudioEvent event ->
            double pitch = result.getPitch()
            //if (pitch > 0) {
            String note = Utils.frequencyToNote(pitch)
            //println "🎶 Note detected: ${note} (${pitch.round(2)} Hz)"
            if (lastPluckedTime > 0 && note != 'Unknown') {
                //println "🎸 String plucked at ${lastPluckedTime.round(3)} sec - Note: ${note} (${pitch.round(3)} Hz)"
                //println((result.probability * 100).round(3))
                PubSub.instance.publish('Note', new Event(timestamp: lastPluckedTime, note: note, frequency: pitch.round(2)))
                lastPluckedTime = -1.0

                if(Math.abs(NoteSubscriber.instance.lastFrequency() - pitch) > 1.5) {
                    println('Bending')
                }
            }
        }
        PitchProcessor pitchProcessor = new PitchProcessor(DSPConfig.LIVE_PITCH_ALGO, sampleRate, bufferSize, pitchHandler)
        addAudioProcessor(onsetDetector)
        addAudioProcessor(pitchProcessor)
    }

    void addAudioProcessor(AudioProcessor audioProcessor) {
        this.dispatcher.addAudioProcessor(audioProcessor)
    }

    void addAudioProcessor(ComplexOnsetDetector onsetDetector) {
        this.dispatcher.addAudioProcessor(onsetDetector)
    }

    void run() {
        this.dispatcher.run()
    }
}
