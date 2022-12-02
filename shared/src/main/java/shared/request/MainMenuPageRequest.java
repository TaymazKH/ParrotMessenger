package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.util.Command;

@JsonTypeName("MainMenuPageRequest")
public class MainMenuPageRequest extends Request {
    public MainMenuPageRequest(){}
    public MainMenuPageRequest(Command command) {
        super(command);
    }
    public MainMenuPageRequest(Command command, String[] stringArgs) {
        super(command, stringArgs);
    }
    public MainMenuPageRequest(Command command, byte[] imageData) {
        super(command, imageData);
    }
    public MainMenuPageRequest(Command command, String[] stringArgs, byte[] imageData) {
        super(command, stringArgs, imageData);
    }

    @Override
    public void run(RequestHandler requestHandler) {
        requestHandler.handleMainMenuPageRequest(this);
    }
}
