package vztrack.gls.com.vztrack_user.profile;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 18/9/17.
 */
public class VehicleBean {
    @SerializedName("societyId")
    String societyId;
    @SerializedName("familyId")
    String familyId;
    @SerializedName("vehicleType")
    String vehicleType;
    @SerializedName("vehicleNumber")
    String vehicleNumber;
    @SerializedName("parkingLevel")
    String parkingLevel;
    @SerializedName("parkingNo")
    String parkingNo;
    @SerializedName("parkingStickerNo")
    String parkingStickerNo;
    @SerializedName("parkingType")
    String parkingType;
    public String getSocietyId() {
        return societyId;
    }
    public void setSocietyId(String societyId) {
        this.societyId = societyId;
    }
    public String getFamilyId() {
        return familyId;
    }
    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }
    public String getVehicleType() {
        return vehicleType;
    }
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
    public String getVehicleNumber() {
        return vehicleNumber;
    }
    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
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
