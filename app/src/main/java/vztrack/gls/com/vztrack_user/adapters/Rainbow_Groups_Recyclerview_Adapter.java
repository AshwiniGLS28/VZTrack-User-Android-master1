package vztrack.gls.com.vztrack_user.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ale.infra.list.ArrayItemList;
import com.ale.infra.manager.room.Room;
import com.ale.infra.manager.room.RoomParticipant;
import com.ale.infra.proxy.room.IRoomProxy;
import com.ale.rainbowsdk.RainbowSdk;


import mehdi.sakout.fancybuttons.FancyButton;
import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.Rainbow_Edit_Group;
import vztrack.gls.com.vztrack_user.activity.Rainbow_MessageActivity;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;


public class Rainbow_Groups_Recyclerview_Adapter extends RecyclerView.Adapter<Rainbow_Groups_Recyclerview_Adapter.MyViewHolder> {

    private Activity m_context;
    ArrayItemList<Room> allRoomArrayItemList = new ArrayItemList<>();
    ArrayItemList<Room> pendingRoomArrayItemList = new ArrayItemList<>();
    ProgressDialog progressDialog;
    public static Room roomObejectToSend;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView group_name, group_desc, no_dp_text;
        ImageView group_dp;
        LinearLayout row;
        FancyButton btn_accept, btn_reject, btn_more_details;

        public MyViewHolder(View view) {
            super(view);
            group_name = (TextView) view.findViewById(R.id.group_name);
            group_desc = (TextView) view.findViewById(R.id.group_desc);
            no_dp_text = (TextView) view.findViewById(R.id.no_dp_text);
            group_dp = (ImageView) view.findViewById(R.id.group_dp);
            btn_accept =  view.findViewById(R.id.btn_accept);
            btn_reject =  view.findViewById(R.id.btn_reject);
            btn_more_details =  view.findViewById(R.id.btn_more_details);
            row = (LinearLayout) view.findViewById(R.id.group_row);
        }
    }

    public Rainbow_Groups_Recyclerview_Adapter(Activity context) {
        this.m_context = context;
        updateGroupList();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Room room = allRoomArrayItemList.get(position);
        String jId = room.getJid();
        String groupName = room.getName();
        String groupDesc = room.getTopic();
        boolean isInvited = false ;

        room.getUserStatus();

        int size = pendingRoomArrayItemList==null?0:pendingRoomArrayItemList.getCount();
        if(size!=0){
            for(int i =0; i < size; i++){
                String pendingJid = pendingRoomArrayItemList.get(i).getJid();
                if(jId.equals(pendingJid)){
                    holder.btn_accept.setVisibility(View.VISIBLE);
                    holder.btn_reject.setVisibility(View.VISIBLE);
                    holder.btn_more_details.setVisibility(View.GONE);
                    isInvited = true;
                    break;
                }else{
                    holder.btn_accept.setVisibility(View.GONE);
                    holder.btn_reject.setVisibility(View.GONE);
                    holder.btn_more_details.setVisibility(View.VISIBLE);
                }
            }
        }else {
            holder.btn_accept.setVisibility(View.GONE);
            holder.btn_reject.setVisibility(View.GONE);
            holder.btn_more_details.setVisibility(View.VISIBLE);
        }


        Bitmap group_dp_bitmap = room.getPhoto();
        int colorCode = m_context.getResources().getColor(R.color.pollcolor);//UtilityMethods.getRandomColor();

        if (group_dp_bitmap == null) {
            GradientDrawable shape = (GradientDrawable) holder.no_dp_text.getBackground();
            holder.no_dp_text.setVisibility(View.VISIBLE);
            holder.group_dp.setVisibility(View.INVISIBLE);
            shape.setColor(colorCode);
            try {
                holder.no_dp_text.setText( UtilityMethodsAndroid.getIntitialLetter(groupName));
            } catch (Exception ex) {
                Log.e("exception : ", " " + ex);
            }
        } else {
            holder.no_dp_text.setVisibility(View.INVISIBLE);
            holder.group_dp.setImageBitmap(group_dp_bitmap);
        }

        holder.group_name.setText(groupName);
        holder.group_desc.setText(groupDesc);
        holder.group_dp.setImageBitmap(group_dp_bitmap);

        holder.btn_accept.setTag(room);
        holder.btn_accept.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = ProgressDialog.show(m_context,"","Loading...");
                Room room = (Room) holder.btn_accept.getTag();
                AcceptInvitation(room, progressDialog);
            }
        });

        holder.btn_reject.setTag(room);
        holder.btn_reject.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = ProgressDialog.show(m_context,"","Loading...");
                Room room = (Room) holder.btn_reject.getTag();
                RejectInvitation(room, progressDialog);
            }
        });
        holder.btn_more_details.setTag(room);
        holder.btn_more_details.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                roomObejectToSend = (Room) holder.btn_more_details.getTag();
                Intent intent = new Intent(m_context, Rainbow_Edit_Group.class);
                m_context.startActivity(intent);
            }
        });

        if(!isInvited){
            holder.row.setTag(room);
            holder.row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Room room = (Room) view.getTag();
                    String jabberId = room.getJid();
                    Rainbow_MessageActivity.room = room;
                    Intent intent = new Intent(m_context, Rainbow_MessageActivity.class);
                    intent.putExtra("ColorCode", colorCode);
                    intent.putExtra("JabberId", "");
                    m_context.startActivity(intent);
                }
            });
        }else{
            holder.row.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return allRoomArrayItemList.getCount();
    }

    public void updateGroupList() {
        m_context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pendingRoomArrayItemList.clear();
                if(RainbowSdk.instance().bubbles()!=null && RainbowSdk.instance().bubbles().getPendingList()!=null && RainbowSdk.instance().bubbles().getPendingList().size()!=0) {
                    pendingRoomArrayItemList.addAll(RainbowSdk.instance().bubbles().getPendingList());
                }
                allRoomArrayItemList.clear();
                allRoomArrayItemList.addAll(RainbowSdk.instance().bubbles().getAllBubbles().getCopyOfDataList());
                notifyDataSetChanged();
            }
        });
    }

    private void AcceptInvitation(Room room, ProgressDialog progressDialog){
        RainbowSdk.instance().bubbles().acceptInvitation(room, new IRoomProxy.IChangeUserRoomDataListener() {
            @Override
            public void onChangeUserRoomDataSuccess(RoomParticipant roomParticipant) {
                m_context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //updateGroupList();
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        CommonMethods.showToastSuccess(m_context, "You are added in group");//.show();
                    }
                });
            }

            @Override
            public void onChangeUserRoomDataFailed() {
                m_context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //updateGroupList();
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        CommonMethods.showToastError(m_context, "Error while accepting invitation");//.show();
                    }
                });
            }
        });
    }

    private void RejectInvitation(Room room, ProgressDialog progressDialog){
        RainbowSdk.instance().bubbles().rejectInvitation(room, new IRoomProxy.IChangeUserRoomDataListener() {
            @Override
            public void onChangeUserRoomDataSuccess(RoomParticipant roomParticipant) {
                m_context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //updateGroupList();
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        CommonMethods.showToastError(m_context, "You rejected invitation");//.show();
                    }
                });
            }

            @Override
            public void onChangeUserRoomDataFailed() {
                m_context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //updateGroupList();
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        CommonMethods.showToastError(m_context, "Error while rejecting request");//.show();
                    }
                });
            }
        });
    }
}
