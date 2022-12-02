package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.simple_model.SimpleUser;

@JsonTypeName("OfflineDataBaseResponse")
public class OfflineDataBaseResponse implements Response {
    private SimpleUser simpleUser;

    public OfflineDataBaseResponse(){}
    public OfflineDataBaseResponse(SimpleUser simpleUser) {
        this.simpleUser = simpleUser;
    }

    public SimpleUser getSimpleUser() {
        return simpleUser;
    }
    public void setSimpleUser(SimpleUser simpleUser) {
        this.simpleUser = simpleUser;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleOfflineDataBaseResponse(this);
    }
}
