package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("RequestsPageResponse")
public class RequestsPageResponse implements Response {
    private String dialog,username,groupName;
    private int listType;
    private boolean isThereItemInList;
    private long[] itemInList;
    private byte[] profileImage;

    public RequestsPageResponse(){}
    public RequestsPageResponse(String dialog, String username, String groupName, int listType, boolean isThereItemInList, long[] itemInList, byte[] profileImage) {
        this.dialog = dialog;
        this.username = username;
        this.groupName = groupName;
        this.listType = listType;
        this.isThereItemInList = isThereItemInList;
        this.itemInList = itemInList;
        this.profileImage = profileImage;
    }

    public String getDialog() {
        return dialog;
    }
    public String getUsername() {
        return username;
    }
    public String getGroupName() {
        return groupName;
    }
    public int getListType() {
        return listType;
    }
    public boolean isThereItemInList() {
        return isThereItemInList;
    }
    public long[] getItemInList() {
        return itemInList;
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
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public void setListType(int listType) {
        this.listType = listType;
    }
    public void setThereItemInList(boolean thereItemInList) {
        isThereItemInList = thereItemInList;
    }
    public void setItemInList(long[] itemInList) {
        this.itemInList = itemInList;
    }
    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleRequestsPageResponse(this);
    }
}
