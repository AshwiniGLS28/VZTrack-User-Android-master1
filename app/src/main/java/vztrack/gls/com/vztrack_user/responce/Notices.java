package vztrack.gls.com.vztrack_user.responce;

import com.google.gson.annotations.SerializedName;

import java.io.File;

/**
 * Created by sandeep on 31/3/16.
 */
public class Notices {

    @SerializedName("noticeDesc")
    String noticeDesc;
    @SerializedName("noticeEndDate")
    String noticeEndDate;
    @SerializedName("noticeHeading")
    String noticeHeading;
    @SerializedName("noticeId")
    String noticeId;
    @SerializedName("noticeStartDate")
    String noticeStartDate;
    @SerializedName("path")
    String path;
    @SerializedName("postById")
    String postById;
    @SerializedName("socityId")
    String socityId;
    @SerializedName("fileType")
    String fileType;
    @SerializedName("fileName")
    String fileName;
    @SerializedName("file")
    File file;

    public String getNoticeDesc() {
        return noticeDesc;
    }

    public void setNoticeDesc(String noticeDesc) {
        this.noticeDesc = noticeDesc;
    }

    public String getNoticeEndDate() {
        return noticeEndDate;
    }

    public void setNoticeEndDate(String noticeEndDate) {
        this.noticeEndDate = noticeEndDate;
    }

    public String getNoticeHeading() {
        return noticeHeading;
    }

    public void setNoticeHeading(String noticeHeading) {
        this.noticeHeading = noticeHeading;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeStartDate() {
        return noticeStartDate;
    }

    public void setNoticeStartDate(String noticeStartDate) {
        this.noticeStartDate = noticeStartDate;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPostById() {
        return postById;
    }

    public void setPostById(String postById) {
        this.postById = postById;
    }

    public String getSocityId() {
        return socityId;
    }

    public void setSocityId(String socityId) {
        this.socityId = socityId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
