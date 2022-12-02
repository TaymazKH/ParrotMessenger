package client.offline.event;

public class ChatListPageEvent extends Event {
    private String targetUsername;

    public ChatListPageEvent(EventType eventType) {
        super(eventType);
    }
    public ChatListPageEvent(EventType eventType, String targetUsername) {
        super(eventType);
        this.targetUsername = targetUsername;
    }

    public String getTargetUsername() {
        return targetUsername;
    }

    @Override
    public void run(EventHandler eventHandler) {
        eventHandler.handleChatListPageEvent(this);
    }
}
