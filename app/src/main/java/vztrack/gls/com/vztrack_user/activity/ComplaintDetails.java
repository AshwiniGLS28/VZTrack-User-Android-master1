package vztrack.gls.com.vztrack_user.activity;

/**
 * Created by sandeep on 14/3/16.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.adapters.Comment_RecyclerViewAdapter;
import vztrack.gls.com.vztrack_user.beans.CommentBean;
import vztrack.gls.com.vztrack_user.beans.ComplainsBean;
import vztrack.gls.com.vztrack_user.beans.ReopenComplainBean;
import vztrack.gls.com.vztrack_user.responce.ComplainResponceBean;
import vztrack.gls.com.vztrack_user.responce.LoginResponse;
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
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;

public class ComplaintDetails extends BaseActivity implements DatePickerDialog.OnDateSetListener {
    BaseActivity context;
    CheckConnection cc;
    boolean type;
    CoordinatorLayout coordinatorLayout;
    LinearLayout llMainLinearLayout,llreopeneddate,llreopenedcomment;
    CardView card_view;
    TextView tvCategory, tvDescription, tvClosedDate, btnInProgress, btnClose, tvNoComment, tvCommentCount,btnreopen;
    TextView txtreopeneddate,txtreopenedreason;
    LinearLayout dateLayout;
    public ComplainResponceBean complainResponceBean  = null;
    String[] months;
    String strDate;
    RecyclerView comment_recycler_view;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    boolean adminAccess;
    String strStatus;  TextView titleactivity;
    ImageView backpress,img_complaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_details);
        titleactivity=findViewById(R.id.title);
        backpress=findViewById(R.id.backpress);
        txtreopeneddate=findViewById(R.id.txtreopeneddate);

        titleactivity.setText("Complaint");
        llreopeneddate=findViewById(R.id.llreopeneddate);
        llreopenedcomment=findViewById(R.id.llreopenedcomment);
        backpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        context = ComplaintDetails.this;
        cc = new CheckConnection(context);
        adminAccess = SheredPref.getAdminAccess(this);
        complainResponceBean = MainActivity.complainResponceBean;
        img_complaint=findViewById(R.id.img_complaint);
        txtreopenedreason=findViewById(R.id.txtreopenedreason);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        llMainLinearLayout = (LinearLayout) findViewById(R.id.llMainLinearLayout);
        card_view = (CardView) findViewById(R.id.card_view);
        tvCategory = (TextView) findViewById(R.id.tvCategory);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvClosedDate = (TextView) findViewById(R.id.tvClosedDate);
        btnInProgress = (TextView) findViewById(R.id.btnInProgress);
        btnreopen=findViewById(R.id.btnreopen);
        tvNoComment = (TextView) findViewById(R.id.tvNoComment);
        btnClose = (TextView) findViewById(R.id.btnClose);
        tvCommentCount = (TextView) findViewById(R.id.tvCommentCount);
        comment_recycler_view = (RecyclerView) findViewById(R.id.comment_recycler_view);
        dateLayout = (LinearLayout) findViewById(R.id.dateLayout);

        months = getResources().getStringArray(R.array.months);

        tvCategory.setText(complainResponceBean.getComplainDetails().getCategory());
        tvDescription.setText(complainResponceBean.getComplainDetails().getDescription().replaceAll("\n+", "\n"));
        Log.e("close date",complainResponceBean.getComplainDetails().getEstimate_date()+"--");
        tvClosedDate.setVisibility(View.VISIBLE);
        tvClosedDate.setText(complainResponceBean.getComplainDetails().getEstimate_date());

        strStatus = complainResponceBean.getComplainDetails().getStatus();
        if (complainResponceBean.getComplainDetails().getReOpenComment()!=null) {
            llreopenedcomment.setVisibility(View.VISIBLE);
            txtreopenedreason.setText(complainResponceBean.getComplainDetails().getReOpenComment());
        }else
        {
            llreopenedcomment.setVisibility(View.GONE);
        }

        if (complainResponceBean.getComplainDetails().getReopenDate()!=null) {
            llreopeneddate.setVisibility(View.VISIBLE);
            txtreopeneddate.setText(complainResponceBean.getComplainDetails().getReopenDate());
        }else
        {
            llreopeneddate.setVisibility(View.GONE);
        }

        if (complainResponceBean.getComplainDetails().getComplainPhoto()!=null) {
            Log.e("complainResponceBean",complainResponceBean.getComplainDetails().getComplainPhoto()+"--");
            if (!complainResponceBean.getComplainDetails().getComplainPhoto().equalsIgnoreCase("not added")) {
               String imgurl= Constants.BASE_IMG_URL+"/"+ complainResponceBean.getComplainDetails().getComplainPhoto();
               Log.e("complaintimgurl",imgurl+"--");

                img_complaint.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(imgurl)
                        .placeholder(R.drawable.no_photo_icon)
                        .error(R.drawable.no_photo_icon)
                        .skipMemoryCache(false)
                        .thumbnail(0.4f)
                        .into(img_complaint);
//                new LoadImage().loadImage(context, R.drawable.ic_avatar,imgurl, img_complaint, null);
            }else
            {
                img_complaint.setVisibility(View.GONE);
            }
        }else
        {
            img_complaint.setVisibility(View.VISIBLE);
        }

        if(adminAccess){
            btnInProgress.setVisibility(View.VISIBLE);
        }else{
            btnInProgress.setVisibility(View.GONE);
        }


        String estimatedDate = complainResponceBean.getComplainDetails().getEstimate_date();
        Log.e("strStatus",strStatus+"-");

        if(strStatus.equalsIgnoreCase("close") && (estimatedDate == null || estimatedDate.equals("")) && adminAccess){
            dateLayout.setVisibility(View.GONE);
            btnreopen.setVisibility(View.GONE);
//            llreopened.setVisibility(View.GONE);
        }

        if((strStatus.equalsIgnoreCase("open") || strStatus.equalsIgnoreCase("close")) && (estimatedDate == null || estimatedDate.equals("")) && !adminAccess){
            dateLayout.setVisibility(View.GONE);
            btnreopen.setVisibility(View.GONE);
//            llreopened.setVisibility(View.GONE);
        }

        if(strStatus.equalsIgnoreCase("Close")){
            btnInProgress.setVisibility(View.GONE);
            btnClose.setVisibility(View.GONE);
            btnreopen.setVisibility(View.VISIBLE);
//            llreopened.setVisibility(View.GONE);
        }
        else if (strStatus.equalsIgnoreCase("reopen"))
//            llreopened.setVisibility(View.VISIBLE);

        tvClosedDate.setVisibility(View.VISIBLE);
        if(complainResponceBean.getComplainDetails().getComments().size()==0){
            tvNoComment.setVisibility(View.VISIBLE);
            comment_recycler_view.setVisibility(View.GONE);
        }

        tvClosedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    showDatePicker();
            }
        });
        btnreopen.setTag(complainResponceBean);

        btnreopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComplainResponceBean complainResponceBean= (ComplainResponceBean) v.getTag();
                ShowMessageboxToComplaintReopen(complainResponceBean);
            }
        });

        btnInProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = tvClosedDate.getText().toString().trim();
                if(date == null || date.equals("")){
//                    Toasty.info(context, "Please select estimated date", Toast.LENGTH_SHORT, true).show();
                    CommonMethods.showToastSuccess(context, "Please select estimated date");
                }else{
                    if(cc.isConnectingToInternet()){
                        try{
                            String strSelectedDate = tvClosedDate.getText().toString().trim();
                            Calendar now = Calendar.getInstance();
                            int year = now.get(Calendar.YEAR);
                            int month =now.get(Calendar.MONTH);
                            int day =now.get(Calendar.DAY_OF_MONTH);
                            String strTodayDate = day+" "+months[month]+" "+year;
                            int hh = now.get(Calendar.HOUR);
                            int mm =now.get(Calendar.MINUTE);
                            int ss =now.get(Calendar.SECOND);
                            int compareFlag = UtilityMethods.compareTwoDates(strSelectedDate, strTodayDate, "dd MMM yyyy", "dd MMM yyyy");
                            if(compareFlag==-1){
                                CommonMethods.showToastSuccess(context, "Estimated date past away, Please select another estimate date");
//                                Toasty.info(context, "Estimated date past away, Please select another estimate date", Toast.LENGTH_SHORT, true).show();
                            }else{
                                strSelectedDate = strSelectedDate+" "+hh+":"+mm+":"+ss;
                                strSelectedDate = UtilityMethods.ChangeDateFormat(strSelectedDate,"dd MMM yyyy HH:mm:ss", "yyyy-MM-dd HH:mm:ss");
                                int complain_id = complainResponceBean.getComplainDetails().getVz_comp_id();
                                CleverTap.cleverTap_Record_Event(context, Events.event_in_progress_complaint_button);
                                ComplainsBean complainsBean = new ComplainsBean();
                                complainsBean.setVz_comp_id(complain_id);
                                complainsBean.setStatus("In Progress");
                                complainsBean.setEstimate_date(strSelectedDate);
                                complainsBean.setComment("");
                                complainsBean.setNewVersion(true);
                                new PostData(new Gson().toJson(complainsBean), context, CallFor.CHANGE_COMPLAINT_STATUS).execute();
                            }
                        }catch(Exception e){
                            Log.e("Exception ",""+e);
                        }

                    }else{
                        CommonMethods.showToastSuccess(context, "Please check internet connection");
//                            Toasty.info(context, "Please check internet connection", Toast.LENGTH_SHORT, true).show();
                            }
                    }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openCommentDialog(complainResponceBean.getComplainDetails().getVz_comp_id());
                ShowBottomSheetDialogToCloseComplaint(complainResponceBean.getComplainDetails().getVz_comp_id());
            }
        });

        tvCommentCount.setText(getResources().getString(R.string.comment)+" "+complainResponceBean.getComplainDetails().getComments().size());
        comment_recycler_view.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        comment_recycler_view.setLayoutManager(mLayoutManager);
        mAdapter = new Comment_RecyclerViewAdapter(context, complainResponceBean.getComplainDetails().getComments());
        comment_recycler_view.setAdapter(mAdapter);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    private void callForComplaintReopenAPI(ComplainResponceBean complainResponceBean, String comment) {

        if (complainResponceBean!=null) {
            ReopenComplainBean reopenComplainBean = new ReopenComplainBean();
            reopenComplainBean.setNewVersion(true);
            reopenComplainBean.setReopenById(complainResponceBean.getComplainDetails().getFamily_id());
            reopenComplainBean.setReOpenComment(comment);
            reopenComplainBean.setReopenName(SheredPref.getUsername(getApplicationContext()));
            reopenComplainBean.setVz_comp_id(complainResponceBean.getComplainDetails().getVz_comp_id());
            new PostData(new Gson().toJson(reopenComplainBean), context, CallFor.REOPENCOMPLAINT).execute();
        }
    }


    public void ShowBottomSheetDialogToCloseComplaint(final int complain_id){
        if (cc.isConnectingToInternet()) {
            // Create custom dialog object
            final BottomSheetDialog dialog = new BottomSheetDialog(this);
            dialog.setCancelable(true);
            // Include dialog.xml file
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_add_complain_commnet);
            final LinearLayout mainLinearLayout = (LinearLayout) dialog.findViewById(R.id.mainLinearLayout);
            final EditText description = (EditText) dialog.findViewById(R.id.inputCommnet);
            final ImageView fancyButton =  dialog.findViewById(R.id.btn_add_Complemt_commnet);
            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            new KeyboardUtil(this, mainLinearLayout);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            dialog.show();
            description.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    int len = editable.length();
                    if(len>=1){
                        fancyButton.setVisibility(View.VISIBLE);
                    }else{
                        fancyButton.setVisibility(View.INVISIBLE);
                    }
                }
            });

            fancyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String commentText = description.getText().toString().trim();
                    if(commentText.equals("")){
                        CommonMethods.showToastError(context, "Comment/Remark should not be blank");
//                        Toasty.error(context, "Comment/Remark should not be blank", Toast.LENGTH_SHORT, true).show();
                    }else{
                        if(cc.isConnectingToInternet()){
                            CleverTap.cleverTap_Record_Event(context, Events.event_close_complaint_button);
                            ComplainsBean complainsBean = new ComplainsBean();
                            complainsBean.setVz_comp_id(complain_id);
                            complainsBean.setStatus("Close");
                            complainsBean.setComment(commentText);
                            complainsBean.setNewVersion(true);
                            new PostData(new Gson().toJson(complainsBean), context, CallFor.CHANGE_COMPLAINT_STATUS).execute();
                            dialog.dismiss();
                            CloseKeyBoard();
                        }else {
                            CommonMethods.showToastSuccess(context, "Please check internet connection");
//                            Toasty.info(context, "Please check internet connection", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                }
            });


            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
                        dialog.dismiss();
                    return false;
                }
            });
        }else{
//            Toasty.info(context, "Please check internet connection", Toast.LENGTH_SHORT, true).show();
            CommonMethods.showToastSuccess(context, "Please check internet connection");
        }
    }

    public void ShowBottomSheetDialog(View v){
        if(!strStatus.equalsIgnoreCase("Close")){
            final BottomSheetDialog dialog = new BottomSheetDialog(this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.write_comment_layout);
            final EditText commentText = (EditText) dialog.findViewById(R.id.commentText);
            final TextView name = (TextView) dialog.findViewById(R.id.name);
            final ImageView sendButton = (ImageView) dialog.findViewById(R.id.btnSend);
            final LinearLayout llMainLayout = (LinearLayout) dialog.findViewById(R.id.llMainLayout);
            new KeyboardUtil(this, llMainLayout);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
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
                    CommentBean commentBean = new CommentBean();
                    commentBean.setCommentForId(complainResponceBean.getComplainDetails().getVz_comp_id());
                    commentBean.setCommentText(comment);
                    new PostData(new Gson().toJson(commentBean), context, CallFor.SAVE_COMPLAINT_COMMENT).execute();
                    dialog.dismiss();
                    CloseKeyBoard();
                }
            });


            if(adminAccess){
                name.setText("A");
            }else{
                name.setText("U");
            }
            dialog.show();
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
                        dialog.dismiss();
                    return false;
                }
            });
        }else{
            CommonMethods.showToastSuccess(context, "This complaint is closed, You can't add comment");
//            Toasty.info(this, "This complaint is closed, You can't add comment", Toast.LENGTH_SHORT, true).show();
        }
    }

    public void ShowMessageboxToComplaintReopen(ComplainResponceBean complainResponceBean1){
//        if(!strStatus.equalsIgnoreCase("Close")){
            final BottomSheetDialog dialog = new BottomSheetDialog(this);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.write_comment_layout);
            final EditText commentText = (EditText) dialog.findViewById(R.id.commentText);
            final TextView name = (TextView) dialog.findViewById(R.id.name);
            final ImageView sendButton = (ImageView) dialog.findViewById(R.id.btnSend);
            final LinearLayout llMainLayout = (LinearLayout) dialog.findViewById(R.id.llMainLayout);
            new KeyboardUtil(this, llMainLayout);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
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
                    callForComplaintReopenAPI(complainResponceBean1,comment);
                    dialog.dismiss();
                    CloseKeyBoard();
                }
            });


            if(adminAccess){
                name.setText("A");
            }else{
                name.setText("U");
            }
            dialog.show();
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
                        dialog.dismiss();
                    return false;
                }
            });
        }
//        else{
//            CommonMethods.showToastSuccess(context, "This complaint is closed, You can't add comment");
////            Toasty.info(this, "This complaint is closed, You can't add comment", Toast.LENGTH_SHORT, true).show();
//        }
//    }
    @Override
    public void onGetResponse(String response, String callFor) {
        LoginResponse loginResponse = null;
        ComplainResponceBean complainResponceBean  = null;
        if (callFor.equals(CallFor.REOPENCOMPLAINT))
        {
            Log.e("response",response+"-");
            loginResponse = new Gson().fromJson(response, LoginResponse.class);
            Log.e("value",new Gson().toJson(loginResponse)+"-");
            if (loginResponse.getCode().equalsIgnoreCase("success")) {
//                CommonMethods.showToastSuccess(getApplicationContext(), loginResponse.getMessage());
//                MainActivity.complian_result.clear();
//                MainActivity.pageNo=0;
//                String extendedUrl = "?pageNo="+MainActivity.pageNo+"&newVersion=true";
//                new GetData(MainActivity.mainActivity, CallFor.GET_COMPLAIN, extendedUrl).execute();
                this.finish();
            }else
            {
                CommonMethods.showToastSuccess(getApplicationContext(), loginResponse.getMessage());
            }
        }
        if (callFor.equals(CallFor.LOGIN)) {
            loginResponse = new Gson().fromJson(response, LoginResponse.class);
            try {
                if (loginResponse.getCode().equals("SUCCESS")) {
                   // new_password = NewPassword1 .getText().toString().trim();
                  //  new GetData(context, CallFor.CHANGE_PASSWORD, new_password).execute();
                }
                else{
                    CommonMethods.showToastError(context, "Something went wrong, Please try again");
//                    Toasty.error(this, "Something went wrong, Please try again", Toast.LENGTH_SHORT, true).show();
                }
            }catch (Exception ex)
            {
                Log.e("Ex Setting Details "," "+ex);
            }
        }
        if (callFor.equals(CallFor.SAVE_COMPLAINT_COMMENT)) {
            complainResponceBean=new Gson().fromJson(response, ComplainResponceBean.class);
            try {
                if (complainResponceBean.getCode().equals("SUCCESS")) {
                    CommonMethods.showToastError(context, complainResponceBean.getMessage());
//                    Toasty.success(this, complainResponceBean.getMessage(), Toast.LENGTH_SHORT, true).show();
                    this.finish();
                }
                else{
                    CommonMethods.showToastError(context, complainResponceBean.getMessage());
//                    Toasty.error(this, complainResponceBean.getMessage(), Toast.LENGTH_SHORT, true).show();
                }

            }catch (Exception ex)
            {
                Log.e("Ex","");
            }
        }

        if (callFor.equals(CallFor.CHANGE_COMPLAINT_STATUS)) {
            complainResponceBean=new Gson().fromJson(response, ComplainResponceBean.class);
            try {
                if (complainResponceBean.getCode().equals("SUCCESS")) {
//                    CommonMethods.showToastSuccess(context, complainResponceBean.getMessage());
////                    Toasty.success(this, complainResponceBean.getMessage(), Toast.LENGTH_SHORT, true).show();
//                    MainActivity.complian_result.clear();
//                    MainActivity.pageNo=0;
//                    String extendedUrl = "?pageNo="+MainActivity.pageNo+"&newVersion=true";
//                    new GetData(MainActivity.mainActivity, CallFor.GET_COMPLAIN, extendedUrl).execute();
                    this.finish();
                }
                else{
                    CommonMethods.showToastError(context, complainResponceBean.getMessage());
//                    Toasty.error(this, complainResponceBean.getMessage(), Toast.LENGTH_SHORT, true).show();
                }

            }catch (Exception ex)
            {
                Log.e("Ex","");
            }
        }
    }

    public void showDatePicker(View v){
            showDatePicker();
    }

    private void showDatePicker(){
        // In Complaint is closed or user is not admin then dont show date picker
        if(adminAccess == true && !strStatus.equalsIgnoreCase("Close")){
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    ComplaintDetails.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            dpd.setMinDate(now);
            dpd.show(this.getFragmentManager(), "Datepickerdialog");
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+" "+months[monthOfYear]+" "+year;
        tvClosedDate.setVisibility(View.VISIBLE);
        Log.e("closedate",date+"=");
        tvClosedDate.setText(date);
        int month = monthOfYear+1;
        strDate = dayOfMonth+"-"+month+"-"+year;
    }

    public void CloseKeyBoard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public void ShowPhoto(View v) {
        final Dialog settingsDialog = new Dialog(this, R.style.DialogTheme);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.getWindow().getAttributes().windowAnimations = R.style.animationName;
        View showData = getLayoutInflater().inflate(R.layout.complaint_photo_layout, null);
//        final TextView ShowUserName = (TextView) showData.findViewById(R.id.tvNameAndNumber);
        final ImageView imageView = (ImageView) showData.findViewById(R.id.imgImage);

//        ShowUserName.setText("");
        if (complainResponceBean.getComplainDetails().getComplainPhoto()!=null) {
            Log.e("complainResponceBean",complainResponceBean.getComplainDetails().getComplainPhoto()+"--");
            if (!complainResponceBean.getComplainDetails().getComplainPhoto().equalsIgnoreCase("not added")) {
                String imgurl= Constants.BASE_IMG_URL+"/"+ complainResponceBean.getComplainDetails().getComplainPhoto();
                Log.e("complaintimgurl",imgurl+"--");
                new LoadImage().loadImage(context, R.drawable.no_photo_icon,imgurl, imageView, null);
            }
        }
//        new LoadImage().loadImage(this, R.drawable.ic_avatar_white, PhotoUrl, imageView, null);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsDialog.dismiss();
            }
        });

//        ShowUserName.setText(Name);
        settingsDialog.setContentView(showData);
        settingsDialog.setCanceledOnTouchOutside(false);
        settingsDialog.show();
    }
}