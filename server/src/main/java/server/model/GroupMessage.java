package server.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class GroupMessage {
    private String text;
    private long id, pictureID, groupID, sender, repostedFrom;
    private int[] postedTime;
    private int editedTimes;
    private boolean owned;

    public GroupMessage(){}
    public GroupMessage(String text, long id, long groupID, long sender, long repostedFrom, long pictureID){
        this.text = text;
        this.id = id;
        this.groupID = groupID;
        this.sender = sender;
        this.pictureID = pictureID;
        this.repostedFrom = repostedFrom;
        LocalDateTime date = LocalDateTime.now();
        this.postedTime = new int[6];
        this.postedTime[0] = date.getYear();
        this.postedTime[1] = date.getMonthValue();
        this.postedTime[2] = date.getDayOfMonth();
        this.postedTime[3] = date.getHour();
        this.postedTime[4] = date.getMinute();
        this.postedTime[5] = date.getSecond();
        this.editedTimes=0;
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
    public long getGroupID() {
        return groupID;
    }
    public long getSender() {
        return sender;
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
    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }
    public void setSender(long sender) {
        this.sender = sender;
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
    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupMessage that = (GroupMessage) o;
        return id == that.id &&
                groupID == that.groupID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupID);
    }
}
