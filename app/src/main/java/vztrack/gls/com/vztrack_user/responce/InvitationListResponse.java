package vztrack.gls.com.vztrack_user.responce;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.beans.InvitationBean;
import vztrack.gls.com.vztrack_user.beans.ResponceBean;

/**
 * Created by Santosh on 11/2/2018.
 */

public class InvitationListResponse extends ResponceBean {
    @SerializedName("invitationsResponse")
    private ArrayList<InvitationBean> invitationsResponse;

    public ArrayList<InvitationBean> getInvitationsResponse() {
        return invitationsResponse;
    }

    public void setInvitationsResponse(ArrayList<InvitationBean> invitationsResponse) {
        this.invitationsResponse = invitationsResponse;
    }
}
