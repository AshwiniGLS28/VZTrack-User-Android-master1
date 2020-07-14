package vztrack.gls.com.vztrack_user.responce;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.beans.PollBean;

/**
 * Created by Santosh on 18-Dec-17.
 */

public class PollResponce {
    @SerializedName("message")
    String message;
    @SerializedName("code")
    String code;
    @SerializedName("objPollBeans")
    ArrayList<PollBean>  objPollBeans = new ArrayList<>();

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

    public ArrayList<PollBean> getObjPollBeans() {
        return objPollBeans;
    }

    public void setObjPollBeans(ArrayList<PollBean> objPollBeans) {
        this.objPollBeans = objPollBeans;
    }
}
