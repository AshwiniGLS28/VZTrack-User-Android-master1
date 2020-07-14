package vztrack.gls.com.vztrack_user.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.manager.IMMessage;
import com.ale.infra.proxy.conversation.IRainbowConversation;
import com.ale.listener.IRainbowImListener;
import com.ale.rainbowsdk.RainbowSdk;

import java.util.Date;
import java.util.List;

import vztrack.gls.com.vztrack_user.activity.MainActivity;

/**
 * Created by sandeep on 13/3/18.
 */

public class ImNotificationMgr implements IRainbowImListener {
    private Context context;
    private String TAG = "ImNotificationMgr-->";
    private NotificationGeneratorRainbow notificationGeneratorRainbow;
    private static ImNotificationMgr imNotificationMgr = null;

    public ImNotificationMgr(){

    }

    public static ImNotificationMgr getInstance() {
        return imNotificationMgr;
    }


    public ImNotificationMgr(Context context) {
        this.context = context;
        unRegisterListener();
        notificationGeneratorRainbow = new NotificationGeneratorRainbow(context);
        RainbowSdk.instance().im().registerListener(this);
        imNotificationMgr = this;
    }

    @Override
    public void onImReceived(String conversationId, IMMessage imMessage) {
        Log.e(TAG," Received Notification When App Is In Foreground");
        int rMsgCnt = SheredPref.getRainbowCount(context);
        SheredPref.setRainbowCount(context, rMsgCnt+1);
        IRainbowConversation conversation = RainbowSdk.instance().conversations().getConversationFromId(conversationId);
        if (conversation != null) {
            setupNotificationData(conversation, imMessage);
        }
    }

    private void setupNotificationData(IRainbowConversation conversation, IMMessage imMessage) {
        final IMMessage msg = imMessage;
        msg.setMessageDateReceived(new Date());
        if (MainActivity.rainbowFragmentFlag) {
            playReceivedSoundWhenAppFroground();
        } else {
            if (conversation.isRoomType()) {
                Log.e(TAG, "ROOM Type");
                notificationGeneratorRainbow.displayNotificationForRoomConversation(conversation.getRoom(), imMessage);
            } else {
                Log.e(TAG, "P2P Type");
                notificationGeneratorRainbow.displayNotificationForP2PConversation(conversation.getContact(), imMessage);
            }
        }
    }

    @Override
    public void onImSent(String s, IMMessage message) {

    }

    @Override
    public void isTypingState(IRainbowContact other, boolean isTyping, String roomId) {

    }

    @Override
    public void onMessagesListUpdated(int i, String s, List<IMMessage> list) {

    }

    @Override
    public void onMoreMessagesListUpdated(int i, String s, List<IMMessage> list) {

    }

    public void playReceivedSoundWhenAppFroground() {
        try {
            MediaPlayer mp;
            mp = MediaPlayer.create(context.getApplicationContext(), context.getResources().getIdentifier("stairs", "raw", context.getPackageName()));
            mp.start();

        } catch (Exception e) {
            Log.e("Exception:", "ImNotification Impossible to get the default ringtone", e);
        }
    }

    public void unRegisterListener() {
        RainbowSdk.instance().im().unregisterListener(this);
        imNotificationMgr = null;
    }

    public void registerListener() {
        RainbowSdk.instance().im().registerListener(this);
    }
}
