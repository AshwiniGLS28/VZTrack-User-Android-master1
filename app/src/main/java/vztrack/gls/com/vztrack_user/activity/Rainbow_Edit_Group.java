package vztrack.gls.com.vztrack_user.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ale.infra.list.ArrayItemList;
import com.ale.infra.manager.room.Room;
import com.ale.infra.manager.room.RoomParticipant;
import com.ale.infra.manager.room.RoomStatus;
import com.ale.infra.proxy.room.IRoomProxy.IDeleteRoomListener;
import com.ale.rainbowsdk.RainbowSdk;


import java.io.File;

import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.adapters.Rainbow_Group_Participants_Recyclerview_Adapter;
import vztrack.gls.com.vztrack_user.adapters.Rainbow_Groups_Recyclerview_Adapter;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;

public class Rainbow_Edit_Group extends AppCompatActivity {
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Rainbow_Group_Participants_Recyclerview_Adapter adapter;
    TextView groupName, groupDesc, noGroupIconText;
    private static TextView tvSelectedContacts;
    private static ArrayItemList<RoomParticipant> participantsList;
    Context context;
    ImageView group_profile_photo;
    Room room;
    RoomStatus userStatus;
    String strGroupName;
    String strGroupDesc;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rainbow_edit_group);
        context = Rainbow_Edit_Group.this;
        showLoading(context);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left);
        getSupportActionBar().setTitle("Group Details");
        room = Rainbow_Groups_Recyclerview_Adapter.roomObejectToSend;

        group_profile_photo = (ImageView) findViewById(R.id.group_profile_photo);
        recyclerView = (RecyclerView)findViewById(R.id.contactRecyclerView);
        groupName = (TextView) findViewById(R.id.tvGroupName);
        groupDesc = (TextView) findViewById(R.id.tvGroupDesc);
        tvSelectedContacts = (TextView) findViewById(R.id.tvSelectedContacts);
        noGroupIconText = (TextView) findViewById(R.id.noImgText);

        Bitmap bitmap = room.getPhoto();
        strGroupName = room.getName().toString();
        strGroupDesc = room.getTopic();
        participantsList =room.getParticipants();
        room.getCreationDate();

        userStatus = room.getUserStatus();

        if (bitmap == null) {
            GradientDrawable shape = (GradientDrawable) noGroupIconText.getBackground();
            noGroupIconText.setVisibility(View.VISIBLE);
            group_profile_photo.setVisibility(View.INVISIBLE);
            int colorCode = getResources().getColor(R.color.pollcolor);//UtilityMethods.getRandomColor();
            shape.setColor(colorCode);
            try {
                noGroupIconText.setText(strGroupName.substring(0,2).toUpperCase());
            } catch (Exception ex) {
                Log.e("exception : ", " " + ex);
            }
        } else {
            noGroupIconText.setVisibility(View.INVISIBLE);
            group_profile_photo.setImageBitmap(bitmap);
        }

        groupName.setText(strGroupName);
        groupDesc.setText(strGroupDesc);
        tvSelectedContacts.setText("Total Participants : "+participantsList.getCount());

        adapter = new Rainbow_Group_Participants_Recyclerview_Adapter(context, room);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int righcontactRecyclerViewt, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                recyclerView.removeOnLayoutChangeListener(this);
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showLoading(Context con) {
        progressDialog = ProgressDialog.show(con,"","Loading...");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_call).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        if(room.isUserOwner()){
            menu.findItem(R.id.action_delete).setVisible(true);
            menu.findItem(R.id.action_edit).setVisible(true);
        }else{
            menu.findItem(R.id.action_delete).setVisible(false);
            menu.findItem(R.id.action_edit).setVisible(false);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
                case android.R.id.home:
                    onBackPressed();
                    return true;
            case R.id.action_delete:
                showConfirmationDialog();
                return true;
            case R.id.action_edit:
                openEditGroupActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openEditGroupActivity() {
        Rainbow_Create_Group.room = room;
        Log.e("Edit Group","");
        Intent intent = new Intent(context, Rainbow_Create_Group.class);
        intent.putExtra("CallFrom","EDIT_GROUP");
        startActivity(intent);
        this.finish();
    }

    private void showConfirmationDialog() {
        //New Pop up alert
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.custum_alert_dialog, null);
        dialogBuilder.setView(dialogView);
        TextView txtalertheading = dialogView.findViewById(R.id.txtalertheading);
        TextView txtalertsubheading = dialogView.findViewById(R.id.txtalertsubheading);

        TextView btnegative = dialogView.findViewById(R.id.btnegative);
        TextView btnpositive = dialogView.findViewById(R.id.btnpositive);

        txtalertheading.setText("Delete Group "+room.getName());
        txtalertsubheading.setText("Do you really want to proceed?");

        btnegative.setVisibility(View.VISIBLE);


        final android.app.AlertDialog b = dialogBuilder.create();
        b.setCanceledOnTouchOutside(false);
        b.setCancelable(false);
        b.show();

        btnpositive.setText("Yes");
        btnegative.setText("No");
        btnpositive.setOnClickListener(v -> {
            b.dismiss();
            CommonMethods.showToastSuccess(context, "Please wait, Deleting Group");//.show();
            progressDialog = ProgressDialog.show(context,"","Loading...");
            DeleteGroup();
        });
        btnegative.setOnClickListener(v2 -> b.dismiss());
        //end of alert
    }

    private void DeleteGroup() {
        RainbowSdk.instance().bubbles().deleteBubble(room, new IDeleteRoomListener() {
            @Override
            public void onRoomDeletedSuccess() {
                MainActivity.mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(progressDialog!=null){
                            progressDialog.dismiss();
                        }
                        CommonMethods.showToastSuccess(context, "Successfully deleted group");//, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
            @Override
            public void onRoomDeletedFailed() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(progressDialog!=null){
                                progressDialog.dismiss();
                            }
                            CommonMethods.showToastError(context, "Unable to delete group, Error while deleting group");//, Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}