package vztrack.gls.com.vztrack_user.adapters;

/**
 * Created by sandeep on 14/3/16.
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.CustumView.CustomTextView;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.beans.ComplainsBean;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

public class Complain_RecyclerViewAdapter extends RecyclerView.Adapter<Complain_RecyclerViewAdapter.MyViewHolder> {

    ArrayList<ComplainsBean> complaintBeans;
    Context context;
    int complain_id;
    String strCategory;
    String strDescription;
    String strStatus;
    String strCloasedBy;
    String strDate, strClosedDate, strOwnerNameAndFlat;
    CheckConnection cc;
    public static Dialog dialog;

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView category, date ,createdBy;
        public TextView description;
        //        public FancyButton btnClose;
        public CustomTextView btnClose;
        public TextView btnDisableClose, tvClosedDate;
        //        public View viewColorBar;
        public LinearLayout layoutClosedByAndDate;
        public CardView card_view;


        public MyViewHolder(View view) {
            super(view);
            category = (TextView) view.findViewById(R.id.tvCategory);
            description = (TextView) view.findViewById(R.id.description);
            date = (TextView) view.findViewById(R.id.tvDate);
            tvClosedDate = (TextView) view.findViewById(R.id.tvClosedDate);
            createdBy = (TextView) view.findViewById(R.id.createdBy);
            btnClose = view.findViewById(R.id.btnCloseButton);
            btnDisableClose = (TextView) view.findViewById(R.id.btnCloseDisableButton);
//            viewColorBar = (View) view.findViewById(R.id.viewColorBar);
            layoutClosedByAndDate = (LinearLayout) view.findViewById(R.id.layoutClosedByAndDate);
            card_view = (CardView) view.findViewById(R.id.card_view);
        }
    }

    public Complain_RecyclerViewAdapter(Context con,  ArrayList<ComplainsBean> complaintBeans) {
        this.complaintBeans = complaintBeans;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_complaint, parent, false);
        cc = new CheckConnection(context);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        strCategory = complaintBeans.get(position).getCategory();
        strDescription = complaintBeans.get(position).getDescription().replaceAll("\n+","\n");
        strDate = complaintBeans.get(position).getCreated_date();
        strStatus = complaintBeans.get(position).getStatus();
        strCloasedBy = complaintBeans.get(position).getClosed_by();
        strClosedDate = complaintBeans.get(position).getModify_date();
//        strComment = complaintBeans.get(position).getComment();
        strOwnerNameAndFlat = complaintBeans.get(position).getFlatNo()+" : "+ complaintBeans.get(position).getOwnerName();

        if(strStatus.equals("CLOSE")){
            holder.btnClose.setVisibility(View.GONE);
            holder.layoutClosedByAndDate.setVisibility(View.VISIBLE);
            holder.btnDisableClose.setTextColor(context.getResources().getColor(R.color.red));
            holder.tvClosedDate.setTextColor(context.getResources().getColor(R.color.red));
            holder.btnDisableClose.setText("Closed By : "+strCloasedBy+" on ");
            holder.tvClosedDate.setText(strClosedDate);
//            holder.viewColorBar.setBackgroundColor(Color.parseColor("#ff4d4d"));
        } else  if(strStatus.equals("REOPEN")){
            holder.btnClose.setVisibility(View.VISIBLE);
            holder.btnClose.setText(complaintBeans.get(position).getStatus());
            holder.layoutClosedByAndDate.setVisibility(View.VISIBLE);
            holder.btnDisableClose.setTextColor(context.getResources().getColor(R.color.callcolor));
            holder.tvClosedDate.setTextColor(context.getResources().getColor(R.color.callcolor));
            holder.btnDisableClose.setText("Reopened By : "+complaintBeans.get(position).getReopenName()+" on ");
            holder.tvClosedDate.setText(complaintBeans.get(position).getReopenDate());
//            holder.viewColorBar.setBackgroundColor(Color.parseColor("#ff4d4d"));
        }
        else{
            holder.layoutClosedByAndDate.setVisibility(View.GONE);
            holder.btnClose.setVisibility(View.VISIBLE);
            holder.btnClose.setText(complaintBeans.get(position).getStatus());
//            holder.viewColorBar.setBackgroundColor(Color.parseColor("#1F96F2"));
        }

        holder.category.setText(strCategory);
        holder.description.setText(strDescription);
        holder.date.setText(strDate);
        holder.btnClose.setTag(position);
        holder.card_view.setTag(position);

        if( SheredPref.getAdminAccess(context)){
            holder.createdBy.setVisibility(View.VISIBLE);
            holder.createdBy.setText("Posted by : "+strOwnerNameAndFlat);
        }

        int intPos= position+1;
        int intPage= MainActivity.pageNo+1;
        int intmodVal = intPage*10;
        if((intPos)%intmodVal==0){
            MainActivity.pageNo++;
            String extendedUrl = "?pageNo="+MainActivity.pageNo+"&newVersion=true";
            new GetData(MainActivity.mainActivity, CallFor.GET_COMPLAIN, extendedUrl).execute();
        }
        if(cc.isConnectingToInternet()){
            holder.btnClose.setOnClickListener(clickListener);
            holder.card_view.setOnClickListener(clickListener);
        }
        else{
            CommonMethods.showToastError(context, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int pos= (int) view.getTag();
            complain_id = complaintBeans.get(pos).getVz_comp_id();
            gotoNextActivity(complain_id, context);
        }
    };

    private void gotoNextActivity(int complain_id, Context context) {
        String extendedUrl = "?soc_complain_id="+complain_id;
        new GetData(MainActivity.mainActivity, CallFor.GET_COMPLAIN_DETAILS,""+extendedUrl).execute();
    }

    @Override
    public int getItemCount() {
        return complaintBeans.size();
    }




}

//package vztrack.gls.com.vztrack_user.adapters;
//
///**
// * Created by sandeep on 14/3/16.
// */
//
//import android.app.Dialog;
//import android.content.Context;
//import android.graphics.Color;
//import androidx.cardview.widget.CardView;
//import androidx.recyclerview.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//
//
//import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
//import vztrack.gls.com.vztrack_user.CustumView.CustomTextView;
//import vztrack.gls.com.vztrack_user.activity.MainActivity;
//import vztrack.gls.com.vztrack_user.R;
//import vztrack.gls.com.vztrack_user.beans.ComplainsBean;
//import vztrack.gls.com.vztrack_user.utils.CallFor;
//import vztrack.gls.com.vztrack_user.utils.CheckConnection;
//import vztrack.gls.com.vztrack_user.utils.GetData;
//import vztrack.gls.com.vztrack_user.utils.SheredPref;
//
//public class Complain_RecyclerViewAdapter extends RecyclerView.Adapter<Complain_RecyclerViewAdapter.MyViewHolder> {
//
//    ArrayList<ComplainsBean> complaintBeans;
//    Context context;
//    int complain_id;
//    String strCategory;
//    String strDescription;
//    String strStatus;
//    String strCloasedBy;
//    String strDate, strClosedDate, strOwnerNameAndFlat;
//    CheckConnection cc;
//    public static Dialog dialog;
//
//    public class MyViewHolder extends RecyclerView.ViewHolder  {
//        public TextView category, date ,createdBy;
//        public TextView description;
////        public FancyButton btnClose;
//        public CustomTextView btnClose;
//        public TextView btnDisableClose, tvClosedDate;
////        public View viewColorBar;
//        public LinearLayout layoutClosedByAndDate;
//        public CardView card_view;
//
//
//        public MyViewHolder(View view) {
//            super(view);
//            category = (TextView) view.findViewById(R.id.tvCategory);
//            description = (TextView) view.findViewById(R.id.description);
//            date = (TextView) view.findViewById(R.id.tvDate);
//            tvClosedDate = (TextView) view.findViewById(R.id.tvClosedDate);
//            createdBy = (TextView) view.findViewById(R.id.createdBy);
//            btnClose = view.findViewById(R.id.btnCloseButton);
//            btnDisableClose = (TextView) view.findViewById(R.id.btnCloseDisableButton);
////            viewColorBar = (View) view.findViewById(R.id.viewColorBar);
//            layoutClosedByAndDate = (LinearLayout) view.findViewById(R.id.layoutClosedByAndDate);
//            card_view = (CardView) view.findViewById(R.id.card_view);
//        }
//    }
//
//    public Complain_RecyclerViewAdapter(Context con,  ArrayList<ComplainsBean> complaintBeans) {
//        this.complaintBeans = complaintBeans;
//        this.context = con;
//    }
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.card_complaint, parent, false);
//        cc = new CheckConnection(context);
//        return new MyViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, int position) {
//        strCategory = complaintBeans.get(position).getCategory();
//        strDescription = complaintBeans.get(position).getDescription().replaceAll("\n+","\n");
//        strDate = complaintBeans.get(position).getCreated_date();
//        strStatus = complaintBeans.get(position).getStatus();
//        strCloasedBy = complaintBeans.get(position).getClosed_by();
//        strClosedDate = complaintBeans.get(position).getModify_date();
////        strComment = complaintBeans.get(position).getComment();
//        strOwnerNameAndFlat = complaintBeans.get(position).getFlatNo()+" : "+ complaintBeans.get(position).getOwnerName();
//
//        if(strStatus.equals("CLOSE")){
//            holder.btnClose.setVisibility(View.GONE);
//            holder.layoutClosedByAndDate.setVisibility(View.VISIBLE);
//            holder.btnDisableClose.setTextColor(context.getResources().getColor(R.color.red));
//            holder.tvClosedDate.setTextColor(context.getResources().getColor(R.color.red));
//            holder.btnDisableClose.setText("Closed By : "+strCloasedBy+" on ");
//            holder.tvClosedDate.setText(strClosedDate);
////            holder.viewColorBar.setBackgroundColor(Color.parseColor("#ff4d4d"));
//        } else  if(strStatus.equals("REOPEN")){
//            holder.btnClose.setVisibility(View.VISIBLE);
//            holder.btnClose.setText(complaintBeans.get(position).getStatus());
//            holder.layoutClosedByAndDate.setVisibility(View.VISIBLE);
//            holder.btnDisableClose.setTextColor(context.getResources().getColor(R.color.callcolor));
//            holder.tvClosedDate.setTextColor(context.getResources().getColor(R.color.callcolor));
//            holder.btnDisableClose.setText("Reopened By : "+complaintBeans.get(position).getReopenName()+" on ");
//            holder.tvClosedDate.setText(complaintBeans.get(position).getReopenDate());
////            holder.viewColorBar.setBackgroundColor(Color.parseColor("#ff4d4d"));
//        }
//        else{
//            holder.layoutClosedByAndDate.setVisibility(View.GONE);
//            holder.btnClose.setVisibility(View.VISIBLE);
//            holder.btnClose.setText(complaintBeans.get(position).getStatus());
////            holder.viewColorBar.setBackgroundColor(Color.parseColor("#1F96F2"));
//        }
//
//        holder.category.setText(strCategory);
//        holder.description.setText(strDescription);
//        holder.date.setText(strDate);
//        holder.btnClose.setTag(position);
//        holder.card_view.setTag(position);
//
//        if( SheredPref.getAdminAccess(context)){
//            holder.createdBy.setVisibility(View.VISIBLE);
//            holder.createdBy.setText("Posted by : "+strOwnerNameAndFlat);
//        }
//
//        int intPos= position+1;
//        int intPage= MainActivity.pageNo+1;
//        int intmodVal = intPage*10;
//        if((intPos)%intmodVal==0){
//            MainActivity.pageNo++;
//            String extendedUrl = "?pageNo="+MainActivity.pageNo+"&newVersion=true";
//            new GetData(MainActivity.mainActivity, CallFor.GET_COMPLAIN, extendedUrl).execute();
//        }
//        if(cc.isConnectingToInternet()){
//            holder.btnClose.setOnClickListener(clickListener);
//            holder.card_view.setOnClickListener(clickListener);
//        }
//        else{
//            CommonMethods.showToastError(context, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
//        }
//    }
//
//    View.OnClickListener clickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            int pos= (int) view.getTag();
//            complain_id = complaintBeans.get(pos).getVz_comp_id();
//            gotoNextActivity(complain_id, context);
//        }
//    };
//
//    private void gotoNextActivity(int complain_id, Context context) {
//        String extendedUrl = "?soc_complain_id="+complain_id;
//        new GetData(MainActivity.mainActivity, CallFor.GET_COMPLAIN_DETAILS,""+extendedUrl).execute();
//    }
//
//    @Override
//    public int getItemCount() {
//        return complaintBeans.size();
//    }
//
//
//
//
//}