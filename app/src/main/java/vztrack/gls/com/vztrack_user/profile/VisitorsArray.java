package vztrack.gls.com.vztrack_user.profile;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sandeep on 11/4/16.
 */
public class VisitorsArray {
    @SerializedName("message")
    String message;
    @SerializedName("code")
    String code;
    @SerializedName("visitors")
    ArrayList<Visitors> visitors;

    public ArrayList<Visitors> getVisitors() {
        return visitors;
    }

    public void setVisitors(ArrayList<Visitors> visitors) {
        this.visitors = visitors;
    }

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



