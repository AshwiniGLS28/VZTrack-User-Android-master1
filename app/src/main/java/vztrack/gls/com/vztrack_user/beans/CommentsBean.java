package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 22/12/16.
 */

public class CommentsBean {
    @SerializedName("name")
    private String name;
    @SerializedName("time")
    private String time;
    @SerializedName("comment")
    private String comment;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

}
