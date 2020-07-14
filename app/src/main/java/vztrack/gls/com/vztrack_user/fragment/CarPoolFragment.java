package vztrack.gls.com.vztrack_user.fragment;

/**
 * Created by sandeep on 14/3/16.
 */

import android.app.Dialog;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.BaseActivity;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.adapters.Preapproval.MyAdapter;
import vztrack.gls.com.vztrack_user.adapters.ViewPagerAdapter;
import vztrack.gls.com.vztrack_user.beans.CarPoolBean;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.PostData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

public class CarPoolFragment extends Fragment implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    CheckConnection cc;

    //Fragments
    OfferRideFragment offerRideFragment;
    RequestRideFragment requestRideFragment;
    private FloatingActionButton fab;
    public static Dialog dialog;
    private AppCompatSpinner appCompatSpinner;
    private EditText startRideLocation, endRideLocation, seats, remark, tvMobileNo;
    private TextView dateAndTime, counter;
    private Button btnPublish;
    private ImageView imageCalender, back;
    String[] months;
    String actionName;
    String strSelectedValue;
    String strDateAndTimeForApi;
    String strDateAndTime;
    ArrayList<String> spinnerValues = new ArrayList();
    BaseActivity context = MainActivity.mainActivity;
    boolean dialogOpenFlag;


    public CarPoolFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_car_pool, null);
        setHasOptionsMenu(true);
        dialogOpenFlag = false;
        //Initializing viewPager
        viewPager = (ViewPager) root.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);
        actionName = "OFFER";
//        UtilityMethodsAndroid.setActionBarTitle(getActivity());
        String title= UtilityMethods.getTitleFromMenuJson(getResources().getString(R.string.carpool),context);
        Log.e("titlevisitor",title+"");
        ((MainActivity) context).getSupportActionBar().setTitle(title);
        //Initializing the tablayout
        tabLayout = (TabLayout) root.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSaveFromParentEnabled(false);
        setupViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageSelected(int position) {
                if(position==1){
                    actionName = "REQUEST";
                    new GetData(MainActivity.mainActivity, CallFor.GET_CAR_POOL_REQUEST, "").execute();
                }else if(position==0){
                    actionName = "OFFER";
                    new GetData(MainActivity.mainActivity, CallFor.GET_CAR_POOL_OFFERS, "").execute();
                }
            }
        });

        months = getResources().getStringArray(R.array.months);
        cc = new CheckConnection(getActivity());
        fab = (FloatingActionButton) root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cc.isConnectingToInternet()) {
                    dialogOpenFlag = true;
                    TextView selectedDateAndTime;

                    dialog = new Dialog(getActivity());//, android.R.style.Theme_Translucent_NoTitleBar
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_offer_request_ride);
                    Window window = dialog.getWindow();
                    Spinner spinner=dialog.findViewById(R.id.spinner);
                    selectedDateAndTime=dialog.findViewById(R.id.selectedDateAndTime);
                    WindowManager.LayoutParams wlp = window.getAttributes();
                    wlp.gravity = Gravity.CENTER;
                    wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
                    window.setAttributes(wlp);
                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    initializeDialogComponant(dialog);

                    // set max len of phone number edit text
                    UtilityMethodsAndroid.setEditTextMaxLength(SheredPref.getMaxMobileLen(context), tvMobileNo);

                    spinnerValues.clear();
                    spinnerValues.add("Request A Ride");
                    spinnerValues.add("Offer A Ride");



                    if(actionName.equals("OFFER")){
                        Collections.reverse(spinnerValues);
                    }
                    MyAdapter adapter1=new MyAdapter(context, R.layout.preapprovalspinner,spinnerValues);
                    spinner.setAdapter(adapter1);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
//                            position = pos;
//                            selectedItem = adapterView.getItemAtPosition(pos).toString();
                            strSelectedValue = adapterView.getItemAtPosition(pos).toString();
                        }

                        public void onNothingSelected(AdapterView<?> adapterView) {
                            return;
                        }
                    });

                    counter.setText("0 / Max 300");

                    remark.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            counter.setText("Max 300 / " + editable.length());
                        }
                    });

//                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(dialog.getContext(), android.R.layout.simple_spinner_item, spinnerValues);
//                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                    appCompatSpinner.setAdapter(adapter);
//
//                    appCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
//                            strSelectedValue = adapterView.getItemAtPosition(pos).toString();
//                        }
//
//                        public void onNothingSelected(AdapterView<?> adapterView) {
//                            return;
//                        }
//                    });

                    imageCalender.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openDateAndTimePicker();
                        }
                    });
                    selectedDateAndTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openDateAndTimePicker();
                        }
                    });
                    dateAndTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openDateAndTimePicker();
                        }
                    });

                    btnPublish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String strSourceLocation = startRideLocation.getText().toString().trim();
                            String strDestinationLocation = endRideLocation.getText().toString().trim();
                            String strSeats = seats.getText().toString().trim();
                            String strRemark = remark.getText().toString().trim();
                            String strDateAndTime = dateAndTime.getText().toString();

                            String strMobileNo = tvMobileNo.getText().toString();
                            int mobileNoMinLen = SheredPref.getMinMobileLen(context);
                            int mobileNoMaxLen = SheredPref.getMaxMobileLen(context);

                            if(!cc.isConnectingToInternet()){
                                CommonMethods.showToastError(getActivity(),"Please Check Internet Connection");//.show();
                            }else if(strSourceLocation.equals("")){
                                CommonMethods.showToastError(getActivity(),"Enter Source Location");//.show();
                            }else if(strDestinationLocation.equals("")){
                                CommonMethods.showToastError(getActivity(),"Enter Destination Location");//.show();
                            }else if(strSeats.equals("")){
                                CommonMethods.showToastError(getActivity(),"Enter Number Of Seats");//.show();
                            }else if(strSeats.equals("0")){
                                CommonMethods.showToastError(getActivity(),"Please Enter Valid Number Of Seats");//.show();
                            }else if(strMobileNo.length()!=0 && !UtilityMethods.between(strMobileNo.length(), mobileNoMaxLen, mobileNoMinLen)){
                                CommonMethods.showToastError(getActivity(),"Invalid Mobile Number");//.show();
                            }else if(strDateAndTime.equals("")){
                                CommonMethods.showToastError(getActivity(),"Select Date And Time");//.show();
                            }else if(strRemark.equals("")){
                                CommonMethods.showToastError(getActivity(),"Enter Remark");//.show();
                            }else{
                                CarPoolBean carPoolBean = new CarPoolBean();
                                carPoolBean.setSource(strSourceLocation);
                                carPoolBean.setDestination(strDestinationLocation);
                                carPoolBean.setNoOfSeatsAvailable(Integer.parseInt(strSeats));
                                carPoolBean.setTripDate(strDateAndTimeForApi);
                                carPoolBean.setRemark(strRemark);
                                carPoolBean.setMobileNo(strMobileNo);
                                carPoolBean.setFlatNo(SheredPref.getFlat_No(getActivity()));
                                if(strSelectedValue.equalsIgnoreCase("offer a ride")){
                                    CleverTap.cleverTap_Record_Event(context, Events.car_pool_publish_offer);
                                    new PostData(new Gson().toJson(carPoolBean), context, CallFor.ADD_CAR_POOL_OFFER).execute();
                                }else{
                                    CleverTap.cleverTap_Record_Event(context, Events.car_pool_publish_request);
                                    new PostData(new Gson().toJson(carPoolBean), context, CallFor.ADD_CAR_POOL_REQUEST).execute();
                                }
                            }
                        }
                    });

                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    // Prevent to open keyBoard
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    dialog.show();
                } else {
                    CommonMethods.showToastError(getActivity(), "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        return root;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        offerRideFragment = new OfferRideFragment();
        requestRideFragment = new RequestRideFragment();
        adapter.addFragment(offerRideFragment, "Offer A Ride");
        adapter.addFragment(requestRideFragment, "Request A Ride");
        viewPager.setAdapter(adapter);
        if(MainActivity.carPoolFragmentName.equalsIgnoreCase("request")){
            actionName = "REQUEST";
            new GetData(MainActivity.mainActivity, CallFor.GET_CAR_POOL_REQUEST, "").execute();
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }else{
            actionName = "OFFER";
            new GetData(MainActivity.mainActivity, CallFor.GET_CAR_POOL_OFFERS, "").execute();
            viewPager.setCurrentItem(viewPager.getCurrentItem());
        }
        MainActivity.carPoolFragmentName = "";
    }

    private void initializeDialogComponant(Dialog dialog) {
        appCompatSpinner = (AppCompatSpinner) dialog.findViewById(R.id.spinner);
        startRideLocation = (EditText) dialog.findViewById(R.id.fromLocation);
        endRideLocation = (EditText) dialog.findViewById(R.id.destinationLocation);
        seats = (EditText) dialog.findViewById(R.id.numberOfseats);
        remark = (EditText) dialog.findViewById(R.id.remark);
        dateAndTime = (TextView) dialog.findViewById(R.id.selectedDateAndTime);
        counter = (TextView) dialog.findViewById(R.id.textCounter);
        btnPublish =  dialog.findViewById(R.id.btnPublish);
        imageCalender = (ImageView) dialog.findViewById(R.id.imgCalender);
        back = (ImageView) dialog.findViewById(R.id.imgBack);
        tvMobileNo = (EditText) dialog.findViewById(R.id.tvMobileNo);
    }

    private void openDateAndTimePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                CarPoolFragment.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setMinDate(now);
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    private void showTimePicker(int dayOfMonth) {
        Calendar now = Calendar.getInstance();
        int hours = now.get(Calendar.HOUR_OF_DAY);
        int minutes = now.get(Calendar.MINUTE);
        int seconds = now.get(Calendar.SECOND);
        if(minutes+30>=59){
            hours++;
        }
        minutes = minutes + 30;
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                CarPoolFragment.this,
                hours,
                minutes,
                false
        );
        if(dayOfMonth == now.get(Calendar.DAY_OF_MONTH)){
            tpd.setMinTime(hours, minutes, seconds);
        }
        tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = monthOfYear + 1;
        strDateAndTimeForApi = year+"-"+month+"-"+dayOfMonth;
        strDateAndTime = dayOfMonth + " " + months[monthOfYear] + " " + String.valueOf(year).substring(2);
        showTimePicker(dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String strTime =  updateTime(hourOfDay ,minute);
        strDateAndTime = strDateAndTime + " at " + strTime;
        strDateAndTimeForApi = strDateAndTimeForApi+" "+hourOfDay+":"+minute+":"+second;
        dateAndTime.setText(strDateAndTime);
        dateAndTime.setVisibility(View.VISIBLE);
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

    public static void ClearAllData(){
        dialog.dismiss();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(dialogOpenFlag == true){
            dateAndTime.setText("");
        }
    }
}