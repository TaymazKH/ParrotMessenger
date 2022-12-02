package server.model;

import java.util.Arrays;
import java.util.Objects;

public class TimedMessage {
    private String text;
    private long groupID,sender,pictureID;
    private int[] sendDate;

    public TimedMessage(){}
    public TimedMessage(String text, long groupID, long sender, long pictureID, int[] sendDate) {
        this.text = text;
        this.groupID = groupID;
        this.sender = sender;
        this.pictureID = pictureID;
        this.sendDate = sendDate;
    }

    public String getText() {
        return text;
    }
    public long getGroupID() {
        return groupID;
    }
    public long getSender() {
        return sender;
    }
    public long getPictureID() {
        return pictureID;
    }
    public int[] getSendDate() {
        return sendDate;
    }

    public void setText(String text) {
        this.text = text;
    }
    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }
    public void setSender(long sender) {
        this.sender = sender;
    }
    public void setPictureID(long pictureID) {
        this.pictureID = pictureID;
    }
    public void setSendDate(int[] sendDate) {
        this.sendDate = sendDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimedMessage that = (TimedMessage) o;
        return groupID == that.groupID &&
                sender == that.sender &&
                pictureID == that.pictureID &&
                text.equals(that.text) &&
                Arrays.equals(sendDate, that.sendDate);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(text, groupID, sender, pictureID);
        result = 31 * result + Arrays.hashCode(sendDate);
        return result;
    }
}
