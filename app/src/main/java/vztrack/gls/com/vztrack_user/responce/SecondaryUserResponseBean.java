package vztrack.gls.com.vztrack_user.responce;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.beans.SecondaryUserBean;

public class SecondaryUserResponseBean {
    @SerializedName("code")
    String code;
    @SerializedName("message")
    String message;
    @SerializedName("secondaoryUserBeanList")
    ArrayList<SecondaryUserBean> secondaoryUserBeanList = new ArrayList<SecondaryUserBean>();
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<SecondaryUserBean> getSecondaoryUserBeanList() {
        return secondaoryUserBeanList;
    }

    public void setSecondaoryUserBeanList(ArrayList<SecondaryUserBean> secondaoryUserBeanList) {
        this.secondaoryUserBeanList = secondaoryUserBeanList;
    }
}
