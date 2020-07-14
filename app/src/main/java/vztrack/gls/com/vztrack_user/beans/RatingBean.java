package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 20/12/16.
 */

public class RatingBean {
    @SerializedName("visitorMobile")
    private String visitorMobile;
    @SerializedName("visitorPurpose")
    private String visitorPurpose;
    @SerializedName("inTime")
    private String inTime;
    @SerializedName("visitorPhoto")
    private String visitorPhoto;
    @SerializedName("visitorName")
    private String visitorName;

    public String getVisitorMobile() {
        return visitorMobile;
    }
    public void setVisitorMobile(String visitorMobile) {
        this.visitorMobile = visitorMobile;
    }
    public String getVisitorPurpose() {
        return visitorPurpose;
    }
    public void setVisitorPurpose(String visitorPurpose) {
        this.visitorPurpose = visitorPurpose;
    }
    public String getInTime() {
        return inTime;
    }
    public void setInTime(String inTime) {
        this.inTime = inTime;
    }
    public String getVisitorPhoto() {
        return visitorPhoto;
    }
    public void setVisitorPhoto(String visitorPhoto) {
        this.visitorPhoto = visitorPhoto;
    }
    public String getVisitorName() {
        return visitorName;
    }
    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }
}

