package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

public class NotificationBean {
	@SerializedName("gcmId")
	private String gcmId;
	@SerializedName("deviceOs")
	private String deviceOs;
	@SerializedName("visitorName")
	private String visitorName;
	@SerializedName("visitorMobile")
	private String visitorMobile;
	@SerializedName("visitorPurpose")
	private String visitorPurpose;
	@SerializedName("visitorPhoto")
	private String visitorPhoto;
	@SerializedName("inTime")
	private String inTime;

	public String getVisitorName() {
		return visitorName;
	}

	public void setVisitorName(String visitorName) {
		this.visitorName = visitorName;
	}

	public void setGcmId(String gcmId) {
		this.gcmId = gcmId;
	}

	public String getDeviceOs() {
		return deviceOs;
	}

	public void setDeviceOs(String deviceOs) {
		this.deviceOs = deviceOs;
	}

	public String getVisitorPhoto() {
		return visitorPhoto;
	}

	public void setVisitorPhoto(String visitorPhoto) {
		this.visitorPhoto = visitorPhoto;
	}

	public String getGcmId() {
		return gcmId;
	}
	public String getVisitorMobile() {
		return visitorMobile;
	}
	public void setVisitorMobile(String visitorMobile) {
		this.visitorMobile = visitorMobile;
	}
	public String getVisitorPurpose() {
		return visitorPurpose;
	}
	public void setVisitorPurpose(String visitorPurpose) {
		this.visitorPurpose = visitorPurpose;
	}
	public String getInTime() {
		return inTime;
	}
	public void setInTime(String inTime) {
		this.inTime = inTime;
	}
}
