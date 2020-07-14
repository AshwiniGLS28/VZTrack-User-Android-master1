package vztrack.gls.com.vztrack_user.responce;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 17/3/16.
 */
public class CommonResponse {
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
