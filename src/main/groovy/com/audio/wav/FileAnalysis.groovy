package audio.wav

import javax.sound.sampled.*

class FileAnalysis {

    def detectNotes(File wavFile) {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(wavFile)
        AudioFormat format = audioStream.getFormat()

        int sampleRate = format.getSampleRate() as int
        int bufferSize = 1024 // Buffer size for pitch tracking

        WavDispatcher dispatcher = new WavDispatcher(wavFile.absolutePath, sampleRate)

        //// Store detected plucks with timestamps
        //HashMap<Double,String> pluckedTimes = new HashMap<>()
        //
        //// Onset Detector: Detects when a string is plucked
        //def onsetDetector = new ComplexOnsetDetector(bufferSize, 1.0, 0.02)
        //
        //onsetDetector.setHandler { double timestamp, double note ->
        //    pluckedTimes.put(timestamp, '')
        //    println "🎸 String plucked at ${timestamp.round(3)} sec"
        //}
        //
        //// Pitch Detector: Detects the frequency of the played note
        //def pitchHandler = { PitchDetectionResult result, AudioEvent event ->
        //    double pitch = result.getPitch()
        //    if (pitch > 0) {
        //        String note = Utils.frequencyToNote(pitch)
        //
        //        // Find the closest pluck event
        //        def closestPluck = pluckedTimes.keySet().max()
        //
        //        if (pluckedTimes[closestPluck] == '') {
        //            pluckedTimes[closestPluck] = note
        //            println "🎶 Note detected: ${note} (${pitch.round(2)} Hz) at ${closestPluck.round(3)} sec"
        //        }
        //    }
        //} as PitchDetectionHandler
        //
        //PitchProcessor pitchProcessor = new PitchProcessor(DSPConfig.WAV_PITCH_ALGO, sampleRate, bufferSize, pitchHandler)
        //
        //
        ////dispatcher.addAudioProcessor(new PitchProcessor(
        ////        PitchProcessor.PitchEstimationAlgorithm.YIN,
        ////        sampleRate,
        ////        bufferSize,
        ////        { PitchDetectionResult result, AudioEvent event ->
        ////            double pitch = result.getPitch()
        ////            if (pitch > 0) {
        ////                String note = frequencyToNote(pitch)
        ////                println "Detected: ${note} (${pitch.round(2)} Hz) at ${event.getTimeStamp().round(2)} sec"
        ////            }
        ////        } as PitchDetectionHandler
        ////))
        //
        //// Create an onset detector
        ////def onsetDetector = new ComplexOnsetDetector(bufferSize, 0.85) // Sensitivity factor
        //
        ////onsetDetector.setHandler { timeStamp, ignored ->
        ////    println "String plucked at ${timeStamp.round(3)} sec -- ${ignored}"
        ////}
        //
        //dispatcher.addAudioProcessor(onsetDetector)
        //dispatcher.addAudioProcessor(pitchProcessor)

        dispatcher.run()
    }
}
