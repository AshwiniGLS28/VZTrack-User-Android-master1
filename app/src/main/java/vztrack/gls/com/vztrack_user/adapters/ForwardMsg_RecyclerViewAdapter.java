package vztrack.gls.com.vztrack_user.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.list.ArrayItemList;
import com.ale.infra.manager.room.Room;
import com.ale.infra.proxy.conversation.IRainbowConversation;
import com.ale.rainbowsdk.RainbowSdk;

import java.util.List;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.ForwardMsgActivity;
import vztrack.gls.com.vztrack_user.activity.Rainbow_MessageActivity;
import vztrack.gls.com.vztrack_user.beans.GroupBeans;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

public class ForwardMsg_RecyclerViewAdapter extends RecyclerView.Adapter<ForwardMsg_RecyclerViewAdapter.MyViewHolder> {

    private ForwardMsgActivity mContext;
    private ArrayItemList<GroupBeans> allContactgrpList = new ArrayItemList<GroupBeans>();
    private ArrayItemList<GroupBeans> allArrayList = new ArrayItemList<GroupBeans>();
    private int colorCode = 0;
    private GroupBeans groupBeans, roomBean;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView noImgText;
        ImageView dp, selectedStatus;
        TextView flatNo;
        LinearLayout row;
    public MyViewHolder(@NonNull View view) {
        super(view);
        name = (TextView) view.findViewById(R.id.contactName);
        flatNo = (TextView) view.findViewById(R.id.contactFlatNo);
        noImgText = (TextView) view.findViewById(R.id.noImgText);
        dp = (ImageView) view.findViewById(R.id.contactDp);
        selectedStatus = (ImageView) view.findViewById(R.id.selectedStatus);
        row = (LinearLayout) view.findViewById(R.id.row);
    }
}

    public ForwardMsg_RecyclerViewAdapter(@NonNull Context context) {
        mContext = (ForwardMsgActivity) context;
        getInitialData();
        updateListAll();
    }

    private void getInitialData() {
        addAllContacts();
        addAllGroups();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false);
        return  new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GroupBeans contact = allArrayList.get(position);
        String fullName = contact.getGroup_name();
        if(contact.getGroupPhoto() == null) {
            GradientDrawable shape = (GradientDrawable) holder.noImgText.getBackground();
            holder.noImgText.setVisibility(View.VISIBLE);
            holder.dp.setVisibility(View.GONE);
            colorCode = mContext.getResources().getColor(R.color.ripplecolor);
            shape.setColor(mContext.getResources().getColor(R.color.pollcolor));///colorCode
            try {
                holder.noImgText.setText(UtilityMethodsAndroid.getIntitialLetter(fullName));
            } catch (Exception ex) {
                Log.e("exception : ", " " + ex);
            }
        }else {
                holder.noImgText.setVisibility(View.INVISIBLE);
                holder.dp.setVisibility(View.VISIBLE);
                holder.dp.setImageBitmap(contact.getGroupPhoto());
        }
        holder.name.setText(fullName);
        holder.row.setTag(contact);

        if(ForwardMsgActivity.selectedContacts.contains(contact.getGrp_id()) || ForwardMsgActivity.room.contains(contact.getRoom())){
            holder.selectedStatus.setVisibility(View.VISIBLE);
        }else{
            holder.selectedStatus.setVisibility(View.GONE);
        }
        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupBeans beans = (GroupBeans) holder.row.getTag();
                String jaberId = beans.getGrp_id();
                if(holder.selectedStatus.getVisibility() == View.VISIBLE){
                    holder.selectedStatus.setVisibility(View.GONE);
                    ForwardMsgActivity.selectedContacts.remove(jaberId);
                    if (jaberId.contains("room")) {
                        ForwardMsgActivity.room.remove(beans.getRoom());
                    }
                }else {
                    holder.selectedStatus.setVisibility(View.VISIBLE);
                     if(jaberId.contains("room")) {
                         ForwardMsgActivity.room.add(beans.getRoom());
                     }else {
                         ForwardMsgActivity.selectedContacts.add(jaberId);
                     }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return allArrayList.getCount();
    }

    public void updateListAll() {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                allArrayList.clear();
                allArrayList.addAll(allContactgrpList.getCopyOfDataList());
                notifyDataSetChanged();
            }
        });
    }

    public void updateSearchedConversations(String query) {
        query = query.toLowerCase();
        allArrayList.clear();
        List<GroupBeans> data = allContactgrpList.getCopyOfDataList();
        for (int i = 0; i < data.size(); i++) {
           String nameStr = data.get(i).getGroup_name().toLowerCase();
           if(nameStr.contains(query)){
               allArrayList.add(data.get(i));
           }
        }
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });

    }

    public void addAllContacts(){
        for(IRainbowContact contact : RainbowSdk.instance().contacts().getRainbowContacts().getCopyOfDataList()){
            groupBeans = new GroupBeans();
            groupBeans.setGrp_id(contact.getImJabberId());
            groupBeans.setGroup_name(contact.getFirstName()+" "+contact.getLastName());
            groupBeans.setGroupPhoto(contact.getPhoto());
            if(!(Rainbow_MessageActivity.jabberId !=null && contact.getImJabberId().equals(Rainbow_MessageActivity.jabberId))){
                allContactgrpList.add(groupBeans);
            }
        }
    }

    public void addAllGroups(){
       for (Room room : RainbowSdk.instance().bubbles().getAllBubbles().getCopyOfDataList()){
           roomBean  = new GroupBeans();
           roomBean.setGrp_id(room.getJid());
           roomBean.setGroup_name(room.getName());
           roomBean.setGroupPhoto(room.getPhoto());
           roomBean.setRoom(room);
           if(!(Rainbow_MessageActivity.room !=null && room.getJid().equals(Rainbow_MessageActivity.room.getJid()))){
               allContactgrpList.add(roomBean);
           }
       }
    }
}
