package vztrack.gls.com.vztrack_user.beans.PreApprovalBean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PreApprovalPurposeBean {

    @SerializedName("code")
    String code;
 @SerializedName("message")
    String message;
    @SerializedName("preApprovalPurposeBean")
    ArrayList<PreApprovalPurpose> preApprovalPurposeBean;

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

    public ArrayList<PreApprovalPurpose> getPreApprovalPurposeBean() {
        return preApprovalPurposeBean;
    }

    public void setPreApprovalPurposeBean(ArrayList<PreApprovalPurpose> preApprovalPurposeBean) {
        this.preApprovalPurposeBean = preApprovalPurposeBean;
    }
}
