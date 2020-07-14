package vztrack.gls.com.vztrack_user.activity;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.beans.NoticeBean;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.LoadImage;
import vztrack.gls.com.vztrack_user.utils.PostDataForFile;

public class Notice_DetailsActivity extends BaseActivity {


    CheckConnection cc;
    String title, description, fileType, noticeId;
    String URLPhoto;
    private final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 1;
    TextView titleactivity;
    ImageView backpress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice__details);
        titleactivity=findViewById(R.id.title);
        backpress=findViewById(R.id.backpress);
        titleactivity.setText("Notice Details");
        backpress.setOnClickListener(v -> onBackPressed());
        CleverTap.cleverTap_Record_Event(this, Events.event_notice_details);
        cc = new CheckConnection(this);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        setTitle("Notice Details");
        CreateFolderIfNotExist();

        TextView noticeTitle = (TextView) findViewById(R.id.tvNoticeTitle);
        TextView tvDescription = (TextView) findViewById(R.id.tvDescription);
        TextView text = (TextView) findViewById(R.id.tvText);
        ImageView noticePhoto = (ImageView) findViewById(R.id.tvNoticePhoto);
        ImageView imgFileType = (ImageView) findViewById(R.id.imgFileType);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.descLayout);
        View view = (View) findViewById(R.id.view);
        title = getIntent().getStringExtra("TITLE");
        description = getIntent().getStringExtra("DESCRIPTION");
        fileType = getIntent().getStringExtra("FILE_TYPE");
        noticeId = getIntent().getStringExtra("NOTICE_ID");
        noticeTitle.setText(title);

        if(description == null || description.equals("")){
            linearLayout.setVisibility(View.GONE);
            view.setVisibility(View.GONE);

        }
        tvDescription.setText(description);

        tvDescription.setMovementMethod(new ScrollingMovementMethod());
        tvDescription.setMovementMethod(LinkMovementMethod.getInstance());

        URLPhoto = getIntent().getStringExtra("PHOTO");

        if(fileType.equalsIgnoreCase(".png") || fileType.equalsIgnoreCase(".jpg") || fileType.equalsIgnoreCase(".jpeg")){
            noticePhoto.setVisibility(View.VISIBLE);
            imgFileType.setVisibility(View.GONE);
            text.setVisibility(View.GONE);
            tvDescription.setMaxLines(5);
            new LoadImage().loadImage(this, R.drawable.no_photo_icon, URLPhoto ,noticePhoto, null);
        }else if(fileType == null || fileType.equals("")){
            //noticePhoto.setVisibility(View.GONE);
            noticePhoto.setBackgroundResource(R.drawable.text_icon);
            imgFileType.setVisibility(View.GONE);
            text.setVisibility(View.GONE);
            tvDescription.setMaxLines(15);
        }else{
            noticePhoto.setVisibility(View.GONE);
            imgFileType.setVisibility(View.VISIBLE);
            text.setVisibility(View.VISIBLE);
            if(fileType.equalsIgnoreCase(".ppt") || fileType.equalsIgnoreCase(".pptx")){
                imgFileType.setImageResource(R.drawable.ppt);
            }else if(fileType.equalsIgnoreCase(".pdf")){
                imgFileType.setImageResource(R.drawable.pdf);
            }else if(fileType.equalsIgnoreCase(".doc") || fileType.equalsIgnoreCase(".docx")){
                imgFileType.setImageResource(R.drawable.doc);
            }else if(fileType.equalsIgnoreCase(".xls") || fileType.equalsIgnoreCase(".xlsx")){
                imgFileType.setImageResource(R.drawable.xls);
            }else if(fileType.equalsIgnoreCase(".txt")){
                imgFileType.setImageResource(R.drawable.txt);
            }
            else{
                imgFileType.setImageResource(R.drawable.no_photo_icon);
            }
        }
        cc = new CheckConnection(this);
    }

    @Override
    public void onBackPressed() {
        MainActivity.backPressFlag=0;
        MainActivity.redirectToScreen="2";
        finish();
    }

    public void DownLoadFile(View v)
    {
        if(cc.isConnectingToInternet()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                askPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_READ_STORAGE);
            } else {
                getFile();
            }
        }else{
            CommonMethods.showToastError(this,"Please Check Internet Connection");//.show();
        }
    }

    public void getFile()
    {
        NoticeBean noticeBean = new NoticeBean();
        noticeBean.setNoticeId(Integer.parseInt(noticeId));
        new PostDataForFile(noticeBean, this, CallFor.DOWNLOAD_NOTICE_FILE).execute();
    }

    public void askPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            // We dont have permission
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        } else {
            getFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getFile();
                }
                else{
                    CommonMethods.showToastSuccess(this,"To Download Notice Attachment Please Allow The Permission");//.show();
                }
            }
        }
    }

    public  void CreateFolderIfNotExist(){
        try{
            File folder = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "VZ/");
            if (!folder.exists()) {
                folder.mkdirs();
            }
        }catch (Exception ex){
            Log.e("Exception "," "+ex);
        }
    }

    @Override
    public void onGetResponse(String response, String callFor) {

    }
}
