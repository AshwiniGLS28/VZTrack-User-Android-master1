package vztrack.gls.com.vztrack_user.beans;

import java.util.ArrayList;

public class SectionModel{
    private String sectionLabel;
    private ArrayList<DrawerConfig> itemArrayList;

    public SectionModel(String sectionLabel, ArrayList<DrawerConfig> itemArrayList) {
        this.sectionLabel = sectionLabel;
        this.itemArrayList = itemArrayList;
    }

    public String getSectionLabel() {
        return sectionLabel;
    }

    public ArrayList<DrawerConfig> getItemArrayList() {
        return itemArrayList;
    }
}