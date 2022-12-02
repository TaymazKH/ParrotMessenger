package shared.util;

public enum Command {
    // general
    home,back,update,getOfflineDB,

    // login
    login,signup,

    // main menu
    gotoPersonalPage,gotoTimeLinePage,gotoExploringPage,gotoChatListPage,gotoGroupsPage,gotoSettingsPage,exit,

    // exploring & timeline & tweets
    refresh,search,newTweet,repost,save,forwardToUser,showNext,showPrevious,showUpperTweet,showComments,addComment,showSender,edit,delete,like,dislike,report,

    // settings
    togglePrivacy,toggleInfoVisibility,deactivateAccount,changePassword,changeUsername,deleteAccount,logOut,

    // personal
    changeProfilePicture,editBio,editPhoneNumber,editEmail,editBirthDate,hideBirthDate,gotoTweetsPage,gotoListsPage,gotoRequestsPage,gotoNotificationsPage, follow,toggleBan,toggleMute,

    // personal lists
    showFollowings,showFollowers,showBlacklist,showMuteList,showTheirPage,remove,

    // personal requests
    showIncomingRequests,showOutgoingRequests,accept,decline,silentDecline,cancelRequest,

    // personal notifications
    showNextNotification,

    // chat lists
    showSavesMessages,messageUser,showPVGroups,selectionMode,
    enterPVGroupManagement,addUser,removeThisUser,
    selectUser,selectGroup,deselectUser,deselectGroup,stopSelectionAndCreateGroup,stopSelectionAndSendMessage,

    // groups
    newGroup,enterGroup,showMembers,newMessage,newTimedMessage,leave,transferOwnership,invite,kick
}
