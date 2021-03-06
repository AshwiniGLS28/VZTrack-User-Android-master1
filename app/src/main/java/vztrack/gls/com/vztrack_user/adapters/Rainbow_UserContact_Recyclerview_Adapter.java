package vztrack.gls.com.vztrack_user.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ale.infra.contact.IRainbowContact;
import com.ale.rainbowsdk.RainbowSdk;

import java.util.ArrayList;


import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.activity.Rainbow_Create_Group;
import vztrack.gls.com.vztrack_user.activity.Rainbow_MessageActivity;

import vztrack.gls.com.vztrack_user.profile.FamilyBean;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

public class Rainbow_UserContact_Recyclerview_Adapter extends RecyclerView.Adapter<Rainbow_UserContact_Recyclerview_Adapter.MyViewHolder> {

    private Context m_context;
    ArrayList<FamilyBean> rainbowUsersList = new ArrayList<FamilyBean>();
    ArrayList<FamilyBean> selectedRainbowUsers = new ArrayList<>();
    String callFor;
    public static int getViewTypeValue;
    private int counter = 0;
    int colorCode = 0;
    String emailId = "";


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView noImgText;
        ImageView dp, selectedStatus;
        TextView flatNo;
        LinearLayout row;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.contactName);
            flatNo = (TextView) view.findViewById(R.id.contactFlatNo);
            noImgText = (TextView) view.findViewById(R.id.noImgText);
            dp = (ImageView) view.findViewById(R.id.contactDp);
            selectedStatus = (ImageView) view.findViewById(R.id.selectedStatus);
            row = (LinearLayout) view.findViewById(R.id.row);
        }
    }


    public Rainbow_UserContact_Recyclerview_Adapter(Context context, String callFor) {
        m_context = context;
        this.callFor = callFor;
        rainbowUsersList.addAll(MainActivity.userList);
    }

    @Override
    public Rainbow_UserContact_Recyclerview_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false);
        return  new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FamilyBean familyBean = rainbowUsersList.get(position);
        emailId =familyBean.getRainbowEmailId();
        String flatNumber = familyBean.getFlatNo();
        String nameStr = familyBean.getFlatOwnerName();
        GradientDrawable shape = (GradientDrawable) holder.noImgText.getBackground();
        holder.noImgText.setVisibility(View.VISIBLE);
        holder.dp.setVisibility(View.INVISIBLE);
        colorCode =m_context.getResources().getColor(R.color.ripplecolor);
//        colorCode = UtilityMethods.getRandomColor();
        shape.setColor(m_context.getResources().getColor(R.color.pollcolor));
        holder.noImgText.setText(UtilityMethodsAndroid.getIntitialLetter(nameStr));
        holder.name.setText(nameStr);
        holder.flatNo.setText(flatNumber);

        if(Rainbow_Create_Group.callFrom != null && Rainbow_Create_Group.callFrom.equals("EDIT_GROUP")){
            for(int i = 0; i < Rainbow_Create_Group.room.getParticipantsAsContactList().size(); i++ ){
                String roomEmail = Rainbow_Create_Group.room.getParticipantsAsContactList().get(i).getLoginEmail();
                if(roomEmail != null && roomEmail.equals(emailId)){
                    holder.selectedStatus.setVisibility(View.VISIBLE);
                }else{
                    holder.selectedStatus.setVisibility(View.INVISIBLE);
                }
            }
        }

        if (callFor.equals("GROUP")) {
            int size = Rainbow_Create_Group.selectedRaibowContacts.size();
            if (size == 0) {
                holder.selectedStatus.setVisibility(View.GONE);
            } else {
                for (int i = 0; i < size; i++) {
                    String selectedEmailId = Rainbow_Create_Group.selectedRaibowContacts.get(i).getRainbowEmailId();
                    if (emailId.equals(selectedEmailId)) {
                        holder.selectedStatus.setVisibility(View.VISIBLE);
                        break;
                    } else {
                        holder.selectedStatus.setVisibility(View.GONE);
                    }
                }
            }
        }
        holder.row.setTag(familyBean);
        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FamilyBean familyBean = (FamilyBean) holder.row.getTag();
                String jabberId = familyBean.getRainbowJid();
                if (callFor.equals("GROUP")) {
                    IRainbowContact contact = RainbowSdk.instance().contacts().getContactFromJabberId(jabberId);
                    if (holder.selectedStatus.getVisibility() == View.VISIBLE) {
                        holder.selectedStatus.setVisibility(View.GONE);
                        Rainbow_Create_Group.selectedRaibowContacts.remove(familyBean);
                        Rainbow_Create_Group.removedRaibowContacts.add(familyBean);
                    } else {
                        holder.selectedStatus.setVisibility(View.VISIBLE);
                        if (!Rainbow_Create_Group.selectedContacts.contains(contact)) {
                            Rainbow_Create_Group.selectedRaibowContacts.add(familyBean);
                            Rainbow_Create_Group.removedRaibowContacts.remove(familyBean);
                        }
                    }
                    Rainbow_Create_Group.tvSelectedContacts.setText("Selected " + Rainbow_Create_Group.selectedRaibowContacts.size() + "/" + MainActivity.rainbowUsers.size());
                } else if (callFor.equals("CONTACT")) {
                    Intent intent = new Intent(m_context, Rainbow_MessageActivity.class);
                    intent.putExtra("ColorCode", colorCode);
                    intent.putExtra("JabberId", jabberId);
                    m_context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return rainbowUsersList.size();
    }

    public void updateContacts() {
        rainbowUsersList.clear();
        rainbowUsersList.addAll(MainActivity.userList);
        notifyDataSetChanged();
    }
    public void updateContactsForSelectAll() {
        counter++;
        selectedRainbowUsers.clear();
        rainbowUsersList.clear();
        rainbowUsersList.addAll(MainActivity.userList);
        int size = MainActivity.adminList.size() + MainActivity.userList.size();
        if (counter % 2 == 0) {
            Rainbow_Create_Group.selectedRaibowContacts.removeAll(MainActivity.userList);
            Rainbow_Create_Group.tvSelectedContacts.setText("Selected " + Rainbow_Create_Group.selectedRaibowContacts.size() + "/" + size);
        } else {
            Rainbow_Create_Group.removedRaibowContacts.clear();
            Rainbow_Create_Group.selectedRaibowContacts.addAll(MainActivity.userList);
            Rainbow_Create_Group.tvSelectedContacts.setText("Selected " + Rainbow_Create_Group.selectedRaibowContacts.size() + "/" + size);
        }
        notifyDataSetChanged();
    }
    public void updateSearchedContacts(String query) {
        this.rainbowUsersList.clear();
        for (int i = 0; i < MainActivity.userList.size(); i++) {
            String jabberId = MainActivity.userList.get(i).getRainbowJid();
            String flatNo = MainActivity.userList.get(i).getFlatNo();
            String nameStr = MainActivity.userList.get(i).getFlatOwnerName();
            if (nameStr.toLowerCase().contains(query) || flatNo.toLowerCase().contains(query)) {
                this.rainbowUsersList.add(MainActivity.userList.get(i));
            }
        }
        notifyDataSetChanged();
    }
}
