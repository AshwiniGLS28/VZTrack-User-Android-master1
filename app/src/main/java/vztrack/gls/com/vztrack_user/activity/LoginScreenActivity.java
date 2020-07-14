package vztrack.gls.com.vztrack_user.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.method.PasswordTransformationMethod;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.multidex.BuildConfig;

import com.ale.infra.manager.call.ITelephonyListener;
import com.ale.infra.manager.call.WebRTCCall;
import com.ale.listener.SigninResponseListener;
import com.ale.listener.SignoutResponseListener;
import com.ale.listener.StartResponseListener;
import com.ale.rainbowsdk.RainbowSdk;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.livechatinc.inappchat.ChatWindowActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

//
import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.application.RainbowApplication;
import vztrack.gls.com.vztrack_user.beans.DrawerConfigBean.DrawerConfigParent;
import vztrack.gls.com.vztrack_user.beans.RainbowDetailsBean;
import vztrack.gls.com.vztrack_user.profile.FamilyBean;
import vztrack.gls.com.vztrack_user.profile.UserBean;
import vztrack.gls.com.vztrack_user.responce.LoginResponse;
import vztrack.gls.com.vztrack_user.responce.RainbowDetailsResponce;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Constants;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.Finals;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.ImNotificationMgr;
import vztrack.gls.com.vztrack_user.utils.PostData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

public class LoginScreenActivity extends BaseActivity {

    EditText etPassword, etUsername;
    public static boolean isPrimaryUser;
    private String strSocietyName, strFlatNo, wingName, strUsername, strPassword;
    private int strSocietyId, strFamilyId;
    private String device_id;
  Button etSubmit;
    LinearLayout LoginLayout,linearLayout1;//, SplashLayout;
    public static boolean splashFlag = true;
    private static final int REQUEST_CODE = 1234;
//    private TextView instr;
    public static boolean primary;
    CheckConnection cc;
    Context context;
    public static String token;
    private ImNotificationMgr imNotificationMgr;
    public String TAG = "Login Screen Activity";
    public static String secondaryUserName;
ImageView imgsplash;
ImageView imgshowpasswaord;
RelativeLayout lllogin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_login_screen);
        linearLayout1=findViewById(R.id.linearLayout1);
        FirebaseApp.initializeApp(this);
        context = this;
        cc = new CheckConnection(context);
        if (cc.isConnectingToInternet()) {
            token = generateFCMid();
        }
        imgshowpasswaord=findViewById(R.id.imgshowpasswaord);
        lllogin=findViewById(R.id.lllogin);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 4;
        SheredPref.setSound(this, "ENABLE");
        SheredPref.setVibration(this, "ENABLE");
        SheredPref.setNotification(this, "ENABLE");
        imgsplash=findViewById(R.id.imgsplash);
        Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
        imgsplash.startAnimation(animFadeIn);

        if (animFadeIn.hasEnded())
        {
            Animation loginanimate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
            linearLayout1.startAnimation(loginanimate);
        }
        LoginLayout = (LinearLayout) findViewById(R.id.linearLayout1);

        etPassword = (EditText) findViewById(R.id.etPassword);
        etUsername = (EditText) findViewById(R.id.etUsername);

        etSubmit =  findViewById(R.id.etSubmit);

        CleverTap.cleverTap_Record_Event(this, Events.event_login_screen);

        String userName = SheredPref.getUsername(this);
        try {
            if (userName.equals("")) {
                etUsername.setText("");
            } else {
                etUsername.setText(userName);
            }
        } catch (Exception ex) {
            Log.e(TAG, " Exception " + ex);
        }

        if (splashFlag == true) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LoginLayout.setVisibility(View.VISIBLE);
                }
            }, 2500);
        } else {
            LoginLayout.setVisibility(View.VISIBLE);
        }

        if (!cc.isConnectingToInternet()) {
            Intent ConnCheck = new Intent(this, NoInternetConnection.class);
            startActivity(ConnCheck);
        }
        try {
            getSupportActionBar().hide();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        imgshowpasswaord.setTag(0);
        imgshowpasswaord.setOnClickListener(v->{
            int tag=Integer.parseInt(v.getTag().toString());
            Log.e("tag",tag+"----");
            if (tag==0) {
                imgshowpasswaord.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_24px));
                imgshowpasswaord.setTag(1);
                etPassword.setTransformationMethod(null);
            }
            else
            {
                imgshowpasswaord.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_off_24px));
                imgshowpasswaord.setTag(0);
                etPassword.setTransformationMethod(new PasswordTransformationMethod());
            }
        });
    }

    private String generateFCMid() {
        return FirebaseInstanceId.getInstance().getToken();
    }

    public void Submit(View v) {
        etSubmit.setEnabled(false);
        if (etUsername.getText().toString().equalsIgnoreCase("")) {
            CommonMethods.showToastError(getApplicationContext(),  "Username should not be blank");
            etSubmit.setEnabled(true);
        } else if (etPassword.getText().toString().equalsIgnoreCase("")) {
            CommonMethods.showToastError(getApplicationContext(),  "Password should not be blank");
            etSubmit.setEnabled(true);
        } else if (!cc.isConnectingToInternet()) {
            UtilityMethodsAndroid.ShowNoInternetToast(this);
            etSubmit.setEnabled(true);
        } else {

            if (cc.isConnectingToInternet()) {
                device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
                setDataForLoginAPIcall();
                etSubmit.setEnabled(true);
            }
        }
    }

    private void setDataForLoginAPIcall() {
        SheredPref.setDateForApi(this, "");
        UserBean userBean = new UserBean();
        userBean.setUser_name(etUsername.getEditableText().toString().trim());
        userBean.setUser_password(etPassword.getEditableText().toString().trim());
        userBean.setUser_dev_id(device_id);
        if(token==null){
            token = generateFCMid();
        }
        userBean.setUser_gcm_id(token);
        userBean.setDevice_os(Finals.AND);
        userBean.setAppVersion(BuildConfig.VERSION_CODE);
        UtilityMethodsAndroid.CloseKeyBoard(this);
        new PostData(new Gson().toJson(userBean), LoginScreenActivity.this, CallFor.LOGIN).execute();
    }

    public void chat(View v) {
        if (cc.isConnectingToInternet()) {
            // CleverTap
            CleverTapAPI cleverTap = null;
            try {
                cleverTap = CleverTapAPI.getInstance(this);
                HashMap<String, Object> loginAction = new HashMap<String, Object>();
                cleverTap.event.push(Events.event_login_chat, loginAction);
            } catch (CleverTapMetaDataNotFoundException e) {
                e.printStackTrace();
            } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
                cleverTapPermissionsNotSatisfied.printStackTrace();
            }
            Intent intent = new Intent(this, com.livechatinc.inappchat.ChatWindowActivity.class);
            intent.putExtra(com.livechatinc.inappchat.ChatWindowActivity.KEY_GROUP_ID, "");
            intent.putExtra(ChatWindowActivity.KEY_LICENCE_NUMBER, "9226615");
            this.startActivity(intent);
        } else {
            UtilityMethodsAndroid.ShowNoInternetToast(LoginScreenActivity.this);
        }
    }

    @Override
    public void onBackPressed() {
       AlertDialog.Builder builder = new AlertDialog.Builder(LoginScreenActivity.this, R.style.AppCompatAlertDialogStyle);
        builder.setMessage("Are you sure exit from application?");
        builder.setPositiveButton("Yes, Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void StartMainActivity() {
//        linearLayout1.setVisibility(View.GONE);
        etUsername.setText("");
        etPassword.setText("");
        Intent intent = new Intent(LoginScreenActivity.this, MainActivity.class);
        intent.putExtra("CALL", "FROM_LOGIN");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        SheredPref.setTutorialFlag(LoginScreenActivity.this, true);
    }

    public void openWebView(View v) {
        CheckConnection ccAccess = new CheckConnection(getApplicationContext());
        Boolean isInternetAvailable = ccAccess.isConnectingToInternet();

        if (isInternetAvailable) {
            Intent I = new Intent(this, WebviewActivity.class);
            I.putExtra("URL", Constants.WEB_LINK);
            startActivity(I);
        } else {
            UtilityMethodsAndroid.ShowNoInternetToast(LoginScreenActivity.this);
        }
    }

    @Override
    public void onGetResponse(String response, String callFor) {

        if (response == null) {
            return;
        }

        if (callFor.equals(CallFor.LOGIN)) {
            Log.e("Rainbow", "Login From Main");
            LoginResponse loginResponse = new Gson().fromJson(response, LoginResponse.class);
            etSubmit.setEnabled(true);
//            try {
                if (loginResponse.getCode().equals("SUCCESS")) {
                    strSocietyName = loginResponse.getSocity().getSocity_name();
                    strFlatNo = loginResponse.getFamily().getFlatNo();
                    wingName = loginResponse.getFamily().getWing();
                    strSocietyId = loginResponse.getFamily().getSocietyId();
                    strFamilyId = loginResponse.getFamily().getFamilyId();
                    strUsername = loginResponse.getFamily().getFlatUserName();
                    strPassword = loginResponse.getFamily().getFlatPassword();
                    isPrimaryUser = loginResponse.getFamily().isPrimary();
                    secondaryUserName = loginResponse.getFamily().getName();
                    SheredPref.setLoginInfo(this, "LoggedIn");
                    SheredPref.setUSername(this, strUsername);
                    SheredPref.setPassword(this, strPassword);
                    SheredPref.setSociety_Name(this, strSocietyName);
                    SheredPref.setFlat_No(this, strFlatNo);
                    SheredPref.setWingName(this, wingName);
                    SheredPref.setSocietyId(this, "" + strSocietyId);
                    SheredPref.setFamilyId(this, "" + strFamilyId);
                    SheredPref.setNotification(this, "ENABLE");
                    SheredPref.setPrimary(this,isPrimaryUser);
                    SheredPref.setName(this,secondaryUserName);
                    SheredPref.setPhotoURLOfUser(this,loginResponse.getFamily().getUserPhoto());
                    UtilityMethodsAndroid.updateSheredPreferenceValues(this, loginResponse);
                    SheredPref.setSocietyApproval(this,loginResponse.getFamily().isApproval());
                    String email = loginResponse.getFamily().getEmail();
                    SheredPref.setDrawerMenuList(context,new Gson().toJson(loginResponse.getConfigBean().getUserAppMenuJsonObject()));
                    ArrayList<DrawerConfigParent> drawerConfigParents = new Gson().fromJson(SheredPref.getDrawerMenuList(context), new TypeToken<ArrayList<DrawerConfigParent>>() {
                    }.getType());
                    Log.e("login_drawerConfigParents",new Gson().toJson(drawerConfigParents)+"");
                    if (email== null || email.equals("")) {
                        if (UtilityMethods.validateEmail(etUsername.getText().toString().trim())) {
                            SheredPref.setUserEmail(this, etUsername.getText().toString().trim());
                        }
                    } else {
                        SheredPref.setUserEmail(this, email);
                    }
                    try {
                        CleverTapAPI cleverTap = CleverTapAPI.getInstance(this);
                        HashMap<String, Object> loginAction = new HashMap<String, Object>();
                        loginAction.put(Events.event_key_society_name, strSocietyName);
                        cleverTap.event.push(Events.event_login_action, loginAction);
                    } catch (CleverTapMetaDataNotFoundException e) {
                        e.printStackTrace();
                    } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
                        cleverTapPermissionsNotSatisfied.printStackTrace();
                    }
                    int rainbowAccount = SheredPref.getRainbowAccountCount(this);

                    if(rainbowAccount == 0){
                        StartMainActivity();
                    }else if(rainbowAccount == 1){
                        if(SheredPref.getAccess_Raibow(this) && loginResponse.getFamily().getRainbowEmailId() != null && !loginResponse.getFamily().getRainbowEmailId().equals("")){
                            startRainbow(loginResponse.getFamily());
                        }else{
                            SheredPref.setRainbowLogin(context, false);
                        }
                        StartMainActivity();
                    }else if(rainbowAccount >= 2){
                        if(SheredPref.getAccess_Raibow(this)){
                            new GetData(this, CallFor.RAINBOW_ACCOUNT_DETAILS, "").execute();
                        }else{
                            StartMainActivity();
                            SheredPref.setRainbowLogin(context, false);
                        }
                    }
                } else if (loginResponse.getCode().equals("ERROR")) {
                    CommonMethods.showToastSuccess(getApplicationContext(),  loginResponse.getMessage());
                } else {
                    CommonMethods.showToastError(getApplicationContext(),  "Please check login details");
                }
        }
        if (callFor.equals(CallFor.RAINBOW_ACCOUNT_DETAILS)) {
            RainbowDetailsResponce rainbowDetailsResponce = new Gson().fromJson(response, RainbowDetailsResponce.class);
            if (rainbowDetailsResponce.getCode().equals("SUCCESS")) {
                showRainbowChooseAccountDialog(this, rainbowDetailsResponce.getRainbowDetailsBeans());
            }else if (rainbowDetailsResponce.getCode().equals("ERROR")) {
                Log.e(TAG," Error Response in getting rainbow account "+rainbowDetailsResponce.getMessage());
            } else {
                Log.e(TAG," Something went wrong");
            }
        }
    }
    private void showRainbowChooseAccountDialog(Context context, ArrayList<RainbowDetailsBean> rainbowDetailsBeans)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.radiobutton_dialog, null);
        dialogBuilder.setView(dialogView);
        List<String> stringList=new ArrayList<>();
        stringList.clear();
        for(int i=0;i<rainbowDetailsBeans.size();i++) {
            stringList.add((i + 1) +" "+rainbowDetailsBeans.get(i).getName());
        }

        RadioGroup rg = (RadioGroup) dialogView.findViewById(R.id.radio_group);
        TextView btnskip=dialogView.findViewById(R.id.btnskip);
        TextView btnlogin=dialogView.findViewById(R.id.btnlogin);

        for(int i=0;i<stringList.size();i++){
            RadioButton rb = (RadioButton) getLayoutInflater().inflate(R.layout.radio_button, null, false);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.setMargins(5, 5, 5, 5);

            rb.setText(stringList.get(i));
            rb.setId(i);
            rg.addView(rb);
        }

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        btnskip.setOnClickListener(v->{
            if(cc.isConnectingToInternet()){
                StartMainActivity();
            }else{
                UtilityMethodsAndroid.ShowNoInternetToast(LoginScreenActivity.this);
            }
            alertDialog.dismiss();
        });
        btnlogin.setOnClickListener(v->{
            FamilyBean familyBean = new FamilyBean();
            int checkedRadioButtonId = rg.getCheckedRadioButtonId();
            if(checkedRadioButtonId == -1){
                CommonMethods.showToastError(getApplicationContext(),  getResources().getString(R.string.select_acct));
            }else if(!cc.isConnectingToInternet()){
                UtilityMethodsAndroid.ShowNoInternetToast(LoginScreenActivity.this);
            }else{
                if(cc.isConnectingToInternet()){
                    alertDialog.dismiss();

                    familyBean.setRainbowEmailId(rainbowDetailsBeans.get(checkedRadioButtonId).getEmailId());
                    familyBean.setRainbowPassword(rainbowDetailsBeans.get(checkedRadioButtonId).getPassword());
                    startRainbow(familyBean);
                    StartMainActivity();
                }else{
                    UtilityMethodsAndroid.ShowNoInternetToast(LoginScreenActivity.this);
                }
            }
        });
    }

    public void startRainbow(final FamilyBean familyBean) {
        if (RainbowSdk.instance().connection().isDisconnected()) {
            RainbowSdk.instance().connection().start(new StartResponseListener() {
                @Override
                public void onStartSucceeded() {
                    loginInRainbow(familyBean);
                }

                @Override
                public void onRequestFailed(RainbowSdk.ErrorCode errorCode, String s) {
                    Log.e("Rainbow ", "CONNECTING ERROR IN Login " + s);
                }
            });
        } else {
            Log.e(" Rainbow ", "ALREADY CONNECTED");
        }
    }

    public void loginInRainbow(final FamilyBean familyBean) {
        String emailId = familyBean.getRainbowEmailId();
        String password = familyBean.getRainbowPassword();
        String host = Constants.RAINBOW_ENVIRONMENT;
        RainbowSdk.instance().connection().signin(emailId, password, host, new SigninResponseListener() {
            @Override
            public void onSigninSucceeded() {
                Log.e("Rainbow", "LOGIN SUCCESS FROM LOGIN ACTIVITY");
                SheredPref.setRainbowEmailId(context, familyBean.getRainbowEmailId());
                SheredPref.setRainbowPassword(context, familyBean.getRainbowPassword());
                SheredPref.setRainbowLogin(context, true);
                RainbowSdk.instance().webRTC().registerTelephonyListener(callListner);
                if (ImNotificationMgr.getInstance()==null){
                    imNotificationMgr = new ImNotificationMgr(context);
                }
            }

            @Override
            public void onRequestFailed(RainbowSdk.ErrorCode errorCode, String s) {
                RainbowSdk.instance().connection().signout(new SignoutResponseListener() {
                    @Override
                    public void onSignoutSucceeded() {
                        Log.e("Rainbow", "LOG LOGOUT SUCCESS");
                    }
                });
                SheredPref.setRainbowLogin(context, false);
            }
        });
    }

    ITelephonyListener callListner = new ITelephonyListener() {
        @Override
        public void onCallAdded(WebRTCCall webRTCCall) {
            startActivity(new Intent(getApplicationContext(), Rainbow_CallActivity.class));
        }

        @Override
        public void onCallModified(WebRTCCall webRTCCall) {
            Log.e(RainbowApplication.TAG,"onCallModified");
        }

        @Override
        public void onCallRemoved(WebRTCCall webRTCCall) {
            Log.e(RainbowApplication.TAG,"onCallRemoved");
        }
    };

}