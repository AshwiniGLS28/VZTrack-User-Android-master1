package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MessageBean {
	@SerializedName("messageId")
	private int messageId;
	@SerializedName("message")
	private String message;
	@SerializedName("family_id")
	private int[] family_id;
	@SerializedName("flatNo")
	private String flatNo;
	@SerializedName("groupId")
	private int[] groupId;
	@SerializedName("groupName")
	private String[] groupName;
	@SerializedName("sent_date")
	private String sent_date;
	@SerializedName("messageToAll")
	private boolean messageToAll;
	@SerializedName("societyId")
	private String societyId;
	@SerializedName("sentBy")
	private String sentBy;
	@SerializedName("readCount")
	private int readCount;
	@SerializedName("totalCount")
	private int totalCount;
	@SerializedName("familyList")
	public ArrayList<FamilyListBean> familyList;

	public ArrayList<FamilyListBean> getFamilyList() {
		return familyList;
	}

	public void setFamilyList(ArrayList<FamilyListBean> familyList) {
		this.familyList = familyList;
	}

	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getFlatNo() {
		return flatNo;
	}
	public void setFlatNo(String flatNo) {
		this.flatNo = flatNo;
	}	
	public String getSent_date() {
		return sent_date;
	}
	public void setSent_date(String sent_date) {
		this.sent_date = sent_date;
	}
	public int[] getFamily_id() {
		return family_id;
	}
	public void setFamily_id(int[] family_id) {
		this.family_id = family_id;
	}
	public int[] getGroupId() {
		return groupId;
	}
	public void setGroupId(int[] groupId) {
		this.groupId = groupId;
	}
	public String[] getGroupName() {
		return groupName;
	}
	public void setGroupName(String[] groupName) {
		this.groupName = groupName;
	}

	public boolean isMessageToAll() {
		return messageToAll;
	}

	public void setMessageToAll(boolean messageToAll) {
		this.messageToAll = messageToAll;
	}

	public String getSocietyId() {
		return societyId;
	}

	public void setSocietyId(String societyId) {
		this.societyId = societyId;
	}

	public String getSentBy() {
		return sentBy;
	}

	public void setSentBy(String sentBy) {
		this.sentBy = sentBy;
	}

	public int getReadCount() {
		return readCount;
	}

	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}