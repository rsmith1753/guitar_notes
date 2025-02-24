package com.audio.live

import com.audio.utils.DSPConfig

import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.TargetDataLine

class TargetData {

    TargetDataLine line
    AudioFormat format

    TargetData(String targetInput) {
        try {
            def mixerInfo = AudioSystem.getMixerInfo().find { it.name.contains(targetInput) }
            if(!mixerInfo) {
                println "❌ AXE I/O interface not found. Using default input."
                mixerInfo = AudioSystem.getMixerInfo().find { it.name.contains("USB") || it.name.contains("Built-in") }
            }

            format = new AudioFormat(DSPConfig.LIVE_SAMPLE_RATE, 16, 1, true, true) // 44.1kHz, Mono
            DataLine.Info info = new DataLine.Info(TargetDataLine, format)

            line = AudioSystem.getMixer(mixerInfo).getLine(info) as TargetDataLine
            line.open(format)
            //line.start()
        }
        catch(Exception e) {
            throw new Exception("❌ No supported audio input found!")
        }
    }

    void start() {
        line.start()
    }
}
