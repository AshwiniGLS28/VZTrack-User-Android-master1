package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ComplainsBean{
	@SerializedName("socity_id")
	int socity_id;
	@SerializedName("family_id")
	int family_id;
	@SerializedName("category")
	String category;
	@SerializedName("description")
	String description;
	@SerializedName("status")
	String status;
	@SerializedName("created_date")
	String created_date;
	@SerializedName("modify_date")
	String modify_date;
	@SerializedName("seen_date")
	String seen_date;
	@SerializedName("closed_by")
	String closed_by;
	@SerializedName("close_by_id")
	int close_by_id;
	@SerializedName("vz_comp_id")
	int vz_comp_id;
	@SerializedName("comment")
	String comment;
	@SerializedName("flatNo")
	String flatNo;
	@SerializedName("ownerName")
	String ownerName;
	@SerializedName("estimate_date")
	String estimate_date;
	@SerializedName("comments")
	ArrayList<CommentBean> comments;
	@SerializedName("newVersion")
	boolean newVersion;
	@SerializedName("reOpenComment")
	private String reOpenComment;
	@SerializedName("reopenById")
	private int reopenById;
	@SerializedName("reopenDate")
	private String reopenDate;
	@SerializedName("reopenName")
	private String reopenName;
	@SerializedName("complainPhoto")
	private String complainPhoto;

	
	public int getVz_comp_id() {
		return vz_comp_id;
	}
	public void setVz_comp_id(int vz_comp_id) {
		this.vz_comp_id = vz_comp_id;
	}
	public int getSocity_id() {
		return socity_id;
	}
	public void setSocity_id(int socity_id) {
		this.socity_id = socity_id;
	}
	
	public int getFamily_id() {
		return family_id;
	}
	public void setFamily_id(int family_id) {
		this.family_id = family_id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreated_date() {
		return created_date;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	public String getModify_date() {
		return modify_date;
	}
	public void setModify_date(String modify_date) {
		this.modify_date = modify_date;
	}
	public String getSeen_date() {
		return seen_date;
	}
	public void setSeen_date(String seen_date) {
		this.seen_date = seen_date;
	}
	public String getClosed_by() {
		return closed_by;
	}
	public void setClosed_by(String closed_by) {
		this.closed_by = closed_by;
	}
	public int getClose_by_id() {
		return close_by_id;
	}
	public void setClose_by_id(int close_by_id) {
		this.close_by_id = close_by_id;
	}


	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getFlatNo() {
		return flatNo;
	}

	public void setFlatNo(String flatNo) {
		this.flatNo = flatNo;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getEstimate_date() {
		return estimate_date;
	}

	public void setEstimate_date(String estimate_date) {
		this.estimate_date = estimate_date;
	}

	public ArrayList<CommentBean> getComments() {
		return comments;
	}

	public void setComments(ArrayList<CommentBean> comments) {
		this.comments = comments;
	}

	public boolean isNewVersion() {
		return newVersion;
	}

	public void setNewVersion(boolean newVersion) {
		this.newVersion = newVersion;
	}

	public String getReOpenComment() {
		return reOpenComment;
	}

	public void setReOpenComment(String reOpenComment) {
		this.reOpenComment = reOpenComment;
	}

	public int getReopenById() {
		return reopenById;
	}

	public void setReopenById(int reopenById) {
		this.reopenById = reopenById;
	}

	public String getReopenDate() {
		return reopenDate;
	}

	public void setReopenDate(String reopenDate) {
		this.reopenDate = reopenDate;
	}

	public String getReopenName() {
		return reopenName;
	}

	public void setReopenName(String reopenName) {
		this.reopenName = reopenName;
	}

	public String getComplainPhoto() {
		return complainPhoto;
	}

	public void setComplainPhoto(String complainPhoto) {
		this.complainPhoto = complainPhoto;
	}
}
