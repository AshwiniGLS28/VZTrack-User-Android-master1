package vztrack.gls.com.vztrack_user.adapters;

/**
 * Created by sandeep on 14/3/16.
 */

import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.RatingActivity;
import vztrack.gls.com.vztrack_user.beans.DataObjectRating;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.Constants;
import vztrack.gls.com.vztrack_user.utils.LoadImage;

public class Rating_RecyclerViewAdapter extends RecyclerView.Adapter<Rating_RecyclerViewAdapter.MyViewHolder> {

    private ArrayList<DataObjectRating> mDataset;
    Context context;
    String PhotoURL;
    String strVisistorName;
    String strMobileNo;
    String strInTime;
    String strVisitPurpose;
    String strVisitorPhoto;
    private static MyClickListener myClickListener;
   // public int InternetCheckFlag;
    private List<String> visibleObjects;
    private List<String> allObjects;
    CheckConnection cc;



    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView tvVisitorName, tvVisitPurpose;
        public ImageView imgVisitorPhoto;
        public TextView btnRateVisitor;
        public CardView cardView;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            tvVisitorName = (TextView) view.findViewById(R.id.tvVisitorName);
            tvVisitPurpose = (TextView) view.findViewById(R.id.tvVisitPurpose);
            imgVisitorPhoto = (ImageView)view.findViewById(R.id.imgVisitorPhoto);
            btnRateVisitor =  view.findViewById(R.id.btnRateVisitor);
            cardView = (CardView)view.findViewById(R.id.card_view);
        }
    }

    public Rating_RecyclerViewAdapter(Context con, ArrayList<DataObjectRating> myDataset) {
        this.mDataset = myDataset;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_rating_list, parent, false);

        cc = new CheckConnection(context);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        strVisistorName = mDataset.get(position).getMvisitorName();
        strVisitPurpose = mDataset.get(position).getMvisitorPurpose();
        strMobileNo = mDataset.get(position).getMvisitorMobile();
        strInTime = mDataset.get(position).getMinTime();

        if(cc.isConnectingToInternet())
        {
            strVisitorPhoto = Constants.BASE_IMG_URL+"/"+mDataset.get(position).getMvisitorPhoto();
            new LoadImage().loadImage(context, R.drawable.no_photo_icon, strVisitorPhoto ,  holder.imgVisitorPhoto, null);
        }
        else
        {
           // strPhotUrl = mDataset.get(position).getmPhoto();

           // new LoadImage().loadImage(context, R.drawable.no_photo_icon, strPhotUrl ,  holder.imgProfilePic, null);
        }

        holder.tvVisitorName.setText(strVisistorName);
        holder.tvVisitPurpose.setText(strVisitPurpose);
        holder.btnRateVisitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos= position;
                Intent I = new Intent(context, RatingActivity.class);
                I.putExtra("VISITOR_NAME",mDataset.get(position).getMvisitorName());
                I.putExtra("VISIT_PURPOSE", mDataset.get(position).getMvisitorPurpose());
                I.putExtra("IN_TIME",mDataset.get(position).getMinTime());
                I.putExtra("MOBILE_NO",mDataset.get(position).getMvisitorMobile());
                I.putExtra("VISITR_PHOTO",mDataset.get(position).getMvisitorPhoto());
                I.putExtra("FROM","Activity");

                context.startActivity(I);
            }
        });

        holder.cardView.setTag(position);
        holder.cardView.setOnClickListener(clickListener);
    }

   View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int position= (int) view.getTag();
            Intent I = new Intent(context, RatingActivity.class);
            I.putExtra("VISITOR_NAME",mDataset.get(position).getMvisitorName());
            I.putExtra("VISIT_PURPOSE", mDataset.get(position).getMvisitorPurpose());
            I.putExtra("IN_TIME",mDataset.get(position).getMinTime());
            I.putExtra("MOBILE_NO",mDataset.get(position).getMvisitorMobile());
            I.putExtra("VISITR_PHOTO",mDataset.get(position).getMvisitorPhoto());
            I.putExtra("FROM","Activity");
            context.startActivity(I);
        }
    };

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

}