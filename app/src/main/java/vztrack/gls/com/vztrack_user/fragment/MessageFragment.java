package vztrack.gls.com.vztrack_user.fragment;

/**
 * Created by sandeep on 14/3/16.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.activity.BaseFragment;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.SendMessageActivity;
import vztrack.gls.com.vztrack_user.adapters.Message_RecyclerViewAdapter;
import vztrack.gls.com.vztrack_user.adapters.SentMessageAdapter;
import vztrack.gls.com.vztrack_user.beans.DataObjectMessage;
import vztrack.gls.com.vztrack_user.beans.MessageBean;
import vztrack.gls.com.vztrack_user.responce.MessageResponceBean;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

import static android.app.Activity.RESULT_OK;

public class MessageFragment extends BaseFragment {
    String TAG = "MESSAGE FRAGMENT";
    Context context;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList results;
    CheckConnection cc;
    public static SwipeRefreshLayout mSwipeRefreshLayout;
    public static SwipeRefreshLayout mSwipeRefreshLayoutSentMsg;
    private FloatingActionButton fab;
    LinearLayout NoDataLayout;
    LinearLayout receivedPage;
    LinearLayout tab;
    RelativeLayout sentPage;
    View receivedIndicator, sentIndicator;
    RelativeLayout receivedTab, sentTab;
    ListView sentMessageList;
    Menu menu;
    LinearLayout noImageDataLayout;
    SentMessageAdapter adapter;
    ArrayList<MessageBean> sentMessages = new ArrayList<>();
    int PAGE_SIZE = 10;
    int PAGE_NO = 0;
    String getAdmin = "";

    MessageFragment messageFragment;
    TextView txtrecievedtab,tabsent;

    public MessageFragment() {
        messageFragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_message, null);
        CleverTap.cleverTap_Record_Event(getActivity(), Events.event_message_screen);
        cc = new CheckConnection(getActivity());
        setHasOptionsMenu(true);
        context = getActivity();
//        UtilityMethodsAndroid.setActionBarTitle(getActivity());
        String title= UtilityMethods.getTitleFromMenuJson(getResources().getString(R.string.message),context);
        Log.e("titlevisitor",title+"");
        ((MainActivity) context).getSupportActionBar().setTitle(title);
        SheredPref.setMessageCount(getActivity(), 0);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayoutSentMsg = (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout_for_sent);
        txtrecievedtab=root.findViewById(R.id.txtrecievedtab);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.notice_recycler_view);
        fab = (FloatingActionButton) root.findViewById(R.id.fab);
        NoDataLayout = (LinearLayout) root.findViewById(R.id.NoDataLayout);
        receivedPage = root.findViewById(R.id.receivedPage);
        sentPage = root.findViewById(R.id.sentPage);
        receivedIndicator = root.findViewById(R.id.receivedTabIndicator);

        sentIndicator = root.findViewById(R.id.sentTabIndicator);
        tabsent=root.findViewById(R.id.tabsent);
        receivedTab = root.findViewById(R.id.receivedTab);
        sentTab = root.findViewById(R.id.sentTab);
        noImageDataLayout = root.findViewById(R.id.noImageDataLayout);
        sentMessageList = root.findViewById(R.id.sentMessageList);
        tab = root.findViewById(R.id.tabs);

        if(MainActivity.sentMessage == 1){
            Log.e(TAG, "sent", null);
            callSentView();
        }else if(MainActivity.sentMessage == 2){
            Log.e(TAG, "Receive", null);
            callReceiveView();
        }

        if(!SheredPref.getAdminAccess(getActivity())){
            fab.setVisibility(View.GONE);
            tab.setVisibility(View.GONE);

        }

        adapter = new SentMessageAdapter(this,R.layout.sent_msg_row, sentMessages);
        sentMessageList.setAdapter(adapter);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if(MainActivity.messageResponceBean == null || MainActivity.messageResponceBean.getMessageBeans().size()==0){
            if(MainActivity.message_PageNo==0){
                NoDataLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        }
        else{
            NoDataLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        try
        {
            mAdapter = new Message_RecyclerViewAdapter(context, getDataSet("All"));
        }catch (Exception ex)
        {
            Log.e("Ex in Message Frag"," "+ex);
        }

        mRecyclerView.setAdapter(mAdapter);

        if (MainActivity.message_PageNo != 0) {
            mRecyclerView.scrollToPosition(MainActivity.message_PageNo * 10);
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(cc.isConnectingToInternet())
                {
                    MainActivity.fragment_flag=1;
                    MainActivity.updatedMessageResponceBean.clear();
                    MainActivity.message_PageNo = 0;
                    new GetData(MainActivity.mainActivity, CallFor.MESSAGE, ""+0).execute();
                }
                else
                {
                    mSwipeRefreshLayout.setRefreshing(false);
                    CommonMethods.showToastError(context, "Unable to refresh, Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                if(cc.isConnectingToInternet())
                {
                    Intent intent = new Intent(context, SendMessageActivity.class);
                    startActivity(intent);
                }
                else{
                    CommonMethods.showToastError(context, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                }
            }
        });


        //tab on click listener
            sentTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "onClick: sentTab",null );
                        if(sentIndicator.getVisibility() != View.VISIBLE) {
                            txtrecievedtab.setTextColor(getResources().getColor(R.color.gray));
                            receivedIndicator.setBackgroundColor(getResources().getColor(R.color.fragmentBackground));
                            tabsent.setTextColor(getResources().getColor(R.color.colorPrimary));
                            sentIndicator.setBackgroundColor(getResources().getColor(R.color.cardviewbordercolor));
                            MainActivity.sentMessage = 1;
                            callSentView();
                            mSwipeRefreshLayoutSentMsg.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                @Override
                                public void onRefresh() {
                                    if(cc.isConnectingToInternet())
                                    {
                                        sentMessages.clear();
                                        PAGE_NO = 0;
                                        loadMessages();
                                    }
                                    else
                                    {
                                        mSwipeRefreshLayoutSentMsg.setRefreshing(false);
                                        CommonMethods.showToastError(context, "Unable to refresh, Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                                    }
                                }
                            });
                        }
                    }
            });

        receivedTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(receivedIndicator.getVisibility() != View.VISIBLE) {
                        txtrecievedtab.setTextColor(getResources().getColor(R.color.colorPrimary));
                        receivedIndicator.setBackgroundColor(getResources().getColor(R.color.cardviewbordercolor));
                        tabsent.setTextColor(getResources().getColor(R.color.gray));
                        sentIndicator.setBackgroundColor(getResources().getColor(R.color.fragmentBackground));
                            callReceiveView();
                        MainActivity.sentMessage = 2;
                            MainActivity.fragment_flag=1;
                            MainActivity.updatedMessageResponceBean.clear();
                            MainActivity.message_PageNo = 0;
                            new GetData(MainActivity.mainActivity, CallFor.MESSAGE, ""+0).execute();
                        }
                    }
             });
        return root;
    }
    public void callSentView(){
        sentIndicator.setVisibility(View.VISIBLE);
        sentPage.setVisibility(View.VISIBLE);
        receivedPage.setVisibility(View.GONE);
        receivedIndicator.setVisibility(View.GONE);
        sentMessages.clear();
        loadMessages();
    }

    public void callReceiveView(){
        Log.e(TAG, "onClick: receive tab",null );
        sentIndicator.setVisibility(View.GONE);
        sentPage.setVisibility(View.GONE);
        receivedPage.setVisibility(View.VISIBLE);
        receivedIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
//        MainActivity.backPressFlag=0;
//        if(MainActivity.sentMessage == 1){
//            Log.e(TAG, "sent", null);
//            callSentView();
//        }
   }

    private ArrayList<DataObjectMessage> getDataSet(String startWith) {
        results = new ArrayList<DataObjectMessage>();
        for (int index = 0; index < MainActivity.messageResponceBean.getMessageBeans().size(); index++) {
            DataObjectMessage obj = new DataObjectMessage(
                    MainActivity.messageResponceBean.getMessageBeans().get(index).getMessage(),
                    MainActivity.messageResponceBean.getMessageBeans().get(index).getGroupName(),
                    MainActivity.messageResponceBean.getMessageBeans().get(index).getSent_date()
            );
            results.add(obj);
        }
        MainActivity.updatedMessageResponceBean.addAll(results);
        return MainActivity.updatedMessageResponceBean;
    }

    public void manageSentListUI(){
        if(sentMessages.size() == 0){
            noImageDataLayout.setVisibility(View.VISIBLE);
            sentMessageList.setVisibility(View.GONE);
        } else {
            noImageDataLayout.setVisibility(View.GONE);
            sentMessageList.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onGetResponse(String response, String callFor) {
        if(callFor.equals(CallFor.SENT_MESSAGE_API)){
            MessageResponceBean messageResponce = new Gson().fromJson(response, MessageResponceBean.class);
            if(messageResponce.getCode().equals("SUCCESS")){
                if(messageResponce.getMessageBeans().size() == PAGE_SIZE){
                    messageResponce.getMessageBeans().add(null);
                }
                if(PAGE_NO != 0){
                    sentMessages.remove(sentMessages.size()-1);
                }
                sentMessages.addAll(messageResponce.getMessageBeans());
                manageSentListUI();
                mSwipeRefreshLayoutSentMsg.setRefreshing(false);
            } else {
                Toast.makeText(getActivity().getApplicationContext(),messageResponce.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void loadMore(){
        Log.e(TAG, "loadMore: ",null );
        PAGE_NO ++;
        loadMessages();
    }

    public void loadMessages(){
        new GetData(messageFragment,CallFor.SENT_MESSAGE_API,"?pageNo="+PAGE_NO).execute();
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                Log.e("onactivity ","result");
                MainActivity.backPressFlag=0;
                if(MainActivity.sentMessage == 1){
                    Log.e(TAG, "sent", null);
                    callSentView();
                }
            }
        }

    }
}


