package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 18/9/17.
 */

public class SearchVehicleBean {
    @SerializedName("flatNo_OR_name")
    String flatNo_OR_name;
    @SerializedName("ownerType")
    String ownerType;
    @SerializedName("time_OR_vehicleType")
    String time_OR_vehicleType;
    @SerializedName("vehicleNumber")
    String vehicleNumber;
    @SerializedName("flatNumber")
    String flatNumber;
    @SerializedName("notificationIconFlag")
    boolean notificationIconFlag;

    public String getFlatNo_OR_name() {
        return flatNo_OR_name;
    }
    @SerializedName("parkingLevel")
    String parkingLevel;
    @SerializedName("parkingNo")
    String parkingNo;
    @SerializedName("parkingStickerNo")
    String parkingStickerNo;
    @SerializedName("parkingType")
    String parkingType;
    public void setFlatNo_OR_name(String flatNo_OR_name) {
        this.flatNo_OR_name = flatNo_OR_name;
    }
    public String getOwnerType() {
        return ownerType;
    }
    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }
    public String getTime_OR_vehicleType() {
        return time_OR_vehicleType;
    }
    public void setTime_OR_vehicleType(String time_OR_vehicleType) {
        this.time_OR_vehicleType = time_OR_vehicleType;
    }
    public String getVehicleNumber() {
        return vehicleNumber;
    }
    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public boolean isNotificationIconFlag() {
        return notificationIconFlag;
    }

    public void setNotificationIconFlag(boolean notificationIconFlag) {
        this.notificationIconFlag = notificationIconFlag;
    }

    public String getParkingLevel() {
        return parkingLevel;
    }

    public void setParkingLevel(String parkingLevel) {
        this.parkingLevel = parkingLevel;
    }

    public String getParkingNo() {
        return parkingNo;
    }

    public void setParkingNo(String parkingNo) {
        this.parkingNo = parkingNo;
    }

    public String getParkingStickerNo() {
        return parkingStickerNo;
    }

    public void setParkingStickerNo(String parkingStickerNo) {
        this.parkingStickerNo = parkingStickerNo;
    }

    public String getParkingType() {
        return parkingType;
    }

    public void setParkingType(String parkingType) {
        this.parkingType = parkingType;
    }
}
