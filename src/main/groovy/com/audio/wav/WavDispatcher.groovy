package audio.wav

import audio.DSPConfig
import audio.utils.Dispatcher
import audio.utils.Utils
import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.onsets.ComplexOnsetDetector
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchDetectionResult
import be.tarsos.dsp.pitch.PitchProcessor

class WavDispatcher extends Dispatcher {

    // Store detected plucks with timestamps
    //HashMap<Double,String> pluckedTimes = new HashMap<>()

    WavDispatcher(String path, int sampleRate) {
        super(path, sampleRate)
        //setupOnsetDetector()
        //setupPitchProcessor()
    }

    void setupOnsetDetector() {
        // Onset Detector: Detects when a string is plucked
        def onsetDetector = new ComplexOnsetDetector(bufferSize, DSPConfig.WAV_PEAK_THRESHOLD, DSPConfig.WAV_MIN_INTER_ONSET_INTERVAL)
        onsetDetector.setHandler { double timestamp, double note ->
            pluckedTimes.put(timestamp, '')
            println "🎸 String plucked at ${timestamp.round(3)} sec"
        }
        addAudioProcessor(onsetDetector)
    }

    void setupPitchProcessor() {
        // Pitch Detector: Detects the frequency of the played note
        PitchDetectionHandler pitchHandler = { PitchDetectionResult result, AudioEvent event ->
            double pitch = result.getPitch()
            if (pitch > 0) {
                String note = Utils.frequencyToNote(pitch)

                // Find the closest pluck event
                def closestPluck = pluckedTimes.keySet().max()

                if (pluckedTimes[closestPluck] == '') {
                    pluckedTimes[closestPluck] = note
                    println "🎶 Note detected: ${note} (${pitch.round(2)} Hz) at ${closestPluck.round(3)} sec"
                }
            }
        }

        PitchProcessor pitchProcessor = new PitchProcessor(DSPConfig.WAV_PITCH_ALGO, sampleRate, bufferSize, pitchHandler)
        addAudioProcessor(pitchProcessor)
    }

}
