package vztrack.gls.com.vztrack_user.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ale.infra.contact.Contact;
import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.list.ArrayItemList;
import com.ale.infra.list.IItemListChangeListener;
import com.ale.infra.manager.room.Room;
import com.ale.infra.proxy.room.IRoomProxy;
import com.ale.rainbowsdk.RainbowSdk;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
//import vztrack.gls.com.vztrack_user.adapters.Rainbow_Contact_Recyclerview_Adapter;
import vztrack.gls.com.vztrack_user.adapters.NwRainbowContactAdapter;
import vztrack.gls.com.vztrack_user.profile.FamilyBean;
import vztrack.gls.com.vztrack_user.utils.SheredPref;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

public class Rainbow_Create_Group extends AppCompatActivity {
    RecyclerView recyclerView1;//,recyclerView1;
    TextView tvLoadingContact;
    private RecyclerView.LayoutManager layoutManager,layoutManager1;
    private NwRainbowContactAdapter adapter1;
    EditText groupName, groupDesc;
    public static TextView tvSelectedContacts;
    public static List<IRainbowContact> selectedContacts = new ArrayList<>();
    public static ArrayList<FamilyBean> selectedRaibowContacts = new ArrayList<>();
    public static ArrayList<FamilyBean> removedRaibowContacts = new ArrayList<>();
    ArrayList<FamilyBean> rainbowlist = new ArrayList<FamilyBean>();
    ArrayItemList<IRainbowContact> contacts;
    Context context;
    ProgressDialog progressDialog;
    public static Room room;
    public static String callFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rainbow__create_group);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left);
        context = Rainbow_Create_Group.this;
        tvLoadingContact = findViewById(R.id.loadingContacts);
        recyclerView1 = (RecyclerView)findViewById(R.id.contactRecyclerViewNew);
//        recyclerView1 = (RecyclerView)findViewById(R.id.contactRecyclerViewAdmin);
        groupName = (EditText) findViewById(R.id.groupName);
        groupDesc = (EditText) findViewById(R.id.groupDesc);
        tvSelectedContacts = (TextView) findViewById(R.id.tvSelectedContacts);
        Button fancyButton = findViewById(R.id.btnCreateGroup);
        contacts = RainbowSdk.instance().contacts().getRainbowContacts();
        int size = MainActivity.adminList.size() + MainActivity.userList.size();
        tvSelectedContacts.setText("Selected 0/"+size);

        //String call_from = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            callFrom = extras.getString("CallFrom");
        }

        String title;
        if(callFrom!=null && callFrom.equals("EDIT_GROUP")){
            title = "Update Group";
            if(room!=null){
                groupName.setText(room.getName());
                groupName.setSelection(groupName.getText().length());
                groupDesc.setText(room.getTopic());
            }else{
                CommonMethods.showToastError(this, "Unable to edit group");
                this.finish();
            }
        }else{
            title = "Create Group";
        }

        fancyButton.setText(title);
        getSupportActionBar().setTitle(title);
        rainbowlist.addAll(MainActivity.adminList);
        rainbowlist.addAll(MainActivity.userList);
        selectedRaibowContacts.clear();
        if(callFrom != null && callFrom.equals("EDIT_GROUP")){
            for(int k = 0; k <Rainbow_Create_Group.room.getParticipantsAsContactList().size(); k++ ){
                String roomJid = Rainbow_Create_Group.room.getParticipantsAsContactList().get(k).getImJabberId();

                for(int i = 0; i < rainbowlist.size(); i++) {
                    String jId = rainbowlist.get(i).getRainbowJid();
                    if(roomJid != null && roomJid.equals(jId) && !selectedRaibowContacts.contains(rainbowlist.get(i))){
                        selectedRaibowContacts.add(rainbowlist.get(i));
                        break;
                    }
                }
            }
            Rainbow_Create_Group.tvSelectedContacts.setText("Selected " + Rainbow_Create_Group.selectedRaibowContacts.size() + "/" + size);
        }

        adapter1 = new NwRainbowContactAdapter(this, "GROUP");
        recyclerView1.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setAdapter(adapter1);
        Log.e("size",  Rainbow_Create_Group.selectedRaibowContacts.size()+"---");

        RainbowSdk.instance().contacts().getRainbowContacts().registerChangeListener(m_changeListener);
    }

    @Override
    protected void onDestroy() {
        selectedContacts.clear();
        selectedRaibowContacts.clear();
        RainbowSdk.instance().contacts().getRainbowContacts().unregisterChangeListener(m_changeListener);
        super.onDestroy();
    }

    private IItemListChangeListener m_changeListener = new IItemListChangeListener() {
        @Override
        public void dataChanged() {
            adapter1.updateContacts();
        }
    };

    public void CreateGroup(View v){
        String strGroupName = groupName.getText().toString().trim();
        String strGroupDesc = groupDesc.getText().toString().trim();
        if(strGroupName.equals("")){
            CommonMethods.showToastError(this, "Group name should not blank");//.show();
        }
        else if(selectedRaibowContacts.size() == 0){
            CommonMethods.showToastError(this, "Select contact to add in group");//.show();
        }else if(strGroupName.length()<=3){
            CommonMethods.showToastError(this, "Group name should have more than 3 characters");//.show();
        }else{
            if(callFrom.equals("EDIT_GROUP")){
                UtilityMethodsAndroid.CloseKeyBoard(context);
                CommonMethods.showToastSuccess(context, "Please wait, Updating Group");//.show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = ProgressDialog.show(context, "", "Loading...");
                    }
                });

                Set<String> contactsToRemove = new HashSet<>();
                List<Contact>  contacts = room.getParticipantsAsContactList();

                for(int i = 1; i < contacts.size(); i++){
                    String roomEmail1 = contacts.get(i).getLoginEmail();
                    for(int j = 0; j < removedRaibowContacts.size(); j++){
                        String removedEmail1 = removedRaibowContacts.get(j).getRainbowEmailId();
                        if(roomEmail1.equals(removedEmail1)){
                            contactsToRemove.add(removedRaibowContacts.get(j).getRainbowJid());
                        }
                    }
                }

                for(int i = 1; i < contacts.size(); i++){
                    String roomEmail = contacts.get(i).getLoginEmail();
                    for(int j = 0; j < selectedRaibowContacts.size(); j++){
                        String removedEmail = selectedRaibowContacts.get(j).getRainbowEmailId();
                        if(roomEmail.equals(removedEmail)){
                            selectedRaibowContacts.remove(selectedRaibowContacts.get(j));
                        }
                    }
                }

                Iterator<String> iterator = contactsToRemove.iterator();
                int index = 0;
                int size = contactsToRemove.size();
                boolean isLastRecord;
                while(iterator.hasNext()){
                    index++;
                    String jId = iterator.next();
                    IRainbowContact iRainbowContact = RainbowSdk.instance().contacts().getContactFromJabberId(jId);
                    isLastRecord = false;
                    if(index == size){
                        isLastRecord = true;
                        contactsToRemove.clear();
                        removedRaibowContacts.clear();
                    }
                    deleteParticipantFromBubble(room, iRainbowContact, isLastRecord);
                }

                List<IRainbowContact> iRainbowContacts = new ArrayList<>();
                for(int i =0;i<selectedRaibowContacts.size();i++){
                    String jid = selectedRaibowContacts.get(i).getRainbowJid();
                    iRainbowContacts.add(RainbowSdk.instance().contacts().getContactFromJabberId(jid));
                }
                if(iRainbowContacts.size()!=0){
                    InviteUserInGroup(room, iRainbowContacts);
                }

                if(strGroupName != room.getName() || strGroupDesc != room.getTopic()){
                    changeBubbleData(room, strGroupName, strGroupDesc, selectedRaibowContacts.size(), contactsToRemove.size());
                }
                selectedRaibowContacts.clear();
                removedRaibowContacts.clear();
            }else{
                UtilityMethodsAndroid.CloseKeyBoard(context);
                CommonMethods.showToastSuccess(context, "Please wait, Creating Group");//.show();
                CreateGroup(strGroupName, strGroupDesc);
                progressDialog = ProgressDialog.show(context, "", "Loading...");
            }
        }

    }

    private void changeBubbleData(Room room,String strGroupName, String strGroupDesc, int selectedCnt, int removedCnt) {
        RainbowSdk.instance().bubbles().changeBubbleData(room, strGroupName, strGroupDesc, true, new IRoomProxy.IChangeRoomDataListener() {
            @Override
            public void onChangeRoomDataSuccess(Room room) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(selectedCnt == 0 && removedCnt ==0){
                            CommonMethods.showToastSuccess(context, "Group Name Updated Successfully");//.show();
                            if(progressDialog!=null && progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Intent intent = new Intent(context, Rainbow_Edit_Group.class);
                            startActivity(intent);
                            Rainbow_Create_Group.this.finish();
                        }
                    }
                });
            }

            @Override
            public void onChangeRoomDataFailed(String roomId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(selectedCnt == 0 && removedCnt ==0) {
                            CommonMethods.showToastError(context, "Error In Updating Group Name");//.show();
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Intent intent = new Intent(context, Rainbow_Edit_Group.class);
                            startActivity(intent);
                            Rainbow_Create_Group.this.finish();
                        }
                    }
                });

            }
        });
    }

    private void deleteParticipantFromBubble(Room room, IRainbowContact iRainbowContact, boolean isLastRescord) {
        RainbowSdk.instance().bubbles().deleteParticipantFromBubble(room, iRainbowContact, new IRoomProxy.IDeleteParticipantListener() {
            @Override
            public void onDeleteParticipantSuccess(String roomId, String participantIdDeleted) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isLastRescord){
                            if(progressDialog!=null && progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            CommonMethods.showToastSuccess(context, "Group Updated Successfully");//.show();
                            Rainbow_Create_Group.this.finish();
                        }
                    }
                });
            }

            @Override
            public void onDeleteParticipantFailure() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CommonMethods.showToastError(context, "Error in deleting participants");
                        if(isLastRescord){
                            if(progressDialog!=null && progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            CommonMethods.showToastSuccess(context, "Group Updated Successfully");//.show();
                            Intent intent = new Intent(context, Rainbow_Edit_Group.class);
                            startActivity(intent);
                            Rainbow_Create_Group.this.finish();
                        }
                    }
                });
            }
        });
    }

    public void CreateGroup(String strGroupName, String strGroupDesc){
        Log.e("Create grp","sizee ---"+selectedRaibowContacts.size());
        RainbowSdk.instance().bubbles().createBubble(strGroupName, strGroupDesc, false, new IRoomProxy.IRoomCreationListener() {
            @Override
            public void onCreationSuccess(Room room) {
                // Do something in the thread UI
                List<IRainbowContact> iRainbowContacts = new ArrayList<>();
                for(int i =0;i<selectedRaibowContacts.size();i++){
                    String jid = selectedRaibowContacts.get(i).getRainbowJid();
                    iRainbowContacts.add(RainbowSdk.instance().contacts().getContactFromJabberId(jid));
                }
                InviteUserInGroup(room, iRainbowContacts);
            }

            @Override
            public void onCreationFailed(IRoomProxy.RoomCreationError roomCreationError) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(progressDialog!=null){
                            progressDialog.dismiss();
                        }
                        CommonMethods.showToastError(context, "Error in creating group");//.show();
                        finish();
                    }
                });
            }
        });
    }

    public void InviteUserInGroup(Room room, List<IRainbowContact> contacts){
        RainbowSdk.instance().bubbles().addParticipantsToBubble(room, contacts, false, true, new IRoomProxy.IAddParticipantsListener() {
            @Override
            public void onAddParticipantsSuccess() {
                Rainbow_Create_Group.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(progressDialog!=null && progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        if(callFrom!=null && callFrom.equals("EDIT_GROUP")){
                            CommonMethods.showToastSuccess(context, "Group Updated Successfully");//.show();
                            Intent intent = new Intent(context, Rainbow_Edit_Group.class);
                            startActivity(intent);
                        }else{
                            CommonMethods.showToastSuccess(context, "Group Created Successfully");//.show();
                        }
                        Rainbow_Create_Group.this.finish();
                    }
                });

            }

            @Override
            public void onMaxParticipantsReached() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(progressDialog!=null){
                            progressDialog.dismiss();
                        }
                        CommonMethods.showToastError(context, "Maximum participant added, You cant add more");//.show();
                    }
                });
            }

            @Override
            public void onAddParticipantFailed(Contact contact) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(progressDialog!=null){
                            progressDialog.dismiss();
                        }
                        CommonMethods.showToastError(context, "Error in adding participant ");//.show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_call).setVisible(false);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        boolean type = SheredPref.getType(context);
        // If type true then this is Company
        if (!type) {
            searchView.setQueryHint("Search By Name Or House No.");
        }else{
            searchView.setQueryHint("Search By Name Or Employee No.");
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                query = query.toLowerCase();
                if(query.equals("")){
//                    adapter.updateContacts();
                    adapter1.updateContacts();
                }else {
//                    adapter.updateSearchedContacts(query);
                    adapter1.updateSearchedContacts(query);
                }
                return true;
            }
        });
        menu.findItem(R.id.action_all).setVisible(true);
        menu.findItem(R.id.action_all).setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
//                adapter.updateContactsForSelectAll();
                adapter1.updateContactsForSelectAll();
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        removedRaibowContacts.clear();
        selectedContacts.clear();
        selectedRaibowContacts.clear();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}