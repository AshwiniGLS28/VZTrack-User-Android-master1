package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

public class FamilyListBean {

    @SerializedName("familyId")
    int familyId;
    @SerializedName("loginId")
    int loginId;

    public int getFamilyId() {
        return familyId;
    }

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }

    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }
}
