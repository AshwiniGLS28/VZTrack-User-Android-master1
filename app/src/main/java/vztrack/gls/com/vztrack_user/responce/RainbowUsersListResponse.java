package vztrack.gls.com.vztrack_user.responce;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.profile.FamilyBean;

public class RainbowUsersListResponse {
    @SerializedName("rainbowUsersList")
    ArrayList<ArrayList<FamilyBean>> rainbowUsersList = new ArrayList<ArrayList<FamilyBean>>();

    public ArrayList<ArrayList<FamilyBean>> getRainbowUsersList() {
        return rainbowUsersList;
    }

    public void setRainbowUsersList(ArrayList<ArrayList<FamilyBean>> rainbowUsersList) {
        this.rainbowUsersList = rainbowUsersList;
    }
}
