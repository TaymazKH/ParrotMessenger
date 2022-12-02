package client.offline.event;

public class GroupsPageEvent extends Event {
    public GroupsPageEvent(EventType eventType) {
        super(eventType);
    }

    @Override
    public void run(EventHandler eventHandler) {
        eventHandler.handleGroupsPageEvent(this);
    }
}
