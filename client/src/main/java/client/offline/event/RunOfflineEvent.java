package client.offline.event;

public class RunOfflineEvent extends Event {
    public RunOfflineEvent() {
        super(EventType.runOffline);
    }

    @Override
    public void run(EventHandler eventHandler) {
        eventHandler.handleRunOfflineEvent();
    }
}
