package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sandeep on 22/12/16.
 */

public class OutputBeanSearchProvider {
    @SerializedName("visitorName")
    private String visitorName;
    @SerializedName("visitorPurpose")
    private String visitorPurpose;
    @SerializedName("visitorPhoto")
    private String visitorPhoto;
    @SerializedName("visitorMobile")
    private String visitorMobile;
    @SerializedName("societyPriceRating")
    private int societyPriceRating;
    @SerializedName("societyQualityRating")
    private int societyQualityRating;
    @SerializedName("societyPunctualityRating")
    private int societyPunctualityRating;
    @SerializedName("overAllPriceRating")
    private int overAllPriceRating;
    @SerializedName("overAllQualityRating")
    private int overAllQualityRating;
    @SerializedName("overAllPunctualityRating")
    private int overAllPunctualityRating;
    @SerializedName("visitCount")
    private int visitCount;
    @SerializedName("listCommentsBeans")
    private ArrayList<CommentsBean> listCommentsBeans;

    public String getVisitorName() {
        return visitorName;
    }
    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }
    public String getVisitorPurpose() {
        return visitorPurpose;
    }
    public void setVisitorPurpose(String visitorPurpose) {
        this.visitorPurpose = visitorPurpose;
    }
    public String getVisitorPhoto() {
        return visitorPhoto;
    }
    public void setVisitorPhoto(String visitorPhoto) {
        this.visitorPhoto = visitorPhoto;
    }
    public String getVisitorMobile() {
        return visitorMobile;
    }
    public void setVisitorMobile(String visitorMobile) {
        this.visitorMobile = visitorMobile;
    }
    public int getSocietyPriceRating() {
        return societyPriceRating;
    }
    public void setSocietyPriceRating(int societyPriceRating) {
        this.societyPriceRating = societyPriceRating;
    }
    public int getSocietyQualityRating() {
        return societyQualityRating;
    }
    public void setSocietyQualityRating(int societyQualityRating) {
        this.societyQualityRating = societyQualityRating;
    }
    public int getSocietyPunctualityRating() {
        return societyPunctualityRating;
    }
    public void setSocietyPunctualityRating(int societyPunctualityRating) {
        this.societyPunctualityRating = societyPunctualityRating;
    }
    public int getOverAllPriceRating() {
        return overAllPriceRating;
    }
    public void setOverAllPriceRating(int overAllPriceRating) {
        this.overAllPriceRating = overAllPriceRating;
    }
    public int getOverAllQualityRating() {
        return overAllQualityRating;
    }
    public void setOverAllQualityRating(int overAllQualityRating) {
        this.overAllQualityRating = overAllQualityRating;
    }
    public int getOverAllPunctualityRating() {
        return overAllPunctualityRating;
    }
    public void setOverAllPunctualityRating(int overAllPunctualityRating) {
        this.overAllPunctualityRating = overAllPunctualityRating;
    }
    public ArrayList<CommentsBean> getListCommentsBeans() {
        return listCommentsBeans;
    }
    public void setListCommentsBeans(ArrayList<CommentsBean> listCommentsBeans) {
        this.listCommentsBeans = listCommentsBeans;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }
}
