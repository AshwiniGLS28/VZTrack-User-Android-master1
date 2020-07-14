package vztrack.gls.com.vztrack_user.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.CustumView.CustomTextView;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.adapters.InvitationContactAdapter;
import vztrack.gls.com.vztrack_user.adapters.Preapproval.CustomAdapterForPreapproval;
import vztrack.gls.com.vztrack_user.adapters.Preapproval.MyAdapter;
import vztrack.gls.com.vztrack_user.beans.ContactBean;
import vztrack.gls.com.vztrack_user.beans.InvitationBean;
import vztrack.gls.com.vztrack_user.beans.PreApprovalBean.PreApprovalPurpose;
import vztrack.gls.com.vztrack_user.beans.ResponceBean;
import vztrack.gls.com.vztrack_user.responce.InvitationResponse;
import vztrack.gls.com.vztrack_user.responce.PurposeResponse;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.PostData;

import static vztrack.gls.com.vztrack_user.fragment.ApprovalListFragment.getCallFroDelete;

public class InvitationActivity extends BaseActivity {

    public static final int REQUEST_READ_CONTACTS = 79;
    public static final int REQUEST_CONTACT = 0;
    LinearLayout popup;
    EditText newFirstName, newLastName, newMobile, additionalInfo;
    TextView addInfoTextCount, invitationDate, addedContactCount, createInvBtn, invPurposeText, addInfoText, addInfoLabel;
    Spinner invPurposeSpinner;
//    View addInfoLine;
    Calendar myCalendar = Calendar.getInstance();
    public static ArrayList<ContactBean> contacts;
    public static ArrayList<ContactBean> selectedContacts;
    InvitationContactAdapter adapter;
    ListView contactList;
    String callFor;
    Button pupupBtn;
    TextView titleactivity;
    ImageView backpress;
    String[] months;
    RelativeLayout addInfoLL;
    Context context;
    String datetoserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);
        titleactivity=findViewById(R.id.title);
        context=InvitationActivity.this;
        months = getResources().getStringArray(R.array.months);
        backpress=findViewById(R.id.backpress);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Add New Pre Approval");
        titleactivity.setText("Create New Invitation");
        backpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        popup = findViewById(R.id.popup);
        pupupBtn = findViewById(R.id.pupupBtn);
        createInvBtn = findViewById(R.id.createInvBtn);
        invPurposeText = findViewById(R.id.invPurposeText);
        invPurposeSpinner = findViewById(R.id.invPurposeSpinner);
        newFirstName = findViewById(R.id.newContactFirstName);
        newLastName = findViewById(R.id.newContactLastName);
        newMobile = findViewById(R.id.newContactMobile);
        additionalInfo = findViewById(R.id.addInfo);
        additionalInfo.addTextChangedListener(watcher);
        addInfoTextCount = findViewById(R.id.addInfoTextCount);
        invitationDate = findViewById(R.id.invitationDate);
        addedContactCount = findViewById(R.id.addedContactCount);
        addInfoText = findViewById(R.id.addInfoText);
//        addInfoLine = findViewById(R.id.addInfoLine);
        addInfoLL = findViewById(R.id.addInfoLL);
        contactList = findViewById(R.id.contactList);
        addInfoLabel = findViewById(R.id.addInfoLabel);
        contacts = new ArrayList<>();
        adapter = new InvitationContactAdapter(this, R.layout.invitation_contact, contacts);
        contactList.setAdapter(adapter);
        callFor = getIntent().getStringExtra("CALL_FOR");
        if(!callFor.equals("NEW")){
            titleactivity.setText("Invitation Details");
            pupupBtn.setVisibility(View.GONE);
            createInvBtn.setVisibility(View.GONE);
            invPurposeText.setVisibility(View.VISIBLE);
            invPurposeSpinner.setVisibility(View.GONE);
            addInfoTextCount.setVisibility(View.GONE);
            additionalInfo.setVisibility(View.GONE);
            addInfoLL.setVisibility(View.GONE);
            String purpose = getIntent().getStringExtra("PURPOSE");
            String date  = getIntent().getStringExtra("DATE");
            int id = getIntent().getIntExtra("ID",0);
            String addInfo = getIntent().getStringExtra("ADD_INFO");
            if(addInfo!=null && !addInfo.equals("")){
                addInfoLL.setVisibility(View.VISIBLE);
                addInfoText.setVisibility(View.VISIBLE);
                addInfoText.setText(addInfo);
                addInfoLabel.setVisibility(View.VISIBLE);
            } else {
                addInfoLL.setVisibility(View.GONE);
                addInfoText.setText(" ");
                addInfoText.setVisibility(View.GONE);
//                addInfoLine.setVisibility(View.GONE);
            }
            invPurposeText.setText(purpose);
            invitationDate.setText(date);
//            getSupportActionBar().setTitle("Invitation Detail");
            new GetData(this, CallFor.INVITATION_DETAIL, "?invitationId="+id).execute();
        } else {
            newMobile.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        addContact(null);
                        return true;
                    }
                    return false;
                }
            });
            new GetData(this, CallFor.INVITATION_PURPOSE, "").execute();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }
        return true;
    }



    public void openPopup(View v){
        popup.setVisibility(View.VISIBLE);
        createInvBtn.setVisibility(View.GONE);
        newFirstName.requestFocus();
    }

    public void hidePopup(View v){
        popup.setVisibility(View.GONE);
        createInvBtn.setVisibility(View.VISIBLE);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void addContact(View v){
        if(newFirstName.getText().toString().trim().length() == 0){
            CommonMethods.showToastSuccess(getApplicationContext(),"Please enter name.");
//            Toasty.info(getApplicationContext(), "Please enter name.", Toast.LENGTH_SHORT, true).show();
            newFirstName.requestFocus();
            return;
        }

        if(newMobile.getText().toString().trim().length() == 0 || newMobile.getText().toString().trim().length() !=10){
            CommonMethods.showToastSuccess(getApplicationContext(),"ease enter valid mobile no.");
//            Toasty.info(getApplicationContext(), "Please enter valid mobile no.", Toast.LENGTH_SHORT, true).show();
            newMobile.requestFocus();
            return;
        }

        if(isAdded(newMobile.getText().toString().trim())){
            CommonMethods.showToastSuccess(getApplicationContext(),"Contact already added.");
//            Toasty.info(getApplicationContext(), "Contact already added.", Toast.LENGTH_SHORT, true).show();
            newMobile.requestFocus();
            return;
        }

        ContactBean contactBean = new ContactBean();
        contactBean.setContactName(newFirstName.getText().toString().trim());
        if(!newLastName.getText().toString().trim().equals("")){
            contactBean.setContactName(contactBean.getContactName()+" "+newLastName.getText().toString().trim());
        }
        contactBean.setMobileNo(newMobile.getText().toString().trim());
        contacts.add(contactBean);
        newFirstName.setText("");
        newLastName.setText("");
        newMobile.setText("");
        dataChanged();
        hidePopup(addedContactCount);
    }

    public void openDatePicker(View v){
        if(!callFor.equals("NEW")){
            return;
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, datePicker, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener datePicker = (view, year, monthOfYear, dayOfMonth) ->{
        datetoserver=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
        String date=convertTo2D(dayOfMonth) + " " + months[monthOfYear] + " " + String.valueOf(year).substring(2);
        updateDateLabel(date);
} ;

    public void updateDateLabel(String date){
        invitationDate.setText(date);
    }
    public String convertTo2D(int num){
        return String.format("%02d", num);

    }
    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.e("TEXT : ",s.toString());
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int length = s.toString().length();
            if(length <= 50) {
                addInfoTextCount.setText(s.toString().length() + "/50");
            } else {
                s.delete(50,length);
            }
        }
    };

    public void chooseFromContact(View v){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            getContacts();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContacts();
                } else {
                    CommonMethods.showToastSuccess(getApplicationContext(), "Permission denied.");

//                    Toasty.info(getApplicationContext(), "Permission denied.", Toast.LENGTH_SHORT, true).show();
                }
                return;
            }

        }
    }

    public void getContacts() {
        hidePopup(addedContactCount);
        Intent intent = new Intent(getApplicationContext(), ContactsActivity.class);
        startActivityForResult(intent, REQUEST_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data1111) {
        if(requestCode == REQUEST_CONTACT){
            if(resultCode == RESULT_OK){
                for(ContactBean contactBean: selectedContacts){
                    if(!isAdded(contactBean.getMobileNo())){
                        this.contacts.add(contactBean);
                    }
                }
                selectedContacts = null;
                dataChanged();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(popup.getVisibility() == View.VISIBLE){
            hidePopup(addedContactCount);
        } else {
            super.onBackPressed();
        }
    }

    public void dataChanged(){
        addedContactCount.setText(contacts.size()+"");
        adapter.notifyDataSetChanged();
    }

    public boolean isAdded(String mobileNo){
        boolean added = false;
        for(ContactBean contact : contacts){
            if(contact.getMobileNo().equals(mobileNo)){
                added = true;
                break;
            }
        }
        return added;
    }

    @Override
    public void onGetResponse(String response, String callFor) {
        if(callFor.equals(CallFor.INVITATION_PURPOSE)){
            PurposeResponse purposeResponse = new Gson().fromJson(response, PurposeResponse.class);
            if(purposeResponse.getCode().equals("SUCCESS")){
                setPurposeSpinner(purposeResponse.getInvitePurposeList());
            } else {
                CommonMethods.showToastSuccess(getApplicationContext(), "Something went wrong.");
//                Toasty.info(getApplicationContext(), "Something went wrong.", Toast.LENGTH_SHORT, true).show();
                onBackPressed();
            }
        }

        if(callFor.equals(CallFor.INVITATION_DETAIL)){
            InvitationResponse invitation = new Gson().fromJson(response, InvitationResponse.class);
            if(invitation.getCode().equals("SUCCESS")){
                contacts.addAll(invitation.getInvitatonDetails().getContactgroup());
                adapter.notifyDataSetChanged();
                addedContactCount.setText(contacts.size()+"");
            }
        }

        if(callFor.equals(CallFor.SAVE_INVITATION)){
            ResponceBean responseBean = new Gson().fromJson(response, ResponceBean.class);
            if(responseBean.getCode().equals("SUCCESS")){
                CommonMethods.showToastSuccess(getApplicationContext(), "Invitation Sent.");
//                Toasty.info(getApplicationContext(), "Invitation Sent.", Toast.LENGTH_SHORT, true).show();
                onBackPressed();
            } else {
                CommonMethods.showToastSuccess(getApplicationContext(), "Something went wrong.");
//                Toasty.info(getApplicationContext(), "Something went wrong.", Toast.LENGTH_SHORT, true).show();
            }
        }
    }

//    public void setPurposeSpinner(ArrayList<String> purposes){
//        purposes.add(0,"Select Type");
//        invPurposeSpinner.setAdapter(new ArrayAdapter<String>(this, R.layout.preapprovalspinner,purposes));
//    }

    public void sendInvitation(View v){
        if(invPurposeSpinner.getSelectedItemPosition() == 0){
            CommonMethods.showToastSuccess(getApplicationContext(), "Please select purpose.");
//            Toasty.info(getApplicationContext(), "Please select purpose.", Toast.LENGTH_SHORT, true).show();
            invPurposeSpinner.requestFocus();
            return;
        }

        if(invitationDate.getText().toString().equals("Choose Date")){
            CommonMethods.showToastSuccess(getApplicationContext(), "Please Choose Date.");
//            Toasty.info(getApplicationContext(), "Please Choose Date.", Toast.LENGTH_SHORT, true).show();
            invitationDate.performClick();
            return;
        }

        if(contacts.size() == 0){
            CommonMethods.showToastSuccess(getApplicationContext(),  "No contacts in invitation.");
//            Toasty.info(getApplicationContext(), "No contacts in invitation.", Toast.LENGTH_SHORT, true).show();
            return;
        }

        InvitationBean invitationBean = new InvitationBean();
        invitationBean.setInvitedDate(datetoserver+"");
        invitationBean.setPurpose(invPurposeSpinner.getSelectedItem().toString());
        invitationBean.setDescription(additionalInfo.getText().toString());
        invitationBean.setContactgroup(contacts);
//        AlertDialog.Builder confirm= new AlertDialog.Builder(this)
//                .setTitle("Send Invitation?")
//                .setMessage("Are you sure to send an invitation?")
//                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        new PostData(new Gson().toJson(invitationBean),InvitationActivity.this, CallFor.SAVE_INVITATION).execute();
//                    }
//                })
//                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//        confirm.show();

        //New Pop up alert
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.custum_alert_dialog, null);
        dialogBuilder.setView(dialogView);
        TextView txtalertheading = dialogView.findViewById(R.id.txtalertheading);
        TextView txtalertsubheading = dialogView.findViewById(R.id.txtalertsubheading);

        TextView btnegative = dialogView.findViewById(R.id.btnegative);
        TextView btnpositive = dialogView.findViewById(R.id.btnpositive);

        txtalertheading.setText("Send Invitation?");
        txtalertsubheading.setText("Are you sure to send an invitation?");

        btnegative.setVisibility(View.VISIBLE);


        final android.app.AlertDialog b = dialogBuilder.create();
        b.setCanceledOnTouchOutside(false);
        b.setCancelable(false);
        b.show();

        btnpositive.setText("Yes");
        btnegative.setText("No");
        btnpositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
                new PostData(new Gson().toJson(invitationBean),InvitationActivity.this, CallFor.SAVE_INVITATION).execute();
            }
        });
        btnegative.setOnClickListener(v1 -> b.dismiss());
        //end of alert

    }

    private void setPurposeSpinner(ArrayList<String> preApprovalPurposeBean) {
        preApprovalPurposeBean.add(0,"Select Type");
        MyAdapter adapter1=new MyAdapter(this, R.layout.preapprovalspinner,preApprovalPurposeBean);
        invPurposeSpinner.setAdapter(adapter1);// =adapter
//        invPurposeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                spinnerid= Integer.parseInt(view.getTag().toString());
////                Log.e("positionofspinnerfrompreviousdata",positionofspinnerfrompreviousdata+"--");
//
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }
}
