package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

public class ReopenComplainBean {
    @SerializedName("code")
    String code;
    @SerializedName("message")
    String message;
    @SerializedName("newVersion")
    boolean newVersion;
    @SerializedName("vz_comp_id")
    int vz_comp_id;
    @SerializedName("reOpenComment")
    String reOpenComment;
    @SerializedName("reopenById")
    int reopenById;
    @SerializedName("reopenName")
    String reopenName;

    public boolean isNewVersion() {
        return newVersion;
    }

    public void setNewVersion(boolean newVersion) {
        this.newVersion = newVersion;
    }

    public int getVz_comp_id() {
        return vz_comp_id;
    }

    public void setVz_comp_id(int vz_comp_id) {
        this.vz_comp_id = vz_comp_id;
    }

    public String getReOpenComment() {
        return reOpenComment;
    }

    public void setReOpenComment(String reOpenComment) {
        this.reOpenComment = reOpenComment;
    }

    public int getReopenById() {
        return reopenById;
    }

    public void setReopenById(int reopenById) {
        this.reopenById = reopenById;
    }

    public String getReopenName() {
        return reopenName;
    }

    public void setReopenName(String reopenName) {
        this.reopenName = reopenName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
