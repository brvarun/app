package automate.com.automate.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by varun on 7/30/2016.
 */
public class ServerResponse implements Serializable {

    @SerializedName("message")
    private String message;
    @SerializedName("response_code")
    private int responseCode;

    public ServerResponse(String username, String password, String message, int responseCode){
        this.message = message;
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
