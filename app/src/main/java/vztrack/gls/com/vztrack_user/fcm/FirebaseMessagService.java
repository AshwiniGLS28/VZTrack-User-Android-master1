package vztrack.gls.com.vztrack_user.fcm;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.TaskStackBuilder;
import android.util.Log;

import com.ale.infra.manager.call.ITelephonyListener;
import com.ale.infra.manager.call.WebRTCCall;
import com.ale.listener.SigninResponseListener;
import com.ale.listener.StartResponseListener;
import com.ale.rainbowsdk.RainbowSdk;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.NotificationInfo;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.activity.Rainbow_CallActivity;
import vztrack.gls.com.vztrack_user.activity.RatingActivity;
import vztrack.gls.com.vztrack_user.activity.VisitorNotification;
import vztrack.gls.com.vztrack_user.activity.WebviewActivity;
import vztrack.gls.com.vztrack_user.application.RainbowApplication;
import vztrack.gls.com.vztrack_user.beans.NotificationBean;
import vztrack.gls.com.vztrack_user.utils.Constants;
import vztrack.gls.com.vztrack_user.utils.SheredPref;
import vztrack.gls.com.vztrack_user.utils.WakeLocker;

public class FirebaseMessagService extends FirebaseMessagingService {
    public static String NotificationFlag;
    public static long[] vibration = {0, 500, 200, 500};
    public static Uri uri;
    public static String notification_check;
    public static String vibration_check;
    public String title;
    public String message;
    private String photoUrl;
    private String purpose;
    private String visitorId;
    private String temperature;
    private String redirectTo;
    private String webviewActivityName;
    private String webviewUrl;
    private String mask;
    private int showCallScreen;
    private String strVisitorName;
    private NotificationBean notificationBean;
    public static ArrayList<NotificationBean> notificationBeenAll = new ArrayList<NotificationBean>();
    public static int cnt;
    public static int cnt_pro = 0;
    private String result;
    private Notification.Builder builder;
    private Intent intentVisitor = null;
    private Intent intentWebView = null;
    Intent intentNotice = null;
    Intent intentMessage = null;
    Intent intentRating = null;
    Intent intentVehicle = null;
    Intent intentPoll = null;
    Intent intentComplaint = null;
    Intent intentMarketPlace = null;
    Intent intentInvoice = null;
    private String TAG = "FCM-->";
    String senderJid;
    Context context;
    NotificationManager notificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        context = this;
        Log.e(TAG, " Received Notification");
        Map<String, String> data = remoteMessage.getData();
        senderJid = (String) data.get("last-message-sender");
        if (senderJid == null || senderJid.equals("")) {
            AppNotification(remoteMessage);
        }
        else {
            String callAction = (String) data.get("call-action");
            String callId = (String) data.get("call-id");
            String messageType = (String) data.get("last-message-type");
            String messageSender = data.get("message-id");
            if (SheredPref.getAppBackgroundStatus(this) && !SheredPref.getNotificationMessageId(this).equals(messageSender) && messageType.toLowerCase().equals("call") && callId != null && callAction.toLowerCase().equals("propose")) {
                SheredPref.setNotificationMessageId(this, messageSender);
                startRainbow(remoteMessage);
            } else {
                if(!messageType.toLowerCase().equals("call")){
                    int rMsgCnt = SheredPref.getRainbowCount(this);
                    SheredPref.setRainbowCount(this, rMsgCnt+1);
                    RainbowSdk.instance().push().onMessageReceived(remoteMessage.getData());
                }
            }
        }
        super.onMessageReceived(remoteMessage);

    }

    private void AppNotification(RemoteMessage remoteMessage) {
        try {
            if (remoteMessage.getData().size() > 0) {
                Bundle extras = new Bundle();
                for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                    extras.putString(entry.getKey(), entry.getValue());
                }
                NotificationInfo info = CleverTapAPI.getNotificationInfo(extras);
                if (info.fromCleverTap) {
                    CreateCleverTapNotification(extras);
                } else {
                    CreateVZTrackNotification(remoteMessage);
                }
            }
        } catch (Throwable t) {
            Log.d(TAG, "Error parsing FCM message", t);
        }
    }

    private void CreateVZTrackNotification(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        Log.e(TAG, "KEY:"+data.toString());
        WakeLocker.acquire(this.getApplicationContext());
        notification_check = SheredPref.getNotification(this);
        vibration_check = SheredPref.getVibration(this);

        try {
            cnt++;
            try{
                title = data.get("title").toString();
                message = data.get("message").toString();
                redirectTo = data.get("redirect_to").toString();
                webviewUrl = data.get("webview_url").toString();
                webviewActivityName = data.get("webview_activity_title").toString();
            }catch (Exception ex){
               Log.e(TAG, "Exception on getting data "+ex);
            }

            if (title.contains("You have visitor ")) {
                photoUrl = data.get("img_url").toString();
                purpose = data.get("visit_purpose").toString();
                visitorId = data.get("visitor_id").toString();
                temperature = data.get("temperature").toString();
                mask = data.get("mask").toString();
                showCallScreen = Integer.parseInt(data.get("showCallScreen").toString());
                strVisitorName = data.get("visitor_name").toString();
            }
            notificationBean = new NotificationBean();
            if (notification_check.equals("ENABLE")) {
                createNotificationBuilder();
                if (title.contains("You have visitor ")) {
                    NotificationFlag = "1";
                    intentVisitor = new Intent(this, MainActivity.class);
                    intentVisitor.putExtra("NOT_FLAG", NotificationFlag);
                    CreatePendingIntent(this, intentVisitor, MainActivity.class);
                } else if (title.contains("You have notice ")) {
                    int noticeCnt = SheredPref.getNoticeConut(this);
                    noticeCnt = noticeCnt + 1;
                    SheredPref.setNoticeCount(this, noticeCnt);
                    NotificationFlag = "2";
                    intentNotice = new Intent(this, MainActivity.class);
                    intentNotice.putExtra("NOT_FLAG", NotificationFlag);
                    CreatePendingIntent(this, intentNotice, MainActivity.class);
                } else if (title.contains("Please Rate Visited ")) {
                    NotificationFlag = "3";
                    cnt_pro++;
                    result = data.get("object").toString();
                    Gson gson = new Gson();
                    notificationBean = gson.fromJson(result, NotificationBean.class);
                    notificationBeenAll.add(notificationBean);

                    if (cnt_pro == 1) {
                        intentRating = new Intent(this, RatingActivity.class);
                        intentRating.putExtra("VISITOR_NAME", notificationBean.getVisitorName());
                        intentRating.putExtra("VISIT_PURPOSE", notificationBean.getVisitorPurpose());
                        intentRating.putExtra("IN_TIME", notificationBean.getInTime());
                        intentRating.putExtra("MOBILE_NO", notificationBean.getVisitorMobile());
                        intentRating.putExtra("VISITR_PHOTO", notificationBean.getVisitorPhoto());
                        intentRating.putExtra("FROM", "Notification");
                        TaskStackBuilder tsb = TaskStackBuilder.create(this);
                        tsb.addParentStack(RatingActivity.class);
                        tsb.addNextIntent(intentRating);
                        PendingIntent pendingIntent = tsb.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentIntent(pendingIntent);
                    } else {
                        intentRating = new Intent(this, MainActivity.class);
                        CreatePendingIntent(this, intentRating, MainActivity.class);
                    }
                } else if (title.contains("You have new message")) {
                    int msgCnt = SheredPref.getMessageCount(this);
                    SheredPref.setMessageCount(this, ++msgCnt);
                    NotificationFlag = "4";
                    intentMessage = new Intent(this, MainActivity.class);
                    intentMessage.putExtra("NOT_FLAG", NotificationFlag);
                    CreatePendingIntent(this, intentMessage, MainActivity.class);
                } else if (title.contains("Vehicle Alert")) {
                    NotificationFlag = "5";
                    intentVehicle = new Intent(this, MainActivity.class);
                    intentVehicle.putExtra("NOT_FLAG", NotificationFlag);
                    intentVehicle.putExtra("TITLE", title);
                    intentVehicle.putExtra("MESSAGE", message);
                    CreatePendingIntent(this, intentVehicle, MainActivity.class);
                } else if (title.contains("You have a new Poll")) {
                    NotificationFlag = "6";
                    intentPoll = new Intent(this, MainActivity.class);
                    intentPoll.putExtra("NOT_FLAG", NotificationFlag);
                    CreatePendingIntent(this, intentPoll, MainActivity.class);
                } else if (title.contains("New carpool offer") || title.contains("Carpool offer update")) {
                    NotificationFlag = "7";
                    intentPoll = new Intent(this, MainActivity.class);
                    intentPoll.putExtra("NOT_FLAG", NotificationFlag);
                    CreatePendingIntent(this, intentPoll, MainActivity.class);
                } else if (title.contains("New carpool request")) {
                    NotificationFlag = "8";
                    intentPoll = new Intent(this, MainActivity.class);
                    intentPoll.putExtra("NOT_FLAG", NotificationFlag);
                    CreatePendingIntent(this, intentPoll, MainActivity.class);
                } else if (title.contains("New carpool offer")) {
                    NotificationFlag = "6";
                    intentPoll = new Intent(this, MainActivity.class);
                    intentPoll.putExtra("NOT_FLAG", NotificationFlag);
                    CreatePendingIntent(this, intentPoll, MainActivity.class);
                } else if (title.toLowerCase().contains("complaint")) {
                    if (title.toLowerCase().contains("raised a complaint")) {
                        int complaintConut = SheredPref.getComplaintConut(this);
                        complaintConut = complaintConut + 1;
                        SheredPref.setComplaintCount(this, complaintConut);
                    }
                    NotificationFlag = "9";
                    intentComplaint = new Intent(this, MainActivity.class);
                    intentComplaint.putExtra("NOT_FLAG", NotificationFlag);
                    CreatePendingIntent(this, intentComplaint, MainActivity.class);
                } else if (title.toLowerCase().contains("an ad") || title.toLowerCase().contains("marketplace-new comment") || title.toLowerCase().contains("marketplace-new reply")) {
                    NotificationFlag = "10";
                    intentMarketPlace = new Intent(this, MainActivity.class);
                    intentMarketPlace.putExtra("NOT_FLAG", NotificationFlag);
                    CreatePendingIntent(this, intentMarketPlace, MainActivity.class);
                }else if (title.contains("You have new invoice") || title.contains("Due Invoice -")) {
                    NotificationFlag = "12";
                    intentInvoice = new Intent(this, MainActivity.class);
                    intentInvoice.putExtra("NOT_FLAG", NotificationFlag);
                    CreatePendingIntent(this, intentInvoice, MainActivity.class);
                }else {
                    NotificationFlag = "1";
                    intentVisitor = new Intent(this, MainActivity.class);
                    intentVisitor.putExtra("NOT_FLAG", NotificationFlag);
                    CreatePendingIntent(this, intentVisitor, MainActivity.class);
                }

                if(redirectTo != null && redirectTo.equalsIgnoreCase("webview")){
                    NotificationFlag = "13";
                    intentWebView = new Intent(this, MainActivity.class);
                    intentWebView.putExtra("NOT_FLAG", NotificationFlag);
                    intentWebView.putExtra("WEB_URL", webviewUrl);
                    intentWebView.putExtra("WEB_ACTIVITY_NAME", webviewActivityName);
                    CreatePendingIntent(this, intentWebView, MainActivity.class);
                }

                if (vibration_check.equals("ENABLE")) {
                    builder.setVibrate(vibration);
                } else {
                    builder.setVibrate(null);
                }
                notificationManager.notify(cnt, builder.build());
                WakeLocker.release();
            }
        } catch (Exception ex) {
            Log.e(TAG, " Exception While Creating Notification " + ex);
        }
    }

    private void CreateCleverTapNotification(Bundle extras) {
        CleverTapAPI cleverTapAPI = null;
        try {
            cleverTapAPI = CleverTapAPI.getInstance(getApplicationContext());
            cleverTapAPI.createNotification(getApplicationContext(), extras);
        } catch (CleverTapMetaDataNotFoundException e) {
            e.printStackTrace();
        } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
            cleverTapPermissionsNotSatisfied.printStackTrace();
        }
    }

    private Uri getupdatedSoundURI(boolean ringFlag) {
        Uri uri = null;

        //if (SheredPref.getSound(this).equals("ENABLE") && showCallScreen.equals("true")) {
        if (SheredPref.getSound(this).equals("ENABLE") && showCallScreen != 0) {
            if (ringFlag) {
                showVisitorCallLikeActivity(strVisitorName);
            } else {
                String ringtone = SheredPref.getNotificationRingtone(this);
                if (ringtone.equals("")) {
                    uri = getDefaultURI();
                } else {
                    uri = Uri.parse(ringtone);
                }
            }
        }
        return uri;
    }

    private Uri getDefaultURI() {
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }


    private void createNotificationBuilder() {
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_stat_vz);
        builder.setContentTitle(title);
        Bitmap largeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.logo_vz_track);
        builder.setLargeIcon(largeIcon);
        builder.setContentText(message);
        Notification.BigTextStyle bigText = new Notification.BigTextStyle();
        bigText.bigText(message);
        bigText.setBigContentTitle(title);
        bigText.setSummaryText("By : VZTrack");
        builder.setStyle(bigText);
        builder.setAutoCancel(true);

        boolean flag = false;
        if (SheredPref.getMyPhoneRingtoneAsNotificationTone(this) && title.contains("You have visitor")) {
            flag = true;
        }

        boolean visitorFlag = false;
        if (SheredPref.getMyPhoneRingtoneAsNotificationTone(this) && title.contains("You have visitor") && showCallScreen !=0) {
            visitorFlag = true;
        }

        builder.setSound(getupdatedSoundURI(flag));

        String channelId = SheredPref.getNotificationChannelId(this);
        if (channelId.equals("")) {
            channelId = getString(R.string.default_notification_channel_id);
        }
        setChannelIdToBuilder(notificationManager, builder, channelId, visitorFlag);
    }

    private void setChannelIdToBuilder(NotificationManager mNotificationManager, Notification.Builder notificationCompatBuilder, String channelId, boolean visitorCallLikeTone) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (mNotificationManager != null && !visitorCallLikeTone) {
                notificationCompatBuilder.setChannelId(channelId);
            }
        }
    }

    public void CreatePendingIntent(Context context, Intent intent, Class aClass) {
        TaskStackBuilder tsb = TaskStackBuilder.create(context);
        tsb.addParentStack(aClass);
        tsb.addNextIntent(intent);
        PendingIntent pendingIntent = tsb.getPendingIntent(cnt, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
    }

    ITelephonyListener callListner = new ITelephonyListener() {
        @Override
        public void onCallAdded(WebRTCCall webRTCCall) {
            try {
                Intent intent = new Intent(FirebaseMessagService.this, Rainbow_CallActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } catch (Exception ex) {
                Log.e(TAG, " EX " + ex);
            }
        }

        @Override
        public void onCallModified(WebRTCCall webRTCCall) {
            Log.e(RainbowApplication.TAG, "onCallModified");
        }

        @Override
        public void onCallRemoved(WebRTCCall webRTCCall) {
            Log.e(RainbowApplication.TAG, "onCallRemoved");
            RainbowSdk.instance().webRTC().unregisterTelephonyListener(callListner);
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("NOT_FLAG", "1");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    };

    public void startRainbow(RemoteMessage remoteMessage) {
        Log.e(TAG," STATE : --> "+RainbowSdk.instance().connection().getState());
        if (RainbowSdk.instance().connection().isDisconnected()) {
            Log.e(TAG, "CONNECTING FROM FCM");
            RainbowSdk.instance().connection().start(new StartResponseListener() {
                @Override
                public void onStartSucceeded() {
                    loginInRainbow(remoteMessage);
                }

                @Override
                public void onRequestFailed(RainbowSdk.ErrorCode errorCode, String s) {
                    Log.e(TAG, " ERROR RAINBOW LOGIN");
                }
            });
        } else {
            RegisterListenerAndInitiateCall(remoteMessage);
            Log.e(TAG, "ALREADY CONNECTED FROM FCM");
        }
    }

    private void RegisterListenerAndInitiateCall(RemoteMessage remoteMessage) {
        RainbowSdk.instance().push().onMessageReceived(remoteMessage.getData());
        RainbowSdk.instance().webRTC().registerTelephonyListener(callListner);
    }

    public void loginInRainbow(RemoteMessage remoteMessage) {
        String rainbowEmail = SheredPref.getRainbowEmailId(this);
        String rainbowPassword = SheredPref.getRainbowPassword(this);
        RainbowSdk.instance().connection().signin(rainbowEmail, rainbowPassword, Constants.RAINBOW_ENVIRONMENT, new SigninResponseListener() {
            @Override
            public void onSigninSucceeded() {
                Log.e(TAG, "LOG IN SUCCESS IN FCM");
                RegisterListenerAndInitiateCall(remoteMessage);
            }

            @Override
            public void onRequestFailed(RainbowSdk.ErrorCode errorCode, String s) {
                Log.e(TAG, "LOG IN ERROR IN FCM " + s);
            }
        });
    }

    private void showVisitorCallLikeActivity(String strVisitorName) {
        Intent intent = new Intent(this, VisitorNotification.class);
        intent.putExtra("VISITOR_NAME", strVisitorName);
        intent.putExtra("VISITOR_PHOTO", photoUrl);
        intent.putExtra("VISITOR_PURPOSE", purpose);
        intent.putExtra("VISITOR_ID", visitorId);
        intent.putExtra("IS_APPROVE", showCallScreen);
        intent.putExtra("TEMPERATURE", temperature);
        intent.putExtra("MASK", mask);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
    }


    private boolean applicationInForeground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> services = activityManager.getRunningAppProcesses();
        boolean isActivityFound = false;
        if (services.get(0).processName
                .equalsIgnoreCase(getPackageName()) && services.get(0).importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
            isActivityFound = true;
        }
        return isActivityFound;
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }
}