package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.util.Command;

@JsonTypeName("UpdatePageRequest")
public class UpdatePageRequest extends Request {
    public UpdatePageRequest(){
        super(Command.update);
    }

    @Override
    public void run(RequestHandler requestHandler) {
        requestHandler.handleUpdatePageRequest();
    }
}
