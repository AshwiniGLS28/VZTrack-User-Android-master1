package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 1/2/17.
 */


public class AppBean {
    @SerializedName("appVersion")
    int appVersion;
    @SerializedName("forceUpdate")
    String forceUpdate;
    @SerializedName("versionInfo")
    String versionInfo;

    public int getAppVersion() {
        return appVersion;
    }
    public void setAppVersion(int appVersion) {
        this.appVersion = appVersion;
    }
    public String getForceUpdate() {
        return forceUpdate;
    }
    public void setForceUpdate(String forceUpdate) {
        this.forceUpdate = forceUpdate;
    }
    public String getVersionInfo() {
        return versionInfo;
    }
    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }


}
