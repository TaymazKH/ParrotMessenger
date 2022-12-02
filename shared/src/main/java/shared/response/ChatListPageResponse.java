package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("ChatListPageResponse")
public class ChatListPageResponse implements Response {
    private String dialog, sectionTitle, nameText;
    private int section, count;
    private byte[] image;

    public ChatListPageResponse(){}
    public ChatListPageResponse(String dialog, String sectionTitle, String nameText, int section, int count, byte[] image) {
        this.dialog = dialog;
        this.sectionTitle = sectionTitle;
        this.nameText = nameText;
        this.section = section;
        this.count = count;
        this.image = image;
    }

    public String getDialog() {
        return dialog;
    }
    public String getSectionTitle() {
        return sectionTitle;
    }
    public String getNameText() {
        return nameText;
    }
    public int getSection() {
        return section;
    }
    public int getCount() {
        return count;
    }
    public byte[] getImage() {
        return image;
    }

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }
    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }
    public void setNameText(String nameText) {
        this.nameText = nameText;
    }
    public void setSection(int section) {
        this.section = section;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleChatListPageResponse(this);
    }
}
