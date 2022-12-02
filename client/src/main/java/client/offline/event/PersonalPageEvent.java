package client.offline.event;

public class PersonalPageEvent extends Event {
    public PersonalPageEvent(EventType eventType) {
        super(eventType);
    }

    @Override
    public void run(EventHandler eventHandler) {
        eventHandler.handlePersonalPageEvent(this);
    }
}
