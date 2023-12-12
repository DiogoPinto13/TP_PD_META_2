package isec.pd.meta2.Shared;

import java.io.Serializable;

public class Request implements Serializable {
    Messages typeMessage;
    String Message;

    public Request(Messages typeMessage, String message) {
        this.typeMessage = typeMessage;
        Message = message;
    }

    public Messages getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(Messages typeMessage) {
        this.typeMessage = typeMessage;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
