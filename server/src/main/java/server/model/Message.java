package server.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Message {
    private String text;
    private long id,pictureID;
    private int[] postedTime;
    private long sender, receiver, repostedFrom;
    private boolean owned;
    private int editedTimes,seen;

    public Message(){}
    public Message(String text, long sender, long receiver, long id, long repostedFrom, int seen, long pictureID){
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
        this.receiver = receiver;
        this.id = id;
        this.pictureID = pictureID;
        this.repostedFrom = repostedFrom;
        this.owned = true;
        this.editedTimes=0;
        this.seen = seen;
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
    public int[] getPostedTime() {
        return postedTime;
    }
    public long getSender() {
        return sender;
    }
    public long getReceiver() {
        return receiver;
    }
    public long getRepostedFrom() {
        return repostedFrom;
    }
    public boolean isOwned() {
        return owned;
    }
    public int getEditedTimes() {
        return editedTimes;
    }
    public int getSeen() {
        return seen;
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
    public void setPostedTime(int[] postedTime) {
        this.postedTime = postedTime;
    }
    public void setSender(long sender) {
        this.sender = sender;
    }
    public void setReceiver(long receiver) {
        this.receiver = receiver;
    }
    public void setRepostedFrom(long repostedFrom) {
        this.repostedFrom = repostedFrom;
    }
    public void setOwned(boolean owned) {
        this.owned = owned;
    }
    public void setEditedTimes(int editedTimes) {
        this.editedTimes = editedTimes;
    }
    public void setSeen(int seen) {
        this.seen = seen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id == message.id &&
                sender == message.sender &&
                receiver == message.receiver;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sender, receiver);
    }
}
