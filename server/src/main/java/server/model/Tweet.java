package server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Objects;

public class Tweet {
    private String text;
    private long id,pictureID;
    private long sender,mainSender,repostedFrom;
    private int[] postedTime;
    private int editedTimes;
    @JsonIgnore
    private Tweet upperTweet;
    private long upperTweetID;
    private boolean isPrimaryTweet;
    private LinkedList<Long> comments;
    private LinkedList<Long> likedUsers, dislikedUsers, reportedUsers;
    private long likedTimes, dislikedTimes, votedTimes;
    private boolean viewable,owned;

    public Tweet(){}
    public Tweet(String text, long sender, long mainSender, long repostedFrom, long id, Tweet upperTweet, long pictureID){
        LocalDateTime date = LocalDateTime.now();
        this.postedTime = new int[6];
        this.postedTime[0] = date.getYear();
        this.postedTime[1] = date.getMonthValue();
        this.postedTime[2] = date.getDayOfMonth();
        this.postedTime[3] = date.getHour();
        this.postedTime[4] = date.getMinute();
        this.postedTime[5] = date.getSecond();
        this.text = text;
        this.sender = sender;
        this.mainSender = mainSender;
        this.repostedFrom = repostedFrom;
        this.id = id;
        this.pictureID = pictureID;
        this.editedTimes=0;
        this.upperTweet = upperTweet;
        this.isPrimaryTweet = upperTweet==null;
        if(!isPrimaryTweet())
            this.upperTweetID = upperTweet.getId();
        else this.upperTweetID = -1;
        this.comments = new LinkedList<>();
        this.likedUsers = new LinkedList<>();
        this.dislikedUsers = new LinkedList<>();
        this.reportedUsers = new LinkedList<>();
        this.likedTimes=0;
        this.dislikedTimes=0;
        this.votedTimes=0;
        this.viewable = true;
        this.owned = true;
    }

    public String getText() {
        return text;
    }
    public long getId() {
        return id;
    }
    public long getPictureID() {
        return pictureID;
    }
    public long getSender() {
        return sender;
    }
    public long getMainSender() {
        return mainSender;
    }
    public long getRepostedFrom() {
        return repostedFrom;
    }
    public int[] getPostedTime() {
        return postedTime;
    }
    public int getEditedTimes() {
        return editedTimes;
    }
    public Tweet getUpperTweet() {
        return upperTweet;
    }
    public long getUpperTweetID() {
        return upperTweetID;
    }
    public boolean isPrimaryTweet() {
        return isPrimaryTweet;
    }
    public LinkedList<Long> getComments() {
        return comments;
    }
    public LinkedList<Long> getLikedUsers() {
        return likedUsers;
    }
    public LinkedList<Long> getDislikedUsers() {
        return dislikedUsers;
    }
    public LinkedList<Long> getReportedUsers() {
        return reportedUsers;
    }
    public long getLikedTimes() {
        return likedTimes;
    }
    public long getDislikedTimes() {
        return dislikedTimes;
    }
    public long getVotedTimes() {
        return votedTimes;
    }
    public boolean isViewable() {
        return viewable;
    }
    public boolean isOwned() {
        return owned;
    }

    public void setText(String text) {
        this.text = text;
    }
    public void setId(long id) {
        this.id = id;
    }
    public void setPictureID(long pictureID) {
        this.pictureID = pictureID;
    }
    public void setSender(long sender) {
        this.sender = sender;
    }
    public void setMainSender(long mainSender) {
        this.mainSender = mainSender;
    }
    public void setRepostedFrom(long repostedFrom) {
        this.repostedFrom = repostedFrom;
    }
    public void setPostedTime(int[] postedTime) {
        this.postedTime = postedTime;
    }
    public void setEditedTimes(int editedTimes) {
        this.editedTimes = editedTimes;
    }
    public void setUpperTweet(Tweet upperTweet) {
        this.upperTweet = upperTweet;
    }
    public void setUpperTweetID(long upperTweetID) {
        this.upperTweetID = upperTweetID;
    }
    public void setPrimaryTweet(boolean primaryTweet) {
        isPrimaryTweet = primaryTweet;
    }
    public void setComments(LinkedList<Long> comments) {
        this.comments = comments;
    }
    public void setLikedUsers(LinkedList<Long> likedUsers) {
        this.likedUsers = likedUsers;
    }
    public void setDislikedUsers(LinkedList<Long> dislikedUsers) {
        this.dislikedUsers = dislikedUsers;
    }
    public void setReportedUsers(LinkedList<Long> reportedUsers) {
        this.reportedUsers = reportedUsers;
    }
    public void setLikedTimes(long likedTimes) {
        this.likedTimes = likedTimes;
    }
    public void setDislikedTimes(long dislikedTimes) {
        this.dislikedTimes = dislikedTimes;
    }
    public void setVotedTimes(long votedTimes) {
        this.votedTimes = votedTimes;
    }
    public void setViewable(boolean viewable) {
        this.viewable = viewable;
    }
    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tweet tweet = (Tweet) o;
        return id == tweet.id && mainSender == tweet.mainSender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mainSender);
    }
}
