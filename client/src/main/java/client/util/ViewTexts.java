package client.util;

public class ViewTexts {
    private static final Config config = Config.getConfig("viewTexts");
    private static final String
            nothingToShow = config.getProperty(String.class,"nothingToShow"),
            chatListTitle = config.getProperty(String.class,"chatListTitle"),
            unreadOpenParentheses = config.getProperty(String.class,"unreadOpenParentheses"),
            unreadCloseParentheses = config.getProperty(String.class,"unreadCloseParentheses"),
            pvGroupsListTitle = config.getProperty(String.class,"pvGroupsListTitle"),
            managingGroupTitle = config.getProperty(String.class,"managingGroupTitle"),
            selectionModeOpenParentheses = config.getProperty(String.class,"selectionModeOpenParentheses"),
            selectionModeCloseParentheses = config.getProperty(String.class,"selectionModeCloseParentheses"),
            selectionModeTitle = config.getProperty(String.class,"selectionModeTitle"),
            slash = config.getProperty(String.class,"slash"),
            dash = config.getProperty(String.class,"dash"),
            colon = config.getProperty(String.class,"colon"),
            from = config.getProperty(String.class,"from"),
            edited = config.getProperty(String.class,"edited"),
            times = config.getProperty(String.class,"times"),
            seen = config.getProperty(String.class,"seen"),
            followingsTitle = config.getProperty(String.class,"followingsTitle"),
            followersTitle = config.getProperty(String.class,"followersTitle"),
            blackListTitle = config.getProperty(String.class,"blackListTitle"),
            muteListTitle = config.getProperty(String.class,"muteListTitle"),
            listIsEmpty = config.getProperty(String.class,"listIsEmpty"),
            noListSelected = config.getProperty(String.class,"noListSelected"),
            nowFollowingYouNotification = config.getProperty(String.class,"nowFollowingYouNotification"),
            noLongerFollowingYouNotification = config.getProperty(String.class,"noLongerFollowingYouNotification"),
            declinedYourRequestNotification = config.getProperty(String.class,"declinedYourRequestNotification"),
            declinedYourInvitationNotification = config.getProperty(String.class,"declinedYourInvitationNotification"),
            noNewNotification = config.getProperty(String.class,"noNewNotification"),
            online = config.getProperty(String.class,"online"),
            lastSeenAt = config.getProperty(String.class,"lastSeenAt"),
            lastSeenRecently = config.getProperty(String.class,"lastSeenRecently"),
            incomingRequestsTitle = config.getProperty(String.class,"incomingRequestsTitle"),
            outgoingRequestsTitle = config.getProperty(String.class,"outgoingRequestsTitle"),
            wantsToFollow = config.getProperty(String.class,"wantsToFollow"),
            invitedYou = config.getProperty(String.class,"invitedYou"),
            requestedToFollow = config.getProperty(String.class,"requestedToFollow"),
            youHaveInvited = config.getProperty(String.class,"youHaveInvited"),
            toJoin = config.getProperty(String.class,"toJoin"),
            publicTitle = config.getProperty(String.class,"publicTitle"),
            privateTitle = config.getProperty(String.class,"privateTitle"),
            noOneTitle = config.getProperty(String.class,"noOneTitle"),
            followingsOnlyTitle = config.getProperty(String.class,"followingsOnlyTitle"),
            everyOneTitle = config.getProperty(String.class,"everyOneTitle");

    public static Config getConfig() {
        return config;
    }

    public static String getNothingToShow() {
        return nothingToShow;
    }

    public static String getChatListTitle() {
        return chatListTitle;
    }

    public static String getUnreadOpenParentheses() {
        return unreadOpenParentheses;
    }

    public static String getUnreadCloseParentheses() {
        return unreadCloseParentheses;
    }

    public static String getPvGroupsListTitle() {
        return pvGroupsListTitle;
    }

    public static String getManagingGroupTitle() {
        return managingGroupTitle;
    }

    public static String getSelectionModeOpenParentheses() {
        return selectionModeOpenParentheses;
    }

    public static String getSelectionModeCloseParentheses() {
        return selectionModeCloseParentheses;
    }

    public static String getSelectionModeTitle() {
        return selectionModeTitle;
    }

    public static String getSlash() {
        return slash;
    }

    public static String getDash() {
        return dash;
    }

    public static String getColon() {
        return colon;
    }

    public static String getFrom() {
        return from;
    }

    public static String getEdited() {
        return edited;
    }

    public static String getTimes() {
        return times;
    }

    public static String getSeen() {
        return seen;
    }

    public static String getFollowingsTitle() {
        return followingsTitle;
    }

    public static String getFollowersTitle() {
        return followersTitle;
    }

    public static String getBlackListTitle() {
        return blackListTitle;
    }

    public static String getMuteListTitle() {
        return muteListTitle;
    }

    public static String getListIsEmpty() {
        return listIsEmpty;
    }

    public static String getNoListSelected() {
        return noListSelected;
    }

    public static String getNowFollowingYouNotification() {
        return nowFollowingYouNotification;
    }

    public static String getNoLongerFollowingYouNotification() {
        return noLongerFollowingYouNotification;
    }

    public static String getDeclinedYourRequestNotification() {
        return declinedYourRequestNotification;
    }

    public static String getDeclinedYourInvitationNotification() {
        return declinedYourInvitationNotification;
    }

    public static String getNoNewNotification() {
        return noNewNotification;
    }

    public static String getOnline() {
        return online;
    }

    public static String getLastSeenAt() {
        return lastSeenAt;
    }

    public static String getLastSeenRecently() {
        return lastSeenRecently;
    }

    public static String getIncomingRequestsTitle() {
        return incomingRequestsTitle;
    }

    public static String getOutgoingRequestsTitle() {
        return outgoingRequestsTitle;
    }

    public static String getWantsToFollow() {
        return wantsToFollow;
    }

    public static String getInvitedYou() {
        return invitedYou;
    }

    public static String getRequestedToFollow() {
        return requestedToFollow;
    }

    public static String getYouHaveInvited() {
        return youHaveInvited;
    }

    public static String getToJoin() {
        return toJoin;
    }

    public static String getPublicTitle() {
        return publicTitle;
    }

    public static String getPrivateTitle() {
        return privateTitle;
    }

    public static String getNoOneTitle() {
        return noOneTitle;
    }

    public static String getFollowingsOnlyTitle() {
        return followingsOnlyTitle;
    }

    public static String getEveryOneTitle() {
        return everyOneTitle;
    }
}
