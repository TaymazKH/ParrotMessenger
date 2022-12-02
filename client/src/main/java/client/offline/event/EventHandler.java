package client.offline.event;

public interface EventHandler {
    void handleChatListPageEvent(ChatListPageEvent event);
    void handleGroupsPageEvent(GroupsPageEvent event);
    void handleMainMenuPageEvent(MainMenuPageEvent event);
    void handleMessagingPageEvent(MessagingPageEvent event);
    void handlePersonalPageEvent(PersonalPageEvent event);
    void handleTweetsPageEvent(TweetsPageEvent event);
    void handleInitializeConnectionEvent();
    void handleRunOfflineEvent();
    void handleExitEvent();
}
