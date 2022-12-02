package client.page_data;

import client.util.PageDataType;
import client.util.ViewTexts;
import shared.response.MessagingPageResponse;
import shared.simple_model.SimpleMessage;

public class MessagingPageData implements PageData {
    private String dialog,messageText,nameText,postedTime,repostedOrEdited,seen;
    private byte[] postImage,profilePicture;

    public MessagingPageData(MessagingPageResponse response) {
        dialog = response.getDialog();
        SimpleMessage currentMessage = response.getSimpleMessage();
        if(currentMessage!=null){
            messageText = currentMessage.getText();
            nameText = currentMessage.getSender();
            int[] t = currentMessage.getPostedTime();
            postedTime = t[0]+ ViewTexts.getSlash()+t[1]+ViewTexts.getSlash()+t[2]+ViewTexts.getDash()+t[3]+ViewTexts.getColon()+t[4]+ViewTexts.getColon()+t[5];
            if(currentMessage.getRepostedFrom()!=null)
                repostedOrEdited = ViewTexts.getFrom()+currentMessage.getRepostedFrom();
            else if(currentMessage.getEditedTimes()>0)
                repostedOrEdited = ViewTexts.getEdited()+currentMessage.getEditedTimes()+ViewTexts.getTimes();
            postImage = currentMessage.getPostImage();
            profilePicture = currentMessage.getProfilePicture();
            seen = ViewTexts.getSeen().repeat(currentMessage.getSeen());
        }
        else{
            messageText = ViewTexts.getNothingToShow();
        }
    }

    public String getDialog() {
        return dialog;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getNameText() {
        return nameText;
    }

    public String getPostedTime() {
        return postedTime;
    }

    public String getRepostedOrEdited() {
        return repostedOrEdited;
    }

    public String getSeen() {
        return seen;
    }

    public byte[] getPostImage() {
        return postImage;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    @Override
    public PageDataType getPageDataType() {
        return PageDataType.messaging;
    }
}
