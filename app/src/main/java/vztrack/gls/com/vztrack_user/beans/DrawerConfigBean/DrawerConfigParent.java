package vztrack.gls.com.vztrack_user.beans.DrawerConfigBean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.beans.DrawerConfig;

public class DrawerConfigParent {
    @SerializedName("menuname")
    private String menuname;
    @SerializedName("contents")
    private ArrayList<ConfigBean> contents;

    public DrawerConfigParent(String sectionLabel, ArrayList<ConfigBean> itemArrayList) {
        this.menuname = sectionLabel;
        this.contents = itemArrayList;
    }

    public String getMenuname() {
        return menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }

    public ArrayList<ConfigBean> getContents() {
        return contents;
    }

    public void setContents(ArrayList<ConfigBean> contents) {
        this.contents = contents;
    }
}
