package client.offline.event;

public abstract class Event {
    private EventType eventType;

    public Event(EventType eventType) {
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }

    public abstract void run(EventHandler eventHandler);
}
