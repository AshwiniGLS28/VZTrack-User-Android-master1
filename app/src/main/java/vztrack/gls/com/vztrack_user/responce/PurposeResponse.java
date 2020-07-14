package vztrack.gls.com.vztrack_user.responce;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.beans.ResponceBean;

/**
 * Created by Santosh on 11/2/2018.
 */

public class PurposeResponse extends ResponceBean {
    @SerializedName("invitePurposeList")
    ArrayList<String> invitePurposeList;

    public ArrayList<String> getInvitePurposeList() {
        return invitePurposeList;
    }

    public void setInvitePurposeList(ArrayList<String> invitePurposeList) {
        this.invitePurposeList = invitePurposeList;
    }
}
