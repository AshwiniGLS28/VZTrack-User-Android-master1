package vztrack.gls.com.vztrack_user.responce;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.beans.ResponceBean;
import vztrack.gls.com.vztrack_user.profile.FamilyBean;

/**
 * Created by sandeep on 30/1/18.
 */

public class RainbowUserResponse extends ResponceBean {
    @SerializedName("rainbowUsers")
    ArrayList<FamilyBean> rainbowUsers = new ArrayList<>();
    public ArrayList<FamilyBean> getRainbowUsers() {
        return rainbowUsers;
    }
    public void setRainbowUsers(ArrayList<FamilyBean> rainbowUsers) {
        this.rainbowUsers = rainbowUsers;
    }
}
