package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.simple_model.SimpleGroupMessage;

@JsonTypeName("GroupsPageResponse")
public class GroupsPageResponse implements Response {
    private SimpleGroupMessage simpleGroupMessage;
    private String dialog,groupName,memberCount,memberNameText;
    private int section;
    private boolean isOwner;
    private byte[] memberImage;

    public GroupsPageResponse(){}
    public GroupsPageResponse(SimpleGroupMessage simpleGroupMessage, String dialog, String groupName, String memberCount, String memberNameText, int section, boolean isOwner, byte[] memberImage) {
        this.simpleGroupMessage = simpleGroupMessage;
        this.dialog = dialog;
        this.groupName = groupName;
        this.memberCount = memberCount;
        this.memberNameText = memberNameText;
        this.section = section;
        this.isOwner = isOwner;
        this.memberImage = memberImage;
    }

    public SimpleGroupMessage getSimpleGroupMessage() {
        return simpleGroupMessage;
    }
    public String getDialog() {
        return dialog;
    }
    public String getGroupName() {
        return groupName;
    }
    public String getMemberCount() {
        return memberCount;
    }
    public String getMemberNameText() {
        return memberNameText;
    }
    public int getSection() {
        return section;
    }
    public boolean isOwner() {
        return isOwner;
    }
    public byte[] getMemberImage() {
        return memberImage;
    }

    public void setSimpleGroupMessage(SimpleGroupMessage simpleGroupMessage) {
        this.simpleGroupMessage = simpleGroupMessage;
    }
    public void setDialog(String dialog) {
        this.dialog = dialog;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public void setMemberCount(String memberCount) {
        this.memberCount = memberCount;
    }
    public void setMemberNameText(String memberNameText) {
        this.memberNameText = memberNameText;
    }
    public void setSection(int section) {
        this.section = section;
    }
    public void setOwner(boolean owner) {
        isOwner = owner;
    }
    public void setMemberImage(byte[] memberImage) {
        this.memberImage = memberImage;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleGroupsPageResponse(this);
    }
}
