package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

public class FeaturesBean {

    @SerializedName("featureName")
    String featureName;
    @SerializedName("isFeatureAccess")
    boolean isFeatureAccess;

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public boolean isFeatureAccess() {
        return isFeatureAccess;
    }

    public void setFeatureAccess(boolean featureAccess) {
        isFeatureAccess = featureAccess;
    }
}
