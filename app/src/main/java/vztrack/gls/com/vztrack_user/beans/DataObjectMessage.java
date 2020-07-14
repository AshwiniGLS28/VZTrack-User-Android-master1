package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 14/3/16.
 */
public class DataObjectMessage {
    @SerializedName("mTextMessage")
    private String mTextMessage;
    @SerializedName("mTextGroups")
    private String mTextGroups[];
    @SerializedName("mTextDate")
    private String mTextDate;

    public DataObjectMessage(String mTextMessage, String[] mTextGroups,String mTextDate) {
        this.mTextMessage = mTextMessage;
        this.mTextGroups = mTextGroups;
        this.mTextDate = mTextDate;
    }

    public String getmTextMessage() {
        return mTextMessage;
    }

    public void setmTextMessage(String mTextMessage) {
        this.mTextMessage = mTextMessage;
    }

    public String[] getmTextGroups() {
        return mTextGroups;
    }

    public void setmTextGroups(String[] mTextGroups) {
        this.mTextGroups = mTextGroups;
    }

    public String getmTextDate() {
        return mTextDate;
    }

    public void setmTextDate(String mTextDate) {
        this.mTextDate = mTextDate;
    }
}