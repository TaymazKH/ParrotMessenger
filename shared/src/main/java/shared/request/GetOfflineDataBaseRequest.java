package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.util.Command;

@JsonTypeName("GetOfflineDataBaseRequest")
public class GetOfflineDataBaseRequest extends Request {
    public GetOfflineDataBaseRequest() {
        super(Command.getOfflineDB);
    }

    @Override
    public void run(RequestHandler requestHandler) {
        requestHandler.handleGetOfflineDataBaseRequest();
    }
}
