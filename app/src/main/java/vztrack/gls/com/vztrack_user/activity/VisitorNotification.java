package vztrack.gls.com.vztrack_user.activity;

import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.io.IOException;


import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.beans.UserBean;
import vztrack.gls.com.vztrack_user.responce.CommonResponse;
import vztrack.gls.com.vztrack_user.responce.LoginResponse;
import vztrack.gls.com.vztrack_user.responce.NotificationMenuResponseBean;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Constants;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.LoadImage;
import vztrack.gls.com.vztrack_user.utils.PostData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

public class VisitorNotification extends BaseActivity {

    private MediaPlayer mp;
    private String TAG = "VISIOTR_NOTIFICATION_ACTIVITY";
    private KeyguardManager myKM;
    private boolean screenLock = false;
    private Uri notification;
    private int callingFor;
    private String visitorId;
    private int loginApiCount = 0;
    private final int APPROVE = 1, DENAY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_notification);
//        setContentView(R.layout.activity_notification);

        TextView visitor_name = findViewById(R.id.visitor_name);
        LinearLayout view_visitor = findViewById(R.id.view_visitor);
        TextView view_visit_purpose = findViewById(R.id.visit_purpose);
        ImageView view_visitor_photo = findViewById(R.id.visitor_photo);
        TextView temperature = findViewById(R.id.temperature);
        TextView mask = findViewById(R.id.mask);
        LinearLayout close = findViewById(R.id.close);
        TextView txtcancel=findViewById(R.id.txtcancel);
        TextView txtok=findViewById(R.id.txtok);
        ImageView imgok=findViewById(R.id.imgok);

        String strVisitorName = getIntent().getStringExtra("VISITOR_NAME");
        String strVisitorPhoto = getIntent().getStringExtra("VISITOR_PHOTO");
        String strVisitPurpose = getIntent().getStringExtra("VISITOR_PURPOSE");
        visitorId = getIntent().getStringExtra("VISITOR_ID");
        int isApprove = getIntent().getIntExtra("IS_APPROVE", 1);
        String strTemperature = getIntent().getStringExtra("TEMPERATURE");
        String strMask = getIntent().getStringExtra("MASK");

        if(strTemperature == null || strTemperature.equals("") || strTemperature.equals("NA")){
            temperature.setVisibility(View.GONE);
        }else{
            temperature.setText("Temperature : "+strTemperature);
        }

        if(strMask == null || strMask.equals("") || strMask.equals("NA")){
            mask.setVisibility(View.GONE);
        }else{
            mask.setText("Mask : "+strMask);
        }

        visitor_name.setText(strVisitorName);
        view_visit_purpose.setText("Purpose : "+strVisitPurpose);
        strVisitorPhoto = Constants.BASE_IMG_URL + "/" + strVisitorPhoto;

        myKM = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
        if (myKM.inKeyguardRestrictedInputMode()) {
            screenLock = true;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        getWindow().addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | LayoutParams.FLAG_DISMISS_KEYGUARD
                | LayoutParams.FLAG_TURN_SCREEN_ON
                | LayoutParams.FLAG_KEEP_SCREEN_ON);

        String strRingtone = SheredPref.getPhoneNotificationRingtone(this);
        notification = Uri.parse(strRingtone);

        startActivityFor30Sec();

        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (!screenLock && mNotificationManager.getCurrentInterruptionFilter() != NotificationManager.INTERRUPTION_FILTER_PRIORITY) {
                playSoundForXSeconds(notification);
            }
        } else {
            playSoundForXSeconds(notification);
        }

        if (isApprove == 2) {
            txtok.setText("Approve");
            txtcancel.setText("Deny");
            imgok.setImageDrawable(getResources().getDrawable(R.drawable.approvenew));
        }else
        {
            txtok.setText("View Details");
            txtcancel.setText("Close");
            imgok.setImageDrawable(getResources().getDrawable(R.drawable.ic_view_details));
        }

        Glide.with(this)
                .load(strVisitorPhoto)
                .thumbnail(0.4f)
//                .placeholder(R.drawable.contact)
                .into(view_visitor_photo);

        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isApprove == 2) {
                    DenayApiCall();
                }else {
                    closeActivity();
                }
            }
        });

        view_visitor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isApprove == 2) {
                    ApproveApiCall();
                }else{
                    ViewVisitorDetails();

                }
            }
        });
    }

    private void ViewVisitorDetails() {
        try {
            Intent intent = new Intent(VisitorNotification.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("NOT_FLAG", "1");
            startActivity(intent);
            closeActivity();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void startActivityFor30Sec() {
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                try {
                    stopMusicPlayer();
                    closeActivity();
                } catch (Exception e) {
                    Log.e(TAG, " EX " + e);
                }
            }
        }, 30 * 1000);
    }

    @Override
    public void onGetResponse(String response, String callFor) {
        Log.e("RESP", " " + response);
        if (response == null) {
            return;
        }

        if (callFor.equals(CallFor.LOGIN)) {
            loginApiCount++;
            LoginResponse loginResponse = new Gson().fromJson(response, LoginResponse.class);
            try {
                if (loginResponse.getCode().equals("SUCCESS")) {
                    if(callingFor == APPROVE){
                        ApproveApiCall();
                    }else if(callingFor == DENAY){
                        DenayApiCall();
                    }
                }else if(loginResponse.getCode().equals("ERROR")){
                    Log.e(TAG, "Login Error");
                } else {
                    Log.e(TAG, "Error In Login:"+loginResponse.getMessage());
                }
            } catch (Exception ex) {
                Log.e(TAG, "Exception" + ex);
            }
        }

        if (callFor.equals(CallFor.UPDATE_VISITOR_STATUS)) {
            CommonResponse commonResponse = new Gson().fromJson(response, CommonResponse.class);
            try {
                if (commonResponse.getCode().equals("SUCCESS")) {
                    Log.e(TAG, "Updated Status Successfully");
                        ViewVisitorDetails();
                } else if (commonResponse.getCode().equals("NEED_LOGIN")) {
                    if(loginApiCount == 2){
                        return;
                    }
                   LoginApiCall();
                } else {
                    ViewVisitorDetails();
                    Log.e(TAG, "Error In Updating Status");
                }
            } catch (Exception ex) {
                Log.e("Ex Getting Details ", " " + ex);
            }
        }

        if (callFor.equals(CallFor.ERROR_ENTRY)) {
            CommonResponse commonResponse = new Gson().fromJson(response, CommonResponse.class);
            try {
                if (commonResponse.getCode().equals("SUCCESS")) {
                    Log.e(TAG, "Mark Error Successfully");
//                    new GetData(VisitorNotification.this, CallFor.UPDATE_VISITOR_STATUS, "?visitorId=" + visitorId + "&status=UD").execute();
                    new GetData(VisitorNotification.this, CallFor.UPDATE_VISITOR_STATUS, "?visitorId=" + visitorId + "&status=DEN").execute();
                } else if (commonResponse.getCode().equals("NEED_LOGIN")) {
                    if(loginApiCount == 2){
                        return;
                    }
                    LoginApiCall();
                } else {
                    Log.e(TAG, "Error In Marking Error");
                }
            } catch (Exception ex) {
                Log.e("Ex Getting Details ", " " + ex);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (screenLock) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }, 300);

            if (VERSION.SDK_INT >= VERSION_CODES.M) {
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (mNotificationManager.getCurrentInterruptionFilter() != NotificationManager.INTERRUPTION_FILTER_PRIORITY) {
                    playSoundForXSeconds(notification);
                }
            } else {
                playSoundForXSeconds(notification);
            }
        }
    }

    private void stopMusicPlayer() {
        if (mp != null && mp.isPlaying()) {
            mp.stop();
        }
    }

    private void closeActivity() {
        stopMusicPlayer();
        finishActivity();
    }

    private void playSoundForXSeconds(final Uri soundUri) {
        if (soundUri != null) {
            mp = new MediaPlayer();
            startMusicPlayer(soundUri);
        }
    }


    private void startMusicPlayer(Uri soundUri) {
        setVolumeControlStream(AudioManager.STREAM_NOTIFICATION);
        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_RING);
        try {
            mp.setDataSource(this, soundUri);
            mp.prepare();
            if (!mp.isPlaying()) {
                mp.start();
            }

        } catch (IOException e) {
            Log.e(TAG, " IOException " + e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopMusicPlayer();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeActivity();
    }

    public void finishActivity() {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }

    public void LoginApiCall(){
        UserBean userBean = new UserBean();
        userBean.setUser_name(SheredPref.getUsername(this));
        userBean.setUser_password(SheredPref.getPassword(this));
        new PostData(new Gson().toJson(userBean), this, CallFor.LOGIN).execute();
    }

    public void DenayApiCall(){
        SheredPref.setRun(VisitorNotification.this, "RUN");
        callingFor = DENAY;
        CleverTap.cleverTap_Record_Event(this, Events.action_deny);
        new GetData(VisitorNotification.this, CallFor.UPDATE_VISITOR_STATUS, "?visitorId=" + visitorId + "&status=DEN").execute();
    }

    public void ApproveApiCall(){
        SheredPref.setRun(VisitorNotification.this, "RUN");
        callingFor = APPROVE;
        CleverTap.cleverTap_Record_Event(this, Events.action_approve);
        new GetData(VisitorNotification.this, CallFor.UPDATE_VISITOR_STATUS, "?visitorId=" + visitorId + "&status=APP").execute();
    }
}
