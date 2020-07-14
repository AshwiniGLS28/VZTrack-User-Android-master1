package vztrack.gls.com.vztrack_user.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.ale.infra.contact.Contact;
import com.ale.infra.contact.IContact;
import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.http.adapter.concurrent.RainbowServiceException;
import com.ale.infra.list.ArrayItemList;
import com.ale.infra.proxy.users.IUserProxy;
import com.ale.listener.IRainbowContactsSearchListener;
import com.ale.listener.IRainbowSentInvitationListener;
import com.ale.rainbowsdk.RainbowSdk;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;
import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.BaseActivity;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.activity.Rainbow_Create_Group;
import vztrack.gls.com.vztrack_user.activity.Rainbow_MessageActivity;
import vztrack.gls.com.vztrack_user.profile.FamilyBean;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

/**
 * Created by Administrator on 30/06/2015.
 */
public class ContactHeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private BaseActivity m_context;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final String ADMIN = "Admin";
    private static final String RESIDENT = "Resident";
    private static final String VENDOR = "Vendor";
    private ArrayList<FamilyBean> rainbowUsersLisToShow = new ArrayList<>();
    private String callFor;
    private int counter = 0;
    private int colorCode = 0;
    private String emailId = "";
    private ArrayList<String> emailIdsList = new ArrayList<>();
    private String headerTitle;
    private static String TAG = "ContactHeaderAdapter";

    public ContactHeaderAdapter(Context context, String callFor) {
        this.m_context = (BaseActivity) context;
        this.callFor = callFor;
        updateContacts();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_item, parent, false);
            return new VHHeader(v);
        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_row, parent, false);
            return new VHItem(v);
        }
        return null;
    }

    private FamilyBean getItem(int position) {
        return rainbowUsersLisToShow.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VHHeader) {
            VHHeader VHheader = (VHHeader) holder;
            VHheader.txtTitle.setText(headerTitle);
        } else if (holder instanceof VHItem) {
            VHItem VHitem = (VHItem) holder;
            setupSingleRow(VHitem, position);
        }
    }

    private void setupSingleRow(VHItem holder, int position) {
        FamilyBean familyBean = rainbowUsersLisToShow.get(position);
        emailId = familyBean.getRainbowEmailId();
        String flatNumber = familyBean.getFlatNo();
        String nameStr = familyBean.getFlatOwnerName();
        GradientDrawable shape = (GradientDrawable) holder.noImgText.getBackground();
        holder.noImgText.setVisibility(View.VISIBLE);
        holder.dp.setVisibility(View.INVISIBLE);
        colorCode = m_context.getResources().getColor(R.color.pollcolor);
        shape.setColor(colorCode);
        holder.noImgText.setText(UtilityMethodsAndroid.getIntitialLetter(nameStr));
        holder.name.setText(nameStr);
        holder.flatNo.setText(flatNumber);

        if (Rainbow_Create_Group.callFrom != null && Rainbow_Create_Group.callFrom.equals("EDIT_GROUP")) {
            Rainbow_Create_Group.room.getParticipantsAsContactList();
            if (emailIdsList.contains(emailId)) {
                holder.selectedStatus.setVisibility(View.VISIBLE);
            } else {
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
        } else if (callFor.equals("CONTACT")) {
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
                IRainbowContact contact = RainbowSdk.instance().contacts().getContactFromJabberId(jabberId);
                if (contact != null) {
                    if(isContactInMyNetwork(jabberId)){
                        Intent intent = new Intent(m_context, Rainbow_MessageActivity.class);
                        intent.putExtra("ColorCode", colorCode);
                        intent.putExtra("JabberId", jabberId);
                        m_context.startActivity(intent);
                    }else{
                        searchContactFromJabberId(holder, jabberId);
                    }

                } else {
                    searchContactFromJabberId(holder, jabberId);
                }
            }
        });
    }

    private void searchContactFromJabberId(VHItem holder, String jabberId) {
        RainbowSdk.instance().contacts().searchByJid(jabberId, new IRainbowContactsSearchListener() {
            @Override
            public void searchStarted() {
                Log.e(TAG, "Search Started");
            }

            @Override
            public void searchFinished(List<IContact> list) {
                if (!list.isEmpty()) {
                    IContact iContact = list.get(0);
                    showSnackBarForInvitation(holder.row, iContact.getCorporateId());
                }
            }

            @Override
            public void searchError(RainbowServiceException e) {
                Log.e(TAG, "Error In Search " + e.toString());
            }
        });
    }

    private boolean isContactInInvitations(String jabberId) {
        boolean isInMyNetwork = false;
        List<IRainbowContact> contactsSent= RainbowSdk.instance().contacts().getPendingSentInvitations();
        List<IRainbowContact> contactsReceived = RainbowSdk.instance().contacts().getPendingReceivedInvitations();
        List<IRainbowContact> contacts = new ArrayList<>();
        contacts.addAll(contactsSent);
        contacts.addAll(contactsReceived);
            for(int i = 0; i<contacts.size(); i++){
                Log.e(TAG, " NAMMMMMME : "+contacts.get(i).getFirstName());
                if (contacts.get(i).getImJabberId().equals(jabberId)) {
                    isInMyNetwork = true;
                    break;
                }
            }
        return isInMyNetwork;
    }

    private boolean isContactInMyNetwork(String jabberId) {
        boolean isInMyNetwork = false;
        ArrayItemList<IRainbowContact> contacts = RainbowSdk.instance().contacts().getRainbowContacts();
        for(int i = 0; i<contacts.getCount(); i++){
            if (contacts.get(i).getImJabberId().equals(jabberId)) {
                isInMyNetwork = true;
                break;
            }
        }
        return isInMyNetwork;
    }

    private void showSnackBarForInvitation(View view, String jabberId) {
        Snackbar snack = Snackbar.make(view, R.string.message, Snackbar.LENGTH_LONG);
        snack.setText(R.string.invite_snackbar_message);
        snack.setAction(R.string.send, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRemoveContactToRoaster(jabberId, true);
            }
        });
        snack.setActionTextColor(Color.parseColor("#BB4444"));
        snack.show();
    }

    private void addRemoveContactToRoaster(String corporateId, boolean isAdd) {
        if (isAdd) {
            RainbowSdk.instance().contacts().addRainbowContactToRoster(corporateId, new IRainbowSentInvitationListener() {
                @Override
                public void onInvitationSentSuccess() {
                    m_context.runOnUiThread(() -> {
                        CommonMethods.showToastSuccess(m_context, m_context.getResources().getString(R.string.invitation_sent));
                    });
                }

                @Override
                public void onInvitationSentError(final RainbowServiceException exception) {
                    Log.e(TAG, "Exception : " + exception);
                    String toastMessage;
                    if (exception.getStatusCode() == 409) {
                        toastMessage = "You have already invited this person recently";
                    } else {
                        toastMessage = m_context.getResources().getString(R.string.invitation_error);
                    }
                    m_context.runOnUiThread(() -> {
                        CommonMethods.showToastError(m_context, toastMessage);
                    });
                }

                @Override
                public void onInvitationError() {
                    m_context.runOnUiThread(() -> {
                        Log.e(TAG, " IN onInvitationError ");
                        CommonMethods.showToastError(m_context, m_context.getResources().getString(R.string.invitation_error));
                    });
                }
            });
        } else {
            RainbowSdk.instance().contacts().removeContactFromRoster(corporateId, new IUserProxy.IContactRemovedFromRosterListener() {
                @Override
                public void onSuccess() {
                    Log.e(TAG, "IN onSuccess");
                }

                @Override
                public void onFailure(RainbowServiceException e) {
                    Log.e(TAG, "IN onFailure " + e);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        boolean isHeader = false;
        if (rainbowUsersLisToShow.get(position).getUserType() == -1) {
            headerTitle = rainbowUsersLisToShow.get(position).getFlatOwnerName();
            isHeader = true;
        }
        return isHeader;
    }

    @Override
    public int getItemCount() {
        return rainbowUsersLisToShow.size();
    }

    class VHHeader extends RecyclerView.ViewHolder {
        TextView txtTitle;

        public VHHeader(View itemView) {
            super(itemView);
            this.txtTitle = itemView.findViewById(R.id.txtHeader);
        }
    }

    class VHItem extends RecyclerView.ViewHolder {
        TextView name;
        TextView noImgText;
        ImageView dp, selectedStatus;
        TextView flatNo;
        LinearLayout row;

        public VHItem(View view) {
            super(view);
            name = view.findViewById(R.id.contactName);
            flatNo = view.findViewById(R.id.contactFlatNo);
            noImgText = view.findViewById(R.id.noImgText);
            dp = view.findViewById(R.id.contactDp);
            selectedStatus = view.findViewById(R.id.selectedStatus);
            row = view.findViewById(R.id.row);
        }
    }

    public void updateContacts() {
        rainbowUsersLisToShow.clear();
        addHeader(ADMIN);
        rainbowUsersLisToShow.addAll(MainActivity.adminList);
        addHeader(RESIDENT);
        rainbowUsersLisToShow.addAll(MainActivity.userList);
        addHeader(VENDOR);
        rainbowUsersLisToShow.addAll(MainActivity.vendorList);
        if (Rainbow_Create_Group.callFrom != null && Rainbow_Create_Group.callFrom.equals("EDIT_GROUP")) {
            emailIdsList.clear();
            for (Contact bean : Rainbow_Create_Group.room.getParticipantsAsContactList()) {
                emailIdsList.add(bean.getLoginEmail());
            }
        }
        notifyDataSetChanged();
    }

    private void addHeader(String headerTitle) {
        FamilyBean familyBean = new FamilyBean();
        familyBean.setFlatOwnerName(headerTitle);
        familyBean.setUserType(-1);
        rainbowUsersLisToShow.add(familyBean);
    }

    public void updateSearchedContacts(String query) {
        ArrayList<FamilyBean> list = new ArrayList<>();
        list.addAll(MainActivity.adminList);
        list.addAll(MainActivity.userList);
        list.addAll(MainActivity.vendorList);
        rainbowUsersLisToShow = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String flatNo = list.get(i).getFlatNo();
            String nameStr = list.get(i).getFlatOwnerName();
            if (nameStr.toLowerCase().contains(query) || flatNo.toLowerCase().contains(query)) {
                this.rainbowUsersLisToShow.add(list.get(i));
            }
        }
        notifyDataSetChanged();
    }
}
