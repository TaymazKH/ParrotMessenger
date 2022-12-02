package client.offline.event;

public enum EventType {
    // general
    home,back,

    // connection
    retryConnection,runOffline,exit,

    // main menu
    gotoPersonalPage,gotoChatListPage,gotoGroupsPage,

    // tweets & messages
    showNext,showPrevious,

    // personal
    gotoTweetsPage,

    // chat lists
    showSavesMessages,messageUser,

    // groups
    enterGroup
}
