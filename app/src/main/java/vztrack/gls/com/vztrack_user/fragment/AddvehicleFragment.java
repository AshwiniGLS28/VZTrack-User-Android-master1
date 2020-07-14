package vztrack.gls.com.vztrack_user.fragment;

/**
 * Created by sandeep on 14/3/16.
 */

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import io.ghyeok.stickyswitch.widget.StickySwitch;
import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.CustumView.CustomButton;
import vztrack.gls.com.vztrack_user.activity.BaseActivity;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

import static android.content.ContentValues.TAG;

public class AddvehicleFragment extends Fragment {
    BaseActivity context;
    CheckConnection cc;
    StickySwitch mSwitch;
    boolean switchState;
    CustomButton btnAddButton;
    int vehicleType;
    public static EditText vehicleNumber;
    public static TextView no_Data;
    public static ListView vehicle_List;
    public static ListAdapter mAdapter;
    public static LinearLayout noImageDataLayout;

    public AddvehicleFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_add_vehicle, null);
        mSwitch = (StickySwitch) root.findViewById(R.id.vehicleTypeSwitch);
        btnAddButton =  root.findViewById(R.id.btnAddButton);
        vehicleNumber = (EditText) root.findViewById(R.id.vehicleNumber);
        no_Data = (TextView) root.findViewById(R.id.no_Data);
        noImageDataLayout=root.findViewById(R.id.noImageDataLayout);
        vehicle_List = (ListView) root.findViewById(R.id.vehicle_List);
        cc = new CheckConnection(getActivity());
        vehicleType =2;
        if(cc.isConnectingToInternet()){
            new GetData(MainActivity.mainActivity, CallFor.GET_VEHICLES,"").execute();
            //getFlatVehicles
        }else{
            CommonMethods.showToastError(context, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
        }

        mSwitch.setOnSelectedChangeListener((direction, text) -> {
            Log.d(TAG, "Now Selected : " + direction.name() + ", Current Text : " + text);
            if(direction.name().equals("LEFT")){
                vehicleType =2;
            }else{
                vehicleType =4;
            }
        });

        btnAddButton.setOnClickListener(view -> {
            String strVehicleNumber = vehicleNumber.getText().toString().trim();
            if(strVehicleNumber.equals("")){
                CommonMethods.showToastError(context, "Please enter vehicle number");//, Toast.LENGTH_SHORT, true).show();
            }else{
                boolean flag = false;
                String all_vehicle = SheredPref.getVehicleNo_for_add(context);
                all_vehicle = all_vehicle.replace(",","\n");
                String all_pattern = SheredPref.getVehiclePatternNo(context);
                String[] all_pattern_array = all_pattern.split("@");
                String all_flag[] = SheredPref.getVehicleFlag(context).split(",");

                for(int i=0;i<all_pattern_array.length;i++){
                    if(all_flag[i].trim().equals("0")){
                        String strPattern = all_pattern_array[i].trim();
                        Pattern pattern = Pattern.compile(strPattern);
                        Matcher matcher = pattern.matcher(strVehicleNumber);
                        if (matcher.find()) {
                            flag = true;
                        }
                    }
                }

                if(flag){
                    if(cc.isConnectingToInternet()){
                        CleverTap.cleverTap_Record_Event(getActivity(), Events.event_add_vehicle_button);
                        String strVehicleNo = vehicleNumber.getText().toString().trim().replaceAll("\\s+","%20");
                        String parm = "?vehicleType="+vehicleType+"&vehicleNumber="+strVehicleNo;
                        new GetData(MainActivity.mainActivity, CallFor.ADD_VEHICLE,""+parm).execute();
                    }else{
                        CommonMethods.showToastError(context, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                    }
                }else{
//                    SweetAlertDialog alertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
//                    alertDialog .setTitleText("Invalid Vehicle Number");
//                    alertDialog.setContentText("Please enter vehicle number in following format\n"+all_vehicle+"\n");
//                    alertDialog.show();

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                    LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View dialogView = inflater1.inflate(R.layout.custum_alert_dialog, null);
                    dialogBuilder.setView(dialogView);
                    TextView txtalertheading = dialogView.findViewById(R.id.txtalertheading);
                    TextView txtalertsubheading = dialogView.findViewById(R.id.txtalertsubheading);

                    TextView btnegative = dialogView.findViewById(R.id.btnegative);
                    TextView btnpositive = dialogView.findViewById(R.id.btnpositive);

                    txtalertheading.setText("Invalid Vehicle Number");
                    txtalertsubheading.setText("Please enter vehicle number in following format \n"+all_vehicle+"\n");

                    btnegative.setVisibility(View.GONE);

                    final AlertDialog b = dialogBuilder.create();
                    b.setCanceledOnTouchOutside(false);
                    b.setCancelable(false);
                    b.show();

                    btnpositive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            b.dismiss();

                        }
                    });
                    btnegative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            b.dismiss();
                        }
                    });
                }
            }
        });


        context = MainActivity.mainActivity;
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }
}