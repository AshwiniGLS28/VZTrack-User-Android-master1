package vztrack.gls.com.vztrack_user.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.adapters.SentMessageDetailAdaptor;
import vztrack.gls.com.vztrack_user.beans.MessageDetailBean;
import vztrack.gls.com.vztrack_user.responce.MessageResponceBean;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.GetData;

public class SentMessageDetailActivity extends BaseActivity {
    String TAG = "MESSAGE DETAILS";
    TextView sentByTV,readByTV,messageTextTV,sentDateTV,sentToTV;
    Button viewBtn;
    View lineV;
    ArrayList<MessageDetailBean> displayList = new ArrayList<MessageDetailBean>();
    ListView sentMessageList;
    SentMessageDetailAdaptor adapter;
    ArrayList<MessageDetailBean> messageList = new ArrayList<>();
    EditText filterET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_message_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        sentMessageList = findViewById(R.id.messageDetailList);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Message Detail");

        TextView title=toolbar.findViewById(R.id.title);
        title.setText("Message Details");

        ImageView backpress=findViewById(R.id.backpress);
        backpress.setOnClickListener(v->onBackPressed());
        //intent data
        int messageId = getIntent().getIntExtra("ID",0);
        String messageText = getIntent().getStringExtra("MESSAGE_TEXT");
        String messageDate = getIntent().getStringExtra("MESSAGE_DATE");
        String sentBy = getIntent().getStringExtra("SENT_BY");
        String readBy = getIntent().getStringExtra("READ_BY");
        String sentTo = getIntent().getStringExtra("SENT_TO");

        //find Ids
//
        sentByTV = findViewById(R.id.sentBy);
        readByTV = findViewById(R.id.readBy) ;
        messageTextTV =  findViewById(R.id.messageText);
        sentDateTV = findViewById(R.id.sentDate);
        viewBtn =  findViewById(R.id.viewBtn);
        lineV = findViewById(R.id.line);
        sentToTV = findViewById(R.id.sentTo);
        filterET = findViewById(R.id.filterET);
        filterET.addTextChangedListener(new Watcher());
//
//        // view btn hide // set text for message detail
        viewBtn.setVisibility(View.INVISIBLE);

        sentByTV.setText("Sent By : "+sentBy);
        readByTV.setText("Read By : "+readBy);
        if(messageText.length() > 100){
            messageText = messageText.substring(0, 100);
            messageText = messageText+" ...";
        }
        messageTextTV.setText(messageText);
        sentDateTV.setText(messageDate);
        sentToTV.setText("Sent To : "+sentTo);
        adapter = new SentMessageDetailAdaptor(getApplicationContext(),R.layout.sent_msg_detail_row,displayList);
        sentMessageList.setAdapter(adapter);

        //api hit
        if(messageId !=0) {
            new GetData(this, CallFor.SENT_MESSAGE_DETAIL, "?messageId=" + messageId).execute();//if for calFor in getData
        } else {
            Toast.makeText(getApplicationContext(),"Invalid Message detail..", Toast.LENGTH_LONG).show();
            onBackPressed();
        }
    }

    public void setReadCount(){
        int cnt = 0;
        for(int i=0; i< messageList.size(); i++){
            if(messageList.get(i).isReadStatus()){
                cnt ++;
            }
        }

        readByTV.setText("Read By : "+cnt+" / "+messageList.size());

    }

    @Override
    public void onGetResponse(String response, String callFor) {
        //handle response
        if(callFor.equals(CallFor.SENT_MESSAGE_DETAIL)){
            MessageResponceBean messageResponce = new Gson().fromJson(response, MessageResponceBean.class);
            Log.e(TAG, "onGetResponse: "+messageResponce);
            if(messageResponce.getCode().equals("SUCCESS")){
                messageList.addAll(messageResponce.getMessageDetailBeans());
                setReadCount();
                applyFilter();
            } else {
                Toast.makeText(getApplicationContext(),messageResponce.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return  true;
    }

    public void applyFilter(){
        displayList.clear();
        String filterText = filterET.getText().toString();
        if(filterText.length() == 0){
            displayList.addAll(messageList);
            adapter.notifyDataSetChanged();
        } else {
            for(int i = 0; i < messageList.size(); i++){
                if((messageList.get(i).getReadBy()+messageList.get(i).getFlatNo()).toUpperCase().contains(filterText.toUpperCase())){
                    displayList.add(messageList.get(i));
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    class Watcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            applyFilter();
        }
    }

    public void clearFilter(View v){
        filterET.setText("");
    }
}
