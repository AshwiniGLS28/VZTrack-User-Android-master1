package vztrack.gls.com.vztrack_user.profile;

import android.util.Log;

import com.google.gson.annotations.SerializedName;


/**
 * Created by sandeep on 31/3/16.
 */
public class FamilyBean {

    @SerializedName("admin")
    boolean admin;
    @SerializedName("buildingId")
    int buildingId;
    @SerializedName("flatNo")
    String flatNo;
    @SerializedName("flatOwnerName")
    String flatOwnerName;
    @SerializedName("flatOwnerMobile")
    String flatOwnerMobile;
    @SerializedName("flatFamilySize")
    int flatFamilySize;
    @SerializedName("flatVehickeSize")
    int flatVehickeSize;
    @SerializedName("flatVehicles")
    String flatVehicles;
    @SerializedName("flatUserName")
    String flatUserName;
    @SerializedName("flatPassword")
    String flatPassword;
    @SerializedName("active")
    boolean active;
    @SerializedName("societyId")

    int societyId;
    @SerializedName("familyId")
    int familyId;
    @SerializedName("wing")
    String wing;
    @SerializedName("extraAccess")
    boolean extraAccess;
    @SerializedName("soc_phoneNo")
    String soc_phoneNo;
    @SerializedName("sosAccess")
    boolean sosAccess;
    @SerializedName("type")
    boolean type;
    @SerializedName("complaint")
    boolean complaint;
    @SerializedName("loginByEmail")
    boolean loginByEmail;
    @SerializedName("email")
    String email;
    @SerializedName("rainbowEmailId")
    String rainbowEmailId;
    @SerializedName("rainbowPassword")
    String rainbowPassword;
    @SerializedName("rainbowJid")
    String rainbowJid;
    @SerializedName("numberOfRainbowAcct")
    int numberOfRainbowAcct;
    @SerializedName("primary")
    boolean primary;
    @SerializedName("name")
    String name;
    @SerializedName("approval")
    boolean approval;
    @SerializedName("userPhoto")
    String userPhoto;
    @SerializedName("userType")
    int userType;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public String getRainbowEmailId() {
        return rainbowEmailId;
    }

    public void setRainbowEmailId(String rainbowEmailId) {
        this.rainbowEmailId = rainbowEmailId;
    }

    public String getRainbowPassword() {
        return rainbowPassword;
    }

    public void setRainbowPassword(String rainbowPassword) {
        this.rainbowPassword = rainbowPassword;
    }

    public boolean isSosAccess() {
        return sosAccess;
    }

    public void setSosAccess(boolean sosAccess) {
        this.sosAccess = sosAccess;
    }

    public String getSoc_phoneNo() {
        return soc_phoneNo;
    }

    public void setSoc_phoneNo(String soc_phoneNo) {
        this.soc_phoneNo = soc_phoneNo;
    }

    public boolean isExtraAccess() {
        return extraAccess;
    }

    public void setExtraAccess(boolean extraAccess) {
        this.extraAccess = extraAccess;
    }

    public void setWing(String wing) {
        this.wing = wing;
    }

    public String getWing() {
        return wing;
    }

    public String getFlatUserName() {
        return flatUserName;
    }

    public void setFlatUserName(String flatUserName) {
        this.flatUserName = flatUserName;
    }
    public int getBuildingId() {
        return buildingId;
    }
    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
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
    public String getFlatOwnerMobile() {
        return flatOwnerMobile;
    }
    public void setFlatOwnerMobile(String flatOwnerMobile) {
        this.flatOwnerMobile = flatOwnerMobile;
    }
    public int getFlatFamilySize() {
        return flatFamilySize;
    }
    public void setFlatFamilySize(int flatFamilySize) {
        this.flatFamilySize = flatFamilySize;
    }
    public int getFlatVehickeSize() {
        return flatVehickeSize;
    }
    public void setFlatVehickeSize(int flatVehickeSize) {
        this.flatVehickeSize = flatVehickeSize;
    }
    public String getFlatVehicles() {
        return flatVehicles;
    }
    public void setFlatVehicles(String flatVehicles) {
        this.flatVehicles = flatVehicles;
    }
    public String getFlatPassword() {
        return flatPassword;
    }
    public void setFlatPassword(String flatPassword) {
        this.flatPassword = flatPassword;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
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
    public boolean isType() {
        return type;
    }
    public void setType(boolean type) {
        this.type = type;
    }


    public boolean isComplaint() {
        return complaint;
    }

    public void setComplaint(boolean complaint) {
        this.complaint = complaint;
    }

    public boolean isLoginByEmail() {
        return loginByEmail;
    }

    public void setLoginByEmail(boolean loginByEmail) {
        this.loginByEmail = loginByEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRainbowJid() {
        return rainbowJid;
    }

    public void setRainbowJid(String rainbowJid) {
        this.rainbowJid = rainbowJid;
    }

    public int getNumberOfRainbowAcct() {
        return numberOfRainbowAcct;
    }

    public void setNumberOfRainbowAcct(int numberOfRainbowAcct) {
        this.numberOfRainbowAcct = numberOfRainbowAcct;
    }

    public boolean isApproval() {
        return approval;
    }

    public void setApproval(boolean approval) {
        this.approval = approval;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}

