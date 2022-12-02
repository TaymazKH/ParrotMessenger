package shared.response;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "subclassType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ChatListPageResponse.class, name = "ChatListPageResponse"),
        @JsonSubTypes.Type(value = ExploringPageResponse.class, name = "ExploringPageResponse"),
        @JsonSubTypes.Type(value = GroupsPageResponse.class, name = "GroupsPageResponse"),
        @JsonSubTypes.Type(value = ListsPageResponse.class, name = "ListsPageResponse"),
        @JsonSubTypes.Type(value = LoginPageResponse.class, name = "LoginPageResponse"),
        @JsonSubTypes.Type(value = MainMenuPageResponse.class, name = "MainMenuPageResponse"),
        @JsonSubTypes.Type(value = MessagingPageResponse.class, name = "MessagingPageResponse"),
        @JsonSubTypes.Type(value = NotificationsPageResponse.class, name = "NotificationsPageResponse"),
        @JsonSubTypes.Type(value = PersonalPageResponse.class, name = "PersonalPageResponse"),
        @JsonSubTypes.Type(value = RequestsPageResponse.class, name = "RequestsPageResponse"),
        @JsonSubTypes.Type(value = SettingsPageResponse.class, name = "SettingsPageResponse"),
        @JsonSubTypes.Type(value = TimeLinePageResponse.class, name = "TimeLinePageResponse"),
        @JsonSubTypes.Type(value = TweetsPageResponse.class, name = "TweetsPageResponse"),
        @JsonSubTypes.Type(value = OfflineDataBaseResponse.class, name = "OfflineDataBaseResponse")
})
public interface Response {
    void run(ResponseHandler responseHandler);
}
