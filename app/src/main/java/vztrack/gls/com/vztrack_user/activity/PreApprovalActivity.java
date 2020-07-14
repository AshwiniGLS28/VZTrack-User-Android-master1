package vztrack.gls.com.vztrack_user.activity;

import android.net.Uri;
import android.os.Bundle;

import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;

import android.content.Context;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import com.google.gson.Gson;

import vztrack.gls.com.vztrack_user.adapters.Preapproval.CustomAdapterForPreapproval;
//import vztrack.gls.com.vztrack_user.adapters.Preapproval.CustumAdapter1;
//import vztrack.gls.com.vztrack_user.adapters.Preapproval.MyAdapter;
import vztrack.gls.com.vztrack_user.beans.ContactBean;
import vztrack.gls.com.vztrack_user.beans.PreApprovalBean.ApprovalBean;
import vztrack.gls.com.vztrack_user.beans.PreApprovalBean.PreApprovalPurpose;
import vztrack.gls.com.vztrack_user.beans.PreApprovalBean.PreApprovalPurposeBean;
import vztrack.gls.com.vztrack_user.beans.PreapprovalBean;
import vztrack.gls.com.vztrack_user.profile.UserBean;
import vztrack.gls.com.vztrack_user.responce.LoginResponse;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.PostData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static vztrack.gls.com.vztrack_user.activity.InvitationActivity.REQUEST_CONTACT;

public class PreApprovalActivity extends BaseActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    public static final int REQUEST_READ_CONTACTS = 79;
    public static ContactBean contactBean=new ContactBean();
    String strDateAndTimeForApi,strDateAndTimeForApiTo,strDateAndTimeForApi1;
    String strDateAndTime;
    ApprovalBean approvalbean;
    String visitorName;
    String Purpose;
    String mobno;
    String title;
    Context context;
    CheckConnection cc;
    int spinnerid;
    int fromedit;
    int Id;
    CustomAdapterForPreapproval adapter;
    Toolbar toolbar;
    RadioButton radiopurpose,radiomobilenumber;
    String[] months;
    Button txtto;
    Button txtfrom;
    EditText txtfromdate,txttodate;
    boolean isfrom=true;
    TextView btnsave;
    Spinner spinnerpurpose;
    EditText editmobno,editname;
    RelativeLayout llmobno;
    int positionofspinnerfrompreviousdata=0;
    int callfor=0;
    PreapprovalBean preapprovalBean;
    ArrayList<PreApprovalPurpose> preApprovalPurposeBean;
    TextView titleactivity;
    ImageView backpress;
//    RelativeLayout rltodate,rlfromdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_approval);
        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        context=this;
        cc = new CheckConnection(this);
        titleactivity=findViewById(R.id.title);
        backpress=findViewById(R.id.backpress);
//        rltodate=findViewById(R.id.rltodate);
//        rlfromdate=findViewById(R.id.rlfromdate);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Add New Pre Approval");
        titleactivity.setText("Add New Pre Approval");
        backpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent preaaaprovalactivity = getIntent();
        fromedit = preaaaprovalactivity.getIntExtra("foredit", 0);
        String approvalbean1 = preaaaprovalactivity.getStringExtra("preaaprovalbean");
        approvalbean=new Gson().fromJson(approvalbean1,ApprovalBean.class);
        visitorName = preaaaprovalactivity.getStringExtra("visitorName");
        Purpose = preaaaprovalactivity.getStringExtra("Purpose");
        mobno = preaaaprovalactivity.getStringExtra("mobno");
        radiopurpose=findViewById(R.id.radiopurpose);
        llmobno=findViewById(R.id.llmobno);
        radiomobilenumber=findViewById(R.id.radiomobilenumber);
        btnsave=findViewById(R.id.btnsave);
        spinnerpurpose=findViewById(R.id.spinnerpurpose);
        txtfrom=findViewById(R.id.txtfrom);
        txtfromdate=findViewById(R.id.txtfromdate);
        txtto=findViewById(R.id.txtto);
        txttodate=findViewById(R.id.txttodate);
        editmobno=findViewById(R.id.editmobno);
        editname=findViewById(R.id.editname);

        if (mobno!=null)
        {
            editmobno.setText(mobno);
            radioclickedMobile();
        }
        if (visitorName!=null)
        {
            editname.setText(visitorName+"");
        }
        radiopurpose.setOnClickListener(v->{
                radioclickedPurpose();
        });

        radiomobilenumber.setOnClickListener(v->{
                radioclickedMobile();
        });

        if(radiopurpose.isChecked())
        {
          new GetData(this, CallFor.GETPREAPPROVALPURPOSE, "").execute();
            callfor=0;
        }
        months = getResources().getStringArray(R.array.months);
        txtfrom.setOnClickListener(v -> {
            isfrom=true;
            openDateAndTimePicker() ;
        });
        txtfromdate.setOnClickListener(v -> {
            isfrom=true;
            openDateAndTimePicker() ;
        });
        txtto.setOnClickListener(v -> {
            isfrom=false;
            openDateAndTimePicker();
        });
        txttodate.setOnClickListener(v -> {
            isfrom=false;
            openDateAndTimePicker();
        });
        btnsave.setOnClickListener(v -> {
            try
            {

                preapprovalBean=new PreapprovalBean();
             Log.e("spinner id",spinnerid+"");
                if (spinnerid==1)
                {
                    CommonMethods.showToastSuccess(getApplicationContext(), "Please select purpose");//, Toast.LENGTH_SHORT, true).show();
                    return;
                }

                if (editname.getText()!=null)
                {

                    if (!editname.getText().toString().isEmpty())
                    {
                        if (editname.getText().toString().trim().isEmpty())
                        {
                            CommonMethods.showToastError(getApplicationContext(), "Please add name");//, Toast.LENGTH_SHORT, true).show();
                            return;
                        }
                        preapprovalBean.setVisitorName(editname.getText().toString()+"");
                    }

                }

                if (txtfrom.getText()!=null)
                {
                    if (txtfrom.getText().toString().isEmpty()||txtfrom.getText().toString().equalsIgnoreCase("from date"))
                    {
                        CommonMethods.showToastError(getApplicationContext(), "Please add from date");//, Toast.LENGTH_SHORT, true).show();
                        return;
                    }
                    else
                    {
                        preapprovalBean.setStartDate(strDateAndTimeForApi);
                    }

                }else
                {
                    CommonMethods.showToastError(getApplicationContext(), "Please add from date");//, Toast.LENGTH_SHORT, true).show();
                    return;
                }


                if (txtto.getText()!=null)
                {
                    if (txtto.getText().toString().isEmpty()||txtto.getText().toString().equalsIgnoreCase("to"))
                    {
                        CommonMethods.showToastError(getApplicationContext(), "Please add to date");//, Toast.LENGTH_SHORT, true).show();
                        return;
                    }
                    else
                    {
                        preapprovalBean.setEndDate(strDateAndTimeForApiTo);
                    }

                }else
                {
                    CommonMethods.showToastError(getApplicationContext(), "Please add to date");//, Toast.LENGTH_SHORT, true).show();
                    return;
                }


                if(radiopurpose.isChecked())
                {
                    preapprovalBean.setPurposeId(spinnerid);
                    if(fromedit==0)
                        preapprovalBean.setId(0);
                    else{
                        if (approvalbean!=null)
                        {
                            preapprovalBean.setId(approvalbean.getId());
                        }
                    }
                }else
                {
                    if(fromedit==1)
                        if (approvalbean!=null)
                        {
                            preapprovalBean.setId(approvalbean.getId());
                        }
                        else
                        if (approvalbean!=null)
                        {
                            preapprovalBean.setId(0);
                        }
                    if (editmobno.getText()!=null)
                    {
                        if (editmobno.getText().toString().isEmpty())
                        {
                            CommonMethods.showToastError(getApplicationContext(), "Please add mobile number");//, Toast.LENGTH_SHORT, true).show();
                            return;
                        }
                        if (editmobno.getText().toString().length()<10)
                        {
                            CommonMethods.showToastError(getApplicationContext(), "Please add valid mobile number");//, Toast.LENGTH_SHORT, true).show();
                            return;
                        }else
                        {
                            preapprovalBean.setMobileNo(editmobno.getText().toString());
                        }

                    }else
                    {
                        CommonMethods.showToastError(getApplicationContext(), "Please add mobile number");//, Toast.LENGTH_SHORT, true).show();
                        return;
                    }




                }
                Date startdate=null,enddate=null;
                startdate=getdConvertedDte(preapprovalBean.getStartDate());
                enddate=getdConvertedDte(preapprovalBean.getEndDate());

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String nedt=format.format(Calendar.getInstance().getTime());

                StringBuilder string = new StringBuilder(nedt);
                string.setCharAt(nedt.length()-1, '0');
                string.setCharAt(nedt.length()-2, '0');
                nedt=string.toString();
                Date systemDate=getdConvertedDte(nedt);
                if (systemDate.compareTo(startdate)>0)
                {
                    CommonMethods.showToastError(getApplicationContext(), "From date time exceeded current time!Please check");//, Toast.LENGTH_SHORT, true).show();
                    return;
                }
                if (startdate.compareTo(enddate)>0)
                {
                    CommonMethods.showToastError(getApplicationContext(), "From date is after to date! Please select valid date");//, Toast.LENGTH_SHORT, true).show();
                    return;
                }
                if (startdate.compareTo(enddate)==0)
                {
                    CommonMethods.showToastError(getApplicationContext(), "From date and to dates are equal! Please select different time.");//, Toast.LENGTH_SHORT, true).show();
                    return;
                }
                if(cc.isConnectingToInternet()){
                    new PostData(new Gson().toJson(preapprovalBean), PreApprovalActivity.this, CallFor.ADD_PREAPPROVAL).execute();
                    callfor = 1;
                }else
                {
                    CommonMethods.showToastError(getApplicationContext(), "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                    return;
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        });


        if (fromedit==1)
            showdata();

    }

    private Date getdConvertedDte(String datestr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date=null;
        try {

         date  = formatter.parse(datestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    private void showdata() {
        if (approvalbean!=null)
        {
            if (approvalbean.getPurposeName().equalsIgnoreCase("not added"))
            {
                radioclickedMobile();
                editmobno.setText(approvalbean.getMobileNo()+"");
                spinnerpurpose.setTag(approvalbean.getPurposeId());
            }else
            {
                radioclickedPurpose();
            }
            if (approvalbean.getVisitorName()!=null) {
                if (!approvalbean.getVisitorName().equalsIgnoreCase("not added"))
                editname.setText(approvalbean.getVisitorName() + "");
                else
                    editname.setText("");
            }

            String sd="";
            if (approvalbean.getStartDate()!=null)
            {
                String temp=approvalbean.getStartDate();
             sd=temp.substring(0,temp.length()-2);
                txtfromdate.setText(sd+"");
            }

            if (approvalbean.getEndDate()!=null)
            {
                String temp=approvalbean.getEndDate();
                sd=temp.substring(0,temp.length()-2);
                txttodate.setText(sd);
            }
            if (preApprovalPurposeBean!=null)
                spinnerpurpose.setAdapter(adapter);
            spinnerpurpose.setSelection(positionofspinnerfrompreviousdata);


            Date enddate=converttodate(approvalbean.getEndDate());

            Date startdate=converttodate(approvalbean.getStartDate());
            String starddt=approvalbean.getStartDate();
            String enddt=approvalbean.getEndDate();

            strDateAndTimeForApi= starddt.substring(0, starddt.length() - 2);
            strDateAndTimeForApiTo= enddt.substring(0, enddt.length() - 2);


            txtfromdate.setText(showDate(startdate)+"");
            txttodate.setText(showDate(enddate)+"");

        }
    }

    private int getpositionfromspinner(String purposeName) {
        int position=0;
        if (preApprovalPurposeBean!=null)
        {
          for (int i=0;i<preApprovalPurposeBean.size();i++)
          {
              if (purposeName.equalsIgnoreCase(preApprovalPurposeBean.get(i).getPurposeName()))
              {
               position=i;
              }
          }
        }
        spinnerpurpose.setTag(position);
        return position;
    }

    public void chooseFromContactForPreapproval( View view) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            getContacts();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
    }

    @Override
    public void onGetResponse(String response, String callFor) {
        if (callFor == CallFor.GETPREAPPROVALPURPOSE) {
            PreApprovalPurposeBean purposeResponse = new Gson().fromJson(response, PreApprovalPurposeBean.class);
            if (purposeResponse!=null)
            {
                if (purposeResponse.getCode().equalsIgnoreCase("SUCCESS")) {
                    PreApprovalPurpose preApprovalPurpose=new PreApprovalPurpose();
                    preApprovalPurpose.setPurposeId(1);
                    preApprovalPurpose.setPurposeName("Select Purpose");
                    preApprovalPurposeBean=new ArrayList<>();
                    preApprovalPurposeBean.add(preApprovalPurpose);
                    preApprovalPurposeBean.addAll(purposeResponse.getPreApprovalPurposeBean());
                    setPurposeSpinner(preApprovalPurposeBean);
                    if (approvalbean!=null) {
                        positionofspinnerfrompreviousdata = getpositionfromspinner(approvalbean.getPurposeName());
                        spinnerpurpose.setSelection(positionofspinnerfrompreviousdata);
                    }
                }else if (purposeResponse.getCode().equals("NEED_LOGIN")) {
                    UserBean userBean = new UserBean();
                    userBean.setUser_name(SheredPref.getUsername(getApplicationContext()));
                    userBean.setUser_password(SheredPref.getPassword(getApplicationContext()));
                    new PostData(new Gson().toJson(userBean), this, CallFor.LOGIN).execute();
                } else {
                    CommonMethods.showToastSuccess(getApplicationContext(), "Something went wrong");//, Toast.LENGTH_SHORT, true).show();
                    // onBackPressed()
                }


            }else
            {
                CommonMethods.showToastSuccess(getApplicationContext(), "Something went wrong");//, Toast.LENGTH_SHORT, true).show();
            }

        }
        if (callFor == CallFor.LOGIN) {
            LoginResponse  loginResponse = new Gson().fromJson(response, LoginResponse.class);
            if (loginResponse!=null)
            {
                if (loginResponse.getCode().equalsIgnoreCase("SUCCESS"))
                {
                    if (callfor==0)
                    {
                        new GetData(this, CallFor.GETPREAPPROVALPURPOSE, "").execute();
                        callfor=0;

                    }else if (callfor==1)
                    {
                        if (preapprovalBean!=null) {
                            new PostData(new Gson().toJson(preapprovalBean), PreApprovalActivity.this, CallFor.ADD_PREAPPROVAL).execute();
                            callfor = 1;
                        }else
                        {
                            CommonMethods.showToastError(getApplicationContext(), "Please fill all data");//, Toast.LENGTH_SHORT, true).show();
                            return;
                        }
                    }
                }
            }
        }
        if (callFor == CallFor.ADD_PREAPPROVAL) {
//            Log.e("response",response);
            PreApprovalPurposeBean preApprovalPurposeBean;
            preApprovalPurposeBean=new Gson().fromJson(response, PreApprovalPurposeBean.class);
            if (preApprovalPurposeBean!=null)
//                Log.e("code",preApprovalPurposeBean.getCode()+"-----");
            if (preApprovalPurposeBean.getCode().equalsIgnoreCase( "SUCCESS")) {
                CommonMethods.showToastSuccess(getApplicationContext(), "Added successfully");//, Toast.LENGTH_SHORT, true).show();
                Intent data = new Intent();
                String text = "Preapprovallist";
                data.setData(Uri.parse(text));
                setResult(RESULT_OK, data);
                finish();
//                onBackPressed();
            } else if (preApprovalPurposeBean.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                UserBean userBean = new UserBean();
                userBean.setUser_name(SheredPref.getUsername(getApplicationContext()));
                userBean.setUser_password(SheredPref.getPassword(getApplicationContext()));
                new PostData(new Gson().toJson(userBean), this, CallFor.LOGIN).execute();
            }else {
                CommonMethods.showToastSuccess(getApplicationContext(), "Something went wrong.");//, Toast.LENGTH_SHORT, true).show();
                // onBackPressed()
            }
        }

    }

    private void setPurposeSpinner(ArrayList<PreApprovalPurpose> preApprovalPurposeBean) {
        adapter=new CustomAdapterForPreapproval(this,preApprovalPurposeBean);
        spinnerpurpose.setAdapter(adapter);// =adapter
//        spinnerpurpose.setSelection(adapter.getPosition("Meeting"));
        spinnerpurpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerid= Integer.parseInt(view.getTag().toString());
//                Log.e("positionofspinnerfrompreviousdata",positionofspinnerfrompreviousdata+"--");


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void radioclickedMobile() {
        spinnerid=0;
        llmobno.setVisibility(View.VISIBLE);//=View.VISIBLE;
        spinnerpurpose.setVisibility(View.GONE);//=View.GONE;
        radiomobilenumber.setChecked(true);//;=true;
        radiopurpose.setChecked(false);//=false;
    }

    public void radioclickedPurpose() {
        llmobno.setVisibility(View.GONE);//=View.GONE
        spinnerpurpose.setVisibility(View.VISIBLE);//=View.VISIBLE
        radiomobilenumber.setChecked(false);//=false
        radiopurpose.setChecked(true);//=true
        if(preApprovalPurposeBean==null) {
            new GetData(this, CallFor.GETPREAPPROVALPURPOSE, "").execute();
            callfor = 0;
        }
        else {
            setPurposeSpinner(preApprovalPurposeBean);
            if (approvalbean!=null) {
                positionofspinnerfrompreviousdata = getpositionfromspinner(approvalbean.getPurposeName());
                spinnerpurpose.setSelection(positionofspinnerfrompreviousdata);
            }
        }
    }

    private void openDateAndTimePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                PreApprovalActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMinDate(now);
        dpd.show(this.getFragmentManager(), "Datepickerdialog");
    }

    private void showTimePicker(int dayOfMonth) {
        Calendar now = Calendar.getInstance();
        int hours = now.get(Calendar.HOUR_OF_DAY);
        int minutes = now.get(Calendar.MINUTE);
        int seconds = now.get(Calendar.SECOND);
//        if(minutes+30>=59){
//            hours++;
//        }
//        minutes = minutes ;//+ 30;
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                PreApprovalActivity.this,
                hours,
                minutes,
                false
        );
        if(dayOfMonth == now.get(Calendar.DAY_OF_MONTH)){
//            Log.e("day of month",dayOfMonth+"");
//            Log.e("minutes",minutes+"");
//            Log.e("hours",hours+"");
            tpd.setMinTime(hours, minutes, seconds);
        }
        tpd.show(this.getFragmentManager(), "Timepickerdialog");
    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = monthOfYear + 1;
        strDateAndTimeForApi1 = year+"-"+month+"-"+dayOfMonth;
        strDateAndTime = convertTo2D(dayOfMonth) + " " + months[monthOfYear] + " " + String.valueOf(year).substring(2);
        showTimePicker(dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String strTime =  updateTime(hourOfDay ,minute);
        strDateAndTime = strDateAndTime + " at " + strTime;

        if(isfrom) {
            strDateAndTimeForApi = strDateAndTimeForApi1+" "+convertTo2D(hourOfDay)+":"+convertTo2D(minute)+":"+convertTo2D(0);
            String text = "<font color=#cc0029>First Color</font> <font color=#ffcc00>Second Color</font>";
//            yourtextview.setText(Html.fromHtml(text));
            txtfromdate.setText(strDateAndTime);
        }else {
            strDateAndTimeForApiTo=strDateAndTimeForApi1+" "+convertTo2D(hourOfDay)+":"+convertTo2D(minute)+":"+convertTo2D(0);
            txttodate.setText(strDateAndTime);//=strDateAndTimeForApi
        }
//        dateAndTime.setText(strDateAndTime);
//        dateAndTime.setVisibility(View.VISIBLE);
    }

    // Used to convert 24hr format to 12hr format with AM/PM values
    private String updateTime(int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        String hrs = "";
        if (hours < 10)
            hrs = "0" + hours;
        else
            hrs = String.valueOf(hours);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hrs).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
        return aTime;
    }
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
                    CommonMethods.showToastSuccess(getApplicationContext(), "Permission denied.");//, Toast.LENGTH_SHORT, true).show();
                }
                return;
            }

        }
    }

    public void getContacts() {
//        hidePopup(addedContactCount);
        Intent intent = new Intent(getApplicationContext(), ContactsActivity.class);
        intent.putExtra("frompreapproval", 1);
        startActivityForResult(intent, REQUEST_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data1111) {
        if(requestCode == REQUEST_CONTACT){
            if(resultCode == RESULT_OK){
                try {
                    if (contactBean.getMobileNo() != null)
                        editmobno.setText(contactBean.getMobileNo());
                    editname.setText(contactBean.getContactName()+"");
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }


        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }
        return true;
    }
    public String convertTo2D(int num){
        return String.format("%02d", num);

    }

    // Used to convert 24hr format to 12hr format with AM/PM values
//    private String updateTime(int hours, int mins) {
//        String timeSet = "";
//        if (hours > 12) {
//            hours -= 12;
//            timeSet = "PM";
//        } else if (hours == 0) {
//            hours += 12;
//            timeSet = "AM";
//        } else if (hours == 12)
//            timeSet = "PM";
//        else
//            timeSet = "AM";
//
//        String minutes = "";
//        if (mins < 10)
//            minutes = "0" + mins;
//        else
//            minutes = String.valueOf(mins);
//
//        String hrs = "";
//        if (hours < 10)
//            hrs = "0" + hours;
//        else
//            hrs = String.valueOf(hours);
//
//        // Append in a StringBuilder
//        String aTime = new StringBuilder().append(hrs).append(':')
//                .append(minutes).append(" ").append(timeSet).toString();
//        return aTime;
//    }

    public String showDate(Date date)
    {
        int dayOfMonth=date.getDate();
        int monthOfYear=date.getMonth();
        int year=date.getYear();
//        Log.e("day",dayOfMonth+" ");
//        Log.e("monthOfYear",monthOfYear+" ");
//        Log.e("year",year+" ");
        String strDateAndTime = convertTo2D(dayOfMonth) + " " + months[monthOfYear] + " " + String.valueOf(year).substring(1);

        String strTime =  strDateAndTime+" at "+updateTime(date.getHours() ,date.getMinutes());
        return strTime;
    }

    public Date converttodate(String datestr)
    {
        Date date=null;
        if (datestr!=null &&!datestr.isEmpty()) {
            String sd = datestr.substring(0, datestr.length() - 2);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {

                date  = formatter.parse(sd);
                System.out.println(date);
                System.out.println(formatter.format(date));

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return date;
    }
}
