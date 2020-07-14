package vztrack.gls.com.vztrack_user.adapters;

/**
 * Created by sandeep on 14/3/16.
 */

import android.app.Dialog;
import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Date;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.CustumView.CustomButton;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.beans.PollBean;
import vztrack.gls.com.vztrack_user.responce.PollResponce;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.GetData;

public class UserPoll_RecyclerViewAdapter extends RecyclerView.Adapter<UserPoll_RecyclerViewAdapter.MyViewHolder> {

    private PollResponce pollResponce;
    Context context;
    String strPollName;
    Date pollCloseDate;
    String strPollQuestion;
    String strPollStatus;
    String[] strGroup;
    CheckConnection cc;

    // Dialog UI componants
    public static TextView tvRemark, tvTotalParticipant, tvCurrentStatus, tvCloseDate, tvPollName, tvCreatedDate, tvPollQuestion, tvGroupName;
    //public TextView tvDialogTitle;
    Button btnVote;
    ImageView back;
    EditText editTextRemark;
    RadioButton optionRadioButton1,optionRadioButton2,optionRadioButton3,optionRadioButton4,optionRadioButton5;
    TextView tvOptionAns1,tvOptionAns2,tvOptionAns3,tvOptionAns4,tvOptionAns5;
    LinearLayout optionsLayout1,optionsLayout2,optionsLayout3,optionsLayout4,optionsLayout5;
    RadioButton radioOption1, radioOption2, radioOption3, radioOption4, radioOption5;
    LinearLayout layoutTerminated;
    public static Dialog dialog;
    int selectedOptionId;
    boolean isPotionSelected = false;
    int myVotedOptionId = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView tvPollName, tvClosesDate, tvPollQuestion, tvPollStatus, pollTvGroupName;
        public CardView cvPollCard;
//        public View viewColorBar;
        public TextView btnAddVote;

        public MyViewHolder(View view) {
            super(view);
            tvPollName = (TextView) view.findViewById(R.id.tvPollName);
            tvClosesDate = (TextView) view.findViewById(R.id.tvClosesDate);
            tvPollQuestion = (TextView) view.findViewById(R.id.tvPollQuestion);
            tvPollStatus = (TextView) view.findViewById(R.id.tvStatus);
            cvPollCard = (CardView) view.findViewById(R.id.cvPollCard);
//            viewColorBar = (View) view.findViewById(R.id.viewColorBar);
            btnAddVote =  view.findViewById(R.id.btnAddVote);
            pollTvGroupName = (TextView) view.findViewById(R.id.pollTvGroupName);

        }
    }

    public UserPoll_RecyclerViewAdapter(Context con, PollResponce pollResponce) {
        this.pollResponce = pollResponce;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_poll_list_item_for_admin_and_user, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        strPollName = pollResponce.getObjPollBeans().get(position).getName();
        strPollQuestion = pollResponce.getObjPollBeans().get(position).getQuestion();
        pollCloseDate = pollResponce.getObjPollBeans().get(position).getCloseDate();
        strPollStatus = pollResponce.getObjPollBeans().get(position).getStatus();
        strGroup = pollResponce.getObjPollBeans().get(position).getGroupName();
        String groups = Arrays.toString(strGroup);
        holder.pollTvGroupName.setText("Send To : "+groups.substring(1, groups.length()-1).replace(",",",").trim());

        cc = new CheckConnection(context);

        String date =  UtilityMethods.formatDate(pollCloseDate, "dd MMM yyyy");

        holder.tvPollName.setText(strPollName);
        holder.tvPollQuestion.setText("Poll Que. :"+strPollQuestion);
        holder.tvClosesDate.setText("Close Date : "+date);
        holder.tvPollStatus.setText(strPollStatus);

        if(pollResponce.getObjPollBeans().get(position).getStatus().equalsIgnoreCase("Published")){
            if( pollResponce.getObjPollBeans().get(position).isIsvoted()){
                holder.btnAddVote.setText("Voted");
            }else {
                holder.btnAddVote.setText("Vote");
            }
        }else{
            holder.btnAddVote.setText("View");
//            holder.btnAddVote.setIconResource("\uf06e");
        }

        if(strPollStatus.equalsIgnoreCase("published")){
//            holder.viewColorBar.setBackgroundColor(context.getResources().getColor(R.color.colorButton));
            holder.tvPollStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }else if (strPollStatus.equalsIgnoreCase("closed")){
//            holder.viewColorBar.setBackgroundColor(context.getResources().getColor(R.color.red_btn_bg_color));
            holder.tvPollStatus.setTextColor(context.getResources().getColor(R.color.nwred));
        }else if (strPollStatus.equalsIgnoreCase("terminated")){
//            holder.viewColorBar.setBackgroundColor(context.getResources().getColor(R.color.text_color));
            holder.tvPollStatus.setTextColor(context.getResources().getColor(R.color.ripplecolor));
        }

        holder.cvPollCard.setTag(pollResponce.getObjPollBeans().get(position));
        holder.btnAddVote.setTag(pollResponce.getObjPollBeans().get(position));
        holder.cvPollCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PollBean pollBean = (PollBean) holder.cvPollCard.getTag();
                CreateDialog(pollBean);
            }
        });

        holder.btnAddVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PollBean pollBean = (PollBean) holder.btnAddVote.getTag();
                CreateDialog(pollBean);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return pollResponce.getObjPollBeans().size();
    }

    public void CreateDialog(PollBean pollBean){
        isPotionSelected=false;
        myVotedOptionId = 0;
        dialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_poll_admin_and_user);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.show();
        initializeDialogComponant(dialog);
        ClickListenerOfDialogComponant(dialog, pollBean);
    }

    private void initializeDialogComponant(Dialog dialog){

        // All Buttons
        btnVote =  dialog.findViewById(R.id.btnVote);

        // All Textviews
        tvPollName = (TextView) dialog.findViewById(R.id.tvPollName);
        tvCreatedDate = (TextView) dialog.findViewById(R.id.tvCreatedDate);
        tvPollQuestion = (TextView) dialog.findViewById(R.id.tvPollQuestion);
        tvCloseDate = (TextView) dialog.findViewById(R.id.tvCloseDate);
        tvCurrentStatus = (TextView) dialog.findViewById(R.id.tvCurrentStatus);
        tvTotalParticipant = (TextView) dialog.findViewById(R.id.tvTotalParticipant);
        //tvDialogTitle = (TextView) dialog.findViewById(R.id.tvDialogTitle);
        tvRemark = (TextView) dialog.findViewById(R.id.tvRemark);
        tvGroupName = (TextView) dialog.findViewById(R.id.tvGroupName);

        editTextRemark = (EditText) dialog.findViewById(R.id.editTextRemark);
        tvOptionAns1 = (TextView) dialog.findViewById(R.id.tvOptionAns1);
        tvOptionAns2 = (TextView) dialog.findViewById(R.id.tvOptionAns2);
        tvOptionAns3 = (TextView) dialog.findViewById(R.id.tvOptionAns3);
        tvOptionAns4 = (TextView) dialog.findViewById(R.id.tvOptionAns4);
        tvOptionAns5 = (TextView) dialog.findViewById(R.id.tvOptionAns5);

        // All Linear Layouts
        optionsLayout1 = (LinearLayout) dialog.findViewById(R.id.optionsLayout1);
        optionsLayout2 = (LinearLayout) dialog.findViewById(R.id.optionsLayout2);
        optionsLayout3 = (LinearLayout) dialog.findViewById(R.id.optionsLayout3);
        optionsLayout4 = (LinearLayout) dialog.findViewById(R.id.optionsLayout4);
        optionsLayout5 = (LinearLayout) dialog.findViewById(R.id.optionsLayout5);

        // All Radion Button of Dialog
        radioOption1 = (RadioButton)dialog.findViewById(R.id.radioOption1);
        radioOption2 = (RadioButton)dialog.findViewById(R.id.radioOption2);
        radioOption3 = (RadioButton)dialog.findViewById(R.id.radioOption3);
        radioOption4 = (RadioButton)dialog.findViewById(R.id.radioOption4);
        radioOption5 = (RadioButton)dialog.findViewById(R.id.radioOption5);

        // All ImageView
        back = (ImageView) dialog.findViewById(R.id.imgBack);

        layoutTerminated = (LinearLayout) dialog.findViewById(R.id.layoutTerminated);
    }

    public void ClickListenerOfDialogComponant(final Dialog dialog, final PollBean pollBean){

        final int pollId = pollBean.getId();
        tvPollName.setText(pollBean.getName());
        tvCreatedDate.setText(UtilityMethods.formatDate(pollBean.getCreatedDate(), "dd MMM yy"));
        tvCloseDate.setText(UtilityMethods.formatDate(pollBean.getCloseDate(), "dd MMM yy"));
        tvPollQuestion.setText(pollBean.getQuestion());
        strGroup = pollBean.getGroupName();
        String groups = Arrays.toString(strGroup);
        tvGroupName.setText("Sent To : "+groups.substring(1, groups.length()-1).replace(",",",").trim());

        int totalOptions = pollBean.getOptions().size();

        if(pollBean.getStatus().equalsIgnoreCase("published")){
            if(pollBean.isIsvoted()){
                btnVote.setText("Already Voted");
                btnVote.setEnabled(false);
                btnVote.setVisibility(View.GONE);
                myVotedOptionId = pollBean.getMyOptionId();
                //tvDialogTitle.setText("You Already Voted");
            }else{
                //tvDialogTitle.setText("Submit Your Vote");
            }
        }
        else if(pollBean.getStatus().equalsIgnoreCase("terminated")){
            btnVote.setVisibility(View.VISIBLE);
            btnVote.setEnabled(false);
            if(pollBean.isIsvoted()){
                myVotedOptionId = pollBean.getMyOptionId();
            }
            layoutTerminated.setVisibility(View.VISIBLE);
            tvRemark.setText(pollBean.getRemark());
            disableAllRadioOptionButtons();
            btnVote.setVisibility(View.GONE);
        }else if(pollBean.getStatus().equalsIgnoreCase("closed")){
            btnVote.setVisibility(View.VISIBLE);
            btnVote.setEnabled(false);
            if(pollBean.isIsvoted()){
                myVotedOptionId = pollBean.getMyOptionId();
            }
            btnVote.setVisibility(View.GONE);
            disableAllRadioOptionButtons();
        }

        tvCurrentStatus.setText(pollBean.getStatus());
        tvTotalParticipant.setText(""+pollBean.getTotalParticipants());

        for(int i=0;i<totalOptions;i++){
            int val = i+1;
            String optext = pollBean.getOptions().get(i).getOptionText();
            int opId = pollBean.getOptions().get(i).getOptionId();
            double opPercent = pollBean.getOptions().get(i).getVotePercent();
            if(val==1){
                setupOption1(opId, optext, opPercent);
            }else if(val==2){
                setupOption2(opId, optext, opPercent);
            }else if(val==3){
                setupOption3(opId, optext, opPercent);
            }else if(val==4){
                setupOption4(opId, optext, opPercent);
            }else if(val==5){
                setupOption5(opId, optext, opPercent);
            }
        }

        radioOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPotionSelected = true;
                selectedOptionId = (int) radioOption1.getTag();
                radioOption1.setChecked(true);
                radioOption2.setChecked(false);
                radioOption3.setChecked(false);
                radioOption4.setChecked(false);
                radioOption5.setChecked(false);
            }
        });

        radioOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPotionSelected = true;
                selectedOptionId = (int) radioOption2.getTag();
                radioOption1.setChecked(false);
                radioOption2.setChecked(true);
                radioOption3.setChecked(false);
                radioOption4.setChecked(false);
                radioOption5.setChecked(false);
            }
        });
        radioOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPotionSelected = true;
                selectedOptionId = (int) radioOption3.getTag();
                radioOption1.setChecked(false);
                radioOption2.setChecked(false);
                radioOption3.setChecked(true);
                radioOption4.setChecked(false);
                radioOption5.setChecked(false);
            }
        });
        radioOption4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPotionSelected = true;
                selectedOptionId = (int) radioOption4.getTag();
                radioOption1.setChecked(false);
                radioOption2.setChecked(false);
                radioOption3.setChecked(false);
                radioOption4.setChecked(true);
                radioOption5.setChecked(false);
            }
        });
        radioOption5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPotionSelected = true;
                selectedOptionId = (int) radioOption5.getTag();
                radioOption1.setChecked(false);
                radioOption2.setChecked(false);
                radioOption3.setChecked(false);
                radioOption4.setChecked(false);
                radioOption5.setChecked(true);
            }
        });


        btnVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cc.isConnectingToInternet()){
                    if(isPotionSelected){
                        SavePollApiCall(pollId, selectedOptionId);
                    }else{
                        CommonMethods.showToastError(context,"Please select option");//, Toast.LENGTH_LONG).show();
                    }
                }else{
                    CommonMethods.showToastError(context, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void setupOption1(int opId, String opText, double opPercent){
        optionsLayout1.setVisibility(View.VISIBLE);
        radioOption1.setText(opText);
        radioOption1.setTag(opId);
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) tvOptionAns1.getLayoutParams();
        double width =  dpFromPx(context, (float) opPercent*2) +  pxFromDp(context, 40);;
        params.width = (int) width;
        tvOptionAns1.setLayoutParams(params);
        tvOptionAns1.setText((int)opPercent+"%");
        if(myVotedOptionId==opId){
            radioOption1.setChecked(true);
            disableAllRadioOptionButtons();
        }
    }

    public void setupOption2(int opId, String opText, double opPercent){
        optionsLayout2.setVisibility(View.VISIBLE);
        radioOption2.setText(opText);
        radioOption2.setTag(opId);
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) tvOptionAns2.getLayoutParams();
        double width =  dpFromPx(context, (float) opPercent*2) +  pxFromDp(context, 40);;
        params.width = (int) width;
        tvOptionAns2.setLayoutParams(params);
        tvOptionAns2.setText((int)opPercent+"%");
        if(myVotedOptionId==opId){
            radioOption2.setChecked(true);
            disableAllRadioOptionButtons();
        }
    }
    public void setupOption3(int opId, String opText, double opPercent){
        optionsLayout3.setVisibility(View.VISIBLE);
        radioOption3.setText(opText);
        radioOption3.setTag(opId);
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) tvOptionAns3.getLayoutParams();
        double width =  dpFromPx(context, (float) opPercent*2) +  pxFromDp(context, 40);;
        params.width = (int) width;
        tvOptionAns3.setLayoutParams(params);
        tvOptionAns3.setText((int)opPercent+"%");
        if(myVotedOptionId==opId){
            radioOption3.setChecked(true);
            disableAllRadioOptionButtons();
        }
    }
    public void setupOption4(int opId, String opText, double opPercent){
        optionsLayout4.setVisibility(View.VISIBLE);
        radioOption4.setText(opText);
        radioOption4.setTag(opId);
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) tvOptionAns4.getLayoutParams();
        double width =  dpFromPx(context, (float) opPercent*2) +  pxFromDp(context, 40);;
        params.width = (int) width;
        tvOptionAns4.setLayoutParams(params);
        tvOptionAns4.setText((int)opPercent+"%");
        if(myVotedOptionId==opId){
            radioOption4.setChecked(true);
            disableAllRadioOptionButtons();
        }
    }
    public void setupOption5(int opId, String opText, double opPercent){
        optionsLayout5.setVisibility(View.VISIBLE);
        radioOption5.setText(opText);
        radioOption5.setTag(opId);
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) tvOptionAns5.getLayoutParams();
        double width =  dpFromPx(context, (float) opPercent*2) +  pxFromDp(context, 40);;
        params.width = (int) width;
        tvOptionAns5.setLayoutParams(params);
        tvOptionAns5.setText((int)opPercent+"%");
        if(myVotedOptionId==opId){
            radioOption5.setChecked(true);
            disableAllRadioOptionButtons();
        }
    }

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public void SavePollApiCall(int pollId, int pollOption){
        String extendedUrl = "?pollId="+pollId+"&pollOptionId="+pollOption;
        new GetData(MainActivity.mainActivity, CallFor.SAVE_POLL, ""+extendedUrl).execute();
    }

    public  void disableAllRadioOptionButtons(){
        radioOption1.setEnabled(false);
        radioOption2.setEnabled(false);
        radioOption3.setEnabled(false);
        radioOption4.setEnabled(false);
        radioOption5.setEnabled(false);
    }

    public static void ClearAllData(){
        dialog.dismiss();
    }

}