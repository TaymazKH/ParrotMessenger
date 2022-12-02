package shared.simple_model;

public class SimpleMessage {
    private String text,sender,repostedFrom;
    private byte[] postImage,profilePicture;
    private int[] postedTime;
    private int editedTimes,seen;

    public SimpleMessage(){}
    public SimpleMessage(String text, String sender, String repostedFrom, byte[] postImage, byte[] profilePicture, int[] postedTime, int editedTimes, int seen) {
        this.text = text;
        this.sender = sender;
        this.repostedFrom = repostedFrom;
        this.postImage = postImage;
        this.profilePicture = profilePicture;
        this.postedTime = postedTime;
        this.editedTimes = editedTimes;
        this.seen = seen;
    }

    public String getText() {
        return text;
    }
    public String getSender() {
        return sender;
    }
    public String getRepostedFrom() {
        return repostedFrom;
    }
    public byte[] getPostImage() {
        return postImage;
    }
    public byte[] getProfilePicture() {
        return profilePicture;
    }
    public int[] getPostedTime() {
        return postedTime;
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
    public void setSender(String sender) {
        this.sender = sender;
    }
    public void setRepostedFrom(String repostedFrom) {
        this.repostedFrom = repostedFrom;
    }
    public void setPostImage(byte[] postImage) {
        this.postImage = postImage;
    }
    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }
    public void setPostedTime(int[] postedTime) {
        this.postedTime = postedTime;
    }
    public void setEditedTimes(int editedTimes) {
        this.editedTimes = editedTimes;
    }
    public void setSeen(int seen) {
        this.seen = seen;
    }
}
