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

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import vztrack.gls.com.vztrack_user.activity.Provider_Details;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.beans.OutputBeanSearchProvider;
import vztrack.gls.com.vztrack_user.fragment.SearchProviderFragment;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.Constants;
import vztrack.gls.com.vztrack_user.utils.LoadImage;

public class Search_Provider_RecyclerViewAdapter extends RecyclerView.Adapter<Search_Provider_RecyclerViewAdapter.MyViewHolder> {

    private ArrayList<OutputBeanSearchProvider> outputBeanSearchProviders;
    Context context;
    private String PhotoURL;
    private String strVisistorName;
    private String strMobileNo;
    private int visitCount;
    private String strVisitPurpose;
    private String strVisitorPhoto;
    private int intSocietyPriceRating;
    private int intSocietyQualityRating;
    private int intSocietyPunctualityRating;
    private int intOverAllPriceRating;
    private int intOverAllQualityRating;
    private int intOverAllPunctualityRating;
    private static MyClickListener myClickListener;
    private List<String> visibleObjects;
    private List<String> allObjects;
//    private ImageView img_soc_happy,img_soc_sad,img_all_happy,img_all_sad;
    CheckConnection cc;



    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView tvVisitorName, tvVisitPurpose,tvVisitCount;
        private MaterialRatingBar societyRating,overAllRating;
        public ImageView imgVisitorPhoto;
        public CardView cardView;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            tvVisitorName = (TextView) view.findViewById(R.id.tvVisitorName);
            tvVisitPurpose = (TextView) view.findViewById(R.id.tvVisitPurpose);
            tvVisitCount = (TextView) view.findViewById(R.id.tvVisitCount);
            imgVisitorPhoto = (ImageView)view.findViewById(R.id.imgVisitorPhoto);
            societyRating = (MaterialRatingBar)view.findViewById(R.id.societyRating);
            overAllRating = (MaterialRatingBar)view.findViewById(R.id.overAllRating);

//            img_soc_happy = (ImageView) view.findViewById(R.id.img_soc_happy);
//            img_soc_sad = (ImageView) view.findViewById(R.id.img_soc_sad);
//            img_all_happy = (ImageView) view.findViewById(R.id.img_all_happy);
//            img_all_sad = (ImageView) view.findViewById(R.id.img_all_sad);

            cardView = (CardView)view.findViewById(R.id.card_view);
        }
    }

    public Search_Provider_RecyclerViewAdapter(Context con, ArrayList<OutputBeanSearchProvider> outputBeanSearchProviders) {
        this.outputBeanSearchProviders = outputBeanSearchProviders;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_searched_providers_list, parent, false);
        cc = new CheckConnection(context);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        strVisistorName = outputBeanSearchProviders.get(position).getVisitorName();
        strVisitPurpose = SearchProviderFragment.strProviderVal;
        strMobileNo = outputBeanSearchProviders.get(position).getVisitorMobile();
        strVisitorPhoto = outputBeanSearchProviders.get(position).getVisitorPhoto();
        visitCount = outputBeanSearchProviders.get(position).getVisitCount();
        intOverAllQualityRating = outputBeanSearchProviders.get(position).getOverAllQualityRating();
        intOverAllPriceRating = outputBeanSearchProviders.get(position).getOverAllPriceRating();
        intOverAllPunctualityRating = outputBeanSearchProviders.get(position).getOverAllPunctualityRating();
        intSocietyQualityRating = outputBeanSearchProviders.get(position).getSocietyQualityRating();
        intSocietyPriceRating = outputBeanSearchProviders.get(position).getSocietyPriceRating();
        intSocietyPunctualityRating = outputBeanSearchProviders.get(position).getSocietyPunctualityRating();

        holder.societyRating.setRating(intSocietyPriceRating);
        holder.overAllRating.setRating(intOverAllPriceRating);

//        if(intSocietyPriceRating<3){
//            img_soc_happy.setVisibility(View.GONE);
//            img_soc_sad.setVisibility(View.VISIBLE);
//        }
//        else{
//            img_soc_happy.setVisibility(View.VISIBLE);
//            img_soc_sad.setVisibility(View.GONE);
//        }
//
//        if(intOverAllPriceRating<3){
//            img_all_happy.setVisibility(View.GONE);
//            img_all_sad.setVisibility(View.VISIBLE);
//        }
//        else{
//            img_all_happy.setVisibility(View.VISIBLE);
//            img_all_sad.setVisibility(View.GONE);
//        }

        if(cc.isConnectingToInternet())
        {
            strVisitorPhoto = Constants.BASE_IMG_URL+"/"+strVisitorPhoto;
            new LoadImage().loadImage(context, R.drawable.no_photo_icon, strVisitorPhoto ,  holder.imgVisitorPhoto, null);
        }

        holder.tvVisitorName.setText(strVisistorName);
        holder.tvVisitPurpose.setText(strVisitPurpose);
        holder.tvVisitCount.setText("Visited : "+visitCount);

        holder.cardView.setTag(position);
        holder.cardView.setOnClickListener(clickListener);
    }

   View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position= (int) view.getTag();
            Intent I = new Intent(context, Provider_Details.class);
            I.putExtra("VISITOR_NAME",outputBeanSearchProviders.get(position).getVisitorName());
            I.putExtra("VISIT_PURPOSE",SearchProviderFragment.strProviderVal);
            I.putExtra("MOBILE_NO", outputBeanSearchProviders.get(position).getVisitorMobile());
            I.putExtra("VISITOR_PHOTO", Constants.BASE_IMG_URL+"/"+outputBeanSearchProviders.get(position).getVisitorPhoto());

            context.startActivity(I);
        }
    };

    @Override
    public int getItemCount() {
        return outputBeanSearchProviders.size();
    }


    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

}