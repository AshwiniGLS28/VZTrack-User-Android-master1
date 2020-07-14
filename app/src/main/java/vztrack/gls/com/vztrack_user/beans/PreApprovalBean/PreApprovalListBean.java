package vztrack.gls.com.vztrack_user.beans.PreApprovalBean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PreApprovalListBean {
    @SerializedName("code")
    String code;
    @SerializedName("message")
    String message;
    @SerializedName("preApprovalList")
    ArrayList<ApprovalBean> preApprovalList;

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

    public ArrayList<ApprovalBean> getPreApprovalList() {
        return preApprovalList;
    }

    public void setPreApprovalList(ArrayList<ApprovalBean> preApprovalList) {
        this.preApprovalList = preApprovalList;
    }
}
