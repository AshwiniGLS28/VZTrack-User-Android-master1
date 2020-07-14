package vztrack.gls.com.vztrack_user.adapters;

/**
 * Created by sandeep on 14/3/16.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.beans.CarPoolBean;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.GetData;

public class CarPoolRequest_RecyclerViewAdapter extends RecyclerView.Adapter<CarPoolRequest_RecyclerViewAdapter.MyViewHolder> {

    private ArrayList<CarPoolBean> carPoolBeans;
    Context context;
    int id;
    String strSourceLocation;
    String strDestinationLocation;
    int numnerOfSeats;
    String strDateTime;
    String strRemark, strMobileNo, strFlatNo;
    CheckConnection cc;
    boolean click;

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView tvSourceLocation, tvDestinationLocation, tvSeats,
                tvDateAndTime, tvRemark, header, tvFlatNo, tvMobileNo;
        public CardView cvPoolCar;
        public LinearLayout llMobileNo;
//        public ImageView imgCall;

        public MyViewHolder(View view) {
            super(view);
            tvSourceLocation = (TextView) view.findViewById(R.id.tvSource);
            tvDestinationLocation = (TextView) view.findViewById(R.id.tvDestination);
            tvSeats = (TextView) view.findViewById(R.id.tvSeats);
            tvDateAndTime = (TextView) view.findViewById(R.id.tvDateAndTime);
            tvRemark = (TextView) view.findViewById(R.id.tvRemark);
            header = (TextView) view.findViewById(R.id.header);
            cvPoolCar = (CardView) view.findViewById(R.id.cvPoolCar);
            tvFlatNo = (TextView) view.findViewById(R.id.tvFlatNo);
            tvMobileNo = (TextView) view.findViewById(R.id.tvMobileNo);
            llMobileNo = (LinearLayout) view.findViewById(R.id.llMobileNo);
//            imgCall = (ImageView) view.findViewById(R.id.imgCall);
        }
    }

    public CarPoolRequest_RecyclerViewAdapter(Context con, ArrayList<CarPoolBean> carPoolBeans) {
        this.carPoolBeans = carPoolBeans;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        cc = new CheckConnection(context);
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_car_pool, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        id = carPoolBeans.get(position).getId();
        if(id ==-1){
            holder.header.setVisibility(View.VISIBLE);
            holder.cvPoolCar.setVisibility(View.GONE);
            holder.header.setText("My Requests");
            click = true;
        }else if(id == -2){
            holder.header.setVisibility(View.VISIBLE);
            holder.cvPoolCar.setVisibility(View.GONE);
            holder.header.setText("Other Requests");
            click = false;
        }else{
            holder.header.setVisibility(View.GONE);
            holder.cvPoolCar.setVisibility(View.VISIBLE);
            strSourceLocation = carPoolBeans.get(position).getSource();
            strDestinationLocation = carPoolBeans.get(position).getDestination();
            numnerOfSeats = carPoolBeans.get(position).getNoOfSeatsAvailable();
            strDateTime = carPoolBeans.get(position).getTripDate();
            strRemark = carPoolBeans.get(position).getRemark();
            strMobileNo = carPoolBeans.get(position).getMobileNo();
            strFlatNo = carPoolBeans.get(position).getFlatNo();

            holder.tvSourceLocation.setText(strSourceLocation);
            holder.tvDestinationLocation.setText(strDestinationLocation);
            holder.tvSeats.setText(""+numnerOfSeats);
            holder.tvDateAndTime.setText(strDateTime);
            holder.tvRemark.setText(strRemark);
            holder.tvFlatNo.setText(strFlatNo);
            if(strMobileNo == null || strMobileNo.equals("")){
                holder.llMobileNo.setVisibility(View.GONE);
            }else{
                holder.llMobileNo.setVisibility(View.VISIBLE);
                holder.tvMobileNo.setText(strMobileNo);
            }
//            holder.imgCall.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Call(holder.tvMobileNo.getText().toString().trim());
//                }
//            });

            holder.tvMobileNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Call(holder.tvMobileNo.getText().toString().trim());
                }
            });
            if( click == true){
                holder.cvPoolCar.setTag(id);
                holder.cvPoolCar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final int id = (int)holder.cvPoolCar.getTag();

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View dialogView = inflater.inflate(R.layout.custum_alert_dialog, null);
                        dialogBuilder.setView(dialogView);
                        TextView txtalertheading = dialogView.findViewById(R.id.txtalertheading);
                        TextView txtalertsubheading = dialogView.findViewById(R.id.txtalertsubheading);

                        TextView btnegative = dialogView.findViewById(R.id.btnegative);
                        TextView btnpositive = dialogView.findViewById(R.id.btnpositive);

                        txtalertheading.setText("Close My Request");
                        txtalertsubheading.setText("Do you really want to close this request?");

                        btnpositive.setText("Yes");
                        btnegative.setText("No");

                        final AlertDialog b = dialogBuilder.create();
                        b.setCanceledOnTouchOutside(false);
                        b.setCancelable(false);
                        b.show();

                        btnpositive.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                b.dismiss();
                                if (cc.isConnectingToInternet()) {
                                    new GetData(MainActivity.mainActivity, CallFor.CLOSE_CAR_POOL_REQUEST, ""+id).execute();
                                    CleverTap.cleverTap_Record_Event(context, Events.car_pool_close_request);
                                }
                                else{
                                    CommonMethods.showToastSuccess(context, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                                }
                            }
                        });
                        btnegative.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                b.dismiss();
                            }
                        });

//                        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
//                                .setTitleText("Close My Request")
//                                .setContentText("Do you really want to close this request?")
//                                .setCancelText("Cancel")
//                                .setConfirmText("Ok")
//                                .showCancelButton(true)
//                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sDialog) {
//                                        sDialog.cancel();
//                                    }
//                                })
//                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                    @Override
//                                    public void onClick(SweetAlertDialog sDialog) {
//                                        sDialog.cancel();
//                                        if (cc.isConnectingToInternet()) {
//                                            new GetData(MainActivity.mainActivity, CallFor.CLOSE_CAR_POOL_REQUEST, ""+id).execute();
//                                            CleverTap.cleverTap_Record_Event(context, Events.car_pool_close_request);
//                                        }
//                                        else{
//                                            CommonMethods.showToastSuccess(context, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
//                                        }
//
//                                    }
//                                })
//                                .show();
                    }
                });
            }
        }
    }

    private void Call(String strMobileNo) {
        Uri number = Uri.parse("tel:" + strMobileNo);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        context.startActivity(callIntent);
    }

    @Override
    public int getItemCount() {
        return carPoolBeans.size();
    }

}