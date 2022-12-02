package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.util.Command;

@JsonTypeName("MessagingPageRequest")
public class MessagingPageRequest extends Request {
    public MessagingPageRequest(){}
    public MessagingPageRequest(Command command) {
        super(command);
    }
    public MessagingPageRequest(Command command, String[] stringArgs) {
        super(command, stringArgs);
    }
    public MessagingPageRequest(Command command, byte[] imageData) {
        super(command, imageData);
    }
    public MessagingPageRequest(Command command, String[] stringArgs, byte[] imageData) {
        super(command, stringArgs, imageData);
    }

    @Override
    public void run(RequestHandler requestHandler) {
        requestHandler.handleMessagingPageRequest(this);
    }
}
