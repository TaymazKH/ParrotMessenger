package shared.simple_model;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class SimpleUser {
    private String firstName, lastName, userName;
    private long id;
    private byte[] picture;
    private String birthDate, email, phoneNumber, bio;
    private LinkedList<SimpleTweet> tweets;
    private LinkedHashMap<String,LinkedList<SimpleGroupMessage>> groupMessages;
    private LinkedHashMap<String,LinkedList<SimpleMessage>> messages;

    public SimpleUser(){}
    public SimpleUser(String firstName, String lastName, String userName, long id, byte[] picture, String birthDate, String email, String phoneNumber, String bio, LinkedList<SimpleTweet> tweets, LinkedHashMap<String, LinkedList<SimpleGroupMessage>> groupMessages, LinkedHashMap<String, LinkedList<SimpleMessage>> messages) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.id = id;
        this.picture = picture;
        this.birthDate = birthDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.bio = bio;
        this.tweets = tweets;
        this.groupMessages = groupMessages;
        this.messages = messages;
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
    public long getId() {
        return id;
    }
    public byte[] getPicture() {
        return picture;
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
    public LinkedList<SimpleTweet> getTweets() {
        return tweets;
    }
    public LinkedHashMap<String, LinkedList<SimpleGroupMessage>> getGroupMessages() {
        return groupMessages;
    }
    public LinkedHashMap<String, LinkedList<SimpleMessage>> getMessages() {
        return messages;
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
    public void setId(long id) {
        this.id = id;
    }
    public void setPicture(byte[] picture) {
        this.picture = picture;
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
    public void setTweets(LinkedList<SimpleTweet> tweets) {
        this.tweets = tweets;
    }
    public void setGroupMessages(LinkedHashMap<String, LinkedList<SimpleGroupMessage>> groupMessages) {
        this.groupMessages = groupMessages;
    }
    public void setMessages(LinkedHashMap<String, LinkedList<SimpleMessage>> messages) {
        this.messages = messages;
    }
}
