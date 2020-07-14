package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Santosh on 10/31/2018.
 */

public class InvitationBean {
    @SerializedName("invitationId")
    int invitationId;
    @SerializedName("purpose")
    String purpose;
    @SerializedName("invitedDate")
    String invitedDate;
    @SerializedName("description")
    String description;
    @SerializedName("guestCount")
    int guestCount;
    @SerializedName("contactgroup")
    private ArrayList<ContactBean> contactgroup;

    public int getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(int invitationId) {
        this.invitationId = invitationId;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getInvitedDate() {
        return invitedDate;
    }

    public void setInvitedDate(String invitedDate) {
        this.invitedDate = invitedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(int guestCount) {
        this.guestCount = guestCount;
    }

    public ArrayList<ContactBean> getContactgroup() {
        return contactgroup;
    }

    public void setContactgroup(ArrayList<ContactBean> contactgroup) {
        this.contactgroup = contactgroup;
    }
}
