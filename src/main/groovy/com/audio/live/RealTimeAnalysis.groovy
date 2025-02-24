package com.audio.live

import be.tarsos.dsp.io.TarsosDSPAudioFormat
import be.tarsos.dsp.io.UniversalAudioInputStream
import com.events.PubSub

import javax.sound.sampled.AudioInputStream

class RealTimeAnalysis {


    PubSub pubSub

    def analyzeGuitarRealTime() {

        TargetData targetData = new TargetData('AXE I/O')
        targetData.start()

        // Wrap TargetDataLine in an AudioInputStream
        AudioInputStream audioStream = new AudioInputStream(targetData.line)

        // Convert Java Sound format to TarsosDSP format
        TarsosDSPAudioFormat tarsosFormat = new TarsosDSPAudioFormat(targetData.format.sampleRate, 16, 1, true, true)

        // Wrap in TarsosDSP input stream
        UniversalAudioInputStream tarsosStream = new UniversalAudioInputStream(audioStream, tarsosFormat)

        LiveDispatcher dispatcher = new LiveDispatcher(tarsosStream)
        dispatcher.run()
    }
}
