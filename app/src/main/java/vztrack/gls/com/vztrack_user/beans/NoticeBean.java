package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

import java.io.File;

public class NoticeBean{
	@SerializedName("noticeId")
	int noticeId;
	@SerializedName("noticeHeading")
	String noticeHeading;
	@SerializedName("noticeStartDate")
	String noticeStartDate;
	@SerializedName("noticeEndDate")
	String noticeEndDate;
	@SerializedName("noticeDesc")
	String noticeDesc;
	@SerializedName("noticeBanner")
	String noticeBanner;
	@SerializedName("socityId")
	int socityId;
	@SerializedName("postById")
	int postById;
	@SerializedName("path")
	String path;
	@SerializedName("noticeType")
	String noticeType;
	@SerializedName("noticeFlats")
	String noticeFlats;
	@SerializedName("noticeTo")
	String noticeTo;
	@SerializedName("fileType")
	String fileType;
	@SerializedName("fileName")
	String fileName;
	@SerializedName("file")
	File file;
	
	public String getNoticeTo() {
		return noticeTo;
	}
	public void setNoticeTo(String noticeTo) {
		this.noticeTo = noticeTo;
	}
	public String getNoticeFlats() {
		return noticeFlats;
	}
	public void setNoticeFlats(String noticeFlats) {
		this.noticeFlats = noticeFlats;
	}
	public String getNoticeType() {
		return noticeType;
	}
	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getSocityId() {
		return socityId;
	}
	public void setSocityId(int socityId) {
		this.socityId = socityId;
	}
	public int getPostById() {
		return postById;
	}
	public void setPostById(int postById) {
		this.postById = postById;
	}
	public int getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(int noticeId) {
		this.noticeId = noticeId;
	}
	public String getNoticeHeading() {
		return noticeHeading;
	}
	public void setNoticeHeading(String noticeHeading) {
		this.noticeHeading = noticeHeading;
	}
	public String getNoticeStartDate() {
		return noticeStartDate;
	}
	public void setNoticeStartDate(String noticeStartDate) {
		this.noticeStartDate = noticeStartDate;
	}
	public String getNoticeEndDate() {
		return noticeEndDate;
	}
	public void setNoticeEndDate(String noticeEndDate) {
		this.noticeEndDate = noticeEndDate;
	}
	public String getNoticeDesc() {
		return noticeDesc;
	}
	public void setNoticeDesc(String noticeDesc) {
		this.noticeDesc = noticeDesc;
	}
	public String getNoticeBanner() {
		return noticeBanner;
	}
	public void setNoticeBanner(String noticeBanner) {
		this.noticeBanner = noticeBanner;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
