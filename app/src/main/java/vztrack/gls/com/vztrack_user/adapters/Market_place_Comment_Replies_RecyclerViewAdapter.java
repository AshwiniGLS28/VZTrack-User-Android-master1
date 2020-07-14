package vztrack.gls.com.vztrack_user.adapters;

/**
 * Created by sandeep on 14/3/16.
 */

import android.app.Dialog;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.beans.CommentBean;

public class Market_place_Comment_Replies_RecyclerViewAdapter extends RecyclerView.Adapter<Market_place_Comment_Replies_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    String strDate, strComment, strFlatNumber, strCommentedBy;
    int replyCount;
    public static Dialog dialog;
    ArrayList<CommentBean> commentsBeans;

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView tvFlatNumber, tvDate, tvComment, tvReplyText;

        public MyViewHolder(View view) {
            super(view);
            tvComment = (TextView) view.findViewById(R.id.tvComment);
            tvFlatNumber = (TextView) view.findViewById(R.id.tvFlatNumber);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvReplyText = (TextView) view.findViewById(R.id.tvReply);
        }
    }

    public Market_place_Comment_Replies_RecyclerViewAdapter(Context con, ArrayList<CommentBean> commentsBeans) {
        this.commentsBeans = commentsBeans;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_market_place_replies, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        strDate = commentsBeans.get(position).getCommentDate();
        strFlatNumber = commentsBeans.get(position).getCommentedBy();
        strComment = commentsBeans.get(position).getCommentText();
        strCommentedBy = commentsBeans.get(position).getFlatNo();
        replyCount = commentsBeans.size();
        holder.tvDate.setText(strDate);
        holder.tvFlatNumber.setText(strCommentedBy);
        holder.tvComment.setText(strComment);
    }

    @Override
    public int getItemCount() {
        return commentsBeans.size();
    }
}