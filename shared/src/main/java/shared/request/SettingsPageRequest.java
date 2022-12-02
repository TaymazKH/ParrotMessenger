package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.util.Command;

@JsonTypeName("SettingsPageRequest")
public class SettingsPageRequest extends Request {
    public SettingsPageRequest(){}
    public SettingsPageRequest(Command command) {
        super(command);
    }
    public SettingsPageRequest(Command command, String[] stringArgs) {
        super(command, stringArgs);
    }
    public SettingsPageRequest(Command command, byte[] imageData) {
        super(command, imageData);
    }
    public SettingsPageRequest(Command command, String[] stringArgs, byte[] imageData) {
        super(command, stringArgs, imageData);
    }

    @Override
    public void run(RequestHandler requestHandler) {
        requestHandler.handleSettingsPageRequest(this);
    }
}
