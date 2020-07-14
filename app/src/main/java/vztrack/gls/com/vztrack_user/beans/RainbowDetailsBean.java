package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

public class RainbowDetailsBean {
    @SerializedName("name")
    private String name;
    @SerializedName("emailId")
    private String emailId;
    @SerializedName("password")
    private String password;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmailId() {
        return emailId;
    }
    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
