package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("LoginPageResponse")
public class LoginPageResponse implements Response {
    private String dialog;

    public LoginPageResponse(){}
    public LoginPageResponse(String dialog) {
        this.dialog = dialog;
    }

    public String getDialog() {
        return dialog;
    }

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleLoginPageResponse(this);
    }
}
