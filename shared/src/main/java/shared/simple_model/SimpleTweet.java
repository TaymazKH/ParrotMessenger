package shared.simple_model;

public class SimpleTweet {
    private String text,sender,repostedFrom;
    private byte[] postImage,profilePicture;
    private int[] postedTime;
    private int editedTimes;
    private long likedTimes,dislikedTimes;

    public SimpleTweet(){}
    public SimpleTweet(String text, String sender, String repostedFrom, byte[] postImage, byte[] profilePicture, int[] postedTime, int editedTimes, long likedTimes, long dislikedTimes) {
        this.text = text;
        this.sender = sender;
        this.repostedFrom = repostedFrom;
        this.postImage = postImage;
        this.profilePicture = profilePicture;
        this.postedTime = postedTime;
        this.editedTimes = editedTimes;
        this.likedTimes = likedTimes;
        this.dislikedTimes = dislikedTimes;
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
    public long getLikedTimes() {
        return likedTimes;
    }
    public long getDislikedTimes() {
        return dislikedTimes;
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
    public void setLikedTimes(long likedTimes) {
        this.likedTimes = likedTimes;
    }
    public void setDislikedTimes(long dislikedTimes) {
        this.dislikedTimes = dislikedTimes;
    }
}
