package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

public class MessageGroupBean {
	@SerializedName("group_id")
	int group_id;
	@SerializedName("group_name")
	String group_name;
	@SerializedName("Society_id")
	String Society_id;

	public int getGroup_id() {
		return group_id;
	}
	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}
	public String getGroup_name() {
		return group_name;
	}
	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}
	public String getSociety_id() {
		return Society_id;
	}
	public void setSociety_id(String society_id) {
		Society_id = society_id;
	}	
}
