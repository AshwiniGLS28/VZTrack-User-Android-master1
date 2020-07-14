package vztrack.gls.com.vztrack_user.beans;

import android.graphics.Bitmap;

import com.ale.infra.manager.room.Room;
import com.google.gson.annotations.SerializedName;

public class GroupBeans {
    @SerializedName("group_id")
    int group_id;
    @SerializedName("group_name")
    String group_name;
    @SerializedName("groupPhoto")
    private Bitmap groupPhoto;
    @SerializedName("grp_id")
    String grp_id;
    @SerializedName("room")
    Room room;

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getGrp_id() {
        return grp_id;
    }

    public void setGrp_id(String grp_id) {
        this.grp_id = grp_id;
    }

    public Bitmap getGroupPhoto() {
        return groupPhoto;
    }

    public void setGroupPhoto(Bitmap groupPhoto) {
        this.groupPhoto = groupPhoto;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

}
