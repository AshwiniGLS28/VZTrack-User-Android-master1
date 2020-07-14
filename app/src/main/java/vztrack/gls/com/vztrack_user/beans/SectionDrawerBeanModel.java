package vztrack.gls.com.vztrack_user.beans;

import java.util.ArrayList;

public class SectionDrawerBeanModel {
    private String sectionLabel;
    private ArrayList<DrawerConfig> itemArrayList;
    public SectionDrawerBeanModel(String sectionLabel, ArrayList<DrawerConfig> itemArrayList) {
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
