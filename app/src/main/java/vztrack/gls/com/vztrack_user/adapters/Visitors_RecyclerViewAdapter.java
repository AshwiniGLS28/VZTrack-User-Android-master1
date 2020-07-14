package vztrack.gls.com.vztrack_user.adapters;

/**
 * Created by sandeep on 14/3/16.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.CustumView.CustomTextView;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.PreApprovalActivity;
import vztrack.gls.com.vztrack_user.activity.Visiters_Details;
import vztrack.gls.com.vztrack_user.beans.DataObjectVisitors;
import vztrack.gls.com.vztrack_user.utils.AppData;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Constants;
import vztrack.gls.com.vztrack_user.utils.DbHelper;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.LoadImage;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

import static vztrack.gls.com.vztrack_user.activity.MainActivity.REQUEST_PREAPPROVAL;

public class Visitors_RecyclerViewAdapter extends RecyclerView.Adapter<Visitors_RecyclerViewAdapter.DataObjectHolder> {
    private static String LOG_TAG = "Visitors_RecyclerViewAdapter";
    private ArrayList<DataObjectVisitors> mDataset;
    private Context context;
    public String strName,strMobile,strDateTime,strPurpose,strFrom,strPhotUrl,strDocUrl, strTemperature, strMask;
    public boolean isMask;
    CheckConnection cc;
    int size;
    private final int TITLE = 0;
    private final int LOAD_MORE = 1;
    private boolean hasLoadButton = true;
    public static String photo[]=new String[10];
    DbHelper dbHelper;
    int isapprovalsown=1;
    boolean isThermalTemperatureActive;

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvdateTime;
        public TextView tvPurpose;
        public TextView tvMobile;
        public ImageView imgProfilePic;
        public CardView cardView;
        public ImageView imgMarkError;
        public Button btnLoadMore;
        public LinearLayout cardview_Linear,llapprovalbuttons;
        public CustomTextView txincorrect;
        public LinearLayout tvapprove;
        public LinearLayout tvdeny;
        public TextView tvapprovestatus;
        public LinearLayout lltvapprovestatus;
        public LinearLayout thermal;
        public TextView temperature;
        public TextView masked;

        public DataObjectHolder(View itemView) {
            super(itemView);
            imgProfilePic = (ImageView)itemView.findViewById(R.id.circleView);
            tvName = (TextView) itemView.findViewById(R.id.tvTitle);
            tvMobile = (TextView) itemView.findViewById(R.id.tvMobile);
            tvdateTime = (TextView) itemView.findViewById(R.id.tvDateTime);
            tvPurpose = (TextView) itemView.findViewById(R.id.tvPurpose);
            cardView = (CardView)itemView.findViewById(R.id.card_view);
            imgMarkError = (ImageView) itemView.findViewById(R.id.imgMarkError);
            btnLoadMore = (Button) itemView.findViewById(R.id.load_more);
            cardview_Linear = (LinearLayout) itemView.findViewById(R.id.cardview_Linear);
            txincorrect = itemView.findViewById(R.id.txincorrect);
            tvapprove=itemView.findViewById(R.id.tvapprove);
            tvdeny=itemView.findViewById(R.id.tvdeny);
            tvapprovestatus=itemView.findViewById(R.id.tvapprovestatus);
            lltvapprovestatus=itemView.findViewById(R.id.lltvapprovestatus);
            llapprovalbuttons=itemView.findViewById(R.id.llapprovalbuttons);
            thermal =  (LinearLayout) itemView.findViewById(R.id.thermal);
            temperature =  (TextView) itemView.findViewById(R.id.temperature);
            masked =  (TextView) itemView.findViewById(R.id.masked);
        }
    }


    public Visitors_RecyclerViewAdapter(Context con, ArrayList<DataObjectVisitors> myDataset) {
        context = con;
        mDataset = myDataset;
        size = mDataset.size();
        dbHelper = new DbHelper(context);
        isThermalTemperatureActive = SheredPref.getThermalAccess(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getItemCount()-1) {
            return TITLE;
        } else {
            return LOAD_MORE;
        }
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        if (viewType == TITLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nw_visitor_list_cardview, parent, false);
            DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
            cc = new CheckConnection(context);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent I = new Intent(context, Visiters_Details.class);
                    context.startActivity(I);
                }
            });
            return dataObjectHolder;
        } else if (viewType == LOAD_MORE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_row, parent, false);
            Button Load_m_button = (Button) v.findViewById(R.id.load_more);
            if(mDataset.size()%10 !=0 || mDataset.size()==0) {
                Load_m_button.setVisibility(View.GONE);
            }
            else if(MainActivity.visitor_PageNo!=0 && MainActivity.visitorsArray!=null && MainActivity.visitorsArray.getVisitors().size()==0){
                CommonMethods.showToastSuccess(context, "You have reached last visitor...");//, Toast.LENGTH_SHORT, true).show();
                Load_m_button.setVisibility(View.GONE);
            }
            else
            {
                Load_m_button.setVisibility(View.VISIBLE);
            }
            return new DataObjectHolder(v);
        }
        else
        {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        final int pos = position;
        holder.setIsRecyclable(false);
        if(position >= getItemCount()-1) {
            holder.btnLoadMore.setOnTouchListener((v, event) -> {
                return false;
            });
            holder.btnLoadMore.setOnClickListener(v -> {
                MainActivity.visitor_PageNo = MainActivity.visitor_PageNo+1;
                if(cc.isConnectingToInternet())
                {
                    new GetData(MainActivity.mainActivity, CallFor.VISITORS, ""+MainActivity.visitor_PageNo).execute();
                }
                else
                {
                    holder.btnLoadMore.setVisibility(View.GONE);
                    CommonMethods.showToastError(context, "Unable to load more visitors, Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                }
            });
        } else {
                if(isThermalTemperatureActive)
                {
                    //holder.thermal.setVisibility(View.VISIBLE);
                } else {
                    holder.thermal.setVisibility(View.GONE);
                }
            strName = mDataset.get(position).getmFirst_Name()+" "+mDataset.get(position).getmLast_Name();
            strMobile = mDataset.get(position).getmMobile();
            strDateTime = mDataset.get(position).getmTime();
            strPurpose = mDataset.get(position).getmPurpose();
            strFrom = mDataset.get(position).getmFrom();
            strTemperature = mDataset.get(position).getVisitorTemperature();
            isMask = mDataset.get(position).isMask();


            holder.tvName.setText(strName);
            holder.tvMobile.setText(strMobile);
            holder.tvdateTime.setText(strDateTime);
            holder.tvPurpose.setText(strPurpose);
            if(isMask){
                strMask = "Mask : Yes";
            }else{
                strMask = "Mask : No";
            }
            if(strTemperature==null) {
                strTemperature = "NA";
                strMask = "Mask :"+ strTemperature;
            }
            holder.temperature.setText("Temperature : "+strTemperature);
            holder.masked.setText(strMask);
            if(mDataset.get(position).ismError_flag()){
                holder.txincorrect.setVisibility(View.VISIBLE);
            }
            else{
                holder.txincorrect.setVisibility(View.GONE);
            }

            if (mDataset.get(position).isAskForApproval())
            {
                holder.lltvapprovestatus.setVisibility(View.GONE);
                holder.tvapprove.setVisibility(View.VISIBLE);
                holder.tvdeny.setVisibility(View.VISIBLE);
                holder.llapprovalbuttons.setVisibility(View.VISIBLE);
            }else
            {
                holder.tvapprove.setVisibility(View.GONE);
                holder.tvdeny.setVisibility(View.GONE);
                holder.llapprovalbuttons.setVisibility(View.GONE);
                holder.tvapprovestatus.setVisibility(View.VISIBLE);
                if (SheredPref.getSocietyApproval(context))
                {
                    isapprovalsown=1;
                    if (mDataset.get(position).getApprovalStatus()!=null)
                    {
                        holder.lltvapprovestatus.setVisibility(View.VISIBLE);
                        if (mDataset.get(position).getApprovalStatus().equalsIgnoreCase("WFA"))
                        {
                            holder.tvapprovestatus.setText(AppData.WFA);
                        }
                        if (mDataset.get(position).getApprovalStatus().equalsIgnoreCase("SA"))
                        {
                            holder.tvapprovestatus.setText(AppData.SA);
                        }
                        if (mDataset.get(position).getApprovalStatus().equalsIgnoreCase("SGA"))
                        {
                            holder.tvapprovestatus.setText(AppData.SGA);
                        }
                        if (mDataset.get(position).getApprovalStatus().equalsIgnoreCase("SGD"))
                        {
                            holder.tvapprovestatus.setText(AppData.SGD);
                        }
                        if (mDataset.get(position).getApprovalStatus().equalsIgnoreCase("APP"))
                        {
                            holder.tvapprovestatus.setText(AppData.APP);
                        }
                        if (mDataset.get(position).getApprovalStatus().equalsIgnoreCase("DEN"))
                        {
                            holder.tvapprovestatus.setText(AppData.DEN);
                        }
                        if (mDataset.get(position).getApprovalStatus().equalsIgnoreCase("NI"))
                        {
                            holder.tvapprovestatus.setText(AppData.NI);
                        }
                        if (mDataset.get(position).getApprovalStatus().equalsIgnoreCase("OA"))
                        {
                            holder.tvapprovestatus.setText(AppData.OA);
                        }
                        if (mDataset.get(position).getApprovalStatus().isEmpty())
                        {
                            holder.tvapprovestatus.setVisibility(View.GONE);
                            isapprovalsown=0;
                        }
                    }else {
                        isapprovalsown=0;
                        holder.tvapprovestatus.setVisibility(View.GONE);
                    }
                }else
                {
                    isapprovalsown=0;
                    holder.tvapprovestatus.setVisibility(View.GONE);
                }
                holder.cardView.setId(isapprovalsown);
            }
            holder.tvapprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Approve","api call");
                    if(cc.isConnectingToInternet())
                    {
                        CleverTap.cleverTap_Record_Event(context, Events.action_approve);
                        new GetData(MainActivity.mainActivity, CallFor.UPDATE_VISITOR_STATUS, "?visitorId=" + mDataset.get(position).getmVisitor_id() + "&status=APP").execute();//+"?familyId=" + Integer.parseInt(SheredPref.getFamilyId(context))+"?loginId=" + mDataset.get(position).get()
                    }
                    else
                    {
                        CommonMethods.showToastError(context, "Unable to perform action, Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                    }
                }
            });



            holder.tvdeny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("deny","api call");
                    if(cc.isConnectingToInternet())
                    {
                        CleverTap.cleverTap_Record_Event(context, Events.action_deny);
                        new GetData(MainActivity.mainActivity, CallFor.UPDATE_VISITOR_STATUS, "?visitorId=" + mDataset.get(position).getmVisitor_id() + "&status=DEN").execute();//+"?familyId=" + Integer.parseInt(SheredPref.getFamilyId(context))+"?loginId=" + mDataset.get(position).get()
                    }
                    else
                    {
                        CommonMethods.showToastError(context, "Unable to perform action, Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                    }
                }
            });

            if(cc.isConnectingToInternet())
            {
                strPhotUrl = Constants.BASE_IMG_URL+"/"+mDataset.get(position).getmPhoto();
                strDocUrl = Constants.BASE_IMG_URL+"/"+mDataset.get(position).getmDocUrl();
            }
            else
            {
                strPhotUrl = mDataset.get(position).getmPhoto();
                strDocUrl = mDataset.get(position).getmDocUrl();
            }

            new LoadImage().loadImage(context, R.drawable.ic_avatar, strPhotUrl ,  holder.imgProfilePic, null);

            holder.cardView.setTag(position);

            holder.cardView.setOnClickListener(clickListener);
            holder.imgMarkError.setTag(mDataset.get(position));

            holder.imgMarkError.setOnClickListener(view ->
                    showPopupMenu(holder.imgMarkError, mDataset.get(pos).ismError_flag(), mDataset.get(pos).getmVisitor_id(), mDataset.get(pos).getmFirst_Name()+" "+mDataset.get(pos).getmLast_Name(),mDataset.get(pos).getmPurpose(),mDataset.get(pos).getmMobile()));
        }
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int pos= (int) view.getTag();
                if( cc.isConnectingToInternet())
                {
                    Intent I = new Intent(context, Visiters_Details.class);
                    I.putExtra("PHOTO_URL",Constants.BASE_IMG_URL+"/"+mDataset.get(pos).getmPhoto());
                    I.putExtra("NAME",mDataset.get(pos).getmFirst_Name()+" "+mDataset.get(pos).getmLast_Name());
                    I.putExtra("CONTACT_NO",mDataset.get(pos).getmMobile());
                    I.putExtra("DOC_URL",Constants.BASE_IMG_URL+"/"+mDataset.get(pos).getmDocUrl());
                    I.putExtra("POS",""+pos);
                    int isapprovalshown=view.getId();
                    Log.e("isapprovalshown",isapprovalshown+"--");
                I.putExtra("isapprovalshown",isapprovalshown);
                    context.startActivity(I);
                }
                else
                {
                    Intent I = new Intent(context, Visiters_Details.class);
                    I.putExtra("PHOTO_URL",mDataset.get(pos).getmPhoto());
                    I.putExtra("NAME",mDataset.get(pos).getmFirst_Name()+" "+mDataset.get(pos).getmLast_Name());
                    I.putExtra("CONTACT_NO",mDataset.get(pos).getmMobile());
                    I.putExtra("DOC_URL",mDataset.get(pos).getmDocUrl());
                    I.putExtra("POS",""+pos);
                    int isapprovalshown=view.getId();
                    Log.e("isapprovalshown1111",isapprovalshown+"--");
                    I.putExtra("isapprovalshown",isapprovalshown);
                    context.startActivity(I);
                }
        }
    };

    View.OnClickListener errorClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final int pos = (int) view.getTag();
            String name = mDataset.get(pos).getmFirst_Name()+" "+mDataset.get(pos).getmLast_Name();
        }
    };


    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu( View view, Boolean errorFlag, String visitorId, String visitorName,String purpose,String mobile) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.card_menu, popup.getMenu());
        if(errorFlag){
            popup.getMenu().findItem(R.id.action_mark_incorrect).setVisible(false);
            popup.getMenu().findItem(R.id.action_mark_correct).setVisible(true);
                popup.getMenu().findItem(R.id.action_preapproval).setVisible(false);

        }else{
            popup.getMenu().findItem(R.id.action_mark_incorrect).setVisible(true);
            popup.getMenu().findItem(R.id.action_mark_correct).setVisible(false);
            if ( SheredPref.getPreApprovalsAccess(context))
                popup.getMenu().findItem(R.id.action_preapproval).setVisible(true);
            else
                popup.getMenu().findItem(R.id.action_preapproval).setVisible(false);
        }

        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(visitorId, visitorName,purpose,mobile));
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        String visitorId;
        String visitorName;
        String heading;
        String subHeading;
        String buttonText;
        String Purpose;
        String mobile;
        public MyMenuItemClickListener(String visitorId, String visitorName,String Purpose,String mobile) {
            this.visitorId = visitorId;
            this.visitorName = visitorName;
            this.Purpose=Purpose;
            this.mobile=mobile;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_mark_correct:
                    heading = "Mark as Correct";
                    subHeading = visitorName+" is my visitor";
                    buttonText = "Yes, Mark";
                    showCustomDialog(visitorId, heading, subHeading, buttonText);
                    return true;
                case R.id.action_mark_incorrect:
                    heading = "Mark as Incorrect";
                    subHeading = visitorName+" is not my visitor";
                    buttonText = "Yes, Mark";
                    showCustomDialog(visitorId, heading, subHeading, buttonText);
                    return true;
                case R.id.action_preapproval:
                    Intent I = new Intent(context, PreApprovalActivity.class);
                    int visitorid=Integer.parseInt(visitorId);
                    I.putExtra("visitorId",visitorid);
                    I.putExtra("visitorName",visitorName);
                    I.putExtra("Purpose",Purpose);
                    I.putExtra("mobno",mobile);
                    I.putExtra("fromedit",0);
                    ((Activity) context).startActivityForResult(I, REQUEST_PREAPPROVAL);
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        if (hasLoadButton) {
            return mDataset.size() + 1;
        } else {
            return mDataset.size();
        }
    }


    private void showCustomDialog(final String visitorId, final String heading, String subHeading, String buttonText) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.custum_alert_dialog, null);
        dialogBuilder.setView(dialogView);
        TextView txtalertheading = dialogView.findViewById(R.id.txtalertheading);
        TextView txtalertsubheading = dialogView.findViewById(R.id.txtalertsubheading);

        TextView btnegative = dialogView.findViewById(R.id.btnegative);
        TextView btnpositive = dialogView.findViewById(R.id.btnpositive);

        txtalertheading.setText(heading);
        txtalertsubheading.setText(subHeading);

        btnegative.setVisibility(View.VISIBLE);


        final AlertDialog b = dialogBuilder.create();
        b.setCanceledOnTouchOutside(false);
        b.setCancelable(false);
        b.show();

        btnpositive.setText("Yes, Mark");
        btnegative.setText("Cancel");
        btnpositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
                if (cc.isConnectingToInternet()) {
                    if(heading.contains("Incorrect")){
                        new GetData(MainActivity.mainActivity, CallFor.ERROR_ENTRY, "I-"+visitorId).execute();
                    }else{
                        new GetData(MainActivity.mainActivity, CallFor.ERROR_ENTRY, "C-"+visitorId).execute();
                    }
                }
                else{
                    CommonMethods.showToastError(context, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                }
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