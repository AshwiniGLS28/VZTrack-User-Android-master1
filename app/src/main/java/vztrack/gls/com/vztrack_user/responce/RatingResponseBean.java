package vztrack.gls.com.vztrack_user.responce;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 19/12/16.
 */

public class RatingResponseBean {

    @SerializedName("familyId")
    private int familyId;
    @SerializedName("societyId")
    private int societyId;
    @SerializedName("flatNumber")
    private String flatNumber;
    @SerializedName("visitorName")
    private String visitorName;
    @SerializedName("visitorMobile")
    private String visitorMobile;
    @SerializedName("visitorPurpose")
    private String visitorPurpose;
    @SerializedName("visitorPhoto")
    private String visitorPhoto;
    @SerializedName("qualityRating")
    private int qualityRating;
    @SerializedName("priceRating")
    private int priceRating;
    @SerializedName("punctualityRating")
    private int punctualityRating;
    @SerializedName("comments")
    private String comments;

    public int getFamilyId() {
        return familyId;
    }
    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }
    public int getSocietyId() {
        return societyId;
    }
    public void setSocietyId(int societyId) {
        this.societyId = societyId;
    }
    public String getFlatNumber() {
        return flatNumber;
    }
    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }
    public String getVisitorName() {
        return visitorName;
    }
    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }
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
    public String getVisitorPhoto() {
        return visitorPhoto;
    }
    public void setVisitorPhoto(String visitorPhoto) {
        this.visitorPhoto = visitorPhoto;
    }
    public int getQualityRating() {
        return qualityRating;
    }
    public void setQualityRating(int qualityRating) {
        this.qualityRating = qualityRating;
    }
    public int getPriceRating() {
        return priceRating;
    }
    public void setPriceRating(int priceRating) {
        this.priceRating = priceRating;
    }
    public int getPunctualityRating() {
        return punctualityRating;
    }
    public void setPunctualityRating(int punctualityRating) {
        this.punctualityRating = punctualityRating;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }

}
