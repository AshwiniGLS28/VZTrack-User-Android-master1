package vztrack.gls.com.vztrack_user.adapters;

/**
 * Created by sandeep on 14/3/16.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.MarketPlaceAdDeatils;
import vztrack.gls.com.vztrack_user.beans.MarketplaceBean;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Constants;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.LoadImage;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

public class MarketPlace_RecyclerViewAdapter extends RecyclerView.Adapter<MarketPlace_RecyclerViewAdapter.MyViewHolder> {

    private ArrayList<MarketplaceBean> marketplaceBeans;
    Context context;
    String strTitle, strDesc, strPrice, strCreatedBy, strMobileNo,
            strType, strImageUrl, strStatus, strClosedReason;
    int familyId, marketPlaceId;
    CheckConnection cc;

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView tvTitle, tvPrice, tvType,
                tvPostedBy, tvMobileNo, tvReason;//tvDesc
        public ExpandableTextView description;
        public ImageView image;//, imgCall;
        public View viewColorBar;
        public CardView card_view;
        public LinearLayout cardview_Linear;

        public MyViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
//            tvDesc = (TextView) view.findViewById(R.id.tvDescription);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            tvType = (TextView) view.findViewById(R.id.tvType);
            tvPostedBy = (TextView) view.findViewById(R.id.tvPostedBy);
            tvMobileNo = (TextView) view.findViewById(R.id.tvMobileNo);
            image = (ImageView) view.findViewById(R.id.image);
//            imgCall = (ImageView) view.findViewById(R.id.imgCall);
            tvReason = (TextView) view.findViewById(R.id.tvResone);
            card_view = (CardView) view.findViewById(R.id.card_view);
            cardview_Linear = (LinearLayout) view.findViewById(R.id.cardview_Linear);
        }
    }

    public MarketPlace_RecyclerViewAdapter(Context con, ArrayList<MarketplaceBean> marketplaceBeans) {
        this.marketplaceBeans = marketplaceBeans;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        cc = new CheckConnection(context);
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_market_place, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        strTitle = marketplaceBeans.get(position).getTitle();
        strDesc = marketplaceBeans.get(position).getDescription();
        strMobileNo =  marketplaceBeans.get(position).getMobileNo();
        strPrice = marketplaceBeans.get(position).getPrice();
        strCreatedBy = marketplaceBeans.get(position).getFlatNo();
        strType = marketplaceBeans.get(position).getType();
        strStatus = marketplaceBeans.get(position).getStatus();
        strClosedReason = marketplaceBeans.get(position).getCloseReasone();
        familyId = marketplaceBeans.get(position).getFamilyId();
        strImageUrl = marketplaceBeans.get(position).getPhotoUrl();
        marketPlaceId =  marketplaceBeans.get(position).getMarketplaceId();

        holder.tvTitle.setText(strTitle);
//        holder.tvDesc.setText(strDesc);
        holder.tvMobileNo.setText(strMobileNo);
        holder.tvPostedBy.setText("Posted By : "+strCreatedBy);
        holder.tvType.setText(strType);
        if(marketplaceBeans.get(position).getIsFree()==0){
            holder.tvPrice.setText(context.getResources().getString(R.string.price_sym)+strPrice);
        }else {
            holder.tvPrice.setText("Free");
        }

        if(strImageUrl==null || strImageUrl.equals("")){
            holder.image.setImageResource(R.drawable.no_photo_icon);
            //new LoadImage().loadImage(context, R.drawable.no_photo_icon, "", holder.image, null);
        }else{
            String strImgUrl = Constants.BASE_IMG_URL + "/" + strImageUrl;
            new LoadImage().loadImage(context, R.drawable.no_photo_icon, strImgUrl, holder.image, null);
        }

        if(strStatus.equalsIgnoreCase("CLOSE")){
            holder.card_view.setAlpha(0.3f);
            holder.image.setAlpha(0.3f);
            holder.tvReason.setText("Closed\n"+strClosedReason);
            holder.tvReason.setVisibility(View.VISIBLE);
            holder.cardview_Linear.setBackgroundColor(context.getResources().getColor(R.color.gray));
        }else{
            holder.card_view.setAlpha(0.9f);
            holder.image.setAlpha(0.9f);
            holder.tvReason.setVisibility(View.GONE);
            holder.cardview_Linear.setBackgroundColor(Color.WHITE);
        }

        if(strMobileNo == null || strMobileNo.equals("")){
            holder.tvMobileNo.setVisibility(View.INVISIBLE);
//            holder.imgCall.setVisibility(View.INVISIBLE);
        }else{
            holder.tvMobileNo.setTag(strMobileNo);
            holder.tvMobileNo.setVisibility(View.VISIBLE);
//            holder.imgCall.setVisibility(View.VISIBLE);
        }

//        holder.imgCall.setTag(position);
        holder.tvMobileNo.setTag(position);

        holder.tvMobileNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos= (int) v.getTag();
                String mobileNo = marketplaceBeans.get(pos).getMobileNo();
                String status = marketplaceBeans.get(pos).getStatus();
                if(!status.equalsIgnoreCase("CLOSE")){
                    CleverTap.cleverTap_Record_Event(context, Events.market_place_call);
                    UtilityMethodsAndroid.makeCall(context, mobileNo);
                }
            }
        });

//        holder.imgCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int pos= (int) v.getTag();
//                String mobileNo = marketplaceBeans.get(pos).getMobileNo();
//                String status = marketplaceBeans.get(pos).getStatus();
//                if(!status.equalsIgnoreCase("CLOSE")){
//                    CleverTap.cleverTap_Record_Event(context, Events.market_place_call);
//                    UtilityMethodsAndroid.makeCall(context, mobileNo);
//                }
//            }
//        });

        if(strStatus.equals("CLOSE")){

        }

        holder.cardview_Linear.setTag(position);
        holder.cardview_Linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) holder.cardview_Linear.getTag();
                String status = marketplaceBeans.get(pos).getStatus();
                if(status.equalsIgnoreCase("CLOSE")){
                    CommonMethods.showToastError(context,"This ad is closed");//, Toast.LENGTH_SHORT).show();
                }else{
                    Intent I = new Intent(context, MarketPlaceAdDeatils.class);
                    I.putExtra("IMG_URL", marketplaceBeans.get(pos).getPhotoUrl());
                    I.putExtra("TITLE", marketplaceBeans.get(pos).getTitle());
                    I.putExtra("DESC", marketplaceBeans.get(pos).getDescription());
                    I.putExtra("PRICE", marketplaceBeans.get(pos).getPrice());
                    I.putExtra("TYPE", marketplaceBeans.get(pos).getType());
                    I.putExtra("POSTED_BY", marketplaceBeans.get(pos).getFlatNo());
                    I.putExtra("MOBILE_NO", marketplaceBeans.get(pos).getMobileNo());
                    I.putExtra("ID", ""+marketplaceBeans.get(pos).getMarketplaceId());
                    context.startActivity(I);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return marketplaceBeans.size();
    }

}