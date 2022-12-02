package client.page_data;

import client.util.PageDataType;
import client.util.ViewTexts;
import shared.response.TweetsPageResponse;
import shared.simple_model.SimpleTweet;

public class TweetsPageData implements PageData {
    private String dialog,tweetText,nameText,postedTime,repostedOrEdited,likeCount,dislikeCount;
    private boolean isThisCurrentUsersPage;
    private byte[] postImage,profilePicture;

    public TweetsPageData(TweetsPageResponse response) {
        dialog = response.getDialog();
        isThisCurrentUsersPage = response.isThisCurrentUsersPage();
        SimpleTweet currentTweet = response.getSimpleTweet();
        if(currentTweet!=null){
            tweetText = currentTweet.getText();
            nameText = currentTweet.getSender();
            int[] t = currentTweet.getPostedTime();
            postedTime = t[0]+ ViewTexts.getSlash()+t[1]+ViewTexts.getSlash()+t[2]+ViewTexts.getDash()+t[3]+ViewTexts.getColon()+t[4]+ViewTexts.getColon()+t[5];
            if(currentTweet.getRepostedFrom()!=null)
                repostedOrEdited = ViewTexts.getFrom()+currentTweet.getRepostedFrom();
            else if(currentTweet.getEditedTimes()>0)
                repostedOrEdited = ViewTexts.getEdited()+currentTweet.getEditedTimes()+ViewTexts.getTimes();
            likeCount = String.valueOf(currentTweet.getLikedTimes());
            dislikeCount = String.valueOf(currentTweet.getDislikedTimes());
            postImage = currentTweet.getPostImage();
            profilePicture = currentTweet.getProfilePicture();
        }
        else{
            tweetText = ViewTexts.getNothingToShow();
        }
    }

    public String getDialog() {
        return dialog;
    }

    public String getTweetText() {
        return tweetText;
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

    public String getLikeCount() {
        return likeCount;
    }

    public String getDislikeCount() {
        return dislikeCount;
    }

    public boolean isThisCurrentUsersPage() {
        return isThisCurrentUsersPage;
    }

    public byte[] getPostImage() {
        return postImage;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    @Override
    public PageDataType getPageDataType() {
        return PageDataType.tweets;
    }
}
