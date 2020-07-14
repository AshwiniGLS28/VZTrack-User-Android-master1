package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MarketplaceBean {
    @SerializedName("marketplaceId")
    private int marketplaceId;
    @SerializedName("societyId")
    private int societyId;
    @SerializedName("familyId")
    private int familyId;
    @SerializedName("flatNo")
    private String flatNo;
    @SerializedName("type")
    private String type;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("price")
    private String price;
    @SerializedName("isFree")
    private int isFree;
    @SerializedName("mobileNo")
    private String mobileNo;
    @SerializedName("photoUrl")
    private String photoUrl;
    @SerializedName("path")
    private String path;
    @SerializedName("createdDate")
    private String createdDate;
    @SerializedName("modifiedDate")
    private String modifiedDate;
    @SerializedName("closeDate")
    private String closeDate;
    @SerializedName("status")
    private String status;
    @SerializedName("closeReasone")
    private String closeReasone;
    @SerializedName("action")
    private String action;
    @SerializedName("editedPhoto")
    private String editedPhoto;
    @SerializedName("comments")
    private ArrayList<CommentBean> comments;

    public int getMarketplaceId() {
        return marketplaceId;
    }

    public void setMarketplaceId(int marketplaceId) {
        this.marketplaceId = marketplaceId;
    }

    public int getSocietyId() {
        return societyId;
    }

    public void setSocietyId(int societyId) {
        this.societyId = societyId;
    }

    public int getFamilyId() {
        return familyId;
    }

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getIsFree() {
        return isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCloseReasone() {
        return closeReasone;
    }

    public void setCloseReasone(String closeReasone) {
        this.closeReasone = closeReasone;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ArrayList<CommentBean> getComments() {
        return comments;
    }

    public void setComments(ArrayList<CommentBean> comments) {
        this.comments = comments;
    }

    public String getEditedPhoto() {
        return editedPhoto;
    }

    public void setEditedPhoto(String editedPhoto) {
        this.editedPhoto = editedPhoto;
    }
}
