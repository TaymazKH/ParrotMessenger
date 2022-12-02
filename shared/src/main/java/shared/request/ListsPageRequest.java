package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.util.Command;

@JsonTypeName("ListsPageRequest")
public class ListsPageRequest extends Request {
    public ListsPageRequest(){}
    public ListsPageRequest(Command command) {
        super(command);
    }
    public ListsPageRequest(Command command, String[] stringArgs) {
        super(command, stringArgs);
    }
    public ListsPageRequest(Command command, byte[] imageData) {
        super(command, imageData);
    }
    public ListsPageRequest(Command command, String[] stringArgs, byte[] imageData) {
        super(command, stringArgs, imageData);
    }

    @Override
    public void run(RequestHandler requestHandler) {
        requestHandler.handleListsPageRequest(this);
    }
}
