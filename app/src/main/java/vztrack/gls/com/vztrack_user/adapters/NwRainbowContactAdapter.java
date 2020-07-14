package vztrack.gls.com.vztrack_user.adapters;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ale.infra.contact.Contact;
import com.ale.infra.contact.IRainbowContact;
import com.ale.rainbowsdk.RainbowSdk;
import com.google.gson.Gson;

import java.util.ArrayList;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.activity.Rainbow_Create_Group;
import vztrack.gls.com.vztrack_user.activity.Rainbow_MessageActivity;

import vztrack.gls.com.vztrack_user.profile.FamilyBean;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;
public class NwRainbowContactAdapter extends RecyclerView.Adapter<NwRainbowContactAdapter.MyViewHolder> {

    private Context m_context;
    private ArrayList<FamilyBean>  rainbowUsersLisToShow = new ArrayList<FamilyBean>();
    private ArrayList<FamilyBean> selectedRainbowUsers = new ArrayList<>();
    private String callFor;
    private int counter = 0;
    private int colorCode = 0;
    private String emailId = "";
    private ArrayList<String> emailIdsList = new ArrayList<>();


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView noImgText;
        ImageView dp, selectedStatus;
        TextView flatNo;
        LinearLayout row;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.contactName);
            flatNo = view.findViewById(R.id.contactFlatNo);
            noImgText = view.findViewById(R.id.noImgText);
            dp = view.findViewById(R.id.contactDp);
            selectedStatus = view.findViewById(R.id.selectedStatus);
            row = view.findViewById(R.id.row);
        }
    }


    public NwRainbowContactAdapter(Context context, String callFor) {
        m_context = context;
        this.callFor = callFor;
        updateContacts();
    }

    @Override
    public NwRainbowContactAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false);
        return  new NwRainbowContactAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NwRainbowContactAdapter.MyViewHolder holder, int position) {

        FamilyBean familyBean = rainbowUsersLisToShow.get(position);
        emailId =familyBean.getRainbowEmailId();
        String flatNumber = familyBean.getFlatNo();
        String nameStr = familyBean.getFlatOwnerName();
        GradientDrawable shape = (GradientDrawable) holder.noImgText.getBackground();
        holder.noImgText.setVisibility(View.VISIBLE);
        holder.dp.setVisibility(View.INVISIBLE);
         colorCode =m_context.getResources().getColor(R.color.pollcolor);
//        colorCode = UtilityMethods.getRandomColor();
        shape.setColor(colorCode);
        holder.noImgText.setText(UtilityMethodsAndroid.getIntitialLetter(nameStr));
        holder.name.setText(nameStr);
        String next = "<font color='#434343'> Admin </font>";

        Log.e("isadmin",familyBean.isAdmin()+"");
        if (familyBean.isAdmin())
            holder.flatNo.setText(Html.fromHtml(flatNumber +" ( "+next)+" ) ");
        else
            holder.flatNo.setText(flatNumber);

        if(Rainbow_Create_Group.callFrom != null && Rainbow_Create_Group.callFrom.equals("EDIT_GROUP")){
            Rainbow_Create_Group.room.getParticipantsAsContactList();
            if(emailIdsList.contains(emailId)){
                holder.selectedStatus.setVisibility(View.VISIBLE);
            }else{
                holder.selectedStatus.setVisibility(View.INVISIBLE);
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
                        holder.selectedStatus.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }else if(callFor.equals("CONTACT")){
            holder.selectedStatus.setVisibility(View.GONE);
        }
        holder.row.setTag(familyBean);
        holder.row.setOnClickListener(view -> {
            FamilyBean familyBean1 = (FamilyBean) holder.row.getTag();
            String jabberId = familyBean1.getRainbowJid();
            if (callFor.equals("GROUP")) {
                IRainbowContact contact = RainbowSdk.instance().contacts().getContactFromJabberId(jabberId);
                if (holder.selectedStatus.getVisibility() == View.VISIBLE) {
                    holder.selectedStatus.setVisibility(View.GONE);
                    Rainbow_Create_Group.selectedRaibowContacts.remove(familyBean1);
                    Rainbow_Create_Group.removedRaibowContacts.add(familyBean1);
                } else {
                    holder.selectedStatus.setVisibility(View.VISIBLE);
                    if (!Rainbow_Create_Group.selectedContacts.contains(contact)) {
                        Rainbow_Create_Group.selectedRaibowContacts.add(familyBean1);
                        Rainbow_Create_Group.removedRaibowContacts.remove(familyBean1);
                    }
                }
                int size = MainActivity.adminList.size() + MainActivity.userList.size();
                Rainbow_Create_Group.tvSelectedContacts.setText("Selected " + Rainbow_Create_Group.selectedRaibowContacts.size() + "/" + size);
            } else if (callFor.equals("CONTACT")) {
                Intent intent = new Intent(m_context, Rainbow_MessageActivity.class);
                intent.putExtra("ColorCode", colorCode);
                intent.putExtra("JabberId", jabberId);
                m_context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rainbowUsersLisToShow.size();
    }

    public void updateContacts() {
        rainbowUsersLisToShow.clear();
        rainbowUsersLisToShow.addAll(MainActivity.adminList);
        rainbowUsersLisToShow.addAll(MainActivity.userList);
        if(Rainbow_Create_Group.callFrom != null && Rainbow_Create_Group.callFrom.equals("EDIT_GROUP")){
            emailIdsList.clear();
            for(Contact bean : Rainbow_Create_Group.room.getParticipantsAsContactList()){
                emailIdsList.add(bean.getLoginEmail());
            }
        }
        notifyDataSetChanged();
    }

    public void updateSearchedContacts(String query) {
        ArrayList<FamilyBean> list = new ArrayList<>();
        list.addAll(MainActivity.adminList);
        list.addAll(MainActivity.userList);
        rainbowUsersLisToShow = new ArrayList<FamilyBean>();
        for (int i = 0; i < list.size(); i++) {
            String jabberId =list.get(i).getRainbowJid();
            String flatNo = list.get(i).getFlatNo();
            String nameStr =list.get(i).getFlatOwnerName();
            if (nameStr.toLowerCase().contains(query) || flatNo.toLowerCase().contains(query)) {
                this.rainbowUsersLisToShow.add(list.get(i));
            }
        }
        notifyDataSetChanged();
    }
    public void updateContactsForSelectAll() {
        counter++;
        selectedRainbowUsers.clear();
        rainbowUsersLisToShow.clear();
        rainbowUsersLisToShow.addAll(MainActivity.adminList);
        rainbowUsersLisToShow.addAll(MainActivity.userList);
        int size = MainActivity.adminList.size() + MainActivity.userList.size();
        if (counter % 2 == 0) {
            CommonMethods.showToastSuccess(m_context, "Remove all contacts from group");//, Toast.LENGTH_SHORT).show();
            Rainbow_Create_Group.selectedRaibowContacts.removeAll(MainActivity.adminList);
            Rainbow_Create_Group.selectedRaibowContacts.removeAll(MainActivity.userList);
            Rainbow_Create_Group.tvSelectedContacts.setText("Selected " + Rainbow_Create_Group.selectedRaibowContacts.size() + "/" + size);
        } else {
            Rainbow_Create_Group.removedRaibowContacts.clear();
            CommonMethods.showToastSuccess(m_context, "Added all contacts in group");//, Toast.LENGTH_SHORT).show();
            Rainbow_Create_Group.selectedRaibowContacts.clear();
            Rainbow_Create_Group.selectedRaibowContacts.addAll(MainActivity.adminList);
            Rainbow_Create_Group.selectedRaibowContacts.addAll(MainActivity.userList);
            Rainbow_Create_Group.tvSelectedContacts.setText("Selected " + Rainbow_Create_Group.selectedRaibowContacts.size() + "/" + size);
         }
        notifyDataSetChanged();
    }
}
