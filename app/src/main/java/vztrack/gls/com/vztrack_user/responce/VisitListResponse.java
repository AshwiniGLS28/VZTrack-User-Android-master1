package vztrack.gls.com.vztrack_user.responce;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.profile.Visitors;

/**
 * Created by sandeep on 13/4/16.
 */
public class VisitListResponse {

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
