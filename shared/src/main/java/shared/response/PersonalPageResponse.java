package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("PersonalPageResponse")
public class PersonalPageResponse implements Response {
    private String dialog,username,bio,firstName,lastName,birthdate,email,phoneNumber;
    private boolean isThisCurrentUsersPage,canViewDetails,online;
    private int[] lastSeen;
    private byte[] profileImage;

    public PersonalPageResponse(){}
    public PersonalPageResponse(String dialog, String username, String bio, String firstName, String lastName, String birthdate, String email, String phoneNumber, boolean isThisCurrentUsersPage, boolean canViewDetails, boolean online, int[] lastSeen, byte[] profileImage) {
        this.dialog = dialog;
        this.username = username;
        this.bio = bio;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isThisCurrentUsersPage = isThisCurrentUsersPage;
        this.canViewDetails = canViewDetails;
        this.online = online;
        this.lastSeen = lastSeen;
        this.profileImage = profileImage;
    }

    public String getDialog() {
        return dialog;
    }
    public String getUsername() {
        return username;
    }
    public String getBio() {
        return bio;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getBirthdate() {
        return birthdate;
    }
    public String getEmail() {
        return email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public boolean isThisCurrentUsersPage() {
        return isThisCurrentUsersPage;
    }
    public boolean isCanViewDetails() {
        return canViewDetails;
    }
    public boolean isOnline() {
        return online;
    }
    public int[] getLastSeen() {
        return lastSeen;
    }
    public byte[] getProfileImage() {
        return profileImage;
    }

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setThisCurrentUsersPage(boolean thisCurrentUsersPage) {
        isThisCurrentUsersPage = thisCurrentUsersPage;
    }
    public void setCanViewDetails(boolean canViewDetails) {
        this.canViewDetails = canViewDetails;
    }
    public void setOnline(boolean online) {
        this.online = online;
    }
    public void setLastSeen(int[] lastSeen) {
        this.lastSeen = lastSeen;
    }
    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handlePersonalPageResponse(this);
    }
}
