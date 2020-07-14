package vztrack.gls.com.vztrack_user.responce;

import com.google.gson.annotations.SerializedName;

import vztrack.gls.com.vztrack_user.beans.InvitationBean;
import vztrack.gls.com.vztrack_user.beans.ResponceBean;

/**
 * Created by Santosh on 11/5/2018.
 */

public class InvitationResponse extends ResponceBean {
    @SerializedName("invitatonDetails")
    InvitationBean invitatonDetails;

    public InvitationBean getInvitatonDetails() {
        return invitatonDetails;
    }

    public void setInvitatonDetails(InvitationBean invitatonDetails) {
        this.invitatonDetails = invitatonDetails;
    }
}
