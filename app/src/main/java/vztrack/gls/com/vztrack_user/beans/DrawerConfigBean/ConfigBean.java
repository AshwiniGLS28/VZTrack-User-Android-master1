package vztrack.gls.com.vztrack_user.beans.DrawerConfigBean;

import com.google.gson.annotations.SerializedName;

public class ConfigBean implements Comparable {
    @SerializedName("name")
    String name;
    @SerializedName("title")
    String title;
    @SerializedName("image")
    int image;
    @SerializedName("isshow")
    boolean isshow;
    @SerializedName("isNotificationcount")
    boolean isNotificationcount;
    @SerializedName("id")
    int id;
    @SerializedName("frequentUsedCount")
    int frequentUsedCount;
    @SerializedName("menu_image")
    String menu_image;
    @SerializedName("url")
    String url;
    @SerializedName("widgetType")
    String widgetType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getMenu_image() {
        return menu_image;
    }

    public void setMenu_image(String menu_image) {
        this.menu_image = menu_image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(String widgetType) {
        this.widgetType = widgetType;
    }

    @Override
    public int compareTo(Object o) {
        int frequentUsedCount1=((ConfigBean)o).getFrequentUsedCount();
        return frequentUsedCount1-this.frequentUsedCount;
    }
}
