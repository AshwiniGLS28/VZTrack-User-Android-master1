package vztrack.gls.com.vztrack_user.beans.PreApprovalBean;

import com.google.gson.annotations.SerializedName;

public class ApprovalBean {

    @SerializedName("endDate")
    String endDate;
    @SerializedName("familyId")
    int familyId;
    @SerializedName("id")
    int id;
    @SerializedName("loginId")
    int loginId;
    @SerializedName("mobileNo")
    String mobileNo;
    @SerializedName("purposeId")
    int purposeId;
    @SerializedName("purposeName")
    String purposeName;
    @SerializedName("startDate")
    String startDate;
    @SerializedName("visitorName")
    String visitorName;

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getFamilyId() {
        return familyId;
    }

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }
}
