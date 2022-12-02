package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("SettingsPageResponse")
public class SettingsPageResponse implements Response {
    private String dialog,usernameText,passwordText;
    private boolean privacy;
    private int infoVisibility;

    public SettingsPageResponse(){}
    public SettingsPageResponse(String dialog, String usernameText, String passwordText, boolean privacy, int infoVisibility) {
        this.dialog = dialog;
        this.usernameText = usernameText;
        this.passwordText = passwordText;
        this.privacy = privacy;
        this.infoVisibility = infoVisibility;
    }

    public String getDialog() {
        return dialog;
    }
    public String getUsernameText() {
        return usernameText;
    }
    public String getPasswordText() {
        return passwordText;
    }
    public boolean isPrivacy() {
        return privacy;
    }
    public int getInfoVisibility() {
        return infoVisibility;
    }

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }
    public void setUsernameText(String usernameText) {
        this.usernameText = usernameText;
    }
    public void setPasswordText(String passwordText) {
        this.passwordText = passwordText;
    }
    public void setPrivacy(boolean privacy) {
        this.privacy = privacy;
    }
    public void setInfoVisibility(int infoVisibility) {
        this.infoVisibility = infoVisibility;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleSettingsPageResponse(this);
    }
}
