package client.offline.event;

public class MessagingPageEvent extends Event {
    public MessagingPageEvent(EventType eventType) {
        super(eventType);
    }

    @Override
    public void run(EventHandler eventHandler) {
        eventHandler.handleMessagingPageEvent(this);
    }
}
