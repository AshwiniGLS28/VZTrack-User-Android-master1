package vztrack.gls.com.vztrack_user.fragment;

/**
 * Created by sandeep on 14/3/16.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ale.infra.contact.Contact;
import com.ale.infra.contact.IRainbowContact;
import com.ale.infra.contact.RainbowPresence;
import com.ale.infra.list.IItemListChangeListener;
import com.ale.infra.proxy.conversation.IRainbowConversation;
import com.ale.listener.IRainbowInvitationManagementListener;
import com.ale.rainbowsdk.RainbowSdk;

import java.util.List;

import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.activity.Rainbow_MessageActivity;
import vztrack.gls.com.vztrack_user.adapters.Rainbow_ConversationRecyclerAdapter;
import vztrack.gls.com.vztrack_user.utils.CleverTapRegisterEvents;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

public class Rainbow_Conversation_Fragment extends Fragment implements Contact.ContactListener {

    private final String TAG = "Rainbow Conversation-->";
    public static boolean isMessageActivityOpen;
    private Rainbow_ConversationRecyclerAdapter m_adapter;
    private MainActivity m_activity;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout NoDataLayout;
    private ImageView noDataImage;
    private TextView noDataText;
    private ProgressDialog progressDialog;
    public Rainbow_Conversation_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_conv, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.bottom_menu_item1));
        setHasOptionsMenu(true);
        Log.e(TAG, "onCreateView");
        CleverTapRegisterEvents.addCleverTapEvent(getActivity(), Events.event_rainbow_conversation_screen);
        progressDialog = ProgressDialog.show(getActivity(),"","Loading...");
        mRecyclerView = (RecyclerView) root.findViewById(R.id.list_view_conversations);
        NoDataLayout = (LinearLayout) root.findViewById(R.id.NoDataLayout);
        noDataText = (TextView) root.findViewById(R.id.noDataText);
        noDataImage = (ImageView) root.findViewById(R.id.noDataImage);
        RainbowSdk.instance().conversations().getAllConversations().registerChangeListener(m_conversationsListener);
        RainbowSdk.instance().contacts().getRainbowContacts().registerChangeListener(m_contactsListener);
        registerContactsOfConversationsList();
        m_adapter = new Rainbow_ConversationRecyclerAdapter(m_activity, iRainbowInvitationManagementListener);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(m_activity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(m_adapter);
        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                mRecyclerView.removeOnLayoutChangeListener(this);
                progressDialog.dismiss();
                updateLayout();
            }
        });

        RainbowSdk.instance().contacts().registerInvitationsListener(iRainbowInvitationManagementListener);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        setHasOptionsMenu(true);
        menu.findItem(R.id.action_call).setVisible(false);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        boolean type = SheredPref.getType(getContext());
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
                if (query.equals("")) {
                    m_adapter.updateConversations();
                } else {
                    m_adapter.updateSearchedConversations(query);
                }
                return true;
            }
        });
    }

    @Override
    public void onDestroyView() {
        Log.e(TAG, "onDestroyView");
        MainActivity.rainbowFragmentFlag = false;
        try {
            RainbowSdk.instance().conversations().getAllConversations().unregisterChangeListener(m_conversationsListener);
            RainbowSdk.instance().contacts().getRainbowContacts().unregisterChangeListener(m_contactsListener);
            RainbowSdk.instance().contacts().unregisInvitationsterListener(iRainbowInvitationManagementListener);
            unregisterAllContacts();
        } catch (Exception ex) {
            Log.e("Rainbow", "Error in unregister");
        }
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            m_activity = (MainActivity) context;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (activity instanceof MainActivity) {
                m_activity = (MainActivity) activity;
            }
        }
    }

    @Override
    public void onPause() {
        Log.e(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void contactUpdated(Contact contact) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                m_adapter.updateConversations();
            }
        });
    }

    @Override
    public void onPresenceChanged(Contact contact, final RainbowPresence rainbowPresence) {
        try{
            if (isMessageActivityOpen) {
                String id = contact.getImJabberId();
                ((Rainbow_MessageActivity)getActivity()).setPresence(rainbowPresence.toString(), id);
            }
        }catch (Exception ex){
            Log.e(TAG, "Exception : "+ex);
        }
    }


    @Override
    public void onActionInProgress(boolean b) {

    }

    /**
     * Unregister all contacts
     */
    private void unregisterAllContacts() {
        for (IRainbowContact contact : RainbowSdk.instance().contacts().getRainbowContacts().getCopyOfDataList()) {
            contact.unregisterChangeListener(this);
        }
    }

    private IItemListChangeListener m_conversationsListener = new IItemListChangeListener() {
        @Override
        public void dataChanged() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    m_adapter.updateConversations();
                    updateLayout();
                    unregisterAllContacts();
                    registerContactsOfConversationsList();
                }
            });
        }
    };

    private IItemListChangeListener m_contactsListener = new IItemListChangeListener() {
        @Override
        public void dataChanged() {
            unregisterAllContacts();
            registerContactsOfConversationsList();
        }
    };

    /**
     * Only listen to contacts of the list of conversations
     */
    private void registerContactsOfConversationsList() {
        for (IRainbowConversation conversation : RainbowSdk.instance().conversations().getAllConversations().getItems()) {
            if (conversation.getContact() != null) {
                conversation.getContact().registerChangeListener(this);
            }
        }
    }

    public void setDataLayout(){
        mRecyclerView.setVisibility(View.VISIBLE);
        NoDataLayout.setVisibility(View.INVISIBLE);
    }

    private void setNoDataLayout() {
        NoDataLayout.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        noDataText.setText(getResources().getString(R.string.empty_list));
        noDataImage.setVisibility(View.VISIBLE);
    }

    private void updateLayout(){
        int count = 0;
        if (m_adapter != null) {
            count = m_adapter.getItemCount();
        }
        if(count==0){
            setNoDataLayout();
        }else{
            setDataLayout();
        }
    }

    private IRainbowInvitationManagementListener iRainbowInvitationManagementListener = new IRainbowInvitationManagementListener() {
        @Override
        public void onAcceptSuccess() {
            m_activity.runOnUiThread(() -> {
                m_adapter.closeProgressDialog();
                CommonMethods.showToastSuccess(m_activity, "Successfully added to your network");
                m_adapter.updateConversations();
            });
        }

        @Override
        public void onDeclineSuccess() {
            m_activity.runOnUiThread(() -> {
                m_adapter.closeProgressDialog();
                m_activity.runOnUiThread(() -> CommonMethods.showToastSuccess(m_activity, "Invitation rejected"));
                m_adapter.updateConversations();
            });
        }

        @Override
        public void onError() {
            m_activity.runOnUiThread(() -> {
                m_adapter.closeProgressDialog();
                m_activity.runOnUiThread(() -> CommonMethods.showToastSuccess(m_activity, "Error in invitation"));
                m_adapter.updateConversations();
            });
        }

        @Override
        public void onNewReceivedInvitation(List<IRainbowContact> list) {
            m_activity.runOnUiThread(() -> m_adapter.updateConversations());
        }
    };
}