package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SecondaryUserBean {
    @SerializedName("familyId")
    int familyId;
    @SerializedName("isTenant")
    boolean isTenant;
    @SerializedName("loginId")
    int loginId;
    @SerializedName("name")
    String name;
    @SerializedName("username")
    String username;
    @SerializedName("verified")
    boolean verified;
    @SerializedName("tenant")
    boolean tenant;
    @SerializedName("features")
    ArrayList<FeaturesBean> features = null;


    public boolean isTenant() {
        return tenant;
    }

    public int getFamilyId() {
        return familyId;
    }

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }


    public void setTenant(boolean tenant) {
        isTenant = tenant;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }

    public ArrayList<FeaturesBean> getFeatures() {
        return features;
    }

    public void setFeatures(ArrayList<FeaturesBean> features) {
        this.features = features;
    }
}
