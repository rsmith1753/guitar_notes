package com.audio.live

import com.audio.utils.Dispatcher
import be.tarsos.dsp.io.UniversalAudioInputStream
class LiveDispatcher extends Dispatcher {

    LiveDispatcher(UniversalAudioInputStream tarsosStream) {
        super(tarsosStream)
    }

}
