package client.page_data;

import client.util.PageDataType;
import client.util.ViewTexts;
import shared.response.ListsPageResponse;

public class ListsPageData implements PageData {
    private String dialog,listTypeText,nameText;
    private boolean isListSelected, isThereUserInList;
    private byte[] profilePicture;

    public ListsPageData(ListsPageResponse response) {
        isListSelected = response.getListType()!=-1;
        isThereUserInList = response.isThereUserInList();
        dialog = response.getDialog();
        if(response.getListType()!=-1){
            switch(response.getListType()){
                case 0-> listTypeText = ViewTexts.getFollowingsTitle();
                case 1-> listTypeText = ViewTexts.getFollowersTitle();
                case 2-> listTypeText = ViewTexts.getBlackListTitle();
                case 3-> listTypeText = ViewTexts.getMuteListTitle();
            }
            if(response.getNameText()==null) nameText = ViewTexts.getListIsEmpty();
            else{
                profilePicture = response.getProfilePicture();
                nameText = response.getNameText();
            }
        }
        else listTypeText = ViewTexts.getNoListSelected();
    }

    public String getDialog() {
        return dialog;
    }

    public String getListTypeText() {
        return listTypeText;
    }

    public String getNameText() {
        return nameText;
    }

    public boolean isListSelected() {
        return isListSelected;
    }

    public boolean isThereUserInList() {
        return isThereUserInList;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    @Override
    public PageDataType getPageDataType() {
        return PageDataType.lists;
    }
}
