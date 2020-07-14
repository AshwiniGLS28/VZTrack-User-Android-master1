package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MessageResponceBean {
	@SerializedName("code")
	String code;
	@SerializedName("message")
	String message;
	@SerializedName("messageBeans")
	ArrayList<MessageBean> messageBeans = new ArrayList<>();
	@SerializedName("messageGroupBeans")
	ArrayList<MessageGroupBean> messageGroupBeans = new ArrayList<MessageGroupBean>();

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
	
	
}
