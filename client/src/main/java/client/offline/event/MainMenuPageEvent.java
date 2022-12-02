package client.offline.event;

public class MainMenuPageEvent extends Event {
    public MainMenuPageEvent(EventType eventType) {
        super(eventType);
    }

    @Override
    public void run(EventHandler eventHandler) {
        eventHandler.handleMainMenuPageEvent(this);
    }
}
