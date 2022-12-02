package client.offline.event;

public class InitializeConnectionEvent extends Event {
    public InitializeConnectionEvent() {
        super(EventType.retryConnection);
    }

    @Override
    public void run(EventHandler eventHandler) {
        eventHandler.handleInitializeConnectionEvent();
    }
}
