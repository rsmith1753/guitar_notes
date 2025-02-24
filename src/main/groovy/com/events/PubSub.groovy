package com.events

//@Singleton
interface Subscriber {

    Map<String,Stack<Event>> events = [
            'Pluck' : new Stack<Event>(),
            'Note' : new Stack<Event>()
    ]

    void update(String key, Event event)
    Double lastFrequency()
}

@Singleton
class PubSub {

    private Map<String, List<Subscriber>> subscribers = new HashMap<>()

    void subscribe(String key, Subscriber subscriber) {
        subscribers.computeIfAbsent(key, k -> new ArrayList<>()).add(subscriber)
    }

    void unsubscribe(String key, Subscriber subscriber) {
        List<Subscriber> subs = subscribers.get(key)
        if (subs != null) {
            subs.remove(subscriber)
        }
    }

    void publish(String key, Event event) {
        List<Subscriber> subs = subscribers.get(event)
        if (subs != null) {
            for (Subscriber subscriber : subs) {
                subscriber.update(key, event)
            }
        }
    }
}