package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.simple_model.SimpleTweet;

@JsonTypeName("TimeLinePageResponse")
public class TimeLinePageResponse implements Response {
    private SimpleTweet simpleTweet;
    private String dialog;

    public TimeLinePageResponse(){}
    public TimeLinePageResponse(SimpleTweet simpleTweet, String dialog) {
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
        responseHandler.handleTimeLinePageResponse(this);
    }
}
