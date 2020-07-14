package vztrack.gls.com.vztrack_user.adapters;

/**
 * Created by sandeep on 14/3/16.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.activity.MarketPlaceAdDeatils;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.beans.CommentBean;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.KeyboardUtil;
import vztrack.gls.com.vztrack_user.utils.PostData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

public class Market_place_Comment_RecyclerViewAdapter extends RecyclerView.Adapter<Market_place_Comment_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    String strDate, strComment, strFlatNumber, strCommentedBy;
    int replyCount;
    public static Dialog dialog;
    ArrayList<CommentBean> commentsBeans;
    CheckConnection cc;

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        public TextView tvFlatNumber, tvDate, tvComment, tvReplyCount, tvReplyText;

        public MyViewHolder(View view) {
            super(view);
            tvComment = (TextView) view.findViewById(R.id.tvComment);
            tvFlatNumber = (TextView) view.findViewById(R.id.tvFlatNumber);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvReplyCount = (TextView) view.findViewById(R.id.tvCount);
            tvReplyText = (TextView) view.findViewById(R.id.tvReply);
        }
    }

    public Market_place_Comment_RecyclerViewAdapter(Context con, ArrayList<CommentBean> commentsBeans) {
        this.commentsBeans = commentsBeans;
        this.context = con;
        cc = new CheckConnection(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_market_place_comment, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        strDate = commentsBeans.get(position).getCommentDate();
        strFlatNumber = commentsBeans.get(position).getCommentedBy();
        strComment = commentsBeans.get(position).getCommentText();
        strCommentedBy = commentsBeans.get(position).getFlatNo();
        holder.tvDate.setText(strDate);
        holder.tvFlatNumber.setText(strCommentedBy);
        holder.tvComment.setText(strComment);
        replyCount = commentsBeans.get(position).getReplies().size();
        holder.tvReplyCount.setText(""+replyCount);
        holder.tvReplyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowBottomSheetForCommentDialog(commentsBeans.get(position).getCommentForId(), commentsBeans.get(position).getCommentId(), commentsBeans.get(position).getReplies());
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentsBeans.size();
    }

    public void ShowBottomSheetForCommentDialog(final int commentId, final int parentCommentId, ArrayList<CommentBean> commentsBeans){
        //if(!strStatus.equalsIgnoreCase("Close")){
        RecyclerView.LayoutManager mLayoutManager;
        RecyclerView.Adapter mAdapter;
        final BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.write_comment_reply_layout);
        final TextView tvReplyCount = (TextView) dialog.findViewById(R.id.tvReplyCount);
        final RecyclerView reply_recycler_view = (RecyclerView) dialog.findViewById(R.id.reply_recycler_view);
        final TextView tvNoReply = (TextView) dialog.findViewById(R.id.tvNoReply);
        final EditText commentText = (EditText) dialog.findViewById(R.id.commentText);
        final ImageView sendButton = (ImageView) dialog.findViewById(R.id.btnSend);
        final LinearLayout llMainLayout = (LinearLayout) dialog.findViewById(R.id.llMainLayout);
        new KeyboardUtil((Activity) context, llMainLayout);
        UtilityMethodsAndroid.CloseKeyBoard(context);
        reply_recycler_view.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        reply_recycler_view.setLayoutManager(mLayoutManager);
        mAdapter = new Market_place_Comment_Replies_RecyclerViewAdapter(context, commentsBeans);
        reply_recycler_view.setAdapter(mAdapter);
        tvReplyCount.setText("Replies : "+commentsBeans.size());
        if(commentsBeans.size()==0){
            reply_recycler_view.setVisibility(View.GONE);
            tvNoReply.setVisibility(View.VISIBLE);
        }else{
            reply_recycler_view.setVisibility(View.VISIBLE);
            tvNoReply.setVisibility(View.GONE);
        }
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                int len = editable.toString().trim().length();
                if(len>=1){
                    sendButton.setVisibility(View.VISIBLE);
                }else{
                    sendButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = commentText.getText().toString().trim();
                if(cc.isConnectingToInternet()){
                    SheredPref.setRun(context,"DONT RUN");
                    CommentBean commentBean = new CommentBean();
                    commentBean.setCommentForId(commentId);
                    commentBean.setParentComment(parentCommentId);
                    commentBean.setCommentText(comment);
                    new PostData(new Gson().toJson(commentBean), MarketPlaceAdDeatils.context, CallFor.ADD_NEW_MARKET_PLACE_COMMENT).execute();
                    CleverTap.cleverTap_Record_Event(context, Events.market_place_add_reply);
                }else {
                    CommonMethods.showToastError(context, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                }

                dialog.dismiss();
                UtilityMethodsAndroid.CloseKeyBoard(context);
            }
        });
        dialog.show();
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
                    dialog.dismiss();
                return false;
            }
        });
//        }else{
//            Toasty.info(this, "This complaint is closed, You can't add comment", Toast.LENGTH_SHORT, true).show();
//        }
    }

}