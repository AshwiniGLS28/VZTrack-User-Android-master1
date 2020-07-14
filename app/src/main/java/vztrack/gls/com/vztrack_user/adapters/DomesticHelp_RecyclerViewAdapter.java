package vztrack.gls.com.vztrack_user.adapters;

/**
 * Created by sandeep on 14/3/16.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.beans.domesticHelpProviders;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Constants;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.LoadImage;

public class DomesticHelp_RecyclerViewAdapter extends RecyclerView.Adapter<DomesticHelp_RecyclerViewAdapter.MyViewHolder> {

    private ArrayList<domesticHelpProviders> domesticHelpProviders;
    Context context;
    String strName;
    String strPurpose;
    String strFlatNo;
    String strAvailable;
    String strLastVisited;
    String strInTime;
    String strMobileNo;
    String strStatus;
    String strImageUrl;
    CheckConnection cc;

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView tvName, tvPurpose, tvMobile, tvFlatNo, tvAvailable, tvDateTime,mobile;
        public ExpandableTextView description;
//        public LinearLayout CallLayout;
        public ImageView image;
        public View viewColorBar;

        public MyViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.name);
            tvPurpose = (TextView) view.findViewById(R.id.purpose);
            tvMobile = (TextView) view.findViewById(R.id.mobile);
            tvFlatNo = (TextView) view.findViewById(R.id.flatNo);
            tvAvailable = (TextView) view.findViewById(R.id.availability);
            tvDateTime = (TextView) view.findViewById(R.id.dateTime);
            mobile =  view.findViewById(R.id.mobile);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }

    public DomesticHelp_RecyclerViewAdapter(Context con, ArrayList<domesticHelpProviders> domesticHelpProviders) {
        this.domesticHelpProviders = domesticHelpProviders;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        cc = new CheckConnection(context);
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_domestic_help_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        strName = domesticHelpProviders.get(position).getFirstName()+" "+domesticHelpProviders.get(position).getLastName();
        strPurpose =  domesticHelpProviders.get(position).getPurpose();
        strMobileNo =  domesticHelpProviders.get(position).getMobileNo();
        strFlatNo =  domesticHelpProviders.get(position).getFlatsVisited();
        strInTime =  domesticHelpProviders.get(position).getInTime();
        strLastVisited =  domesticHelpProviders.get(position).getLastVisited();
        strStatus = domesticHelpProviders.get(position).getStatus();;
        strImageUrl = domesticHelpProviders.get(position).getImg_url();

        holder.tvName.setText(strName);
        holder.tvPurpose.setText(strPurpose);
        holder.tvMobile.setText(strMobileNo);
        holder.tvFlatNo.setText(strFlatNo);

        if(strStatus.equalsIgnoreCase("present")){
            holder.tvAvailable.setText("Available in society");
            holder.tvDateTime.setText("From : "+strInTime);
        }else{
            holder.tvAvailable.setText("Last visit in society");
            holder.tvDateTime.setText(strLastVisited);
        }
        strImageUrl = Constants.BASE_IMG_URL+"/"+strImageUrl;
        new LoadImage().loadImage(context, R.drawable.no_photo_icon, strImageUrl, holder.image, null);
        holder.mobile.setTag(strMobileNo);
        holder.mobile.setOnClickListener(v -> {
            String mobileNo = (String) holder.mobile.getTag();
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" +mobileNo));
            context.startActivity(call);
            CleverTap.cleverTap_Record_Event(context, Events.domestic_help_call);
        });
        holder.image.setTag(strImageUrl);
        holder.image.setOnClickListener(view -> {
            String imageUrl = (String) holder.image.getTag();
            final Dialog dialog = new Dialog(context);
            dialog.getWindow();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_domestic_help);
            ImageView imageView = (ImageView) dialog.findViewById(R.id.image);
            new LoadImage().loadImage(context, R.drawable.no_photo_icon, imageUrl, imageView, null);
            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return domesticHelpProviders.size();
    }

}