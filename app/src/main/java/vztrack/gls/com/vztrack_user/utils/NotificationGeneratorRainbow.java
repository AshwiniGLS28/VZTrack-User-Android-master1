package vztrack.gls.com.vztrack_user.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.manager.IMMessage;
import com.ale.infra.manager.room.Room;
import com.ale.rainbowsdk.RainbowSdk;

import java.util.Date;
import java.util.Random;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationGeneratorRainbow {
    Context context;
    private String TAG = "NotificationGeneratorRainbow-->";
    Random random = new Random();
    String msgTextForNotification = "";

    public NotificationGeneratorRainbow(Context context) {
        this.context = context;
    }

    public String generateRoomNotificationText(IMMessage imMessage, Room room) {
        String roomType = imMessage.getRoomEventType().toString();
        if (roomType.equals("invitation")) {
            // If group create the self notification comes
            msgTextForNotification = "";
        } else if (roomType.equals("join")) {
            IRainbowContact contact = RainbowSdk.instance().contacts().getContactFromJabberId(imMessage.getContactJid());
            String senderName = contact.getFirstName() + " " + contact.getLastName();
            msgTextForNotification = senderName + " has joined the group " + room.getName();
        } else if(roomType.equals("leave")){
            msgTextForNotification = "";
        }else {
            String text = "";
            if (imMessage.getMessageContent() != null) {
                text = imMessage.getMessageContent();
            }

            //msgTextForNotification = "You Have New Group Message " + text + " From Group " + room.getName();
            msgTextForNotification =  "Group-"+room.getName() + " : "  + text;

            if (msgTextForNotification != null) {
                //msgTextForNotification = msgTextForNotification.split("\\(Pushed App Stopped\\)")[0];
                //msgTextForNotification = msgTextForNotification.split("\\(Pushed\\)")[0];
            }
        }
        return msgTextForNotification;
    }

    public String generateP2PNotificationText(IMMessage imMessage, IRainbowContact contact) {
        final IMMessage msg = imMessage;
        msg.setMessageDateReceived(new Date());
        String senderName = contact.getFirstName() + " " + contact.getLastName();
        if (msg.getCallLogEvent() != null && msg.getCallLogEvent().getState().equals("missed")) {
            if(msg.isMsgSent()){
                msgTextForNotification = "";
            }else{
                msgTextForNotification = "You missed call from " + senderName + ".";

            }
        }else if(msg.getCallLogEvent() != null && msg.getCallLogEvent().getState().equals("answered")){
            msgTextForNotification = "";
        } else {
            if (msgTextForNotification == null || msgTextForNotification.equals("")) {
                msgTextForNotification = senderName + " : " + imMessage.getMessageContent();
            } else {
                msgTextForNotification = senderName + " : " + imMessage.getMessageContent() + "\n" + msgTextForNotification;
            }
        }
        return msgTextForNotification;
    }

    public void displayNotificationForP2PConversation(IRainbowContact contact, IMMessage imMessage) {
        imMessage.setMessageDateReceived(new Date());
        String notificationSubTitle = generateP2PNotificationText(imMessage, contact);
        if(!notificationSubTitle.equals("")){
            ShowNotification(notificationSubTitle);
        }
    }

    public void displayNotificationForRoomConversation(Room room, IMMessage imMessage) {
        imMessage.setMessageDateReceived(new Date());
        String notificationSubTitle = generateRoomNotificationText(imMessage, room);
        if(!notificationSubTitle.equals("")){
            ShowNotification(notificationSubTitle);
        }
    }

    public void ShowNotification(String notificationSubTitle) {
        {
            Context context = RainbowSdk.instance().getContext();
            int notification = random.nextInt();
            Notification.Builder mBuilder = new Notification.Builder(RainbowSdk.instance().getContext());
            mBuilder.setSmallIcon(R.drawable.ic_stat_vz);
            mBuilder.setContentTitle("VZTrack New Message");
            mBuilder.setContentText(notificationSubTitle);
            Intent intent = new Intent(RainbowSdk.instance().getContext(), MainActivity.class);
            intent.putExtra("NOT_FLAG", "11");
            if(msgTextForNotification.toLowerCase().contains("invites you to join")){
                intent.putExtra("Rainbow_Notification_Type","Invite");
            }
            android.app.TaskStackBuilder tsb = android.app.TaskStackBuilder.create(RainbowSdk.instance().getContext());
            tsb.addParentStack(MainActivity.class);
            tsb.addNextIntent(intent);
            PendingIntent pendingIntent = tsb.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setAutoCancel(true);
            mBuilder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) RainbowSdk.instance().getContext().getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = context.getString(R.string.default_notification_channel_id);
                NotificationChannel channel = new NotificationChannel(channelId, msgTextForNotification, NotificationManager.IMPORTANCE_DEFAULT);
                channel.setName(context.getResources().getString(R.string.channel_name));
                channel.setDescription(msgTextForNotification);
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                channel.setShowBadge(true);
                channel.setLightColor(Color.BLUE);
                channel.enableVibration(true);
                channel.setBypassDnd(true);
                manager.createNotificationChannel(channel);
                mBuilder.setBadgeIconType(Notification.BADGE_ICON_SMALL);
                mBuilder.setChannelId(channelId);
            }
            manager.notify(notification, mBuilder.build());
            playReceivedSound();
            msgTextForNotification = "";
        }
    }

    public void playReceivedSound(){
        try{
            MediaPlayer player = MediaPlayer.create(RainbowSdk.instance().getContext(),
                    Settings.System.DEFAULT_NOTIFICATION_URI);
            player.start();
        } catch (Exception e){
            Log.e(TAG, "Play Notification Tone "+e);
        }
    }
}
