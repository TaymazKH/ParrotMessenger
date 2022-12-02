package client.page_data;

import client.util.PageDataType;
import shared.response.LoginPageResponse;

public class LoginPageData implements PageData {
    private String dialog;

    public LoginPageData(){}
    public LoginPageData(LoginPageResponse response) {
        dialog = response.getDialog();
    }

    public String getDialog() {
        return dialog;
    }

    @Override
    public PageDataType getPageDataType() {
        return PageDataType.login;
    }
}
