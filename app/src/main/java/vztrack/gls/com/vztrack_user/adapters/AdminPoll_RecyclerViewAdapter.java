package vztrack.gls.com.vztrack_user.adapters;

/**
 * Created by sandeep on 14/3/16.
 */

import android.app.Dialog;
import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.thomashaertel.widget.MultiSpinner;
import com.vladsch.flexmark.ast.Content;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.CustumView.CustomButton;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.beans.GroupBeans;
import vztrack.gls.com.vztrack_user.beans.OptionsDetailsBean;
import vztrack.gls.com.vztrack_user.beans.PollBean;
import vztrack.gls.com.vztrack_user.fragment.CreateEditPollsFragment;
import vztrack.gls.com.vztrack_user.responce.PollResponce;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.PollActions;
import vztrack.gls.com.vztrack_user.utils.PostData;

public class AdminPoll_RecyclerViewAdapter extends RecyclerView.Adapter<AdminPoll_RecyclerViewAdapter.MyViewHolder> implements DatePickerDialog.OnDateSetListener  {

    private PollResponce pollResponce;
    static Context context;
    String strPollName;
    Date pollCloseDate;
    String strPollQuestion;
    String strPollStatus;
    CheckConnection cc;


    // Dialog UI componants
    public static TextView tvCloseDate, tvPollName, tvCreatedDate, tvPollQuestion, tvGroupName;
   // public TextView tvDialogTitle;
    public static TextView tvTotalParticipant, tvCurrentStatus, tvRemark;
    Button btnVote;
    ImageView back;
    RadioButton optionRadioButton1,optionRadioButton2,optionRadioButton3,optionRadioButton4,optionRadioButton5;
    TextView tvOptionAns1,tvOptionAns2,tvOptionAns3,tvOptionAns4,tvOptionAns5;
    LinearLayout optionsLayout1,optionsLayout2,optionsLayout3,optionsLayout4,optionsLayout5;
    RadioButton radioOption1, radioOption2, radioOption3, radioOption4, radioOption5;
    public static Dialog dialog;
    EditText editTextRemark;
    String strPollCloseDate;
    int addedOptionCount;
    int addedGroup;
    LinearLayout layoutTerminated;

    // Save Dialog Componat
    public static EditText pollName, pollQuestion, optionText ;
    Button btnAddOption, btnPublish, btnSave, btnTerminate;
    public static LinearLayout addedOptionsLayout, addedOptionsLayoutMain;
    ImageView  imgCalender;
    TextView selectedClosedDate, dialogHeading;
    ArrayList<String> optionItems =new ArrayList<String>();
    String[] strGroup;
    public static LinearLayout groupLayout;
    public static List<String> groupListAdmin = new ArrayList<String>();
    public static List<Integer> groupIDListAdmin = new ArrayList<Integer>();
    public static ArrayAdapter<String> adapter;
    public  List<String> groupListCheck = new ArrayList<String>();

    int selectedOptionId;
    boolean isPotionSelected = false;
    int myVotedOptionId = 0;
    LayoutInflater inflater;
    int tagCnt = 0;
    String[] months;
    String strDate;

    //for group
    static ArrayList<String> arrayOfGroup;
    static MultiSpinner groupSpinnerPoll;
    static Switch pollAllSwitch;
    static LinearLayout linearGroupList;
    static TextView counterHeadingGroup;
    static  String group_name = null;
    static ArrayList<String> groupsArray = new ArrayList<>();

    String callFrom;
    AdminPoll_RecyclerViewAdapter adminPoll_recyclerViewAdapter;
    public AdminPoll_RecyclerViewAdapter() {
        adminPoll_recyclerViewAdapter = this;
    }

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

    public AdminPoll_RecyclerViewAdapter(Context con, PollResponce pollResponce) {
        this.pollResponce = pollResponce;
        this.context = con;
    }

    @Override
    public AdminPoll_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.card_poll_list_item_for_admin_and_user, parent, false);
        return new AdminPoll_RecyclerViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AdminPoll_RecyclerViewAdapter.MyViewHolder holder, int position) {
        strPollName = pollResponce.getObjPollBeans().get(position).getName();
        strPollQuestion = pollResponce.getObjPollBeans().get(position).getQuestion();
        pollCloseDate = pollResponce.getObjPollBeans().get(position).getCloseDate();
        strPollStatus = pollResponce.getObjPollBeans().get(position).getStatus();
        strGroup = pollResponce.getObjPollBeans().get(position).getGroupName();
        String groups = Arrays.toString(strGroup);
        holder.pollTvGroupName.setText(groups.substring(1, groups.length()-1).replace(",",",").trim());
        arrayOfGroup = new ArrayList<>();

        String date =  UtilityMethods.formatDate(pollCloseDate, "dd MMM yyyy");

        holder.tvPollName.setText(strPollName);
        holder.tvPollQuestion.setText(strPollQuestion);
        holder.tvClosesDate.setVisibility(View.VISIBLE);
        holder.tvClosesDate.setText("Closed date : "+date);
        holder.tvPollStatus.setText(strPollStatus);
        holder.btnAddVote.setText("View");
//        holder.btnAddVote.setIconResource("\uf06e");
        cc = new CheckConnection(context);

        if(strPollStatus.equalsIgnoreCase("published")){
//            holder.viewColorBar.setBackgroundColor(context.getResources().getColor(R.color.colorButton));
            holder.tvPollStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }else if (strPollStatus.equalsIgnoreCase("closed")){
//            holder.viewColorBar.setBackgroundColor(context.getResources().getColor(R.color.red_btn_bg_color));
            holder.tvPollStatus.setTextColor(context.getResources().getColor(R.color.nwred));
        }else if (strPollStatus.equalsIgnoreCase("terminated")){
//            holder.viewColorBar.setBackgroundColor(context.getResources().getColor(R.color.text_color));
            holder.tvPollStatus.setTextColor(context.getResources().getColor(R.color.ripplecolor));
        }else if (strPollStatus.equalsIgnoreCase("saved")){
//            holder.viewColorBar.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            holder.tvPollStatus.setTextColor(context.getResources().getColor(R.color.slide_2));
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

    public static void setAdapter(List<String> groupList ,List<Integer> groupIDList){
        groupListAdmin.clear();
        groupIDListAdmin.clear();
        for(int i =0;i < groupList.size();i++){
            groupListAdmin.add(groupList.get(i));
            groupIDListAdmin.add(groupIDList.get(i));
        }
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
        adapter.addAll(groupListAdmin);
        groupSpinnerPoll.setAdapter(adapter, false, onSelectedListener);

    }

    private static MultiSpinner.MultiSpinnerListener onSelectedListener =
            new MultiSpinner.MultiSpinnerListener() {
                public void onItemsSelected(boolean[] selected) {
                    if (groupIDListAdmin.size() == 0) {
                        groupSpinnerPoll.setEnabled(false);
                        groupSpinnerPoll.setText("No Group Added Yet");
                    }else {
                        groupSpinnerPoll.setEnabled(true);
                        for(int j=0;j<selected.length;j++){
                            if(selected[j]){
                                group_name = groupSpinnerPoll.getAdapter().getItem(j).toString().trim();
                                boolean isSame = arrayOfGroup.contains(group_name.trim());
                                if(isSame){
                                    CommonMethods.showToastError(context,"Already added group : "+group_name);//,Toast.LENGTH_SHORT).show();
                                }else{
                                    addItem(group_name.trim());
                                    if(j+1==groupListAdmin.size()){
                                        groupSpinnerPoll.setText("SELECT GROUP");
                                    }
                                }
                            }
                        }
                        groupSpinnerPoll.setText("SELECT GROUP");
                    }

                }
            };

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return pollResponce.getObjPollBeans().size();
    }

    public void CreateDialog(PollBean pollBean){
        if(cc.isConnectingToInternet()){
            groupListAdmin.clear();
            new GetData((MainActivity)context, CallFor.GET_GROUPS, "").execute();
        }else{
            CommonMethods.showToastError(context,"Please Check Internet Connection");//,Toast.LENGTH_LONG).show();
        }
        if(pollBean.getStatus().equalsIgnoreCase("published")){
            CreateTerminateAndViewDialog(pollBean);
        }else if(pollBean.getStatus().equalsIgnoreCase("terminated")){
            CreateTerminateAndViewDialog(pollBean);
        }else if(pollBean.getStatus().equalsIgnoreCase("saved")){
            CreateSaveDialog(pollBean);
        }else if(pollBean.getStatus().equalsIgnoreCase("closed")){
            CreateTerminateAndViewDialog(pollBean);
        }
    }

    public void CreateSaveDialog(final PollBean pollBean){
        optionItems.clear();  // Before Creating dialog clear all options
        dialog = new Dialog(context, DialogFragment.STYLE_NO_FRAME);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_poll_create_fragment);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        initializeSaveDialogComponant(dialog);

        months = context.getResources().getStringArray(R.array.months);

        // Prevent to open keyBoard
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        pollName.setText(pollBean.getName());
        pollQuestion.setText(pollBean.getQuestion());
        int groupSize = pollBean.getGroupBeans().size();
            if(groupSize == 0){
                pollAllSwitch.setChecked(true);
                groupLayout.setVisibility(View.GONE);
            }else{
                pollAllSwitch.setChecked(false);
                groupLayout.setVisibility(View.VISIBLE);
//                groupLayout.setBackgroundColor(Color.parseColor("#E8E8E8"));
                groupLayout.setAlpha(1);
                groupSpinnerPoll.setEnabled(true);
                groupSpinnerPoll.setClickable(true);
            }
        for(int i = 0 ;i< pollBean.getGroupBeans().size();i++){
            String group = pollBean.getGroupBeans().get(i).getGroup_name();
            addItem(group);
        }

        dialogHeading.setText(R.string.poll);

        btnAddOption.setEnabled(false);

        String date =  UtilityMethods.formatDate(pollBean.getCloseDate(), "dd MMM yyyy");
        selectedClosedDate.setVisibility(View.VISIBLE);
        selectedClosedDate.setText(date);


        Date dateClosedDate = null;
        Date todayDate = null;
        int dateCompareValues = 0;
        try {
            java.text.DateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            dateClosedDate = sdf.parse(selectedClosedDate.getText().toString().trim());
            Date objDate = new Date();
            todayDate = sdf.parse(sdf.format(objDate));
            dateCompareValues = dateClosedDate.compareTo(todayDate);
            if(dateCompareValues<0){
                selectedClosedDate.setText("");
//                selectedClosedDate.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            Log.e("Exception "," "+e);
        }

        btnAddOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strOption = optionText.getText().toString().trim();
                if(strOption.equalsIgnoreCase("")){
                    CommonMethods.showToastSuccess(context,"Option Should not be blank");//,Toast.LENGTH_LONG).show();
                }else{
                    addOptionItem(inflater, strOption);
                }
            }
        });

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cc.isConnectingToInternet()){
                    callFrom = "publish";
                    boolean validation = validate(callFrom);
                    if(validation){
                        // For publish we have to call save first then Publish
                        publishPoll(pollBean);
                    }
                }else{
                    CommonMethods.showToastSuccess(context, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                }

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(cc.isConnectingToInternet()){
                callFrom = "save";
                boolean validation = validate(callFrom);
                if(validation){
                    savePoll(pollBean);
                }
            }else{
                CommonMethods.showToastSuccess(context, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
            }
            }
        });

        btnTerminate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cc.isConnectingToInternet()){
                    callFrom = "terminate";
                    boolean validation = validate(callFrom);
                    if(validation){
                        terminatePoll(pollBean);
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

        optionText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(optionText.getText().toString().equals("")){
                    btnAddOption.setEnabled(false);
                }else{
                    btnAddOption.setEnabled(true);
                }
            }
        });

        imgCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        int optionCount = pollBean.getOptions().size();

        for(int i=0;i<optionCount;i++){
            String op = pollBean.getOptions().get(i).getOptionText();
            addOptionItem(inflater, op);
        }

        dialog.show();
    }

    private boolean validate(String callFrom){
        boolean validated = false;
        strPollName = pollName.getText().toString().trim();
        strPollQuestion = pollQuestion.getText().toString().trim();
        strPollCloseDate = selectedClosedDate.getText().toString().trim();
        addedOptionCount = addedOptionsLayout.getChildCount();
        addedGroup = linearGroupList.getChildCount();

        if(strPollName.equals("")){
            CommonMethods.showToastError(context,"Poll name should not be blank");//, Toast.LENGTH_SHORT).show();
        }else if(strPollQuestion.equals("")){
            CommonMethods.showToastError(context,"Poll question should not be blank");//, Toast.LENGTH_SHORT).show();
        }else if(!callFrom.equalsIgnoreCase("terminate") && strPollCloseDate.equals("")){
            CommonMethods.showToastError(context,"Poll Close date should not be blank");//, Toast.LENGTH_SHORT).show();
        }
        else if(addedOptionCount<2){
            CommonMethods.showToastError(context,"Minimum 2 options are required");//, Toast.LENGTH_SHORT).show();
        }else if(!callFrom.equalsIgnoreCase("terminate") && addedGroup == 0 && pollAllSwitch.isChecked() == false){
            CommonMethods.showToastError(context,"No Group added yet");//, Toast.LENGTH_SHORT).show();
        } else {
            validated = true;
        }
        return validated;
    }

    private void publishPoll(PollBean pollBean){
        // For Publish first save And then publish
        pollBean.setAction(PollActions.SAVE);
        setDataForApiCall(pollBean);
        pollBean.setAction(PollActions.PUBLISH);
        apiCall(pollBean);
    }

    private void savePoll(PollBean pollBean){
        pollBean.setAction(PollActions.SAVE);
        setDataForApiCall(pollBean);
    }

    private void terminatePoll(PollBean pollBean){
        pollBean.setAction(PollActions.DELETE);
        apiCall(pollBean);
        //setDataForApiCall(pollBean);
    }

    private void apiCall(PollBean pollBean){
        MainActivity.dialogFragment = 2;
        new PostData(new Gson().toJson(pollBean), MainActivity.mainActivity, CallFor.POLL_OPERATION).execute();
    }

    private void setDataForApiCall(PollBean pollBean){
        if(cc.isConnectingToInternet()){
            int[] G_Ids = null;
            pollBean.setName(strPollName);
            pollBean.setQuestion(strPollQuestion);
            ArrayList<OptionsDetailsBean> options = new ArrayList<>();
            for(int i=0;i<optionItems.size();i++){
                OptionsDetailsBean optionsDetailsBean = new OptionsDetailsBean();
                optionsDetailsBean.setOptionText(optionItems.get(i));
                options.add(optionsDetailsBean);
            }
            pollBean.setOptions(options);
            Date dateClosedDate = null;
            try {
                java.text.DateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                dateClosedDate = sdf.parse(selectedClosedDate.getText().toString().trim());
            } catch (Exception e) {
                Log.e("Exception "," "+e);
            }

            pollBean.setCloseDate(dateClosedDate);
            if(pollAllSwitch.isChecked() == true){
                pollBean.setGroupId(G_Ids = new int[]{});
            }else{
                List<Integer> selectedGroupId = new ArrayList<Integer>();
                for (int i = 0; i < arrayOfGroup.size(); i++) {
                    String grp = arrayOfGroup.get(i).trim();
                    for (int k = 0; k < groupIDListAdmin.size(); k++) {
                        if (groupListAdmin.get(k).equals(grp)) {
                            selectedGroupId.add(groupIDListAdmin.get(k));
                        }
                    }
                }
                for (int i = 0; i < selectedGroupId.size(); i++) {
                    if (i == 0) {
                        G_Ids = new int[selectedGroupId.size()];
                        G_Ids[i] = selectedGroupId.get(i);
                    } else {
                        G_Ids[i] = selectedGroupId.get(i);
                    }
                }
                pollBean.setGroupId(G_Ids);
            }
            apiCall(pollBean);
        }else{
            CommonMethods.showToastError(context,"Please check Internet Connection");//,Toast.LENGTH_LONG).show();
        }

    }

    private void addOptionItem(LayoutInflater inflater, String strPptionText){
        String strOptionText = strPptionText;
        int addedOptionCount = addedOptionsLayout.getChildCount();
        if(addedOptionCount<5){
            tagCnt++;
            addedOptionsLayout.setVisibility(View.VISIBLE);
            final ViewGroup option = (ViewGroup) inflater.inflate(R.layout.close_btn, null);
            option.setTag(tagCnt);
            RelativeLayout optionButton = (RelativeLayout) option.findViewById(R.id.optionButton);
            final ImageView closeButton = (ImageView) option.findViewById(R.id.btn_close);
            closeButton.setTag(tagCnt);

            TextView tvOptionText = (TextView)option.findViewById(R.id.name);
            tvOptionText.setText(strOptionText);
            tvOptionText.setTag(tagCnt);
            closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                View viewLinear;
                int cnt = option.getChildCount();
                for(int i=0; i<cnt; i++) {
                    viewLinear = option.getChildAt(i);
                    if (viewLinear instanceof TextView) {
                           TextView textView = (TextView) viewLinear;
                           String removedOption = textView.getText().toString();
                           optionItems.remove(removedOption);
                    }
                }
                        addedOptionsLayout.removeView(option);
                    }
                });

                //added LayoutParams
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.topMargin = 5;
            params.bottomMargin = 5;
            params.leftMargin = 5;
            params.rightMargin = 5;
                optionButton.setLayoutParams(params);
                addedOptionsLayout.addView(option);
                optionItems.add(strOptionText);
                optionText.setText("");
            }else{
            CommonMethods.showToastSuccess(context,"Maximum 5 options are allowed ");//, Toast.LENGTH_SHORT).show();
            }
    }

    private void initializeSaveDialogComponant(Dialog dialog){
        // All Edittext
        pollName = (EditText) dialog.findViewById(R.id.pollName);
        pollQuestion = (EditText) dialog.findViewById(R.id.pollQuestion);
        optionText = (EditText) dialog.findViewById(R.id.optionText);

        // All Textviews
        selectedClosedDate = (TextView) dialog.findViewById(R.id.selectedClosedDate);
        dialogHeading = (TextView) dialog.findViewById(R.id.dialogHeading);

        // All Buttons
        btnAddOption =  dialog.findViewById(R.id.addOption);
        btnPublish =  dialog.findViewById(R.id.btnPublish);
        btnSave =  dialog.findViewById(R.id.btnSave);
        btnTerminate =  dialog.findViewById(R.id.btnTerminate);

        // All Linear Layouts
        addedOptionsLayout = (LinearLayout) dialog.findViewById(R.id.addedOptionsLayout);
        addedOptionsLayoutMain = (LinearLayout) dialog.findViewById(R.id.addedOptionsLayoutMain);

        // All ImageView
        back = (ImageView) dialog.findViewById(R.id.imgBack);
        imgCalender = (ImageView) dialog.findViewById(R.id.imgCalender);

        groupSpinnerPoll = (MultiSpinner) dialog.findViewById(R.id.groupSpinnerPoll);
        pollAllSwitch = dialog.findViewById(R.id.pollAllSwitch);
        groupLayout = (LinearLayout)dialog.findViewById(R.id.groupLayout);
        linearGroupList = (LinearLayout)dialog.findViewById(R.id.linearGroupList);
        counterHeadingGroup = (TextView) dialog.findViewById(R.id.counterHeadingGroup);
        layoutSwitch();
        btnAddOption.setEnabled(false);

    }
    public void layoutSwitch() {
        pollAllSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    groupSpinnerPoll.setText("SELECT GROUP ");
                    groupLayout.setVisibility(View.GONE);
                }else{
                    groupSpinnerPoll.setText("SELECT GROUP");
                    groupLayout.setVisibility(View.VISIBLE);
//                    groupLayout.setBackgroundColor(Color.parseColor("#E8E8E8"));
                    groupLayout.setAlpha(1);
                    groupSpinnerPoll.setEnabled(true);
                    groupSpinnerPoll.setClickable(true);
                    groupSpinnerPoll.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if(groupIDListAdmin.size()==0){
                                CommonMethods.showToastError(MainActivity.mainActivity,"No groups added yet");//,Toast.LENGTH_LONG).show();
                                groupSpinnerPoll.setEnabled(false);
                            }
                            groupSpinnerPoll.setEnabled(true);
                            adapter = new ArrayAdapter<String>((MainActivity)context, android.R.layout.simple_spinner_item);
                            adapter.addAll(groupListAdmin);
                            groupSpinnerPoll.setAdapter(adapter, false, onSelectedListener);
                            return false;
                        }
                    });
                    groupSpinnerPoll.setHint("SELECT GROUP");
                }
            }
        });
    }
    public void CreateTerminateAndViewDialog(PollBean pollBean){
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
        initializeTerminateDialogComponant(dialog);
        ClickListenerOfTerminateDialogComponant(dialog, pollBean);
        if(pollBean.getStatus().equalsIgnoreCase("terminated")){
            editTextRemark.setVisibility(View.GONE);
            btnVote.setVisibility(View.GONE);
            btnVote.setEnabled(false);
            //tvDialogTitle.setText(R.string.terminated);
            layoutTerminated.setVisibility(View.VISIBLE);
            tvRemark.setText(pollBean.getRemark());
        }else if(pollBean.getStatus().equalsIgnoreCase("published")){
            editTextRemark.setVisibility(View.VISIBLE);
            btnVote.setText("Terminate");
            //tvDialogTitle.setText(R.string.terminate);
            btnVote.setEnabled(true);
        }else if(pollBean.getStatus().equalsIgnoreCase("closed")){
            editTextRemark.setVisibility(View.GONE);
            btnVote.setVisibility(View.GONE);
            btnVote.setEnabled(false);
            //tvDialogTitle.setText(R.string.closed);
        }
        disableAllRadioOptionButtons();
    }

    private void initializeTerminateDialogComponant(Dialog dialog){

        // All Buttons
        btnVote =  dialog.findViewById(R.id.btnVote);

        // All Textviews
        tvPollName = (TextView) dialog.findViewById(R.id.tvPollName);
        tvCreatedDate = (TextView) dialog.findViewById(R.id.tvCreatedDate);
        tvPollQuestion = (TextView) dialog.findViewById(R.id.tvPollQuestion);
        tvCloseDate = (TextView) dialog.findViewById(R.id.tvCloseDate);
        //tvDialogTitle = (TextView) dialog.findViewById(R.id.tvDialogTitle);
        tvTotalParticipant = (TextView) dialog.findViewById(R.id.tvTotalParticipant);
        tvCurrentStatus = (TextView) dialog.findViewById(R.id.tvCurrentStatus);
        tvRemark = (TextView) dialog.findViewById(R.id.tvRemark);
        tvGroupName = (TextView) dialog.findViewById(R.id.tvGroupName);
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

        // All Edit Text
        editTextRemark = (EditText) dialog.findViewById(R.id.editTextRemark);

        // All ImageView
        back = (ImageView) dialog.findViewById(R.id.imgBack);
        layoutTerminated = (LinearLayout) dialog.findViewById(R.id.layoutTerminated);
    }

    public void ClickListenerOfTerminateDialogComponant(final Dialog dialog, final PollBean pollBean){

        final int pollId = pollBean.getId();
        tvPollName.setText(pollBean.getName());
        tvCreatedDate.setText(UtilityMethods.formatDate(pollBean.getCreatedDate(), "dd MMM yy"));
        tvCloseDate.setText(UtilityMethods.formatDate(pollBean.getCloseDate(), "dd MMM yy"));
        tvPollQuestion.setText(pollBean.getQuestion());

        tvTotalParticipant.setText(""+pollBean.getTotalParticipants());
        tvCurrentStatus.setText("Status : "+pollBean.getStatus());

        strGroup = pollBean.getGroupName();
        String groups = Arrays.toString(strGroup);
        tvGroupName.setText(groups.substring(1, groups.length()-1).replace(",",",").trim());

        int totalOptions = pollBean.getOptions().size();
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
                    String strRemark = editTextRemark.getText().toString().trim();
                    if(strRemark.equals("")){
                        CommonMethods.showToastSuccess(context,"Remark should not be blank");//, Toast.LENGTH_LONG).show();
                    }else{
                        pollBean.setRemark(strRemark);
                        pollBean.setAction("Terminate");
                        pollBean.setCloseDate(getTodaysDate());
                        apiCallTerminatePoll(pollBean);
                    }
                }else{
                    CommonMethods.showToastSuccess(context, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
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

    public  void disableAllRadioOptionButtons(){
        radioOption1.setEnabled(false);
        radioOption2.setEnabled(false);
        radioOption3.setEnabled(false);
        radioOption4.setEnabled(false);
        radioOption5.setEnabled(false);
        radioOption1.setButtonDrawable(android.R.color.transparent);
        radioOption1.setPadding(31, 0, 0, 0);
        radioOption2.setButtonDrawable(android.R.color.transparent);
        radioOption2.setPadding(31, 0, 0, 0);
        radioOption3.setButtonDrawable(android.R.color.transparent);
        radioOption3.setPadding(31, 0, 0, 0);
        radioOption4.setButtonDrawable(android.R.color.transparent);
        radioOption4.setPadding(31, 0, 0, 0);
        radioOption5.setButtonDrawable(android.R.color.transparent);
        radioOption5.setPadding(31, 0, 0, 0);
    }

    public static void ClearAllData(){
        dialog.dismiss();
    }

    private Date getTodaysDate(){
        Date date = null;
        Calendar now = Calendar.getInstance();
        String strDate = now.get(Calendar.DAY_OF_MONTH)+"-"+ now.get(Calendar.MONTH)+"-"+now.get(Calendar.YEAR);
        try {
            java.text.DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            date = sdf.parse(strDate);
        } catch (Exception e) {
            Log.e("Exception "," "+e);
        }
        return date;
    }

    private void showDatePicker(){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                AdminPoll_RecyclerViewAdapter.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMinDate(now);
        dpd.show(MainActivity.mainActivity.getFragmentManager(), "Datepickerdialog");
    }

    public void apiCallTerminatePoll(PollBean pollBean) {
        MainActivity.dialogFragment = 2;
        new PostData(new Gson().toJson(pollBean),MainActivity.mainActivity, CallFor.POLL_OPERATION).execute();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+" "+months[monthOfYear]+" "+year;
        selectedClosedDate.setVisibility(View.VISIBLE);
        selectedClosedDate.setText(date);
        int month = monthOfYear+1;
        strDate = dayOfMonth+"-"+month+"-"+year;
    }

    public static void addItem(String groupName){
        arrayOfGroup.add(groupName);
        final RelativeLayout linearLayout = new RelativeLayout(context);
        counterHeadingGroup.setText("Added "+arrayOfGroup.size());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5, 5, 5, 5);
        linearLayout.setLayoutParams(lp);
//        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutInflater vi = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View closeView = vi.inflate(R.layout.close_btn, null);
        final TextView name=closeView.findViewById(R.id.name);

        final ImageView btn =  closeView.findViewById(R.id.btn_close);
        name.setText(groupName.trim());

        linearLayout.addView(closeView);
        linearGroupList.addView(linearLayout);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (linearGroupList.getChildCount() == 0) {
                    linearGroupList.setVisibility(View.GONE);
                    arrayOfGroup.clear();
                    ((LinearLayout) linearLayout.getParent()).removeView(linearLayout);
                    View v = linearLayout.getChildAt(0);
                    arrayOfGroup.remove(name.getText().toString().trim());
                } else {
                    linearGroupList.setVisibility(View.VISIBLE);
                    ((LinearLayout) linearLayout.getParent()).removeView(linearLayout);
                    arrayOfGroup.remove(name.getText().toString().trim());
                }
                counterHeadingGroup.setText("Added "+arrayOfGroup.size());
            }
        });
    }
}