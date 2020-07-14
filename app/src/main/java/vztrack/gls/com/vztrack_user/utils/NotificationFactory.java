package vztrack.gls.com.vztrack_user.utils;

import android.util.Log;

import com.ale.infra.contact.IContactCacheMgr;
import com.ale.infra.manager.ChatMgr;
import com.ale.infra.manager.IImNotificationMgr;
import com.ale.infra.manager.INotificationFactory;
import com.ale.infra.xmpp.XMPPWebSocketConnection;
import com.ale.infra.xmpp.XmppConnection;

public class NotificationFactory implements INotificationFactory {
    private String TAG = "Notification_Factory";
    @Override
    public IImNotificationMgr getIMNotificationMgr() {
        return new NotificationMgr();
    }

//    @Override
//    public IMissedCallNotificationMgr getMissedCallNotificationMgr() {
//        return null;
//    }

    @Override
    public void createIMNotificationMgr(XmppConnection xmppConnection, ChatMgr chatMgr, IContactCacheMgr iContactCacheMgr, XMPPWebSocketConnection xmppWebSocketConnection) {
        Log.e(TAG," Create Notification Manager method");
    }

    @Override
    public void stopIMNotificationMgr() {
        Log.e(TAG," Stop Notification Manager method");
    }

//    @Override
//    public void stopMissedNotificationMgr() {

//    }
}
