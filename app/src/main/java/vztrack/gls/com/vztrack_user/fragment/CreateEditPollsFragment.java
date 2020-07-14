package vztrack.gls.com.vztrack_user.fragment;

/**
 * Created by sandeep on 14/3/16.
 */

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.DialogFragment;
import androidx.core.view.MenuItemCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.thomashaertel.widget.MultiSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.CustumView.CustomTextView;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.BaseActivity;
import vztrack.gls.com.vztrack_user.activity.BaseFragment;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.activity.VisitorNotification;
import vztrack.gls.com.vztrack_user.adapters.AdminPoll_RecyclerViewAdapter;
import vztrack.gls.com.vztrack_user.beans.MessageResponceBean;
import vztrack.gls.com.vztrack_user.beans.OptionsDetailsBean;
import vztrack.gls.com.vztrack_user.beans.PollBean;
import vztrack.gls.com.vztrack_user.responce.PollResponce;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.PollActions;
import vztrack.gls.com.vztrack_user.utils.PostData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

public class CreateEditPollsFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener {

    BaseActivity context;
    public static RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mAdapter;
    public static RecyclerView.LayoutManager mLayoutManager;
    public static SwipeRefreshLayout mSwipeRefreshLayout;
    FloatingActionButton fab;
    List<String> groupList = new ArrayList<String>();
    List<Integer> groupIDList = new ArrayList<Integer>();
    ArrayAdapter<String> adapter;


    // Dialog UI componants
    public static EditText pollName, pollQuestion, optionText ;
    public static TextView tvClose, tvDialogTitle;
    Button btnAddOption,  btnPublish, btnSave, btnTerminate;
    public static LinearLayout addedOptionsLayout, addedOptionsLayoutMain, groupLayout;
    ImageView back, imgCalender;
    EditText selectedClosedDate;
    public static Dialog dialog;
    ArrayList<String> optionItems;
    public  ArrayList<String> arrayOfGroup;
    public  MultiSpinner groupSpinnerPoll;
    public  Switch pollAllSwitch;
    public  LinearLayout linearGroupList;
    public  TextView counterHeadingGroup;
    String group_name = null;


    int tagCnt = 0;
    String strPollName;
    String strPollQuestion;
    String strPollCloseDate;
    int addedOptionCount;
    int addedGroup;
    CheckConnection cc;
    String strDate;
    public PollResponce adminPollResponce;

    String[] months ;
    private String TAG = "Admin Poll Fragment";
    CreateEditPollsFragment createEditPollsFragment;
    public static LinearLayout NoDataLayout;
    public CreateEditPollsFragment() {
        createEditPollsFragment = this;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_view_edit_poll, null);

        context = MainActivity.mainActivity;
        CleverTap.cleverTap_Record_Event(getActivity(), Events.poll_screen_admin);

        cc = new CheckConnection(getActivity());
        NoDataLayout=root.findViewById(R.id.NoDataLayout);
        months = getResources().getStringArray(R.array.months);

        adminPollResponce = MainActivity.adminPollResponce;


        setHasOptionsMenu(true);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.activity_main_swipe_refresh_layout);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.mypoll_admin_recycler_view);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(cc.isConnectingToInternet()) {
                    SheredPref.setRun(context, "DONT RUN");
                    new GetData(MainActivity.mainActivity, CallFor.ADMIN_POLLS, "").execute();
                }
                else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    CommonMethods.showToastError(context, "Unable to refresh, Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        // floating action button
        fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
            if(cc.isConnectingToInternet()){
                CleverTap.cleverTap_Record_Event(getActivity(), Events.poll_create_screen);
                dialog = new Dialog(getActivity(), DialogFragment.STYLE_NO_FRAME);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_poll_create_fragment);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                window.setAttributes(wlp);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                initializeDialogComponant(dialog);
                // Prevent to open keyBoard
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                btnTerminate.setVisibility(View.GONE);
                tvDialogTitle.setText(R.string.createPollTitle);

                optionItems = new ArrayList<String>();
                arrayOfGroup = new ArrayList<String>();

                if(cc.isConnectingToInternet()){
                    new GetData(createEditPollsFragment, CallFor.GET_GROUPS, "").execute();
                }else{
                    CommonMethods.showToastError(context,"Please Check Internet Connection");//,Toast.LENGTH_LONG).show();
                }

                btnAddOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addOptionItem(inflater);
                    }
                });

                btnPublish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean validation = validate();
                        if(validation){
                            publishPoll();
                        }
                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean validation = validate();
                        if(validation){
                            savePoll();
                        }
                    }
                });

                tvClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
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
                        //GradientDrawable backgroundColor = (GradientDrawable) btnAddOption.getBackground();
                        if(optionText.getText().toString().equals("")){
                            btnAddOption.setEnabled(false);
                            //backgroundColor.setColor(getResources().getColor(R.color.add_button_disable));
                        }else{
                            btnAddOption.setEnabled(true);
                            // backgroundColor.setColor(getResources().getColor(R.color.add_button_active));
                        }
                    }
                });

                imgCalender.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDatePicker();
                    }
                });
                dialog.show();
            }else{
                CommonMethods.showToastError(context, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
            }
            }
        });

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        setHasOptionsMenu(true);
        menu.findItem(R.id.action_call).setVisible(false);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setQueryHint("Search By Poll Name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                query = query.toLowerCase();
                try {
                    final String finalQuery = query;
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            adminPollResponce = getFilteredDataSet(finalQuery);
                            mRecyclerView.setHasFixedSize(true);
                            mLayoutManager = new LinearLayoutManager(context);
                            mRecyclerView.setLayoutManager(mLayoutManager);
                            mAdapter = new AdminPoll_RecyclerViewAdapter(context, adminPollResponce);
                            mRecyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception ex) {
                    Log.e("Ex ", " " + ex);
                }
                return true;
            }
        });
    }

    public PollResponce getFilteredDataSet(String query){
        PollResponce filteredAdminPollResponce = new PollResponce();
        if(query.equals("")){
            filteredAdminPollResponce = MainActivity.adminPollResponce;
        }else{
            for(int i=0;i<MainActivity.adminPollResponce.getObjPollBeans().size();i++){
                String pollName = MainActivity.adminPollResponce.getObjPollBeans().get(i).getName().toLowerCase();
                if(pollName.contains(query)){
                   filteredAdminPollResponce.getObjPollBeans().add(MainActivity.adminPollResponce.getObjPollBeans().get(i));
                }
            }
        }
        return filteredAdminPollResponce;
    }

    private void initializeDialogComponant(Dialog dialog){
        // All Edittext
        pollName = (EditText) dialog.findViewById(R.id.pollName);
        pollQuestion = (EditText) dialog.findViewById(R.id.pollQuestion);
        optionText = (EditText) dialog.findViewById(R.id.optionText);

        // All Textviews
        selectedClosedDate = (EditText) dialog.findViewById(R.id.selectedClosedDate);
        tvClose = (TextView) dialog.findViewById(R.id.tvClose);
        tvDialogTitle = (TextView) dialog.findViewById(R.id.dialogHeading);

        // All Buttons
        btnAddOption = (Button) dialog.findViewById(R.id.addOption);
        btnPublish = (Button) dialog.findViewById(R.id.btnPublish);
        btnSave = (Button) dialog.findViewById(R.id.btnSave);
        btnTerminate = (Button) dialog.findViewById(R.id.btnTerminate);

        // All Linear Layouts
        addedOptionsLayout = (LinearLayout) dialog.findViewById(R.id.addedOptionsLayout);
        addedOptionsLayoutMain = (LinearLayout) dialog.findViewById(R.id.addedOptionsLayoutMain);

        // All ImageView
        back = (ImageView) dialog.findViewById(R.id.imgBack);
        imgCalender = (ImageView) dialog.findViewById(R.id.imgCalender);
        groupSpinnerPoll = (MultiSpinner) dialog.findViewById(R.id.groupSpinnerPoll);
        pollAllSwitch = (Switch) dialog.findViewById(R.id.pollAllSwitch);
        groupLayout = (LinearLayout)dialog.findViewById(R.id.groupLayout);
        linearGroupList = (LinearLayout)dialog.findViewById(R.id.linearGroupList);
        counterHeadingGroup = (TextView) dialog.findViewById(R.id.counterHeadingGroup);

        btnAddOption.setEnabled(false);
        pollAllSwitch.setChecked(true);
//        groupLayout.setBackgroundColor(Color.parseColor("#B3B3B3"));
        groupLayout.setAlpha(0.3f);
        groupSpinnerPoll.setEnabled(false);
        groupSpinnerPoll.setClickable(false);
        counterHeadingGroup.setText("Added 0");
        layoutSwitch();
    }

    private void addOptionItem(LayoutInflater inflater){
        String strOptionText = optionText.getText().toString().trim();
        if(strOptionText.equals("")){
            CommonMethods.showToastError(context,"");//, Toast.LENGTH_SHORT).show();
        }else {
            int addedOptionCount = addedOptionsLayout.getChildCount();
            if(addedOptionCount<5){
                tagCnt++;
                addedOptionsLayout.setVisibility(View.VISIBLE);
                final ViewGroup option = (ViewGroup) inflater.inflate(R.layout.close_btn, null);
                option.setTag(tagCnt);
                RelativeLayout optionButton =  option.findViewById(R.id.optionButton);
                final TextView name=option.findViewById(R.id.name);
                final ImageView closeButton = (ImageView) option.findViewById(R.id.btn_close);
                closeButton.setTag(tagCnt);

//                TextView tvOptionText = (TextView)option.findViewById(R.id.tvOptionText);
                name.setText(strOptionText);
                name.setTag(tagCnt);
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
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.topMargin = 5;
                params.bottomMargin=5;
                params.leftMargin=5;
                params.rightMargin=5;
                optionButton.setLayoutParams(params);
                addedOptionsLayout.addView(option);
                optionItems.add(strOptionText);
                optionText.setText("");
            }else{
                CommonMethods.showToastError(context,"Maximum 5 options are allowed ");//, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validate(){
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
        }else if(strPollCloseDate.equals("")){
            CommonMethods.showToastError(context,"Poll Close date should not be blank");//, Toast.LENGTH_SHORT).show();
        }else if(addedOptionCount<2){
            CommonMethods.showToastError(context,"Minimum 2 options are required");//, Toast.LENGTH_SHORT).show();
        }else if(addedGroup == 0 && pollAllSwitch.isChecked() == false){
            CommonMethods.showToastError(context,"No Group added yet");//, Toast.LENGTH_SHORT).show();
        }else{
            validated = true;
        }
        return validated;
    }

    public void layoutSwitch() {
        pollAllSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    groupSpinnerPoll.setText("SELECT GROUP");
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
                            if(groupList.size()==0){
                                CommonMethods.showToastError(MainActivity.mainActivity,"No groups added yet");//,Toast.LENGTH_LONG).show();
                                groupSpinnerPoll.setEnabled(false);
                            }
                            groupSpinnerPoll.setEnabled(true);
                            adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
                            adapter.addAll(groupList);
                            groupSpinnerPoll.setAdapter(adapter, false, onSelectedListener);
                            return false;
                        }
                    });
                    groupSpinnerPoll.setHint("SELECT GROUP");

                }
            }
        });
    }

    private void savePoll(){
        PollBean pollBean = new PollBean();
        pollBean.setAction(PollActions.SAVE);
        setDataForApiCall(pollBean);
    }

    private void publishPoll(){
        PollBean pollBean = new PollBean();
        pollBean.setAction(PollActions.PUBLISH);
        setDataForApiCall(pollBean);
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
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                dateClosedDate = sdf.parse(strDate);
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
                    for (int k = 0; k < groupIDList.size(); k++) {
                        if (groupList.get(k).equals(grp)) {
                            selectedGroupId.add(groupIDList.get(k));
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

    private void apiCall(PollBean pollBean){
        MainActivity.dialogFragment = 1;
        groupSpinnerPoll.setText("SELECT GROUP ");
        new PostData(new Gson().toJson(pollBean), MainActivity.mainActivity, CallFor.POLL_OPERATION).execute();
    }

    private void showDatePicker(){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                CreateEditPollsFragment.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMinDate(now);
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth+" "+months[monthOfYear]+" "+year;
        selectedClosedDate.setVisibility(View.VISIBLE);
        selectedClosedDate.setText(date);
        int month = monthOfYear+1;
        strDate = year+"-"+month+"-"+dayOfMonth;
    }

    public static void ClearAllData(){
        pollName.setText("");
        pollQuestion.setText("");
        optionText.setText("");
        tvClose.setText("");
        tvClose.setVisibility(View.GONE);
        addedOptionsLayout.removeAllViews();
        dialog.dismiss();
    }
    private MultiSpinner.MultiSpinnerListener onSelectedListener =
            new MultiSpinner.MultiSpinnerListener() {
                public void onItemsSelected(boolean[] selected) {
                    if (groupIDList.size() == 0) {
                        groupSpinnerPoll.setEnabled(false);
                        groupSpinnerPoll.setText("No Group Added Yet");
                    }else{
                        groupSpinnerPoll.setEnabled(true);
                        for(int j=0;j<selected.length;j++){
                            if(selected[j]){
                                group_name = groupSpinnerPoll.getAdapter().getItem(j).toString().trim();
                                boolean isSame = arrayOfGroup.contains(group_name.trim());
                                if(isSame){
                                    CommonMethods.showToastError(getContext(),"Already added group : "+group_name);//,Toast.LENGTH_SHORT).show();
                                }else{
                                    addItem(group_name.trim());
                                    if(j+1==groupList.size()){
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
    public void onGetResponse(String response, String callFor) {
        MessageResponceBean messageResponceBean = new Gson().fromJson(response, MessageResponceBean.class);
        if(callFor.equals(CallFor.GET_GROUPS)){
            if (messageResponceBean.getCode().equals("SUCCESS")){
                groupList.clear();
                groupIDList.clear();
                for(int i =0;i<messageResponceBean.getMessageGroupBeans().size();i++){
                    String groupName = messageResponceBean.getMessageGroupBeans().get(i).getGroup_name();
                    int groupId = messageResponceBean.getMessageGroupBeans().get(i).getGroup_id();
                    groupIDList.add(groupId);
                    groupList.add(groupName);
                }
                setAdapter();
            }
            else{
                CommonMethods.showToastError(MainActivity.mainActivity,"Error");//,Toast.LENGTH_LONG).show();
            }
        }
    }

    public void setAdapter(){
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
        adapter.addAll(groupList);
        groupSpinnerPoll.setAdapter(adapter, false, onSelectedListener);
    }

    public void addItem(String groupName){
        arrayOfGroup.add(groupName.trim());
        final RelativeLayout linearLayout = new RelativeLayout(context);
        counterHeadingGroup.setText("Added "+arrayOfGroup.size());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5, 5, 5, 5);
        linearLayout.setLayoutParams(lp);
//        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View closeView = vi.inflate(R.layout.close_btn, null);
        TextView tvOptionText=closeView.findViewById(R.id.name);

        final ImageView btn =  closeView.findViewById(R.id.btn_close);
        tvOptionText.setText(groupName.trim());

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

                } else {
                    linearGroupList.setVisibility(View.VISIBLE);
                    ((LinearLayout) linearLayout.getParent()).removeView(linearLayout);
                    arrayOfGroup.remove(tvOptionText.getText().toString().trim());
                }
                counterHeadingGroup.setText("Added "+arrayOfGroup.size());
            }
        });
    }


}