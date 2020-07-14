package vztrack.gls.com.vztrack_user.activity;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import com.ale.infra.contact.Contact;
import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.list.ArrayItemList;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.multidex.BuildConfig;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ale.infra.manager.call.ITelephonyListener;
import com.ale.infra.manager.call.WebRTCCall;
import com.ale.listener.SigninResponseListener;
import com.ale.listener.SignoutResponseListener;
import com.ale.listener.StartResponseListener;
import com.ale.rainbowsdk.RainbowSdk;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.adapters.AdminPoll_RecyclerViewAdapter;
import vztrack.gls.com.vztrack_user.adapters.CarPoolOffer_RecyclerViewAdapter;
import vztrack.gls.com.vztrack_user.adapters.CarPoolRequest_RecyclerViewAdapter;
import vztrack.gls.com.vztrack_user.adapters.Complain_RecyclerViewAdapter;
import vztrack.gls.com.vztrack_user.adapters.MarketPlace_RecyclerViewAdapter;
import vztrack.gls.com.vztrack_user.adapters.Search_Provider_RecyclerViewAdapter;
import vztrack.gls.com.vztrack_user.adapters.Search_Vehicles_RecyclerViewAdapter;
import vztrack.gls.com.vztrack_user.adapters.UserPoll_RecyclerViewAdapter;
import vztrack.gls.com.vztrack_user.adapters.VehicleListAdapter;
import vztrack.gls.com.vztrack_user.beans.CarPoolBean;
import vztrack.gls.com.vztrack_user.beans.ComplainsBean;
import vztrack.gls.com.vztrack_user.beans.DataObjectMessage;
import vztrack.gls.com.vztrack_user.beans.DataObjectVisitors;
import vztrack.gls.com.vztrack_user.beans.DrawerConfigBean.ConfigBean;
import vztrack.gls.com.vztrack_user.beans.DrawerConfigBean.DrawerConfigParent;
import vztrack.gls.com.vztrack_user.beans.OutputBeanSearchProvider;
import vztrack.gls.com.vztrack_user.beans.RainbowDetailsBean;
import vztrack.gls.com.vztrack_user.beans.ResponceBean;
import vztrack.gls.com.vztrack_user.fragment.AddvehicleFragment;
import vztrack.gls.com.vztrack_user.fragment.ApprovalFragment;
import vztrack.gls.com.vztrack_user.fragment.CarPoolFragment;
import vztrack.gls.com.vztrack_user.fragment.ComplaintFragment;
import vztrack.gls.com.vztrack_user.fragment.CreateEditPollsFragment;
import vztrack.gls.com.vztrack_user.fragment.DomesticHelpFragment;
import vztrack.gls.com.vztrack_user.fragment.ExtraFieldFragmentOne;
import vztrack.gls.com.vztrack_user.fragment.HomeScreenVisitorFragment;
import vztrack.gls.com.vztrack_user.fragment.InvoiceFragment;
import vztrack.gls.com.vztrack_user.fragment.LocalStoreFragment;
import vztrack.gls.com.vztrack_user.fragment.MarketPlaceFragment;
import vztrack.gls.com.vztrack_user.fragment.MessageFragment;
import vztrack.gls.com.vztrack_user.fragment.NoInternetFragment;
import vztrack.gls.com.vztrack_user.fragment.NoticesFragment;
import vztrack.gls.com.vztrack_user.fragment.Nw_service_provider_fragment;
import vztrack.gls.com.vztrack_user.fragment.OfferRideFragment;
import vztrack.gls.com.vztrack_user.fragment.PollsFragment;
import vztrack.gls.com.vztrack_user.fragment.Rainbow_Fragment;
import vztrack.gls.com.vztrack_user.fragment.RatingFragment;
import vztrack.gls.com.vztrack_user.fragment.RequestRideFragment;
import vztrack.gls.com.vztrack_user.fragment.SearchProviderFragment;
import vztrack.gls.com.vztrack_user.fragment.SearchVehicleFragment;
import vztrack.gls.com.vztrack_user.fragment.SettingFragment;
import vztrack.gls.com.vztrack_user.fragment.SupportFragment;
import vztrack.gls.com.vztrack_user.fragment.VehicleServicing;
import vztrack.gls.com.vztrack_user.fragment.VehiclesFragment;
import vztrack.gls.com.vztrack_user.fragment.VisitorsFragment;
import vztrack.gls.com.vztrack_user.profile.FamilyBean;
import vztrack.gls.com.vztrack_user.profile.UserBean;
import vztrack.gls.com.vztrack_user.profile.VisitorsArray;
import vztrack.gls.com.vztrack_user.responce.CarPoolOfferResponceBean;
import vztrack.gls.com.vztrack_user.responce.CarPoolRequestResponceBean;
import vztrack.gls.com.vztrack_user.responce.ComplainResponceBean;
import vztrack.gls.com.vztrack_user.responce.DomesticHelpResponceBean;
import vztrack.gls.com.vztrack_user.responce.LocalStoreResponse;
import vztrack.gls.com.vztrack_user.responce.LoginResponse;
import vztrack.gls.com.vztrack_user.responce.LogoutResponse;
import vztrack.gls.com.vztrack_user.responce.MarketPlaceResponceBean;
import vztrack.gls.com.vztrack_user.responce.MessageResponceBean;
import vztrack.gls.com.vztrack_user.responce.NoticesResponse;
import vztrack.gls.com.vztrack_user.responce.PollResponce;
import vztrack.gls.com.vztrack_user.responce.RainbowDetailsResponce;
import vztrack.gls.com.vztrack_user.responce.RainbowUserResponse;
import vztrack.gls.com.vztrack_user.responce.RainbowUsersListResponse;
import vztrack.gls.com.vztrack_user.responce.RatingResponseBean;
import vztrack.gls.com.vztrack_user.responce.VehicleResponce;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.CleverTapRegisterEvents;
import vztrack.gls.com.vztrack_user.utils.Constants;
import vztrack.gls.com.vztrack_user.utils.DbHelper;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.Finals;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.ImNotificationMgr;
import vztrack.gls.com.vztrack_user.utils.PermissionConstant;
import vztrack.gls.com.vztrack_user.utils.Permissions;
import vztrack.gls.com.vztrack_user.utils.PostData;
import vztrack.gls.com.vztrack_user.utils.RecyclerViewType;
import vztrack.gls.com.vztrack_user.utils.ServerConnection;
import vztrack.gls.com.vztrack_user.utils.SheredPref;
import vztrack.gls.com.vztrack_user.utils.URL;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

import static vztrack.gls.com.vztrack_user.BuildConfig.BASE_URL;

public class MainActivity extends BaseActivity {

    ImageView notification_indicator;
    ImageView imgsplash;
    public int slidestate = 0;
    public static final int REQUEST_PREAPPROVAL = 100;
    public static FragmentTransaction ft;
    public static NoticesResponse noticesResponse;
    public static MessageResponceBean messageResponceBean;
    public static ArrayList<DataObjectMessage> updatedMessageResponceBean = new ArrayList<>();
    public static VisitorsArray visitorsArray = null;
    public static ComplainResponceBean complainResponceBean = null;
    public static VehicleResponce vehicleResponce = null;
    //    public static ArrayList<RatingBean> ratingBeanArrayList = new ArrayList<RatingBean>();
    public ArrayList<OutputBeanSearchProvider> outputBeanSearchProviders = new ArrayList<OutputBeanSearchProvider>();
    public static int fragment_flag = 0;
    public static BaseActivity mainActivity;
    public static String redirectToScreen;
    public static String webUrl;
    public static String webViewActivityTitle;
    public static int showFlag;
    public static FrameLayout NoVisiterLayout;
    public static LinearLayout llwelcomeuser;
    public static ArrayList<DataObjectVisitors> Updated_result = new ArrayList<>();
    public static ArrayList<ComplainsBean> complian_result = new ArrayList<>();
    public static int visitor_PageNo = 0;
    public static int message_PageNo = 0;
    public static int backPressFlag = 1;
    public static String RATING_FLAG = "0";
    public static String[] prov_list;
    public static boolean isNoticeZero;
    public static int pageNo = 0;
    public static PollResponce adminPollResponce;
    public static int dialogFragment;
    public static boolean callFromAdmin = false;
    public static String carPoolFragmentName = "";
    public static DomesticHelpResponceBean domesticHelpResponceBean;
    public static MarketPlaceResponceBean marketPlaceResponceBean;
    public static ArrayList<FamilyBean> rainbowUsers;
    public static ArrayList<ArrayList<FamilyBean>> rainbowUsersList;
    public static boolean rainbowFragmentFlag;
    public static boolean logoutStatusFlag = false;
    public static HashMap<Integer, String> hm = new HashMap<Integer, String>();
    public static boolean invitationNotification;
    public FragmentManager fragmentManager = getSupportFragmentManager();
    public ImageView imgHiddenImage;
    public boolean adminAccess;
    static String title = "";
    public static int sentMessage = 2;
    CheckConnection cc;
    DbHelper dbHelper;
    LinearLayout splashLayout;
    DrawerLayout drawer;
    String strSplashCheck = "";
    boolean doubleBackToExitPressedOnce = false;
    static int drFlag = 0;
    String date;
    int appVersion;
    Button btnSOS;
    boolean type;
    boolean complaint, localStores, isPollAccess;
    String shareBody;
    public static boolean callFromRainbowProfile;
    private TextView tvFlatno, tvSocietyName, tvWing, tvFLatOwnerName, NoVisitorText;
    private String strValidation;
    private String TAG = "MainActivity";
    private String TAG_RESP = "Main Activity Responce";
    private PollResponce pollResponce;
    boolean due = false;
    public static boolean showInvoiceDueDialog;
    public static boolean isVisitorAccessMainScreen;
    public static boolean isPrimaryUser, isComapny;
    public static ArrayList<FamilyBean> adminList = new ArrayList<>();
    public static ArrayList<FamilyBean> userList = new ArrayList<>();
    public static ArrayList<FamilyBean> vendorList = new ArrayList<>();

    public List<String> groupList = new ArrayList<String>();
    public List<Integer> groupIDList = new ArrayList<Integer>();
    ArrayAdapter<String> adapter;

    public ArrayList<ConfigBean> drawerConfigs = new ArrayList<>();
    ImageView imgCall, imgsetting, splashimage, imgvzlogo;
    LinearLayout llsplashicon;
    LinearLayout noImageDataLayoutmain;
    static Context context;
    RecyclerView recyclerView;
    boolean isUpdateAlertShow = false;
    private boolean isShowPermissionsDialog = false;
    boolean isCameFromPermission = false;

    ITelephonyListener callListner = new ITelephonyListener() {
        @Override
        public void onCallAdded(WebRTCCall webRTCCall) {
            startActivity(new Intent(getApplicationContext(), Rainbow_CallActivity.class));
        }

        @Override
        public void onCallModified(WebRTCCall webRTCCall) {
        }

        @Override
        public void onCallRemoved(WebRTCCall webRTCCall) {
        }
    };

    public static void ShowErrorAlert() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.custum_alert_dialog, null);
        dialogBuilder.setView(dialogView);
        TextView txtalertheading = dialogView.findViewById(R.id.txtalertheading);
        TextView txtalertsubheading = dialogView.findViewById(R.id.txtalertsubheading);
        TextView btnegative = dialogView.findViewById(R.id.btnegative);
        TextView btnpositive = dialogView.findViewById(R.id.btnpositive);
        txtalertheading.setText("Oops...");
        txtalertsubheading.setText("Check your internet connection");
        btnegative.setVisibility(View.GONE);
        final AlertDialog b = dialogBuilder.create();
        b.setCanceledOnTouchOutside(false);
        b.setCancelable(false);
        b.show();

        btnpositive.setOnClickListener(v -> b.dismiss());
        btnegative.setOnClickListener(v -> b.dismiss());
    }

    public static void logShopCall(final String shopId) {
        class LogCall extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] params) {
                String url = Constants.BASE_URL + URL.STORE_CALL_LOG + "?storeId=" + shopId;
                String result = ServerConnection.giveResponse(url, "", null);
                return null;
            }
        }
        new LogCall().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                redirectToScreen = "0";
            } else {
                strSplashCheck = extras.getString("CALL") == null ? "" : extras.getString("CALL");
                redirectToScreen = extras.getString("NOT_FLAG") == null || extras.getString("NOT_FLAG").equals("") ? "0" : extras.getString("NOT_FLAG");
                webUrl = extras.getString("WEB_URL");
                webViewActivityTitle = extras.getString("WEB_ACTIVITY_NAME");
                Log.e("redirectToScreen", redirectToScreen + "-"+webUrl+ "-"+webViewActivityTitle);
            }
        } catch (Exception ex) {
            Log.e(TAG, " Exception In Getting Intent Values " + ex);
        }
        SheredPref.setExecuteOffline(getApplication(), "Yes");
        SheredPref.setExecute(getApplication(), "");
        notification_indicator = findViewById(R.id.notification_indicator);
        imgvzlogo = findViewById(R.id.imgvzlogo);
        llsplashicon = findViewById(R.id.llsplashicon);
        splashimage = findViewById(R.id.splashimage);
        noImageDataLayoutmain = findViewById(R.id.noImageDataLayoutmain);
        visitor_PageNo = 0;
        backPressFlag = 1;
        NoVisiterLayout = (FrameLayout) findViewById(R.id.main);
        NoVisitorText = (TextView) findViewById(R.id.NoVistorScreen);
        llwelcomeuser = (LinearLayout) findViewById(R.id.llWelcomeUser);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        if (SheredPref.getNotificationAccessDialogFlag(this) && !hasNotificationAccess()) {
            showOpenNotificationSettingsDialog();
        } else {
            ShowDialogRequestingForPermissions();
        }
        mainActivity = MainActivity.this;
        adminAccess = SheredPref.getAdminAccess(mainActivity);
        dbHelper = new DbHelper(this);
        imgCall = findViewById(R.id.imgCall);
        imgsetting = findViewById(R.id.imgsetting);
        imgHiddenImage = (ImageView) findViewById(R.id.imgHiddenImage);
        splashLayout = (LinearLayout) findViewById(R.id.splash);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 4;
        crossfade();

        tvFlatno = (TextView) findViewById(R.id.tvFLatNo);
        tvFLatOwnerName = (TextView) findViewById(R.id.tvFLatOwnerName);
        tvSocietyName = (TextView) findViewById(R.id.tvSocietyName);
        tvWing = (TextView) findViewById(R.id.tvWing);
        btnSOS = findViewById(R.id.btnSOS);

        tvSocietyName.setSelected(true);
        tvWing.setSelected(true);
        tvFlatno.setSelected(true);
        tvFLatOwnerName.setSelected(true);
        if (SheredPref.isPrimary(this) == true) {
            tvFLatOwnerName.setText(SheredPref.getOwnerName(this));
        } else {
            tvFLatOwnerName.setText(SheredPref.getName(this));
        }
        type = SheredPref.getType(this);
        // If type true then this is Company
        if (!type) {
            tvFlatno.setText("House No. " + SheredPref.getFlat_No(this));
            tvWing.setText(SheredPref.getWingName(this));
            shareBody = "\t\t\t" + getResources().getString(R.string.app_name) + " - Smart Digital Solution to manage visitors for societies and commercial complex.\n" +
                    "-----------------------------------------------\n" +
                    "What do we provide to get started?" +
                    "\n1. Training to security guards" +
                    "\n2. Multiple user login per house" +
                    "\n3. Ongoing support & maintenance " +
                    "\n4. Instant notification on owner's mobile with visitors details" +
                    "\n5. Analytics and Reports of visitors visited the premise" +
                    "\n\nContact us : \n" +
                    "Call us: " + getResources().getString(R.string.mobile_number) + "\n" +
                    "Email us: " + getResources().getString(R.string.email_id) + "\n" +
                    "Website : " + getResources().getString(R.string.website) + "\n";
        } else {
            tvFlatno.setText("Employee No. " + SheredPref.getFlat_No(this));
            tvWing.setText("Department : " + SheredPref.getWingName(this));

            shareBody = "\t\t\tVZTrack - Smart Digital Solution to manage visitors for commercial complex.\n" +
                    "--------------------------------------\n" +
                    "What do we provide to get started in commercial complex?" +
                    "\n3. User login for employee" +
                    "\n4. Ongoing support & maintenance " +
                    "\n5. Instant notification on employee's mobile with visitors details" +
                    "\n6. Analytics and Reports of visitors visited the premise" +
                    "\n\nContact us : \n" +
                    "Call us: " + getResources().getString(R.string.mobile_number) + "\n" +
                    "Email us: " + getResources().getString(R.string.email_id) + "\n" +
                    "Website : " + getResources().getString(R.string.website) + "\n";
        }

        tvSocietyName.setText(SheredPref.getSociety_Name(this));
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        loadGridData();
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        slidestate = newState;
                    }
                }
        );

        SheredPref.setRun(mainActivity, "DONT RUN");

        if (strSplashCheck.equals("")) {
            if (redirectToScreen.equals("1") || redirectToScreen.equals("0")) {
                drFlag = 0;
                if (RATING_FLAG.equals("0")) {
                    splashLayout.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            splashLayout.setVisibility(View.INVISIBLE);
                            drawer.setVisibility(View.VISIBLE);
                            SheredPref.setRun(mainActivity, "RUN");
                        }
                    }, 2500);
                } else {
                    SheredPref.setRun(mainActivity, "RUN");
                    splashLayout.setVisibility(View.INVISIBLE);
                    drawer.setVisibility(View.VISIBLE);
                }
            }
            if (redirectToScreen.equals("2")) {
                drFlag = 1;
                if (RATING_FLAG.equals("0")) {
                    splashLayout.setVisibility(View.VISIBLE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            splashLayout.setVisibility(View.INVISIBLE);
                            drawer.setVisibility(View.VISIBLE);
                            SheredPref.setRun(mainActivity, "RUN");
                        }
                    }, 2500);
                } else {
                    SheredPref.setRun(mainActivity, "RUN");
                    splashLayout.setVisibility(View.INVISIBLE);
                    drawer.setVisibility(View.VISIBLE);
                }
            }
        } else if (strSplashCheck.equals("FROM_LOGIN") || redirectToScreen.equals("3")) {
            SheredPref.setRun(mainActivity, "RUN");
            splashLayout.setVisibility(View.INVISIBLE);
            drawer.setVisibility(View.VISIBLE);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // this disables the animation
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        if (redirectToScreen.equalsIgnoreCase("5") || redirectToScreen.equalsIgnoreCase("2") || redirectToScreen.equalsIgnoreCase("3") || redirectToScreen.equalsIgnoreCase("4") ||
                redirectToScreen.equalsIgnoreCase("6") || redirectToScreen.equalsIgnoreCase("7") || redirectToScreen.equalsIgnoreCase("8") || redirectToScreen.equalsIgnoreCase("9") ||
                redirectToScreen.equalsIgnoreCase("10") || redirectToScreen.equalsIgnoreCase("11") || redirectToScreen.equalsIgnoreCase("12") || redirectToScreen.equalsIgnoreCase("1")) {
            if (drawer != null)
                drawer.closeDrawer(GravityCompat.START);
        } else {
            if (drawer != null)
                drawer.openDrawer(GravityCompat.START);
        }
        imgCall.setOnClickListener(v -> {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            showCallDialog();
        });
        imgsetting.setOnClickListener(v -> {
            title = "Settings";
            getSupportActionBar().setTitle(title);
            drawer.closeDrawer(GravityCompat.START);
            if (drFlag != 6) {
                if (cc.isConnectingToInternet()) {
                    title = "Settings";
                    NoVisiterLayout.setVisibility(View.VISIBLE);
                    llwelcomeuser.setVisibility(View.GONE);
                    ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.main, new SettingFragment(), "Data").commitAllowingStateLoss();
                } else {
                    title = "Settings";
                    ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.main, new SettingFragment(), "Data").commitAllowingStateLoss();
                }
                drFlag = 6;
            }
        });
        btnSOS.setOnClickListener(view -> {
            drawer.closeDrawers();
            if (cc.isConnectingToInternet()) {
                //New Pop up alert
                android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.custum_alert_dialog, null);
                dialogBuilder.setView(dialogView);
                TextView txtalertheading = dialogView.findViewById(R.id.txtalertheading);
                TextView txtalertsubheading = dialogView.findViewById(R.id.txtalertsubheading);

                TextView btnegative = dialogView.findViewById(R.id.btnegative);
                TextView btnpositive = dialogView.findViewById(R.id.btnpositive);

                txtalertheading.setText("Need Help");
                txtalertsubheading.setText("Raise an alarm with security?");

                btnegative.setVisibility(View.VISIBLE);


                final android.app.AlertDialog b = dialogBuilder.create();
                b.setCanceledOnTouchOutside(false);
                b.setCancelable(false);
                b.show();

                btnpositive.setText("Ok");
                btnegative.setText("Cancel");
                btnpositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        b.dismiss();
                        if (cc.isConnectingToInternet()) {
                            FamilyBean familyBean = new FamilyBean();
                            familyBean.setSocietyId(Integer.parseInt(SheredPref.getSocietyId(mainActivity)));
                            familyBean.setFamilyId(Integer.parseInt(SheredPref.getFamilyId(mainActivity)));
                            familyBean.setWing(SheredPref.getWingName(mainActivity));
                            familyBean.setFlatOwnerName(SheredPref.getOwnerName(mainActivity));
                            familyBean.setFlatNo(SheredPref.getFlat_No(mainActivity));
                            new PostData(new Gson().toJson(familyBean), MainActivity.this, CallFor.SOS).execute();
                        } else {
                            CommonMethods.showToastError(context, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
                btnegative.setOnClickListener(v -> b.dismiss());
                //end of alert

            } else {
                CommonMethods.showToastSuccess(getApplicationContext(), "Please check internet connection");
            }
        });

        if (redirectToScreen.equals("0")) {
            CleverTapRegisterEvents.LoginActionEvent(this);
        }
    }

    private void ShowDialogRequestingForPermissions() {
        boolean cameraPermission = (ContextCompat.checkSelfPermission(this, PermissionConstant.PERMISSION_CAMERA) != PackageManager.PERMISSION_GRANTED);
        boolean audioPermission = (ContextCompat.checkSelfPermission(this, PermissionConstant.PERMISSION_AUDIO) != PackageManager.PERMISSION_GRANTED);
        if (cameraPermission == true || audioPermission == true) {
            strValidation = SheredPref.getLoginInfo(this);
            boolean rainbowAccess = SheredPref.getAccess_Raibow(this);
            if (strValidation.equals("LoggedIn") && rainbowAccess) {
                CheckPermissionsCamAndAudio();
            }
        }
    }

    private void showOpenNotificationSettingsDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.custum_alert_dialog, null);
        dialogBuilder.setView(dialogView);
        TextView txtalertheading = dialogView.findViewById(R.id.txtalertheading);
        TextView txtalertsubheading = dialogView.findViewById(R.id.txtalertsubheading);

        TextView btnegative = dialogView.findViewById(R.id.btnegative);
        TextView btnpositive = dialogView.findViewById(R.id.btnpositive);

        txtalertheading.setText("Allow Notification Access");
        txtalertsubheading.setText("For better call experience allow the access notification for VZTrack app");
        btnegative.setText(getResources().getString(R.string.dont_show));

        btnegative.setText(getResources().getString(R.string.dont_show));
        btnpositive.setText("Open");


        final AlertDialog b = dialogBuilder.create();
        b.setCanceledOnTouchOutside(false);
        b.setCancelable(false);
        b.show();
        btnegative.setOnClickListener(v -> {
            SheredPref.setNotificationAccessDialogFlag(mainActivity, false);
            b.dismiss();
            ShowDialogRequestingForPermissions();
        });

        btnpositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
                    b.dismiss();
                    isShowPermissionsDialog = true;
                } catch (Exception e) {
                    CommonMethods.showToastError(getApplicationContext(), getResources().getString(R.string.unable_to_open));
                }
                b.dismiss();
            }
        });
    }

    boolean hasNotificationAccess() {
        ContentResolver contentResolver = this.getContentResolver();
        String enabledNotificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = this.getPackageName();
        // check to see if the enabledNotificationListeners String contains our package name
        return !(enabledNotificationListeners == null || !enabledNotificationListeners.contains(packageName));
    }

    @Override
    public void onResume() {
        super.onResume();

        logoutStatusFlag = true;
        cc = new CheckConnection(getApplicationContext());
        strValidation = SheredPref.getLoginInfo(this);
        if (strValidation.equals("LoggedIn")) {
            if (cc.isConnectingToInternet()) {
                if (backPressFlag == 1) {
                    CallLoginApi();
                }
                if (redirectToScreen.equals("3")) {
                    drFlag = 3;
                    try {
                        RatingResponseBean ratingResponseBean = new RatingResponseBean();
                        ratingResponseBean.setSocietyId(Integer.parseInt(SheredPref.getSocietyId(mainActivity)));
                        ratingResponseBean.setFamilyId(Integer.parseInt(SheredPref.getFamilyId(mainActivity)));
                        new PostData(new Gson().toJson(ratingResponseBean), MainActivity.this, CallFor.PENDING_RATING).execute();

                    } catch (Exception ex) {
                        Log.e("Exception in onResume", " " + ex);
                    }
                }
            } else {
                SetupDrawer();
                if (!SheredPref.getUserManualSeenValue(MainActivity.this)) {
                    Intent i = new Intent(MainActivity.this, UserManualActivity.class);
                    startActivity(i);
                }
                if (title.equals("Visitors")) {
                    if (SheredPref.getExecuteOffline(getApplicationContext()).equals("")) {
                        ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.main, new VisitorsFragment(), "Data").commitAllowingStateLoss();
                        SheredPref.setExecuteOffline(getApplication(), "Not");
                    } else if (SheredPref.getExecuteOffline(getApplicationContext()).equals("Yes")) {
                        ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.main, new VisitorsFragment(), "Data").commitAllowingStateLoss();
                        SheredPref.setExecuteOffline(getApplication(), "Not");
                    } else {
                        SheredPref.setExecuteOffline(MainActivity.this, "NotExecute");
                    }
                }
                if (title.equals("Notices And Minutes")) {
                    ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.main, new NoticesFragment(), "Data").commitAllowingStateLoss();
                }

                if (title.equals("Feedback")) {
                    ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.main, new RatingFragment(), "Data").commitAllowingStateLoss();
                    NoVisiterLayout.setVisibility(View.GONE);
                    llwelcomeuser.setVisibility(View.VISIBLE);
                }
            }
        } else {
            Intent I = new Intent(MainActivity.this, LoginScreenActivity.class);
            startActivity(I);
            I.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }

        if (isShowPermissionsDialog) {
            isShowPermissionsDialog = false;
            ShowDialogRequestingForPermissions();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                onStop();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            CommonMethods.showToastError(this, "Press again to exit");
            //Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search) {
            return true;
        }

        if (id == R.id.action_call) {
            showCallDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        //RainbowSdk.instance().bubbles().getAllBubbles().unregisterChangeListener(roomChangeListener);
        super.onDestroy();
    }

    public void showCallDialog() {
        try {
            if (SheredPref.getPhoneNo(this).equals("")) {
                CommonMethods.showToastError(this, "No phone number added, Ask your admin to add security gate phone number");
            } else {
                String[] phone_numbers_info = SheredPref.getPhoneNo(this).trim().split("-");
                String[] gates = phone_numbers_info[0].trim().split(",");
                String[] phoneNo = phone_numbers_info[1].trim().split(",");
                ArrayList<String> phones = new ArrayList<>();
                for (int i = 0; i < phoneNo.length; i++) {
                    if (!phoneNo[i].equals("")) {
                        phones.add(gates[i] + " - " + phoneNo[i]);
                    }
                }

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
                builderSingle.setIcon(R.drawable.ic_phone);
                builderSingle.setTitle(R.string.society_emergency_contacts);
                if (SheredPref.getType(this) == true) {
                    builderSingle.setTitle(R.string.company_emergency_contacts);
                }
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item_phone);
                for (int i = 0; i < phones.size(); i++) {
                    arrayAdapter.add(phones.get(i));
                }
                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strPhoneNoAndGateName = arrayAdapter.getItem(which);
                        String phoneNumber = strPhoneNoAndGateName.split("-")[1];
                        UtilityMethodsAndroid.makeCall(mainActivity, phoneNumber);
                    }
                });
                builderSingle.show();
            }
        } catch (Exception ex) {
            Log.e("Exception In Calling", " " + ex);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionConstant.REQ_CODE_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Permissions.askPermission(this, PermissionConstant.PERMISSION_AUDIO, PermissionConstant.REQ_CODE_AUDIO);
                }
                break;
            }
            case PermissionConstant.REQ_CODE_AUDIO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Permissions.askPermission(this, PermissionConstant.PERMISSION_EXTERNAL_STORAGE, PermissionConstant.REQ_CODE_EXTERNAL_STORAGE);
                }
                break;
            }

            case PermissionConstant.REQ_CODE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Permissions.askPermission(this, PermissionConstant.PERMISSION_READ_CONTACT, PermissionConstant.REQ_CODE_READ_CONTACT);
                }
                break;
            }
            case PermissionConstant.REQ_CODE_READ_CONTACT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Permissions.askPermission(this, PermissionConstant.PERMISSION_READ_PHONE_STATE, PermissionConstant.REQ_CODE_READ_PHONE_STATE);
                }
                break;
            }

            case PermissionConstant.REQ_CODE_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Permissions.askPermission(this, PermissionConstant.PERMISSION_CALL_PHONE, PermissionConstant.REQ_CODE_CALL_PHONE);
                }
                break;
            }
        }
    }

    private void UpdateDatasetWithVisitorList() {
        if (cc.isConnectingToInternet()) {
            if (drFlag != 0) {
                ShowUI();
                visitor_PageNo = 0;
                Updated_result.clear();
                new GetData(MainActivity.this, CallFor.VISITORS, "" + visitor_PageNo).execute();
            }
        } else {
            ft.replace(R.id.main, new VisitorsFragment(), "Data").commitAllowingStateLoss();
        }
    }

    public void ShowUI() {
        NoVisiterLayout.setVisibility(View.VISIBLE);
        llwelcomeuser.setVisibility(View.GONE);
    }

    @Override
    public void onGetResponse(String response, String callFor) {

        if (cc.isConnectingToInternet()) {
            LoginResponse loginResponse = null;
            if (response == null) {
                return;
            }

            if (callFor.equals(CallFor.UPDATE_VISITOR_STATUS)) {
                loginResponse = new Gson().fromJson(response, LoginResponse.class);
                try {
                    if (loginResponse.getCode().equalsIgnoreCase("SUCCESS")) {

                        visitor_PageNo = 0;
                        Updated_result.clear();
                        new GetData(MainActivity.this, CallFor.VISITORS, "" + visitor_PageNo).execute();
                    } else {

                    }
                } catch (Exception e) {

                }
            }

            if (callFor.equals(CallFor.LOGIN)) {
                loginResponse = new Gson().fromJson(response, LoginResponse.class);
                if (loginResponse.getCode().equals("SUCCESS")) {
                    UtilityMethodsAndroid.updateSheredPreferenceValues(this, loginResponse);
                    SetupDrawer();
                    SheredPref.setPhotoURLOfUser(this, loginResponse.getFamily().getUserPhoto());
                    if (strSplashCheck.equals("")) {
                        //*** strSplashCheck is have value if call comes from login Activity
                        //  in Login Activity already rainbow connected so need not to check
                        if (loginResponse.getFamily().getRainbowEmailId() != null && !loginResponse.getFamily().getRainbowEmailId().equals("")) {
                            if (SheredPref.getAccess_Raibow(this)) {
                                FamilyBean familyBean = new FamilyBean();
                                familyBean.setRainbowEmailId(SheredPref.getRainbowEmailId(this));
                                familyBean.setRainbowPassword(SheredPref.getRainbowPassword(this));
                                startRainbow(familyBean);
                            }
                        } else {
                            SheredPref.setRainbowLogin(this, false);
                        }
                    }
                    SheredPref.setSocietyApproval(this, loginResponse.getFamily().isApproval());
                    String phoneNo = loginResponse.getFamily().getSoc_phoneNo();
                    String emailId = loginResponse.getFamily().getEmail();
                    String storedEmailId = SheredPref.getUserEmail(this);
                    isPrimaryUser = loginResponse.getFamily().isPrimary();
                    SheredPref.setPrimary(this, isPrimaryUser);
                    SheredPref.setPhotoURLOfUser(this, loginResponse.getFamily().getUserPhoto());
                    SheredPref.setName(this, loginResponse.getFamily().getName());
                    boolean sosAccess = loginResponse.getConfigBean().isSos();
                    boolean complaintAccess = loginResponse.getConfigBean().isComplain();
                    boolean localStoreAccess = loginResponse.getConfigBean().isLocalStore();
                    if (!SheredPref.getPhoneNo(this).equals(phoneNo)) {
                        SheredPref.setPhoneNo(this, phoneNo);
                    }
                    if (SheredPref.getSOSAccess(this) != sosAccess) {
                        SheredPref.setSOSAccess(this, sosAccess);
                    }
                    if (SheredPref.getComplain(this) != complaintAccess) {
                        SheredPref.setComplaint(this, complaintAccess);
                    }
                    if (SheredPref.getLocalStores(this) != localStoreAccess) {
                        SheredPref.setLocalStores(this, localStoreAccess);
                    }
                    if (SheredPref.getSOSAccess(this)) {
                        btnSOS.setVisibility(View.VISIBLE);
                    } else {
                        btnSOS.setVisibility(View.GONE);
                    }
                    if (emailId != null && !emailId.equals(storedEmailId)) {
                        SheredPref.setUserEmail(this, emailId);
                    }
                    SheredPref.setLoginByEmail(this, loginResponse.getFamily().isLoginByEmail());
                    SheredPref.setCompany(this, loginResponse.getConfigBean().isCompany());
                    type = SheredPref.getType(this);
                    complaint = SheredPref.getComplain(this);
                    localStores = SheredPref.getLocalStores(this);
                    isPollAccess = SheredPref.getPoll(this);
                    SheredPref.setDrawerMenuList(context, new Gson().toJson(loginResponse.getConfigBean().getUserAppMenuJsonObject()));

                    SheredPref.setAdminAccess(this, loginResponse.getFamily().isExtraAccess());
                    appVersion = loginResponse.getAppBean().getAppVersion();
                    int app_version_code = UtilityMethodsAndroid.GetVersionCode(context, TAG);
                    if (app_version_code != appVersion) {
                        int forcedUpdate = Integer.parseInt(loginResponse.getAppBean().getForceUpdate());
                        String versionInfo = loginResponse.getAppBean().getVersionInfo();
                        if (forcedUpdate == 1) {
                            if (!isUpdateAlertShow)
                                showUpdateDialog(versionInfo, true);
                        } else {
                            if (SheredPref.getLatestAppVersion(mainActivity) != appVersion) {
                                if (!isUpdateAlertShow)
                                    showUpdateDialog(versionInfo, false);
                            }
                        }
                    }
                    if (SheredPref.getSocietyId(this).equals("")) {
                        int strSocietyId = loginResponse.getFamily().getSocietyId();
                        int strFamilyId = loginResponse.getFamily().getFamilyId();
                        SheredPref.setSocietyId(this, "" + strSocietyId);
                        SheredPref.setFamilyId(this, "" + strFamilyId);
                    }
                    SetupDrawer();
                    if (SheredPref.getExecute(getApplicationContext()).equals("")) {
                        if (redirectToScreen.equals("1") || redirectToScreen.equals("0")) {
                            drFlag = 0;
                            Updated_result.clear();
                            new GetData(MainActivity.this, CallFor.VISITORS, "" + visitor_PageNo).execute();
                            SheredPref.setExecute(getApplication(), "Not");
                            updateUIAsLoading();
                            redirectToScreen = "0";
                            if (showInvoiceDueDialog && SheredPref.getAccess_Invoice(this) == true) {
                                showDueInvoiceDialog();
                                showInvoiceDueDialog = false;
                            }
                        }
                        if (redirectToScreen.equals("2")) {
                            drFlag = 1;
                            new GetData(MainActivity.this, CallFor.NOTICES, "").execute();
                            SheredPref.setExecute(getApplication(), "Not");
                            redirectToScreen = "0";
                        }
                        if (redirectToScreen.equals("4")) {
                            drFlag = 2;
                            updatedMessageResponceBean.clear();
                            message_PageNo = 0;
                            SheredPref.setExecute(getApplication(), "Not");
                            new GetData(this, CallFor.MESSAGE, "" + 0).execute();
                            redirectToScreen = "0";
                        }
                        if (redirectToScreen.equals("5")) {
                            Bundle extras = getIntent().getExtras();
                            String message = extras.getString("MESSAGE");
                            String title = extras.getString("TITLE");
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View dialogView = inflater.inflate(R.layout.vehiclealert, null);
                            dialogBuilder.setView(dialogView);
                            TextView txtalertheading = dialogView.findViewById(R.id.txtalertheading);
                            TextView txtalertsubheading = dialogView.findViewById(R.id.txtalertsubheading);
                            TextView btnpositive = dialogView.findViewById(R.id.btnpositive);
                            txtalertheading.setText(title);
                            txtalertsubheading.setText(message);
                            final AlertDialog b = dialogBuilder.create();
                            b.setCanceledOnTouchOutside(false);
                            b.setCancelable(false);
                            b.show();
                            btnpositive.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    b.dismiss();
                                }
                            });
                            drFlag = 0;
                            Updated_result.clear();
                            new GetData(MainActivity.this, CallFor.VISITORS, "" + visitor_PageNo).execute();
                            SheredPref.setExecute(getApplication(), "Not");
                            redirectToScreen = "0";
                        }
                        if (redirectToScreen.equals("6")) {
                            drFlag = 12;
                            SheredPref.setRun(mainActivity, "RUN");
                            new GetData(MainActivity.this, CallFor.USER_POLLS, "" + 0).execute();
                            redirectToScreen = "0";
                        }
                        if (redirectToScreen.equals("7")) {
                            drFlag = 14;
                            SheredPref.setRun(mainActivity, "RUN");
                            ft = fragmentManager.beginTransaction();
                            ft.replace(R.id.main, new CarPoolFragment(), "Data").commitAllowingStateLoss();
                            redirectToScreen = "0";
                        }
                        if (redirectToScreen.equals("8")) {
                            drFlag = 14;
                            SheredPref.setRun(mainActivity, "RUN");
                            ft = fragmentManager.beginTransaction();
                            carPoolFragmentName = "Request";
                            ft = fragmentManager.beginTransaction();
                            ft.replace(R.id.main, new CarPoolFragment(), "Data").commitAllowingStateLoss();
                            redirectToScreen = "0";
                        }
                        if (redirectToScreen.equals("9")) {
                            drFlag = 8;
                            SheredPref.setRun(mainActivity, "RUN");
                            ft = fragmentManager.beginTransaction();
                            ft.replace(R.id.main, new ComplaintFragment(), "Data").commitAllowingStateLoss();
                            redirectToScreen = "0";
                        }
                        if (redirectToScreen.equals("10")) {
                            drFlag = 15;
                            SheredPref.setRun(mainActivity, "RUN");
                            ft = fragmentManager.beginTransaction();
                            ft.replace(R.id.main, new MarketPlaceFragment(), "Data").commitAllowingStateLoss();
                            redirectToScreen = "0";
                        }
                        if (redirectToScreen.equals("11")) {
                            drFlag = 20;
                            Bundle extras = getIntent().getExtras();
                            String type = extras.getString("Rainbow_Notification_Type") == null ? "" : extras.getString("Rainbow_Notification_Type");
                            if (type.equals("Invite")) {
                                getSupportActionBar().setTitle("Group");
                                invitationNotification = true;
                            } else {
                                getSupportActionBar().setTitle("Conversation");
                                invitationNotification = false;
                            }
                            SheredPref.setExecute(getApplication(), "Not");
                            ft = fragmentManager.beginTransaction();
                            new GetData(MainActivity.mainActivity, CallFor.RAINBOW_USERS, SheredPref.getRainbowEmailId(this)).execute();
                            redirectToScreen = "0";
                        }
                        if (redirectToScreen.equals("12")) {
                            drFlag = 23;
                            SheredPref.setRun(mainActivity, "RUN");
                            ft = fragmentManager.beginTransaction();
                            ft.replace(R.id.main, new InvoiceFragment(), "Data").commitAllowingStateLoss();
                            redirectToScreen = "0";
                        }
                        if (redirectToScreen.equals("13")) {
                            drawer.closeDrawer(GravityCompat.START);
                            title = webViewActivityTitle;
                            SheredPref.setRun(mainActivity, "RUN");
                            drFlag = 24;
                            if (cc.isConnectingToInternet()) {
                                String url = webUrl;
                                if (url != null && !url.equals("")) {
                                    ShowUI();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("URL", url);
                                    bundle.putString("POST_DATA", UtilityMethods.getPostData(MainActivity.this));
                                    bundle.putString("ACTION_BAR_TITLE", title);
                                    ExtraFieldFragmentOne fragmentOne = new ExtraFieldFragmentOne();
                                    fragmentOne.setArguments(bundle);
                                    ft = fragmentManager.beginTransaction();
                                    ft.replace(R.id.main, fragmentOne, "Data").commitAllowingStateLoss();
                                } else {
                                    CommonMethods.showToastSuccess(context, "Unable to perform action, Empty Url");
                                }
                            } else {
                                ft.replace(R.id.main, new NoInternetFragment(), "Data").commitAllowingStateLoss();
                            }
                            redirectToScreen = "0";
                        }
                    } else {
                        SheredPref.setExecute(MainActivity.this, "NotExecute");
                    }
                    if (!SheredPref.getUserManualSeenValue(MainActivity.this)) {
                        Intent i = new Intent(MainActivity.this, UserManualActivity.class);
                        startActivity(i);
                    }
                } else if (loginResponse.getCode().equals("ERROR")) {
                    CommonMethods.showToastError(this, "Something went wrong, Please login again");
                    String device_id = Settings.Secure.getString(mainActivity.getContentResolver(), Settings.Secure.ANDROID_ID);
                    UserBean userBean = new UserBean();
                    userBean.setUser_dev_id(device_id);
                    new PostData(new Gson().toJson(userBean), MainActivity.this, CallFor.LOGOUT).execute();

                }
            }

            if (callFor.equals(CallFor.NOTICES)) {
                noticesResponse = new Gson().fromJson(response, NoticesResponse.class);
                if (fragment_flag == 1) {
                    NoticesFragment.mSwipeRefreshLayout.setRefreshing(false);
                    fragment_flag = 0;
                }
                try {
                    if (noticesResponse.getCode().equals("SUCCESS")) {
                        ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.main, new NoticesFragment(), "Data").commitAllowingStateLoss();

                        if (noticesResponse.getMessage().equals("No Notices")) {
                            isNoticeZero = true;
                        } else {
                            isNoticeZero = false;
                        }
                    } else if (noticesResponse.getCode().equals("NEED_LOGIN")) {
                        CallLoginApi();
                    } else {
                        ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.main, new NoticesFragment(), "Data").commitAllowingStateLoss();
                    }
                } catch (Exception e) {
                    Log.e("Exception Notices", " " + e);
                }
            }  // End If

            if (callFor.equals(CallFor.VISITORS)) {
                if (isVisitorAccessMainScreen == true) {
                    ShowUI();
                    visitorsArray = new Gson().fromJson(response, VisitorsArray.class);
                    if (fragment_flag == 1) {
                        VisitorsFragment.mSwipeRefreshLayout.setRefreshing(false);
                        fragment_flag = 0;
                    }
                    try {
                        if (visitorsArray.getCode().equals("SUCCESS")) {
                            if (visitorsArray.getVisitors() == null || visitorsArray.getVisitors().size() == 0 && visitor_PageNo == 0) {
                                NoVisiterLayout.setVisibility(View.GONE);
                                llwelcomeuser.setVisibility(View.GONE);
                                noImageDataLayoutmain.setVisibility(View.VISIBLE);
                            } else {
                                NoVisiterLayout.setVisibility(View.VISIBLE);
                                llwelcomeuser.setVisibility(View.GONE);
                                noImageDataLayoutmain.setVisibility(View.GONE);
                                NoVisitorText.setVisibility(View.GONE);
                            }
                            showFlag = 1;
                            ft = fragmentManager.beginTransaction();
                            ft.replace(R.id.main, new VisitorsFragment(), "Data").commitAllowingStateLoss();
                        } else if (visitorsArray.getCode().equals("NEED_LOGIN")) {
                            CallLoginApi();
                        } else {
                            ft = fragmentManager.beginTransaction();
                            ft.replace(R.id.main, new VisitorsFragment(), "Data").commitAllowingStateLoss();
                        }
                    } catch (Exception e) {
                        Log.e("Exception Visitor", " " + e);
                    }
                } else {
                    llwelcomeuser.setVisibility(View.VISIBLE);
                    noImageDataLayoutmain.setVisibility(View.GONE);
                    NoVisitorText.setVisibility(View.VISIBLE);
                    isVisitorAccessMainScreen = false;
                    ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.main, new HomeScreenVisitorFragment(), "Data").commitAllowingStateLoss();

                }

            }

            if (callFor.equals(CallFor.MESSAGE)) {
                messageResponceBean = new Gson().fromJson(response, MessageResponceBean.class);
                if (fragment_flag == 1) {
                    MessageFragment.mSwipeRefreshLayout.setRefreshing(false);
                    fragment_flag = 0;
                }
                try {
                    if (messageResponceBean.getCode().equals("SUCCESS")) {
                        sentMessage = 2;
                        ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.main, new MessageFragment(), "Data").commitAllowingStateLoss();
                    } else if (messageResponceBean.getCode().equals("NEED_LOGIN")) {
                        CallLoginApi();
                    } else {
                        ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.main, new MessageFragment(), "Data").commitAllowingStateLoss();
                    }
                } catch (Exception e) {
                    Log.e("Exception ", " " + e);
                }
            }

            if (callFor.equals(CallFor.PROVIDER_LIST)) {
                try {
                    prov_list = new Gson().fromJson(response, String[].class);
                    // Store the Provider List in SharedPreferences
                    SharedPreferences prefs = this.getSharedPreferences("LIST", 0);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putInt("array_size", prov_list.length);
                    for (int i = 0; i < prov_list.length; i++)
                        edit.putString("array_" + i, prov_list[i]);
                    edit.commit();
                } catch (Exception e) {
                    Log.e("Exception ProviderList", " " + e);
                }
            }

            if (callFor.equals(CallFor.PROVIDERS_DATA)) {
//                getSupportActionBar().setTitle("Search Provider");
                try {
                    outputBeanSearchProviders = new Gson().fromJson(response, new TypeToken<ArrayList<OutputBeanSearchProvider>>() {
                    }.getType());
                    if (outputBeanSearchProviders.size() == 0) {
                        SearchProviderFragment.tvNoProvider.setVisibility(View.VISIBLE);
                        SearchProviderFragment.tvNoProvider.setText("No Provider Found !");
                        SearchProviderFragment.mRecyclerView.setVisibility(View.GONE);
                    } else {
                        SearchProviderFragment.tvNoProvider.setVisibility(View.GONE);
                        SearchProviderFragment.mRecyclerView.setVisibility(View.VISIBLE);
                    }
                    SearchProviderFragment.mAdapter = new Search_Provider_RecyclerViewAdapter(this, outputBeanSearchProviders);
                    SearchProviderFragment.mRecyclerView.setAdapter(SearchProviderFragment.mAdapter);
                } catch (Exception ex) {
                    Log.e("EXCEPTION ProviderData", " " + ex);
                }
            }


            if (callFor.equals(CallFor.LOGOUT)) {
                LogoutResponse logoutResponse = new Gson().fromJson(response, LogoutResponse.class);
                try {
                    RainbowSdk.instance().getContext().getCacheDir().delete();
                    SheredPref.setLoginInfo(this, "LoggedOut");
                    SheredPref.setPassword(this, "");
                    SheredPref.setDateForApi(this, "");
                    SheredPref.setDateForCompApi(this, "");
                    SheredPref.setUserEmail(this, "");
                    SheredPref.setNoticeCount(this, 0);
                    SheredPref.setComplaintCount(this, 0);
                    SheredPref.setMessageCount(this, 0);
                    SheredPref.setRainbowCount(this, 0);
                    SheredPref.setRainbowEmailId(this, "");
                    SheredPref.setRainbowPassword(this, "");
                    SheredPref.setName(this, "");
                    SheredPref.setOwnerName(this, "");
                    SharedPreferences prefs = this.getSharedPreferences("COMP_LIST", 0);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.clear();
                    edit.commit();
                    logoutStatusFlag = true;
                    LogoutRainbow(null);
                    SheredPref.setFrequentlyUsedMenuList(this, null);
                    SheredPref.setDrawerMenuList(context, null);
                    Intent I = new Intent(MainActivity.this, LoginScreenActivity.class);
                    startActivity(I);
                    this.finish();
                    I.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    SheredPref.setNotification(this, "DISABLE");
                } catch (Exception ex) {
                    SheredPref.setLoginInfo(this, "LoggedOut");
                    SheredPref.setPassword(this, "");
                    SheredPref.setDateForApi(this, "");
                    SheredPref.setDateForCompApi(this, "");
                    SheredPref.setNoticeCount(this, 0);
                    SheredPref.setComplaintCount(this, 0);
                    SheredPref.setMessageCount(this, 0);
                    SheredPref.setRainbowCount(this, 0);
                    SheredPref.setName(this, "");
                    SheredPref.setOwnerName(this, "");
                    SharedPreferences prefs = this.getSharedPreferences("COMP_LIST", 0);
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.clear();
                    edit.commit();
                    Intent I = new Intent(MainActivity.this, LoginScreenActivity.class);
                    startActivity(I);
                    this.finish();
                    I.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    SheredPref.setFrequentlyUsedMenuList(this, null);
                    SheredPref.setDrawerMenuList(context, null);
                    SheredPref.setNotification(this, "DISABLE");
                }
            }

            if (callFor.equals(CallFor.SOS)) {
                loginResponse = new Gson().fromJson(response, LoginResponse.class);
                try {
                    if (loginResponse.getCode().equalsIgnoreCase("SUCCESS")) {
                        drawer.closeDrawer(GravityCompat.START);
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View dialogView = inflater.inflate(R.layout.custum_alert_dialog, null);
                        dialogBuilder.setView(dialogView);
                        TextView txtalertheading = dialogView.findViewById(R.id.txtalertheading);
                        TextView txtalertsubheading = dialogView.findViewById(R.id.txtalertsubheading);

                        TextView btnegative = dialogView.findViewById(R.id.btnegative);
                        TextView btnpositive = dialogView.findViewById(R.id.btnpositive);

                        txtalertheading.setText(loginResponse.getMessage().split(",")[0]);
                        txtalertsubheading.setText(loginResponse.getMessage().split(",")[1]);
                        btnegative.setText(getResources().getString(R.string.dont_show));

                        btnegative.setVisibility(View.GONE);
                        btnpositive.setText("OK");


                        final AlertDialog b = dialogBuilder.create();
                        b.setCanceledOnTouchOutside(false);
                        b.setCancelable(false);
                        b.show();

                        btnpositive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                b.dismiss();
                            }
                        });

                    } else if (loginResponse.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                        drawer.closeDrawer(GravityCompat.START);
                        CallLoginApi();
                    } else {
                        drawer.closeDrawer(GravityCompat.START);
                        CommonMethods.showToastError(this, "Something went wrong, Please try again");
                    }
                } catch (Exception ex) {
                    Log.e("Exception In SOS", " " + ex);
                }
            }

            if (callFor.equals(CallFor.FEEDBACK)) {
                loginResponse = new Gson().fromJson(response, LoginResponse.class);
                try {
                    if (loginResponse.getCode().equalsIgnoreCase("SUCCESS")) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View dialogView = inflater.inflate(R.layout.custum_alert_dialog, null);
                        dialogBuilder.setView(dialogView);
                        TextView txtalertheading = dialogView.findViewById(R.id.txtalertheading);
                        TextView txtalertsubheading = dialogView.findViewById(R.id.txtalertsubheading);

                        TextView btnegative = dialogView.findViewById(R.id.btnegative);
                        TextView btnpositive = dialogView.findViewById(R.id.btnpositive);

                        txtalertheading.setText(loginResponse.getMessage().split(",")[0]);
                        txtalertsubheading.setText("Your feedback is valuable \nfor us");
                        btnegative.setVisibility(View.GONE);
                        final AlertDialog b = dialogBuilder.create();
                        b.setCanceledOnTouchOutside(false);
                        b.setCancelable(false);
                        b.show();

                        btnpositive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                b.dismiss();
                            }
                        });

                    } else if (loginResponse.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                        drawer.closeDrawer(GravityCompat.START);
                        CallLoginApi();
                    } else {
                        drawer.closeDrawer(GravityCompat.START);
                        CommonMethods.showToastError(this, "Something went wrong, Please try again");
                    }
                } catch (Exception ex) {
                    Log.e("Exception Feedback", " " + ex);
                }
            }
            if (callFor.equals(CallFor.GET_COMPLAIN)) {

                complainResponceBean = new Gson().fromJson(response, ComplainResponceBean.class);
                try {
                    if (complainResponceBean.getCode().equalsIgnoreCase("SUCCESS")) {
                        try {
                            complian_result.addAll(complainResponceBean.getComplains());
                            ComplaintFragment.mSwipeRefreshLayout.setRefreshing(false);
                            ComplaintFragment.mAdapter = new Complain_RecyclerViewAdapter(this, complian_result);
                            ComplaintFragment.mRecyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                            ComplaintFragment.mRecyclerView.setLayoutManager(mLayoutManager);
                            ComplaintFragment.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            ComplaintFragment.mRecyclerView.setAdapter(ComplaintFragment.mAdapter);
                            ComplaintFragment.mAdapter.notifyDataSetChanged();
                            if (pageNo != 0) {
                                ComplaintFragment.mRecyclerView.scrollToPosition(MainActivity.pageNo * 9);
                            }
                            if (((complainResponceBean == null || complainResponceBean.getComplains().size() == 0)) && pageNo == 0) {
                                ComplaintFragment.NoDataLayout.setVisibility(View.VISIBLE);
                                ComplaintFragment.NoDataText.setText("No complaints to display");
                                ComplaintFragment.mRecyclerView.setVisibility(View.GONE);
                            } else {
                                ComplaintFragment.NoDataLayout.setVisibility(View.GONE);
                                ComplaintFragment.mRecyclerView.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception ex) {
                            Log.e(TAG, " Exception In Adapter Setting " + ex);
                        }
                    } else if (complainResponceBean.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                        drawer.closeDrawer(GravityCompat.START);
                        CallLoginApi();
                    } else {
                        drawer.closeDrawer(GravityCompat.START);
                        CommonMethods.showToastError(this, "Something went wrong, Please try again");
                    }
                } catch (Exception ex) {
                    Log.e("Exception Complain", " " + ex);
                }
            }
            if (callFor.equals(CallFor.GET_COMPLAIN_DETAILS)) {
                complainResponceBean = new Gson().fromJson(response, ComplainResponceBean.class);
                try {
                    if (complainResponceBean.getCode().equalsIgnoreCase("SUCCESS")) {
                        Intent intent = new Intent(this, ComplaintDetails.class);
                        startActivity(intent);
                    } else if (complainResponceBean.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                        drawer.closeDrawer(GravityCompat.START);
                        CallLoginApi();
                    } else {
                        drawer.closeDrawer(GravityCompat.START);
                        CommonMethods.showToastError(this, "Something went wrong, Please try again");
                    }
                } catch (Exception ex) {
                    Log.e("Exception Complain", " " + ex);
                }
            }

            if (callFor.equals(CallFor.CLOSE_COMPLAIN)) {
                complainResponceBean = new Gson().fromJson(response, ComplainResponceBean.class);
                try {
                    if (complainResponceBean.getCode().equalsIgnoreCase("SUCCESS")) {
                        Complain_RecyclerViewAdapter.dialog.dismiss();
                        complian_result.clear();
                        pageNo = 0;
                        ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.main, new ComplaintFragment(), "Data").commitAllowingStateLoss();
                        CommonMethods.showToastSuccess(this, complainResponceBean.getMessage());
                    } else if (complainResponceBean.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                        drawer.closeDrawer(GravityCompat.START);
                        CallLoginApi();
                    } else {
                        drawer.closeDrawer(GravityCompat.START);
                        CommonMethods.showToastError(this, "Something went wrong, Please try again");
                    }
                } catch (Exception ex) {
                    Log.e("Exception Complain", " " + ex);
                }
            }

            if (callFor.equals(CallFor.ADD_COMPLAIN)) {
                ComplaintFragment.encodedImageUrl = null;

                complainResponceBean = new Gson().fromJson(response, ComplainResponceBean.class);
                try {
                    if (complainResponceBean.getCode().equalsIgnoreCase("SUCCESS")) {
                        complian_result.clear();
                        pageNo = 0;
                        ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.main, new ComplaintFragment(), "Data").commitAllowingStateLoss();
                        CommonMethods.showToastSuccess(this, complainResponceBean.getMessage());//, Toast.LENGTH_SHORT, true).show();
                    } else if (complainResponceBean.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                        drawer.closeDrawer(GravityCompat.START);
                        CallLoginApi();
                    } else {
                        drawer.closeDrawer(GravityCompat.START);
                        CommonMethods.showToastError(this, "Something went wrong, Please try again");//, Toast.LENGTH_SHORT, true).show();
                    }
                } catch (Exception ex) {
                    Log.e("Exception Complain", " " + ex);
                }
            }
            if (callFor.equals(CallFor.ERROR_ENTRY)) {
                MainActivity.Updated_result.clear();
                visitor_PageNo = 0;
                visitorsArray = new Gson().fromJson(response, VisitorsArray.class);
                try {
                    if (visitorsArray.getCode().equals("SUCCESS")) {
                        CommonMethods.showToastSuccess(getApplicationContext(), "Successfully marked visitor");//,getResources().getColor(R.color.pollcolor));
                        showFlag = 1;
                        ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.main, new VisitorsFragment(), "Data").commitAllowingStateLoss();
                    } else if (visitorsArray.getCode().equals("NEED_LOGIN")) {
                        CallLoginApi();
                    } else if (visitorsArray.getCode().equals("Already_Marked")) {
                        CommonMethods.showToastSuccess(getApplicationContext(), visitorsArray.getMessage());//,getResources().getColor(R.color.pollcolor));
                        showFlag = 1;
                        ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.main, new VisitorsFragment(), "Data").commitAllowingStateLoss();
                    } else {
                        ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.main, new VisitorsFragment(), "Data").commitAllowingStateLoss();
                    }

                } catch (Exception ex) {
                    Log.e("EXCEPTION Error Entry", "" + ex);
                }
            }

            if (callFor.equals(CallFor.VEHICLENOPATTERN)) {
                vehicleResponce = new Gson().fromJson(response, VehicleResponce.class);
                try {
                    if (vehicleResponce.getCode().equalsIgnoreCase("SUCCESS")) {
                        String all_vehicle_no_for_add = null;
                        String all_vehicle_no_for_search = null;
                        String all_vehicle_pattern = null;
                        String all_flags = null;
                        for (int i = 0; i < MainActivity.vehicleResponce.getVehiclePattern().size(); i++) {
                            if (i == 0) {
                                all_vehicle_pattern = vehicleResponce.getVehiclePattern().get(i).split("=")[1].trim();
                                all_flags = vehicleResponce.getVehiclePattern().get(i).split("=")[2].trim();
                                String flag = vehicleResponce.getVehiclePattern().get(i).split("=")[2].trim();
                                if (flag.trim().equals("0")) {
                                    all_vehicle_no_for_add = vehicleResponce.getVehiclePattern().get(i).split("=")[0].trim();
                                }
                                all_vehicle_no_for_search = vehicleResponce.getVehiclePattern().get(i).split("=")[0].trim();

                            } else {
                                all_vehicle_pattern = all_vehicle_pattern + "@ " + vehicleResponce.getVehiclePattern().get(i).split("=")[1].trim();
                                all_flags = all_flags + ", " + vehicleResponce.getVehiclePattern().get(i).split("=")[2].trim();
                                String flag = vehicleResponce.getVehiclePattern().get(i).split("=")[2].trim();
                                if (flag.trim().equals("0")) {
                                    if (all_vehicle_no_for_add == null) {
                                        all_vehicle_no_for_add = vehicleResponce.getVehiclePattern().get(i).split("=")[0].trim();
                                    } else {
                                        all_vehicle_no_for_add = all_vehicle_no_for_add + ", " + vehicleResponce.getVehiclePattern().get(i).split("=")[0].trim();
                                    }
                                }
                                all_vehicle_no_for_search = all_vehicle_no_for_search + ", " + vehicleResponce.getVehiclePattern().get(i).split("=")[0].trim();
                            }
                        }
                        SheredPref.setVehicleNo_for_add(this, all_vehicle_no_for_add);
                        SheredPref.setVehicleNo_for_search(this, all_vehicle_no_for_search);
                        SheredPref.setVehiclePatternNo(this, all_vehicle_pattern);
                        SheredPref.setVehicleFlag(this, all_flags);

                    } else if (complainResponceBean.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                        CallLoginApi();
                        new GetData(MainActivity.mainActivity, CallFor.VEHICLENOPATTERN, "").execute();
                    } else {
                        drawer.closeDrawer(GravityCompat.START);
                        CommonMethods.showToastError(this, "Something went wrong, Please try again");//, Toast.LENGTH_SHORT, true).show();
                    }
                } catch (Exception ex) {
                    Log.e("Ex Vehicle Pattern", " " + ex);
                }
            }
        }

        if (callFor.equals(CallFor.SEARCH_VEHICLE)) {
            vehicleResponce = new Gson().fromJson(response, VehicleResponce.class);
            try {
                if (vehicleResponce.getCode().equalsIgnoreCase("SUCCESS")) {
                    int size = vehicleResponce.getSearchVehicleBeans().size();
                    AddvehicleFragment.vehicleNumber.setText("");
                    if (size == 0) {
                        SearchVehicleFragment.noImageDataLayout.setVisibility(View.VISIBLE);
                        SearchVehicleFragment.noData.setVisibility(View.VISIBLE);
                        SearchVehicleFragment.noData.setText("No result found for vehicle Number \n '" + SearchVehicleFragment.res.getText().toString().trim().split("'")[1] + "'");
                        SearchVehicleFragment.searched_vehicle_recycler_view.setVisibility(View.GONE);
                        SearchVehicleFragment.res.setVisibility(View.GONE);
                    } else {
                        SearchVehicleFragment.noImageDataLayout.setVisibility(View.GONE);
                        SearchVehicleFragment.noData.setVisibility(View.GONE);
                        SearchVehicleFragment.searched_vehicle_recycler_view.setVisibility(View.VISIBLE);
                        SearchVehicleFragment.res.setVisibility(View.VISIBLE);
                    }
                    try {
                        SearchVehicleFragment.mAdapter = new Search_Vehicles_RecyclerViewAdapter(this, MainActivity.vehicleResponce.getSearchVehicleBeans());
                        SearchVehicleFragment.searched_vehicle_recycler_view.setHasFixedSize(true);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                        SearchVehicleFragment.searched_vehicle_recycler_view.setLayoutManager(mLayoutManager);
                        SearchVehicleFragment.searched_vehicle_recycler_view.setItemAnimator(new DefaultItemAnimator());
                        SearchVehicleFragment.mAdapter.notifyDataSetChanged();
                        SearchVehicleFragment.searched_vehicle_recycler_view.setAdapter(SearchVehicleFragment.mAdapter);

                    } catch (Exception ex) {
                        Log.e("Exception ", " " + ex);
                    }
                } else if (vehicleResponce.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                    CallLoginApi();
                } else {
                    drawer.closeDrawer(GravityCompat.START);
                    CommonMethods.showToastError(this, "Something went wrong, Please try again");//, Toast.LENGTH_SHORT, true).show();
                }
            } catch (Exception ex) {
                Log.e("Ex Search Vehicle", " " + ex);
            }
        }

        if (callFor.equals(CallFor.GET_VEHICLES)) {
            vehicleResponce = new Gson().fromJson(response, VehicleResponce.class);
            try {
                if (vehicleResponce.getCode().equalsIgnoreCase("SUCCESS")) {
                    int size = vehicleResponce.getVehicleBeans().size();
                    if (size == 0) {
                        AddvehicleFragment.no_Data.setVisibility(View.VISIBLE);
                        AddvehicleFragment.vehicle_List.setVisibility(View.GONE);
                        AddvehicleFragment.noImageDataLayout.setVisibility(View.VISIBLE);
                    } else {
                        AddvehicleFragment.no_Data.setVisibility(View.GONE);
                        AddvehicleFragment.vehicle_List.setVisibility(View.VISIBLE);
                        AddvehicleFragment.noImageDataLayout.setVisibility(View.GONE);
                    }
                    try {
                        AddvehicleFragment.mAdapter = new VehicleListAdapter(vehicleResponce.getVehicleBeans(), getApplicationContext());
                        AddvehicleFragment.vehicle_List.setAdapter(AddvehicleFragment.mAdapter);

                        SearchVehicleFragment.mAdapter = new Search_Vehicles_RecyclerViewAdapter(this, MainActivity.vehicleResponce.getSearchVehicleBeans());
                        SearchVehicleFragment.searched_vehicle_recycler_view.setHasFixedSize(true);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                        SearchVehicleFragment.searched_vehicle_recycler_view.setLayoutManager(mLayoutManager);
                        SearchVehicleFragment.searched_vehicle_recycler_view.setItemAnimator(new DefaultItemAnimator());
                        SearchVehicleFragment.mAdapter.notifyDataSetChanged();
                        SearchVehicleFragment.searched_vehicle_recycler_view.setAdapter(SearchVehicleFragment.mAdapter);

                    } catch (Exception ex) {
                        Log.e("Exception ", " " + ex);
                    }
                } else if (vehicleResponce.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                    CallLoginApi();
                    CommonMethods.showToastError(this, "Please try again");//, Toast.LENGTH_SHORT, true).show();
                } else {
                    drawer.closeDrawer(GravityCompat.START);
                    CommonMethods.showToastError(this, "Something went wrong, Please try again");//, Toast.LENGTH_SHORT, true).show();
                }
            } catch (Exception ex) {
                Log.e("Ex Get Vehicles", " " + ex);
            }
        }

        if (callFor.equals(CallFor.ADD_VEHICLE)) {
            vehicleResponce = new Gson().fromJson(response, VehicleResponce.class);
            try {
                if (vehicleResponce.getCode().equalsIgnoreCase("SUCCESS")) {
                    SearchVehicleFragment.noData.setVisibility(View.GONE);
                    SearchVehicleFragment.noImageDataLayout.setVisibility(View.GONE);
                    SearchVehicleFragment.searched_vehicle_recycler_view.setVisibility(View.GONE);
//                    SearchVehicleFragment.card_view.setVisibility(View.GONE);
                    SearchVehicleFragment.res.setVisibility(View.GONE);
                    AddvehicleFragment.vehicleNumber.setText("");
                    int size = vehicleResponce.getVehicleBeans().size();
                    if (size == 0) {
                        AddvehicleFragment.no_Data.setVisibility(View.VISIBLE);
                        AddvehicleFragment.noImageDataLayout.setVisibility(View.VISIBLE);
                        AddvehicleFragment.vehicle_List.setVisibility(View.GONE);
                    } else {
                        AddvehicleFragment.no_Data.setVisibility(View.GONE);
                        AddvehicleFragment.noImageDataLayout.setVisibility(View.GONE);
                        AddvehicleFragment.vehicle_List.setVisibility(View.VISIBLE);
                    }
                    try {
                        AddvehicleFragment.mAdapter = new VehicleListAdapter(vehicleResponce.getVehicleBeans(), getApplicationContext());
                        AddvehicleFragment.vehicle_List.setAdapter(AddvehicleFragment.mAdapter);

                        SearchVehicleFragment.mAdapter = new Search_Vehicles_RecyclerViewAdapter(this, MainActivity.vehicleResponce.getSearchVehicleBeans());
                        SearchVehicleFragment.searched_vehicle_recycler_view.setHasFixedSize(true);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                        SearchVehicleFragment.searched_vehicle_recycler_view.setLayoutManager(mLayoutManager);
                        SearchVehicleFragment.searched_vehicle_recycler_view.setItemAnimator(new DefaultItemAnimator());
                        SearchVehicleFragment.mAdapter.notifyDataSetChanged();
                        SearchVehicleFragment.searched_vehicle_recycler_view.setAdapter(SearchVehicleFragment.mAdapter);

                    } catch (Exception ex) {
                        Log.e("Exception", " " + ex);
                    }
                } else if (vehicleResponce.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                    CallLoginApi();
                } else {
                    drawer.closeDrawer(GravityCompat.START);
                    CommonMethods.showToastError(this, vehicleResponce.getMessage());//, Toast.LENGTH_SHORT, true).show();
                }
            } catch (Exception ex) {
                Log.e("Ex Add Vehicle", " " + ex);
            }
        }

        if (callFor.equals(CallFor.DELETE_VEHICLE)) {
            vehicleResponce = new Gson().fromJson(response, VehicleResponce.class);
            try {
                if (vehicleResponce.getCode().equalsIgnoreCase("SUCCESS")) {
                    SearchVehicleFragment.noData.setVisibility(View.GONE);
                    SearchVehicleFragment.noImageDataLayout.setVisibility(View.GONE);
                    SearchVehicleFragment.searched_vehicle_recycler_view.setVisibility(View.GONE);
//                    SearchVehicleFragment.card_view.setVisibility(View.GONE);
                    SearchVehicleFragment.res.setVisibility(View.GONE);
                    int size = vehicleResponce.getVehicleBeans().size();
                    if (size == 0) {
                        AddvehicleFragment.no_Data.setVisibility(View.VISIBLE);
                        AddvehicleFragment.vehicle_List.setVisibility(View.GONE);
                        AddvehicleFragment.noImageDataLayout.setVisibility(View.VISIBLE);
                    } else {
                        AddvehicleFragment.no_Data.setVisibility(View.GONE);
                        AddvehicleFragment.vehicle_List.setVisibility(View.VISIBLE);
                        AddvehicleFragment.noImageDataLayout.setVisibility(View.GONE);
                    }
                    try {
                        AddvehicleFragment.mAdapter = new VehicleListAdapter(vehicleResponce.getVehicleBeans(), getApplicationContext());

                        AddvehicleFragment.vehicle_List.setAdapter(AddvehicleFragment.mAdapter);

                        SearchVehicleFragment.mAdapter = new Search_Vehicles_RecyclerViewAdapter(this, MainActivity.vehicleResponce.getSearchVehicleBeans());
                        SearchVehicleFragment.searched_vehicle_recycler_view.setHasFixedSize(true);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                        SearchVehicleFragment.searched_vehicle_recycler_view.setLayoutManager(mLayoutManager);
                        SearchVehicleFragment.searched_vehicle_recycler_view.setItemAnimator(new DefaultItemAnimator());
                        SearchVehicleFragment.mAdapter.notifyDataSetChanged();
                        SearchVehicleFragment.searched_vehicle_recycler_view.setAdapter(SearchVehicleFragment.mAdapter);

                    } catch (Exception ex) {
                        Log.e("Exception Vehicle", " " + ex);
                    }
                } else if (vehicleResponce.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                    CallLoginApi();
                } else if (vehicleResponce.getCode().equalsIgnoreCase("ERROR")) {
                    CommonMethods.showToastError(this, vehicleResponce.getMessage());//, Toast.LENGTH_SHORT, true).show();
                    SearchVehicleFragment.noData.setVisibility(View.GONE);
                    SearchVehicleFragment.noImageDataLayout.setVisibility(View.GONE);
                    SearchVehicleFragment.searched_vehicle_recycler_view.setVisibility(View.GONE);
//                    SearchVehicleFragment.card_view.setVisibility(View.GONE);
                    SearchVehicleFragment.res.setVisibility(View.GONE);
                    int size = vehicleResponce.getVehicleBeans().size();
                    if (size == 0) {
                        AddvehicleFragment.no_Data.setVisibility(View.VISIBLE);
                        AddvehicleFragment.vehicle_List.setVisibility(View.GONE);
                        AddvehicleFragment.noImageDataLayout.setVisibility(View.VISIBLE);
                    } else {
                        AddvehicleFragment.no_Data.setVisibility(View.GONE);
                        AddvehicleFragment.vehicle_List.setVisibility(View.VISIBLE);
                        AddvehicleFragment.noImageDataLayout.setVisibility(View.GONE);
                    }
                    try {
                        AddvehicleFragment.mAdapter = new VehicleListAdapter(vehicleResponce.getVehicleBeans(), getApplicationContext());
                        AddvehicleFragment.vehicle_List.setAdapter(AddvehicleFragment.mAdapter);
                        SearchVehicleFragment.mAdapter = new Search_Vehicles_RecyclerViewAdapter(this, MainActivity.vehicleResponce.getSearchVehicleBeans());
                        SearchVehicleFragment.searched_vehicle_recycler_view.setHasFixedSize(true);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                        SearchVehicleFragment.searched_vehicle_recycler_view.setLayoutManager(mLayoutManager);
                        SearchVehicleFragment.searched_vehicle_recycler_view.setItemAnimator(new DefaultItemAnimator());
                        SearchVehicleFragment.mAdapter.notifyDataSetChanged();
                        SearchVehicleFragment.searched_vehicle_recycler_view.setAdapter(SearchVehicleFragment.mAdapter);

                    } catch (Exception ex) {
                        Log.e("Exception ", " " + ex);
                    }
                } else {
                    drawer.closeDrawer(GravityCompat.START);
                    CommonMethods.showToastError(this, "Something went wrong, Please try again");//, Toast.LENGTH_SHORT, true).show();
                }
            } catch (Exception ex) {
                Log.e("Ex Delete Vehicle", " " + ex);
            }
        }

        if (callFor.equals(CallFor.SEND_VEHICLE_MESSAGE_NOTIFICATION)) {
            vehicleResponce = new Gson().fromJson(response, VehicleResponce.class);
            try {
                if (vehicleResponce.getCode().equalsIgnoreCase("SUCCESS")) {
                    CommonMethods.showToastSuccess(this, vehicleResponce.getMessage());//, Toast.LENGTH_SHORT, true).show();
                } else if (vehicleResponce.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                    CallLoginApi();
                    //Toasty.error(this, "Please try again", Toast.LENGTH_SHORT, true).show();
                } else {
                    CommonMethods.showToastError(this, vehicleResponce.getMessage());//, Toast.LENGTH_SHORT, true).show();
                }
            } catch (Exception ex) {
                Log.e("Ex Search Vehicle", " " + ex);
            }
        }
        if (callFor.equals(CallFor.LOCAL_STORES)) {
            try {
                LocalStoreResponse result = new Gson().fromJson(response, LocalStoreResponse.class);
                if (result.getCode().equals("SUCCESS")) {
                    LocalStoreFragment.localStores.clear();
                    if (result.getData().size() == 0) {
                        result.getData().add(null);
                        LocalStoreFragment.NoDataLayout.setVisibility(View.VISIBLE);
                        LocalStoreFragment.local_store_recycler_view.setVisibility(View.GONE);
                    } else {
                        LocalStoreFragment.NoDataLayout.setVisibility(View.GONE);
                        LocalStoreFragment.local_store_recycler_view.setVisibility(View.VISIBLE);
                        LocalStoreFragment.localStores.addAll(result.getData());
                        LocalStoreFragment.localStoreAdapter.notifyDataSetChanged();
                    }

                } else if (result.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                    CallLoginApi();
                    //Toasty.error(this, "Please try again", Toast.LENGTH_SHORT, true).show();
                } else {
                    drawer.closeDrawer(GravityCompat.START);
                    CommonMethods.showToastError(this, "Something went wrong, Please try again");//, Toast.LENGTH_SHORT, true).show();
                }
            } catch (Exception ex) {
                Log.e("Ex Delete Vehicle", " " + ex);
            }
        }

        // Poll Operations API
        if (callFor.equals(CallFor.POLL_OPERATION)) {
            ResponceBean result = new Gson().fromJson(response, ResponceBean.class);
            if (result.getCode().equals("SUCCESS")) {
                CommonMethods.showToastSuccess(this, result.getMessage());//, Toast.LENGTH_LONG).show();
                if (dialogFragment == 1) {
                    CreateEditPollsFragment.ClearAllData();
                }
                if (dialogFragment == 2) {
                    AdminPoll_RecyclerViewAdapter.ClearAllData();
                }
                new GetData(this, CallFor.ADMIN_POLLS, "").execute();

            } else if (result.getCode().equals("ERROR")) {
                CommonMethods.showToastError(this, result.getMessage());//, Toast.LENGTH_LONG).show();
            } else if (result.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                CallLoginApi();
                //Toasty.error(this, "Please try again", Toast.LENGTH_SHORT, true).show();
            } else {
                CommonMethods.showToastError(this, "Something went wrong, Please try again");//, Toast.LENGTH_SHORT, true).show();
            }
        }
        if (callFor.equals(CallFor.GET_GROUPS)) {
            MessageResponceBean messageResponceBean = new Gson().fromJson(response, MessageResponceBean.class);
            if (messageResponceBean.getCode().equals("SUCCESS")) {
                groupList.clear();
                groupIDList.clear();
                for (int i = 0; i < messageResponceBean.getMessageGroupBeans().size(); i++) {
                    String groupName = messageResponceBean.getMessageGroupBeans().get(i).getGroup_name();
                    int groupId = messageResponceBean.getMessageGroupBeans().get(i).getGroup_id();
                    groupIDList.add(groupId);
                    groupList.add(groupName);
                }
                AdminPoll_RecyclerViewAdapter.setAdapter(groupList, groupIDList);
            } else {
                CommonMethods.showToastError(MainActivity.mainActivity, "Error");//,Toast.LENGTH_LONG).show();
            }
        }
        if (callFor.equals(CallFor.USER_POLLS)) {
            try {
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd").create();
                pollResponce = gson.fromJson(response, PollResponce.class);
                if (pollResponce.getCode().equals("SUCCESS")) {
                    ft = fragmentManager.beginTransaction();
                    PollsFragment.pollResponce = pollResponce;
                    ft.replace(R.id.main, new PollsFragment(), "Data").commitAllowingStateLoss();
                } else if (pollResponce.getCode().equals("ERROR")) {
                    CommonMethods.showToastError(this, pollResponce.getMessage());//, Toast.LENGTH_LONG).show();
                } else if (pollResponce.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                    CallLoginApi();
                } else {
                    CommonMethods.showToastError(this, "Something went wrong in getting polls");//, Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                Log.e(TAG_RESP, "User Poll " + ex);
            }
        }
        if (callFor.equals(CallFor.ADMIN_POLLS)) {
            CreateEditPollsFragment.mSwipeRefreshLayout.setRefreshing(false);
            try {
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd").create();
                adminPollResponce = gson.fromJson(response, PollResponce.class);
                if (adminPollResponce.getCode().equals("SUCCESS")) {
                    try {
                        if (adminPollResponce != null) {
                            if (adminPollResponce.getObjPollBeans().size() != 0) {
                                CreateEditPollsFragment.mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                                CreateEditPollsFragment.NoDataLayout.setVisibility(View.GONE);
                                CreateEditPollsFragment.mAdapter = new AdminPoll_RecyclerViewAdapter(this, adminPollResponce);
                                CreateEditPollsFragment.mRecyclerView.setHasFixedSize(true);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                                CreateEditPollsFragment.mRecyclerView.setLayoutManager(mLayoutManager);
                                CreateEditPollsFragment.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                                CreateEditPollsFragment.mRecyclerView.setAdapter(CreateEditPollsFragment.mAdapter);
                            } else {
                                CreateEditPollsFragment.NoDataLayout.setVisibility(View.VISIBLE);
                                CreateEditPollsFragment.mSwipeRefreshLayout.setVisibility(View.GONE);
                            }
                        } else {
                            CreateEditPollsFragment.NoDataLayout.setVisibility(View.VISIBLE);
                            CreateEditPollsFragment.mSwipeRefreshLayout.setVisibility(View.GONE);
                        }

                    } catch (Exception ex) {
                        Log.e(TAG, " Exception In Adapter Setting " + ex);
                    }

                } else if (adminPollResponce.getCode().equals("ERROR")) {
                    CommonMethods.showToastError(this, adminPollResponce.getMessage());//, Toast.LENGTH_LONG).show();
                } else if (adminPollResponce.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                    CallLoginApi();
                    //Toasty.error(this, "Please try again", Toast.LENGTH_SHORT, true).show();
                } else {
                    CommonMethods.showToastError(this, "Something went wrong in getting polls");//, Toast.LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                Log.e(TAG_RESP, "User Poll " + ex);
            }
        }
        if (callFor.equals(CallFor.SAVE_POLL)) {
            ResponceBean result = new Gson().fromJson(response, ResponceBean.class);
            if (result.getCode().equals("SUCCESS")) {
                CommonMethods.showToastSuccess(this, result.getMessage());//, Toast.LENGTH_LONG).show();
                UserPoll_RecyclerViewAdapter.ClearAllData();
                new GetData(this, CallFor.USER_POLLS, "").execute();
            } else if (result.getCode().equals("ERROR")) {
                CommonMethods.showToastError(this, result.getMessage());//, Toast.LENGTH_LONG).show();
            } else if (result.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                CallLoginApi();
                //Toasty.error(this, "Please try again", Toast.LENGTH_SHORT, true).show();
            } else {
                CommonMethods.showToastError(this, "Something went wrong to save poll");//, Toast.LENGTH_LONG).show();
            }
        }

        if (callFor.equals(CallFor.DOMESTIC_HELP)) {
            domesticHelpResponceBean = new Gson().fromJson(response, DomesticHelpResponceBean.class);
            if (domesticHelpResponceBean.getCode().equals("SUCCESS")) {
                ft.replace(R.id.main, new DomesticHelpFragment(), "DATA").commitAllowingStateLoss();
            } else if (domesticHelpResponceBean.getCode().equals("ERROR")) {
                CommonMethods.showToastError(this, domesticHelpResponceBean.getMessage());//, Toast.LENGTH_LONG).show();
            } else if (domesticHelpResponceBean.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                CallLoginApi();
                //Toasty.error(this, "Please try again", Toast.LENGTH_SHORT, true).show();
            } else {
                CommonMethods.showToastError(this, "Something went wrong");//, Toast.LENGTH_LONG).show();
            }
        }
        if (callFor.equals(CallFor.ADD_CAR_POOL_OFFER)) {
            ResponceBean result = new Gson().fromJson(response, ResponceBean.class);
            if (result.getCode().equals("SUCCESS")) {
                CommonMethods.showToastSuccess(this, result.getMessage());//, Toast.LENGTH_LONG).show();
                CarPoolFragment.ClearAllData();
                new GetData(MainActivity.mainActivity, CallFor.GET_CAR_POOL_OFFERS, "").execute();
            } else if (result.getCode().equals("ERROR")) {
                CommonMethods.showToastError(this, result.getMessage());//, Toast.LENGTH_LONG).show();
            } else if (result.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                CallLoginApi();
                //Toasty.error(this, "Please try again", Toast.LENGTH_SHORT, true).show();
            } else {
                CommonMethods.showToastError(this, "Something went wrong to save ride");//, Toast.LENGTH_LONG).show();
            }
        }
        if (callFor.equals(CallFor.ADD_CAR_POOL_REQUEST)) {
            ResponceBean result = new Gson().fromJson(response, ResponceBean.class);
            if (result.getCode().equals("SUCCESS")) {
                CommonMethods.showToastSuccess(this, result.getMessage());//, Toast.LENGTH_LONG).show();
                CarPoolFragment.ClearAllData();
                new GetData(MainActivity.mainActivity, CallFor.GET_CAR_POOL_REQUEST, "").execute();
            } else if (result.getCode().equals("ERROR")) {
                CommonMethods.showToastError(this, result.getMessage());//, Toast.LENGTH_LONG).show();
            } else if (result.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                CallLoginApi();
            } else {
                CommonMethods.showToastError(this, "Something went wrong to save ride");//, Toast.LENGTH_LONG).show();
            }
        }
        if (callFor.equals(CallFor.GET_CAR_POOL_REQUEST)) {
            if (fragment_flag == 1) {
                RequestRideFragment.mSwipeRefreshLayout.setRefreshing(false);
                fragment_flag = 0;
            }
            CarPoolRequestResponceBean result = new Gson().fromJson(response, CarPoolRequestResponceBean.class);
            if (result.getCode().equals("SUCCESS")) {
                try {
                    ArrayList<CarPoolBean> carPoolBeans = new ArrayList<>();
                    int myRequestSize = result.getMyCarPoolRequests().size();
                    if (myRequestSize != 0) {
                        carPoolBeans.add(0, CreateABlankBean(-1));
                    }
                    int societyRequestSize = result.getSocietyCarPoolRequests().size();
                    carPoolBeans.addAll(result.getMyCarPoolRequests());
                    if (societyRequestSize != 0) {
                        carPoolBeans.add(CreateABlankBean(-2));
                    }
                    carPoolBeans.addAll(result.getSocietyCarPoolRequests());

                    if (myRequestSize == 0 && societyRequestSize == 0) {
                        RequestRideFragment.noDataLayout.setVisibility(View.VISIBLE);
                        RequestRideFragment.mSwipeRefreshLayout.setVisibility(View.GONE);
                    } else {
                        RequestRideFragment.noDataLayout.setVisibility(View.GONE);
                        RequestRideFragment.mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        RequestRideFragment.mAdapter = new CarPoolRequest_RecyclerViewAdapter(this, carPoolBeans);
                        RequestRideFragment.mRecyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                        RequestRideFragment.mRecyclerView.setLayoutManager(mLayoutManager);
                        RequestRideFragment.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        RequestRideFragment.mRecyclerView.setAdapter(RequestRideFragment.mAdapter);
                    }
                } catch (Exception ex) {
                    Log.e(TAG, " Exception In Adapter Setting " + ex);
                }
            } else if (result.getCode().equals("ERROR")) {
                CommonMethods.showToastError(this, result.getMessage());//, Toast.LENGTH_LONG).show();
            } else if (result.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                CallLoginApi();
                //Toasty.error(this, "Please try again", Toast.LENGTH_SHORT, true).show();
            } else {
                CommonMethods.showToastError(this, "Something went wrong to save ride");//, Toast.LENGTH_LONG).show();
            }
        }

        if (callFor.equals(CallFor.GET_CAR_POOL_OFFERS)) {
            if (fragment_flag == 1) {
                OfferRideFragment.mSwipeRefreshLayout.setRefreshing(false);
                fragment_flag = 0;
            }
            CarPoolOfferResponceBean result = new Gson().fromJson(response, CarPoolOfferResponceBean.class);
            if (result.getCode().equals("SUCCESS")) {
                try {
                    ArrayList<CarPoolBean> carPoolBeans = new ArrayList<>();
                    int myOfferSize = result.getMyCarPoolOffers().size();
                    if (myOfferSize != 0) {
                        carPoolBeans.add(0, CreateABlankBean(-1));
                    }
                    carPoolBeans.addAll(result.getMyCarPoolOffers());

                    int societyOfferSize = result.getSocietyCarPoolOffers().size();
                    if (societyOfferSize != 0) {
                        carPoolBeans.add(CreateABlankBean(-2));
                    }
                    carPoolBeans.addAll(result.getSocietyCarPoolOffers());
                    if (myOfferSize == 0 && societyOfferSize == 0) {
                        OfferRideFragment.noDataLayout.setVisibility(View.VISIBLE);
                        OfferRideFragment.mSwipeRefreshLayout.setVisibility(View.GONE);
                    } else {
                        OfferRideFragment.noDataLayout.setVisibility(View.GONE);
                        OfferRideFragment.mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        OfferRideFragment.mAdapter = new CarPoolOffer_RecyclerViewAdapter(this, carPoolBeans);
                        OfferRideFragment.mRecyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                        OfferRideFragment.mRecyclerView.setLayoutManager(mLayoutManager);
                        OfferRideFragment.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        OfferRideFragment.mRecyclerView.setAdapter(OfferRideFragment.mAdapter);
                    }
                } catch (Exception ex) {
                    Log.e(TAG, " Exception In Adapter Setting " + ex);
                }
            } else if (result.getCode().equals("ERROR")) {
                CommonMethods.showToastError(this, result.getMessage());//, Toast.LENGTH_LONG).show();
            } else if (result.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                CallLoginApi();
            } else {
                CommonMethods.showToastError(this, "Something went wrong to save ride");//, Toast.LENGTH_LONG).show();
            }
        }
        if (callFor.equals(CallFor.CLOSE_CAR_POOL_OFFERS)) {
            ResponceBean result = new Gson().fromJson(response, ResponceBean.class);
            if (result.getCode().equals("SUCCESS")) {
                CommonMethods.showToastSuccess(this, result.getMessage());//, Toast.LENGTH_LONG).show();
                new GetData(MainActivity.mainActivity, CallFor.GET_CAR_POOL_OFFERS, "").execute();
            } else if (result.getCode().equals("ERROR")) {
                CommonMethods.showToastError(this, result.getMessage());//, Toast.LENGTH_LONG).show();
            } else if (result.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                CallLoginApi();
                //Toasty.error(this, "Please try again", Toast.LENGTH_SHORT, true).show();
            } else {
                CommonMethods.showToastError(this, "Something went wrong");//, Toast.LENGTH_LONG).show();
            }
        }
        if (callFor.equals(CallFor.CLOSE_CAR_POOL_REQUEST)) {
            ResponceBean result = new Gson().fromJson(response, ResponceBean.class);
            if (result.getCode().equals("SUCCESS")) {
                CommonMethods.showToastSuccess(this, result.getMessage());//, Toast.LENGTH_LONG).show();
                new GetData(MainActivity.mainActivity, CallFor.GET_CAR_POOL_REQUEST, "").execute();
            } else if (result.getCode().equals("ERROR")) {
                CommonMethods.showToastError(this, result.getMessage());//, Toast.LENGTH_LONG).show();
            } else if (result.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                CallLoginApi();
            } else {
                CommonMethods.showToastError(this, "Something went wrong");//, Toast.LENGTH_LONG).show();
            }
        }

        // Market Place Response
        if (callFor.equals(CallFor.GET_ALL_MARKET_PLACE_ADD)) {
            MarketPlaceFragment.context.supportInvalidateOptionsMenu();
            MarketPlaceFragment.mSwipeRefreshLayout.setRefreshing(false);
            marketPlaceResponceBean = new Gson().fromJson(response, MarketPlaceResponceBean.class);
            MarketPlaceFragment.filteredMarketPlaceResponceBean = new Gson().fromJson(response, MarketPlaceResponceBean.class);
            if (marketPlaceResponceBean.getCode().equals("SUCCESS")) {
                try {
                    int adSize = marketPlaceResponceBean.getAdvertises().size();
                    if (adSize == 0) {
                        MarketPlaceFragment.NoDataLayout.setVisibility(View.VISIBLE);
                        MarketPlaceFragment.mSwipeRefreshLayout.setVisibility(View.GONE);
                    } else {
                        MarketPlaceFragment.NoDataLayout.setVisibility(View.GONE);
                        MarketPlaceFragment.mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                        MarketPlaceFragment.mAdapter = new MarketPlace_RecyclerViewAdapter(this, MarketPlaceFragment.filteredMarketPlaceResponceBean.getAdvertises());
                        MarketPlaceFragment.mRecyclerView.setHasFixedSize(true);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                        MarketPlaceFragment.mRecyclerView.setLayoutManager(mLayoutManager);
                        MarketPlaceFragment.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        MarketPlaceFragment.mRecyclerView.setAdapter(MarketPlaceFragment.mAdapter);
                    }

                } catch (Exception ex) {
                    Log.e(TAG, " Exception In Adapter Setting " + ex);
                }
            } else if (marketPlaceResponceBean.getCode().equals("ERROR")) {
                CommonMethods.showToastError(this, marketPlaceResponceBean.getMessage());//, Toast.LENGTH_LONG).show();
            } else if (marketPlaceResponceBean.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                CallLoginApi();
                //Toasty.error(this, "Please try again", Toast.LENGTH_SHORT, true).show();
            } else {
                CommonMethods.showToastError(this, "Something went wrong");//, Toast.LENGTH_LONG).show();
            }
        }
        if (callFor.equals(CallFor.RAINBOW_USERS)) {
            SheredPref.setRun(this, "RUN");
            RainbowUserResponse rainbowUserResponse = new Gson().fromJson(response, RainbowUserResponse.class);
            RainbowUsersListResponse rainbowUsersListResponse = new Gson().fromJson(response, RainbowUsersListResponse.class);
            if (rainbowUserResponse.getCode().equals("SUCCESS")) {
                Rainbow_Create_Group.selectedRaibowContacts.clear();
                adminList.clear();
                userList.clear();
                vendorList.clear();
                rainbowUsers = rainbowUserResponse.getRainbowUsers();
                rainbowUsersList = rainbowUsersListResponse.getRainbowUsersList();
                for (int i = 0; i < rainbowUsersList.get(0).size(); i++) {
                    FamilyBean familyBean = rainbowUsersList.get(0).get(i);
                    familyBean.setAdmin(true);
                    adminList.add(familyBean);
                }

                for (int i = 0; i < rainbowUsersList.get(1).size(); i++) {
                    FamilyBean familyBean = rainbowUsersList.get(1).get(i);
                    familyBean.setAdmin(false);
                    userList.add(familyBean);
                }
                for (int i = 0; i < rainbowUsersList.get(2).size(); i++) {
                    FamilyBean familyBean = rainbowUsersList.get(2).get(i);
                    vendorList.add(familyBean);
                }
                if (callFromRainbowProfile) {
                    callFromRainbowProfile = false;
                    UpdateDatasetWithVisitorList();
                    getSupportActionBar().setTitle(title);
                    CommonMethods.showToastSuccess(this, "Account change Successfully");//, Toast.LENGTH_SHORT).show();

                } else {
                    ft.replace(R.id.main, new Rainbow_Fragment(), "DATA").commitAllowingStateLoss();
                }
                Log.e(TAG, " Loaded Rainbow User");
            }
        }

        if (callFor.equals(CallFor.RAINBOW_ACCOUNT_DETAILS)) {
            RainbowDetailsResponce rainbowDetailsResponce = new Gson().fromJson(response, RainbowDetailsResponce.class);
            if (rainbowDetailsResponce.getCode().equals("SUCCESS")) {
                ShowRainbowAccountChooserDialog(this, rainbowDetailsResponce.getRainbowDetailsBeans());
            } else if (rainbowDetailsResponce.getCode().equals("ERROR")) {
                Log.e(TAG, " Error Response in getting rainbow account " + rainbowDetailsResponce.getMessage());
            } else {
                Log.e(TAG, " Something went wrong");
            }
        }
    }

    private void updateUIAsLoading() {
        NoVisiterLayout.setVisibility(View.GONE);
        llwelcomeuser.setVisibility(View.VISIBLE);
    }

    private CarPoolBean CreateABlankBean(int id) {
        CarPoolBean carPoolBean = new CarPoolBean();
        carPoolBean.setId(id);
        return carPoolBean;
    }

    public void showUpdateDialog(String versionInfo, Boolean value) {
        isUpdateAlertShow = true;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.custum_alert_dialog, null);
        dialogBuilder.setView(dialogView);
        TextView txtalertheading = dialogView.findViewById(R.id.txtalertheading);
        TextView txtalertsubheading = dialogView.findViewById(R.id.txtalertsubheading);

        TextView btnegative = dialogView.findViewById(R.id.btnegative);
        TextView btnpositive = dialogView.findViewById(R.id.btnpositive);

        txtalertheading.setText("Update VZTrack Application");
        txtalertsubheading.setText("New version of VZTrack application available on play store. Please update application to get new features - " + versionInfo);

        btnegative.setVisibility(View.VISIBLE);


        final AlertDialog b = dialogBuilder.create();
        if (b != null)
            b.dismiss();

        b.setCanceledOnTouchOutside(false);
        b.setCancelable(false);
        b.show();
        btnpositive.setText("Update");


        btnpositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse(getResources().getString(R.string.app_link)));
                startActivity(viewIntent);
            }
        });

        if (value == true) {
            btnpositive.setText(getResources().getString(R.string.exit));

            btnpositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                    homeIntent.addCategory(Intent.CATEGORY_HOME);
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                    finish();
                    b.dismiss();

                }
            });

        } else {
            btnegative.setText(getResources().getString(R.string.later));
            btnegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SheredPref.setLatestAppVersion(mainActivity, appVersion);
                    b.dismiss();

                }
            });
        }
    }

    public void CallLoginApi() {
        UserBean userBean = new UserBean();
        userBean.setUser_name(SheredPref.getUsername(getApplicationContext()));
        userBean.setUser_password(SheredPref.getPassword(getApplicationContext()));
        userBean.setDevice_os(Finals.AND);
        userBean.setAppVersion(UtilityMethods.getVersionCode(context));
        new PostData(new Gson().toJson(userBean), MainActivity.this, CallFor.LOGIN).execute();
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
                }
            });
        } else {
//            RainbowSdk.instance().webRTC().unregisterTelephonyListener(callListner);//uncomment by pratima
            RainbowSdk.instance().webRTC().registerTelephonyListener(callListner);
        }
    }

    public void loginInRainbow(final FamilyBean familyBean) {
        String emailId = familyBean.getRainbowEmailId();
        String password = familyBean.getRainbowPassword();
        String host = Constants.RAINBOW_ENVIRONMENT;
        RainbowSdk.instance().connection().signin(emailId, password, host, new SigninResponseListener() {
            @Override
            public void onSigninSucceeded() {
                SheredPref.setRainbowEmailId(mainActivity, familyBean.getRainbowEmailId());
                SheredPref.setRainbowPassword(mainActivity, familyBean.getRainbowPassword());
                SheredPref.setRainbowLogin(mainActivity, true);
                RainbowSdk.instance().webRTC().unregisterTelephonyListener(callListner);
                RainbowSdk.instance().webRTC().registerTelephonyListener(callListner);
            }

            @Override
            public void onRequestFailed(RainbowSdk.ErrorCode errorCode, String s) {
                SheredPref.setRainbowLogin(mainActivity, false);
            }
        });
    }

    public void LogoutRainbow(FamilyBean familyBean) {
        if (RainbowSdk.instance().connection().isConnected()) {
            RainbowSdk.instance().getContext().getCacheDir().delete();
            RainbowSdk.instance().webRTC().unregisterTelephonyListener(callListner);
            RainbowSdk.instance().connection().uninitialize();
            RainbowSdk.instance().connection().signout(new SignoutResponseListener() {
                @Override
                public void onSignoutSucceeded() {
                    ImNotificationMgr imNotificationMgr = new ImNotificationMgr();
                    imNotificationMgr.unRegisterListener();
                    if (familyBean != null) {
                        if (ImNotificationMgr.getInstance() == null) {
                            new ImNotificationMgr(MainActivity.this);
                        }

                        startRainbow(familyBean);

                    }
                }
            });
        } else {
        }
    }

    public boolean CheckPermissionsCamAndAudio() {
        boolean val = false;
        boolean resultCamera = Permissions.askPermission(this, PermissionConstant.PERMISSION_CAMERA, PermissionConstant.REQ_CODE_CAMERA);
        isCameFromPermission = true;
        if (resultCamera) {
            boolean resultAudio = Permissions.askPermission(this, PermissionConstant.PERMISSION_AUDIO, PermissionConstant.REQ_CODE_AUDIO);
            if (resultAudio) {
                boolean resultPhoneStorage = Permissions.askPermission(this, PermissionConstant.PERMISSION_EXTERNAL_STORAGE, PermissionConstant.REQ_CODE_EXTERNAL_STORAGE);
                if (resultPhoneStorage) {
                    boolean resultReadContact = Permissions.askPermission(this, PermissionConstant.PERMISSION_READ_CONTACT, PermissionConstant.REQ_CODE_READ_CONTACT);
                    if (resultReadContact) {
                        boolean resultPhoneState = Permissions.askPermission(this, PermissionConstant.PERMISSION_READ_PHONE_STATE, PermissionConstant.REQ_CODE_READ_PHONE_STATE);
                        if (resultPhoneState) {
                            boolean resultPhoneCall = Permissions.askPermission(this, PermissionConstant.PERMISSION_CALL_PHONE, PermissionConstant.REQ_CODE_CALL_PHONE);
                            if (resultPhoneCall) {
                                val = true;
                            }
                        }
                    }
                }
            }
        }
        return val;
    }

    public void SetupDrawer() {
        drawerConfigs = new ArrayList<>();
        ImageView circleView = findViewById(R.id.circleViewProfile);
        imgvzlogo.setImageDrawable(getResources().getDrawable(R.drawable.logo_vz_track));
        boolean visitorAccess = SheredPref.getAccess_Visitor(this);
        isVisitorAccessMainScreen = visitorAccess;
        loadGridData();
    }


    public void ShowRainbowAccountChooserDialog(Context context, ArrayList<RainbowDetailsBean> rainbowDetailsBeans) {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.radiobutton_dialog, null);
        dialogBuilder.setView(dialogView);
        int alreadySelectedAccount = -2;
        String rainbowEmail = SheredPref.getRainbowEmailId(context);
        List<String> stringList = new ArrayList<>();
        stringList.clear();
        for (int i = 0; i < rainbowDetailsBeans.size(); i++) {
            if (rainbowEmail.equals(rainbowDetailsBeans.get(i).getEmailId())) {
                alreadySelectedAccount = i;
            }
            stringList.add((i + 1) + " " + rainbowDetailsBeans.get(i).getName());
        }
        RadioGroup rg = dialogView.findViewById(R.id.radio_group);
        TextView btnskip = dialogView.findViewById(R.id.btnskip);
        TextView btnlogin = dialogView.findViewById(R.id.btnlogin);
//        RadioGroup rg = (RadioGroup) dialogView.findViewById(R.id.radio_group);
        for (int i = 0; i < stringList.size(); i++) {
            RadioButton rb = (RadioButton) getLayoutInflater().inflate(R.layout.radio_button, null, false);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.setMargins(5, 5, 5, 5);

            rb.setText(stringList.get(i));
            rb.setId(i);
            rg.addView(rb);
        }
        for (int i = 0; i < rainbowDetailsBeans.size(); i++) {
            if (rainbowEmail.equals(rainbowDetailsBeans.get(i).getEmailId())) {
                rg.check(i);
            }
        }
        android.app.AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        int finalAlreadySelectedAccount = alreadySelectedAccount;

        btnskip.setOnClickListener(v -> {
            callFromRainbowProfile = false;
            alertDialog.dismiss();
        });

        btnlogin.setOnClickListener(v1 -> {
            alertDialog.dismiss();
            if (cc.isConnectingToInternet()) {
                FamilyBean familyBean = new FamilyBean();
                int checkedRadioButtonId = rg.getCheckedRadioButtonId();
                if (checkedRadioButtonId == finalAlreadySelectedAccount) {
                    CommonMethods.showToastSuccess(context, "You already logged in with same account");//, Toast.LENGTH_SHORT).show();
                } else if (checkedRadioButtonId == -1) {
                    CommonMethods.showToastSuccess(context, getResources().getString(R.string.select_acct));//, Toast.LENGTH_SHORT).show();
                } else {
                    familyBean.setRainbowEmailId(rainbowDetailsBeans.get(checkedRadioButtonId).getEmailId());
                    familyBean.setRainbowPassword(rainbowDetailsBeans.get(checkedRadioButtonId).getPassword());
                    if (RainbowSdk.instance().connection().isConnected()) {
                        LogoutRainbow(familyBean);
                    } else {
                        startRainbow(familyBean);
                    }

                    new GetData(MainActivity.this, CallFor.RAINBOW_USERS, SheredPref.getRainbowEmailId(context)).execute();
                    CleverTapRegisterEvents.addCleverTapEvent(mainActivity, Events.event_rainbow_screen);
                    ShowUI();
                }
            } else {
                UtilityMethodsAndroid.ShowNoInternetToast(MainActivity.this);
            }
        });

        dialogBuilder.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        callFromRainbowProfile = false;
                    }
                }
        );

        dialogBuilder.create();
        dialogBuilder.show();
    }


    public void showDueInvoiceDialog() {
        class LogCall extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] params) {
                String url = Constants.BASE_URL + URL.DUE_INVOICE_INFO;
                String result = ServerConnection.giveResponse(url, "", null);
                try {
                    JSONObject obj = new JSONObject(result);
                    if (obj.getBoolean("invoiceDue") == true) {
                        due = true;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

                if (due) {

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View dialogView = inflater.inflate(R.layout.custum_alert_dialog, null);
                    dialogBuilder.setView(dialogView);
                    TextView txtalertheading = dialogView.findViewById(R.id.txtalertheading);
                    TextView txtalertsubheading = dialogView.findViewById(R.id.txtalertsubheading);

                    TextView btnegative = dialogView.findViewById(R.id.btnegative);
                    TextView btnpositive = dialogView.findViewById(R.id.btnpositive);

                    txtalertheading.setText("Invoice Due");
                    txtalertsubheading.setText("");
                    btnegative.setVisibility(View.VISIBLE);
                    final AlertDialog b = dialogBuilder.create();
                    b.setCanceledOnTouchOutside(false);
                    b.setCancelable(false);
                    b.show();

                    btnpositive.setText("Check");
                    btnegative.setText("Cancel");
                    btnpositive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            b.dismiss();
                            ShowUI();
                            ft = fragmentManager.beginTransaction();
                            ft.replace(R.id.main, new InvoiceFragment(), "Data").commitAllowingStateLoss();
//                            getSupportActionBar().setTitle("Invoice");
                        }
                    });
                    btnegative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            b.dismiss();
                        }
                    });
                }
            }
        }
        new LogCall().execute();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data1111) {
        super.onActivityResult(requestCode, resultCode, data1111);
        if (requestCode == REQUEST_PREAPPROVAL) {
            if (resultCode == RESULT_OK) {
                try {
                    String returnedResult = data1111.getData().toString();
                    if (returnedResult.equalsIgnoreCase("Preapprovallist")) {
                        title = "Pre Approval";
                        if (cc.isConnectingToInternet()) {
                            ShowUI();
                            if (drFlag != 22) {
                                ft = fragmentManager.beginTransaction();
                                ft.replace(R.id.main, new ApprovalFragment(), "Data").commitAllowingStateLoss();
                            }
                        } else {
                            ft = fragmentManager.beginTransaction();
                            ft.replace(R.id.main, new NoInternetFragment(), "Data").commitAllowingStateLoss();
                        }
                        drFlag = 22;
                        //getSupportActionBar().setTitle(title);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadGridData() {
        ArrayList<ConfigBean> drawerConfigArrayList = null;//=new DrawerConfigArrayList();
        ImageView circleView = findViewById(R.id.circleViewProfile);

        Glide.with(this)
                .load(UtilityMethods.getUserProfilePhoto(context))
                .placeholder(R.drawable.ic_avatar)
                .error(R.drawable.ic_add_user)
                .skipMemoryCache(false)
                .thumbnail(0.4f)
                .into(circleView);

        ArrayList<ConfigBean> freqdrawerConfigs = new ArrayList<>();
        ArrayList<DrawerConfigParent> drawerConfigParents = new Gson().fromJson(SheredPref.getDrawerMenuList(context), new TypeToken<ArrayList<DrawerConfigParent>>() {
        }.getType());
        if (drawerConfigParents != null) {
            for (int i = 1; i < drawerConfigParents.size(); i++) {
                freqdrawerConfigs.addAll(drawerConfigParents.get(i).getContents());
            }
        }
        ArrayList<ConfigBean> storedfreqdrawerConfigs = new ArrayList<>();
        storedfreqdrawerConfigs = new Gson().fromJson(SheredPref.getFrequentlyUsedMenuList(context), new TypeToken<ArrayList<ConfigBean>>() {
        }.getType());
        if (storedfreqdrawerConfigs == null) {
            SheredPref.setFrequentlyUsedMenuList(MainActivity.this, freqdrawerConfigs);
        } else {
            for (int i = 0; i < freqdrawerConfigs.size(); i++) {
                for (int j = 0; j < storedfreqdrawerConfigs.size(); j++) {
                    if (freqdrawerConfigs.get(i).getName().equalsIgnoreCase(storedfreqdrawerConfigs.get(j).getName())) {
                        freqdrawerConfigs.get(i).setFrequentUsedCount(storedfreqdrawerConfigs.get(j).getFrequentUsedCount());
                        break;
                    }
                }

            }
            SheredPref.setFrequentlyUsedMenuList(MainActivity.this, freqdrawerConfigs);
        }
        circleView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SettingDetails.class);
            intent.putExtra("FLAG", "PROF");
            startActivity(intent);
        });

        if (SheredPref.getFrequentlyUsedMenuList(context) != null) {
            drawerConfigArrayList = new Gson().fromJson(SheredPref.getFrequentlyUsedMenuList(context), new TypeToken<ArrayList<ConfigBean>>() {
            }.getType());
            boolean visitorAccess = SheredPref.getAccess_Visitor(this);
            isVisitorAccessMainScreen = visitorAccess;

            //for setting icon
            boolean settingAccess = SheredPref.getAccess_Setting(this);
            if (settingAccess) {
                imgsetting.setVisibility(View.VISIBLE);
            } else {
                imgsetting.setVisibility(View.GONE);
            }

            if (drawerConfigArrayList != null) {
                for (int i = 0; i < drawerConfigArrayList.size(); i++) {
                    CommonMethods.updateConfigBean(drawerConfigArrayList.get(i), context);
                }
            }
        }
        setUpRecyclerView();
        populateRecyclerView();
    }

    public void ShowFragment(ConfigBean drawerConfig) {
        String menuName = drawerConfig.getName();
        String widgetName = drawerConfig.getWidgetType();
        ft = fragmentManager.beginTransaction();
        if (widgetName != null && widgetName.equals("webview")) {
            title = drawerConfig.getTitle();
            drFlag = 24;
            if (cc.isConnectingToInternet()) {
                String url = drawerConfig.getUrl();
                if (url != null && !url.equals("")) {
                    ShowUI();
                    Bundle bundle = new Bundle();
                    bundle.putString("URL", BASE_URL + url);
                    bundle.putString("POST_DATA", UtilityMethods.getPostData(MainActivity.this));
                    bundle.putString("ACTION_BAR_TITLE", title);
                    ExtraFieldFragmentOne fragmentOne = new ExtraFieldFragmentOne();
                    fragmentOne.setArguments(bundle);
                    ft.replace(R.id.main, fragmentOne, "Data").commitAllowingStateLoss();
                } else {
                    CommonMethods.showToastSuccess(context, "Unable to perform action, Empty Url");
                }
            } else {
                ft.replace(R.id.main, new NoInternetFragment(), "Data").commitAllowingStateLoss();
            }
        } else {
            if (menuName.equalsIgnoreCase(getResources().getString(R.string.admin))) {
                drFlag = -1;
            } else if (menuName.equalsIgnoreCase(getResources().getString(R.string.home))) {
                title = "Home";
                drFlag = 0;
                isVisitorAccessMainScreen = false;
                ft.replace(R.id.main, new HomeScreenVisitorFragment(), "Data").commitAllowingStateLoss();
            } else if (menuName.equalsIgnoreCase(getResources().getString(R.string.visitors))) {
                if (isVisitorAccessMainScreen == true) {
                    title = "Visitors";
                    UpdateDatasetWithVisitorList();
                    drFlag = 0;
                } else {
                    title = "Home";
                    drFlag = 0;
                    isVisitorAccessMainScreen = false;
                    ft.replace(R.id.main, new HomeScreenVisitorFragment(), "Data").commitAllowingStateLoss();
                }

            } else if (menuName.equalsIgnoreCase(getResources().getString(R.string.noticenminutes)) || menuName.equalsIgnoreCase(getResources().getString(R.string.createNotice))) {
                title = "Notices And Minutes";
                ShowUI();
                if (cc.isConnectingToInternet()) {
                    if (drFlag != 1) {
                        new GetData(MainActivity.this, CallFor.NOTICES, "").execute();
                    }
                } else {
                    ft.replace(R.id.main, new NoticesFragment(), "Data").commitAllowingStateLoss();
                }
                drFlag = 1;
            } else if (menuName.equalsIgnoreCase(getResources().getString(R.string.message)) || menuName.equalsIgnoreCase(getResources().getString(R.string.createMessage))) {
                title = "Message";
                ShowUI();
                if (cc.isConnectingToInternet()) {
                    if (drFlag != 2) {
                        if (menuName.equalsIgnoreCase(getResources().getString(R.string.createMessage))) {
                            sentMessage = 1;
                            ft.replace(R.id.main, new MessageFragment(), "Data").commitAllowingStateLoss();
                        } else if (menuName.equalsIgnoreCase(getResources().getString(R.string.message))) {
                            updatedMessageResponceBean.clear();
                            message_PageNo = 0;
                            sentMessage = 2;
                            new GetData(MainActivity.this, CallFor.MESSAGE, "" + 0).execute();
                        }
                    }

                } else {
                    ft.replace(R.id.main, new NoInternetFragment(), "DATA").commitAllowingStateLoss();
                }
                drFlag = 2;
            } else if (menuName.equalsIgnoreCase(getResources().getString(R.string.serviceprovider))) {
                drFlag = 25;
                title = getResources().getString(R.string.serviceprovider);
                ShowUI();
                ft.replace(R.id.main, new Nw_service_provider_fragment(), "DATA").commitAllowingStateLoss();
            } else if (menuName.equalsIgnoreCase(getResources().getString(R.string.servicefeedback))) {
                title = "Service Feedback";
                if (cc.isConnectingToInternet()) {
                    if (drFlag != 3) {
                        RatingResponseBean ratingResponseBean = new RatingResponseBean();
                        ratingResponseBean.setSocietyId(Integer.parseInt(SheredPref.getSocietyId(mainActivity)));
                        ratingResponseBean.setFamilyId(Integer.parseInt(SheredPref.getFamilyId(mainActivity)));
                        new PostData(new Gson().toJson(ratingResponseBean), MainActivity.this, CallFor.PENDING_RATING).execute();
                        ShowUI();
                    }
                } else {
                    ft.replace(R.id.main, new NoInternetFragment(), "DATA").commitAllowingStateLoss();
                }
                drFlag = 3;
            } else if (menuName.equalsIgnoreCase(getResources().getString(R.string.searchprovider))) {
                title = "Search Provider";
                if (drFlag != 4) {
                    if (cc.isConnectingToInternet()) {
                        ShowUI();
                        ft.replace(R.id.main, new SearchProviderFragment(), "Data").commitAllowingStateLoss();
                    } else {
                        ft.replace(R.id.main, new NoInternetFragment(), "DATA").commitAllowingStateLoss();
                    }
                    drFlag = 4;
                }
            } else if (menuName.equalsIgnoreCase(getResources().getString(R.string.logout))) {
                outputBeanSearchProviders.clear();
                if (cc.isConnectingToInternet()) {
                    androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mainActivity, R.style.AppCompatAlertDialogStyle);
                    builder.setMessage("Are you sure? If you logout you will not receive visitor notification");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            String device_id = Settings.Secure.getString(mainActivity.getContentResolver(), Settings.Secure.ANDROID_ID);
                            UserBean userBean = new UserBean();
                            userBean.setUser_dev_id(device_id);
                            new PostData(new Gson().toJson(userBean), MainActivity.this, CallFor.LOGOUT).execute();
                            LoginScreenActivity.splashFlag = false;
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    CommonMethods.showToastSuccess(this, "Unable to logout, Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                }
            } else if (menuName.equalsIgnoreCase(getResources().getString(R.string.refervztrack))) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "VZTrack");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                CleverTap.cleverTap_Record_Event(this, Events.event_refer);
            } else if (menuName.equalsIgnoreCase(getResources().getString(R.string.searchnaddvehicle))) {
                title = "Vehicle Search";
                CleverTap.cleverTap_Record_Event(this, Events.event_search_vehicle_screen);
                if (drFlag != 5) {
                    if (cc.isConnectingToInternet()) {
                        NoVisiterLayout.setVisibility(View.VISIBLE);
                        llwelcomeuser.setVisibility(View.GONE);
                        ft.replace(R.id.main, new VehiclesFragment(), "Data").commitAllowingStateLoss();
                    } else {
                        ft.replace(R.id.main, new NoInternetFragment(), "DATA").commitAllowingStateLoss();
                    }
                    drFlag = 5;
                }

            } else if (menuName.equalsIgnoreCase(getResources().getString(R.string.supprot_chat))) {
                title = "Support";
                if (drFlag != 7) {
                    if (cc.isConnectingToInternet()) {
                        NoVisiterLayout.setVisibility(View.VISIBLE);
                        llwelcomeuser.setVisibility(View.GONE);
                        ft.replace(R.id.main, new SupportFragment(), "Data").commitAllowingStateLoss();
                    } else {
                        ft.replace(R.id.main, new SupportFragment(), "Data").commitAllowingStateLoss();
                    }
                    drFlag = 7;
                }
            } else if (menuName.equalsIgnoreCase(getResources().getString(R.string.complaintregister)) || menuName.equalsIgnoreCase(getResources().getString(R.string.createComplaint))) {
                title = "Complaint Register";
                if (drFlag != 8) {
                    if (cc.isConnectingToInternet()) {
                        NoVisiterLayout.setVisibility(View.VISIBLE);
                        llwelcomeuser.setVisibility(View.GONE);
                        ft.replace(R.id.main, new ComplaintFragment(), "Data").commitAllowingStateLoss();
                    } else {
                        ft.replace(R.id.main, new NoInternetFragment(), "DATA").commitAllowingStateLoss();
                    }
                    drFlag = 8;
                }
            } else if (menuName.equalsIgnoreCase(getResources().getString(R.string.createMessage))) {
                drFlag = 9;
            } else if (menuName.equalsIgnoreCase(getResources().getString(R.string.back))) {
                drFlag = 10;
                SetupDrawer();
            } else if (menuName.equalsIgnoreCase(getResources().getString(R.string.localstores))) {
                title = "Local Stores";
                ShowUI();
                if (drFlag != 11) {
                    if (cc.isConnectingToInternet()) {
                        ft.replace(R.id.main, new LocalStoreFragment(), "DATA").commitAllowingStateLoss();
                        new GetData(this, CallFor.LOCAL_STORES, "").execute();
                        CleverTap.cleverTap_Record_Event(this, Events.local_store_open);
                    } else {
                        ft.replace(R.id.main, new NoInternetFragment(), "DATA").commitAllowingStateLoss();
                    }
                }
                drFlag = 11;
            } else if (menuName.equalsIgnoreCase(getResources().getString(R.string.knowyourmaid))) {
                title = getResources().getString(R.string.daily_help);
                ShowUI();
                if (drFlag != 13) {
                    if (cc.isConnectingToInternet()) {
                        new GetData(this, CallFor.DOMESTIC_HELP, "All").execute();
                        CleverTap.cleverTap_Record_Event(this, Events.domestic_help_open);
                    } else {
                        ft.replace(R.id.main, new NoInternetFragment(), "DATA").commitAllowingStateLoss();
                    }
                }
                drFlag = 13;
            }
            if (menuName.equalsIgnoreCase(getResources().getString(R.string.polls)) || menuName.equalsIgnoreCase(getResources().getString(R.string.createPoll))) {
                title = "Polls";
                if (cc.isConnectingToInternet()) {
                    ShowUI();
                    if (drFlag != 12) {
                        if (menuName.equalsIgnoreCase(getResources().getString(R.string.createPoll))) {
                            callFromAdmin = true;
                        }
                        new GetData(this, CallFor.USER_POLLS, "").execute();
                    }
                } else {
                    ft.replace(R.id.main, new NoInternetFragment(), "Data").commitAllowingStateLoss();
                }
                drFlag = 12;
            }
            if (menuName.equalsIgnoreCase(getResources().getString(R.string.carpool))) {
                title = "Carpool";
                if (cc.isConnectingToInternet()) {
                    ShowUI();
                    if (drFlag != 14) {
                        ft.replace(R.id.main, new CarPoolFragment(), "Data").commitAllowingStateLoss();
                    }
                } else {
                    ft.replace(R.id.main, new NoInternetFragment(), "Data").commitAllowingStateLoss();
                }
                drFlag = 14;
            }
            if (menuName.equalsIgnoreCase(getResources().getString(R.string.marketplace))) {
                title = "Marketplace";
                if (cc.isConnectingToInternet()) {
                    ShowUI();
                    if (drFlag != 15) {
                        ft.replace(R.id.main, new MarketPlaceFragment(), "Data").commitAllowingStateLoss();
                    }
                } else {
                    ft.replace(R.id.main, new NoInternetFragment(), "Data").commitAllowingStateLoss();
                }
                drFlag = 15;
            } else if (menuName.equalsIgnoreCase(getResources().getString(R.string.rainbowresidents))) {
                if (cc.isConnectingToInternet()) {
                    if (RainbowSdk.instance().connection().isConnected()) {
                        new GetData(MainActivity.mainActivity, CallFor.RAINBOW_USERS, SheredPref.getRainbowEmailId(this)).execute();
                        CleverTapRegisterEvents.addCleverTapEvent(mainActivity, Events.event_rainbow_screen);
                        ShowUI();
                    } else if (RainbowSdk.instance().connection().isInProgress()) {
                        CommonMethods.showToastError(mainActivity, getResources().getString(R.string.wait));//.show();
                    } else if (RainbowSdk.instance().connection().isDisconnected() && SheredPref.getRainbowAccountCount(MainActivity.this) >= 2) {
                        new GetData(this, CallFor.RAINBOW_ACCOUNT_DETAILS, "").execute();
                    } else if (RainbowSdk.instance().connection().isDisconnected() && SheredPref.getRainbowAccountCount(MainActivity.this) < 2) {
                        CommonMethods.showToastSuccess(mainActivity, getResources().getString(R.string.no_connected));//.show();
                    }
                } else {
                    title = "Rainbow Residents";
                    ft.replace(R.id.main, new NoInternetFragment(), "DATA").commitAllowingStateLoss();
                }
                drFlag = 20;
            } else if (menuName.equalsIgnoreCase(getResources().getString(R.string.vehicleservice))) {
                ShowUI();
                title = "Vehicle Servicing";
                ft.replace(R.id.main, new VehicleServicing(), "DATA").commitAllowingStateLoss();
                CleverTapRegisterEvents.addCleverTapEvent(mainActivity, Events.event_vehicle_servicing_screen);
                drFlag = 21;
            } else if (menuName.equalsIgnoreCase(getResources().getString(R.string.preapproval))) {
                boolean invitationAccess = SheredPref.getInvitationAccess(this);
                boolean approvalaccess = SheredPref.getPreApprovalsAccess(this);
                if (invitationAccess && approvalaccess) {
                    title = "Pre Approval";
                } else if (!invitationAccess || approvalaccess) {
                    title = "Pre Approval";
                } else if (invitationAccess || !approvalaccess) {
                    title = "Invite Guest";
                }
                if (cc.isConnectingToInternet()) {
                    ShowUI();
                    if (drFlag != 22) {
                        ft.replace(R.id.main, new ApprovalFragment(), "Data").commitAllowingStateLoss();
                    }
                } else {
                    ft.replace(R.id.main, new NoInternetFragment(), "Data").commitAllowingStateLoss();
                }
                drFlag = 22;
            } else if (menuName.equalsIgnoreCase(getResources().getString(R.string.invoice))) {
                title = "Invoice";
                if (cc.isConnectingToInternet()) {
                    ShowUI();
                    if (drFlag != 23) {
                        ft.replace(R.id.main, new InvoiceFragment(), "Data").commitAllowingStateLoss();
                    }
                } else {
                    ft.replace(R.id.main, new NoInternetFragment(), "Data").commitAllowingStateLoss();
                }
                drFlag = 23;
            } else if (menuName.equalsIgnoreCase(getResources().getString(R.string.usermanual))) {
                if (cc.isConnectingToInternet()) {
                    Intent viewIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse("https://www.vztrack.in/user-manual"));
                    startActivity(viewIntent);
                } else {
                    CommonMethods.showToastSuccess(this, "Unable to logout, Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                }
            }
        }
       // getSupportActionBar().setTitle(title);
        if (drFlag == 9 || drFlag == 10 || drFlag == -1) {
        } else {
            drawer.closeDrawer(GravityCompat.START);
        }
    }


    private void crossfade() {
        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        llsplashicon.setAlpha(0f);
        llsplashicon.setVisibility(View.VISIBLE);
        splashimage.setAlpha(0f);
        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        llsplashicon.animate()
                .alpha(1f)
                .setDuration(700)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        splashimage.animate()
                .alpha(1f)
                .setDuration(700)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        splashimage.setVisibility(View.VISIBLE);
                    }
                });
    }

    //setup recycler view
    private void setUpRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.drawermenu_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void populateRecyclerView() {
        ArrayList<ConfigBean> drawerConfigArrayList = new Gson().fromJson(SheredPref.getFrequentlyUsedMenuList(context), new TypeToken<ArrayList<ConfigBean>>() {
        }.getType());
        ArrayList<ConfigBean> frequentusedmenus = new ArrayList<>();
        frequentusedmenus.addAll(CommonMethods.sortDrawerMenu(drawerConfigArrayList));
        if (SheredPref.getDrawerMenuList(context) != null) {
            ArrayList<DrawerConfigParent> drawerConfigParents = new Gson().fromJson(SheredPref.getDrawerMenuList(context), new TypeToken<ArrayList<DrawerConfigParent>>() {
            }.getType());
            if (drawerConfigParents != null) {
                DrawerConfigParent drawerConfigParent = new DrawerConfigParent(drawerConfigParents.get(0).getMenuname(), frequentusedmenus);
                drawerConfigParents.set(0, drawerConfigParent);

                int noticecount = SheredPref.getNoticeConut(mainActivity);
                int complanitcount = SheredPref.getComplaintConut(mainActivity);
                int messagecount = SheredPref.getMessageCount(mainActivity);
                int rainbowCount = SheredPref.getRainbowCount(mainActivity);


                for (int i = 0; i < drawerConfigParents.size(); i++) {
                    ArrayList<ConfigBean> contentsforcompany = new ArrayList<>();
                    for (int j = 0; j < drawerConfigParents.get(i).getContents().size(); j++) {

                        if (type) {
                            if ((drawerConfigParents.get(i).getContents().get(j).getName().trim().equalsIgnoreCase(getResources().getString(R.string.knowyourmaid))) ||
                                    drawerConfigParents.get(i).getContents().get(j).getName().trim().equalsIgnoreCase(getResources().getString(R.string.serviceprovider)) ||
                                    drawerConfigParents.get(i).getContents().get(j).getName().trim().equalsIgnoreCase(getResources().getString(R.string.marketplace)) ||
                                    drawerConfigParents.get(i).getContents().get(j).getName().trim().equalsIgnoreCase(getResources().getString(R.string.localstores))) {

                            } else {
                                contentsforcompany.add(drawerConfigParents.get(i).getContents().get(j));
                            }
                        } else {
                            contentsforcompany.add(drawerConfigParents.get(i).getContents().get(j));
                        }

                    }
                    drawerConfigParents.get(i).setContents(contentsforcompany);
                }


                for (int i = 0; i < drawerConfigParents.size(); i++) {
                    for (int j = 0; j < drawerConfigParents.get(i).getContents().size(); j++) {
                        if (drawerConfigParents.get(i).getContents().get(j).getName().equalsIgnoreCase(getResources().getString(R.string.noticenminutes))) {
                            //notification count
                            if (noticecount != 0) {
                                drawerConfigParents.get(i).getContents().get(j).setNotificationcount(true);
                            } else {
                                drawerConfigParents.get(i).getContents().get(j).setNotificationcount(false);
                            }


                        } else if (drawerConfigParents.get(i).getContents().get(j).getName().equalsIgnoreCase(getResources().getString(R.string.complaintregister))) {
                            //complaint count
                            if (complanitcount != 0) {
                                drawerConfigParents.get(i).getContents().get(j).setNotificationcount(true);
                            } else {
                                drawerConfigParents.get(i).getContents().get(j).setNotificationcount(false);
                            }


                        } else if (drawerConfigParents.get(i).getContents().get(j).getName().equalsIgnoreCase(getResources().getString(R.string.message))) {
                            //complaint count
                            if (messagecount != 0) {
                                drawerConfigParents.get(i).getContents().get(j).setNotificationcount(true);
                            } else {
                                drawerConfigParents.get(i).getContents().get(j).setNotificationcount(false);
                            }

                        } else if (drawerConfigParents.get(i).getContents().get(j).getName().equalsIgnoreCase(getResources().getString(R.string.rainbowresidents))) {
                            //complaint count
                            if (rainbowCount != 0) {
                                drawerConfigParents.get(i).getContents().get(j).setNotificationcount(true);
                            } else {
                                drawerConfigParents.get(i).getContents().get(j).setNotificationcount(false);
                            }
                        }
                    }
                }

                if (!adminAccess) {
                    removeAdminMenu(drawerConfigParents);
                }
                SectionRecyclerViewAdapter adapter = new SectionRecyclerViewAdapter(this, RecyclerViewType.GRID, drawerConfigParents);
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(getApplicationContext(), "something went worng", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void removeAdminMenu(ArrayList<DrawerConfigParent> drawerConfigParents) {
        for(int i = 0; i < drawerConfigParents.size(); i++){
            if(drawerConfigParents.get(i).getMenuname().equals("Admin Features")){
                drawerConfigParents.remove(i);
                break;
            }
        }
    }

    public class SectionRecyclerViewAdapter extends RecyclerView.Adapter<SectionRecyclerViewAdapter.SectionViewHolder> {
        class SectionViewHolder extends RecyclerView.ViewHolder {
            private TextView sectionLabel;//, showAllButton;
            private RecyclerView itemRecyclerView;

            public SectionViewHolder(View itemView) {
                super(itemView);
                sectionLabel = (TextView) itemView.findViewById(R.id.section_label);
                itemRecyclerView = (RecyclerView) itemView.findViewById(R.id.item_recycler_view);
            }
        }

        private Context context;
        private RecyclerViewType recyclerViewType;
        private ArrayList<DrawerConfigParent> sectionModelArrayList;

        public SectionRecyclerViewAdapter(Context context, RecyclerViewType recyclerViewType, ArrayList<DrawerConfigParent> sectionModelArrayList) {
            this.context = context;
            this.recyclerViewType = recyclerViewType;
            this.sectionModelArrayList = sectionModelArrayList;
        }

        @Override
        public SectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_custom_row_layout, parent, false);
            return new SectionViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SectionViewHolder holder, int position) {
            final DrawerConfigParent sectionModel = sectionModelArrayList.get(position);
            holder.sectionLabel.setText(sectionModel.getMenuname());

            //recycler view for items
            holder.itemRecyclerView.setHasFixedSize(true);
            holder.itemRecyclerView.setNestedScrollingEnabled(false);

            /* set layout manager on basis of recyclerview enum type */
            switch (recyclerViewType) {
                case LINEAR_VERTICAL:
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    holder.itemRecyclerView.setLayoutManager(linearLayoutManager);
                    break;
                case LINEAR_HORIZONTAL:
                    LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                    holder.itemRecyclerView.setLayoutManager(linearLayoutManager1);
                    break;
                case GRID:
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
                    holder.itemRecyclerView.setLayoutManager(gridLayoutManager);
                    break;
            }


            ItemRecyclerViewAdapter adapter = new ItemRecyclerViewAdapter(context, sectionModel.getContents(), position);
            holder.itemRecyclerView.setAdapter(adapter);

        }

        @Override
        public int getItemCount() {
            return sectionModelArrayList.size();
        }


    }

    //item adapter for drawer
    public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemViewHolder> {

        class ItemViewHolder extends RecyclerView.ViewHolder {
            private TextView itemLabel;
            private ImageView imgmenu, ic_notification;
            LinearLayout lldrawergrid;
            CardView llmaterialcardview;

            public ItemViewHolder(View itemView) {
                super(itemView);
                itemLabel = (TextView) itemView.findViewById(R.id.item_label);
                imgmenu = itemView.findViewById(R.id.imgmenu);
                ic_notification = itemView.findViewById(R.id.notification_indicator);
                lldrawergrid = itemView.findViewById(R.id.lldrawergrid);
                llmaterialcardview = itemView.findViewById(R.id.llmaterialcardview);
            }
        }

        private Context context;
        private ArrayList<ConfigBean> arrayList;

        public ItemRecyclerViewAdapter(Context context, ArrayList<ConfigBean> arrayList, int position) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_row_layout, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {

            try {
                holder.itemLabel.setText(arrayList.get(position).getTitle());
                String name = arrayList.get(position).getMenu_image();
                name = name.toLowerCase();
                name = name.replaceAll("\\s", "");
                int id = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
                Drawable drawable = context.getResources().getDrawable(id);
                holder.imgmenu.setImageDrawable(drawable);
                if (arrayList.get(position).isNotificationcount())
                    holder.ic_notification.setVisibility(View.VISIBLE);
                else
                    holder.ic_notification.setVisibility(View.INVISIBLE);

                holder.lldrawergrid.setTag(arrayList.get(position));

                boolean type = SheredPref.getType(context);
                holder.llmaterialcardview.setVisibility(View.VISIBLE);
                {
                    if (!arrayList.get(position).isIsshow()) {
                        holder.lldrawergrid.setAlpha((float) 0.3);
                        holder.lldrawergrid.setOnClickListener(v -> {
                            CommonMethods.showToastSuccess(context, "This menu is not available for you, please contact VZTrack administrator to activate this.");//show();
                        });

                    } else {
                        holder.lldrawergrid.setAlpha((float) 1);
                        holder.lldrawergrid.setOnClickListener(v -> {
                            ConfigBean drawerConfig = (ConfigBean) v.getTag();
                            if (drawerConfig != null) {
                                isCameFromPermission = false;
                                ShowFragment(drawerConfig);
                                CommonMethods.updatefrequentlyaddedcount(mainActivity, drawerConfig);
                            }
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }
    }

    public String getMenuTitle() {
        return title;
    }
}