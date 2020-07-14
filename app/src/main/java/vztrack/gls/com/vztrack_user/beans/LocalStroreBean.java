package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Santosh on 18-Dec-17.
 */

public class LocalStroreBean{
    @SerializedName("activeFlag")
    boolean activeFlag;
    @SerializedName("address")
    String address;
    @SerializedName("area")
    String area;
    @SerializedName("contactNumber")
    String contactNumber;
    @SerializedName("contactNumber2")
    String contactNumber2;
    @SerializedName("latitude")
    double latitude;
    @SerializedName("longitude")
    double longitude;
    @SerializedName("specialDescription")
    String specialDescription;
    @SerializedName("storeCategoryId")
    int storeCategoryId;
    @SerializedName("storeCategoryName")
    String storeCategoryName;
    @SerializedName("storeId")
    int storeId;
    @SerializedName("clicked")
    boolean clicked = false;
    @SerializedName("storeImageUrl")
    String storeImageUrl;
    @SerializedName("storeName")
    String storeName;
    @SerializedName("isRainbowactivated")
    boolean isRainbowactivated;
    @SerializedName("jid")
    String jid;

    public boolean isActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(boolean activeFlag) {
        this.activeFlag = activeFlag;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactNumber2() {
        return contactNumber2;
    }

    public void setContactNumber2(String contactNumber2) {
        this.contactNumber2 = contactNumber2;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getSpecialDescription() {
        return specialDescription;
    }

    public void setSpecialDescription(String specialDescription) {
        this.specialDescription = specialDescription;
    }

    public int getStoreCategoryId() {
        return storeCategoryId;
    }

    public void setStoreCategoryId(int storeCategoryId) {
        this.storeCategoryId = storeCategoryId;
    }

    public String getStoreCategoryName() {
        return storeCategoryName;
    }

    public void setStoreCategoryName(String storeCategoryName) {
        this.storeCategoryName = storeCategoryName;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public String getStoreImageUrl() {
        return storeImageUrl;
    }

    public void setStoreImageUrl(String storeImageUrl) {
        this.storeImageUrl = storeImageUrl;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public boolean isRainbowactivated() {
        return isRainbowactivated;
    }

    public void setRainbowactivated(boolean rainbowactivated) {
        isRainbowactivated = rainbowactivated;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }
}
