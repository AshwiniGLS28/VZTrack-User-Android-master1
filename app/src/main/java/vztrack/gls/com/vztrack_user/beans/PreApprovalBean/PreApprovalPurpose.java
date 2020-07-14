package vztrack.gls.com.vztrack_user.beans.PreApprovalBean;

import com.google.gson.annotations.SerializedName;

public class PreApprovalPurpose {
    @SerializedName("purposeId")
    int purposeId;
    @SerializedName("purposeName")
    String purposeName;

    public int getPurposeId() {
        return purposeId;
    }

    public void setPurposeId(int purposeId) {
        this.purposeId = purposeId;
    }

    public String getPurposeName() {
        return purposeName;
    }

    public void setPurposeName(String purposeName) {
        this.purposeName = purposeName;
    }
}
