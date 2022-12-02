package client.util;

import java.util.HashMap;

public class Dialogs {
    private static final Config config = Config.getConfig("dialogs");
    private static final HashMap<String,String> dialogs = new HashMap<>(){
        {
            put("CantShowNext", config.getProperty(String.class, "CantShowNext"));
            put("CantShowPrevious", config.getProperty(String.class, "CantShowPrevious"));
            put("MustProvideName", config.getProperty(String.class, "MustProvideName"));
            put("UserNotFound", config.getProperty(String.class, "UserNotFound"));
            put("AccountNotActive", config.getProperty(String.class, "AccountNotActive"));
            put("AccountNotOwned", config.getProperty(String.class, "AccountNotOwned"));
            put("YouHaveBeenBanned", config.getProperty(String.class, "YouHaveBeenBanned"));
            put("FollowingNeededToMessage", config.getProperty(String.class, "FollowingNeededToMessage"));
            put("SelectedGroup", config.getProperty(String.class, "SelectedGroup"));
            put("GroupNotFound", config.getProperty(String.class, "GroupNotFound"));
            put("RedundantRemove", config.getProperty(String.class, "RedundantRemove"));
            put("DeselectedUser", config.getProperty(String.class, "DeselectedUser"));
            put("DeselectedGroup", config.getProperty(String.class, "DeselectedGroup"));
            put("OverrodeGroup", config.getProperty(String.class, "OverrodeGroup"));
            put("CreatedGroup", config.getProperty(String.class, "CreatedGroup"));
            put("LeftSelectionWithEmptyList", config.getProperty(String.class, "LeftSelectionWithEmptyList"));
            put("MustProvideText", config.getProperty(String.class, "MustProvideText"));
            put("MessageSent", config.getProperty(String.class, "MessageSent"));
            put("NoUpperTweet", config.getProperty(String.class, "NoUpperTweet"));
            put("NoComment", config.getProperty(String.class, "NoComment"));
            put("NotTheOwner", config.getProperty(String.class, "NotTheOwner"));
            put("CantEditForwarded", config.getProperty(String.class, "CantEditForwarded"));
            put("AlreadyReported", config.getProperty(String.class, "AlreadyReported"));
            put("CantUndoReport", config.getProperty(String.class, "CantUndoReport"));
            put("AutomaticallyDisliked", config.getProperty(String.class, "AutomaticallyDisliked"));
            put("RetractedDislike", config.getProperty(String.class, "RetractedDislike"));
            put("AlreadyDisliked", config.getProperty(String.class, "AlreadyDisliked"));
            put("Disliked", config.getProperty(String.class, "Disliked"));
            put("RetractedLike", config.getProperty(String.class, "RetractedLike"));
            put("AlreadyLiked", config.getProperty(String.class, "AlreadyLiked"));
            put("Liked", config.getProperty(String.class,"Liked"));
            put("CantExecuteNothingToShow", config.getProperty(String.class, "CantExecuteNothingToShow"));
            put("AlreadyInvited", config.getProperty(String.class, "AlreadyInvited"));
            put("InvitationSent", config.getProperty(String.class, "InvitationSent"));
            put("TransferOwnershipToLeave", config.getProperty(String.class, "TransferOwnershipToLeave"));
            put("NotInAnyGroup", config.getProperty(String.class, "NotInAnyGroup"));
            put("FillTheFields", config.getProperty(String.class, "FillTheFields"));
            put("NoAccountWithThisUsername", config.getProperty(String.class, "NoAccountWithThisUsername"));
            put("WrongPassword", config.getProperty(String.class, "WrongPassword"));
            put("InvalidUsername", config.getProperty(String.class, "InvalidUsername"));
            put("UsernameTaken", config.getProperty(String.class, "UsernameTaken"));
            put("InvalidEmail", config.getProperty(String.class, "InvalidEmail"));
            put("EmailTaken", config.getProperty(String.class, "EmailTaken"));
            put("PhoneNumberTaken", config.getProperty(String.class, "PhoneNumberTaken"));
            put("AlreadyLoggedIn", config.getProperty(String.class, "AlreadyLoggedIn"));
            put("InvalidDate", config.getProperty(String.class, "InvalidDate"));
            put("AlreadyRequested", config.getProperty(String.class, "AlreadyRequested"));
            put("RequestSent", config.getProperty(String.class, "RequestSent"));
            put("NowFollowing", config.getProperty(String.class, "NowFollowing"));
            put("OneOfYouHasBlockedTheOther", config.getProperty(String.class, "OneOfYouHasBlockedTheOther"));
            put("AlreadyFollowing", config.getProperty(String.class, "AlreadyFollowing"));
            put("Unblocked", config.getProperty(String.class, "Unblocked"));
            put("Blocked", config.getProperty(String.class, "Blocked"));
            put("Unmuted", config.getProperty(String.class, "Unmuted"));
            put("Muted", config.getProperty(String.class, "Muted"));
            put("OfflineDBNotFound", config.getProperty(String.class, "OfflineDBNotFound"));

            put("NoticeTitle", config.getProperty(String.class, "NoticeTitle"));
            put("InputTitle", config.getProperty(String.class, "InputTitle"));
            put("EnterUsername", config.getProperty(String.class, "EnterUsername"));
            put("EnterGroupName", config.getProperty(String.class, "EnterGroupName"));
            put("EnterMessageText", config.getProperty(String.class, "EnterMessageText"));
            put("EnterBio", config.getProperty(String.class, "EnterBio"));
            put("EnterBirthDate", config.getProperty(String.class, "EnterBirthDate"));
            put("EnterSendDate", config.getProperty(String.class, "EnterSendDate"));
            put("EnterEmail", config.getProperty(String.class, "EnterEmail"));
            put("EnterPhoneNumber", config.getProperty(String.class, "EnterPhoneNumber"));
            put("EnterCurrentPassword", config.getProperty(String.class, "EnterCurrentPassword"));
            put("EnterNewPassword", config.getProperty(String.class, "EnterNewPassword"));
        }
    };
    public static String get(String name){
        String text = dialogs.get(name);
        if(text!=null) return text;
        else return "< CONFIG ERROR >";
    }
}
