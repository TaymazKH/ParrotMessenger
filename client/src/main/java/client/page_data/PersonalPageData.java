package client.page_data;

import client.util.PageDataType;
import client.util.ViewTexts;
import shared.response.PersonalPageResponse;

public class PersonalPageData implements PageData {
    private String dialog,username,bio,realName,activity,birthdate,email,phoneNumber;
    private boolean isThisCurrentUsersPage,canViewDetails;
    private byte[] profileImage;

    public PersonalPageData(PersonalPageResponse response) {
        isThisCurrentUsersPage = response.isThisCurrentUsersPage();
        canViewDetails = true;
        dialog = response.getDialog();
        profileImage = response.getProfileImage();
        username = response.getUsername();
        bio = response.getBio();
        realName = response.getFirstName()+" "+response.getLastName();
        if(isThisCurrentUsersPage){
            activity = ViewTexts.getOnline();
            birthdate = response.getBirthdate();
            email = response.getEmail();
            phoneNumber = response.getPhoneNumber();
        }
        else{
            canViewDetails = response.isCanViewDetails();
            if(canViewDetails){
                if(response.isOnline()) activity = ViewTexts.getOnline();
                else{
                    int[] t = response.getLastSeen();
                    activity = ViewTexts.getLastSeenAt() + t[0] + ViewTexts.getSlash() + t[1] + ViewTexts.getSlash() + t[2] + ViewTexts.getDash() + t[3] + ViewTexts.getColon() + t[4] + ViewTexts.getColon() + t[5];
                }
                birthdate = response.getBirthdate();
                email = response.getEmail();
                phoneNumber = response.getPhoneNumber();
            }
            else{
                activity = ViewTexts.getLastSeenRecently();
            }
        }
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

    public String getRealName() {
        return realName;
    }

    public String getActivity() {
        return activity;
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

    public byte[] getProfileImage() {
        return profileImage;
    }

    @Override
    public PageDataType getPageDataType() {
        return PageDataType.personal;
    }
}
