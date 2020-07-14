package vztrack.gls.com.vztrack_user.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.ale.infra.http.adapter.concurrent.RainbowServiceException;
import com.ale.infra.manager.fileserver.IFileProxy;
import com.ale.infra.manager.fileserver.RainbowFileDescriptor;
import com.ale.infra.manager.room.Room;
import com.ale.infra.proxy.conversation.IRainbowConversation;
import com.ale.rainbowsdk.RainbowSdk;
import java.io.File;
import java.util.ArrayList;
import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.adapters.ForwardMsg_RecyclerViewAdapter;
import vztrack.gls.com.vztrack_user.utils.Finals;

public class ForwardMsgActivity extends AppCompatActivity {

    private static final String TAG = "ForwardMsgActivity";
    ForwardMsg_RecyclerViewAdapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    RecyclerView contactRecyclerList;
    public static ArrayList<String> selectedContacts = new ArrayList<String>();
    String msgToFrwd,receivedPath;
    File fileToFrwd;
    IRainbowConversation conversation;
    ArrayList<File> files = new ArrayList<>();
    public Context context;
    private int totalSender;
    public static ArrayList<Room> room = new ArrayList<Room>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forward_msg);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left);
        getSupportActionBar().setTitle("Forward to");

        initViews();
        context = this;
        setAdapter();

        Intent extras = getIntent();
        if (extras != null){
            msgToFrwd = extras.getStringExtra("TextMsgToFrwd");
            receivedPath = extras.getStringExtra("FilePathToFrwd");
            if(receivedPath != null) {
                fileToFrwd = new File(receivedPath);
            }
        }
        selectedContacts.clear();
        room.clear();
    }

    public void setAdapter(){
        recyclerAdapter = new ForwardMsg_RecyclerViewAdapter(this);
        contactRecyclerList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        contactRecyclerList.setLayoutManager(layoutManager);
        contactRecyclerList.setBackgroundColor(Color.WHITE);
        contactRecyclerList.setAdapter(recyclerAdapter);
    }
    public void initViews(){
        contactRecyclerList = findViewById(R.id.contactToFrwd);
    }

    public void OnclicksendMsg(View view) {
        int i;
        if (selectedContacts.size() != 0 || room.size() != 0) {
            if (msgToFrwd != null) {    // for text msg forward to grps as well contacts
                for (i = 0; i < selectedContacts.size(); i++) {
                    conversation = RainbowSdk.instance().conversations().getConversationFromContact(selectedContacts.get(i));
                    RainbowSdk.instance().im().sendMessageToConversation(conversation, msgToFrwd);
                }
                for(int j =0; j<room.size(); j++){
                    conversation = RainbowSdk.instance().conversations().getConversationFromRoom(room.get(j));
                    RainbowSdk.instance().im().sendMessageToConversation(conversation, msgToFrwd);
                }
                Redirect();
            } else {                   // for file forward to contacts
                ProgressDialog progressDialog = ProgressDialog.show(context, "", "Sending...");
                totalSender = selectedContacts.size() + room.size();
                for (i = 0; i < selectedContacts.size(); i++) {
                    conversation = RainbowSdk.instance().conversations().getConversationFromContact(selectedContacts.get(i));
                    uploadFile(fileToFrwd, conversation, progressDialog);
                }
                for(int j =0; j<room.size(); j++){
                    conversation = RainbowSdk.instance().conversations().getConversationFromRoom(room.get(j));
                    uploadFile(fileToFrwd, conversation, progressDialog);
                }
            }
        }else {
            CommonMethods.showToastSuccess(context, "Please select Contacts or Groups");//.show();
        }
    }

    private void Redirect() {
        Rainbow_MessageActivity.from = Finals.FROM_MESSAGE_FORWARD;
        int colorCode = getResources().getColor(R.color.pollcolor);
        if(selectedContacts.size() == 1  &&  room.size() == 0){
            Rainbow_MessageActivity.from = 0;
            Intent intent = new Intent(ForwardMsgActivity.this, Rainbow_MessageActivity.class);
            String jabberID = selectedContacts.get(0);
            intent.putExtra("ColorCode", colorCode);
            intent.putExtra("JabberId", jabberID);
            startActivity(intent);
        }
        if(selectedContacts.size() == 0 &&  room.size() == 1){
            Rainbow_MessageActivity.from = 0;
            Intent intent = new Intent(ForwardMsgActivity.this, Rainbow_MessageActivity.class);
            Rainbow_MessageActivity.room = conversation.getRoom();
            intent.putExtra("ColorCode", colorCode);
            startActivity(intent);
        }
        CommonMethods.showToastSuccess(context, "Message shared");
        this.finish();
    }

    @Override
    public void onBackPressed() {
        try {
            this.finish();
        } catch (Exception e) {
            Log.e("Exception : ", " " + e);
        }
    }

    public void goBack(View view) {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        selectedContacts.clear();
        room.clear();
        super.onDestroy();
    }

    public void uploadFile(File file, IRainbowConversation conversation, ProgressDialog progressDialog) {
        Uri uri = Uri.fromFile(file);
        RainbowSdk.instance().fileStorage().uploadFileToConversation(conversation, uri, "File", new IFileProxy.IUploadFileListener() {
            @Override
            public void onUploadInProgress(int i) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("File", " Uploaded "+i);
                    }
                });
            }

            @Override
            public void onUploadSuccess(RainbowFileDescriptor fileDescriptor) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        totalSender--;
                        Log.e("File ", "Successfully sent totalSender"+totalSender);
                        if(totalSender <= 0){
                            if(progressDialog!=null && progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            CommonMethods.showToastSuccess(context, "Sent Successfully");
                            Redirect();
                        }
                    }
                });
            }
            @Override
            public void onUploadFailed(RainbowFileDescriptor rainbowFileDescriptor, RainbowServiceException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        totalSender--;
                        Log.e("File "+totalSender, "Upload Fail "+e);
                        if(totalSender <= 0){
                            if(progressDialog!=null && progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            CommonMethods.showToastError(context, "Error in sending");//.show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        menu.findItem(R.id.action_call).setVisible(false);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setQueryHint("Search By Name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                query = query.toLowerCase();
                if (query.equals("")) {
                    recyclerAdapter.updateListAll();
                } else {
                    recyclerAdapter.updateSearchedConversations(query);
                }
                return true;
            }
        });
        return true;
    }
}
