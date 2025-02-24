package audio.live

import be.tarsos.dsp.io.TarsosDSPAudioFormat
import be.tarsos.dsp.io.UniversalAudioInputStream
import javax.sound.sampled.AudioInputStream

class RealTimeAnalysis {

    def analyzeGuitarRealTime() {
        // Find AXE I/O interface
        //def mixerInfo = AudioSystem.getMixerInfo().find { it.name.contains("AXE I/O") }
        //if (!mixerInfo) {
        //    println "❌ AXE I/O interface not found. Using default input."
        //    mixerInfo = AudioSystem.getMixerInfo().find { it.name.contains("USB") || it.name.contains("Built-in") }
        //}
        //
        //TargetDataLine line
        //AudioFormat format = new AudioFormat(44100, 16, 1, true, true) // 44.1kHz, Mono
        //DataLine.Info info = new DataLine.Info(TargetDataLine, format)
        //
        //if (AudioSystem.isLineSupported(info)) {
        //    line = AudioSystem.getMixer(mixerInfo).getLine(info) as TargetDataLine
        //    line.open(format)
        //    line.start()
        //} else {
        //    println "❌ No supported audio input found!"
        //    return
        //}

        TargetData targetData = new TargetData('AXE I/O')
        targetData.start()

        // Wrap TargetDataLine in an AudioInputStream
        AudioInputStream audioStream = new AudioInputStream(targetData.line)

        // Convert Java Sound format to TarsosDSP format
        TarsosDSPAudioFormat tarsosFormat = new TarsosDSPAudioFormat(targetData.format.sampleRate, 16, 1, true, true)

        // Wrap in TarsosDSP input stream
        UniversalAudioInputStream tarsosStream = new UniversalAudioInputStream(audioStream, tarsosFormat)


        LiveDispatcher dispatcher = new LiveDispatcher(tarsosStream)

        //def dispatcher = new AudioDispatcher(tarsosStream, bufferSize, overlap)
        //double lastPluckedTime = -1.0
        //String lastDetectedNote = ""
        //
        //// Onset Detector
        //def onsetDetector = new ComplexOnsetDetector(bufferSize, 0.25)
        //onsetDetector.setHandler { double timestamp, double note ->
        //    //println "🎸 String plucked at ${timestamp.round(3)} sec"
        //    lastPluckedTime = timestamp
        //}
        //
        //// Pitch Detector
        //def pitchHandler = { PitchDetectionResult result, AudioEvent event ->
        //    double pitch = result.getPitch()
        //    //if (pitch > 0) {
        //    String note = Utils.frequencyToNote(pitch)
        //    println "🎶 Note detected: ${note} (${pitch.round(2)} Hz)"
        //    if (lastPluckedTime > 0 && note != 'Unknown') {
        //        println "🎸 String plucked at ${lastPluckedTime.round(3)} sec - Note: ${note} (${pitch.round(3)} Hz)"
        //        lastPluckedTime = -1.0
        //    }
        //} as PitchDetectionHandler
        //
        //dispatcher.addAudioProcessor(onsetDetector)
        //dispatcher.addAudioProcessor(new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 44100, bufferSize, pitchHandler))
        //
        //println "🎸 Listening to AXE I/O input... Play your guitar!"
        dispatcher.run()
    }
}
