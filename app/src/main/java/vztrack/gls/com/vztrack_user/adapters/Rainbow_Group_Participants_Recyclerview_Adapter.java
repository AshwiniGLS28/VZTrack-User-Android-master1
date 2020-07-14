package vztrack.gls.com.vztrack_user.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ale.infra.contact.Contact;
import com.ale.infra.list.ArrayItemList;
import com.ale.infra.manager.room.Room;
import com.ale.infra.manager.room.RoomParticipant;

import java.util.ArrayList;
import java.util.Map;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.profile.FamilyBean;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;


public class Rainbow_Group_Participants_Recyclerview_Adapter extends RecyclerView.Adapter<Rainbow_Group_Participants_Recyclerview_Adapter.MyViewHolder> {

    private Context m_context;
    private ArrayItemList<RoomParticipant> participantArrayItemList = new ArrayItemList<>();
    private Room room;
    private ArrayList<FamilyBean> rainbowUsers;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView noImgText, status_text;
        ImageView dp, selectedStatus, status_icon;
        TextView flatNo;
        LinearLayout row;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.contactName);
            flatNo = (TextView) view.findViewById(R.id.contactFlatNo);
            noImgText = (TextView) view.findViewById(R.id.noImgText);
            status_text = (TextView) view.findViewById(R.id.status_text);
            dp = (ImageView) view.findViewById(R.id.contactDp);
            status_icon = (ImageView) view.findViewById(R.id.status_icon);
            selectedStatus = (ImageView) view.findViewById(R.id.selectedStatus);
            row = (LinearLayout) view.findViewById(R.id.row);
        }
    }


    public Rainbow_Group_Participants_Recyclerview_Adapter(Context context, Room room) {
        m_context = context;
        this.room = room;
        updateParticipants();
    }

    public void updateParticipants() {
        participantArrayItemList = room.getParticipants();
        rainbowUsers = MainActivity.rainbowUsers;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_participants_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Contact contact = participantArrayItemList.get(position).getContact();
        RoomParticipant roomParticipant = participantArrayItemList.get(position);
        String nameStr = contact.getFirstName() + " " + contact.getLastName();
        holder.name.setText(nameStr);
        String emailId = contact.getMainEmailAddress();
        String flatNo = getFlatNumberFromEmail(emailId);
        holder.flatNo.setText(flatNo);

        if (contact.getPhoto() == null) {
            GradientDrawable shape = (GradientDrawable) holder.noImgText.getBackground();
            holder.noImgText.setVisibility(View.VISIBLE);
            holder.dp.setVisibility(View.INVISIBLE);
            int colorCode =m_context.getResources().getColor(R.color.pollcolor);
            shape.setColor(colorCode);
            try {
                String name = contact.getFirstName() + " " + contact.getLastName();
                holder.noImgText.setText(UtilityMethodsAndroid.getIntitialLetter(name));
            } catch (Exception ex) {
                Log.e("exception : ", " " + ex);
            }
        } else {
            holder.noImgText.setVisibility(View.INVISIBLE);
            holder.dp.setImageBitmap(contact.getPhoto());
        }
        Drawable drawable = null;
        if(roomParticipant.isModerator()){
            drawable = m_context.getResources().getDrawable(R.drawable.ic_admin);
            holder.status_text.setVisibility(View.VISIBLE);
            holder.status_icon.setImageDrawable(drawable);
        }else{
            if(roomParticipant.getStatus().isAccepted()){
                drawable = m_context.getResources().getDrawable(R.drawable.ic_verified);
                holder.status_icon.setImageDrawable(drawable);
            }else if(roomParticipant.getStatus().isRejected() || roomParticipant.getStatus().isDeleted()){
                drawable = m_context.getResources().getDrawable(R.drawable.ic_round_delete_button);
                holder.status_icon.setImageDrawable(drawable);
            }else if(roomParticipant.getStatus().isInvited() || roomParticipant.getStatus().isPending()){
                drawable = m_context.getResources().getDrawable(R.drawable.ic_clock);
                holder.status_icon.setImageDrawable(drawable);
            }else {
                drawable = m_context.getResources().getDrawable(R.drawable.ic_exclamation);
                holder.status_icon.setImageDrawable(drawable);
            }
            holder.status_text.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return participantArrayItemList.getCount();
    }

    public String getFlatNumberFromEmail(String email) {
        String flatNumber = "";
        if (rainbowUsers != null) {
            for (int i = 0; i < rainbowUsers.size(); i++) {
                if (rainbowUsers.get(i).getRainbowEmailId().equals(email)) {
                    flatNumber = rainbowUsers.get(i).getFlatNo();
                    break;
                }
            }
        }
        return flatNumber;
    }

    public int getColorCode(String id) {
        int colorCode = 0;
        boolean findFlag = false;
        if (MainActivity.hm.size() == 0) {
            int genColorCode = UtilityMethods.getRandomColor();
            MainActivity.hm.put(genColorCode, id);
            colorCode = genColorCode;
        } else {
            for (Map.Entry m : MainActivity.hm.entrySet()) {
                if (m.getValue().equals(id)) {
                    colorCode = (int) m.getKey();
                    findFlag = true;
                    break;
                }
            }
            if (!findFlag) {
                int genColorCode = UtilityMethods.getRandomColor();
                MainActivity.hm.put(genColorCode, id);
                colorCode = genColorCode;
            }
        }
        return colorCode;
    }
}
