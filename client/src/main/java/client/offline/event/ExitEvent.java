package client.offline.event;

public class ExitEvent extends Event {
    public ExitEvent() {
        super(EventType.exit);
    }

    @Override
    public void run(EventHandler eventHandler) {
        eventHandler.handleExitEvent();
    }
}
