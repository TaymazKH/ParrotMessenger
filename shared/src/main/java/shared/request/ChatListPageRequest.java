package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.util.Command;

@JsonTypeName("ChatListPageRequest")
public class ChatListPageRequest extends Request {
    public ChatListPageRequest(){}
    public ChatListPageRequest(Command command) {
        super(command);
    }
    public ChatListPageRequest(Command command, String[] stringArgs) {
        super(command, stringArgs);
    }
    public ChatListPageRequest(Command command, byte[] imageData) {
        super(command, imageData);
    }
    public ChatListPageRequest(Command command, String[] stringArgs, byte[] imageData) {
        super(command, stringArgs, imageData);
    }

    @Override
    public void run(RequestHandler requestHandler) {
        requestHandler.handleChatListPageRequest(this);
    }
}
