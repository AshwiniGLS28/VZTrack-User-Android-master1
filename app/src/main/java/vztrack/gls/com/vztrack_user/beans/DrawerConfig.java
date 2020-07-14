package vztrack.gls.com.vztrack_user.beans;

public class DrawerConfig implements Comparable {
    String Menuname;
    int image;
    boolean isshow;
    boolean isNotificationcount;
    int id;
    int frequentUsedCount;

    public String getMenuname() {
        return Menuname;
    }

    public void setMenuname(String menuname) {
        Menuname = menuname;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isIsshow() {
        return isshow;
    }

    public void setIsshow(boolean isshow) {
        this.isshow = isshow;
    }

    public boolean isNotificationcount() {
        return isNotificationcount;
    }

    public void setNotificationcount(boolean notificationcount) {
        isNotificationcount = notificationcount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrequentUsedCount() {
        return frequentUsedCount;
    }

    public void setFrequentUsedCount(int frequentUsedCount) {
        this.frequentUsedCount = frequentUsedCount;
    }

    @Override
    public int compareTo(Object o) {
        int frequentUsedCount1=((DrawerConfig)o).getFrequentUsedCount();
        /* For Ascending order*/
//        return this.frequentUsedCount-frequentUsedCount1;

        /* For Descending order do like this */
        return frequentUsedCount1-this.frequentUsedCount;
    }
}
