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

public class Comment_RecyclerViewAdapter extends RecyclerView.Adapter<Comment_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    String strDate, strComment, strFlatNumber, strCommentedBy;
    public static Dialog dialog;
    ArrayList<CommentBean> commentsBeans;

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView tvFlatNumber, tvDate, tvComment, tvName;

        public MyViewHolder(View view) {
            super(view);
            tvComment = (TextView) view.findViewById(R.id.tvComment);
            tvFlatNumber = (TextView) view.findViewById(R.id.tvFlatNumber);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvName = (TextView) view.findViewById(R.id.tvName);
        }
    }

    public Comment_RecyclerViewAdapter(Context con, ArrayList<CommentBean> commentsBeans) {
        this.commentsBeans = commentsBeans;
        this.context = con;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_comment, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        strDate = commentsBeans.get(position).getCommentDate();
        strFlatNumber = commentsBeans.get(position).getFlatNo();
        strComment = commentsBeans.get(position).getCommentText();
        strCommentedBy = commentsBeans.get(position).getCommentedBy();
        holder.tvName.setText(strCommentedBy.substring(0,1));
        holder.tvDate.setText(strDate);
        if(strCommentedBy.equalsIgnoreCase("admin")){
            holder.tvFlatNumber.setText(strCommentedBy);
            holder.tvName.setBackground(context.getDrawable(R.drawable.rounded_background_admin));
        }else{
            holder.tvFlatNumber.setText("User ("+strFlatNumber+")");
            holder.tvName.setBackground(context.getDrawable(R.drawable.rounded_button_cardviewshadocolor));

        }
        holder.tvComment.setText(strComment);
    }

    @Override
    public int getItemCount() {
        return commentsBeans.size();
    }
    
    
    

}