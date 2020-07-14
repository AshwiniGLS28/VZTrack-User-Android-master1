package vztrack.gls.com.vztrack_user.adapters;

/**
 * Created by sandeep on 14/3/16.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.proxy.conversation.IRainbowConversation;
import com.ale.rainbowsdk.RainbowSdk;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.Rainbow_MessageActivity;
import vztrack.gls.com.vztrack_user.beans.LocalStroreBean;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.LoadImage;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

public class LocalStoreAdapter_RecyclerViewAdapter extends RecyclerView.Adapter<LocalStoreAdapter_RecyclerViewAdapter.MyViewHolder> {

    private ArrayList<LocalStroreBean> mDataset;
    Context context;
    String strShopName;
    String strShopDescription;
    String strShopCategory;
    String strShopPhotoUrl;
    int strShopId;
    CheckConnection cc;

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView tvShopName, tvShopCategory, tvShopMobileNumber;
        public ExpandableTextView description;
        public LinearLayout CallLayout;
        public ImageView imageViewShop,imgrainboeactivated;
//        public View viewColorBar;

        public MyViewHolder(View view) {
            super(view);
            tvShopName = (TextView) view.findViewById(R.id.shopName);
            tvShopCategory = (TextView) view.findViewById(R.id.shopCategory);
            tvShopMobileNumber = (TextView) view.findViewById(R.id.shopMobile);
            description = (ExpandableTextView) view.findViewById(R.id.expand_text_view_shop_desc);
//            viewColorBar = (View) view.findViewById(R.id.View1);
            CallLayout = (LinearLayout) view.findViewById(R.id.callLL);
            imageViewShop = (ImageView) view.findViewById(R.id.shopImage);
            imgrainboeactivated=view.findViewById(R.id.imgrainboeactivated);
        }
    }

    public LocalStoreAdapter_RecyclerViewAdapter(Context con, ArrayList<LocalStroreBean> myDataset) {
        this.mDataset = myDataset;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        cc = new CheckConnection(context);
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_store_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if(mDataset.size()!=0){
            strShopName = mDataset.get(position).getStoreName();
            strShopDescription = mDataset.get(position).getSpecialDescription();
            final String strShopMobileNumber = mDataset.get(position).getContactNumber();
            strShopCategory = mDataset.get(position).getStoreCategoryName();
            strShopPhotoUrl = mDataset.get(position).getStoreImageUrl();
            strShopId = mDataset.get(position).getStoreId();

//            if(strShopDescription.equals("")){
//                holder.viewColorBar.setVisibility(View.GONE);
//            }
//            else{
//                holder.viewColorBar.setVisibility(View.VISIBLE);
//            }

            if(cc.isConnectingToInternet()){
                new LoadImage().loadImage(context, R.drawable.no_photo_icon, strShopPhotoUrl, holder.imageViewShop, null);
            }
            else{
                CommonMethods.showToastSuccess(context, "Unable to load Shop photo. Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
            }
            holder.tvShopName.setText(strShopName);
            holder.tvShopCategory.setText(strShopCategory);
            holder.description.setText(strShopDescription);
            holder.tvShopMobileNumber.setText(strShopMobileNumber);

            if (mDataset.get(position).isRainbowactivated())
            {
                holder.imgrainboeactivated.setVisibility(View.VISIBLE);
            }else
                holder.imgrainboeactivated.setVisibility(View.GONE);


            if (mDataset.get(position).getJid()!=null) {
                holder.imgrainboeactivated.setTag(mDataset.get(position).getJid());
            }
            holder.imgrainboeactivated.setOnClickListener(v -> {
                String jabberId=null;

                try {
                    jabberId= (String) v.getTag();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                if(SheredPref.getAccess_Raibow(context))
                {
                    if (jabberId!=null) {
                        IRainbowContact contact = RainbowSdk.instance().contacts().getContactFromJabberId(jabberId);
                        IRainbowConversation iRainbowConversation = RainbowSdk.instance().conversations().getConversationFromContact(jabberId);
                        int colorCode = context.getResources().getColor(R.color.pollcolor);
//                Rainbow_MessageActivity.room = iRainbowConversation.getRoom();

                        Intent intent = new Intent(context, Rainbow_MessageActivity.class);
                        intent.putExtra("ColorCode", colorCode);

                        if (iRainbowConversation.isRoomType()) {
                            Rainbow_MessageActivity.room = iRainbowConversation.getRoom();
                            intent.putExtra("JabberId", "");
                        } else {
                            Rainbow_MessageActivity.room = null;
                            intent.putExtra("JabberId", jabberId);
                        }
                        context.startActivity(intent);
                    }else{
                        CommonMethods.showToastSuccess(context,"Rainbow is not activated.");
                    }
                }else {
                    CommonMethods.showToastSuccess(context,"Please activate your rainbow account!");
                }
//                String jabberId= "e13f64ab2f2c46618ea5f77b356392f9@openrainbow.com";

            });
            holder.CallLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent call = new Intent(Intent.ACTION_DIAL);
                    call.setData(Uri.parse("tel:" +strShopMobileNumber));
                    context.startActivity(call);
                    if(cc.isConnectingToInternet()){
                        MainActivity.logShopCall(""+strShopId);
                    }
                    CleverTap.cleverTap_Record_Event(context, Events.local_store_call);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}