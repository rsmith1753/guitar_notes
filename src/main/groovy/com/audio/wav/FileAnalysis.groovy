package com.audio.wav

import javax.sound.sampled.*

class FileAnalysis {

    def detectNotes(File wavFile) {
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(wavFile)
        AudioFormat format = audioStream.getFormat()

        int sampleRate = format.getSampleRate() as int
        int bufferSize = 1024 // Buffer size for pitch tracking

        WavDispatcher dispatcher = new WavDispatcher(wavFile.absolutePath, sampleRate)

        dispatcher.run()
    }
}
