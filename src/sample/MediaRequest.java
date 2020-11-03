package sample;

public class MediaRequest {

    MediaRequestType requestType;
    String timestamp;

    public MediaRequest (MediaRequestType requestType, String timestamp) {
        this.requestType = requestType;
        this.timestamp = timestamp;
    }

    public MediaRequestType getRequestType () {
        return this.requestType;
    }

    public String getTimestamp () {
        return this.timestamp;
    }

}
