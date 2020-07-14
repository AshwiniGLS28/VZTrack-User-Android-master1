package vztrack.gls.com.vztrack_user.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ale.infra.contact.Contact;
import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.http.adapter.concurrent.RainbowServiceException;
import com.ale.infra.list.IItemListChangeListener;
import com.ale.infra.manager.IMMessage;
import com.ale.infra.manager.call.WebRTCCall;
import com.ale.infra.manager.fileserver.IFileProxy;
import com.ale.infra.manager.fileserver.RainbowFileDescriptor;
import com.ale.infra.manager.room.Room;
import com.ale.infra.proxy.conversation.IRainbowConversation;
import com.ale.listener.IRainbowImListener;
import com.ale.rainbowsdk.RainbowSdk;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.models.sort.SortingTypes;

import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.adapters.Rainbow_MessageChatAdapter;
import vztrack.gls.com.vztrack_user.fragment.Rainbow_Conversation_Fragment;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.Finals;
import vztrack.gls.com.vztrack_user.utils.PermissionConstant;
import vztrack.gls.com.vztrack_user.utils.Permissions;
import vztrack.gls.com.vztrack_user.utils.SoftKeyboard;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

public class Rainbow_MessageActivity extends AppCompatActivity implements IRainbowImListener {

    static ImageView photo;
    static TextView name,txtflatno;
    public static TextView typeStatusAndOnline;
    static TextView noDpText;
    EditText newMessage;
    LinearLayout mainLayout;
    private IRainbowConversation conversation = null;
    LayoutInflater inflater;
    RecyclerView messageRecyclerView;
    private int colorCode;
    public static String jId;
    private Rainbow_MessageChatAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public static Context context;
    private MediaPlayer m_mediaPlayer;
    private static final String TAG = "Conv_Msg_Activity";
    private static final int NB_MESSAGES_TO_RETRIEVE = 10;
    private SwipeRefreshLayout m_swipeContainer = null;
    private boolean setBottom = true;
    public ImageView videoCallBtn;
    public ImageView audioCallBtn;
    CheckConnection cc;
    public static String jabberId;
    private int callType = 0;      // 1 is for audio call and 2 is for video call
    private int permissionFor = 0;  // 1 is for browse file and 2 is for image selection

    private ArrayList<String> docPaths = new ArrayList<>();
    private int MAX_ATTACHMENT_COUNT = 10;

    private static final int FILE_PICKER_REQUEST_CODE = 200;
    private static final int IMAGE_PICKER_REQUEST_CODE = 100;
    private String contactNameOrRoomName;
    private ProgressDialog progressDialog;
    private LinearLayout noDataLayout;
    public static Room room = null;
    private Contact rainbowContact = null;
    private LinearLayout uploadingFileLayout;
    private TextView uploadPercentage, fileName;
    ArrayList<File> files = new ArrayList<>();
    public static int from = 0;
    public ImageView  forwardBtnImg , closeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        context = this;
        Rainbow_Conversation_Fragment.isMessageActivityOpen = true;
        cc = new CheckConnection(getApplicationContext());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        txtflatno=findViewById(R.id.txtflatno);
        photo = (ImageView) findViewById(R.id.conImg);
        name = (TextView) findViewById(R.id.conName);
        typeStatusAndOnline = (TextView) findViewById(R.id.typeStatusAndOnline);
        noDpText = (TextView) findViewById(R.id.noDpText);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        m_swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        videoCallBtn = (ImageView) findViewById(R.id.videoCallBtn);
        audioCallBtn = (ImageView) findViewById(R.id.audioCallBtn);
        newMessage = (EditText) findViewById(R.id.newMessage);
        messageRecyclerView = (RecyclerView) findViewById(R.id.messageScrollView);
        noDataLayout = (LinearLayout) findViewById(R.id.NoDataLayout);
        uploadingFileLayout = (LinearLayout) findViewById(R.id.uploadingFileLayout);
        uploadPercentage = (TextView) findViewById(R.id.uploadPercentage);
        fileName = (TextView) findViewById(R.id.fileName);

        forwardBtnImg =findViewById(R.id.forwardBtn);
        closeBtn =findViewById(R.id.closeBtn);

        progressDialog = progressDialog.show(context, "", "Loading");
        Bundle extras = getIntent().getExtras();
        try{
            jId = extras.getString("JabberId");
        }catch (Exception ex){
            Log.e(TAG, "EXCEPTION : "+jId);
        }

        if (room != null &&  (jId == null || jId.equals(""))) {
            conversation = RainbowSdk.instance().conversations().getConversationFromRoom(room);
           if (conversation.getContact()!=null)
           {
               String emailId = conversation.getContact().getMainEmailAddress();
               String flatNo = UtilityMethods.getFlatNumberFromEmail(emailId);
               if (flatNo!=null) {
                   flatNo=flatNo.replace("(","");
                   flatNo=flatNo.replace(")","");
                   txtflatno.setText(flatNo);
               }
           }
           setData(conversation);
        } else {
            rainbowContact = (Contact) RainbowSdk.instance().contacts().getContactFromJabberId(jId);
            conversation = RainbowSdk.instance().conversations().getConversationFromContact(jId);
            if (conversation.getContact()!=null)
            {
                String emailId = conversation.getContact().getMainEmailAddress();
                String flatNo = UtilityMethods.getFlatNumberFromEmail(emailId);
                if (flatNo!=null) {
                    flatNo=flatNo.replace("(","");
                    flatNo=flatNo.replace(")","");
                    txtflatno.setText(flatNo);
                }
            }
            setData(conversation);
        }

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goneForwardMenu(conversation);
                mAdapter.clearSelection();
            }
        });
    }

    private void setData(IRainbowConversation conversation) {
        if (conversation.isRoomType()) {
            setUpRoomConversation(conversation.getRoom());
        } else {
            setUpP2PConversation(conversation.getContact());
        }

        renderMessages();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                Log.e(TAG, "Error in get Conversation by jabber Id ");
            }
        });

        m_swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setBottom = false;
                mAdapter.markedPosition = -1;
                RainbowSdk.instance().im().getMoreMessagesFromConversation(conversation, NB_MESSAGES_TO_RETRIEVE);
                goneForwardMenu(conversation);
            }
        });

        RainbowSdk.instance().im().getMessagesFromConversation(conversation, NB_MESSAGES_TO_RETRIEVE);
        conversation.getMessages().registerChangeListener(m_changeListener);

        newMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() == 0) {
                    RainbowSdk.instance().im().sendIsTyping(conversation, false);
                } else {
                    RainbowSdk.instance().im().sendIsTyping(conversation, true);
                }
            }
        });

        messageRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        messageRecyclerView.setLayoutManager(layoutManager);
        inflater = getLayoutInflater();

        InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        SoftKeyboard softKeyboard;
        softKeyboard = new SoftKeyboard(mainLayout, im);
        softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged() {
            @Override
            public void onSoftKeyboardHide() {
                setRecyclerViewToBottom();
            }

            @Override
            public void onSoftKeyboardShow() {
                setRecyclerViewToBottom();
            }
        });
        RainbowSdk.instance().im().markMessagesFromConversationAsRead(conversation);
    }

    @Override
    public void onResume() {
        goneForwardMenu(conversation);
        MainActivity.rainbowFragmentFlag = true;
        RainbowSdk.instance().im().markMessagesFromConversationAsRead(conversation);
        RainbowSdk.instance().im().registerListener(this);
        if(from == Finals.FROM_MESSAGE_FORWARD){
            from = 0;
            finish();
        }
        super.onResume();
    }

    public void goBack(View v) {
        onBackPressed();
    }

    @Override
    public void onPause() {
        Log.e("ACTIVITY ", " onPause");
        MainActivity.rainbowFragmentFlag = false;
        RainbowSdk.instance().im().unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        RainbowSdk.instance().im().sendIsTyping(conversation, false);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        try {
            this.finish();
        } catch (Exception e) {
            Log.e("Exception : ", " " + e);
        }
    }

    public void sendMessage(View v) {
        String msg = newMessage.getText().toString().trim();
        if (msg.equals("")) {
            return;
        }
        RainbowSdk.instance().im().sendMessageToConversation(conversation, msg);
        newMessage.setText("");
        setRecyclerViewToBottom();
    }

    public void makeAudioCall(View v) {
        callType = 1;
        mAdapter.markedPosition = -1;
        if (CheckPermissionsCamAndAudio()) {
            makeCall(false);
        }else{
            UtilityMethodsAndroid.ShowPermissionToast(context);
        }
    }

    public void makeVideoCall(View v) {
        callType = 2;
        mAdapter.markedPosition = -1;
        if (CheckPermissionsCamAndAudio()) {
            makeCall(true);
        }else{
            UtilityMethodsAndroid.ShowPermissionToast(context);
        }
    }

    private void makeCall(boolean isVideoCall) {
        if (cc.isConnectingToInternet()) {
            if (rainbowContact != null) {
                RainbowSdk.instance().webRTC().makeCall(rainbowContact, isVideoCall, "VZTrack");
                WebRTCCall currentCall = RainbowSdk.instance().webRTC().getCurrentCall();
                Log.e("status",currentCall.getState().toString()+"----");
                Log.e("isCallAnswered",currentCall.isCallAnswered()+"----");

                Log.e("isAudioConnected",RainbowSdk.instance().webRTC().isAudioConnected()+"----");
            }
        } else {
            CommonMethods.showToastSuccess(this, "Unable to place a call, Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
        }
    }

    private boolean CheckPermissionsCamAndAudio() {
        boolean val = false;
        boolean resultCamera = Permissions.askPermission(this, PermissionConstant.PERMISSION_CAMERA, PermissionConstant.REQ_CODE_CAMERA);
        if (resultCamera) {
            boolean resultAudio = Permissions.askPermission(this, PermissionConstant.PERMISSION_AUDIO, PermissionConstant.REQ_CODE_AUDIO);
            if (resultAudio) {
                val = true;
            }
        }
        return val;
    }

    private boolean CheckPermissionsCamAndStorage() {
        boolean val = false;
        boolean resultCamera = Permissions.askPermission(this, PermissionConstant.PERMISSION_CAMERA, PermissionConstant.REQ_CODE_CAMERA);
        if (resultCamera) {
            boolean resultStorage = Permissions.askPermission(this, PermissionConstant.PERMISSION_EXTERNAL_STORAGE, PermissionConstant.REQ_CODE_EXTERNAL_STORAGE);
            if (resultStorage) {
                val = true;
            }
        }
        return val;
    }

    @Override
    public void onImReceived(String s, final IMMessage imMessage) {
        IRainbowContact fromMsg = RainbowSdk.instance().contacts().getContactFromJabberId(imMessage.getContactJid());
        if (conversation.getContact()!=null)
        if (conversation.getContact().getMainEmailAddress()!=null) {
            if (!conversation.isRoomType() && conversation.getContact().getMainEmailAddress().equals(fromMsg.getMainEmailAddress())) {
                //if (!conversation.isRoomType() && conversation.getContact().getMainEmailAddress().equals(fromMsg.getMainEmailAddress())) {
                //if (conversation.getContact().getMainEmailAddress().equals(fromMsg.getMainEmailAddress())) {
                RainbowSdk.instance().im().markMessageFromConversationAsRead(conversation, imMessage);
            }
        }

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setRecyclerViewToBottom();
            }
        });
    }

    @Override
    public void onImSent(String s, final IMMessage imMessage) {
    }

    @Override
    public void isTypingState(IRainbowContact iRainbowContact, boolean typing, String s) {
        final boolean typeStatusBool = typing;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String id = iRainbowContact.getImJabberId();
//                String id = iRainbowContact.getJid();
                if (id.equals(jabberId)) {
                    if (typeStatusBool) {
                        typeStatusAndOnline.setText("Typing...");
                    } else {
                        // CRASH
                        //setPresence(conversation.getContact().getPresence().toString(), id);
                        setPresence(iRainbowContact.getPresence().toString(), id);
                    }
                }
            }
        });

    }

    @Override
    public void onMessagesListUpdated(int status, String conversationId, List<IMMessage> messages) {
        if (conversationId.equals(conversation.getId())) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    m_swipeContainer.setRefreshing(false);
                    updateLayout();
                }
            });
            RainbowSdk.instance().im().markMessagesFromConversationAsRead(conversation);
        }
    }

    @Override
    public void onMoreMessagesListUpdated(int status, String conversationId, final List<IMMessage> messages) {
        if (conversationId.equals(conversation.getId())) {
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    m_swipeContainer.setRefreshing(false);
                    if (messageRecyclerView != null) {
                        messageRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                //Log.e("MSG LIST", " UPDATED");
                                //m_swipeContainer.setRefreshing(false);
                                //messageRecyclerView.get(0);
                                //setRecyclerViewToBottom();
                            }
                        });
                    }
                }
            });
        }
    }

    private void renderMessages() {
        mAdapter = new Rainbow_MessageChatAdapter(this, conversation);
        messageRecyclerView.setAdapter(mAdapter);
        messageRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                messageRecyclerView.removeOnLayoutChangeListener(this);
                progressDialog.dismiss();
                updateLayout();
            }
        });
        setRecyclerViewToBottom();
    }

    private void setRecyclerViewToBottom() {
        messageRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    private IItemListChangeListener m_changeListener = new IItemListChangeListener() {
        @Override
        public void dataChanged() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (setBottom) {
                        setBottom = false;
                        setRecyclerViewToBottom();
                    }
                    mAdapter.setMessages(conversation.getMessages());
                    mAdapter.notifyDataSetChanged();
                    m_swipeContainer.setRefreshing(false);
                    updateLayout();
                }
            });
        }
    };

    public void setPresence(String presence, String id) {
        if (id.equals(jabberId)) {
            MainActivity.mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String status = presence.replace("_", " ");
                    status = UtilityMethods.camelCase(status);
                    videoCallBtn.setVisibility(View.VISIBLE);
                    audioCallBtn.setVisibility(View.VISIBLE);
                    if (status.toLowerCase().contains("offline") || status.toLowerCase().contains("busy audio") || status.toLowerCase().contains("busy video") || status.toLowerCase().contains("donotdisturb")) {
//                        videoCallBtn.setVisibility(View.GONE);
//                        audioCallBtn.setVisibility(View.GONE);
                    }
                    if (status.toLowerCase().contains("manual away")) {
                        status = "Away";
                    }
                    if (status.toLowerCase().contains("donotdisturb")) {
                        status = "Do Not Disturb";
                    }
                    if (status.toLowerCase().contains("unsubscribed")) {
                        status = "";
                    }
                    Log.e("statusss",status+"");
                    typeStatusAndOnline.setText(status);
                }
            });
        }
    }

    public void openCamera(View v) {
        permissionFor = 2;
        if (CheckPermissionsCamAndStorage()) {
            openCameraAndImagePicker();
        }else{
            UtilityMethodsAndroid.ShowPermissionToast(context);
        }
    }

    public void openCameraAndImagePicker() {
        try {

            Pix.start(Rainbow_MessageActivity.this, Options.init().setRequestCode(IMAGE_PICKER_REQUEST_CODE));
//            Pix.start(Rainbow_MessageActivity.this,               //Activity or Fragment Instance
//                       //Request code for activity results
//                    5);             //Number of images to restict selection count
        } catch (Exception ex) {
            Log.e("Exception ", " In opening photo picker " + ex);
        }

    }

    public void openFileExp(View v) {
        permissionFor = 1;
        boolean resultCamera = Permissions.askPermission(this, PermissionConstant.PERMISSION_EXTERNAL_STORAGE, PermissionConstant.REQ_CODE_EXTERNAL_STORAGE);
        if (resultCamera) {
            openFileChooser();
        }else{
            UtilityMethodsAndroid.ShowPermissionToast(context);
        }
    }

    public void openFileChooser() {
        String[] zips = {".zip", ".rar"};
        String[] pdfs = {".pdf"};
        String[] apk = {".apk"};
        String[] doc = {".doc", ".docx", "excel", ".txt", ".ppt"};
        //String[] video = {".mp4", ".3gp", ".mkv", ".avi", ".flv", ".mov", ".avi"};
        String[] video = {".mp4", ".3gp", ".mkv"};
        String[] audio = {".aac", ".m4a", "amr", ".opus", ".mp3"};

        docPaths.clear();

        int maxCount = MAX_ATTACHMENT_COUNT;
        FilePickerBuilder filePickerBuilder = new FilePickerBuilder();
        filePickerBuilder.setMaxCount(maxCount);
        filePickerBuilder.setSelectedFiles(docPaths);
        filePickerBuilder.setActivityTheme(R.style.LibAppTheme);
        filePickerBuilder.setActivityTitle("Please select doc");

        filePickerBuilder.addFileSupport("Video", video);
        filePickerBuilder.addFileSupport("Audio", audio);
        filePickerBuilder.addFileSupport("PDF", pdfs, R.drawable.pdf);
        filePickerBuilder.addFileSupport("Doc", doc);
        filePickerBuilder.addFileSupport("ZIP", zips);
        filePickerBuilder.addFileSupport("App", apk);
        filePickerBuilder.enableDocSupport(false);
        filePickerBuilder.enableSelectAll(true);
        filePickerBuilder.sortDocumentsBy(SortingTypes.name);
        filePickerBuilder.pickFile(this, FILE_PICKER_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICKER_REQUEST_CODE) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            Log.e(TAG,"Return value :"+returnValue);
            if (returnValue.size() == 0) {
                CommonMethods.showToastSuccess(context, "No file selected");//.show();
            } else {
                android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.custum_alert_dialog, null);
                dialogBuilder.setView(dialogView);
                TextView txtalertheading = dialogView.findViewById(R.id.txtalertheading);
                TextView txtalertsubheading = dialogView.findViewById(R.id.txtalertsubheading);

                TextView btnegative = dialogView.findViewById(R.id.btnegative);
                TextView btnpositive = dialogView.findViewById(R.id.btnpositive);

                txtalertheading.setText("Send Attachment");
                txtalertsubheading.setText("Send attachment to " + contactNameOrRoomName + "?");

                btnegative.setVisibility(View.VISIBLE);


                final android.app.AlertDialog b = dialogBuilder.create();
                b.setCanceledOnTouchOutside(false);
                b.setCancelable(false);
                b.show();

                btnpositive.setText("Yes");
                btnegative.setText("No");
                btnpositive.setOnClickListener(v -> {
                    b.dismiss();
                     for (int i = 0; i < returnValue.size(); i++) {
                        File file = new File(returnValue.get(i));
                        files.add(file);
                    }
                    if(files.size()!=0){
                        uploadFile(files.get(0));
                    }
                });
                btnegative.setOnClickListener(v2 -> b.dismiss());
                //end of alert
            }
        }


        if (resultCode == Activity.RESULT_OK && data != null && requestCode == FILE_PICKER_REQUEST_CODE) {
            docPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));

            //New Pop up alert
            android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View dialogView = inflater.inflate(R.layout.custum_alert_dialog, null);
            dialogBuilder.setView(dialogView);
            TextView txtalertheading = dialogView.findViewById(R.id.txtalertheading);
            TextView txtalertsubheading = dialogView.findViewById(R.id.txtalertsubheading);

            TextView btnegative = dialogView.findViewById(R.id.btnegative);
            TextView btnpositive = dialogView.findViewById(R.id.btnpositive);

            txtalertheading.setText("Send Attachment");
            txtalertsubheading.setText("Send attachment to " + contactNameOrRoomName + "?");

            btnegative.setVisibility(View.VISIBLE);


            final android.app.AlertDialog b = dialogBuilder.create();
            b.setCanceledOnTouchOutside(false);
            b.setCancelable(false);
            b.show();

            btnpositive.setText("Yes");
            btnegative.setText("No");
            btnpositive.setOnClickListener(v -> {
                b.dismiss();
                for (int i = 0; i < docPaths.size(); i++) {
                    File file = new File(docPaths.get(i));
                    files.add(file);
                }
                if(files.size()!=0){
                    uploadFile(files.get(0));

                }
            });
            btnegative.setOnClickListener(v2 -> b.dismiss());
            //end of alert
        }
    }

    public void uploadFile(File file) {

        String strFileName = file.getName();
        Uri uri = Uri.fromFile(file);
        Log.e(TAG,"URI :"+uri+"file :"+file);
        CommonMethods.showToastSuccess(context, "Sending...");//.show();

        uploadingFileLayout.setVisibility(View.VISIBLE);
        uploadPercentage.setText("1%");
        fileName.setText(strFileName);

        RainbowSdk.instance().fileStorage().uploadFileToConversation(conversation, uri, "File", new IFileProxy.IUploadFileListener() {
            @Override
            public void onUploadSuccess(RainbowFileDescriptor fileDescriptor) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        uploadPercentage.setText("100%");
                        uploadingFileLayout.setVisibility(View.GONE);
                        files.remove(file);
                        if(files.size()!=0){
                            uploadFile(files.get(0));
                        }
                        CommonMethods.showToastSuccess(context, "Sent Successfully");//.show();
                    }
                });
            }

            @Override
            public void onUploadInProgress(int percent) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //progress.setProgress(percent);
                        //item.setProgress(percent);
                        uploadPercentage.setText(percent+"%");
                    }
                });
                Log.e("File ", "Uploading Process" + percent);
            }

            @Override
            public void onUploadFailed(RainbowFileDescriptor rainbowFileDescriptor, RainbowServiceException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        uploadingFileLayout.setVisibility(View.GONE);
                        if(files.size()!=0){
                            uploadFile(files.get(0));
                        }
                        CommonMethods.showToastError(context, "Error in sending");//.show();
                    }
                });
                Log.e("File ", "Upload Fail");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionConstant.REQ_CODE_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (permissionFor == 2) {
                        Permissions.askPermission(this, PermissionConstant.PERMISSION_EXTERNAL_STORAGE, PermissionConstant.REQ_CODE_EXTERNAL_STORAGE);
                    } else {
                        Permissions.askPermission(this, PermissionConstant.PERMISSION_AUDIO, PermissionConstant.REQ_CODE_AUDIO);
                    }
                }
                break;
            }
            case PermissionConstant.REQ_CODE_AUDIO: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    switch (callType) {
                        case 1: {
                            makeCall(false);
                            break;
                        }
                        case 2: {
                            makeCall(true);
                            break;
                        }
                    }
                }
                break;
            }

            case PermissionConstant.REQ_CODE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (permissionFor == 1) {
                        openFileChooser();
                    } else if (permissionFor == 2) {
                        openCameraAndImagePicker();
                    }
                }
                break;
            }
        }
    }

    private void setUpRoomConversation(Room room) {
        videoCallBtn.setVisibility(View.INVISIBLE);
        audioCallBtn.setVisibility(View.INVISIBLE);
        typeStatusAndOnline.setVisibility(View.INVISIBLE);
        contactNameOrRoomName = room.getName();
        Log.e("ROOM NAME ", " : N :  " + contactNameOrRoomName);
        name.setText(contactNameOrRoomName);
        if (room.getPhoto() == null) {
            noDpText.setVisibility(View.VISIBLE);
            photo.setVisibility(View.GONE);
            Bundle extras = getIntent().getExtras();
            colorCode = extras.getInt("ColorCode");
            GradientDrawable shape = (GradientDrawable) noDpText.getBackground();
            shape.setColor(colorCode);
            noDpText.setText(room.getName().substring(0, 2).toUpperCase());
        } else {
            photo.setImageBitmap(room.getPhoto());
        }
    }

    private void setUpP2PConversation(IRainbowContact contact) {
        //conversationType = "P2P";
        videoCallBtn.setVisibility(View.VISIBLE);
        audioCallBtn.setVisibility(View.VISIBLE);
        typeStatusAndOnline.setVisibility(View.VISIBLE);
        contactNameOrRoomName = contact.getFirstName() + " " + contact.getLastName();
        name.setText(contactNameOrRoomName);

        String status = contact.getPresence().toString();
        jabberId = contact.getImJabberId();
        setPresence(status, jabberId);
        if (contact.getPhoto() == null) {
            noDpText.setVisibility(View.VISIBLE);
            photo.setVisibility(View.GONE);
            Bundle extras = getIntent().getExtras();
            colorCode = extras.getInt("ColorCode");
            GradientDrawable shape = (GradientDrawable) noDpText.getBackground();
            shape.setColor(colorCode);
            String name = contact.getFirstName() + " " + contact.getLastName();
            noDpText.setText(UtilityMethodsAndroid.getIntitialLetter(name));
        } else {
            photo.setImageBitmap(contact.getPhoto());
        }
    }

    public void setDataLayout() {
        messageRecyclerView.setVisibility(View.VISIBLE);
        noDataLayout.setVisibility(View.INVISIBLE);
    }

    private void setNoDataLayout() {
        noDataLayout.setVisibility(View.VISIBLE);
        messageRecyclerView.setVisibility(View.INVISIBLE);
    }

    private void updateLayout() {
        int count = 0;
        if (mAdapter != null) {
            count = mAdapter.getItemCount();
        }
        if (count == 0) {
            setNoDataLayout();
        } else {
            setDataLayout();
        }
    }

    public void visibleForwardMenu(){
        forwardBtnImg.setVisibility(View.VISIBLE);
        closeBtn.setVisibility(View.VISIBLE);
        videoCallBtn.setVisibility(View.GONE);
        audioCallBtn.setVisibility(View.GONE);

    }
    public void goneForwardMenu(IRainbowConversation conversation){
        forwardBtnImg.setVisibility(View.GONE);
        closeBtn.setVisibility(View.GONE);
        if (conversation.isRoomType()) {
            videoCallBtn.setVisibility(View.GONE);
            audioCallBtn.setVisibility(View.GONE);
        } else {
            videoCallBtn.setVisibility(View.VISIBLE);
            audioCallBtn.setVisibility(View.VISIBLE);
        }
    }
}
