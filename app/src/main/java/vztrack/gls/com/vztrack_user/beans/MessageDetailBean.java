package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

public class MessageDetailBean {
    @SerializedName("messageId")
    private int messageId;
    @SerializedName("flatNo")
    private String flatNo;
    @SerializedName("readBy")
    private String readBy;
    @SerializedName("readDate")
    private String readDate;
    @SerializedName("readStatus")
    private boolean readStatus;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public String getReadBy() {
        return readBy;
    }

    public void setReadBy(String readBy) {
        this.readBy = readBy;
    }

    public String getReadDate() {
        return readDate;
    }

    public void setReadDate(String readDate) {
        this.readDate = readDate;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }
}
