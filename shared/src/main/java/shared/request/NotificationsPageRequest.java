package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.util.Command;

@JsonTypeName("NotificationsPageRequest")
public class NotificationsPageRequest extends Request {
    public NotificationsPageRequest(){}
    public NotificationsPageRequest(Command command) {
        super(command);
    }
    public NotificationsPageRequest(Command command, String[] stringArgs) {
        super(command, stringArgs);
    }
    public NotificationsPageRequest(Command command, byte[] imageData) {
        super(command, imageData);
    }
    public NotificationsPageRequest(Command command, String[] stringArgs, byte[] imageData) {
        super(command, stringArgs, imageData);
    }

    @Override
    public void run(RequestHandler requestHandler) {
        requestHandler.handleNotificationsPageRequest(this);
    }
}
