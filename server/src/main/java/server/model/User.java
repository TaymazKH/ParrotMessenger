package server.model;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class User {
    private String firstName, lastName, userName, password;
    private long id,pictureID;
    private String birthDate, email, phoneNumber, bio;
    private boolean isPublic, isActive, isOwned;
    private int infoVisibility; // 0: no one / 1: followings / 2: everyone
    private int[] lastSeen;

    private LinkedList<Long> following, followers, blackList, blockedBy, muted, mutedBy, reported, reportedBy;
    private LinkedList<long[]> incomingRequests, outgoingRequests; // userID,type,[groupID] / 1:follow - 2:invite
    private LinkedHashMap<String,LinkedList<Long>> pvGroups;
    private LinkedList<Long> groups;

    private LinkedList<Long> tweets;
    private LinkedHashMap<Long,LinkedList<Long>> messages;
    private LinkedHashMap<Long,Integer> unread;
    private LinkedHashMap<Long,Long> groupSeen;
    private LinkedList<long[]> notifications;
    // 1: they r now following u / 2: they rnt following u anymore / 3: they declined ur follow request / 4: they declined your invitation

    public User(){}
    public User(String firstName, String lastName, String userName, String password, long id, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.id = id;
        this.pictureID = -1;
        this.birthDate = "****-**-**";
        this.email = email;
        this.phoneNumber = "";
        this.bio = "";
        LocalDateTime date = LocalDateTime.now();
        lastSeen = new int[6];
        lastSeen[0] = date.getYear();
        lastSeen[1] = date.getMonthValue();
        lastSeen[2] = date.getDayOfMonth();
        lastSeen[3] = date.getHour();
        lastSeen[4] = date.getMinute();
        lastSeen[5] = date.getSecond();
        this.isPublic = true;
        this.isActive = true;
        this.isOwned = true;
        this.infoVisibility = 1;
        this.following = new LinkedList<>();
        this.followers = new LinkedList<>();
        this.blackList = new LinkedList<>();
        this.blockedBy = new LinkedList<>();
        this.muted = new LinkedList<>();
        this.mutedBy = new LinkedList<>();
        this.reported = new LinkedList<>();
        this.reportedBy = new LinkedList<>();
        this.incomingRequests = new LinkedList<>();
        this.outgoingRequests = new LinkedList<>();
        this.pvGroups = new LinkedHashMap<>();
        this.groups = new LinkedList<>();
        this.tweets = new LinkedList<>();
        this.messages = new LinkedHashMap<>();
        this.unread = new LinkedHashMap<>();
        this.groupSeen = new LinkedHashMap<>();
        this.notifications = new LinkedList<>();
    }
    public User(String userName, long id){
        this.firstName = "-";
        this.lastName = "-";
        this.userName = userName;
        this.password = "";
        this.id = id;
        this.pictureID = -1;
        this.birthDate = "****-**-**";
        this.email = "";
        this.phoneNumber = "";
        this.bio = "";
        LocalDateTime date = LocalDateTime.now();
        lastSeen = new int[6];
        lastSeen[0] = date.getYear();
        lastSeen[1] = date.getMonthValue();
        lastSeen[2] = date.getDayOfMonth();
        lastSeen[3] = date.getHour();
        lastSeen[4] = date.getMinute();
        lastSeen[5] = date.getSecond();
        this.isPublic = true;
        this.isActive = true;
        this.isOwned = true;
        this.infoVisibility = 0;
        this.following = new LinkedList<>();
        this.followers = new LinkedList<>();
        this.blackList = new LinkedList<>();
        this.blockedBy = new LinkedList<>();
        this.muted = new LinkedList<>();
        this.mutedBy = new LinkedList<>();
        this.reported = new LinkedList<>();
        this.reportedBy = new LinkedList<>();
        this.incomingRequests = new LinkedList<>();
        this.outgoingRequests = new LinkedList<>();
        this.pvGroups = new LinkedHashMap<>();
        this.groups = new LinkedList<>();
        this.tweets = new LinkedList<>();
        this.messages = new LinkedHashMap<>();
        this.unread = new LinkedHashMap<>();
        this.groupSeen = new LinkedHashMap<>();
        this.notifications = new LinkedList<>();
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getUserName() {
        return userName;
    }
    public String getPassword() {
        return password;
    }
    public long getId() {
        return id;
    }
    public long getPictureID() {
        return pictureID;
    }
    public String getBirthDate() {
        return birthDate;
    }
    public String getEmail() {
        return email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getBio() {
        return bio;
    }
    public boolean isPublic() {
        return isPublic;
    }
    public boolean isActive() {
        return isActive;
    }
    public boolean isOwned() {
        return isOwned;
    }
    public int getInfoVisibility() {
        return infoVisibility;
    }
    public LinkedList<Long> getFollowing() {
        return following;
    }
    public LinkedList<Long> getFollowers() {
        return followers;
    }
    public LinkedList<Long> getBlackList() {
        return blackList;
    }
    public LinkedList<Long> getBlockedBy() {
        return blockedBy;
    }
    public LinkedList<Long> getMuted() {
        return muted;
    }
    public LinkedList<Long> getMutedBy() {
        return mutedBy;
    }
    public LinkedList<Long> getReported() {
        return reported;
    }
    public LinkedList<Long> getReportedBy() {
        return reportedBy;
    }
    public LinkedList<long[]> getIncomingRequests() {
        return incomingRequests;
    }
    public LinkedList<long[]> getOutgoingRequests() {
        return outgoingRequests;
    }
    public LinkedHashMap<String, LinkedList<Long>> getPvGroups() {
        return pvGroups;
    }
    public LinkedList<Long> getGroups() {
        return groups;
    }
    public int[] getLastSeen() {
        return lastSeen;
    }
    public LinkedList<Long> getTweets() {
        return tweets;
    }
    public LinkedHashMap<Long, LinkedList<Long>> getMessages() {
        return messages;
    }
    public LinkedHashMap<Long, Integer> getUnread() {
        return unread;
    }
    public LinkedHashMap<Long, Long> getGroupSeen() {
        return groupSeen;
    }
    public LinkedList<long[]> getNotifications() {
        return notifications;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setId(long id) {
        this.id = id;
    }
    public void setPictureID(long pictureID) {
        this.pictureID = pictureID;
    }
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }
    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
    public void setActive(boolean active) {
        isActive = active;
    }
    public void setOwned(boolean owned) {
        isOwned = owned;
    }
    public void setInfoVisibility(int infoVisibility) {
        this.infoVisibility = infoVisibility;
    }
    public void setFollowing(LinkedList<Long> following) {
        this.following = following;
    }
    public void setFollowers(LinkedList<Long> followers) {
        this.followers = followers;
    }
    public void setBlackList(LinkedList<Long> blackList) {
        this.blackList = blackList;
    }
    public void setBlockedBy(LinkedList<Long> blockedBy) {
        this.blockedBy = blockedBy;
    }
    public void setMuted(LinkedList<Long> muted) {
        this.muted = muted;
    }
    public void setMutedBy(LinkedList<Long> mutedBy) {
        this.mutedBy = mutedBy;
    }
    public void setReported(LinkedList<Long> reported) {
        this.reported = reported;
    }
    public void setReportedBy(LinkedList<Long> reportedBy) {
        this.reportedBy = reportedBy;
    }
    public void setIncomingRequests(LinkedList<long[]> incomingRequests) {
        this.incomingRequests = incomingRequests;
    }
    public void setOutgoingRequests(LinkedList<long[]> outgoingRequests) {
        this.outgoingRequests = outgoingRequests;
    }
    public void setPvGroups(LinkedHashMap<String, LinkedList<Long>> pvGroups) {
        this.pvGroups = pvGroups;
    }
    public void setGroups(LinkedList<Long> groups) {
        this.groups = groups;
    }
    public void setLastSeen(int[] lastSeen) {
        this.lastSeen = lastSeen;
    }
    public void setTweets(LinkedList<Long> tweets) {
        this.tweets = tweets;
    }
    public void setMessages(LinkedHashMap<Long, LinkedList<Long>> messages) {
        this.messages = messages;
    }
    public void setUnread(LinkedHashMap<Long, Integer> unread) {
        this.unread = unread;
    }
    public void setGroupSeen(LinkedHashMap<Long, Long> groupSeen) {
        this.groupSeen = groupSeen;
    }
    public void setNotifications(LinkedList<long[]> notifications) {
        this.notifications = notifications;
    }

    @Override
    public boolean equals(Object o) {
        if(this==o) return true;
        if(o==null || getClass()!=o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }
}
