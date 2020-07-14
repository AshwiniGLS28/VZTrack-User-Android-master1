package vztrack.gls.com.vztrack_user.fragment;

/**
 * Created by sandeep on 14/3/16.
 */

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.CustumView.CustomButton;
import vztrack.gls.com.vztrack_user.CustumView.CustomTextView;
import vztrack.gls.com.vztrack_user.activity.BaseActivity;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

public class SearchVehicleFragment extends Fragment {

    BaseActivity context;
    CheckConnection cc;
    public static EditText vehicleNumber;
    CustomButton searchIcon;
    public static RecyclerView searched_vehicle_recycler_view;
    public static TextView noData;
    public static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
//    public static CardView card_view;
    public static TextView res;
   public static LinearLayout noImageDataLayout;
    public SearchVehicleFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_search_vehicle, null);
        context = MainActivity.mainActivity;

        vehicleNumber = (EditText) root.findViewById(R.id.vehicleNumber);
        searchIcon =  root.findViewById(R.id.searchIcon);
//        card_view = (CardView) root.findViewById(R.id.card_view);
        res = (TextView) root.findViewById(R.id.res);
        noImageDataLayout=root.findViewById(R.id.noImageDataLayout);
        res.setVisibility(View.GONE);
//        card_view.setVisibility(View.GONE);

        searched_vehicle_recycler_view = (RecyclerView) root.findViewById(R.id.searched_vehicle_recycler_view);
        noData = (TextView) root.findViewById(R.id.noResultLayout);

        cc = new CheckConnection(getActivity());

        String currentDateString = DateFormat.getDateInstance().format(new Date());
        if(SheredPref.getDateForVehiclePattern(context).equals("")){
            new GetData(MainActivity.mainActivity, CallFor.VEHICLENOPATTERN,"").execute();
            SheredPref.setDateForVehiclePattern(context,currentDateString);
        }else if(!currentDateString.equals(SheredPref.getDateForVehiclePattern(context))){
            new GetData(MainActivity.mainActivity, CallFor.VEHICLENOPATTERN,"").execute();
            SheredPref.setDateForVehiclePattern(context,currentDateString);
        }

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strVehicleNumber =  vehicleNumber.getText().toString().trim();
                if(strVehicleNumber.equals("")){
                    CommonMethods.showToastError(context, "Please enter vehicle number");//, Toast.LENGTH_SHORT, true).show();
                }else{
                    boolean flag = false;
                    String all_vehicle = SheredPref.getVehicleNo_for_search(context);
                    all_vehicle = all_vehicle.replace(",","\n");
                    String all_pattern = SheredPref.getVehiclePatternNo(context);
                    String[] all_pattern_array = all_pattern.split("@");
                    for(int i=0;i<all_pattern_array.length;i++){
                        String strPattern =all_pattern_array[i].trim();
                        Pattern pattern = Pattern.compile(strPattern);
                        Matcher matcher = pattern.matcher(strVehicleNumber);
                        if (matcher.find()) {
                            flag = true;
                        }
                    }

                    if(flag){
                        if(cc.isConnectingToInternet()){
                            CleverTap.cleverTap_Record_Event(getActivity(), Events.event_search_vehicle_button);
                            String strVehicleNo =  vehicleNumber.getText().toString().trim().replaceAll("\\s+","%20");
                            new GetData(MainActivity.mainActivity, CallFor.SEARCH_VEHICLE,""+strVehicleNo).execute();
                            res.setText("Searched result for vehicle number '"+vehicleNumber.getText().toString().trim()+"'");
                            vehicleNumber.setText("");
                        }else{
                            CommonMethods.showToastError(context, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                        }
                    }else{
//                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
//                                .setTitleText("Invalid Vehicle Number")
//                                .setContentText("Please enter vehicle number in following format\n"+all_vehicle)
//                                .show();

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View dialogView = inflater.inflate(R.layout.custom_invalid_vehicle_layout, null);
                        dialogBuilder.setView(dialogView);
                        TextView txtalertheading = dialogView.findViewById(R.id.txtalertheading);
                        TextView txtalertsubheading = dialogView.findViewById(R.id.txtalertsubheading);

                        TextView btnegative = dialogView.findViewById(R.id.btnegative);
                        TextView btnpositive = dialogView.findViewById(R.id.btnpositive);

                        txtalertheading.setText("Invalid Vehicle Number");
                        txtalertsubheading.setText("Please enter vehicle number in following format\n \n"+all_vehicle);

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
            }
        });
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }
}