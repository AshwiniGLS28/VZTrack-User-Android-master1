package vztrack.gls.com.vztrack_user.adapters;

/**
 * Created by sandeep on 14/3/16.
 */

import android.content.Context;
import android.content.Intent;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.CustumView.CustomTextView;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.Visiters_Details;
import vztrack.gls.com.vztrack_user.beans.DataObjectVisitList;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

public class VisitList_RecyclerViewAdapter extends RecyclerView.Adapter<VisitList_RecyclerViewAdapter.DataObjectHolder> {
    private static String LOG_TAG = "AdapterVisitors";
    private ArrayList<DataObjectVisitList> mDataset;
    private Context context;
    public String strName,strMobile,strDateTime,strOutTime , strPurpose,strFrom,strVisitors,strVehicleNo, strBadgeNo;
    public int isapprovalshown=0;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvName;
        public TextView tvVisitors;
        public TextView tvPurpose;
        public TextView tvdateTime;
        public TextView tvFrom;
        public TextView tvVehicleno;
        public TextView tvOutTime;
        public ImageView imgProfilePic;
        public CardView cardView;
        public View line;
        public LinearLayout linearLayout;
//        public LinearLayout cardview_Linear;
//        public LinearLayout cardview_LinearText;
//        public ImageView imgIncorrectLable;
        CustomTextView txincorrect;
        public DataObjectHolder(View itemView) {
            super(itemView);
            imgProfilePic = (ImageView)itemView.findViewById(R.id.circleView);
            tvName = (TextView) itemView.findViewById(R.id.txtcount);
            tvVisitors = (TextView) itemView.findViewById(R.id.tvVisitors);
            tvdateTime = (TextView) itemView.findViewById(R.id.tvDateAndTime);
            tvPurpose = (TextView) itemView.findViewById(R.id.tvPurpose);
            tvFrom = (TextView) itemView.findViewById(R.id.tvfrom);
            tvVehicleno = (TextView) itemView.findViewById(R.id.tvVehicleno);
            tvOutTime = (TextView) itemView.findViewById(R.id.tvOutTime);
            cardView = (CardView)itemView.findViewById(R.id.card_view);
            line = (View) itemView.findViewById(R.id.line);
//            cardview_Linear = (LinearLayout) itemView.findViewById(R.id.cardview_Linear);
            txincorrect=itemView.findViewById(R.id.txincorrect);
//            cardview_LinearText = (LinearLayout) itemView.findViewById(R.id.cardview_LinearText);
//            imgIncorrectLable = (ImageView) itemView.findViewById(R.id.imgIncorrectLable);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }


//    public VisitList_RecyclerViewAdapter(List<String> myDataset) {
//
//
//    }

    public VisitList_RecyclerViewAdapter(Context con, ArrayList<DataObjectVisitList> myDataset,int approvalshown) {
        context = con;
        mDataset = myDataset;
        isapprovalshown=approvalshown;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_visit_list, parent, false);
        CardView cv = (CardView) view.findViewById(R.id.card_view);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);

        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent I = new Intent(context, Visiters_Details.class);
                context.startActivity(I);

            }
        });

        return dataObjectHolder;

    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
       // holder.setIsRecyclable(false);
        strName = mDataset.get(position).getmWhomeToVisit();
        strDateTime = mDataset.get(position).getInTime();
        strOutTime = mDataset.get(position).getOutTime();
        strPurpose = mDataset.get(position).getmPurpose();
        strFrom = mDataset.get(position).getmFrom();
        strVehicleNo = mDataset.get(position).getmVehicle_no();
        strVisitors = mDataset.get(position).getmNoOfVisitors();
        strBadgeNo = mDataset.get(position).getmBadgeNumber();

        boolean type = SheredPref.getType(MainActivity.mainActivity);
        // If type true then this is Company
        if(type){
            holder.tvVisitors.setText("Badge No. : "+strBadgeNo);
        }
        else{
            holder.tvVisitors.setText("Total Visitors : "+strVisitors);
        }

        holder.tvdateTime.setText("In Time : "+strDateTime);
        try{
            if(strOutTime.equals("NO EXIT TIME") || strOutTime.trim().equals(null)){
                holder.tvOutTime.setVisibility(View.GONE);
                holder.line.setVisibility(View.GONE);
            }
            else{
                holder.tvOutTime.setVisibility(View.VISIBLE);
                holder.line.setVisibility(View.VISIBLE);
            }
        }catch (Exception ex){
        }


        holder.tvOutTime.setText("Out Time : "+strOutTime);
        holder.tvPurpose.setText(strPurpose);
        holder.tvFrom.setText("From : "+strFrom);
        holder.tvVehicleno.setText("Vehicle No. : "+strVehicleNo);

        if(mDataset.get(position).ismError_flag()){
//            holder.cardview_Linear.setAlpha(.5f);
            holder.txincorrect.setVisibility(View.VISIBLE);
//            holder.cardview_Linear.setBackgroundColor(Color.parseColor("#E8E8E8"));
            holder.cardView.setOnClickListener(null);
        }
        else{
//            holder.cardview_Linear.setAlpha(1f);
            holder.txincorrect.setVisibility(View.GONE);
//            holder.cardview_Linear.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.cardView.setOnClickListener(null);
        }
        holder.cardView.setOnClickListener(null);
//        Log.e("string",context.getResources().getString(R.string.approvedby)+"");
        if (SheredPref.getSocietyApproval(context)) {
            if (isapprovalshown==1)
            holder.tvName.setText("Approvals : " + mDataset.get(position).getApprovalCount() + "");
            else
                holder.tvName.setText(" ");
        }else
        {
            holder.tvName.setText(" ");
        }
    }

    public void addItem(DataObjectVisitList dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}