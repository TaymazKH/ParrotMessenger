package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.util.Command;

@JsonTypeName("ExploringPageRequest")
public class ExploringPageRequest extends Request {
    public ExploringPageRequest(){}
    public ExploringPageRequest(Command command) {
        super(command);
    }
    public ExploringPageRequest(Command command, String[] stringArgs) {
        super(command, stringArgs);
    }
    public ExploringPageRequest(Command command, byte[] imageData) {
        super(command, imageData);
    }
    public ExploringPageRequest(Command command, String[] stringArgs, byte[] imageData) {
        super(command, stringArgs, imageData);
    }

    @Override
    public void run(RequestHandler requestHandler) {
        requestHandler.handleExploringPageRequest(this);
    }
}
