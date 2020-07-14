package vztrack.gls.com.vztrack_user.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ale.infra.contact.RainbowPresence;
import com.ale.infra.http.adapter.concurrent.RainbowServiceException;
import com.ale.infra.proxy.avatar.IAvatarProxy.IAvatarListener;
import com.ale.infra.proxy.users.IUserProxy.IUsersListener;
import com.ale.rainbowsdk.RainbowSdk;

import java.io.File;


import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CompressImage;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.KeyboardUtil;
import vztrack.gls.com.vztrack_user.utils.PermissionConstant;
import vztrack.gls.com.vztrack_user.utils.Permissions;
import vztrack.gls.com.vztrack_user.utils.SheredPref;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

public class Rainbow_Profile_Fragment extends Fragment{
    ImageView imgProfile, imgStatus, editProfilePhoto, editPresence, editName, editLastName;
    TextView tvUserName, tvEmailId, tvPresence, tvNoImgText, tvUserLastName;
    Bitmap profilePhoto;
    String strFirstName, strLastName, strUserName, strEmail, strPresence;
    Context context;
    private static int PICK_IMAGE_REQUEST = 1;
    private final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 1;
    String strFName;
    String strLName;
    ProgressDialog progressDialog;
    private CardView changeAccount;
    private long mLastClickTime = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_rainbow_profile, null);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.bottom_menu_item4));
        imgProfile = (ImageView) root.findViewById(R.id.profile_photo);
        tvUserName = (TextView) root.findViewById(R.id.tvUserName);
        tvEmailId = (TextView) root.findViewById(R.id.tvEmailId);
        tvPresence = (TextView) root.findViewById(R.id.tvPresence);
        tvNoImgText = (TextView) root.findViewById(R.id.noImgText);
        imgStatus = (ImageView) root.findViewById(R.id.imgStatus);
        editProfilePhoto = (ImageView) root.findViewById(R.id.editProfilePhoto);
        editPresence = (ImageView) root.findViewById(R.id.editPresence);
        editName = (ImageView) root.findViewById(R.id.editName);
        tvUserLastName = (TextView) root.findViewById(R.id.tvUserLastName);
        editLastName = (ImageView) root.findViewById(R.id.editLastName);
        changeAccount = (CardView) root.findViewById(R.id.changeAccount);
        context = getActivity();
        setHasOptionsMenu(true);
        if(SheredPref.getRainbowAccountCount(getActivity())<=1){
            changeAccount.setVisibility(View.GONE);
        }

        strFirstName = RainbowSdk.instance().myProfile().getConnectedUser().getFirstName().trim();
        strLastName = RainbowSdk.instance().myProfile().getConnectedUser().getLastName().trim();

        strUserName = strFirstName+" "+strLastName;
        strEmail = RainbowSdk.instance().myProfile().getConnectedUser().getMainEmailAddress();

        tvUserName.setText(strFirstName);
        tvUserLastName.setText(strLastName);
        tvEmailId.setText(strEmail);
        strPresence = RainbowSdk.instance().myProfile().getConnectedUser().getPresence().toString();
        updatePresence(strPresence);
        updatePhoto();

        imgProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowProfilePhoto();
            }
        });

        editProfilePhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfilePhoto();
            }
        });

        editName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                editFirstName();
            }
        });

        editLastName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                editLastName();
            }
        });

        editPresence.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                editPresence();
            }
        });

        changeAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Preventing multiple clicks, using threshold of 1 second
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                MainActivity.callFromRainbowProfile = true;
                SheredPref.setRun(getActivity(), "RUN");
                new GetData(MainActivity.mainActivity, CallFor.RAINBOW_ACCOUNT_DETAILS, "").execute();
            }
        });

        return root;
    }

    private void editProfilePhoto(){
        if(CheckPermissionsCamAndStorage()){
            browsePhoto();
        }else{
            UtilityMethodsAndroid.ShowPermissionToast(context);
        }
    }

    public boolean CheckPermissionsCamAndStorage(){
        boolean val = false;
        boolean resultCamera = Permissions.askPermission(context, PermissionConstant.PERMISSION_CAMERA, PermissionConstant.REQ_CODE_CAMERA);
        if(resultCamera){
            boolean resultStorage = Permissions.askPermission(context, PermissionConstant.PERMISSION_EXTERNAL_STORAGE, PermissionConstant.REQ_CODE_EXTERNAL_STORAGE);
            if(resultStorage){
                val = true;
            }
        }
        return val;
    }

    private void ShowProfilePhoto(){
        showPhotoAndEdit(1);
    }

    private void editFirstName(){
        showPhotoAndEdit(2);
    }

    private void editLastName(){
        showPhotoAndEdit(5);
    }

    private void editPresence(){
        showPhotoAndEdit(3);
    }

    private String setPresence(String presence){
        String status = presence.replace("_"," ");
        status = UtilityMethods.camelCase(status);
        if(status.toLowerCase().contains("manual away")){
            status = "Away";
        }else if(status.toLowerCase().contains("donotdisturb")){
            status = "Do Not Disturb";
        }else if(status.toLowerCase().contains("xa")){
            status = "Offline";
        }
        return status;
    }

    public void browsePhoto() {
        final BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_bottom_camera_and_gallary_picker_remove_photo);
        dialog.findViewById(R.id.cameraLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openCamera(Rainbow_Profile_Fragment.this, PICK_IMAGE_REQUEST);
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
        dialog.findViewById(R.id.removeLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //RainbowSdk.instance().myProfile().updatePhoto(null, avatarListener);
            }
        });
        dialog.show();
    }

    public void showPhotoAndEdit(int flag) {
        final BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_show_photo_edit_name_change_presence);
        LinearLayout updateName = (LinearLayout) dialog.findViewById(R.id.updateName);
        LinearLayout updatePresence = (LinearLayout)dialog.findViewById(R.id.updatePresence);
        ImageView imgProfilePhoto = (ImageView)dialog.findViewById(R.id.photoProfile);
        TextView title = (TextView) dialog.findViewById(R.id.title);
        LinearLayout mainLayout = (LinearLayout) dialog.findViewById(R.id.mainLayout);

        new KeyboardUtil(getActivity(), mainLayout);
        if(flag == 1){
            imgProfilePhoto.setVisibility(View.VISIBLE);
            updatePresence.setVisibility(View.GONE);
            updateName.setVisibility(View.GONE);
            title.setText("Profile Photo");
            imgProfilePhoto.setImageBitmap(null);
            profilePhoto = RainbowSdk.instance().myProfile().getConnectedUser().getPhoto();
            imgProfilePhoto.setImageBitmap(profilePhoto);
        }else if(flag == 2){
            imgProfilePhoto.setVisibility(View.GONE);
            updatePresence.setVisibility(View.GONE);
            updateName.setVisibility(View.VISIBLE);
            title.setText("Update First Name");
            EditText firstName = (EditText) dialog.findViewById(R.id.etFirstName);
            EditText lastName = (EditText) dialog.findViewById(R.id.etLastName);
            lastName.setVisibility(View.GONE);
            Button btnUpdate =  dialog.findViewById(R.id.btnUpdate);
            firstName.setText(RainbowSdk.instance().myProfile().getConnectedUser().getFirstName());
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    strFName = firstName.getText().toString().trim();
                    if(strFName.equals("")){
                        CommonMethods.showToastError(context, "Enter First Name");//.show();
                    }else if(strFName.equals(strFirstName)){
                        CommonMethods.showToastError(context, "You entered same name, no need to change");//.show();
                    }else{
                        progressDialog = ProgressDialog.show(context,"","Updating...");
                        dialog.dismiss();
                        RainbowSdk.instance().myProfile().updateFirstName(strFName, new IUsersListener() {
                            @Override
                            public void onSuccess() {
                                progressDialog.dismiss();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvUserName.setText(strFName);
                                        tvUserLastName.setText(strLName);
                                        progressDialog.dismiss();
                                        CommonMethods.showToastSuccess(context,"Successfully updated First Name");//.show();
                                    }
                                });
                            }

                            @Override
                            public void onFailure(RainbowServiceException e) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvUserLastName.setText(strLName);
                                        progressDialog.dismiss();
                                        CommonMethods.showToastError(context,"Error in updating First Name");//.show();
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }else if(flag == 5){
            imgProfilePhoto.setVisibility(View.GONE);
            updatePresence.setVisibility(View.GONE);
            updateName.setVisibility(View.VISIBLE);
            title.setText("Update Last Name");
            EditText firstName = (EditText) dialog.findViewById(R.id.etFirstName);
            firstName.setVisibility(View.GONE);
            EditText lastName = (EditText) dialog.findViewById(R.id.etLastName);
            Button btnUpdate =  dialog.findViewById(R.id.btnUpdate);
            lastName.setText(RainbowSdk.instance().myProfile().getConnectedUser().getLastName());
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    strLName = lastName.getText().toString().trim();
                    if(strLName.equals("")){
                        CommonMethods.showToastError(context, "Enter Last Name");//.show();
                    }else if(strLName.equals(strLastName)){
                        CommonMethods.showToastError(context, "You entered same name, no need to change");//.show();
                    }else{
                        progressDialog = ProgressDialog.show(context,"","Updating...");
                        dialog.dismiss();
                        RainbowSdk.instance().myProfile().updateLastName(strLName, new IUsersListener() {
                            @Override
                            public void onSuccess() {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvUserLastName.setText(strLName);
                                        progressDialog.dismiss();
                                        CommonMethods.showToastSuccess(context,"Successfully updated Last Name");//.show();
                                    }
                                });
                            }

                            @Override
                            public void onFailure(RainbowServiceException e) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        CommonMethods.showToastError(context,"Error in updating Last Name");//.show();
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }
        else if(flag == 3){
            imgProfilePhoto.setVisibility(View.GONE);
            updatePresence.setVisibility(View.VISIBLE);
            updateName.setVisibility(View.GONE);
            title.setText("Set Presence");

            TextView tvOnline = (TextView) dialog.findViewById(R.id.tvOnline);
            TextView tvAway = (TextView) dialog.findViewById(R.id.tvAway);
            TextView tvInvisible = (TextView) dialog.findViewById(R.id.tvInvisible);
            TextView tvDnd = (TextView) dialog.findViewById(R.id.tvDnd);


            ImageView imgOnline = (ImageView) dialog.findViewById(R.id.imgOnline);
            ImageView imgAway = (ImageView) dialog.findViewById(R.id.imgAway);
            ImageView imgInvisible = (ImageView) dialog.findViewById(R.id.imgInvisible);
            //imgInvisible.setVisibility(View.GONE);
            ImageView imgDnd = (ImageView) dialog.findViewById(R.id.imgDnd);

            GradientDrawable drawable = (GradientDrawable)imgOnline.getBackground();
            drawable.setStroke(2, Color.parseColor("#000000")); // set stroke width and stroke color
            drawable.setColor(getResources().getColor(R.color.green));


            drawable = (GradientDrawable)imgAway.getBackground();
            drawable.setStroke(2, Color.parseColor("#000000")); // set stroke width and stroke color
            drawable.setColor(getResources().getColor(R.color.yellow));

            drawable = (GradientDrawable)imgInvisible.getBackground();
            drawable.setStroke(2, Color.parseColor("#000000")); // set stroke width and stroke color
            drawable.setColor(getResources().getColor(R.color.subtextcolor));


            drawable = (GradientDrawable)imgDnd.getBackground();
            drawable.setStroke(2, Color.parseColor("#000000")); // set stroke width and stroke color
            drawable.setColor(getResources().getColor(R.color.nwred));


            tvOnline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setPresenceTo(RainbowPresence.MOBILE_ONLINE);
                    updatePresence("Mobile Online");
                    dialog.dismiss();
                }
            });
            tvAway.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setPresenceTo(RainbowPresence.MANUAL_AWAY);
                    updatePresence("Away");
                    dialog.dismiss();
                }
            });
            tvInvisible.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setPresenceTo(RainbowPresence.XA);
                    updatePresence("Offline");
                    dialog.dismiss();
                }
            });
            tvDnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setPresenceTo(RainbowPresence.DND);
                    updatePresence("Do Not Disturb");
                    dialog.dismiss();
                }
            });
        }
        dialog.show();
    }


    public void openGallary(){
        Log.e("PICK_IMAGE_REQUEST",PICK_IMAGE_REQUEST+"--");
        EasyImage.openGallery(Rainbow_Profile_Fragment.this, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("profile requestCode",requestCode+"");
        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
                @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Log.e("onImagePickerError","onImagePickerError");

                    CommonMethods.showToastError(context,"Unable to select image");//, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                Log.e("onImagePicked","onImagePicked");
                try {
                    if(imageFile.isFile()){
                        Log.e("imageFile","imageFile");
                        progressDialog = ProgressDialog.show(context,"","Updating...");
                        uploadProfilePhoto(imageFile, progressDialog);
                    }else{
                        CommonMethods.showToastError(getActivity(), "Invalid file selected");//, Toast.LENGTH_SHORT).show();
                    }
                }catch (OutOfMemoryError e) {
                    Log.e("Out Of Memory "," "+e);
                    e.printStackTrace();
                }catch (Exception e) {
                    Log.e("Exception "," "+e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                super.onCanceled(source, type);
                Log.e("onCanceled","onCanceled");
            }
        });
    }

    public void askPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            // We dont have permission
            requestPermissions(new String[]{permission}, requestCode);
        } else {
            openGallary();
        }
    }

    private void uploadProfilePhoto(File selectedPhoto, ProgressDialog progressDialog){
        Log.e("uploadProfilePhoto","uploadProfilePhoto");
        CompressImage compressImage = new CompressImage();
        String filename = compressImage.compressImage(Uri.fromFile(selectedPhoto), context);
        File file = new File(filename);
        imgProfile.setImageURI(Uri.fromFile(file));
        RainbowSdk.instance().myProfile().updatePhoto(file, new IAvatarListener() {
            @Override
            public void onAvatarSuccess(Bitmap bitmap) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(progressDialog!=null){
                            progressDialog.dismiss();
                        }
                        CommonMethods.showToastSuccess(getActivity(), "Profile updated successfully");//, Toast.LENGTH_LONG).show();
                        if(bitmap!=null){
                            imgProfile.setImageBitmap(bitmap);
                        }else{
                            CommonMethods.showToastError(getActivity(), "It may take sometime to update");//, Toast.LENGTH_LONG).show();
                            imgProfile.setImageURI(Uri.fromFile(file));
                        }
                    }
                });
            }

            @Override
            public void onAvatarFailure(RainbowServiceException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(progressDialog!=null){
                            progressDialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    private void updatePhoto() {
        profilePhoto = RainbowSdk.instance().myProfile().getConnectedUser().getPhoto();
        if(profilePhoto == null){
            tvNoImgText.setVisibility(View.VISIBLE);
            imgProfile.setVisibility(View.GONE);
            String name = RainbowSdk.instance().myProfile().getConnectedUser().getFirstName()+" "+RainbowSdk.instance().myProfile().getConnectedUser().getLastName();
            tvNoImgText.setText(UtilityMethodsAndroid.getIntitialLetter(name));
        }else{
            tvNoImgText.setVisibility(View.GONE);
            imgProfile.setVisibility(View.VISIBLE);
            imgProfile.setImageBitmap(profilePhoto);
        }
    }

    public void updatePresence(String strPresence){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String status = setPresence(strPresence);
                tvPresence.setText(status);
                imgStatus.setBackgroundResource(R.drawable.status_bg);
                imgStatus.setImageDrawable(null);
                GradientDrawable drawable = (GradientDrawable)imgStatus.getBackground();
                drawable.setStroke(2, Color.parseColor("#000000")); // set stroke width and stroke color
                if(status.equals("Online") || status.equals("Mobile Online")){
                    drawable.setColor(getResources().getColor(R.color.green));
                }else if(status.equals("Away")){
                    drawable.setColor(getResources().getColor(R.color.yellow));
                }else if(status.equals("Offline")){
                    drawable.setColor(getResources().getColor(R.color.subtextcolor));
                }else if(status.equals("Do Not Disturb")){
//                    imgStatus.setBackgroundResource(R.drawable.status_bg);
                    drawable.setColor(getResources().getColor(R.color.nwred));
//                    imgStatus.setImageResource(R.drawable.ic_do_not_disturb_on_black_24dp);
                }
            }
        });
    }

    private void setPresenceTo(RainbowPresence rainbowPresence) {
        RainbowSdk.instance().myProfile().setPresenceTo(rainbowPresence);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionConstant.REQ_CODE_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Permissions.askPermission(context, PermissionConstant.PERMISSION_EXTERNAL_STORAGE, PermissionConstant.REQ_CODE_EXTERNAL_STORAGE);
                }
                break;
            }

            case PermissionConstant.REQ_CODE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    browsePhoto();
                }
                break;
            }
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        Log.e("come in","profile");
        inflater.inflate(R.menu.main, menu);
        setHasOptionsMenu(true);
        menu.findItem(R.id.action_call).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
//
//        searchView.setVisibility(View.GONE);

    }
}