package vztrack.gls.com.vztrack_user.activity;

/**
 * Created by sandeep on 14/3/16.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.google.gson.Gson;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;


import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.CustumView.CustomButton;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.beans.MarketplaceBean;
import vztrack.gls.com.vztrack_user.beans.ResponceBean;
import vztrack.gls.com.vztrack_user.profile.UserBean;
import vztrack.gls.com.vztrack_user.responce.LoginResponse;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.Constants;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.KeyboardUtil;
import vztrack.gls.com.vztrack_user.utils.LoadImage;
import vztrack.gls.com.vztrack_user.utils.PermissionConstant;
import vztrack.gls.com.vztrack_user.utils.Permissions;
import vztrack.gls.com.vztrack_user.utils.PostData;
import vztrack.gls.com.vztrack_user.utils.PostDataForFileNew;
import vztrack.gls.com.vztrack_user.utils.SheredPref;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

public class MarketPlacePlaceNewAd extends BaseActivity {
    BaseActivity context;
    CheckConnection cc;
    CoordinatorLayout coordinatorLayout;
    RelativeLayout llMainLinearLayout;

    private CheckBox checkBoxProduct, checkBoxService;
    String selectedChecBoxValue;
    EditText etTitle, etDescription, etPrice, etMobileNo;
    TextView tvCounterTitle, tvCounterDesc, tvRemove;
    CheckBox checkBoxFree;
    CustomButton btnAddPost;
    ImageView imgPhoto;
    int freePriceFlag = 0;
    File file;
    private static int PICK_IMAGE_REQUEST = 1;
    int marketPlaceId;
    String photoAction;
    InputStream inputStream;
    String strTitle, strDescription, strPrice, strMobileNo, strPostedBy, strType, img_url;
    private final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 1;
    int mobileNoMinLen;
    int mobileNoMaxLen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketplace_details);
//        getSupportActionBar().setTitle("Marketplace");
        context = MarketPlacePlaceNewAd.this;
        cc = new CheckConnection(context);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView title=findViewById(R.id.title);
        title.setText("Marketplace");

        ImageView backpress=findViewById(R.id.backpress);
        backpress.setOnClickListener(v->onBackPressed());

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        llMainLinearLayout = (RelativeLayout) findViewById(R.id.llMainLinearLayout);

        checkBoxProduct =  findViewById(R.id.checkbox_product);
        checkBoxService =  findViewById(R.id.checkbox_service);

        tvCounterTitle = (TextView) findViewById(R.id.counterTitle);
        tvCounterDesc = (TextView) findViewById(R.id.counterDesc);
        tvRemove = (TextView) findViewById(R.id.tvRemove);

        etTitle = (EditText) findViewById(R.id.adTitle);
        etDescription = (EditText) findViewById(R.id.description);
        etPrice = (EditText) findViewById(R.id.price);
        etMobileNo = (EditText) findViewById(R.id.mobileNo);
        checkBoxFree = (CheckBox) findViewById(R.id.free);
        imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
        btnAddPost =  findViewById(R.id.btnAddPost);

        // set max len of phone number edit text
        UtilityMethodsAndroid.setEditTextMaxLength(SheredPref.getMaxMobileLen(context), etMobileNo);
        mobileNoMinLen = SheredPref.getMinMobileLen(context);
        mobileNoMaxLen = SheredPref.getMaxMobileLen(context);

        checkBoxProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxProduct.setChecked(true);
                checkBoxService.setChecked(false);
                selectedChecBoxValue = "Product";
                etTitle.setHint(R.string.title_product_hint);
            }
        });

        checkBoxService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxProduct.setChecked(false);
                checkBoxService.setChecked(true);
                selectedChecBoxValue = "Service";
                etTitle.setHint(R.string.title_service_hint);
            }
        });
        tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonMethods.showToastSuccess(context, "Photo removed");//, Toast.LENGTH_LONG).show();
                imgPhoto.setImageResource(R.drawable.ic_action_upload);
                tvRemove.setVisibility(View.GONE);
                photoAction = "";
                file = null;
                inputStream = null;
            }
        });
        marketPlaceId = Integer.parseInt(getIntent().getStringExtra("ID"));
        if (marketPlaceId != 0) {
            strTitle = getIntent().getStringExtra("TITLE");
            strDescription = getIntent().getStringExtra("DESC");
            strPostedBy = getIntent().getStringExtra("POSTED_BY");
            strPrice = getIntent().getStringExtra("PRICE");
            strType = getIntent().getStringExtra("TYPE");
            strMobileNo = getIntent().getStringExtra("MOBILE_NO");
            img_url = getIntent().getStringExtra("IMG_URL");

            etTitle.setText(strTitle);
            etDescription.setText(strDescription);
            etPrice.setText(strPrice);
            etMobileNo.setText(strMobileNo);
            if (strType.equalsIgnoreCase("service")) {
                checkBoxProduct.setChecked(false);
                checkBoxService.setChecked(true);
                selectedChecBoxValue = "Service";
                etTitle.setHint(R.string.title_service_hint);
            }
            if (strType.equalsIgnoreCase("product")) {
                checkBoxProduct.setChecked(true);
                checkBoxService.setChecked(false);
                selectedChecBoxValue = "Product";
                etTitle.setHint(R.string.title_product_hint);
            }

            if (strPrice.equals("")) {
                freePriceFlag = 1;
                etPrice.setEnabled(false);
                checkBoxFree.setChecked(true);
            } else {
                freePriceFlag = 0;
                etPrice.setEnabled(true);
                etPrice.setText(strPrice);
                checkBoxFree.setChecked(false);
            }
            if (img_url.equals("")) {
                imgPhoto.setImageResource(R.drawable.ic_action_upload);
                tvRemove.setVisibility(View.GONE);
            } else {
                String strImgUrl = Constants.BASE_IMG_URL + "/" + img_url;
                new LoadImage().loadImage(this, R.drawable.no_photo_icon, strImgUrl, imgPhoto, null);
                tvRemove.setVisibility(View.VISIBLE);
            }
            photoAction = "old";
        } else {
            checkBoxProduct.setChecked(false);
            checkBoxService.setChecked(false);
            photoAction = "";
        }

        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int len = editable.length();
                tvCounterTitle.setText(len + " / max 100");
            }
        });

        etDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int len = editable.length();
                tvCounterDesc.setText(len + " / max 500");
            }
        });


        checkBoxFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is chkIos checked?
                if (((CheckBox) v).isChecked()) {
                    freePriceFlag = 1;
                    etPrice.setEnabled(false);
                    strPrice = etPrice.getText().toString().trim();
                    etPrice.setText("");
                } else {
                    freePriceFlag = 0;
                    etPrice.setEnabled(true);
                    etPrice.setText(strPrice);
                }
            }
        });

        btnAddPost.setOnClickListener(view -> SubmitData());
    }

    public void SubmitData() {
        strTitle = etTitle.getText().toString().trim();
        strDescription = etDescription.getText().toString().trim();
        strPrice = etPrice.getText().toString().trim();
        strMobileNo = etMobileNo.getText().toString().trim();


        if (checkBoxProduct.isChecked() == false && checkBoxService.isChecked() == false) {
            CommonMethods.showToastSuccess(this, "Please select type Service or Product");//.show();
        } else if (strTitle.equals("")) {
            CommonMethods.showToastSuccess(this, "Please enter title");//.show();
        } else if (strDescription.equals("")) {
            CommonMethods.showToastSuccess(this, "Please enter description");//.show();
        } else if (freePriceFlag == 0 && strPrice.equals("")) {
            CommonMethods.showToastSuccess(this, "Please enter price");//.show();
        } else if (strMobileNo.length() != 0 && !UtilityMethods.between(strMobileNo.length(), mobileNoMaxLen, mobileNoMinLen)) {
            CommonMethods.showToastSuccess(this, "Please enter valid mobile number");//.show();
        } else {
            MarketplaceBean marketplaceBean = new MarketplaceBean();
            marketplaceBean.setType(selectedChecBoxValue);
            marketplaceBean.setTitle(strTitle);
            marketplaceBean.setDescription(strDescription);
            marketplaceBean.setPrice(strPrice);
            marketplaceBean.setIsFree(freePriceFlag);
            marketplaceBean.setMobileNo(strMobileNo);
            if (marketPlaceId != 0) {
                marketplaceBean.setMarketplaceId(marketPlaceId);
                marketplaceBean.setEditedPhoto(photoAction);
                marketplaceBean.setAction("Edit");
            } else {
                marketplaceBean.setAction("Create");
            }

            // CleverTap
            CleverTapAPI cleverTap = null;
            try {
                cleverTap = CleverTapAPI.getInstance(this);
                HashMap<String, Object> marketPlace = new HashMap<String, Object>();
                if (marketPlaceId != 0) {
                    marketPlace.put(Events.market_place_operation, "Edit");
                } else {
                    marketPlace.put(Events.market_place_operation, "Create");
                }
                cleverTap.event.push(Events.market_place_ad, marketPlace);
            } catch (CleverTapMetaDataNotFoundException e) {
                e.printStackTrace();
            } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
                cleverTapPermissionsNotSatisfied.printStackTrace();
            }

            new PostDataForFileNew(new Gson().toJson(marketplaceBean), inputStream, this, CallFor.CREATE_EDIT_CLOSE_ADV).execute();
        }

    }

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

    public void openGallary() {
        EasyImage.openGallery(context, PICK_IMAGE_REQUEST);
    }

    public void ShowBottomSheetDialog(View v) {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.write_comment_layout);
        final EditText commentText = (EditText) dialog.findViewById(R.id.commentText);
        final TextView name = (TextView) dialog.findViewById(R.id.name);
        final ImageView sendButton = (ImageView) dialog.findViewById(R.id.btnSend);
        final LinearLayout llMainLayout = (LinearLayout) dialog.findViewById(R.id.llMainLayout);
        new KeyboardUtil(this, llMainLayout);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
                    dialog.dismiss();
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Log.e("Exception : ", " " + e);
                CommonMethods.showToastError(context, "Unable to select image");//, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                try {
                    Bitmap bitmap = UtilityMethodsAndroid.decodeSampledBitmapFromFile(imageFile.getAbsolutePath(), 400, 500);
                    bitmap = UtilityMethodsAndroid.rotateImage(imageFile, bitmap);
                    inputStream = UtilityMethodsAndroid.ConvertBitmapToInptuStream(bitmap);
                    int length = inputStream.available();
                    imgPhoto.setImageBitmap(bitmap);
                    tvRemove.setVisibility(View.VISIBLE);
                    photoAction = "new";
                } catch (OutOfMemoryError e) {
                    Log.e("Out Of Memory ", " " + e);
                    e.printStackTrace();
                } catch (Exception e) {
                    Log.e("Exception ", " " + e);
                    e.printStackTrace();
                }
            }
        });
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

    @Override
    public void onGetResponse(String response, String callFor) {
        LoginResponse loginResponse = null;
        ResponceBean responceBean;
        if (callFor.equals(CallFor.LOGIN)) {
            loginResponse = new Gson().fromJson(response, LoginResponse.class);
            try {
                if (loginResponse.getCode().equals("SUCCESS")) {
                    MarketplaceBean marketplaceBean = new MarketplaceBean();
                    marketplaceBean.setType(selectedChecBoxValue);
                    marketplaceBean.setTitle(strTitle);
                    marketplaceBean.setDescription(strDescription);
                    marketplaceBean.setPrice(strPrice);
                    marketplaceBean.setIsFree(freePriceFlag);
                    marketplaceBean.setAction("Create");
                    new PostDataForFileNew(new Gson().toJson(marketplaceBean), inputStream, this, CallFor.CREATE_EDIT_CLOSE_ADV).execute();
                } else if (loginResponse.getCode().equals("NEED_LOGIN")) {

                } else {
                    CommonMethods.showToastError(this, "Something went wrong, Please try again");//, Toast.LENGTH_SHORT, true).show();
                }
            } catch (Exception ex) {
                Log.e("Ex Setting Details ", " " + ex);
            }
        }
        if (callFor.equals(CallFor.CREATE_EDIT_CLOSE_ADV)) {
            responceBean = new Gson().fromJson(response, ResponceBean.class);
            try {
                if (responceBean.getCode().equals("SUCCESS")) {
                    CommonMethods.showToastSuccess(this, responceBean.getMessage());//, Toast.LENGTH_SHORT, true).show();
                    new GetData(MainActivity.mainActivity, CallFor.GET_ALL_MARKET_PLACE_ADD, "").execute();
                    if (marketPlaceId != 0) {
                        MarketPlaceAdDeatils.context.finish();
                    }
                    this.finish();
                } else if (loginResponse.getCode().equals("ERROR")) {
                    CommonMethods.showToastError(this, responceBean.getMessage());//, Toast.LENGTH_SHORT, true).show();
                } else if (loginResponse.getCode().equals("NEED_LOGIN")) {
                    UserBean userBean = new UserBean();
                    userBean.setUser_name(SheredPref.getUsername(this));
                    userBean.setUser_password(SheredPref.getPassword(this));
                    new PostDataForFileNew(new Gson().toJson(userBean), null, this, CallFor.LOGIN).execute();
                } else {
                    CommonMethods.showToastError(this, responceBean.getMessage());//, Toast.LENGTH_SHORT, true).show();
                }

            } catch (Exception ex) {
                Log.e("Ex", "");
            }
        }
    }

    public void askPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            // We dont have permission
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        } else {
            openGallary();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallary();
                } else {
                    UtilityMethodsAndroid.ShowPermissionToast(context);
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
}