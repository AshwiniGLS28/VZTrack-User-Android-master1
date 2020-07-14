package vztrack.gls.com.vztrack_user.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.list.ArrayItemList;
import com.ale.infra.manager.IMMessage;
import com.ale.infra.manager.room.Room;
import com.ale.infra.proxy.conversation.IRainbowConversation;
import com.ale.infra.xmpp.xep.Room.RoomMultiUserChatEvent.RoomEventType;
import com.ale.listener.IRainbowInvitationManagementListener;
import com.ale.rainbowsdk.RainbowSdk;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import mehdi.sakout.fancybuttons.FancyButton;
import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.BaseActivity;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.activity.Rainbow_MessageActivity;
import vztrack.gls.com.vztrack_user.beans.ConversationBean;
import vztrack.gls.com.vztrack_user.profile.FamilyBean;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

import static org.webrtc.VideoFrameDrawer.TAG;


public class Rainbow_ConversationRecyclerAdapter extends RecyclerView.Adapter<Rainbow_ConversationRecyclerAdapter.MyViewHolder> {

    private ArrayItemList<ConversationBean> m_conversations = new ArrayItemList<>();
    private ArrayItemList<ConversationBean> main_conversations = new ArrayItemList<>();
    private BaseActivity m_context;
    private List<IRainbowContact> sentInvitations;
    private List<IRainbowContact> pendingInvitations;
    private IRainbowInvitationManagementListener iRainbowInvitationManagementListener;
    private ProgressDialog progressDialog;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView noImgText;
        TextView unReadMessageCountText;
        ImageView dp;
        ImageView onlineStatus;
        TextView details,txtflatno;
        TextView lastMessageDateAndTime;
        GradientDrawable statusShape;
        LinearLayout row;
        LinearLayout acceptRejectLayout;
        FancyButton btn_more_details;
        FancyButton btn_accept;
        FancyButton btn_reject;
        View lineView;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.contactName);
            noImgText = view.findViewById(R.id.noImgText);
            unReadMessageCountText = view.findViewById(R.id.unReadMessageCount);
            dp = view.findViewById(R.id.contactDp);
            txtflatno=view.findViewById(R.id.txtflatno);

            onlineStatus = view.findViewById(R.id.onlineStatus);
            details = view.findViewById(R.id.contactDetail);
            lastMessageDateAndTime = view.findViewById(R.id.lastMessageDateAndTime);
            row = view.findViewById(R.id.row);
            acceptRejectLayout = view.findViewById(R.id.acceptRejectLayout);
            btn_more_details = view.findViewById(R.id.btn_more_details);
            btn_accept = view.findViewById(R.id.btn_accept);
            btn_reject = view.findViewById(R.id.btn_reject);
            lineView = view.findViewById(R.id.view);
        }
    }


    public Rainbow_ConversationRecyclerAdapter(Context context, IRainbowInvitationManagementListener iRainbowInvitationManagementListener) {
        m_context = (BaseActivity) context;
        updateConversations();
        pendingInvitations = getPendingInvitations();
        sentInvitations = getSentInvitations();
        this.iRainbowInvitationManagementListener = iRainbowInvitationManagementListener;
    }

    private List<IRainbowContact> getPendingInvitations() {
//        List<IRainbowContact> myPendingInvitations = new ArrayList<>();
//        for(IRainbowContact contact : RainbowSdk.instance().contacts().getPendingReceivedInvitations()){
//            for(FamilyBean familyBean : rainbowUsersLisToShow){
//                if(familyBean.getRainbowJid()!=null && familyBean.getRainbowJid().equals(contact.getImJabberId())){
//                    myPendingInvitations.add(contact);
//                    Log.e(TAG, "PENDING INVITATIONS : "+contact.getFirstName()+" "+contact.getLastName());
//                }
//            }
//        }
        return RainbowSdk.instance().contacts().getPendingReceivedInvitations();
    }

    private List<IRainbowContact> getSentInvitations() {
//        ArrayItemList<IRainbowContact> sentInvitations = new ArrayItemList<>();
//        for(IRainbowContact contact : RainbowSdk.instance().contacts().getSentInvitations().getCopyOfDataList()){
//            for(FamilyBean familyBean : rainbowUsersLisToShow){
//                if(familyBean.getRainbowJid() != null && familyBean.getRainbowJid().equals(contact.getImJabberId())){
//                    sentInvitations.add(contact);
//                    Log.e(TAG, "SENT INVITATIONS : "+contact.getFirstName()+" "+contact.getLastName());
//                }
//            }
//
//        }
        return RainbowSdk.instance().contacts().getPendingReceivedInvitations();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.conv_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String nameStr="";
        String msgText = null;
        String noProfilePicText = null;
        Bitmap profilePhoto = null;
        String flatno="";

        IRainbowConversation iRainbowConversation = main_conversations.get(position).getConversation();
        if(iRainbowConversation==null){
            holder.onlineStatus.setVisibility(View.GONE);
            holder.lastMessageDateAndTime.setVisibility(View.GONE);
            holder.unReadMessageCountText.setVisibility(View.GONE);
            holder.acceptRejectLayout.setVisibility(View.VISIBLE);
            IRainbowContact iRainbowContact = main_conversations.get(position).getContact();

            nameStr = iRainbowContact.getFirstName() + " " + iRainbowContact.getLastName()+"";
            holder.name.setText(nameStr);
            int colorCode = m_context.getResources().getColor(R.color.pollcolor);
            profilePhoto = iRainbowContact.getPhoto();
            noProfilePicText = UtilityMethodsAndroid.getIntitialLetter(nameStr);

            if (profilePhoto == null) {
                GradientDrawable shape = (GradientDrawable) holder.noImgText.getBackground();
                holder.noImgText.setVisibility(View.VISIBLE);
                holder.dp.setVisibility(View.INVISIBLE);
                shape.setColor(colorCode);
                holder.noImgText.setText(noProfilePicText);
            } else {
                holder.noImgText.setVisibility(View.INVISIBLE);
                holder.dp.setVisibility(View.VISIBLE);
                holder.dp.setImageBitmap(profilePhoto);
            }

            if(main_conversations.get(position).isInvitationSent()){
                holder.details.setText("Invitation Sent");
                holder.btn_accept.setVisibility(View.GONE);
                holder.btn_reject.setVisibility(View.GONE);
                holder.btn_more_details.setVisibility(View.VISIBLE);
            }else{
                holder.details.setText("Received invitation");
                holder.btn_accept.setVisibility(View.VISIBLE);
                holder.btn_reject.setVisibility(View.VISIBLE);
                holder.btn_more_details.setVisibility(View.GONE);
            }

            holder.txtflatno.setVisibility(View.GONE);
            holder.btn_accept.setOnClickListener(v -> {
                progressDialog = ProgressDialog.show(m_context,"","Loading...");
                InvitationAction(iRainbowContact, true);
            });

            holder.btn_reject.setOnClickListener(v -> {
                progressDialog = ProgressDialog.show(m_context,"","Loading...");
                InvitationAction(iRainbowContact, false);
            });

            String finalNameStr = nameStr;
            holder.row.setOnClickListener(view -> {
               CommonMethods.showToastSuccess(m_context,  finalNameStr +" not accepted your invitation, You can not start conversation");
            });
            holder.btn_more_details.setOnClickListener(view -> {
               CommonMethods.showToastSuccess(m_context,  "Invitation response is pending from "+finalNameStr);
            });
        }else{
            holder.txtflatno.setVisibility(View.VISIBLE);
            holder.acceptRejectLayout.setVisibility(View.GONE);
            IMMessage message = iRainbowConversation.getLastMessage();
            int colorCode=m_context.getResources().getColor(R.color.pollcolor);
            int unReadMessageCount = iRainbowConversation.getUnreadMsgNb();
            if (!iRainbowConversation.isRoomType()) {
                IRainbowContact iRainbowContact = main_conversations.get(position).getConversation().getContact();
                nameStr = iRainbowContact.getFirstName() + " " + iRainbowContact.getLastName()+"";
                if(nameStr.trim().equalsIgnoreCase("emily") || nameStr.trim().equalsIgnoreCase("")){
                    holder.row.setVisibility(View.GONE);
                    holder.lineView.setVisibility(View.GONE);
                }else{
                    holder.row.setVisibility(View.VISIBLE);
                    holder.lineView.setVisibility(View.VISIBLE);
                    String senderName = nameStr;
                    String emailId = iRainbowContact.getMainEmailAddress();
                    String flatNumber = UtilityMethods.getFlatNumberFromEmail(emailId);
                    flatno=flatNumber.replace("(","");
                    flatno=flatno.replace(")","");
                    nameStr = nameStr ;//+ flatNumber;
                    msgText = getLastMessageOfP2PConv(message, senderName);
                    String presence = iRainbowContact.getPresence().toString();
                    holder.onlineStatus.setVisibility(View.VISIBLE);
                    holder.statusShape = (GradientDrawable) holder.onlineStatus.getBackground();
                    holder.statusShape.setStroke(2, m_context.getResources().getColor(R.color.fragmentBackground));

                    int color;
                    if (presence.toLowerCase().contains("online")) {
                        color = m_context.getResources().getColor(R.color.green);
                        holder.statusShape.setColor(color);
                    }
                    if (presence.toLowerCase().contains("offline"))
                    {
                        color = m_context.getResources().getColor(R.color.subtextcolor);
                        holder.statusShape.setColor(color);
                    }
                    if (presence.toLowerCase().contains("away"))
                    {
                        color = m_context.getResources().getColor(R.color.yellow);
                        holder.statusShape.setColor(color);
                    }else if (presence.toLowerCase().contains("DoNotDisturb")){
                        color = m_context.getResources().getColor(R.color.nwred);
                        holder.statusShape.setColor(color);
                    }
                    profilePhoto = iRainbowContact.getPhoto();
                    noProfilePicText = UtilityMethodsAndroid.getIntitialLetter(senderName);
                }
            }
            else{
                Room room =  iRainbowConversation.getRoom();
                nameStr = ""+room.getName().trim();
                holder.txtflatno.setVisibility(View.GONE);
                if(nameStr.equals("")){
                    holder.row.setVisibility(View.GONE);
                    holder.lineView.setVisibility(View.GONE);
                }else{
                    holder.row.setVisibility(View.VISIBLE);
                    holder.lineView.setVisibility(View.VISIBLE);
                    IRainbowContact contact = RainbowSdk.instance().contacts().getContactFromJabberId(message.getContactJid());
                    int isEmpty = contact==null?0:1;
                    if(isEmpty == 1){
                        String senderName = contact.getFirstName()+" "+contact.getLastName();
                        msgText = getLastMessageOfRoomConv(message, senderName);
                        holder.onlineStatus.setVisibility(View.GONE);
                        profilePhoto = room.getPhoto();
                        if(!nameStr.equals("")){
                            noProfilePicText = "G-"+nameStr.substring(0,2).toUpperCase();
                        }else{
                            noProfilePicText = "";
                        }
                    }else{
                        holder.onlineStatus.setVisibility(View.GONE);
                        profilePhoto = room.getPhoto();
                        if(!nameStr.equals("")){
                            noProfilePicText = "G-"+nameStr.substring(0,2).toUpperCase();
                        }else{
                            noProfilePicText = "";
                        }
                    }
                }
            }
            holder.name.setText(nameStr);

            if (!flatno.equalsIgnoreCase(""))
            holder.txtflatno.setText(flatno);
            else
                holder.txtflatno.setVisibility(View.GONE);
            if (msgText != null) {
                //msgText = msgText.split("\\(Pushed")[0];
            }
            holder.details.setText(msgText);

            if (unReadMessageCount != 0) {
                holder.unReadMessageCountText.setVisibility(View.VISIBLE);
                holder.unReadMessageCountText.setText("" + unReadMessageCount);
            } else {
                holder.unReadMessageCountText.setVisibility(View.INVISIBLE);
            }

            if (message.getTimeStamp() == 0) {
                holder.lastMessageDateAndTime.setVisibility(View.GONE);
            } else {
                holder.lastMessageDateAndTime.setVisibility(View.VISIBLE);
                holder.lastMessageDateAndTime.setText(timeStampToDate(message.getTimeStamp()));
            }


            if (profilePhoto == null) {
                GradientDrawable shape = (GradientDrawable) holder.noImgText.getBackground();
                holder.noImgText.setVisibility(View.VISIBLE);
                holder.dp.setVisibility(View.INVISIBLE);
                shape.setColor(colorCode);
                holder.noImgText.setText(noProfilePicText);
            } else {
                holder.noImgText.setVisibility(View.INVISIBLE);
                holder.dp.setVisibility(View.VISIBLE);
                holder.dp.setImageBitmap(profilePhoto);
            }

            holder.row.setTag(iRainbowConversation);
            holder.row.setOnClickListener(view -> {
                IRainbowConversation iRainbowConversation1 = (IRainbowConversation) holder.row.getTag();;
                String jabberId = iRainbowConversation1.getJid();
                Intent intent = new Intent(m_context, Rainbow_MessageActivity.class);
                intent.putExtra("ColorCode", colorCode);
                Log.e("jid",jabberId+"--");
                Log.e("roomtype",iRainbowConversation1.isRoomType()+"--");
                Log.e("jid",jabberId+"--");

                if(iRainbowConversation1.isRoomType()){
                    Rainbow_MessageActivity.room = iRainbowConversation1.getRoom();
                    intent.putExtra("JabberId", "");
                }else{
                    Rainbow_MessageActivity.room = null;
                    intent.putExtra("JabberId", jabberId);
                }
                m_context.startActivity(intent);
            });
        }
    }

    private void InvitationAction(IRainbowContact iRainbowContact, boolean isAccepting) {
        if(isAccepting){
            RainbowSdk.instance().contacts().acceptInvitation(iRainbowContact.getInvitationId(), iRainbowInvitationManagementListener);
        }else{
            RainbowSdk.instance().contacts().declineInvitation(iRainbowContact.getInvitationId(), iRainbowInvitationManagementListener);
        }
    }

    public void closeProgressDialog() {
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    private String getLastMessageOfRoomConv(IMMessage message, String senderName) {
        String msgText = message.getMessageContent() == null ? "" : message.getMessageContent();
        if(message.getRoomEventType().equals(RoomEventType.LEAVE)){
            msgText = senderName+" has left the group";
        }else if(message.getRoomEventType().equals(RoomEventType.JOIN)){
            msgText = senderName+" has joined the group";
        }else if(message.getRoomEventType().equals(RoomEventType.INVITATION)){
            msgText = "You invites "+senderName+" to join the group";
        }
        return msgText;
    }

    private String getLastMessageOfP2PConv(IMMessage message, String senderName) {
        String msgText;
        if(message.isFileDescriptorAvailable()){
            if(message.isMsgSent()){
                msgText = "You sent a file ";
            }else {
                msgText = "You received a file ";
            }
            msgText = msgText + message.getFileDescriptor().getFileName();
        }else{
            msgText = message.getMessageContent() == null ? "" : message.getMessageContent();
            if (msgText.equals("")) {
                try {
                    if (message.getCallLogEvent() != null) {
                        if (message.getCallLogEvent().getState().equals("missed")) {
                            if (message.isMsgSent()) {
                                msgText = "You Called " + senderName + ".";
                                msgText = msgText + "\nNot Answered";
                            } else {
                                msgText = "You missed call from " + senderName + ".";
                            }
                        } else {
                            if (message.isMsgSent()) {
                                msgText = "You Called " + senderName + ".";
                            } else {
                                msgText = senderName + " called you.";
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("Exception ", "" + e);
                }
            }
        }


        return msgText;
    }

    @Override
    public int getItemCount() {
        return main_conversations.getCount();
    }

    public String timeStampToDate(long timeStamp) {
        Timestamp timestamp = new Timestamp(timeStamp);
        Date date = new Date(timestamp.getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
        System.out.println(simpleDateFormat.format(timestamp));
        System.out.println(simpleDateFormat.format(date));
        return simpleDateFormat.format(timestamp);
    }

    public void updateConversations() {
        this.main_conversations.clear();
        this.m_conversations.clear();

        addAllPendingAndSentInviteContacts();
        addAllConversation();

        m_conversations.addAll(main_conversations.getCopyOfDataList());
        notifyDataSetChanged();
    }

    private void addAllConversation() {
        for(IRainbowConversation iRainbowConversation : RainbowSdk.instance().conversations().getAllConversations().getCopyOfDataList()){
            ConversationBean conversationBean = new ConversationBean();
            conversationBean.setConversation(iRainbowConversation);
            main_conversations.add(conversationBean);
        }
    }

    private void addAllPendingAndSentInviteContacts() {
        try{
            for(IRainbowContact iRainbowConversation : RainbowSdk.instance().contacts().getPendingReceivedInvitations()){
                ConversationBean conversationBean = new ConversationBean();
                conversationBean.setContact(iRainbowConversation);
                main_conversations.add(conversationBean);
            }

            for(IRainbowContact iRainbowConversation : RainbowSdk.instance().contacts().getPendingSentInvitations()){
                ConversationBean conversationBean = new ConversationBean();
                conversationBean.setContact(iRainbowConversation);
                conversationBean.setInvitationSent(true);
                main_conversations.add(conversationBean);
            }
        }catch (Exception ex){
            Log.e(TAG, "Exception : "+ex);
        }
    }

    public void updateSearchedConversations(String query) {
        this.main_conversations.clear();
        for (int i = 0; i < m_conversations.getCount(); i++) {
            IRainbowConversation conversation = m_conversations.get(i).getConversation();
            String nameStr;
            if(conversation!=null){
                if( conversation.isRoomType()){
                    nameStr = conversation.getRoom().getName();
                    if (nameStr.toLowerCase().contains(query)) {
                        main_conversations.add(m_conversations.get(i));
                    }
                }else{
                    nameStr = m_conversations.get(i).getConversation().getContact().getFirstName() + " " + m_conversations.get(i).getConversation().getContact().getLastName();
                    String emailId = m_conversations.get(i).getConversation().getContact().getMainEmailAddress();
                    String flatNo = UtilityMethods.getFlatNumberFromEmail(emailId);
                    if (nameStr.toLowerCase().contains(query) || flatNo.toLowerCase().contains(query)) {
                        main_conversations.add(m_conversations.get(i));
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public int getColorCode(String id) {
        int colorCode = 0;
        boolean findFlag = false;
        if (MainActivity.hm.size() == 0) {
            int genColorCode = UtilityMethods.getRandomColor();
            MainActivity.hm.put(genColorCode, id);
            colorCode = genColorCode;
        } else {
            for (Map.Entry m : MainActivity.hm.entrySet()) {
                if (m.getValue().equals(id)) {
                    colorCode = (int) m.getKey();
                    findFlag = true;
                    break;
                }
            }
            if (!findFlag) {
                int genColorCode = UtilityMethods.getRandomColor();
                MainActivity.hm.put(genColorCode, id);
                colorCode = genColorCode;
            }
        }
        return colorCode;
    }
}
