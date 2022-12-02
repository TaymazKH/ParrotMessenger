package client.offline.event;

public class TweetsPageEvent extends Event {
    public TweetsPageEvent(EventType eventType) {
        super(eventType);
    }

    @Override
    public void run(EventHandler eventHandler) {
        eventHandler.handleTweetsPageEvent(this);
    }
}
