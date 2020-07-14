package vztrack.gls.com.vztrack_user.beans;

import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.list.ArrayItemList;
import com.ale.infra.proxy.conversation.IRainbowConversation;
import com.google.gson.annotations.SerializedName;

public class ConversationBean {
    @SerializedName("conversations")
    private IRainbowConversation conversation;
    @SerializedName("contacts")
    private IRainbowContact contact;
    @SerializedName("invitationSent")
    private boolean isInvitationSent;

    public IRainbowConversation getConversation() {
        return conversation;
    }

    public void setConversation(IRainbowConversation conversation) {
        this.conversation = conversation;
    }

    public IRainbowContact getContact() {
        return contact;
    }

    public void setContact(IRainbowContact contact) {
        this.contact = contact;
    }

    public boolean isInvitationSent() {
        return isInvitationSent;
    }

    public void setInvitationSent(boolean invitationSent) {
        isInvitationSent = invitationSent;
    }
}
