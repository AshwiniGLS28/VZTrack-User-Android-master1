package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 14/3/16.
 */
public class DataObjectVisitors {
    @SerializedName("mVisitor_id")
    private String mVisitor_id;
    @SerializedName("mFirst_Name")
    private String mFirst_Name;
    @SerializedName("mLast_Name")
    private String mLast_Name;
    @SerializedName("mMobile")
    private String mMobile;
    @SerializedName("mPhoto")
    private String mPhoto;
    @SerializedName("mTime")
    private String mTime;
    @SerializedName("mPurpose")
    private String mPurpose;
    @SerializedName("mFrom")
    private String mFrom;
    @SerializedName("mTomeet")
    private String mTomeet;
    @SerializedName("mDocUrl")
    private String mDocUrl;
    @SerializedName("mError_flag")
    private boolean mError_flag;
    public DataObjectVisitors()
    {}
    @SerializedName("askForApproval")
    boolean askForApproval;
    @SerializedName("approvalStatus")
    String approvalStatus;
    @SerializedName("visitorTemperature")
    String visitorTemperature;
    @SerializedName("mask")
    boolean mask;

    public DataObjectVisitors(String mVisitor_id,String mFirst_Name, String mLast_Name, String mMobile, String mPhoto,
                              String mTime, String mPurpose, String mFrom, String mTomeet,String mDocUrl,
                              boolean mError_flag,boolean askForApproval,String approvalStatus, String visitorTemperature,
                              boolean isMask) {
        this.mVisitor_id = mVisitor_id;
        this.mFirst_Name = mFirst_Name;
        this.mLast_Name = mLast_Name;
        this.mMobile = mMobile;
        this.mPhoto = mPhoto;
        this.mTime = mTime;
        this.mPurpose = mPurpose;
        this.mFrom = mFrom;
        this.mTomeet = mTomeet;
        this.mDocUrl = mDocUrl;
        this.mError_flag = mError_flag;
        this.askForApproval=askForApproval;
        this.approvalStatus=approvalStatus;
        this.visitorTemperature = visitorTemperature;
        this.mask = isMask;
    }

    public String getmVisitor_id() {
        return mVisitor_id;
    }

    public void setmVisitor_id(String mVisitor_id) {
        this.mVisitor_id = mVisitor_id;
    }

    public String getmLast_Name() {
        return mLast_Name;
    }

    public void setmLast_Name(String mLast_Name) {
        this.mLast_Name = mLast_Name;
    }

    public String getmFirst_Name() {
        return mFirst_Name;
    }

    public void setmFirst_Name(String mFirst_Name) {
        this.mFirst_Name = mFirst_Name;
    }

    public String getmMobile() {
        return mMobile;
    }

    public void setmMobile(String mMobile) {
        this.mMobile = mMobile;
    }

    public String getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmPurpose() {
        return mPurpose;
    }

    public void setmPurpose(String mPurpose) {
        this.mPurpose = mPurpose;
    }

    public String getmFrom() {
        return mFrom;
    }

    public void setmFrom(String mFrom) {
        this.mFrom = mFrom;
    }

    public String getmTomeet() {
        return mTomeet;
    }

    public void setmTomeet(String mTomeet) {
        this.mTomeet = mTomeet;
    }

    public String getmDocUrl() {
        return mDocUrl;
    }

    public void setmDocUrl(String mDocUrl) {
        this.mDocUrl = mDocUrl;
    }

    public boolean ismError_flag() {
        return mError_flag;
    }

    public void setmError_flag(boolean mError_flag) {
        this.mError_flag = mError_flag;
    }

    public boolean isAskForApproval() {
        return askForApproval;
    }

    public void setAskForApproval(boolean askForApproval) {
        this.askForApproval = askForApproval;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getVisitorTemperature() {
        return visitorTemperature;
    }

    public void setVisitorTemperature(String visitorTemperature) {
        this.visitorTemperature = visitorTemperature;
    }

    public boolean isMask() {
        return mask;
    }

    public void setMask(boolean mask) {
        this.mask = mask;
    }
}