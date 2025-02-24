package com.audio.utils

import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm

class DSPConfig {

    static Double WAV_PEAK_THRESHOLD = 1.0
    //static Double WAV_MIN_INTER_ONSET_INTERVAL = 0.02
    static Double WAV_MIN_INTER_ONSET_INTERVAL = 0
    static Integer WAV_BUFFER_SIZE = 2048
    static Integer WAV_OVERLAP = 512
    static PitchEstimationAlgorithm WAV_PITCH_ALGO = PitchEstimationAlgorithm.YIN

    static Double LIVE_PEAK_THRESHOLD = 0.1
    //static Double LIVE_MIN_INTER_ONSET_INTERVAL = 0.02
    static Double LIVE_MIN_INTER_ONSET_INTERVAL = 0
    static Integer LIVE_BUFFER_SIZE = 4096
    static Integer LIVE_OVERLAP = 512
    static PitchEstimationAlgorithm LIVE_PITCH_ALGO = PitchEstimationAlgorithm.FFT_YIN
    static Integer LIVE_SAMPLE_RATE = 44100
}
