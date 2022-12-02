package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.util.Command;

@JsonTypeName("PersonalPageRequest")
public class PersonalPageRequest extends Request {
    public PersonalPageRequest(){}
    public PersonalPageRequest(Command command) {
        super(command);
    }
    public PersonalPageRequest(Command command, String[] stringArgs) {
        super(command, stringArgs);
    }
    public PersonalPageRequest(Command command, byte[] imageData) {
        super(command, imageData);
    }
    public PersonalPageRequest(Command command, String[] stringArgs, byte[] imageData) {
        super(command, stringArgs, imageData);
    }

    @Override
    public void run(RequestHandler requestHandler) {
        requestHandler.handlePersonalPageRequest(this);
    }
}
