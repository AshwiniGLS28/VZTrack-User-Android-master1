package vztrack.gls.com.vztrack_user.activity;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.thomashaertel.widget.MultiSpinner;

import java.util.ArrayList;
import java.util.List;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.beans.FamilyListBean;
import vztrack.gls.com.vztrack_user.beans.FlatResponce;
import vztrack.gls.com.vztrack_user.beans.MessageBean;
import vztrack.gls.com.vztrack_user.beans.MessageResponceBean;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.PostData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

public class SendMessageActivity extends BaseActivity {
    Switch layoutSwitch;
    MultiSpinner groupSpinner;
    MultiAutoCompleteTextView flatNumberAutoComplite;
    EditText input_message;
    LinearLayout groupLayout;
    CheckConnection cc;
    private MessageResponceBean messageResponceBean;
    private FlatResponce flatResponce = new FlatResponce();
    List<String> groupList = new ArrayList<String>();
    List<Integer> groupIDList = new ArrayList<Integer>();
    List<String> flatList = new ArrayList<String>();
    List<Integer> loginIdList = new ArrayList<>();
    List<Integer> familyIdList = new ArrayList<Integer>();
    ArrayList<String> arrayOfFlats;
    ArrayList<Integer> arrayOfLoginId;
    LinearLayout ll;
    ArrayAdapter<String> adapter;
    TextView counterHeading;
    TextView counterDesc;
    HorizontalScrollView horizomtalScroll;
    String message;
//    TextView empORflatText;
    private boolean type;
    int familyId , loginId;
    String LoginIdSplit = null;
    FamilyListBean familyListBean;
    ArrayList<FamilyListBean> familyListBeans;
    String flatNumber,ownerName;
    int getloginId,loginIdForAdd;
    String selectedText;
    TextView titleactivity;
    ImageView backpress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().setTitle("Send Message");
        setContentView(R.layout.activity_send_message);
        titleactivity=findViewById(R.id.title);
        backpress=findViewById(R.id.backpress);
        titleactivity.setText("Send Message");
        backpress.setOnClickListener(v -> onBackPressed());
        layoutSwitch = (Switch) findViewById(R.id.layoutSwitch);
        flatNumberAutoComplite = (MultiAutoCompleteTextView) findViewById(R.id.flatNumberAutoComplite);
        input_message = (EditText) findViewById(R.id.input_message);
        groupLayout = (LinearLayout) findViewById(R.id.groupLayout);
        ll = (LinearLayout) findViewById(R.id.flatList);
        counterHeading = (TextView) findViewById(R.id.counterHeading);
        counterDesc = (TextView) findViewById(R.id.counterDesc);
//        empORflatText = (TextView) findViewById(R.id.empORflatText);
        horizomtalScroll = (HorizontalScrollView) findViewById(R.id.horizomtalScroll);
        cc = new CheckConnection(this);
        layoutSwitch.setChecked(false);

        arrayOfFlats = new ArrayList<String>();
        arrayOfLoginId = new ArrayList<Integer>();
        familyListBeans = new ArrayList<>();
        groupSpinner = (MultiSpinner) findViewById(R.id.groupSpinner);

        type = SheredPref.getType(this);
        // If type true then this is Company
        if(!type){
//            empORflatText.setText("House No.");
            flatNumberAutoComplite.setHint("Enter House Number");
        }
        else{
//            empORflatText.setText("Employee No.");
            flatNumberAutoComplite.setHint("Enter Employee Id.");
        }


        groupSpinner.setOnTouchListener((v, event) -> {
            if(groupList.size()==0){
                CommonMethods.showToastError(SendMessageActivity.this,"No groups added yet");//,Toast.LENGTH_LONG).show();
            }

            adapter = new ArrayAdapter<String>(SendMessageActivity.this, R.layout.purpose_spinner);
            adapter.addAll(groupList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            groupSpinner.setAdapter(adapter, false, onSelectedListener);
            return false;
        });

        groupSpinner.setHint(getResources().getString(R.string.selectgroup));

        input_message.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                counterDesc.setText(s.length()+" / max 3000");
            }
        });

        layoutSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    groupSpinner.setText(getResources().getString(R.string.selectgroup));
                    groupLayout.setBackgroundColor(Color.parseColor("#1A19396F"));
                    groupLayout.setAlpha(0.3f);
                    groupSpinner.setEnabled(false);
                    groupSpinner.setClickable(false);
                    flatNumberAutoComplite.setEnabled(false);
                    flatNumberAutoComplite.setClickable(false);
                    flatNumberAutoComplite.setText("");
                    input_message.setText("");
                    counterHeading.setText("Added 0");
                    ll.removeAllViews();
                }else{
                    groupLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    groupLayout.setAlpha(1);
                    groupSpinner.setEnabled(true);
                    groupSpinner.setClickable(true);
                    flatNumberAutoComplite.setEnabled(true);
                    flatNumberAutoComplite.setClickable(true);
                }
            }
        });

        if(cc.isConnectingToInternet()){
            new GetData(this, CallFor.GET_GROUPS, "").execute();
        }else{
            CommonMethods.showToastError(this,"Please Check Internet Connection");//,Toast.LENGTH_LONG).show();
        }

        flatNumberAutoComplite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String flatno = null;
                selectedText = (String) parent.getAdapter().getItem(position).toString();
                flatNumberAutoComplite.setText("");
                try {
                    flatno =selectedText.split("-")[0].trim();
                    for(int i = 0;i < flatList.size();i ++){

                        String FlatNo = selectedText.split("-")[0].trim();
                        String ownername = selectedText.split("-")[1].trim();
                        String flatNoOwnerName = FlatNo +"-"+ownername;

                        if(flatList.get(i).equals(flatNoOwnerName)){
                            getloginId = loginIdList.get(i);
                        }

                    }

                 } catch (Exception ex) {
                }
                boolean containLoginId = arrayOfLoginId.contains(getloginId);
                if(containLoginId){
                    CommonMethods.showToastError(SendMessageActivity.this,"Already Added : ");//+flatno,Toast.LENGTH_SHORT).show();
                }
                else{
                    addItem(flatno);
                }

            }
        });
    }

    public void addItem(String flatno){
            arrayOfFlats.add(flatno);
            arrayOfLoginId.add(getloginId);
            final LinearLayout linearLayout = new LinearLayout(this);
            counterHeading.setText("Added "+arrayOfFlats.size());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(5, 5, 5, 5);
            linearLayout.setLayoutParams(lp);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View closeView = vi.inflate(R.layout.close_btn, null);
            TextView name=closeView.findViewById(R.id.name);
            final ImageView btn =  closeView.findViewById(R.id.btn_close);
             name.setText(flatno.trim());

            linearLayout.addView(closeView);
            ll.addView(linearLayout);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ll.getChildCount() == 0) {
                        ll.setVisibility(View.GONE);
                        arrayOfFlats.clear();
                        ((LinearLayout) linearLayout.getParent()).removeView(linearLayout);
                        View v = linearLayout.getChildAt(0);
                        arrayOfFlats.remove(name.getText().toString().trim());
                    } else {
                        ll.setVisibility(View.VISIBLE);
                        ((LinearLayout) linearLayout.getParent()).removeView(linearLayout);
                        arrayOfFlats.remove(name.getText().toString().trim());
                    }
                    counterHeading.setText("Added "+arrayOfFlats.size());
                }
            });
    }

    public void Send(View v) {
        if (cc.isConnectingToInternet()) {

        CleverTap.cleverTap_Record_Event(this, Events.event_send_message_button);
        message = input_message.getText().toString().trim();
        if (message.equals("")) {
            CommonMethods.showToastError(this, "Message text should not be blank");//.show();
        } else {
            if (!layoutSwitch.isChecked()) {
                if (arrayOfFlats.size() <= 0) {
                    if(!type){
                        CommonMethods.showToastError(this, "Please add House or Group to send messsage");//.show();
                    }
                    else{
                        CommonMethods.showToastError(this, "Please add Employee Id or Group to send messsage");//.show();
                    }

                } else {
                    List<Integer> selectedFamilyId = new ArrayList<Integer>();
                    List<Integer> selectedGroupId = new ArrayList<Integer>();
                    for (int i = 0; i < arrayOfFlats.size(); i++) {
                        String listItem = arrayOfFlats.get(i).trim();
                        int listItemloginId = arrayOfLoginId.get(i);
                        if (listItem.contains("GROUP-")) {
                            String groupName = listItem.split("-")[1].trim();
                            for (int j = 0; j < groupList.size(); j++) {
                                if (groupName.equals(groupList.get(j).trim())) {
                                    int groupId = groupIDList.get(j);
                                    selectedGroupId.add(groupId);

                                }
                            }
                        }else {
                            for (int j = 0; j < flatList.size(); j++) {
                                if (listItemloginId == loginIdList.get(j)){
                                    int familyid = familyIdList.get(j);
                                    selectedFamilyId.add(familyid);
                                    familyListBean = new FamilyListBean();
                                    familyListBean.setFamilyId(familyid);
                                    familyListBean.setLoginId(loginIdList.get(j));
                                    familyListBeans.add(familyListBean);
                                }
                            }
                        }
                    }


                    int[] F_Ids = null;
                    int[] G_Ids = null;

                    for (int i = 0; i < selectedFamilyId.size(); i++) {
                        if (i == 0) {
                            F_Ids = new int[selectedFamilyId.size()];
                            F_Ids[i] = selectedFamilyId.get(i);
                        } else {
                            F_Ids[i] = selectedFamilyId.get(i);
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

                    // Send Message to group or flat
                    MessageBean messageBean = new MessageBean();
                    messageBean.setFamilyList(familyListBeans);
                    messageBean.setFamily_id(F_Ids);
                    messageBean.setGroupId(G_Ids);
                    messageBean.setMessage(message);
                    messageBean.setMessageToAll(false);
                    messageBean.setSocietyId(SheredPref.getSocietyId(this));
                    new PostData(new Gson().toJson(messageBean), this, CallFor.SEND_MESSAGE).execute();
                }
            } else {
                // Send Message to all
                MessageBean messageBean = new MessageBean();
                messageBean.setMessage(message);
                messageBean.setMessageToAll(true);
                messageBean.setSocietyId(SheredPref.getSocietyId(this));
                new PostData(new Gson().toJson(messageBean), this, CallFor.SEND_MESSAGE).execute();
            }
        }
        }else{
            CommonMethods.showToastSuccess(this, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private MultiSpinner.MultiSpinnerListener onSelectedListener =
            new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
//            if(groupIDList.size()==0){
//                groupSpinner.setText("No Group Added Yet");
//            }
            groupSpinner.setText(getResources().getString(R.string.selectgroup));
            for(int i=0;i<selected.length;i++){
                if(selected[i]){
                    String group_name = groupSpinner.getAdapter().getItem(i).toString();
                    boolean isSame = arrayOfFlats.contains("GROUP-"+group_name);
                    if(isSame){
                        CommonMethods.showToastError(SendMessageActivity.this,"Already added group : "+group_name);//,Toast.LENGTH_SHORT).show();
                    }else{
                        addItem("GROUP-"+group_name);
                        if(i+1==groupList.size()){
                                groupSpinner.setText(getResources().getString(R.string.selectgroup));
                        }
                    }
                }
            }
        }
    };

    @Override
    public void onGetResponse(String response, String callFor) {
        messageResponceBean = new Gson().fromJson(response,MessageResponceBean.class);
        if(callFor.equals(CallFor.GET_GROUPS)){
            if (messageResponceBean.getCode().equals("SUCCESS")){
                for(int i =0;i<messageResponceBean.getMessageGroupBeans().size();i++){
                    String groupName = messageResponceBean.getMessageGroupBeans().get(i).getGroup_name();
                    int groupId = messageResponceBean.getMessageGroupBeans().get(i).getGroup_id();
                    groupIDList.add(groupId);
                    groupList.add(groupName);
                }

                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
                adapter.addAll(groupList);
                groupSpinner.setAdapter(adapter, false, onSelectedListener);

                new GetData(this, CallFor.GET_FLATS, "").execute();
            }
            else{
                CommonMethods.showToastError(this,"Error");//,Toast.LENGTH_LONG).show();
            }
        }
        if(callFor.equals(CallFor.GET_FLATS)){
            if (messageResponceBean.getCode().equals("SUCCESS")){
                flatResponce = new Gson().fromJson(response,FlatResponce.class);
                for(int i =0;i<flatResponce.getAvailFlats().size();i++){
                    flatNumber = flatResponce.getAvailFlats().get(i).getFlatNo();
                    ownerName = flatResponce.getAvailFlats().get(i).getFlatOwnerName();
                    loginId = flatResponce.getAvailFlats().get(i).getLoginId();
                    familyId = flatResponce.getAvailFlats().get(i).getFamilyId();
                    familyIdList.add(familyId);
                    flatList.add(flatNumber+"-"+ownerName);
                    loginIdList.add(loginId);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        flatList);
                flatNumberAutoComplite.setThreshold(1);
                flatNumberAutoComplite.setAdapter(arrayAdapter);
                flatNumberAutoComplite.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
            }
        }
        if(callFor.equals(CallFor.SEND_MESSAGE)){
            if (messageResponceBean.getCode().contains("SUCCESS")){
                groupSpinner.setText(getResources().getString(R.string.selectgroup));
                flatNumberAutoComplite.setText("");
                input_message.setText("");
                counterHeading.setText("Added 0");
                ll.removeAllViews();
                arrayOfFlats.clear();
                CommonMethods.showToastSuccess(this, "Successfully Sent Message");//.show();
                String message_id = messageResponceBean.getCode().split("-")[1];
                MessageBean messageBean = new MessageBean();
                messageBean.setMessageId(Integer.parseInt(message_id));
                messageBean.setSocietyId(SheredPref.getSocietyId(this));
                if (!layoutSwitch.isChecked()) {
                    messageBean.setMessageToAll(false);
                }else{
                    messageBean.setMessageToAll(true);
                }
                messageBean.setMessage(message);
                SheredPref.setRun(this, "RUN");
                new PostData(new Gson().toJson(messageBean), this, CallFor.SEND_MESSAGE_NOTIFICATION).execute();
//                this.finish();

            }
            else{
                CommonMethods.showToastError(this, messageResponceBean.getMessage());//.show();
            }
        }
        if(callFor.equals(CallFor.SEND_MESSAGE_NOTIFICATION)){
            Log.e("SENT MSG ACT", "MESSAGE NOTIFICATION SENT");
            this.finish();

            //SheredPref.setRun(this, "RUN");
        }
    }
}
