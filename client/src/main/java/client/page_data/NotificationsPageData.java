package client.page_data;

import client.util.PageDataType;
import client.util.ViewTexts;
import shared.response.NotificationsPageResponse;

public class NotificationsPageData implements PageData {
    private String dialog,dateText,notificationText;
    private byte[] profileImage;

    public NotificationsPageData(NotificationsPageResponse response) {
        dialog = response.getDialog();
        long[] n = response.getNotification();
        if(n!=null){
            dateText = n[2]+ViewTexts.getSlash()+n[3]+ViewTexts.getSlash()+n[4]+ViewTexts.getDash()+n[5]+ViewTexts.getColon()+n[6]+ViewTexts.getColon()+n[7];
            String username = response.getUsername();
            profileImage = response.getProfileImage();
            switch((int) n[1]){
                case 1-> notificationText = username+" "+ViewTexts.getNowFollowingYouNotification();
                case 2-> notificationText = username+" "+ViewTexts.getNoLongerFollowingYouNotification();
                case 3-> notificationText = username+" "+ViewTexts.getDeclinedYourRequestNotification();
                case 4-> notificationText = username+" "+ViewTexts.getDeclinedYourInvitationNotification();
            }
        }
        else{
            notificationText = ViewTexts.getNoNewNotification();
        }
    }

    public String getDialog() {
        return dialog;
    }

    public String getDateText() {
        return dateText;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    @Override
    public PageDataType getPageDataType() {
        return PageDataType.notifications;
    }
}
