package vztrack.gls.com.vztrack_user.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.google.gson.Gson;

import java.util.HashMap;


import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.beans.ResponceBean;
//import vztrack.gls.com.vztrack_user.fcm.FirebaseMessagServiceForUser;
import vztrack.gls.com.vztrack_user.fcm.FirebaseMessagService;
import vztrack.gls.com.vztrack_user.responce.RatingResponseBean;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Constants;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.LoadImage;
import vztrack.gls.com.vztrack_user.utils.PostData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

public class RatingActivity extends BaseActivity {

    private TextView tvVisitorName,tvMobile, tvPurpose, tvDate;
    private ImageView imgVisitorPhoto;
    private ImageView img_qua_happy, img_qua_sad,img_pri_happy,img_pri_sad,img_pun_happy,img_pun_sad;
    private MaterialRatingBar qualityRating,priceRating,punctualityRating;
    private String strVisitrName,strVisitrPhoto,strVisitPurpose,strVisitTime,strVisitrMobileNo,strFrom;
    private int quality,price,punctuality;
    private  EditText etComment;
    private  CheckConnection cc;
    RatingResponseBean ratingResponseBean;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rating);
//        getSupportActionBar().setTitle("Feedback");
        TextView title=findViewById(R.id.title);
        title.setText("Feedback");

        ImageView backpress=findViewById(R.id.backpress);
        backpress.setOnClickListener(v->onBackPressed());
        cc = new CheckConnection(this);

        CleverTap.cleverTap_Record_Event(this,Events.event_rating_details);

        strVisitrPhoto = getIntent().getStringExtra("VISITR_PHOTO");
        strVisitrName = getIntent().getStringExtra("VISITOR_NAME");
        strVisitrMobileNo = getIntent().getStringExtra("MOBILE_NO");
        strVisitPurpose = getIntent().getStringExtra("VISIT_PURPOSE");
        strVisitTime = getIntent().getStringExtra("IN_TIME");
        strFrom = getIntent().getStringExtra("FROM");

        try{
            if(strFrom.equals("Notification")){
//                FirebaseMessagServiceForUser.cnt_pro=0;
                FirebaseMessagService.cnt_pro=0;
                CleverTap.cleverTap_Record_Event(this, Events.event_rating_details);
            }
        }catch (Exception ex){
            Log.e("Exception "," "+ex);
        }


        tvVisitorName = (TextView) findViewById(R.id.tvVisitorName);
        tvMobile = (TextView) findViewById(R.id.tvMobileNo);
        tvPurpose = (TextView) findViewById(R.id.tvPurpose);
        tvDate = (TextView) findViewById(R.id.tvDate);
        etComment = (EditText) findViewById(R.id.etComment);
        imgVisitorPhoto = (ImageView) findViewById(R.id.imgVisitorPhoto);

        img_qua_happy = (ImageView) findViewById(R.id.img_qua_happy);
        img_qua_sad = (ImageView) findViewById(R.id.img_qua_sad);
        img_pri_happy = (ImageView) findViewById(R.id.img_pri_happy);
        img_pri_sad = (ImageView) findViewById(R.id.img_pri_sad);
        img_pun_happy = (ImageView) findViewById(R.id.img_pun_happy);
        img_pun_sad = (ImageView) findViewById(R.id.img_pun_sad);

        tvPurpose.setSelected(true);  // Set focus to the textview
        tvDate.setSelected(true);  // Set focus to the textview

        qualityRating = (MaterialRatingBar) findViewById(R.id.qualityRating);
        priceRating = (MaterialRatingBar) findViewById(R.id.priceRating);
        punctualityRating = (MaterialRatingBar) findViewById(R.id.punctualityRating);

        quality = (int) qualityRating.getRating();
        price = (int) priceRating.getRating();
        punctuality = (int) punctualityRating.getRating();

        qualityRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                quality = (int) rating;
                if(quality<3){
                    img_qua_happy.setVisibility(View.GONE);
                    img_qua_sad.setVisibility(View.VISIBLE);
                }
                else{
                    img_qua_happy.setVisibility(View.VISIBLE);
                    img_qua_sad.setVisibility(View.GONE);
                }

            }
        });


        priceRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                price = (int) rating;
                if(price<3){
                    img_pri_happy.setVisibility(View.GONE);
                    img_pri_sad.setVisibility(View.VISIBLE);
                }
                else{
                    img_pri_happy.setVisibility(View.VISIBLE);
                    img_pri_sad.setVisibility(View.GONE);
                }

            }
        });


        punctualityRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                punctuality = (int) rating;
                if(punctuality<3){
                    img_pun_happy.setVisibility(View.GONE);
                    img_pun_sad.setVisibility(View.VISIBLE);
                }
                else{
                    img_pun_happy.setVisibility(View.VISIBLE);
                    img_pun_sad.setVisibility(View.GONE);
                }

            }
        });

        tvVisitorName.setText(strVisitrName);
        tvMobile.setText(strVisitrMobileNo);
        tvPurpose.setText(strVisitPurpose);
        tvDate.setText(strVisitTime);
        MainActivity.RATING_FLAG="1";

        if(cc.isConnectingToInternet())
        {
            String photo = Constants.BASE_IMG_URL+"/"+strVisitrPhoto;
            new LoadImage().loadImage(this, R.drawable.no_photo_icon, photo ,  imgVisitorPhoto, null);
        }
    }

    public void Submit(View v)
    {
        if(quality==0){
            CommonMethods.showToastError(this, "Please select quality rating");//, Toast.LENGTH_SHORT, true).show();
            //Toast.makeText(this,"Please Select Quality",Toast.LENGTH_SHORT).show();
        }
        else if(price==0){
            CommonMethods.showToastError(this, "Please select price rating");//, Toast.LENGTH_SHORT, true).show();
            //Toast.makeText(this,"Please Select Price",Toast.LENGTH_SHORT).show();
        }else if(punctuality==0){
            CommonMethods.showToastError(this, "Please select punctuality rating");//, Toast.LENGTH_SHORT, true).show();
            //Toast.makeText(this,"Please Select Punctuality",Toast.LENGTH_SHORT).show();
        }else{
            ratingResponseBean = new RatingResponseBean();
            ratingResponseBean.setSocietyId(Integer.parseInt(SheredPref.getSocietyId(this)));
            ratingResponseBean.setFamilyId(Integer.parseInt(SheredPref.getFamilyId(this)));
            ratingResponseBean.setFlatNumber(SheredPref.getFlat_No(this));
            ratingResponseBean.setVisitorName(strVisitrName);
            ratingResponseBean.setVisitorMobile(strVisitrMobileNo);
            ratingResponseBean.setVisitorPurpose(strVisitPurpose);
            ratingResponseBean.setVisitorPhoto(strVisitrPhoto);
            ratingResponseBean.setComments(etComment.getText().toString().trim());
            ratingResponseBean.setQualityRating(quality);
            ratingResponseBean.setPriceRating(price);
            ratingResponseBean.setPunctualityRating(punctuality);
            new PostData(new Gson().toJson(ratingResponseBean), RatingActivity.this, CallFor.SAVE_RATING).execute();
        }
    }
    public void NotInterested(View v)
    {
        ratingResponseBean = new RatingResponseBean();
        ratingResponseBean.setSocietyId(Integer.parseInt(SheredPref.getSocietyId(this)));
        ratingResponseBean.setFamilyId(Integer.parseInt(SheredPref.getFamilyId(this)));
        ratingResponseBean.setVisitorMobile(strVisitrMobileNo);
        ratingResponseBean.setVisitorPurpose(strVisitPurpose);
        new PostData(new Gson().toJson(ratingResponseBean), RatingActivity.this, CallFor.CANCLE_RATING).execute();
    }
    public void Call(View v) {
        String Mobile_no = tvMobile.getText().toString();
        Uri number = Uri.parse("tel:" + Mobile_no);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }

    @Override
    public void onBackPressed() {

        MainActivity.backPressFlag = 3;
        finish();

    }

    @Override
    public void onGetResponse(String response, String callFor) {
        if (response == null) {
            return;
        }
        if (callFor.equals(CallFor.CANCLE_RATING)) {
            // CleverTap
            CleverTapAPI cleverTap = null;
            try {
                cleverTap = CleverTapAPI.getInstance(this);
                HashMap<String, Object> action = new HashMap<String, Object>();
                action.put(Events.event_key_action, "Not Interested");
                cleverTap.event.push(Events.event_rating_action, action);
            } catch (CleverTapMetaDataNotFoundException e) {
                e.printStackTrace();
            } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
                cleverTapPermissionsNotSatisfied.printStackTrace();
            }

            ResponceBean responceBean = new Gson().fromJson(response,ResponceBean.class);
            if(responceBean.getCode().equals("SUCCESS")){
                //Toast.makeText(this,"Thank You !",Toast.LENGTH_SHORT).show();
                CommonMethods.showToastError(this, "Thank you");//, Toast.LENGTH_SHORT, true).show();
                Intent I = new Intent(this, MainActivity.class);
                I.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(I);
                this.finish();
            }
            else{
                CommonMethods.showToastError(this, "Please try again");//, Toast.LENGTH_SHORT, true).show();
                //Toast.makeText(this,"Please Try Again !",Toast.LENGTH_SHORT).show();
            }
        }


        if (callFor.equals(CallFor.SAVE_RATING)) {
            // CleverTap
            CleverTapAPI cleverTap = null;
            try {
                cleverTap = CleverTapAPI.getInstance(this);
                HashMap<String, Object> action = new HashMap<String, Object>();
                action.put(Events.event_key_action, "Submited");
                cleverTap.event.push(Events.event_rating_action, action);
            } catch (CleverTapMetaDataNotFoundException e) {
                e.printStackTrace();
            } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
                cleverTapPermissionsNotSatisfied.printStackTrace();
            }


            ResponceBean responceBean = new Gson().fromJson(response,ResponceBean.class);
            if(responceBean.getCode().equals("SUCCESS")){
                etComment.setText("");

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.custum_alert_dialog, null);
                dialogBuilder.setView(dialogView);
                TextView txtalertheading = dialogView.findViewById(R.id.txtalertheading);
                TextView txtalertsubheading = dialogView.findViewById(R.id.txtalertsubheading);

                TextView btnegative = dialogView.findViewById(R.id.btnegative);
                TextView btnpositive = dialogView.findViewById(R.id.btnpositive);

                txtalertheading.setText("Thanks For Your Feedback");
                txtalertsubheading.setVisibility(View.GONE);

                btnegative.setVisibility(View.GONE);


                final AlertDialog b = dialogBuilder.create();
                b.setCanceledOnTouchOutside(false);
                b.setCancelable(false);
                b.show();

                btnpositive.setOnClickListener(v -> {
                    etComment.setText("");
                    priceRating.setRating(0);
                    qualityRating.setRating(0);
                    punctualityRating.setRating(0);
                    Intent I = new Intent(RatingActivity.this, MainActivity.class);
                    I.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(I);
                    RatingActivity.this.finish();
                    b.dismiss();

                });
                btnegative.setOnClickListener(v -> b.dismiss());

//                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
//                        .setTitleText("Thanks For Your Feedback")
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sDialog) {
//                                etComment.setText("");
//                                priceRating.setRating(0);
//                                qualityRating.setRating(0);
//                                punctualityRating.setRating(0);
//                                Intent I = new Intent(RatingActivity.this, MainActivity.class);
//                                I.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(I);
//                                RatingActivity.this.finish();
//                            }
//                        })
//                        .show();
            }
            else{

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.custum_alert_dialog, null);
                dialogBuilder.setView(dialogView);
                TextView txtalertheading = dialogView.findViewById(R.id.txtalertheading);
                TextView txtalertsubheading = dialogView.findViewById(R.id.txtalertsubheading);

                TextView btnegative = dialogView.findViewById(R.id.btnegative);
                TextView btnpositive = dialogView.findViewById(R.id.btnpositive);

                txtalertheading.setText("Unable To Submit Rating");
                txtalertsubheading.setText("Your family member already submitted rating");

                btnegative.setVisibility(View.GONE);


                final AlertDialog b = dialogBuilder.create();
                b.setCanceledOnTouchOutside(false);
                b.setCancelable(false);
                b.show();

                btnpositive.setOnClickListener(v -> {

                    b.dismiss();

                });
                btnegative.setOnClickListener(v -> b.dismiss());

//                new SweetAlertDialog(this)
//                        .setTitleText("Unable To Submit Rating")
//                        .setContentText("Your family member already submitted rating")
//                        .show();
            }
        }
    }
}
