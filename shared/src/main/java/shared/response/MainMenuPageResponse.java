package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("MainMenuPageResponse")
public class MainMenuPageResponse implements Response {
    private int authToken;

    public MainMenuPageResponse(){}
    public MainMenuPageResponse(int authToken) {
        this.authToken = authToken;
    }

    public int getAuthToken() {
        return authToken;
    }
    public void setAuthToken(int authToken) {
        this.authToken = authToken;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleMainMenuPageResponse(this);
    }
}
