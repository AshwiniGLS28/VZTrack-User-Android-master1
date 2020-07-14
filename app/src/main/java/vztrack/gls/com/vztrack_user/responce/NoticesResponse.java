package vztrack.gls.com.vztrack_user.responce;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sandeep on 17/3/16.
 */
public class NoticesResponse {
    @SerializedName("message")
    String message;
    @SerializedName("code")
    String code;
    @SerializedName("notices")
    ArrayList<Notices> notices;

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

    public ArrayList<Notices> getNotices() {
        return notices;
    }

    public void setNotices(ArrayList<Notices> notices) {
        this.notices = notices;
    }
}
