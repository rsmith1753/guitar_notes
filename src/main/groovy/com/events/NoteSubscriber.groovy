package com.events

@Singleton
class NoteSubscriber implements Subscriber {

    void update(String key, Event event) {
        events[key].push(event)
        //if(events.Note.size() > 0) {
        //    if(Math.abs(events.Note.peek().frequency.toDouble() - event.frequency.toDouble()) > 0.2) {
        //        println('Bending')
        //    }
        //}
        //System.out.println("Note added: " + event.note);
    }

    Double lastFrequency() {
        if(events.Note.size() > 0) {
            return events.Note.peek().frequency.toDouble()
        }
        return -1.0
    }
}