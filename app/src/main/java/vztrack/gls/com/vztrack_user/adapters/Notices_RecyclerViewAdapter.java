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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import vztrack.gls.com.vztrack_user.activity.Notice_DetailsActivity;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.beans.DataObjectNotices;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.Constants;
import vztrack.gls.com.vztrack_user.utils.LoadImage;

public class Notices_RecyclerViewAdapter extends RecyclerView.Adapter<Notices_RecyclerViewAdapter.MyViewHolder> {

    private ArrayList<DataObjectNotices> mDataset;
    Context context;
    String PhotoURL;
    String strTitle;
    String strDescription;
    private String strStartDate,strEndDate, strFileType;
    //private List<String> visibleObjects;
    private List<String> allObjects;
    CheckConnection cc;



    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView title;
        public TextView date;
        public ImageView imgIcon;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tvNoticeTitle);
            date = (TextView) view.findViewById(R.id.tvDate);
            imgIcon = (ImageView)view.findViewById(R.id.imgIcon);
            cardView = (CardView)view.findViewById(R.id.card_view);
        }
    }

    public Notices_RecyclerViewAdapter(Context con, ArrayList<DataObjectNotices> myDataset) {
        this.mDataset = myDataset;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_notices_list, parent, false);

        cc = new CheckConnection(context);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        strTitle = mDataset.get(position).getmTextTitle();
        strDescription = mDataset.get(position).getmTextdescription();
        strStartDate = mDataset.get(position).getmTextDateStart();
        strEndDate = mDataset.get(position).getmTextDateEnd();
        strFileType = mDataset.get(position).getmFileType();

        holder.title.setText(strTitle);
        holder.date.setText("Sent Date : "+strStartDate);
        if(cc.isConnectingToInternet())
        {
            if(strFileType.equalsIgnoreCase(".png") || strFileType.equalsIgnoreCase(".jpg") || strFileType.equalsIgnoreCase(".jpeg")){
                PhotoURL = Constants.BASE_IMG_URL+"/"+mDataset.get(position).getmTextURL();
                new LoadImage().loadImage(context, R.drawable.no_photo_icon, PhotoURL ,  holder.imgIcon, null);

            }else{
                if(strFileType.equalsIgnoreCase(".ppt") || strFileType.equalsIgnoreCase(".pptx")){
                    holder.imgIcon.setImageResource(R.drawable.ppt);
                }else if(strFileType.equalsIgnoreCase(".pdf")){
                    holder.imgIcon.setImageResource(R.drawable.pdf);
                }else if(strFileType.equalsIgnoreCase(".doc") || strFileType.equalsIgnoreCase(".docx")){
                    holder.imgIcon.setImageResource(R.drawable.doc);
                }else if(strFileType.equalsIgnoreCase(".xls") || strFileType.equalsIgnoreCase(".xlsx")){
                    holder.imgIcon.setImageResource(R.drawable.xls);
                }else if(strFileType.equalsIgnoreCase(".txt")){
                    holder.imgIcon.setImageResource(R.drawable.txt);
                }
                else if(strFileType == null || strFileType.equals("")){
                    holder.imgIcon.setImageResource(R.drawable.text_icon);
                }
                else {
                    holder.imgIcon.setImageResource(R.drawable.no_photo_icon);
                }
            }
        }
        else
        {
            new LoadImage().loadImage(context, R.drawable.no_photo_icon, mDataset.get(position).getmTextURL() ,  holder.imgIcon, null);
        }
        holder.cardView.setTag(position);
        holder.cardView.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int pos= (int) view.getTag();
            String noticeId = mDataset.get(pos).getmNoticeId();
            String strFileType =  mDataset.get(pos).getmFileType();
            try {
                    Intent I = new Intent(context, Notice_DetailsActivity.class);
                    I.putExtra("TITLE",mDataset.get(pos).getmTextTitle());
                    I.putExtra("DESCRIPTION",mDataset.get(pos).getmTextdescription());
                    I.putExtra("FILE_TYPE",strFileType);
                    I.putExtra("NOTICE_ID",noticeId);
                    if (cc.isConnectingToInternet())
                    {
                        I.putExtra("PHOTO", Constants.BASE_IMG_URL+"/"+mDataset.get(pos).getmTextURL());
                        I.putExtra("NET","1");
                    }
                    else
                    {
                        I.putExtra("PHOTO",mDataset.get(pos).getmTextURL());
                        I.putExtra("NET","0");
                    }
                    context.startActivity(I);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}