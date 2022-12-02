package shared.request;

public interface RequestHandler {
    void handleChatListPageRequest(ChatListPageRequest chatListPageRequest);
    void handleExploringPageRequest(ExploringPageRequest exploringPageRequest);
    void handleGroupsPageRequest(GroupsPageRequest groupsPageRequest);
    void handleListsPageRequest(ListsPageRequest listsPageRequest);
    void handleLoginPageRequest(LoginPageRequest loginPageRequest);
    void handleMainMenuPageRequest(MainMenuPageRequest mainMenuPageRequest);
    void handleMessagingPageRequest(MessagingPageRequest messagingPageRequest);
    void handleNotificationsPageRequest(NotificationsPageRequest notificationsPageRequest);
    void handlePersonalPageRequest(PersonalPageRequest personalPageRequest);
    void handleRequestsPageRequest(RequestsPageRequest requestsPageRequest);
    void handleSettingsPageRequest(SettingsPageRequest settingsPageRequest);
    void handleTimeLinePageRequest(TimeLinePageRequest timeLinePageRequest);
    void handleTweetsPageRequest(TweetsPageRequest tweetsPageRequest);
    void handleUpdatePageRequest();
    void handleGetOfflineDataBaseRequest();
}
