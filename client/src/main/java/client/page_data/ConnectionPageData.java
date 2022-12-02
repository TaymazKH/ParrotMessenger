package client.page_data;

import client.util.PageDataType;

public class ConnectionPageData implements PageData {
    private String dialog;

    public ConnectionPageData(){}
    public ConnectionPageData(String dialog) {
        this.dialog = dialog;
    }

    public String getDialog() {
        return dialog;
    }

    @Override
    public PageDataType getPageDataType() {
        return PageDataType.connection;
    }
}
