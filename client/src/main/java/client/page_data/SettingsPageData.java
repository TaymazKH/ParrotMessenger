package client.page_data;

import client.util.PageDataType;
import client.util.ViewTexts;
import shared.response.SettingsPageResponse;

public class SettingsPageData implements PageData {
    private String dialog,privacyText,infoVisibilityText,usernameText,passwordText;

    public SettingsPageData(SettingsPageResponse response) {
        dialog = response.getDialog();
        if(response.isPrivacy()) privacyText = ViewTexts.getPublicTitle();
        else privacyText = ViewTexts.getPrivateTitle();
        switch(response.getInfoVisibility()){
            case 0-> infoVisibilityText = ViewTexts.getNoOneTitle();
            case 1-> infoVisibilityText = ViewTexts.getFollowingsOnlyTitle();
            case 2-> infoVisibilityText = ViewTexts.getEveryOneTitle();
        }
        usernameText = response.getUsernameText();
        passwordText = response.getPasswordText();
    }

    public String getDialog() {
        return dialog;
    }

    public String getPrivacyText() {
        return privacyText;
    }

    public String getInfoVisibilityText() {
        return infoVisibilityText;
    }

    public String getUsernameText() {
        return usernameText;
    }

    public String getPasswordText() {
        return passwordText;
    }

    @Override
    public PageDataType getPageDataType() {
        return PageDataType.settings;
    }
}
