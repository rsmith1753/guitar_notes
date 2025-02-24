package com

import com.audio.live.RealTimeAnalysis
import com.events.NoteSubscriber
import com.events.PubSub

static void main(String[] args) {
    println "Hello world!"
    start()
}

static void start() {
    PubSub pubSub = PubSub.instance
    NoteSubscriber noteSubscriber = NoteSubscriber.instance
    pubSub.subscribe('Live Pluck', noteSubscriber)
    RealTimeAnalysis realTimeAnalysis = new RealTimeAnalysis()
    realTimeAnalysis.analyzeGuitarRealTime()
}