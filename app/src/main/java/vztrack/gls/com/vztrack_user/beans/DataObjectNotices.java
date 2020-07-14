package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 14/3/16.
 */
public class DataObjectNotices {
    @SerializedName("mTextTitle")
    private String mTextTitle;
    @SerializedName("mTextdescription")
    private String mTextdescription;
    @SerializedName("mTextDateStart")
    private String mTextDateStart;
    @SerializedName("mTextDateEnd")
    private String mTextDateEnd;
    @SerializedName("mTextURL")
    private String mTextURL;
    @SerializedName("mNoticeId")
    private String mNoticeId;
    @SerializedName("mFileType")
    private String mFileType;

    public DataObjectNotices(String mTextTitle, String mTextdescription,String mTextStartDate, String mTextEndDate, String mTextURL, String mNoticeId, String mFileType) {
        this.mTextTitle = mTextTitle;
        this.mTextdescription = mTextdescription;
        this.mTextDateStart = mTextStartDate;
        this.mTextURL = mTextURL;
        this.mTextDateEnd = mTextEndDate;
        this.mNoticeId = mNoticeId;
        this.mFileType = mFileType;
    }

    public String getmNoticeId() {
        return mNoticeId;
    }

    public void setmNoticeId(String mNoticeId) {
        this.mNoticeId = mNoticeId;
    }

    public String getmTextDateEnd() {
        return mTextDateEnd;
    }

    public void setmTextDateEnd(String mTextDateEnd) {
        this.mTextDateEnd = mTextDateEnd;
    }

    public String getmTextTitle() {
        return mTextTitle;
    }

    public void setmTextTitle(String mTextTitle) {
        this.mTextTitle = mTextTitle;
    }

    public String getmTextdescription() {
        return mTextdescription;
    }

    public void setmTextdescription(String mTextdescription) {
        this.mTextdescription = mTextdescription;
    }

    public String getmTextDateStart() {
        return mTextDateStart;
    }

    public void setmTextDateStart(String mTextDateStart) {
        this.mTextDateStart = mTextDateStart;
    }

    public String getmTextURL() {
        return mTextURL;
    }

    public void setmTextURL(String mTextURL) {
        this.mTextURL = mTextURL;
    }

    public String getmFileType() {
        return mFileType;
    }

    public void setmFileType(String mFileType) {
        this.mFileType = mFileType;
    }
}