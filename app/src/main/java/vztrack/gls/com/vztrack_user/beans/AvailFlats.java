package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sandeep on 6/4/16.
 */
public class AvailFlats {

    @SerializedName("active")
    boolean active;
    @SerializedName("buildingId")
    int buildingId;
    @SerializedName("familyId")
    int familyId;
    @SerializedName("flatFamilySize")
    int flatFamilySize;
    @SerializedName("flatNo")
    String flatNo;
    @SerializedName("flatOwnerName")
    String flatOwnerName;
    @SerializedName("flatVehickeSize")
    int flatVehickeSize;
    @SerializedName("societyId")
    int societyId;
    @SerializedName("loginId")
    int loginId;


    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public int getFamilyId() {
        return familyId;
    }

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }

    public int getFlatFamilySize() {
        return flatFamilySize;
    }

    public void setFlatFamilySize(int flatFamilySize) {
        this.flatFamilySize = flatFamilySize;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public String getFlatOwnerName() {
        return flatOwnerName;
    }

    public void setFlatOwnerName(String flatOwnerName) {
        this.flatOwnerName = flatOwnerName;
    }

    public int getFlatVehickeSize() {
        return flatVehickeSize;
    }

    public void setFlatVehickeSize(int flatVehickeSize) {
        this.flatVehickeSize = flatVehickeSize;
    }

    public int getSocietyId() {
        return societyId;
    }

    public void setSocietyId(int societyId) {
        this.societyId = societyId;
    }
}
