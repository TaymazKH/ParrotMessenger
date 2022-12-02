package shared.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import shared.util.Command;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "subclassType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ChatListPageRequest.class, name = "ChatListPageRequest"),
        @JsonSubTypes.Type(value = ExploringPageRequest.class, name = "ExploringPageRequest"),
        @JsonSubTypes.Type(value = GroupsPageRequest.class, name = "GroupsPageRequest"),
        @JsonSubTypes.Type(value = ListsPageRequest.class, name = "ListsPageRequest"),
        @JsonSubTypes.Type(value = LoginPageRequest.class, name = "LoginPageRequest"),
        @JsonSubTypes.Type(value = MainMenuPageRequest.class, name = "MainMenuPageRequest"),
        @JsonSubTypes.Type(value = MessagingPageRequest.class, name = "MessagingPageRequest"),
        @JsonSubTypes.Type(value = NotificationsPageRequest.class, name = "NotificationsPageRequest"),
        @JsonSubTypes.Type(value = PersonalPageRequest.class, name = "PersonalPageRequest"),
        @JsonSubTypes.Type(value = RequestsPageRequest.class, name = "RequestsPageRequest"),
        @JsonSubTypes.Type(value = SettingsPageRequest.class, name = "SettingsPageRequest"),
        @JsonSubTypes.Type(value = TimeLinePageRequest.class, name = "TimeLinePageRequest"),
        @JsonSubTypes.Type(value = TweetsPageRequest.class, name = "TweetsPageRequest"),
        @JsonSubTypes.Type(value = UpdatePageRequest.class, name = "UpdatePageRequest"),
        @JsonSubTypes.Type(value = GetOfflineDataBaseRequest.class, name = "GetOfflineDataBaseRequest")
})
public abstract class Request {
    private Command command;
    private String[] stringArgs;
    private byte[] imageData;
    private int authToken;

    public Request(){}
    public Request(Command command) {
        this.command = command;
    }
    public Request(Command command, String[] stringArgs) {
        this.command = command;
        this.stringArgs = stringArgs;
    }
    public Request(Command command, byte[] imageData) {
        this.command = command;
        this.imageData = imageData;
    }
    public Request(Command command, String[] stringArgs, byte[] imageData) {
        this.command = command;
        this.stringArgs = stringArgs;
        this.imageData = imageData;
    }

    public Command getCommand() {
        return command;
    }
    public String[] getStringArgs() {
        return stringArgs;
    }
    public byte[] getImageData() {
        return imageData;
    }
    public int getAuthToken() {
        return authToken;
    }

    public void setCommand(Command command) {
        this.command = command;
    }
    public void setStringArgs(String[] stringArgs) {
        this.stringArgs = stringArgs;
    }
    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
    public void setAuthToken(int authToken) {
        this.authToken = authToken;
    }
    
    public abstract void run(RequestHandler requestHandler);
}
