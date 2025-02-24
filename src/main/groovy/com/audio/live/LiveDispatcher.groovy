package audio.live

import audio.DSPConfig
import audio.utils.Dispatcher
import audio.utils.Utils
import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.io.UniversalAudioInputStream
import be.tarsos.dsp.onsets.ComplexOnsetDetector
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchDetectionResult
import be.tarsos.dsp.pitch.PitchProcessor

class LiveDispatcher extends Dispatcher {

    //double lastPluckedTime = -1.0

    LiveDispatcher(UniversalAudioInputStream tarsosStream) {
        super(tarsosStream)
        //setupOnsetDetector()
        //setupPitchProcessor()
    }

    void setupOnsetDetector() {
        // Onset Detector
        def onsetDetector = new ComplexOnsetDetector(bufferSize, DSPConfig.WAV_PEAK_THRESHOLD)
        onsetDetector.setHandler { double timestamp, double note ->
            //println "🎸 String plucked at ${timestamp.round(3)} sec"
            lastPluckedTime = timestamp
        }
        addAudioProcessor(onsetDetector)
    }

    void setupPitchProcessor() {
        PitchDetectionHandler pitchHandler = { PitchDetectionResult result, AudioEvent event ->
            double pitch = result.getPitch()
            //if (pitch > 0) {
            String note = Utils.frequencyToNote(pitch)
            println "🎶 Note detected: ${note} (${pitch.round(2)} Hz)"
            if (lastPluckedTime > 0 && note != 'Unknown') {
                println "🎸 String plucked at ${lastPluckedTime.round(3)} sec - Note: ${note} (${pitch.round(3)} Hz)"
                lastPluckedTime = -1.0
            }
        }

        PitchProcessor pitchProcessor = new PitchProcessor(DSPConfig.LIVE_PITCH_ALGO, sampleRate, bufferSize, pitchHandler)
        addAudioProcessor(pitchProcessor)
    }
}
