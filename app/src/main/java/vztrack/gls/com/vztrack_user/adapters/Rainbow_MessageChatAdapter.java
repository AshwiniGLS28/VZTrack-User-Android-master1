package vztrack.gls.com.vztrack_user.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.recyclerview.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.http.GetFileResponse;
import com.ale.infra.list.ArrayItemList;
import com.ale.infra.manager.IMMessage;
import com.ale.infra.manager.fileserver.IFileProxy;
import com.ale.infra.manager.fileserver.RainbowFileDescriptor;
import com.ale.infra.proxy.conversation.IRainbowConversation;
import com.ale.infra.xmpp.xep.Room.RoomMultiUserChatEvent.RoomEventType;
import com.ale.rainbowsdk.RainbowSdk;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.ForwardMsgActivity;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.activity.Rainbow_MessageActivity;
import vztrack.gls.com.vztrack_user.utils.PermissionConstant;
import vztrack.gls.com.vztrack_user.utils.Permissions;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

/**
 * Created by sandeep on 14/3/16.
 */

public class Rainbow_MessageChatAdapter extends RecyclerView.Adapter<Rainbow_MessageChatAdapter.ViewHolder> {
    private IRainbowConversation m_conversation;
    private ArrayItemList<IMMessage> m_messages;
    private boolean messageTypeRoom;
    Context context;
    public Boolean isFileDownloaded;
    public int markedPosition = -1;
    private RelativeLayout selectedLayout;
    private final static String TAG = "MessageChatAdapter";


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView msgContentSent;
        public TextView msgDateSent;
        public TextView msgContentReceived;
        public TextView msgDateReceived;
        public TextView tvSentFileName, tvReceivedFileName, tvSentFileSize, tvReceivedFileSize, msgReceivedSenderName;
        public TextView tvSentProgressPercent, tvReceivedProgressPercent;
        public ImageView imgStatus, imgFileSent, imgFileReceived, imgReceivedDownloadIcon;
        public RelativeLayout sentLayout;
        public RelativeLayout receivedLayout;
        public ProgressBar progressBarRecieved, progressBarSent;
        public LinearLayout ReceivedFileLayout, SentFileLayout, imgReceivedFileInfo, imgSentFileInfo, actionBarForward;

        public ViewHolder(View v) {
            super(v);
            msgContentSent = (TextView) v.findViewById(R.id.msgContentSend);
            msgDateSent = (TextView) v.findViewById(R.id.msgDateSend);
            msgContentReceived = (TextView) v.findViewById(R.id.msgContentReceived);
            msgDateReceived = (TextView) v.findViewById(R.id.msgDateReceived);
            tvSentProgressPercent = (TextView) v.findViewById(R.id.tvSentProgressPercent);
            tvReceivedProgressPercent = (TextView) v.findViewById(R.id.tvRecievedProgressPercent);
            imgStatus = (ImageView) v.findViewById(R.id.deliveryStatus);
            imgFileSent = (ImageView) v.findViewById(R.id.imgFileSent);
            imgFileReceived = (ImageView) v.findViewById(R.id.imgFileReceived);
            imgReceivedDownloadIcon = (ImageView) v.findViewById(R.id.imgReceivedDownloadIcon);
            sentLayout = (RelativeLayout) v.findViewById(R.id.sentLayout);
            receivedLayout = (RelativeLayout) v.findViewById(R.id.receivedLayout);
            progressBarRecieved = (ProgressBar) v.findViewById(R.id.progressBarRecieved);
            progressBarSent = (ProgressBar) v.findViewById(R.id.progressBarSent);
            ReceivedFileLayout = (LinearLayout) v.findViewById(R.id.ReceivedFileLayout);
            SentFileLayout = (LinearLayout) v.findViewById(R.id.SentFileLayout);

            imgReceivedFileInfo = (LinearLayout) v.findViewById(R.id.imgReceivedFileInfo);
            imgSentFileInfo = (LinearLayout) v.findViewById(R.id.imgSendFileInfo);

            tvSentFileName = (TextView) v.findViewById(R.id.tvSentFileName);
            tvReceivedFileName = (TextView) v.findViewById(R.id.tvRecievedFileName);
            tvSentFileSize = (TextView) v.findViewById(R.id.tvSentFileSize);
            tvReceivedFileSize = (TextView) v.findViewById(R.id.tvRecievedFileSize);
            msgReceivedSenderName = (TextView) v.findViewById(R.id.msgReceivedSenderName);
        }
    }

    public Rainbow_MessageChatAdapter(Context context, IRainbowConversation conversation) {
        this.context = context;
        this.m_conversation = conversation;
        this.m_messages = new ArrayItemList<>();
        if (m_conversation.isRoomType()) messageTypeRoom = true;
        else messageTypeRoom = false;
        onMessagesListUpdated();
    }

    @Override
    public Rainbow_MessageChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.sent_and_recived_msg, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        IMMessage message = m_messages.get(position);
        RainbowSdk.instance().im().markMessagesFromConversationAsRead(m_conversation);
        String msgText = message.getMessageContent() == null ? "" : message.getMessageContent();

        //if message having call type information then change message text as per call info
        if (message.isWebRtcEventType()) {
            msgText = setMessageLayoutIfIsCall(msgText, message);
        }

        if (message.isMsgSent()) {
            setMessageSentLayout(holder, msgText, message, position);
            //message having file type information
            if (message.isFileDescriptorAvailable()) {
                setMessageLayoutForFileSend(holder, message);
            }

        } else {
            setMessageReceivedLayout(holder, msgText, message, position);
            //message having file type information
            if (message.isFileDescriptorAvailable()) {
                setMessageLayoutForFileReceived(holder, message);
            }
        }
    }

    private void openFile(File file, String mimeType) {
        Intent newIntent = new Intent(Intent.ACTION_VIEW);
        newIntent.setDataAndType(Uri.fromFile(file), mimeType);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(newIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
        }
    }

    private void downloadFile(ViewHolder holder, RainbowFileDescriptor fileDescriptor, String type) {
        {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS) + "/rainbow/" + fileDescriptor.computeSavedFileName());

            if (file != null && file.exists()) {
                String mimeType = fileDescriptor.getTypeMIME();
                openFile(file, mimeType);
            } else {
                if (type.equals("sent")) {
                    holder.imgSentFileInfo.setVisibility(View.VISIBLE);
                    holder.progressBarSent.getProgressDrawable().setColorFilter(Color.CYAN, PorterDuff.Mode.SRC_IN);
                    holder.progressBarSent.setProgress(0);
                    holder.tvSentProgressPercent.setText("0%");
                } else {
                    holder.imgReceivedFileInfo.setVisibility(View.VISIBLE);
                    holder.progressBarRecieved.getProgressDrawable().setColorFilter(Color.CYAN, PorterDuff.Mode.SRC_IN);
                    holder.progressBarRecieved.setProgress(0);
                    holder.tvReceivedProgressPercent.setText("0%");
                }
                RainbowSdk.instance().fileStorage().downloadFile(fileDescriptor, new IFileProxy.IDownloadFileListener() {
                    @Override
                    public void onDownloadSuccess(GetFileResponse result) {
                        MainActivity.mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (result.isFileFullyDownloaded()) {
                                    isFileDownloaded = true;
                                    CommonMethods.showToastSuccess(context, "File Downloaded");//.show();
                                    if (type.equals("sent")) {
                                        holder.progressBarSent.setProgress(100);
                                        holder.tvSentProgressPercent.setText("100%");
                                        holder.imgSentFileInfo.setVisibility(View.GONE);
                                    } else {
                                        holder.progressBarRecieved.setProgress(100);
                                        holder.tvReceivedProgressPercent.setText("100%");
                                        holder.imgReceivedFileInfo.setVisibility(View.GONE);
                                        holder.imgReceivedDownloadIcon.setVisibility(View.GONE);
                                    }
                                    OpenFileIfExist(fileDescriptor, holder, type);
                                }
                            }
                        });
                    }

                    @Override
                    public void onDownloadInProgress(GetFileResponse result) {
                        int percentDownloaded = result.getPercentDownloaded();
                        MainActivity.mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (type.equals("sent")) {
                                    holder.progressBarSent.setProgress(percentDownloaded);
                                    holder.tvSentProgressPercent.setText(percentDownloaded + "%");
                                } else {
                                    holder.progressBarRecieved.setProgress(percentDownloaded);
                                    holder.tvReceivedProgressPercent.setText(percentDownloaded + "%");
                                }
                            }
                        });
                    }

                    @Override
                    public void onDownloadFailed(boolean notFound) {
                        MainActivity.mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                CommonMethods.showToastError(context, "Error in downloading file");//.show();
                            }
                        });
                    }
                });
            }
        }
    }

    private void setMessageSentLayout(ViewHolder holder, String msgText, IMMessage message, int position) {
        holder.sentLayout.setVisibility(View.VISIBLE);
        holder.receivedLayout.setVisibility(View.GONE);

        holder.imgSentFileInfo.setVisibility(View.GONE); // Progress bar
        holder.imgFileSent.setVisibility(View.GONE);
        holder.tvSentFileName.setVisibility(View.GONE);
        holder.tvSentFileSize.setVisibility(View.GONE);
        holder.SentFileLayout.setVisibility(View.GONE);

        ((ImageView) holder.imgStatus.findViewById(R.id.deliveryStatus)).setVisibility(View.VISIBLE);
        holder.msgContentSent.setText(msgText);
        holder.msgContentSent.setMovementMethod(LinkMovementMethod.getInstance());
        holder.msgDateSent.setText(getFormattedDate(message.getMessageDate()));
        if (message.getCallLogEvent() == null) {
            if (message.getDeliveryState().equals(IMMessage.DeliveryState.SENT)) {
                ((ImageView) holder.imgStatus.findViewById(R.id.deliveryStatus)).setBackground(context.getResources().getDrawable(R.drawable.sent));
            } else if (message.getDeliveryState().equals(IMMessage.DeliveryState.SENT_SERVER_RECEIVED)) {
                ((ImageView) holder.imgStatus.findViewById(R.id.deliveryStatus)).setBackground(context.getResources().getDrawable(R.drawable.sent));
            } else if (message.getDeliveryState().equals(IMMessage.DeliveryState.SENT_CLIENT_RECEIVED)) {
                ((ImageView) holder.imgStatus.findViewById(R.id.deliveryStatus)).setBackground(context.getResources().getDrawable(R.drawable.deliverd));
            } else if (message.getDeliveryState().equals(IMMessage.DeliveryState.SENT_CLIENT_READ)) {
                ((ImageView) holder.imgStatus.findViewById(R.id.deliveryStatus)).setBackground(context.getResources().getDrawable(R.drawable.ic_remove_red_eye));
            } else if (message.getDeliveryState().equals(IMMessage.DeliveryState.UNKNOWN)) {
                ((ImageView) holder.imgStatus.findViewById(R.id.deliveryStatus)).setBackground(context.getResources().getDrawable(R.drawable.ic_access_time_black_24dp));
            }
        } else {
            ((ImageView) holder.imgStatus.findViewById(R.id.deliveryStatus)).setVisibility(View.GONE);
        }

        holder.imgFileSent.setTag(message);
        holder.imgFileSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean storagePermission = Permissions.askPermission(Rainbow_MessageActivity.context, PermissionConstant.PERMISSION_EXTERNAL_STORAGE, PermissionConstant.REQ_CODE_EXTERNAL_STORAGE);
                if (storagePermission) {
                    IMMessage message = (IMMessage) holder.imgFileSent.getTag();
                    if (message.getFileDescriptor() != null && message.isFileDescriptorAvailable()) {
                        RainbowFileDescriptor fileDescriptor = message.getFileDescriptor();
                        OpenFileIfExist(fileDescriptor, holder, "sent");
                    }
                }else{
                    UtilityMethodsAndroid.ShowPermissionToast(context);
                }
            }
        });
        if(markedPosition == position){
            holder.sentLayout.setBackgroundColor(Color.parseColor("#55C0C0C0"));
            holder.receivedLayout.setBackgroundColor(Color.parseColor("#55C0C0C0"));
        }else{
            holder.sentLayout.setBackgroundColor(Color.TRANSPARENT);
            holder.receivedLayout.setBackgroundColor(Color.TRANSPARENT);
        }
        holder.sentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onLongClick(View view) {
                File file;
                int color = Color.parseColor("#55C0C0C0");
                ((Rainbow_MessageActivity)context).visibleForwardMenu();
                if(message !=null && message.isFileDescriptorAvailable()) {
                    boolean storagePermission = Permissions.askPermission(Rainbow_MessageActivity.context, PermissionConstant.PERMISSION_EXTERNAL_STORAGE, PermissionConstant.REQ_CODE_EXTERNAL_STORAGE);
                    if (storagePermission) {
                        IMMessage message = (IMMessage) holder.imgFileSent.getTag();
                        if (message.getFileDescriptor() != null && message.isFileDescriptorAvailable()) {
                            RainbowFileDescriptor fileDescriptor = message.getFileDescriptor();
                            file = new File(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_DOWNLOADS) + "/Rainbow/" + fileDescriptor.computeSavedFileName());
                            if (file != null && file.exists()) {
                                ((Rainbow_MessageActivity)context).forwardBtnImg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intentToFrwdFile = new Intent(context, ForwardMsgActivity.class);
                                        String absolutePath = file.getAbsolutePath();
                                        intentToFrwdFile.putExtra("FilePathToFrwd",absolutePath);
                                        context.startActivity(intentToFrwdFile);
                                        holder.sentLayout.setBackgroundColor(Color.TRANSPARENT);
                                        ((Rainbow_MessageActivity)context).goneForwardMenu(m_conversation);
                                    }
                                });
                            } else {
                                color = Color.TRANSPARENT;
                                ((Rainbow_MessageActivity)context).goneForwardMenu(m_conversation);
                                Toast.makeText(context, "Download the file then you can share", Toast.LENGTH_LONG).show();
                            }
                        }
                    }else{
                        UtilityMethodsAndroid.ShowPermissionToast(context);
                    }
                }else {
                    ((Rainbow_MessageActivity)context).forwardBtnImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intentToFrwdTxt = new Intent(context, ForwardMsgActivity.class);
                            intentToFrwdTxt.putExtra("TextMsgToFrwd",msgText);
                            context.startActivity(intentToFrwdTxt);
                            holder.sentLayout.setBackgroundColor(Color.TRANSPARENT);
                            ((Rainbow_MessageActivity)context).goneForwardMenu(m_conversation);
                        }
                    });
                }
                if(selectedLayout!=null){
                    selectedLayout.setBackgroundColor(Color.TRANSPARENT);
                }
                holder.sentLayout.setBackgroundColor(color);
                selectedLayout =  holder.sentLayout;
                markedPosition = position;
                return true;
            }
        });


    }

    private void setMessageReceivedLayout(ViewHolder holder, String msgText, IMMessage message, int position) {
        String reciveMsg=msgText;
        holder.sentLayout.setVisibility(View.GONE);
        holder.receivedLayout.setVisibility(View.VISIBLE);
        holder.ReceivedFileLayout.setVisibility(View.GONE);
        holder.imgReceivedFileInfo.setVisibility(View.GONE); // Progress bar
        holder.imgFileReceived.setVisibility(View.GONE);
        holder.tvReceivedFileName.setVisibility(View.GONE);
        holder.tvReceivedFileSize.setVisibility(View.GONE);

        if (messageTypeRoom) {
            String senderJid = message.getContactJid();
            IRainbowContact contact = RainbowSdk.instance().contacts().getContactFromJabberId(senderJid);
            String senderName = null;
            if(contact!=null){
                senderName = contact.getFirstName() + " " + contact.getLastName();
            }
            holder.msgReceivedSenderName.setText(senderName);
            holder.msgReceivedSenderName.setVisibility(View.VISIBLE);
        } else {
            holder.msgReceivedSenderName.setVisibility(View.GONE);
        }


        holder.msgDateReceived.setText(getFormattedDate(message.getMessageDate()));
        if (msgText != null) {
            //msgText = msgText.split("\\(Pushed")[0];
        }
        IRainbowContact contact = RainbowSdk.instance().contacts().getContactFromJabberId(message.getContactJid());
        if (message.getRoomEventType().equals(RoomEventType.LEAVE)) {
            msgText = contact.getFirstName() + " " + contact.getLastName() + " has left the group";
        } else if (message.getRoomEventType().equals(RoomEventType.JOIN)) {
            msgText = contact.getFirstName() + " " + contact.getLastName() + " has joined the group";
        } else if (message.getRoomEventType().equals(RoomEventType.INVITATION)) {
            msgText = "You invites " + contact.getFirstName() + " " + contact.getLastName() + " to join the group";
        }
        holder.msgContentReceived.setText(msgText);
        holder.msgContentReceived.setMovementMethod(LinkMovementMethod.getInstance());

        holder.imgFileReceived.setTag(message);
        holder.imgFileReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    boolean storagePermission = Permissions.askPermission(Rainbow_MessageActivity.context, PermissionConstant.PERMISSION_EXTERNAL_STORAGE, PermissionConstant.REQ_CODE_EXTERNAL_STORAGE);
                    if (storagePermission) {
                        IMMessage message = (IMMessage) holder.imgFileReceived.getTag();
                        if (message.getFileDescriptor() != null && message.isFileDescriptorAvailable()) {
                            RainbowFileDescriptor fileDescriptor = message.getFileDescriptor();
                            OpenFileIfExist(fileDescriptor, holder, "received");
                        }
                    }else{
                        UtilityMethodsAndroid.ShowPermissionToast(context);
                    }
                } catch (Exception ex) {
                    CommonMethods.showToastError(context, "Unable to download file");//, Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.imgReceivedDownloadIcon.setTag(message);
        holder.imgReceivedDownloadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IMMessage message = (IMMessage) holder.imgReceivedDownloadIcon.getTag();
                if (message.getFileDescriptor() != null && message.isFileDescriptorAvailable()) {
                    RainbowFileDescriptor fileDescriptor = message.getFileDescriptor();
                    if(fileDescriptor!=null){
                        downloadFile(holder, fileDescriptor, "received");
                    }else{
                        CommonMethods.showToastError(context, "Invalid File");//.show();
                    }
                }
            }
        });

        holder.receivedLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onLongClick(View view) {
                IMMessage message = (IMMessage) holder.imgFileReceived.getTag();
                int color = Color.parseColor("#55C0C0C0");
                if(message!=null && message.isFileDescriptorAvailable()) {
                    boolean storagePermission = Permissions.askPermission(Rainbow_MessageActivity.context, PermissionConstant.PERMISSION_EXTERNAL_STORAGE, PermissionConstant.REQ_CODE_EXTERNAL_STORAGE);
                    if (storagePermission) {
                        if (message.getFileDescriptor() != null && message.isFileDescriptorAvailable()) {
                            RainbowFileDescriptor fileDescriptor = message.getFileDescriptor();
                            File file1 = new File(Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_DOWNLOADS) + "/rainbow/" + fileDescriptor.computeSavedFileName());
                            if (file1 != null && file1.exists()) {
                                ((Rainbow_MessageActivity)context).visibleForwardMenu();
                                ((Rainbow_MessageActivity)context).forwardBtnImg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intentToFrwdFile = new Intent(context, ForwardMsgActivity.class);
                                        String absolutePath = file1.getAbsolutePath();
                                        intentToFrwdFile.putExtra("FilePathToFrwd", absolutePath);
                                        context.startActivity(intentToFrwdFile);
                                        holder.receivedLayout.setBackgroundColor(Color.TRANSPARENT);
                                        ((Rainbow_MessageActivity)context).goneForwardMenu(m_conversation);
                                    }
                                });
                            } else {
                                color = Color.TRANSPARENT;
                                ((Rainbow_MessageActivity)context).goneForwardMenu(m_conversation);
                                Toast.makeText(context, "Download the file then you can share", Toast.LENGTH_LONG).show();
                            }
                        }
                    }else{
                        UtilityMethodsAndroid.ShowPermissionToast(context);
                    }
                }else {
                    ((Rainbow_MessageActivity)context).visibleForwardMenu();
                    holder.receivedLayout.setBackgroundColor(Color.GRAY);
                    ((Rainbow_MessageActivity)context).forwardBtnImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intentToFrwdTxt = new Intent(context, ForwardMsgActivity.class);
                            intentToFrwdTxt.putExtra("TextMsgToFrwd",reciveMsg);
                            context.startActivity(intentToFrwdTxt);
                            holder.receivedLayout.setBackgroundColor(Color.TRANSPARENT);
                            ((Rainbow_MessageActivity)context).goneForwardMenu(m_conversation);
                        }
                    });
                }
                if(selectedLayout!=null){
                    selectedLayout.setBackgroundColor(Color.TRANSPARENT);
                }
                holder.receivedLayout.setBackgroundColor(color);
                selectedLayout =  holder.receivedLayout;
                markedPosition = position;
                return true;
            }
        });
    }

    private void OpenFileIfExist(RainbowFileDescriptor fileDescriptor, ViewHolder holder, String CallFrom) {
        if(fileDescriptor!=null){
            downloadFile(holder, fileDescriptor, CallFrom);
        }else{
            CommonMethods.showToastError(context, "Invalid File");//.show();
        }
    }

    private String setMessageLayoutIfIsCall(String msgText, IMMessage message) {
        try {
            if (message.getCallLogEvent() != null) {

                IRainbowContact iRainbowContact = RainbowSdk.instance().contacts().getContactFromJabberId(message.getContactJid());
                String senderName = iRainbowContact.getFirstName() + " " + iRainbowContact.getLastName();
                if (message.getCallLogEvent().getState().equals("missed")) {
                    if (message.isMsgSent()) {
                        msgText = "You Called " + senderName + ".";
                        msgText = msgText + "\nNot Answered";
                    } else {
                        msgText = "You missed call from " + senderName + ".";
                    }
                } else {
                    if (message.isMsgSent()) {
                        msgText = "You Called " + senderName;
                        msgText = msgText + "\nDuration : " + message.getCallLogEvent().getDuration();
                    } else {
                        msgText = senderName + " called you";
                        msgText = msgText + "\nDuration : " + message.getCallLogEvent().getDuration();
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Exception In ", "Render Message List");
        }
        return msgText;
    }

    private void setMessageLayoutForFileSend(ViewHolder holder, IMMessage message) {
        String strSize = getFileSize(message);
        holder.tvSentFileName.setText(message.getFileDescriptor().getFileName());
        holder.tvSentFileSize.setText(strSize);
        RainbowFileDescriptor fileDescriptor = message.getFileDescriptor();
        holder.SentFileLayout.setVisibility(View.VISIBLE);
        holder.tvSentFileSize.setVisibility(View.VISIBLE);
        holder.tvSentFileName.setVisibility(View.VISIBLE);
        holder.imgFileSent.setVisibility(View.VISIBLE);
        updateThumbnailView(fileDescriptor, holder.imgFileSent);
    }

    private void setMessageLayoutForFileReceived(ViewHolder holder, IMMessage message) {
        String strSize = getFileSize(message);
        holder.tvReceivedFileName.setText(message.getFileDescriptor().getFileName());
        holder.tvReceivedFileSize.setText(strSize);
        RainbowFileDescriptor fileDescriptor = message.getFileDescriptor();
        holder.ReceivedFileLayout.setVisibility(View.VISIBLE);
        holder.tvReceivedFileSize.setVisibility(View.VISIBLE);
        holder.tvReceivedFileName.setVisibility(View.VISIBLE);
        holder.imgFileReceived.setVisibility(View.VISIBLE);
        updateThumbnailView(fileDescriptor, holder.imgFileReceived);
    }

    private void updateThumbnailView(RainbowFileDescriptor fileDescriptor, ImageView imgFile) {
        Bitmap bitmap;
        if (fileDescriptor.isThumbnailAvailable() && fileDescriptor.getThumbnailFile()!=null) {
            try {
                File file = fileDescriptor.getThumbnailFile();
                Uri uri = Uri.fromFile(file);
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                imgFile.setImageBitmap(bitmap);
            } catch (IOException e) {
                setThumbnailIfNotPresnet(fileDescriptor, imgFile);
                e.printStackTrace();
            }
        } else {
            setThumbnailIfNotPresnet(fileDescriptor, imgFile);
        }
    }

    private void setThumbnailIfNotPresnet(RainbowFileDescriptor fileDescriptor, ImageView imgFile) {
        if (fileDescriptor.isImageType()) {
            imgFile.setImageResource(R.drawable.ic_file_image);
        } else if (fileDescriptor.isPdfFileType()) {
            imgFile.setImageResource(R.drawable.ic_file_pdf);
        } else if (fileDescriptor.isDocumentFileType()) {
            imgFile.setImageResource(R.drawable.ic_file_doc);
        } else if (fileDescriptor.isAudioVideoFileType()) {
            imgFile.setImageResource(R.drawable.ic_file_video);
        } else if (fileDescriptor.getExtension() != null && fileDescriptor.getExtension().equals("apk")) {
            imgFile.setImageResource(R.drawable.ic_file_app);
        } else if (fileDescriptor.getExtension() != null && fileDescriptor.getExtension().equals("mp3")) {
            imgFile.setImageResource(R.drawable.ic_file_mp3);
        } else if (fileDescriptor.getExtension() != null && fileDescriptor.getExtension().equals("zip")) {
            imgFile.setImageResource(R.drawable.ic_file_zip);
        } else if (fileDescriptor.getExtension() != null && fileDescriptor.getExtension().equals("text")) {
            imgFile.setImageResource(R.drawable.ic_file_text);
        } else {
            imgFile.setImageResource(R.drawable.ic_file_icon);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return m_messages.getCount();
    }

    private String getFileSize(IMMessage message) {
        String strSize;
        int size = (int) (message.getFileDescriptor().getSize() / (1024 * 1024));
        if (size == 0) {
            size = (int) (message.getFileDescriptor().getSize() / (1024));
            strSize = " (" + size + "kb)";
        } else {
            strSize = " (" + size + "mb)";
        }
        return strSize;
    }

    public String getFormattedDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MMM dd, hh:mm a");
        return format.format(date);
    }

    public void setMessages(ArrayItemList<IMMessage> messages) {
        m_messages.replaceAll(messages.getCopyOfDataList());
    }

    private void updateMessagesList(List<IMMessage> messages) {
        m_messages.clear();
        for (IMMessage message : messages) {
            m_messages.add(message);
        }
    }

    private void onMessagesListUpdated() {
        updateMessagesList(m_conversation.getMessages().getCopyOfDataList());
    }

    public void clearSelection(){
        selectedLayout.setBackgroundColor(Color.TRANSPARENT);
    }
}