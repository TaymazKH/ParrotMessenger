package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.simple_model.SimpleTweet;

@JsonTypeName("TweetsPageResponse")
public class TweetsPageResponse implements Response {
    private SimpleTweet simpleTweet;
    private String dialog;
    private boolean isThisCurrentUsersPage;

    public TweetsPageResponse(){}
    public TweetsPageResponse(SimpleTweet simpleTweet, String dialog, boolean isThisCurrentUsersPage) {
        this.simpleTweet = simpleTweet;
        this.dialog = dialog;
        this.isThisCurrentUsersPage = isThisCurrentUsersPage;
    }

    public SimpleTweet getSimpleTweet() {
        return simpleTweet;
    }
    public String getDialog() {
        return dialog;
    }
    public boolean isThisCurrentUsersPage() {
        return isThisCurrentUsersPage;
    }

    public void setSimpleTweet(SimpleTweet simpleTweet) {
        this.simpleTweet = simpleTweet;
    }
    public void setDialog(String dialog) {
        this.dialog = dialog;
    }
    public void setThisCurrentUsersPage(boolean thisCurrentUsersPage) {
        isThisCurrentUsersPage = thisCurrentUsersPage;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleTweetsPageResponse(this);
    }
}
