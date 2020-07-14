package vztrack.gls.com.vztrack_user.responce;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.beans.MessageBean;
import vztrack.gls.com.vztrack_user.beans.MessageDetailBean;
import vztrack.gls.com.vztrack_user.beans.MessageGroupBean;

public class MessageResponceBean {
	@SerializedName("code")
	String code;
	@SerializedName("message")
	String message;
	@SerializedName("messageBeans")
	ArrayList<MessageBean> messageBeans = new ArrayList<MessageBean>();
	@SerializedName("messageGroupBeans")
	ArrayList<MessageGroupBean> messageGroupBeans = new ArrayList<MessageGroupBean>();
	@SerializedName("messageDetailBeans")
	ArrayList<MessageDetailBean> messageDetailBeans = new ArrayList<MessageDetailBean>();

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ArrayList<MessageBean> getMessageBeans() {
		return messageBeans;
	}
	public void setMessageBeans(ArrayList<MessageBean> messageBeans) {
		this.messageBeans = messageBeans;
	}
	public ArrayList<MessageGroupBean> getMessageGroupBeans() {
		return messageGroupBeans;
	}
	public void setMessageGroupBeans(ArrayList<MessageGroupBean> messageGroupBeans) {
		this.messageGroupBeans = messageGroupBeans;
	}

	public ArrayList<MessageDetailBean> getMessageDetailBeans() {
		return messageDetailBeans;
	}

	public void setMessageDetailBeans(ArrayList<MessageDetailBean> messageDetailBeans) {
		this.messageDetailBeans = messageDetailBeans;
	}
}
