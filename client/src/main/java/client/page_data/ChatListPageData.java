package client.page_data;

import client.util.PageDataType;
import client.util.ViewTexts;
import shared.response.ChatListPageResponse;

public class ChatListPageData implements PageData {
    private String dialog, sectionTitle, nameText;
    private int section;
    private byte[] image;

    public ChatListPageData(ChatListPageResponse response) {
        dialog = response.getDialog();
        section = response.getSection();
        switch(section){
            case 0->{
                sectionTitle = ViewTexts.getChatListTitle();
                if(response.getNameText()!=null){
                    int unreadCount = response.getCount();
                    if(unreadCount>0) nameText = response.getNameText()+ViewTexts.getUnreadOpenParentheses()+unreadCount+ViewTexts.getUnreadCloseParentheses();
                    else nameText = response.getNameText();
                    image = response.getImage();
                }
                else{
                    nameText = ViewTexts.getNothingToShow();
                }
            }
            case 1->{
                sectionTitle = ViewTexts.getPvGroupsListTitle();
                if(response.getNameText()!=null) nameText = response.getNameText();
                else nameText = ViewTexts.getNothingToShow();
            }
            case 2->{
                sectionTitle = ViewTexts.getManagingGroupTitle()+response.getSectionTitle();
                if(response.getNameText()!=null){
                    nameText = response.getNameText();
                    image = response.getImage();
                }
                else{
                    nameText = ViewTexts.getNothingToShow();
                }
            }
            case 3->{
                int listSize = response.getCount();
                if(listSize>0) sectionTitle = ViewTexts.getSelectionModeOpenParentheses()+listSize+ViewTexts.getSelectionModeCloseParentheses();
                else sectionTitle = ViewTexts.getSelectionModeTitle();
                if(response.getNameText()!=null){
                    nameText = response.getNameText();
                    image = response.getImage();
                }
                else{
                    nameText = ViewTexts.getNothingToShow();
                }
            }
        }
    }

    public String getDialog() {
        return dialog;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public String getNameText() {
        return nameText;
    }

    public int getSection() {
        return section;
    }

    public byte[] getImage() {
        return image;
    }

    @Override
    public PageDataType getPageDataType() {
        return PageDataType.chatList;
    }
}
