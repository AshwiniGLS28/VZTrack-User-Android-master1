package vztrack.gls.com.vztrack_user.responce;

import com.google.gson.annotations.SerializedName;

import vztrack.gls.com.vztrack_user.beans.AppBean;
import vztrack.gls.com.vztrack_user.beans.SocietyFeaturesConfigBean;
import vztrack.gls.com.vztrack_user.profile.FamilyBean;
import vztrack.gls.com.vztrack_user.profile.Socity;

/**
 * Created by sandeep on 17/3/16.
 */
public class LoginResponse {
    @SerializedName("message")
    String message;
    @SerializedName("code")
    String code;
    @SerializedName("socity")
    Socity socity;
    @SerializedName("family")
    FamilyBean family;
    @SerializedName("appBean")
    AppBean appBean;
    @SerializedName("configBean")
    SocietyFeaturesConfigBean configBean;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Socity getSocity() {
        return socity;
    }

    public void setSocity(Socity socity) {
        this.socity = socity;
    }

    public FamilyBean getFamily() {
        return family;
    }

    public void setFamily(FamilyBean family) {
        this.family = family;
    }

    public AppBean getAppBean() {
        return appBean;
    }

    public void setAppBean(AppBean appBean) {
        this.appBean = appBean;
    }

    public SocietyFeaturesConfigBean getConfigBean() {
        return configBean;
    }

    public void setConfigBean(SocietyFeaturesConfigBean configBean) {
        this.configBean = configBean;
    }
}
