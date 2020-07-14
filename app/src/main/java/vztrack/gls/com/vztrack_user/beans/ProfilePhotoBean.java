package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

public class ProfilePhotoBean {
    @SerializedName("getUserPhoto")
    private  String getUserPhoto;

    public String getGetUserPhoto() {
        return getUserPhoto;
    }

    public void setGetUserPhoto(String getUserPhoto) {
        this.getUserPhoto = getUserPhoto;
    }
}
