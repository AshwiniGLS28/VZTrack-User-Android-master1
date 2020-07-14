package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sandeep on 19/3/18.
 * 
 */
public class PollBean {
	@SerializedName("id")
	private int id;
	@SerializedName("name")
	private String name;
	@SerializedName("createdDate")
	private Date createdDate;
	@SerializedName("closeDate")
	private Date closeDate;
	@SerializedName("question")
	private String question;
	@SerializedName("status")
	private String status;
	@SerializedName("remark")
	private String remark;
	@SerializedName("createdBy")
	private int createdBy;
	@SerializedName("action")
	private String action;
	@SerializedName("totalParticipants")
	private int totalParticipants;
	@SerializedName("isvoted")
	private boolean isvoted;
	@SerializedName("myOptionId")
	private int myOptionId;
	@SerializedName("options")
	private ArrayList<OptionsDetailsBean> options;
	@SerializedName("groupId")
	private int[] groupId;
	@SerializedName("groupBeans")
	private  ArrayList<GroupBeans> groupBeans;
	@SerializedName("groupName")
	String groupName[];

	public String[] getGroupName() {
		return groupName;
	}

	public void setGroupName(String[] groupName) {
		this.groupName = groupName;
	}

	public ArrayList<GroupBeans> getGroupBeans() {
		return groupBeans;
	}

	public void setGroupBeans(ArrayList<GroupBeans> groupBeans) {
		this.groupBeans = groupBeans;
	}

	public int[] getGroupId() {
		return groupId;
	}
	public void setGroupId(int[] groupId) {
		this.groupId = groupId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}



	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getTotalParticipants() {
		return totalParticipants;
	}

	public void setTotalParticipants(int totalParticipants) {
		this.totalParticipants = totalParticipants;
	}

	public boolean isIsvoted() {
		return isvoted;
	}

	public void setIsvoted(boolean isvoted) {
		this.isvoted = isvoted;
	}

	public ArrayList<OptionsDetailsBean> getOptions() {
		return options;
	}

	public int getMyOptionId() {
		return myOptionId;
	}

	public void setMyOptionId(int myOptionId) {
		this.myOptionId = myOptionId;
	}

	public void setOptions(ArrayList<OptionsDetailsBean> options) {
		this.options = options;
	}

}