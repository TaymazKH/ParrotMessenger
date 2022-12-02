package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.util.Command;

@JsonTypeName("RequestsPageRequest")
public class RequestsPageRequest extends Request {
    public RequestsPageRequest(){}
    public RequestsPageRequest(Command command) {
        super(command);
    }
    public RequestsPageRequest(Command command, String[] stringArgs) {
        super(command, stringArgs);
    }
    public RequestsPageRequest(Command command, byte[] imageData) {
        super(command, imageData);
    }
    public RequestsPageRequest(Command command, String[] stringArgs, byte[] imageData) {
        super(command, stringArgs, imageData);
    }

    @Override
    public void run(RequestHandler requestHandler) {
        requestHandler.handleRequestsPageRequest(this);
    }
}
