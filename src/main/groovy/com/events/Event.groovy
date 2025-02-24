package com.events

class Event {

    String timestamp
    String note
    String frequency

    Event(String timestamp = null, String note = null, String frequency = null) {
        this.timestamp = timestamp
        this.note = note
        this.frequency = frequency
    }
}
