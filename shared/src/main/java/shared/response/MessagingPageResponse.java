package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.simple_model.SimpleMessage;

@JsonTypeName("MessagingPageResponse")
public class MessagingPageResponse implements Response {
    private SimpleMessage simpleMessage;
    private String dialog;

    public MessagingPageResponse(){}
    public MessagingPageResponse(SimpleMessage simpleMessage, String dialog) {
        this.simpleMessage = simpleMessage;
        this.dialog = dialog;
    }

    public SimpleMessage getSimpleMessage() {
        return simpleMessage;
    }
    public String getDialog() {
        return dialog;
    }

    public void setSimpleMessage(SimpleMessage simpleMessage) {
        this.simpleMessage = simpleMessage;
    }
    public void setDialog(String dialog) {
        this.dialog = dialog;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleMessagingPageResponse(this);
    }
}
