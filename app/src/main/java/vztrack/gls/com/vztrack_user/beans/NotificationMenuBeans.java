package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

public class NotificationMenuBeans {
    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("access")
    boolean access;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }
}
