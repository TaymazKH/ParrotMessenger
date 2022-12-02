package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.util.Command;

@JsonTypeName("GroupsPageRequest")
public class GroupsPageRequest extends Request {
    public GroupsPageRequest(){}
    public GroupsPageRequest(Command command) {
        super(command);
    }
    public GroupsPageRequest(Command command, String[] stringArgs) {
        super(command, stringArgs);
    }
    public GroupsPageRequest(Command command, byte[] imageData) {
        super(command, imageData);
    }
    public GroupsPageRequest(Command command, String[] stringArgs, byte[] imageData) {
        super(command, stringArgs, imageData);
    }

    @Override
    public void run(RequestHandler requestHandler) {
        requestHandler.handleGroupsPageRequest(this);
    }
}
