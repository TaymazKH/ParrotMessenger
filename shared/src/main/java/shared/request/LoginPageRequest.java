package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.util.Command;

@JsonTypeName("LoginPageRequest")
public class LoginPageRequest extends Request {
    public LoginPageRequest(){}
    public LoginPageRequest(Command command) {
        super(command);
    }
    public LoginPageRequest(Command command, String[] stringArgs) {
        super(command, stringArgs);
    }
    public LoginPageRequest(Command command, byte[] imageData) {
        super(command, imageData);
    }
    public LoginPageRequest(Command command, String[] stringArgs, byte[] imageData) {
        super(command, stringArgs, imageData);
    }

    @Override
    public void run(RequestHandler requestHandler) {
        requestHandler.handleLoginPageRequest(this);
    }
}
