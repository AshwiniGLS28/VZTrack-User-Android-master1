package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 20/12/16.
 */

public class ResponceBean {
    @SerializedName("message")
    String message;
    @SerializedName("code")
    String code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
