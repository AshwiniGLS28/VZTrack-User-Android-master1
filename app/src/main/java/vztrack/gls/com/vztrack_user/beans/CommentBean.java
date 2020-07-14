package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class CommentBean implements Serializable {
    @SerializedName("commentId")
    private int commentId;
    @SerializedName("comment_for")
    private String comment_for;    //e.g complain
    @SerializedName("commentForId")
    private int commentForId;    //complain id
    @SerializedName("familyId")
    private int familyId;
    @SerializedName("flatNo")
    private String flatNo;
    @SerializedName("parentComment")
    private int parentComment;    //parent comment id
    @SerializedName("commentText")
    private String commentText;
    @SerializedName("commentDate")
    private String commentDate;
    @SerializedName("isAlive")
    private int isAlive;
    @SerializedName("commentedBy")
    private String commentedBy;
    @SerializedName("replies")
    private ArrayList<CommentBean> replies;

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getComment_for() {
        return comment_for;
    }

    public void setComment_for(String comment_for) {
        this.comment_for = comment_for;
    }

    public int getCommentForId() {
        return commentForId;
    }

    public void setCommentForId(int commentForId) {
        this.commentForId = commentForId;
    }

    public int getFamilyId() {
        return familyId;
    }

    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }

    public int getParentComment() {
        return parentComment;
    }

    public void setParentComment(int parentComment) {
        this.parentComment = parentComment;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public int getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(int isAlive) {
        this.isAlive = isAlive;
    }

    public String getCommentedBy() {
        return commentedBy;
    }

    public void setCommentedBy(String commentedBy) {
        this.commentedBy = commentedBy;
    }

    public ArrayList<CommentBean> getReplies() {
        return replies;
    }

    public void setReplies(ArrayList<CommentBean> replies) {
        this.replies = replies;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }
}
