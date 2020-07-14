package vztrack.gls.com.vztrack_user.adapters;

/**
 * Created by sandeep on 14/3/16.
 */

import android.content.Context;
import android.graphics.Color;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.Arrays;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.beans.DataObjectMessage;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.GetData;

public class Message_RecyclerViewAdapter extends RecyclerView.Adapter<Message_RecyclerViewAdapter.MyViewHolder> {

    private ArrayList<DataObjectMessage> mDataset;
    Context context;
    String strMessage;
    String[] strGroup;
    String strSentDate;
    CheckConnection cc;
    private final int TITLE = 0;
    private final int LOAD_MORE = 1;
    private boolean hasLoadButton = true;

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView tvGroupName, tvSentDate, tvExpandableText;
//        public ExpandableTextView message;
        public TextView message;
        public Button btnLoadMore;

        public MyViewHolder(View view) {
            super(view);
            tvGroupName = (TextView) view.findViewById(R.id.tvGroupName);
            tvExpandableText = (TextView) view.findViewById(R.id.expandable_text);
            tvSentDate = (TextView) view.findViewById(R.id.tvMessageDate);
            message = view.findViewById(R.id.expand_text_view);
            btnLoadMore = (Button) itemView.findViewById(R.id.load_more);
        }
    }

    public Message_RecyclerViewAdapter(Context con, ArrayList<DataObjectMessage> myDataset) {
        this.mDataset = myDataset;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_message_list, parent, false);
        if (viewType == TITLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_message_list, parent, false);
            CardView cv = (CardView) view.findViewById(R.id.card_view);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            cc = new CheckConnection(context);
                      return myViewHolder;
        } else if (viewType == LOAD_MORE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_row, parent, false);
            Button Load_m_button = (Button) v.findViewById(R.id.load_more);
//            CardView cardView = (CardView) v.findViewById(R.id.card_view);
            if(mDataset.size()%10 !=0 || mDataset.size()==0) {
                Load_m_button.setVisibility(View.GONE);
//                cardView.setVisibility(View.GONE);
            }
            else if(MainActivity.message_PageNo!=0 && MainActivity.messageResponceBean!=null && MainActivity.messageResponceBean.getMessageBeans().size()==0){
                CommonMethods.showToastSuccess(context, "You have reached last message...");//, Toast.LENGTH_SHORT, true).show();
                Load_m_button.setVisibility(View.GONE);
//                cardView.setVisibility(View.GONE);
            }
            else
            {
                Load_m_button.setVisibility(View.VISIBLE);
//                cardView.setVisibility(View.VISIBLE);
            }
            return new MyViewHolder(v);
        }
        else
        {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        if(position >= getItemCount()-1) {
            holder.btnLoadMore.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
//                    holder.btnLoadMore.setBackgroundColor(Color.parseColor("#013220"));
                    return false;
                }
            });
            holder.btnLoadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.message_PageNo = MainActivity.message_PageNo+1;
                    if(cc.isConnectingToInternet())
                    {
                        new GetData(MainActivity.mainActivity, CallFor.MESSAGE, ""+ MainActivity.message_PageNo).execute();
                    }
                    else
                    {
                        holder.btnLoadMore.setVisibility(View.GONE);
                        CommonMethods.showToastError(context, "Unable to load more messages, Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                    }
                }
            });
        } else {
            strMessage = mDataset.get(position).getmTextMessage();
            strGroup = mDataset.get(position).getmTextGroups();
            strSentDate = mDataset.get(position).getmTextDate();
            String groups = Arrays.toString(strGroup);
            if(groups.substring(1, groups.length()-1).equals("All") || groups.substring(1, groups.length()-1).equals("Individual")){
                holder.tvGroupName.setText("Send to : "+groups.substring(1, groups.length()-1));
            }else{
                holder.tvGroupName.setText("Send to : "+"Group \n- "+groups.substring(1, groups.length()-1).replace(",","\n-").trim());
            }

            holder.tvSentDate.setText(strSentDate);
            holder.message.setText(strMessage);
//            holder.tvExpandableText.setMovementMethod(LinkMovementMethod.getInstance());
        }
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
    public int getItemCount() {
        if (hasLoadButton) {
            return mDataset.size() + 1;
        } else {
            return mDataset.size();
        }
    }

}