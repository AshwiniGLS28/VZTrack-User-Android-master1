package vztrack.gls.com.vztrack_user.adapters;

/**
 * Created by sandeep on 14/3/16.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.CustumView.CustomButton;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.beans.SearchVehicleBean;
import vztrack.gls.com.vztrack_user.fragment.SearchVehicleFragment;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.PostData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

public class Search_Vehicles_RecyclerViewAdapter extends RecyclerView.Adapter<Search_Vehicles_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    private String strOwnerType;
    private String strFlatNoAndOwnerName;
    private String strVehicleTypeAndTime, strVehicleNumber;
    private String strFlatNumber;
    private boolean iconValue;
    CheckConnection cc;
    Dialog dialog;
    private ArrayList<SearchVehicleBean> searchVehicleBean;
    boolean type;

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView tvFlatNoAndOwnerName,txtparkingType,txtparkingNo,txtparkinglevel,txtparkingstickerNo,txtVehicleType;
        public TextView  tvVehicleNumber,tvVehicleTypeAndTime;
//        public ImageView imgType;
        public CustomButton btnSend;

        public MyViewHolder(View view) {
            super(view);
            tvFlatNoAndOwnerName = (TextView) view.findViewById(R.id.tvFlatNo_OwnerName);
            tvVehicleTypeAndTime = (TextView) view.findViewById(R.id.tvVehicleType_Time);
            tvVehicleNumber = (TextView) view.findViewById(R.id.tvVehicleNumber);
            txtVehicleType=view.findViewById(R.id.txtVehicleType);
//            imgType = (ImageView) view.findViewById(R.id.imgType);
            btnSend =  view.findViewById(R.id.btnSend);
            txtparkingstickerNo=view.findViewById(R.id.txtparkingstickerNo);
            txtparkinglevel=view.findViewById(R.id.txtparkinglevel);
            txtparkingNo=view.findViewById(R.id.txtparkingNo);
            txtparkingType=view.findViewById(R.id.txtparkingType);
        }
    }

    public Search_Vehicles_RecyclerViewAdapter(Context con, ArrayList<SearchVehicleBean> searchVehicleBeen) {
        this.searchVehicleBean = searchVehicleBeen;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_search_vehicle_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        cc = new CheckConnection(context);
        strOwnerType = searchVehicleBean.get(position).getOwnerType();
        strFlatNoAndOwnerName = searchVehicleBean.get(position).getFlatNo_OR_name();
        strVehicleTypeAndTime = searchVehicleBean.get(position).getTime_OR_vehicleType();
        strVehicleNumber = searchVehicleBean.get(position).getVehicleNumber();
        strFlatNumber = searchVehicleBean.get(position).getFlatNumber();


        iconValue = searchVehicleBean.get(position).isNotificationIconFlag();
//        SearchVehicleFragment.card_view.setVisibility(View.VISIBLE);
        type = SheredPref.getType(context);
        // If type true then this is Company
        if(strOwnerType.equals("Visitor")){
            if(type){
                holder.tvFlatNoAndOwnerName.setText("Employee Id. : "+strFlatNoAndOwnerName);
            }else{
                holder.tvFlatNoAndOwnerName.setText("House No. : "+strFlatNoAndOwnerName);
            }
            holder.tvVehicleTypeAndTime.setText("Visited Date And Time : "+strVehicleTypeAndTime);
            holder.txtVehicleType.setVisibility(View.GONE);
            holder.txtparkinglevel.setVisibility(View.GONE);
            holder.txtparkingstickerNo.setVisibility(View.GONE);
            holder.txtparkingNo.setVisibility(View.GONE);
            holder.txtparkingType.setVisibility(View.GONE);
        }else{
            holder.txtparkinglevel.setVisibility(View.VISIBLE);
            holder.txtparkingstickerNo.setVisibility(View.VISIBLE);
            holder.txtparkingNo.setVisibility(View.VISIBLE);
            holder.txtparkingType.setVisibility(View.VISIBLE);
            if(type){
                holder.tvFlatNoAndOwnerName.setText("Employee Id. : "+strFlatNoAndOwnerName);
            }else{
                holder.tvFlatNoAndOwnerName.setText("House No. : "+strFlatNoAndOwnerName);
            }
            holder.tvVehicleTypeAndTime.setVisibility(View.GONE);

            holder.txtVehicleType.setVisibility(View.VISIBLE);
            if(strVehicleTypeAndTime.equals("2")){
//                holder.imgType.setBackgroundResource(R.drawable.ic_action_bike);
                holder.txtVehicleType.setText("Vehicle Type : Two Wheeler");
            }else{
                holder.txtVehicleType.setText("Vehicle Type : Four Wheeler");
//                holder.imgType.setBackgroundResource(R.drawable.ic_action_car);
            }


            boolean typeSoc = SheredPref.getType(context);
//            if (searchVehicleBean.get(position).getParkingType().equalsIgnoreCase("not added"))
            if (searchVehicleBean.get(position).getParkingType()!=null && !searchVehicleBean.get(position).getParkingType().equalsIgnoreCase("not added"))
            holder.txtparkinglevel.setText("Parking Level : "+searchVehicleBean.get(position).getParkingLevel());
           else
                holder.txtparkinglevel.setText("Parking Level : NA");
//            else
//                holder.txtparkinglevel.setVisibility(View.GONE);

            if (searchVehicleBean.get(position).getParkingStickerNo()!=null&& !searchVehicleBean.get(position).getParkingStickerNo().equalsIgnoreCase("not added"))
            holder.txtparkingstickerNo.setText("Parking Sticker No : "+searchVehicleBean.get(position).getParkingStickerNo());
            else
                holder.txtparkingstickerNo.setText("Parking Sticker No. : NA");
//                holder.txtparkingstickerNo.setVisibility(View.GONE);

            if (searchVehicleBean.get(position).getParkingNo()!=null&& !searchVehicleBean.get(position).getParkingNo().equalsIgnoreCase("not added"))
            holder.txtparkingNo.setText("Parking No. : "+searchVehicleBean.get(position).getParkingNo());
            else
                holder.txtparkingNo.setText("Parking No. : NA");;

            if (searchVehicleBean.get(position).getParkingType()!=null&& !searchVehicleBean.get(position).getParkingType().equalsIgnoreCase("not added"))
            holder.txtparkingType.setText("Parking Type : "+searchVehicleBean.get(position).getParkingType());
            else
                holder.txtparkingType.setText("Parking Type : NA");
//            if (dataModel.getVehicleType().equals("2")) {
            // If type true then this is Company
        }
        holder.tvVehicleNumber.setText("Vehicle Number : "+strVehicleNumber);
        if(iconValue){
            holder.btnSend.setVisibility(View.VISIBLE);
        }else{
            holder.btnSend.setVisibility(View.GONE);
        }
        holder.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cc.isConnectingToInternet()) {
                    // Create custom dialog object
                    dialog = new Dialog(context);
                    dialog.setCancelable(true);

                    // Include dialog.xml file
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_send_vehicle_notification);
                    final EditText description = (EditText) dialog.findViewById(R.id.description);
                    final TextView counter = (TextView) dialog.findViewById(R.id.tvCounterDesc);
                    CustomButton fancyButton =  dialog.findViewById(R.id.btn_Send_Notification);
                    final TextView flatNumberAndName = (TextView) dialog.findViewById(R.id.flatNumber);

                    final String flat = searchVehicleBean.get(position).getFlatNumber();
                    final String vehicle = searchVehicleBean.get(position).getVehicleNumber();
                    final String flatAndName = searchVehicleBean.get(position).getFlatNo_OR_name();



                    if(type){
                        flatNumberAndName.setText("Employee Id : "+flatAndName);
                    }else{
                        flatNumberAndName.setText("House No : "+flatAndName);
                    }


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
                            counter.setText(editable.toString().length() + " / max 100");
                        }
                    });

                    fancyButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final String messageText = description.getText().toString();
                            if(description.getText().toString().trim().equals("")){
                                CommonMethods.showToastError(context, "Message should not be blank");//, Toast.LENGTH_SHORT, true).show();
                            }else {
                                CleverTap.cleverTap_Record_Event(context, Events.event_send_vehicle_notification_button);
                                SearchVehicleBean searchVehicleBean = new SearchVehicleBean();
                                searchVehicleBean.setVehicleNumber(vehicle);
                                searchVehicleBean.setFlatNumber(flat);
                                searchVehicleBean.setOwnerType(messageText);   // OwnerType is description
                                new PostData(new Gson().toJson(searchVehicleBean), MainActivity.mainActivity, CallFor.SEND_VEHICLE_MESSAGE_NOTIFICATION).execute();
                                dialog.hide();
                            }
                        }
                    });

                    Window window = dialog.getWindow();
                    window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
                                dialog.dismiss();
                            return false;
                        }
                    });
                }else{
                    CommonMethods.showToastError(context, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchVehicleBean.size();
    }

}