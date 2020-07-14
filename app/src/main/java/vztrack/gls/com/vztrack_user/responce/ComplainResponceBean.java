package vztrack.gls.com.vztrack_user.responce;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.beans.ComplainsBean;

/**
 * Created by sandeep on 10/7/17.
 */

public class ComplainResponceBean {
    @SerializedName("message")
    String message;
    @SerializedName("code")
    String code;
    @SerializedName("complains")
    ArrayList<ComplainsBean> complains;
    @SerializedName("complainDetails")
    ComplainsBean complainDetails;
    @SerializedName("societyComplainCategories")
    ArrayList<String> societyComplainCategories;
    @SerializedName("complainPhoto")
    String complainPhoto;
    @SerializedName("reopenDate")
    String reopenDate;
    @SerializedName("reOpenComment")
    String reOpenComment;

    public ArrayList<String> getSocietyComplainCategories() {
        return societyComplainCategories;
    }

    public void setSocietyComplainCategories(ArrayList<String> societyComplainCategories) {
        this.societyComplainCategories = societyComplainCategories;
    }

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

    public ArrayList<ComplainsBean> getComplains() {
        return complains;
    }

    public ComplainsBean getComplainDetails() {
        return complainDetails;
    }

    public void setComplainDetails(ComplainsBean complainDetails) {
        this.complainDetails = complainDetails;
    }

    public void setComplains(ArrayList<ComplainsBean> complains) {
        this.complains = complains;
    }

    public String getComplainPhoto() {
        return complainPhoto;
    }

    public void setComplainPhoto(String complainPhoto) {
        this.complainPhoto = complainPhoto;
    }

    public String getReopenDate() {
        return reopenDate;
    }

    public void setReopenDate(String reopenDate) {
        this.reopenDate = reopenDate;
    }

    public String getReOpenComment() {
        return reOpenComment;
    }

    public void setReOpenComment(String reOpenComment) {
        this.reOpenComment = reOpenComment;
    }
}
