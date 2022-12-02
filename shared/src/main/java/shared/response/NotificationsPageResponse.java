package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("NotificationsPageResponse")
public class NotificationsPageResponse implements Response {
    private String dialog,username;
    private long[] notification;
    private byte[] profileImage;

    public NotificationsPageResponse(){}
    public NotificationsPageResponse(String dialog, String username, long[] notification, byte[] profileImage) {
        this.dialog = dialog;
        this.username = username;
        this.notification = notification;
        this.profileImage = profileImage;
    }

    public String getDialog() {
        return dialog;
    }
    public String getUsername() {
        return username;
    }
    public long[] getNotification() {
        return notification;
    }
    public byte[] getProfileImage() {
        return profileImage;
    }

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setNotification(long[] notification) {
        this.notification = notification;
    }
    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleNotificationsPageResponse(this);
    }
}
