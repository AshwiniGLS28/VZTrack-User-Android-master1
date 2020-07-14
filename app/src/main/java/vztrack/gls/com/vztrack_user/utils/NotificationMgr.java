package vztrack.gls.com.vztrack_user.utils;

import android.util.Log;

import com.ale.infra.contact.Contact;
import com.ale.infra.manager.Conversation;
import com.ale.infra.manager.IImNotificationMgr;
import com.ale.infra.manager.IMMessage;
import com.ale.infra.manager.channel.Channel;
import com.ale.infra.manager.room.Room;
import com.ale.rainbowsdk.RainbowSdk;

class NotificationMgr implements IImNotificationMgr  {
    private NotificationGeneratorRainbow notificationGeneratorRainbow =
            new NotificationGeneratorRainbow(RainbowSdk.instance().getContext());

    @Override
    public void cancelNotification(String s) {

    }

    @Override
    public void cancelAll() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void setImReadState(IMMessage imMessage) {

    }

    @Override
    public void setCurrentDisplayedConversation(Conversation conversation) {

    }

    @Override
    public void unsetCurrentDisplayedConversation(Conversation conversation) {

    }

//    @Override
//    public void addImNotifWithAppStopped(Conversation conversation, Contact contact, IMMessage imMessage) {
//        notificationGeneratorRainbow.displayNotificationForP2PConversation(contact, imMessage);
//    }
//
//    @Override
//    public void addRoomImNotifWithAppStopped(Conversation conversation, Room room, Contact contact, IMMessage imMessage) {
//        notificationGeneratorRainbow.displayNotificationForRoomConversation(room, imMessage);
//    }

    @Override
    public void addImNotifWithAppStopped(Contact contact, IMMessage imMessage) {
        notificationGeneratorRainbow.displayNotificationForP2PConversation(contact, imMessage);
    }

    @Override
    public void addRoomImNotifWithAppStopped(Room room, Contact contact, IMMessage imMessage) {
        notificationGeneratorRainbow.displayNotificationForRoomConversation(room, imMessage);
    }
}
