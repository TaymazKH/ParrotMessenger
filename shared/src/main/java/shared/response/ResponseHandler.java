package shared.response;

public interface ResponseHandler {
    void handleChatListPageResponse(ChatListPageResponse chatListPageResponse);
    void handleExploringPageResponse(ExploringPageResponse exploringPageResponse);
    void handleGroupsPageResponse(GroupsPageResponse groupsPageResponse);
    void handleListsPageResponse(ListsPageResponse listsPageResponse);
    void handleLoginPageResponse(LoginPageResponse loginPageResponse);
    void handleMainMenuPageResponse(MainMenuPageResponse mainMenuPageResponse);
    void handleMessagingPageResponse(MessagingPageResponse messagingPageResponse);
    void handleNotificationsPageResponse(NotificationsPageResponse notificationsPageResponse);
    void handlePersonalPageResponse(PersonalPageResponse personalPageResponse);
    void handleRequestsPageResponse(RequestsPageResponse requestsPageResponse);
    void handleSettingsPageResponse(SettingsPageResponse settingsPageResponse);
    void handleTimeLinePageResponse(TimeLinePageResponse timeLinePageResponse);
    void handleTweetsPageResponse(TweetsPageResponse tweetsPageResponse);
    void handleOfflineDataBaseResponse(OfflineDataBaseResponse offlineDataBaseResponse);
}
