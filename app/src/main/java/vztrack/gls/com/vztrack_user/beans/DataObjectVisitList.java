package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by sandeep on 14/3/16.
 */
public class DataObjectVisitList implements Serializable {
    @SerializedName("mFirst_Name")
    private String mFirst_Name;
    @SerializedName("mLast_Name")
    private String mLast_Name;
    @SerializedName("InTime")
    private String InTime;
    @SerializedName("OutTime")
    private String OutTime;
    @SerializedName("mPurpose")
    private String mPurpose;
    @SerializedName("mFrom")
    private String mFrom;
    @SerializedName("mTomeet")
    private String mTomeet;
    @SerializedName("mVehicle_no")
    private String mVehicle_no;
    @SerializedName("mNoOfVisitors")
    private String mNoOfVisitors;
    @SerializedName("mWhomeToVisit")
    private String mWhomeToVisit;
    @SerializedName("mError_flag")
    private boolean mError_flag;
    @SerializedName("mBadgeNumber")
    private String mBadgeNumber;
    @SerializedName("approvalCount")
    private int approvalCount;



    public DataObjectVisitList(String mFirst_Name, String mLast_Name, String InTime, String outTime ,String mPurpose, String mFrom, String mTomeet, String mVehicle_no, String mNoOfVisitors, String mWhomeToVisit, boolean isError, String mBadgeNumber,int approvalCount) {
        this.mFirst_Name = mFirst_Name;
        this.mLast_Name = mLast_Name;
        this.InTime = InTime;
        this.OutTime = outTime;
        this.mPurpose = mPurpose;
        this.mFrom = mFrom;
        this.mTomeet = mTomeet;
        this.mVehicle_no = mVehicle_no;
        this.mNoOfVisitors = mNoOfVisitors;
        this.mWhomeToVisit = mWhomeToVisit;
        this.mError_flag = isError;
        this.mBadgeNumber = mBadgeNumber;
        this.approvalCount=approvalCount;
    }

    public String getInTime() {
        return InTime;
    }

    public void setInTime(String inTime) {
        InTime = inTime;
    }

    public String getOutTime() {
        return OutTime;
    }

    public void setOutTime(String outTime) {
        OutTime = outTime;
    }

    public String getmFirst_Name() {
        return mFirst_Name;
    }

    public void setmFirst_Name(String mFirst_Name) {
        this.mFirst_Name = mFirst_Name;
    }

    public String getmLast_Name() {
        return mLast_Name;
    }

    public void setmLast_Name(String mLast_Name) {
        this.mLast_Name = mLast_Name;
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

    public String getmVehicle_no() {
        return mVehicle_no;
    }

    public void setmVehicle_no(String mVehicle_no) {
        this.mVehicle_no = mVehicle_no;
    }

    public String getmNoOfVisitors() {
        return mNoOfVisitors;
    }

    public void setmNoOfVisitors(String mNoOfVisitors) {
        this.mNoOfVisitors = mNoOfVisitors;
    }

    public String getmWhomeToVisit() {
        return mWhomeToVisit;
    }

    public void setmWhomeToVisit(String mWhomeToVisit) {
        this.mWhomeToVisit = mWhomeToVisit;
    }

    public boolean ismError_flag() {
        return mError_flag;
    }

    public void setmError_flag(boolean mError_flag) {
        this.mError_flag = mError_flag;
    }

    public String getmBadgeNumber() {
        return mBadgeNumber;
    }

    public void setmBadgeNumber(String mBadgeNumber) {
        this.mBadgeNumber = mBadgeNumber;
    }

    public int getApprovalCount() {
        return approvalCount;
    }

    public void setApprovalCount(int approvalCount) {
        this.approvalCount = approvalCount;
    }
}