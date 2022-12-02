package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.simple_model.SimpleTweet;

@JsonTypeName("ExploringPageResponse")
public class ExploringPageResponse implements Response {
    private SimpleTweet simpleTweet;
    private String dialog;

    public ExploringPageResponse(){}
    public ExploringPageResponse(SimpleTweet simpleTweet, String dialog) {
        this.simpleTweet = simpleTweet;
        this.dialog = dialog;
    }

    public SimpleTweet getSimpleTweet() {
        return simpleTweet;
    }
    public String getDialog() {
        return dialog;
    }

    public void setSimpleTweet(SimpleTweet simpleTweet) {
        this.simpleTweet = simpleTweet;
    }
    public void setDialog(String dialog) {
        this.dialog = dialog;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleExploringPageResponse(this);
    }
}
