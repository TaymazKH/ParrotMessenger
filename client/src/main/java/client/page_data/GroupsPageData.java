package client.page_data;

import client.util.PageDataType;
import client.util.ViewTexts;
import shared.response.GroupsPageResponse;
import shared.simple_model.SimpleGroupMessage;

public class GroupsPageData implements PageData {
    private String dialog,groupName,memberCount,messageText,nameText,postedTime,repostedOrEdited,memberNameText;
    private int section;
    private boolean isOwner;
    private byte[] postImage,profilePicture,memberImage;

    public GroupsPageData(GroupsPageResponse response) {
        section = response.getSection();
        dialog = response.getDialog();
        switch(section){
            case 0->{
                if(response.getGroupName()!=null){
                    groupName = response.getGroupName();
                    memberCount = response.getMemberCount();
                }
                else{
                    groupName = ViewTexts.getNothingToShow();
                }
            }
            case 1->{
                SimpleGroupMessage message = response.getSimpleGroupMessage();
                if(message!=null){
                    messageText = message.getText();
                    nameText = message.getSender();
                    int[] t = message.getPostedTime();
                    postedTime = t[0]+ ViewTexts.getSlash()+t[1]+ViewTexts.getSlash()+t[2]+ViewTexts.getDash()+t[3]+ViewTexts.getColon()+t[4]+ViewTexts.getColon()+t[5];
                    if(message.getRepostedFrom()!=null)
                        repostedOrEdited = ViewTexts.getFrom()+message.getRepostedFrom();
                    else if(message.getEditedTimes()>0)
                        repostedOrEdited = ViewTexts.getEdited()+message.getEditedTimes()+ViewTexts.getTimes();
                    postImage = message.getPostImage();
                    profilePicture = message.getProfilePicture();
                }
                else{
                    messageText = ViewTexts.getNothingToShow();
                }
            }
            case 2->{
                memberNameText = response.getMemberNameText();
                memberImage = response.getMemberImage();
                isOwner = response.isOwner();
            }
        }
    }

    public String getDialog() {
        return dialog;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getMemberCount() {
        return memberCount;
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

    public String getMemberNameText() {
        return memberNameText;
    }

    public int getSection() {
        return section;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public byte[] getPostImage() {
        return postImage;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public byte[] getMemberImage() {
        return memberImage;
    }

    @Override
    public PageDataType getPageDataType() {
        return PageDataType.groups;
    }
}
