package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.util.Command;

@JsonTypeName("TimeLinePageRequest")
public class TimeLinePageRequest extends Request {
    public TimeLinePageRequest(){}
    public TimeLinePageRequest(Command command) {
        super(command);
    }
    public TimeLinePageRequest(Command command, String[] stringArgs) {
        super(command, stringArgs);
    }
    public TimeLinePageRequest(Command command, byte[] imageData) {
        super(command, imageData);
    }
    public TimeLinePageRequest(Command command, String[] stringArgs, byte[] imageData) {
        super(command, stringArgs, imageData);
    }

    @Override
    public void run(RequestHandler requestHandler) {
        requestHandler.handleTimeLinePageRequest(this);
    }
}
