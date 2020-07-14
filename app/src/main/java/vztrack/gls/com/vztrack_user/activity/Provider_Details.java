package vztrack.gls.com.vztrack_user.activity;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.javon.viewmanager.controllers.Controller;

import java.util.ArrayList;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.adapters.CommetsList_RecyclerViewAdapter;
import vztrack.gls.com.vztrack_user.beans.DataObjectVisitList;
import vztrack.gls.com.vztrack_user.beans.InputBeanSearchProvider;
import vztrack.gls.com.vztrack_user.beans.OutputBeanSearchProvider;
import vztrack.gls.com.vztrack_user.responce.VisitListResponse;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.DbHelper;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.LoadImage;
import vztrack.gls.com.vztrack_user.utils.PostData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

public class Provider_Details extends BaseActivity {

    ImageView Photo;
    TextView tvVisitorName, tvProvidersMobileNo,tvVisitPurpose;

    String strPhotoUrl;
    String Name;
    String Contact_No;
    String strVisitPurpoese;
    String pos;

    private static Context context;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MaterialRatingBar qualitySocRating,priceSocRating,punctualitySocRating,qualityAllRating,priceAllRating,punctualityAllRating;
    public  static OutputBeanSearchProvider outputBeanSearchProviders; // = new ArrayList<OutputBeanSearchProvider>();
    ArrayList results;
    //private int InternetCheckFlag = 0;
    private VisitListResponse visitListResponse;
    DbHelper dbHelper;
    CheckConnection cc;
    LinearLayout linearLayoutSoc, linearLayoutAll;
    Controller controller;
    private  int duration = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_provider_details);
        getSupportActionBar().hide();
        dbHelper = new DbHelper(this);

        CleverTap.cleverTap_Record_Event(this, Events.event_service_details);

        // Check Internet Connection
        cc = new CheckConnection(getApplicationContext());

        Photo = (ImageView) findViewById(R.id.imgProviderPhoto);
        tvVisitorName = (TextView) findViewById(R.id.tvVisitorName);
        tvProvidersMobileNo = (TextView) findViewById(R.id.tvProvidersMobileNo);
        tvVisitPurpose = (TextView) findViewById(R.id.tvVisitPurpose);

        linearLayoutSoc = (LinearLayout) findViewById(R.id.linearSocRating);
        linearLayoutAll = (LinearLayout) findViewById(R.id.linearAllRating);


        ArrayList<View> views = new ArrayList<>();
        views.add(linearLayoutSoc);
        views.add(linearLayoutAll);

        controller = new Controller(this,views,false,true);
        controller.setBackwardAnimationDuration(duration);
        controller.setForwardAnimationDuration(duration);


        qualitySocRating = (MaterialRatingBar) findViewById(R.id.qualitySocRating);
        priceSocRating = (MaterialRatingBar) findViewById(R.id.priceSocRating);
        punctualitySocRating = (MaterialRatingBar) findViewById(R.id.punctualitySocRating);

        qualityAllRating = (MaterialRatingBar) findViewById(R.id.qualityAllRating);
        priceAllRating = (MaterialRatingBar) findViewById(R.id.priceAllRating);
        punctualityAllRating = (MaterialRatingBar) findViewById(R.id.punctualityAllRating);


        strPhotoUrl = getIntent().getStringExtra("VISITOR_PHOTO");
        Name = getIntent().getStringExtra("VISITOR_NAME");
        Contact_No = getIntent().getStringExtra("MOBILE_NO");
        strVisitPurpoese = getIntent().getStringExtra("VISIT_PURPOSE");

        tvVisitorName.setText(Name);
        tvProvidersMobileNo.setText(Contact_No);
        tvVisitPurpose.setText(strVisitPurpoese);

        new LoadImage().loadImage(this, R.drawable.no_photo_icon, strPhotoUrl, Photo, null);
        if (cc.isConnectingToInternet()) {
            InputBeanSearchProvider inputBeanSearchProvider = new InputBeanSearchProvider();
            inputBeanSearchProvider.setSocietyId(Integer.parseInt(SheredPref.getSocietyId(this)));
            inputBeanSearchProvider.setServiceProvider(strVisitPurpoese);
            inputBeanSearchProvider.setVisitorMobile(Contact_No);
            new PostData(new Gson().toJson(inputBeanSearchProvider), Provider_Details.this, CallFor.PROVIDER_DEATILS_DATA).execute();
        }
    }

    @Override
    public void onBackPressed() {
        MainActivity.backPressFlag = 0;
        finish();
    }

    public void ShowPhoto(View v) {
        final Dialog settingsDialog = new Dialog(this, R.style.DialogTheme);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.getWindow().getAttributes().windowAnimations = R.style.animationName;
        View showData = getLayoutInflater().inflate(R.layout.image_layout_for_visitors, null);
        final TextView ShowUserName = (TextView) showData.findViewById(R.id.tvNameAndNumber);
        final ImageView imageView = (ImageView) showData.findViewById(R.id.imgImage);

        ShowUserName.setText(Name);

        if (cc.isConnectingToInternet()) {
            new LoadImage().loadImage(this, R.drawable.no_photo_icon, strPhotoUrl, imageView, null);
        } else {
            new LoadImage().loadImage(this, R.drawable.no_photo_icon, strPhotoUrl, imageView, null);
        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                settingsDialog.dismiss();

            }
        });

        ShowUserName.setText(Name);
        settingsDialog.setContentView(showData);
        settingsDialog.setCanceledOnTouchOutside(false);
        settingsDialog.show();
    }

    public void Call(View v) {
        String Mobile_no = Contact_No;
        Uri number = Uri.parse("tel:" + Mobile_no);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
        CleverTap.cleverTap_Record_Event(this, Events.event_service_call);
    }

    public void Next(View view) {
        controller.next();
    }

    public void Share(View v){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Service Provider Details"+"\n----------------------------------"+"\nName : "+Name+"\nContact Number : "+Contact_No+"\nService Type : "+strVisitPurpoese +"\n\nBy - VZTrack";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "VZTrack");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    public void onGetResponse(String response, String callFor) {
        if (cc.isConnectingToInternet()) {

            outputBeanSearchProviders=new Gson().fromJson(response,OutputBeanSearchProvider.class);

            qualitySocRating.setRating(outputBeanSearchProviders.getSocietyQualityRating());
            priceSocRating.setRating(outputBeanSearchProviders.getSocietyPriceRating());
            punctualitySocRating.setRating(outputBeanSearchProviders.getSocietyPunctualityRating());

            qualityAllRating.setRating(outputBeanSearchProviders.getOverAllQualityRating());
            priceAllRating.setRating(outputBeanSearchProviders.getOverAllPriceRating());
            punctualityAllRating.setRating(outputBeanSearchProviders.getOverAllPunctualityRating());


            mRecyclerView = (RecyclerView) findViewById(R.id.commetsList_recycler_view);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(context);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mAdapter = new CommetsList_RecyclerViewAdapter(context, outputBeanSearchProviders);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setOnClickListener(null);
        }


    }

    private ArrayList<DataObjectVisitList> getDataSet() {

        results = new ArrayList<DataObjectVisitList>();

        if (cc.isConnectingToInternet()) {
            for (int index = 0; index < visitListResponse.getVisitors().size(); index++) {
                DataObjectVisitList obj = new DataObjectVisitList(
                        visitListResponse.getVisitors().get(index).getFirst_name(),
                        visitListResponse.getVisitors().get(index).getLast_name(),
                        visitListResponse.getVisitors().get(index).getIn_time(),
                        visitListResponse.getVisitors().get(index).getOut_time(),
                        visitListResponse.getVisitors().get(index).getVisit_purpose(),
                        visitListResponse.getVisitors().get(index).getFrom(),
                        visitListResponse.getVisitors().get(index).getWhomeToVisit(),
                        visitListResponse.getVisitors().get(index).getVehicle_no(),
                        visitListResponse.getVisitors().get(index).getNo_of_visitors(),
                        visitListResponse.getVisitors().get(index).getWhomeToVisit(),
                        visitListResponse.getVisitors().get(index).isError_flag(),
                        visitListResponse.getVisitors().get(index).getBadgeNumber()
                        ,
                        visitListResponse.getVisitors().get(index).getApprovalCount()

                );
                results.add(index, obj);
            }
            return results;
        } else {
            Cursor rs = dbHelper.getDataFromVisitorsTableById(String.valueOf(pos));
            rs.moveToFirst();
            String first_name = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_FIRST_NAME));
            String last_name = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_LAST_NAME));
            String date_and_time = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_DATE));
            String out_time = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_OutTime));
            String purpose = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_PURPOSE));
            String from = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_FROM));
            String whome_to_see = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_TOMEET));
            String Vehicle_no = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_VEHICLE_NO));
            String no_of_visitors = rs.getString(rs.getColumnIndex(dbHelper.VISITORS_VISITORS_NO));
            boolean isError = Boolean.parseBoolean(rs.getString(rs.getColumnIndex(dbHelper.ERROR_ENTRY)));
            String badgeNo = rs.getString(rs.getColumnIndex(dbHelper.ERROR_ENTRY));
            String approvalcount=rs.getString(rs.getColumnIndex(dbHelper.APPROVALCOUNT));
            int approvalcountint=Integer.parseInt(approvalcount);

            DataObjectVisitList obj = new DataObjectVisitList(
                    first_name, last_name, date_and_time, out_time , purpose, from, whome_to_see, Vehicle_no, no_of_visitors, whome_to_see, isError, badgeNo,approvalcountint
            );
            results.add(0, obj);
            return results;
        }
    }
}
