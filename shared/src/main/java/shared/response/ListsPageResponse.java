package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("ListsPageResponse")
public class ListsPageResponse implements Response {
    private String dialog,nameText;
    private int listType;
    private boolean isThereUserInList;
    private byte[] profilePicture;

    public ListsPageResponse(){}
    public ListsPageResponse(String dialog, String nameText, int listType, boolean isThereUserInList, byte[] profilePicture) {
        this.dialog = dialog;
        this.nameText = nameText;
        this.listType = listType;
        this.isThereUserInList = isThereUserInList;
        this.profilePicture = profilePicture;
    }

    public String getDialog() {
        return dialog;
    }
    public String getNameText() {
        return nameText;
    }
    public int getListType() {
        return listType;
    }
    public boolean isThereUserInList() {
        return isThereUserInList;
    }
    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }
    public void setNameText(String nameText) {
        this.nameText = nameText;
    }
    public void setListType(int listType) {
        this.listType = listType;
    }
    public void setThereUserInList(boolean thereUserInList) {
        isThereUserInList = thereUserInList;
    }
    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleListsPageResponse(this);
    }
}
