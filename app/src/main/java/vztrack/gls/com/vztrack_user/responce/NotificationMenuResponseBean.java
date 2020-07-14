package vztrack.gls.com.vztrack_user.responce;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.beans.NotificationMenuBeans;

public class NotificationMenuResponseBean {
    @SerializedName("code")
    String code;
    @SerializedName("message")
    String message;
    @SerializedName("notificationMenuBeans")
    ArrayList<NotificationMenuBeans> notificationMenuBeans = new ArrayList<NotificationMenuBeans>();

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

    public ArrayList<NotificationMenuBeans> getNotificationMenuBeans() {
        return notificationMenuBeans;
    }

    public void setNotificationMenuBeans(ArrayList<NotificationMenuBeans> notificationMenuBeans) {
        this.notificationMenuBeans = notificationMenuBeans;
    }
}
