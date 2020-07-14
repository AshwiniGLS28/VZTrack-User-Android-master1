package vztrack.gls.com.vztrack_user.activity;

/**
 * Created by sandeep on 14/3/16.
 */

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;


import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.adapters.MenuNotificationAdapter;
import vztrack.gls.com.vztrack_user.adapters.SecondaryUserAdapter;
import vztrack.gls.com.vztrack_user.beans.NotificationMenuBeans;
import vztrack.gls.com.vztrack_user.beans.ProfilePhotoBean;
import vztrack.gls.com.vztrack_user.beans.SecondaryUserBean;
import vztrack.gls.com.vztrack_user.responce.NotificationMenuResponseBean;
import vztrack.gls.com.vztrack_user.responce.SecondaryUserResponseBean;
import vztrack.gls.com.vztrack_user.beans.UserBean;
import vztrack.gls.com.vztrack_user.responce.LoginResponse;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.PermissionConstant;
import vztrack.gls.com.vztrack_user.utils.Permissions;
import vztrack.gls.com.vztrack_user.utils.PostData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

public class SettingDetails extends BaseActivity {

    String photoAction;
    InputStream inputStream;
    private static int PICK_IMAGE_REQUEST = 1;
    private final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 1;
    BaseActivity context;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    CheckConnection cc;
    public static EditText OldPassword, NewPassword1, NewPassword2;
    Button Submit;
    public static String old_password, new_password, confirm_password;
    private CheckBox notification_checkbox;
    private AppCompatCheckBox sound_checkbox;
    private AppCompatCheckBox vibration_checkbox, phone_ringtone_checkbox;
    private RelativeLayout sound_layout;
    private RelativeLayout vibrate_layout, allow_Notification;
    private TextView username, flat_no, soc_name, owenr_name, openSettings, noSecondaryUserAccountLabel, tvSecondaryUserName;//VZTrackID
    private TextView flatNumber, socORcompanyName, ringtoneTitle, selectedPhoneRingtoneTitle;
    private final int ALL_RINGTONES = 6, ALL_TONES = 5;
    RelativeLayout password;
    private LinearLayout  notification, information, selectedPhoneRingtoneLayout, secondaryUserNameLayout, noteInProfile, emailIdLinearLayout;
    RelativeLayout layout_acct;
    private CheckBox checkEmail;
    private EditText emailId;
    private int emailCheckBox;
    private CardView phoneRingtoneCard, toneCard;
    private int selectedFileChooser, ringtoneCallBack;
    ListView secondaryAccountsList, noticationMenuList;
    private FloatingActionButton addUserBtn;
    SecondaryUserAdapter secondaryUseradapter = null;
    MenuNotificationAdapter menuNotificationAdapter = null;
    private ImageView  openNotification;
    ImageView deleteSecondaryUser;
    Button btnAddEmail;
    String needLoginValue;
    public static Dialog dialog;
    public ArrayList<NotificationMenuBeans> notificationMenuBean = new ArrayList<>();
    static public Context mContext;
    String flag;
    boolean type;
    ArrayList<NotificationMenuBeans> notificationMenuBeanTemp;
    NotificationMenuResponseBean notificationMenuBeans;
    NotificationMenuBeans beans;
    ImageView back;
    ImageView backpress;
    Button btnSaveNotification;
    TextView title;
    private static final String TAG = "SettingDetailsAct";
    ImageView circleProfleView;
    LinearLayout llemailid;
    private String strSecondaryUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_details);

        // Check Internet Connection
        cc = new CheckConnection(this);

        llemailid=findViewById(R.id.llemailid);

        backpress=findViewById(R.id.backpress);
        circleProfleView=findViewById(R.id.circleProfleView);
        backpress.setOnClickListener(v -> onBackPressed());
        title=findViewById(R.id.title);
        title.setText("Setting");
        context = SettingDetails.this;
        mContext = SettingDetails.this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Add user
        layout_acct = findViewById(R.id.layout_acct);
        secondaryAccountsList = findViewById(R.id.secondaryUser_listview);
        addUserBtn = findViewById(R.id.addUserBtn);
        deleteSecondaryUser = findViewById(R.id.deleteSecondaryUser);
        noSecondaryUserAccountLabel = findViewById(R.id.noAccountLabel);
//        VZTrackID = findViewById(R.id.VZTrackID);
        password = findViewById(R.id.layout_password);
        notification = findViewById(R.id.layout_notification);

        information = findViewById(R.id.layout_info);
//        mangaeProfileLine = findViewById(R.id.profile_line);
        btnAddEmail = findViewById(R.id.btnAddEmail);
        openNotification = findViewById(R.id.openNotification);
        selectedPhoneRingtoneLayout = (LinearLayout) findViewById(R.id.selectedPhoneRingtoneLayout);
        secondaryUserNameLayout = findViewById(R.id.secondaryUserNameLinearLayout);
        tvSecondaryUserName = findViewById(R.id.tv_name);
        noteInProfile = findViewById(R.id.noteInProfile);
        emailIdLinearLayout = findViewById(R.id.emailIdLinearLayout);


        OldPassword = (EditText) findViewById(R.id.etPasswordOld);
        NewPassword1 = (EditText) findViewById(R.id.etPasswordNew1);
        NewPassword2 = (EditText) findViewById(R.id.etPasswordNew2);
        notification_checkbox = (CheckBox) findViewById(R.id.notification_checkbox);
        sound_checkbox = (AppCompatCheckBox) findViewById(R.id.notification_sount_checkbox);
        vibration_checkbox = (AppCompatCheckBox) findViewById(R.id.notification_vibration_checkbox);
        Submit = findViewById(R.id.btnChangePassword);
        vibrate_layout = (RelativeLayout) findViewById(R.id.rel_lay_vibrate);
        sound_layout = (RelativeLayout) findViewById(R.id.rel_lay_sound);
        allow_Notification = (RelativeLayout) findViewById(R.id.rel_lay_allow_notification);

        flatNumber = (TextView) findViewById(R.id.flatNumber);
        socORcompanyName = (TextView) findViewById(R.id.socORcompanyName);

        username = (TextView) findViewById(R.id.tv_username);
        flat_no = (TextView) findViewById(R.id.tv_flat_no);
        soc_name = (TextView) findViewById(R.id.tv_soc_name);
        owenr_name = (TextView) findViewById(R.id.tv_owner_name);
        ringtoneTitle = (TextView) findViewById(R.id.ringtoneTitle);
        openSettings = (TextView) findViewById(R.id.openSettings);
        selectedPhoneRingtoneTitle = (TextView) findViewById(R.id.selectedPhoneRingtoneTitle);

        checkEmail = (CheckBox) findViewById(R.id.checkEmail);
        phone_ringtone_checkbox = (AppCompatCheckBox) findViewById(R.id.use_ringtone_checkbox);
        emailId = (EditText) findViewById(R.id.emailId);
        phoneRingtoneCard = (CardView) findViewById(R.id.phoneRingtoneCard);
        toneCard = (CardView) findViewById(R.id.toneCard);

        notificationMenuBeans = new NotificationMenuResponseBean();

        strSecondaryUserName = SheredPref.getName(this);

        flag = getIntent().getStringExtra("FLAG");
        if (flag.equals("PASS")) {
            title.setText("Change Password");
            layout_acct.setVisibility(View.GONE);
            addUserBtn.setVisibility(View.GONE);
            password.setVisibility(View.VISIBLE);
            notification.setVisibility(View.GONE);
            information.setVisibility(View.GONE);
        } else if (flag.equals("NOTI")) {
            title.setText("Notifications");
            layout_acct.setVisibility(View.GONE);
            addUserBtn.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            notification.setVisibility(View.VISIBLE);
            information.setVisibility(View.GONE);
        } else if (flag.equals("PROF")) {
            title.setText("Profile");
            emailIdLinearLayout.setVisibility(View.GONE);
            layout_acct.setVisibility(View.GONE);
            addUserBtn.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            noteInProfile.setVisibility(View.VISIBLE);
            notification.setVisibility(View.GONE);
            information.setVisibility(View.VISIBLE);
            noteInProfile.setVisibility(View.VISIBLE);
            emailIdLinearLayout.setVisibility(View.VISIBLE);
            if (MainActivity.isPrimaryUser == false) {
                emailIdLinearLayout.setVisibility(View.GONE);
                btnAddEmail.setVisibility(View.GONE);
                secondaryUserNameLayout.setVisibility(View.VISIBLE);
                llemailid.setVisibility(View.GONE);
                noteInProfile.setVisibility(View.GONE);
                emailId.setVisibility(View.GONE);
            }
            if (strSecondaryUserName.contains("TENANT")) {
                emailIdLinearLayout.setVisibility(View.GONE);
                noteInProfile.setVisibility(View.GONE);
            }
        } else if (flag.equals("Mana")) {
            title.setText("Manage Account");
            if (strSecondaryUserName.contains("TENANT")) {
                addUserBtn.setVisibility(View.GONE);
                layout_acct.setVisibility(View.GONE);
                noSecondaryUserAccountLabel.setVisibility(View.GONE);
                showCustomAlertWindow(this.getString(R.string.cannot_open_account),"For more information please contact " + this.getString(R.string.tenant_contact_emailId),this);
                getSecondaryUser();
            } else {
                title.setText("Manage Accounts");
                layout_acct.setVisibility(View.VISIBLE);
                addUserBtn.setVisibility(View.VISIBLE);
                password.setVisibility(View.GONE);
                notification.setVisibility(View.GONE);
                information.setVisibility(View.GONE);
                getSecondaryUser();
            }
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            vibrate_layout.setVisibility(View.VISIBLE);
            sound_layout.setVisibility(View.VISIBLE);
            allow_Notification.setVisibility(View.VISIBLE);
            openSettings.setVisibility(View.GONE);
            openNotification.setVisibility(View.GONE);

            if (SheredPref.getNotification(this).equals("ENABLE")) {
                notification_checkbox.setChecked(true);
                UpdateUIWhenChecked();
            } else {
                notification_checkbox.setChecked(false);
                UpdateUIWhenUnchecked();
            }

            if (SheredPref.getSound(this).equals("ENABLE")) {
                sound_checkbox.setChecked(true);
            } else {
                sound_checkbox.setChecked(false);
            }

            if (SheredPref.getVibration(this).equals("ENABLE")) {
                vibration_checkbox.setChecked(true);
            } else {
                vibration_checkbox.setChecked(false);
            }


            notification_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        UpdateUIWhenChecked();

                        SheredPref.setNotification(context, "ENABLE");
                        sound_checkbox.setChecked(true);
                        vibration_checkbox.setChecked(true);
                        SheredPref.setSound(context, "ENABLE");
                        SheredPref.setVibration(context, "ENABLE");
                        sound_layout.setVisibility(View.VISIBLE);
                        vibrate_layout.setVisibility(View.VISIBLE);
                        phoneRingtoneCard.setVisibility(View.VISIBLE);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                        builder.setTitle("Notification");
                        builder.setMessage("This will stop VZ Track notification,\nDo you want to stop receiving notifications?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                UpdateUIWhenUnchecked();
                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                notification_checkbox.setChecked(true);
                                dialog.cancel();
                            }
                        });

                        builder.show();
                    }
                }
            });
            sound_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        SheredPref.setSound(context, "ENABLE");
                    } else {
                        SheredPref.setSound(context, "DISABLE");
                    }


                }
            });
            vibration_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        SheredPref.setVibration(context, "ENABLE");
                    } else {
                        SheredPref.setVibration(context, "DISABLE");
                    }
                }
            });
        }

        if (!SheredPref.getPhoneRingtoneTitle(context).equals("")) {
            selectedPhoneRingtoneLayout.setVisibility(View.VISIBLE);
            selectedPhoneRingtoneTitle.setText(SheredPref.getPhoneRingtoneTitle(context));
        } else {
            selectedPhoneRingtoneLayout.setVisibility(View.GONE);
        }

        if (SheredPref.getMyPhoneRingtoneAsNotificationTone(context)) {
            phone_ringtone_checkbox.setChecked(true);
            selectedPhoneRingtoneLayout.setVisibility(View.VISIBLE);
            Ringtone defaultRingtone = RingtoneManager.getRingtone(SettingDetails.this,
                    Settings.System.DEFAULT_RINGTONE_URI);
            selectedPhoneRingtoneTitle.setText(defaultRingtone.getTitle(SettingDetails.this));
        } else {
            phone_ringtone_checkbox.setChecked(false);
            selectedPhoneRingtoneLayout.setVisibility(View.GONE);
        }

        if (!SheredPref.getRingtoneTitle(context).equals("")) {
            ringtoneTitle.setText(SheredPref.getRingtoneTitle(context));
        }

        checkEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    emailCheckBox = 1;
                } else {
                    emailCheckBox = 0;
                }
            }
        });

        String strEmailId = SheredPref.getUserEmail(context);
        boolean loginByEmail = SheredPref.getLoginByEmail(context);

        if (strEmailId.equals("") || loginByEmail) {
            checkEmail.setChecked(true);
        }

        if (MainActivity.isPrimaryUser == false) {
            tvSecondaryUserName.setText(strSecondaryUserName);
        }
        emailId.setText(strEmailId);
        emailId.setSelection(emailId.getText().length());
        username.setText(SheredPref.getUsername(this));
        soc_name.setText(SheredPref.getSociety_Name(this));
        flat_no.setText(SheredPref.getFlat_No(this));
        owenr_name.setText(SheredPref.getOwnerName(this));
        type = SheredPref.getType(this);
        if (type) {
            flatNumber.setText("Employee Id : ");
            socORcompanyName.setText("Company Name : ");
        } else {
            flatNumber.setText("House Number : ");
            socORcompanyName.setText("Society Name : ");
        }

        phone_ringtone_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    updateWhenNotificationIsTrue();
                } else {
                    updateWhenNotificationIsFalse();
                }
            }
        });


        Submit.setOnClickListener(v -> {

            if (cc.isConnectingToInternet()) {
                old_password = OldPassword.getText().toString().trim();
                new_password = NewPassword1.getText().toString().trim();
                confirm_password = NewPassword2.getText().toString().trim();

                if (old_password.equals("")) {
                    CommonMethods.showToastSuccess(context, "Old password should not be blank");//, Toast.LENGTH_SHORT, true).show();
                } else if (new_password.equals("")) {
                    CommonMethods.showToastSuccess(context, "New password should not be blank");//, Toast.LENGTH_SHORT, true).show();
                } else if (confirm_password.equals("")) {
                    CommonMethods.showToastSuccess(context, "Confirm password password should not be blank");//, Toast.LENGTH_SHORT, true).show();
                } else if (SheredPref.getPassword(context).equals(old_password)) {
                    if (new_password.equals(confirm_password)) {

                        if (old_password.equals(new_password) && old_password.equals(confirm_password)) {
                            CommonMethods.showToastSuccess(context, "Old password and new password should not be same");//, Toast.LENGTH_SHORT, true).show();
                        } else {
                            if (new_password.length() >= 6) {
                                new GetData(context, CallFor.CHANGE_PASSWORD, new_password).execute();
                            } else {
                                CommonMethods.showToastSuccess(context, "Password length should be greater than 5");//, Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    } else {
                        CommonMethods.showToastError(context, "New password mismatch");//, Toast.LENGTH_SHORT, true).show();
                    }
                } else {
                    CommonMethods.showToastError(context, "Old password mismatch");//, Toast.LENGTH_SHORT, true).show();
                }
            } else {
                CommonMethods.showToastError(context, "Unable to change password, Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
            }
        });

        addUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                if (cc.isConnectingToInternet()) {
                    Intent intent = new Intent(context, AddSecondaryUser.class);
                    startActivity(intent);
                } else {
                    CommonMethods.showToastError(context, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        Glide.with(this)
                .load(UtilityMethods.getUserProfilePhoto(context))
                .placeholder(R.drawable.ic_avatar)
                .error(R.drawable.ic_add_user)
                .skipMemoryCache(false)
                .thumbnail(0.4f)
                .into(circleProfleView);
        getSecondaryUser();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateWhenNotificationIsFalse() {
        SheredPref.setMyPhoneRingtoneAsNotificationTone(context, false);
        SheredPref.setPhoneRingtoneTitle(context, "");
        selectedPhoneRingtoneLayout.setVisibility(View.GONE);
    }

    private void updateWhenNotificationIsTrue() {
        SheredPref.setMyPhoneRingtoneAsNotificationTone(context, true);
        Ringtone defaultRingtone = RingtoneManager.getRingtone(SettingDetails.this,
                Settings.System.DEFAULT_RINGTONE_URI);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        SheredPref.setPhoneNotificationRingtone(SettingDetails.this, uri.toString());
        selectedPhoneRingtoneLayout.setVisibility(View.VISIBLE);
        selectedPhoneRingtoneTitle.setText(defaultRingtone.getTitle(SettingDetails.this));
        SheredPref.setPhoneNotificationRingtone(SettingDetails.this, uri.toString());
        SheredPref.setPhoneRingtoneTitle(SettingDetails.this, defaultRingtone.getTitle(SettingDetails.this));
    }

    private void UpdateUIWhenUnchecked() {
        notification_checkbox.setChecked(false);
        sound_checkbox.setChecked(false);
        vibration_checkbox.setChecked(false);

        sound_layout.setVisibility(View.GONE);
        vibrate_layout.setVisibility(View.GONE);
        phoneRingtoneCard.setVisibility(View.GONE);
        toneCard.setVisibility(View.GONE);

        SheredPref.setNotification(context, "DISABLE");
        SheredPref.setSound(context, "DISABLE");
        SheredPref.setVibration(context, "DISABLE");
    }

    private void UpdateUIWhenChecked() {
        SheredPref.setNotification(context, "ENABLE");
        sound_checkbox.setChecked(true);
        vibration_checkbox.setChecked(true);
        SheredPref.setSound(context, "ENABLE");
        SheredPref.setVibration(context, "ENABLE");
        sound_layout.setVisibility(View.VISIBLE);
        vibrate_layout.setVisibility(View.VISIBLE);
        phoneRingtoneCard.setVisibility(View.VISIBLE);
        toneCard.setVisibility(View.VISIBLE);
    }

    public void customeNotificationSound(View v) {
        boolean res = Permissions.askPermission(this, PermissionConstant.PERMISSION_EXTERNAL_STORAGE, PermissionConstant.REQ_CODE_EXTERNAL_STORAGE);
        if (res) {
            openRingtoneFileChooser(RingtoneManager.TYPE_NOTIFICATION, ALL_TONES);
        } else {
            selectedFileChooser = RingtoneManager.TYPE_NOTIFICATION;
            ringtoneCallBack = ALL_TONES;
        }
    }

    public void NotificationRingtone(View v) {
        boolean res = Permissions.askPermission(this, PermissionConstant.PERMISSION_EXTERNAL_STORAGE, PermissionConstant.REQ_CODE_EXTERNAL_STORAGE);
        if (res) {
            openRingtoneFileChooser(RingtoneManager.TYPE_RINGTONE, ALL_RINGTONES);
        } else {
            selectedFileChooser = RingtoneManager.TYPE_RINGTONE;
            ringtoneCallBack = ALL_RINGTONES;
        }
    }

    private void openRingtoneFileChooser(int type_ringtone, int ringtone_call_back) {
        final Uri currentTone = Uri.parse(SheredPref.getNotificationRingtone(this));
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, type_ringtone);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, R.string.select_ringtone);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentTone);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        this.startActivityForResult(intent, ringtone_call_back);
    }

    public static String BitmapToString(Bitmap bitmap) throws UnsupportedEncodingException {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 50, bao);
        byte[] ba = bao.toByteArray();
        String s=Base64.encodeToString(ba, Base64.URL_SAFE);
        String query = URLEncoder.encode(s, "utf-8");
        return query;
    }

    public static Bitmap imagePathToBitmap(String path){
        Bitmap bitmap = null;
        File imgFile = new  File(path);
        if(imgFile.exists()){
            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return bitmap;
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        EasyImage.handleActivityResult(requestCode, resultCode, intent, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Log.e("Exception : ", " " + e);
                CommonMethods.showToastError(context, "Unable to select image");//, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                Bitmap bitmap = UtilityMethods.imagePathToBitmap(imageFile.getAbsolutePath());
                bitmap = UtilityMethods.getResizedBitmap(bitmap, 720);
                try {
                    bitmap = UtilityMethods.rotateImageIfRequired(context, bitmap, imageFile.getAbsolutePath());
                } catch (IOException e) {
                    Log.e(TAG, "EXCEPTION UPLOAD GETTING ROTATED IMAGE : " + e);
                }
                if (bitmap != null) {
                    String encodedImageUrl = UtilityMethods.BitmapToString(bitmap);
                    Glide.with(context)
                            .load(bitmap)
                            .placeholder(R.drawable.ic_avatar)
                            .skipMemoryCache(false)
                            .thumbnail(0.4f)
                            .into(circleProfleView);
                    ProfilePhotoBean profilePhotoBean = new ProfilePhotoBean();
                    profilePhotoBean.setGetUserPhoto(encodedImageUrl);
                    new PostData(new Gson().toJson(profilePhotoBean), context, CallFor.SAVE_USER_PHOTO).execute();
                }else{
                    Log.e(TAG, " BITMAP IS NULL NOT ABLE TO UPLOAD");
                    CommonMethods.showToastError(context, "Unable to upload profile photo");
                }
            }
        });
        if (resultCode == Activity.RESULT_OK && requestCode == ALL_TONES) {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            Ringtone tone = RingtoneManager.getRingtone(this, uri);
            String title = tone.getTitle(this);

            if (uri != null) {
                String ringtone = uri.toString();
                SheredPref.setNotificationRingtone(this, ringtone);
                SheredPref.setRingtoneTitle(this, title);
                SheredPref.setCustomNotificationSound(this, true);
                CreateNotificationChannel(uri, SheredPref.getNotificationChannelId(this));
                ringtoneTitle.setText(title);
            } else {
                CommonMethods.showToastError(this, "Unable to set notification ringtone");//, Toast.LENGTH_SHORT, true).show();
            }
        }


        if (resultCode == Activity.RESULT_OK && requestCode == ALL_RINGTONES) {
            Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            Ringtone tone = RingtoneManager.getRingtone(this, uri);
            String title = tone.getTitle(this);
            if (uri != null) {
                String ringtone = uri.toString();
                SheredPref.setPhoneNotificationRingtone(this, ringtone);
                SheredPref.setPhoneRingtoneTitle(this, title);
                selectedPhoneRingtoneLayout.setVisibility(View.VISIBLE);
                selectedPhoneRingtoneTitle.setText(title);
            } else {
                CommonMethods.showToastError(this, "Unable to set notification ringtone");//, Toast.LENGTH_SHORT, true).show();
            }
        }
    }

    private void DeleteNotificationChannel(String channelId, NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.deleteNotificationChannel(channelId);
        }
    }

    private void CreateNotificationChannel(Uri uri, String strChannelCustomSound) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                if (strChannelCustomSound.equals("")) {
                    strChannelCustomSound = this.getString(R.string.default_notification_channel_id_custom_sound) + "_" + generateRandomChar();
                    SheredPref.setNotificationChannelId(this, strChannelCustomSound);
                } else {
                    try {
                        DeleteNotificationChannel(strChannelCustomSound, notificationManager);
                        strChannelCustomSound = this.getString(R.string.default_notification_channel_id_custom_sound) + "_" + generateRandomChar();
                        SheredPref.setNotificationChannelId(this, strChannelCustomSound);
                    } catch (Exception ex) {
                        Log.e("Exception In ", " Split");
                    }
                }

                NotificationChannel channelCustomSound = new NotificationChannel(strChannelCustomSound, "VZTrack Notifications Custom Sound", NotificationManager.IMPORTANCE_HIGH);
                AudioAttributes att = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build();
                channelCustomSound.setSound(uri, att);

                notificationManager.createNotificationChannel(channelCustomSound);
                //channelSetting.setVisibility(View.VISIBLE);
                CommonMethods.showToastSuccess(this, getString(R.string.created_successfully));//, Toast.LENGTH_LONG).show();
            } else {
                Log.e("Unable ", " to get notification Manager");
            }
        }
    }

    private Character generateRandomChar() {
        Random r = new Random();
        return (char) (r.nextInt(26) + 'a');
    }

    public void AddEmail(View v) {

        String strEmailId = emailId.getText().toString().trim();
        if (cc.isConnectingToInternet()) {
            if (strEmailId.equals("")) {
                CommonMethods.showToastError(this, "Email should not be blank");//, Toast.LENGTH_SHORT, true).show();
            } else {
                if (UtilityMethods.validateEmail(strEmailId)) {
                    String apiData = "?email=" + strEmailId + "&loginByEmail=" + emailCheckBox;
                    new GetData(context, CallFor.ADD_EMAIL_ID, apiData).execute();
                } else {
                    CommonMethods.showToastError(this, "Invalid email id");//, Toast.LENGTH_SHORT, true).show();
                }
            }
        } else {
            CommonMethods.showToastSuccess(this, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
        }
    }

    @Override
    public void onGetResponse(String response, String callFor) {
        if (callFor.equals( CallFor.SAVE_USER_PHOTO))
        {
            NotificationMenuResponseBean notificationMenuResponseBean = new Gson().fromJson(response, NotificationMenuResponseBean.class);
            if (notificationMenuResponseBean!=null) {
                CommonMethods.showToastSuccess(getApplicationContext(), notificationMenuResponseBean.getMessage());
                SheredPref.setRandomNumber(context, (SheredPref.getRandomNumber(context)+1));
            }
        }
        if (callFor.equals(CallFor.LOGIN)) {
            LoginResponse  loginResponse = new Gson().fromJson(response, LoginResponse.class);
            try {
                if (loginResponse.getCode().equals("SUCCESS")) {
                     String strSocietyName, strFlatNo, wingName, strUsername, strPassword,secondaryUserName;
                     boolean isPrimaryUser;

                     int strSocietyId, strFamilyId;
                    UtilityMethodsAndroid.updateSheredPreferenceValues(this, loginResponse);
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
                    UtilityMethodsAndroid.updateSheredPreferenceValues(this, loginResponse);
                    SheredPref.setSocietyApproval(this,loginResponse.getFamily().isApproval());
                    String email = loginResponse.getFamily().getEmail();
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            }
        if (callFor.equals(CallFor.GET_NOTIFICATION_MENU)) {
            NotificationMenuResponseBean notificationMenuResponseBean = new Gson().fromJson(response, NotificationMenuResponseBean.class);
            try {
                if (notificationMenuResponseBean.getCode().equals("SUCCESS")) {
                    setAdapterNoticationMenu(notificationMenuResponseBean.getNotificationMenuBeans());
                    notificationMenuBean = notificationMenuResponseBean.getNotificationMenuBeans();
                }
            } catch (Exception ex) {
                Log.e("Ex Getting Details ", " " + ex);
            }
        }
        if(callFor.equals(CallFor.SAVE_NOTIFICATION_OPTION)){
            NotificationMenuResponseBean notificationMenuResponseBean = new Gson().fromJson(response, NotificationMenuResponseBean.class);
            try {
                if (notificationMenuResponseBean.getCode().equals("SUCCESS")) {
                    CommonMethods.showToastSuccess(this, notificationMenuResponseBean.getMessage());//, Toast.LENGTH_SHORT, true).show();
                    dialog.dismiss();
                }else{
                    CommonMethods.showToastError(this, notificationMenuResponseBean.getMessage());//, Toast.LENGTH_SHORT, true).show();
                }
            } catch (Exception ex) {
                Log.e("Ex Getting Details ", " " + ex);
            }
        }
        if (callFor.equals(CallFor.GET_SECONDARY_USER)) {
            SecondaryUserResponseBean secondaryUserResponseBean = new Gson().fromJson(response, SecondaryUserResponseBean.class);
            try {
                if (secondaryUserResponseBean.getCode().equals("SUCCESS")) {
                    setAdapterView(secondaryUserResponseBean.getSecondaoryUserBeanList());
                } else if (secondaryUserResponseBean.getCode().equals("NEED_LOGIN")) {
                    UserBean userBean = new UserBean();
                    userBean.setUser_name(SheredPref.getUsername(this));
                    userBean.setUser_password(SheredPref.getPassword(this));
                    needLoginValue = "secondaryUser";
                    new PostData(new Gson().toJson(userBean), this, CallFor.LOGIN).execute();
                } else {
                    CommonMethods.showToastError(this, secondaryUserResponseBean.getMessage());//, Toast.LENGTH_SHORT, true).show();
                }
            } catch (Exception ex) {
                Log.e("Ex Getting Details ", " " + ex);
            }
        }
        if (callFor.equals(CallFor.DELETE_SECONDARY_USER)) {
            SecondaryUserResponseBean secondaryUserResponseBean = new Gson().fromJson(response, SecondaryUserResponseBean.class);
            if (secondaryUserResponseBean.getCode().equals("SUCCESS")) {
                CommonMethods.showToastSuccess(this, secondaryUserResponseBean.getMessage());//.show();
                getSecondaryUser();
            } else {
                CommonMethods.showToastError(this, secondaryUserResponseBean.getMessage());//.show();
            }
        }
        if (callFor.equals(CallFor.LOGIN)) {
            LoginResponse loginResponse = new Gson().fromJson(response, LoginResponse.class);
            try {
                if (loginResponse.getCode().equals("SUCCESS")) {
                    new_password = NewPassword1.getText().toString().trim();
                    if (needLoginValue.equals("changePassword")) {
                        new GetData(context, CallFor.CHANGE_PASSWORD, new_password).execute();
                    } else if (needLoginValue.equals("AddEmail")) {
                        String strEmailId = emailId.getText().toString().trim();
                        String apiData = "?email=" + strEmailId + "&loginByEmail=" + emailCheckBox;
                        new GetData(context, CallFor.ADD_EMAIL_ID, apiData).execute();
                    } else if (needLoginValue.equals("secondaryUser")) {
                        getSecondaryUser();
                    }
                } else {
                    CommonMethods.showToastError(this, "Something went wrong, Please try again");//, Toast.LENGTH_SHORT, true).show();
                }
            } catch (Exception ex) {
                Log.e("Ex Setting Details ", " " + ex);
            }
        }
        if (callFor.equals(CallFor.CHANGE_PASSWORD)) {
            LoginResponse loginResponse = new Gson().fromJson(response, LoginResponse.class);
            try {
                if (loginResponse.getCode().equals("SUCCESS")) {
                    CleverTap.cleverTap_Record_Event(this, Events.event_setting_change_password);
                    CommonMethods.showToastSuccess(this, "Password changed successfully");//, Toast.LENGTH_SHORT, true).show();
                    SheredPref.setPassword(this, new_password);
                    OldPassword.setText("");
                    NewPassword1.setText("");
                    NewPassword2.setText("");
                    title.setText("Visitors");
                    this.finish();
                } else if (loginResponse.getCode().equals("NEED_LOGIN")) {
                    UserBean userBean = new UserBean();
                    userBean.setUser_name(SheredPref.getUsername(this));
                    userBean.setUser_password(SheredPref.getPassword(this));
                    needLoginValue = "changePassword";
                    new PostData(new Gson().toJson(userBean), this, CallFor.LOGIN).execute();
                } else {
                    CommonMethods.showToastError(this, "Error in password change, Please try again");//, Toast.LENGTH_SHORT, true).show();
                }
            } catch (Exception ex) {
                Log.e("Ex", "");
            }
        }
        if (callFor.equals(CallFor.ADD_EMAIL_ID)) {
            LoginResponse loginResponse = new Gson().fromJson(response, LoginResponse.class);
            try {
                if (loginResponse.getCode().equals("SUCCESS")) {
                    if (checkEmail.isChecked()) {
                        SheredPref.setLoginByEmail(context, true);
                    } else {
                        SheredPref.setLoginByEmail(context, false);
                    }
                    SheredPref.setUserEmail(context, emailId.getText().toString().trim());

                    if (emailCheckBox == 0) {
                        CommonMethods.showToastSuccess(this, loginResponse.getMessage());//, Toast.LENGTH_LONG).show();
                    } else {

                        showCustomAlertWindow(loginResponse.getCode(),loginResponse.getMessage(),this);
                    }
                } else if (loginResponse.getCode().equals("ERROR")) {
                    CommonMethods.showToastError(this, loginResponse.getMessage());//, Toast.LENGTH_SHORT, true).show();
                } else if (loginResponse.getCode().equals("NEED_LOGIN")) {
                    UserBean userBean = new UserBean();
                    userBean.setUser_name(SheredPref.getUsername(this));
                    userBean.setUser_password(SheredPref.getPassword(this));
                    needLoginValue = "AddEmail";
                    new PostData(new Gson().toJson(userBean), this, CallFor.LOGIN).execute();
                } else {
                    CommonMethods.showToastError(this, "Error in password change, Please try again");//, Toast.LENGTH_SHORT, true).show();
                }

            } catch (Exception ex) {
                Log.e("Ex in email add", " " + ex);
            }
        }

    }

    private void setAdapterView(ArrayList<SecondaryUserBean> secondaryUserList) {
        Log.e("setAdapterView","setAdapterView");
        if (secondaryUserList.size() == 0) {
            noSecondaryUserAccountLabel.setVisibility(View.VISIBLE);
            secondaryAccountsList.setVisibility(View.GONE);
        } else {
            noSecondaryUserAccountLabel.setVisibility(View.GONE);
            secondaryAccountsList.setVisibility(View.VISIBLE);
            secondaryUseradapter = new SecondaryUserAdapter(this, R.layout.secondaryuser_row, secondaryUserList);
            secondaryAccountsList.setAdapter(secondaryUseradapter);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionConstant.REQ_CODE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openRingtoneFileChooser(selectedFileChooser, ringtoneCallBack);
                }
                break;
            }
            case MY_PERMISSIONS_REQUEST_READ_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallary();
                } else {
                    UtilityMethodsAndroid.ShowPermissionToast(context);
                    //CommonMethods.showToastSuccess(context, "Please allow this permission");//.show();
                }
            }

            case PermissionConstant.REQ_CODE_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    EasyImage.openCamera(context, PICK_IMAGE_REQUEST);
                } else {
                    UtilityMethodsAndroid.ShowPermissionToast(context);
                }
            }
        }
    }

    public void showChannelSettings(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                String channelId;
                if (!SheredPref.getNotificationChannelId(this).equals("")) {
                    channelId = SheredPref.getNotificationChannelId(this);
                } else {
                    channelId = this.getString(R.string.default_notification_channel_id);
                }

                notificationManager.areNotificationsEnabled();

                showChannelSettings(channelId);
            }
        }
    }

    public void openSettings(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                String channelId = SheredPref.getNotificationChannelId(this);
                showChannelSettings(channelId);
            }
        }
    }


    private void showChannelSettings(String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            startActivity(intent);
        }
    }

    public void getSecondaryUser() {
        new GetData(this, CallFor.GET_SECONDARY_USER, "").execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (flag.equals("Mana")) {
            title.setText("Manage Account");
            if (strSecondaryUserName.contains("TENANT")) {
                addUserBtn.setVisibility(View.GONE);
                layout_acct.setVisibility(View.GONE);
                noSecondaryUserAccountLabel.setVisibility(View.GONE);
                showCustomAlertWindow(this.getString(R.string.cannot_open_account),"For more information please contact " + this.getString(R.string.tenant_contact_emailId),this);
                getSecondaryUser();
            } else {
                title.setText("Manage Accounts");
                layout_acct.setVisibility(View.VISIBLE);
                addUserBtn.setVisibility(View.VISIBLE);
                password.setVisibility(View.GONE);
                notification.setVisibility(View.GONE);
                information.setVisibility(View.GONE);
                getSecondaryUser();
            }
        }
    }

    public void setNotification(View v) {
        //call api
        if(cc.isConnectingToInternet()){
            getNotificationMenuApi();
            dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_notification_menu);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
            window.setAttributes(wlp);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.show();
            initializeDialogComponant(dialog);
        }else{
            CommonMethods.showToastError(this, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
        }

    }

    public void openAppSettings(View view) {
        try {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }catch (Exception ex){
            CommonMethods.showToastError(this, getResources().getString(R.string.unable_to_open));
            Log.e(TAG, " In openAppSettings "+ex);
        }
    }

    public void showNotificationAccess(View view) {
        try{
            Intent intent=new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
        }catch (Exception e){
            Log.e(TAG, " In showOpenNotificationSettingsDialog "+e);
            CommonMethods.showToastError(SettingDetails.this, getResources().getString(R.string.unable_to_open));
        }
    }

    void initializeDialogComponant(Dialog dialog) {
        back = (ImageView) dialog.findViewById(R.id.imgBack);
        noticationMenuList = dialog.findViewById(R.id.noticationMenuList);
        btnSaveNotification = dialog.findViewById(R.id.btnSaveNotification);
        notificationMenuBeanTemp = new ArrayList<>();
        ArrayList<NotificationMenuBeans> bean = new ArrayList<>();
        btnSaveNotification.setOnClickListener(v -> {
            for (int i = 0; i < notificationMenuBean.size(); i++) {
                beans = new NotificationMenuBeans();
                beans.setAccess(notificationMenuBean.get(i).isAccess());
                beans.setName(notificationMenuBean.get(i).getName());
                beans.setId(notificationMenuBean.get(i).getId());
                bean.add(beans);
                notificationMenuBeans.setNotificationMenuBeans(bean);
            }
            saveNotificationApi(notificationMenuBeans);
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void getNotificationMenuApi() {
        new GetData(this, CallFor.GET_NOTIFICATION_MENU, "").execute();
    }

    public void setAdapterNoticationMenu(ArrayList<NotificationMenuBeans> notificationMenuBeanList) {
        menuNotificationAdapter = new MenuNotificationAdapter(this, R.layout.card_notification_menu_list_item, notificationMenuBeanList);
        noticationMenuList.setAdapter(menuNotificationAdapter);
    }

    public void saveNotificationApi(NotificationMenuResponseBean notificationMenuBeans) {
        new PostData(new Gson().toJson(notificationMenuBeans), this, CallFor.SAVE_NOTIFICATION_OPTION).execute();
    }


    //methods related browse photo

    public void browsePhoto(View v) {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_bottom_camera_and_gallary_picker);
        dialog.findViewById(R.id.cameraLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean cameraPermission = Permissions.askPermission(context, PermissionConstant.PERMISSION_CAMERA, PermissionConstant.REQ_CODE_CAMERA);
                if (cameraPermission) {
                    EasyImage.openCamera(context, PICK_IMAGE_REQUEST);
                }
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.gallaryLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    askPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_READ_STORAGE);
                } else {
                    openGallary();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void askPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            // We dont have permission
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        } else {
            openGallary();
        }
    }

    public void openGallary() {


//        Intent i = new Intent(
//                Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        startActivityForResult(i, PICK_IMAGE_REQUEST);
        EasyImage.openGallery(context, PICK_IMAGE_REQUEST);
    }
    public void showCustomAlertWindow(String heading,String subheading, Context context) {
//
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.CustomDialog);
////        LayoutInflater inflater = context.getLayoutInflater();
//
//        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View dialogView = li.inflate(R.layout.custum_alert_dialog, null);
//        TextView txtalertheading = dialogView.findViewById(R.id.txtalertheading);
//        TextView txtalertsubheading = dialogView.findViewById(R.id.txtalertsubheading);
//
//        TextView btnegative = dialogView.findViewById(R.id.btnegative);
//        TextView btnpositive = dialogView.findViewById(R.id.btnpositive);
//        txtalertheading.setText(heading);
//        txtalertsubheading.setText(subheading);
//        btnpositive.setVisibility(View.GONE);
//        final AlertDialog b = dialogBuilder.create();
//        b.setCancelable(true);
//        b.show();
//
//        btnpositive.setOnClickListener(v -> {
//            b.dismiss();
//        });


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.custum_alert_dialog, null);
        dialogBuilder.setView(dialogView);
        TextView txtalertheading = dialogView.findViewById(R.id.txtalertheading);
        TextView txtalertsubheading = dialogView.findViewById(R.id.txtalertsubheading);

        TextView btnegative = dialogView.findViewById(R.id.btnegative);
        TextView btnpositive = dialogView.findViewById(R.id.btnpositive);

        txtalertheading.setText(heading);
        txtalertsubheading.setText(subheading);

        btnegative.setVisibility(View.VISIBLE);
        btnpositive.setVisibility(View.GONE);

        final AlertDialog b = dialogBuilder.create();
        b.setCanceledOnTouchOutside(false);
        b.setCancelable(false);
        b.show();

        btnpositive.setText("Check");
        btnegative.setText("OK");
        btnpositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
        btnegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
    }
    public void CallLoginApi() {
        vztrack.gls.com.vztrack_user.profile.UserBean userBean = new vztrack.gls.com.vztrack_user.profile.UserBean();
        userBean.setUser_name(SheredPref.getUsername(getApplicationContext()));
        userBean.setUser_password(SheredPref.getPassword(getApplicationContext()));
        new PostData(new Gson().toJson(userBean), SettingDetails.this, CallFor.LOGIN).execute();
    }
}

