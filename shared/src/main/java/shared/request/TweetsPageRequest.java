package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.util.Command;

@JsonTypeName("TweetsPageRequest")
public class TweetsPageRequest extends Request {
    public TweetsPageRequest(){}
    public TweetsPageRequest(Command command) {
        super(command);
    }
    public TweetsPageRequest(Command command, String[] stringArgs) {
        super(command, stringArgs);
    }
    public TweetsPageRequest(Command command, byte[] imageData) {
        super(command, imageData);
    }
    public TweetsPageRequest(Command command, String[] stringArgs, byte[] imageData) {
        super(command, stringArgs, imageData);
    }

    @Override
    public void run(RequestHandler requestHandler) {
        requestHandler.handleTweetsPageRequest(this);
    }
}
