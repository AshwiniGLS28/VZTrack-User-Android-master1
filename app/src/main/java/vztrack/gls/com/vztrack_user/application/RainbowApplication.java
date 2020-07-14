package vztrack.gls.com.vztrack_user.application;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;

import com.ale.rainbowsdk.RainbowSdk;
import com.clevertap.android.sdk.ActivityLifecycleCallback;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.utils.ImNotificationMgr;
import vztrack.gls.com.vztrack_user.utils.NotificationFactory;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

/**
 * Created by Santosh on 25-Jan-18.
 */

//public class RainbowApplication extends com.clevertap.android.sdk.Application {
public class RainbowApplication extends Application implements LifecycleObserver {
    public final static String TAG = "MainActivity";
    String secretKey = "YCtk2cDw7XG7YPmsrinwUwpI9p9ojQ5mcYDlYWspdjcd6z4stdlcMVTWHIm0Ymy5";
    String applicationId = "582caf70995a11e8af961b0063faf161";
    private Context context;
    private ImNotificationMgr imNotificationMgr;
    public static boolean wasInBackground;
    private static final DefaultHttpClient client = createClient();


    @Override
    public void onCreate() {
        ActivityLifecycleCallback.register(this);
        super.onCreate();
        context = this;
        MainActivity.showInvoiceDueDialog = true;
        RainbowSdk.instance().initialize(this, applicationId, secretKey);
        RainbowSdk.instance().push().activate(RainbowApplication.this);
        RainbowSdk.instance().setNotificationFactory(new NotificationFactory());

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null)
            {
                String channelId = this.getString(R.string.default_notification_channel_id);
                NotificationChannel channel = new NotificationChannel(channelId, "VZTrack Notifications", NotificationManager.IMPORTANCE_HIGH);
                channel.setShowBadge(true);
                notificationManager.createNotificationChannel(channel);
            }
        }
        if (ImNotificationMgr.getInstance()==null){
            imNotificationMgr = new ImNotificationMgr(context);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {
        // app moved to foreground
        wasInBackground=true;
        SheredPref.setAppBackgroundStatus(context, false);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground() {
        // app moved to background
        wasInBackground =false;
        SheredPref.setAppBackgroundStatus(context, true);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        if(imNotificationMgr!=null){
            imNotificationMgr.unRegisterListener();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }




    public static DefaultHttpClient getClient(){
        return client;
    }
    private static DefaultHttpClient createClient(){
        BasicHttpParams params = new BasicHttpParams();
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        final SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
        schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
        ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
        DefaultHttpClient httpclient = new DefaultHttpClient(cm, params);
        httpclient.getCookieStore().getCookies();
        return httpclient;
    }
}
