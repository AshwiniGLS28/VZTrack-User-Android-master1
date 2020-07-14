package vztrack.gls.com.vztrack_user.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.google.gson.Gson;

import java.util.HashMap;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.adapters.Market_place_Comment_RecyclerViewAdapter;
import vztrack.gls.com.vztrack_user.beans.CommentBean;
import vztrack.gls.com.vztrack_user.beans.MarketplaceBean;
import vztrack.gls.com.vztrack_user.responce.LoginResponse;
import vztrack.gls.com.vztrack_user.responce.MarketPlaceResponceBean;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Constants;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.KeyboardUtil;
import vztrack.gls.com.vztrack_user.utils.LoadImage;
import vztrack.gls.com.vztrack_user.utils.PostData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

public class MarketPlaceAdDeatils extends BaseActivity implements View.OnClickListener {
    String img_url, strTitle, strDescription, strPrice, strType, strPostedBy, strMobileNo;
    int marketPlaceId;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fab1,fab2;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;
    private TextView tvTitle, tvDesc, tvPrice, tvType, tvPostedBy, tvMobile, tvCommentsCount, tvNoComment;
    private TableRow trMobileNo;
    private RecyclerView comment_recycler_view;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    public static BaseActivity context;
    CheckConnection cc;
    String selectedValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_place_ad_deatils);
        context = this;
        strTitle = getIntent().getStringExtra("TITLE");
        strTitle = strTitle.replaceAll("\n+","\n");
        strDescription = getIntent().getStringExtra("DESC");
        strDescription = strDescription.replaceAll("\n+","\n");
        strPostedBy = getIntent().getStringExtra("POSTED_BY");
        strPrice = getIntent().getStringExtra("PRICE");
        strType = getIntent().getStringExtra("TYPE");
        strMobileNo = getIntent().getStringExtra("MOBILE_NO");
        img_url = getIntent().getStringExtra("IMG_URL");
        marketPlaceId = Integer.parseInt(getIntent().getStringExtra("ID"));
        String strImgUrl = Constants.BASE_IMG_URL + "/" + img_url;
        ImageView imageView = findViewById(R.id.img);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        new LoadImage().loadImage(this, R.drawable.image_background, strImgUrl, imageView, null);
        cc = new CheckConnection(context);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvDesc = (TextView) findViewById(R.id.tvDescription);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvType = (TextView) findViewById(R.id.tvType);
        tvPostedBy = (TextView) findViewById(R.id.tvPostedBy);
        tvMobile = (TextView) findViewById(R.id.tvMobile);
        tvCommentsCount = (TextView) findViewById(R.id.tvCommentsCount);
        tvNoComment = (TextView) findViewById(R.id.tvNoComment);
        trMobileNo = (TableRow) findViewById(R.id.trMobileNo);
        comment_recycler_view = (RecyclerView) findViewById(R.id.comment_recycler_view);
        CleverTap.cleverTap_Record_Event(this, Events.market_place_details_screen);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);

        if(!strPostedBy.trim().equals(SheredPref.getFlat_No(context).trim())){
            fab.setVisibility(View.GONE);
        }

        tvDesc.setText(strDescription);
        if(strPrice == null || strPrice.equals("")){
            tvPrice.setText("Free");
        }else{
            tvPrice.setText(strPrice);
            //tvPrice.setText("\u20B9"+strPrice);
        }

        tvTitle.setText(strTitle);
        tvType.setText(strType);
        tvPostedBy.setText(strPostedBy);
        tvMobile.setText(strMobileNo);
        if(strMobileNo==null || strMobileNo.equals("")){
            trMobileNo.setVisibility(View.GONE);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        ctl.setTitle(strType.substring(0,1).toUpperCase()+ strType.substring(1).toLowerCase());
        ctl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPhotoDialog(img_url);
            }
        });
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0)
                {
                    // Fully expanded
                }
                else
                {
                    if(isFabOpen){
                        CloseFab();
                    }
                }
            }
        });

        comment_recycler_view.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        comment_recycler_view.setLayoutManager(mLayoutManager);

        if(cc.isConnectingToInternet()){
            String extendedUrl = "?marketplaceId="+marketPlaceId;
            new GetData(context, CallFor.GET_MARKET_PLACE_DETAILS, extendedUrl).execute();
        }else{
            CommonMethods.showToastSuccess(this, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fab1:
                if(isFabOpen){
                    if (cc.isConnectingToInternet()) {
                        Intent I = new Intent(context, MarketPlacePlaceNewAd.class);
                        I.putExtra("IMG_URL", img_url);
                        I.putExtra("TITLE", strTitle);
                        I.putExtra("DESC", strDescription);
                        I.putExtra("PRICE", strPrice);
                        I.putExtra("TYPE", strType);
                        I.putExtra("POSTED_BY", strPostedBy);
                        I.putExtra("MOBILE_NO", strMobileNo);
                        I.putExtra("ID", ""+marketPlaceId);
                        context.startActivity(I);
                    }else{
                        CommonMethods.showToastSuccess(context, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                    }
                    CloseFab();
                }
                Log.d("Raj", "Fab 1");
                break;
            case R.id.fab2:
                if(isFabOpen){
                    CloseFab();
                }
                ShowReasonDialog(marketPlaceId);
                break;
        }
    }

    public void animateFAB(){
        if(isFabOpen){
            CloseFab();
        } else {
            OpenFab();
        }
    }
    public void OpenFab(){
        fab.startAnimation(rotate_forward);
        fab1.startAnimation(fab_open);
        fab2.startAnimation(fab_open);
        fab1.setClickable(true);
        fab2.setClickable(true);
        isFabOpen = true;
    }
    public void CloseFab(){
        fab.startAnimation(rotate_backward);
        fab1.startAnimation(fab_close);
        fab2.startAnimation(fab_close);
        fab1.setClickable(false);
        fab2.setClickable(false);
        isFabOpen = false;
    }

    public void ShowPhotoDialog(String imageUrl){
        final Dialog dialog = new Dialog(this);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_domestic_help);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.image);
        String strImgUrl = Constants.BASE_IMG_URL + "/" + imageUrl;
        new LoadImage().loadImage(this, R.drawable.no_photo_icon, strImgUrl, imageView, null);
        dialog.show();
    }

    public void ShowBottomSheetForCommentDialog(View v){
        //if(!strStatus.equalsIgnoreCase("Close")){
            final BottomSheetDialog dialog = new BottomSheetDialog(this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.write_comment_layout);
            final EditText commentText = (EditText) dialog.findViewById(R.id.commentText);
            final TextView name = (TextView) dialog.findViewById(R.id.name);
            final ImageView sendButton = (ImageView) dialog.findViewById(R.id.btnSend);
            final LinearLayout llMainLayout = (LinearLayout) dialog.findViewById(R.id.llMainLayout);
            new KeyboardUtil(this, llMainLayout);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            name.setVisibility(View.GONE);
            commentText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    int len = editable.toString().trim().length();
                    if(len>=1){
                        sendButton.setVisibility(View.VISIBLE);
                    }else{
                        sendButton.setVisibility(View.INVISIBLE);
                    }
                }

            });

            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String comment = commentText.getText().toString().trim();
                    if(cc.isConnectingToInternet()){
                        SheredPref.setRun(context,"DONT RUN");
                        CommentBean commentBean = new CommentBean();
                        commentBean.setCommentForId(marketPlaceId);
                        commentBean.setParentComment(0);
                        commentBean.setCommentText(comment);
                        new PostData(new Gson().toJson(commentBean), context, CallFor.ADD_NEW_MARKET_PLACE_COMMENT).execute();
                        tvCommentsCount.setText("Comments : 0");
                        comment_recycler_view.setVisibility(View.GONE);
                        tvNoComment.setVisibility(View.VISIBLE);
                        tvNoComment.setText("Loading...");
                        CleverTap.cleverTap_Record_Event(context, Events.market_place_add_comment);
                    }else {
                        CommonMethods.showToastSuccess(context, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                    }
                    dialog.dismiss();
                    UtilityMethodsAndroid.CloseKeyBoard(context);
                }
            });
            dialog.show();
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
                        dialog.dismiss();
                    return false;
                }
            });
//        }else{
//            Toasty.info(this, "This complaint is closed, You can't add comment", Toast.LENGTH_SHORT, true).show();
//        }
    }

    @Override
    public void onGetResponse(String response, String callFor) {
        LoginResponse loginResponse = null;
        if (callFor.equals(CallFor.LOGIN)) {
            loginResponse = new Gson().fromJson(response, LoginResponse.class);
            try {
                if (loginResponse.getCode().equals("SUCCESS")) {
                    // new_password = NewPassword1 .getText().toString().trim();
                    //  new GetData(context, CallFor.CHANGE_PASSWORD, new_password).execute();
                }
                else{
                    CommonMethods.showToastError(this, "Something went wrong, Please try again");//, Toast.LENGTH_SHORT, true).show();
                }
            }catch (Exception ex)
            {
                Log.e("Ex Setting Details "," "+ex);
            }
        }

        if (callFor.equals(CallFor.GET_MARKET_PLACE_DETAILS)) {
            MarketPlaceResponceBean marketPlaceResponceBean =new Gson().fromJson(response, MarketPlaceResponceBean.class);
            try {
                if (marketPlaceResponceBean.getCode().equals("SUCCESS")) {
                    int size = marketPlaceResponceBean.getDetails().getComments().size();
                    if(size == 0){
                        tvCommentsCount.setText("Comments : 0");
                        comment_recycler_view.setVisibility(View.GONE);
                        tvNoComment.setVisibility(View.VISIBLE);
                    }else{
                        tvCommentsCount.setText("Comments : "+size);
                        comment_recycler_view.setVisibility(View.VISIBLE);
                        tvNoComment.setVisibility(View.GONE);
                    }

                    mAdapter = new Market_place_Comment_RecyclerViewAdapter(context, marketPlaceResponceBean.getDetails().getComments());
                    comment_recycler_view.setAdapter(mAdapter);
                    //Toasty.success(this, complainResponceBean.getMessage(), Toast.LENGTH_SHORT, true).show();
                    //this.finish();
                }
                else{
                    CommonMethods.showToastError(this, marketPlaceResponceBean.getMessage());//, Toast.LENGTH_SHORT, true).show();
                }

            }catch (Exception ex)
            {
                Log.e("Ex","");
            }
        }

        if (callFor.equals(CallFor.ADD_NEW_MARKET_PLACE_COMMENT)) {
            MarketPlaceResponceBean marketPlaceResponceBean =new Gson().fromJson(response, MarketPlaceResponceBean.class);
            try {
                if (marketPlaceResponceBean.getCode().equals("SUCCESS")) {
                    int size = marketPlaceResponceBean.getDetails().getComments().size();
                    tvNoComment.setText(R.string.no_comment);
                    if(size == 0){
                        tvCommentsCount.setText("Comments : 0");
                        comment_recycler_view.setVisibility(View.GONE);
                        tvNoComment.setVisibility(View.VISIBLE);
                    }else{
                        tvCommentsCount.setText("Comments : "+size);
                        comment_recycler_view.setVisibility(View.VISIBLE);
                        tvNoComment.setVisibility(View.GONE);
                    }
                    mAdapter = new Market_place_Comment_RecyclerViewAdapter(context, marketPlaceResponceBean.getDetails().getComments());
                    comment_recycler_view.setAdapter(mAdapter);
                    SheredPref.setRun(context,"RUN");
                    CommonMethods.showToastSuccess(this, "Added Successfully");//, Toast.LENGTH_SHORT, true).show();
                }
                else{
                    CommonMethods.showToastError(this, marketPlaceResponceBean.getMessage());//, Toast.LENGTH_SHORT, true).show();
                }

            }catch (Exception ex)
            {
                Log.e("Ex","");
            }
        }
        if (callFor.equals(CallFor.CREATE_EDIT_CLOSE_ADV)) {
            MarketPlaceResponceBean marketPlaceResponceBean =new Gson().fromJson(response, MarketPlaceResponceBean.class);
            try {
                if (marketPlaceResponceBean.getCode().equals("SUCCESS")) {
                    new GetData(MainActivity.mainActivity, CallFor.GET_ALL_MARKET_PLACE_ADD, "").execute();
                    this.finish();
                    CommonMethods.showToastSuccess(this, "Added Successfully");//, Toast.LENGTH_SHORT, true).show();
                }
                else{
                    CommonMethods.showToastError(this, marketPlaceResponceBean.getMessage());//, Toast.LENGTH_SHORT, true).show();
                }

            }catch (Exception ex)
            {
                Log.e("Ex","");
            }
        }
    }


    public void ShowReasonDialog(final int marketPlaceId){
        final Dialog dialog = new Dialog(this);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_close_market_place_ad);
        final RadioButton radioButton1 = (RadioButton) dialog.findViewById(R.id.radioOption1);
        final RadioButton radioButton2 = (RadioButton) dialog.findViewById(R.id.radioOption2);
        final RadioButton radioButton3 = (RadioButton) dialog.findViewById(R.id.radioOption3);
        final RadioButton radioButton4 = (RadioButton) dialog.findViewById(R.id.radioOption4);
        Button closeButton =  dialog.findViewById(R.id.btn_close_ad);

        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.adCloseReason);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCount = group.getChildCount();
                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId) {
                        selectedValue = btn.getText().toString();

                    }
                }
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cc.isConnectingToInternet()){
                    if(selectedValue != null && !selectedValue.equals("")){
                        MarketplaceBean marketplaceBean = new MarketplaceBean();
                        marketplaceBean.setMarketplaceId(marketPlaceId);
                        marketplaceBean.setAction("close");
                        marketplaceBean.setCloseReasone(selectedValue);
                        new PostData(new Gson().toJson(marketplaceBean), context, CallFor.CREATE_EDIT_CLOSE_ADV).execute();
                        // CleverTap
                        CleverTapAPI cleverTap = null;
                        try {
                            cleverTap = CleverTapAPI.getInstance(context);
                            HashMap<String, Object> marketPlace = new HashMap<String, Object>();
                            marketPlace.put(Events.market_place_operation,"Close");
                            cleverTap.event.push(Events.market_place_ad, marketPlace);
                        } catch (CleverTapMetaDataNotFoundException e) {
                            e.printStackTrace();
                        } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
                            cleverTapPermissionsNotSatisfied.printStackTrace();
                        }
                    }else {
                        CommonMethods.showToastSuccess(context, "Please select option");//, Toast.LENGTH_SHORT, true).show();
                    }
                }else{
                    CommonMethods.showToastSuccess(context, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                }
            }
        });
        dialog.show();
    }

    public void MakeACall(View view) {
        String strMobileNo = tvMobile.getText().toString().trim();
        UtilityMethodsAndroid.makeCall(context, strMobileNo);
    }
}
