package vztrack.gls.com.vztrack_user.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.BaseFragment;
import vztrack.gls.com.vztrack_user.activity.InvitationActivity;
import vztrack.gls.com.vztrack_user.adapters.InvitationsAdapter;
import vztrack.gls.com.vztrack_user.beans.InvitationBean;
import vztrack.gls.com.vztrack_user.responce.InvitationListResponse;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.GetData;

public class InviteGuestFragment  extends BaseFragment {
    ListView invitationList;
    ArrayList<InvitationBean> invitations;
    InvitationsAdapter adapter;
    LinearLayout noDataLL,noImageDataLayout;
    TextView message,nodatatext;
    ProgressBar progress_bar;
    // newInstance constructor for creating fragment with arguments
    public static InviteGuestFragment newInstance(int page, String title) {
        InviteGuestFragment fragmentFirst = new InviteGuestFragment();
//        Bundle args = new Bundle();
////        args.putInt("someInt", page);
////        args.putString("someTitle", title);
////        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_invitation_list, null);
        setHasOptionsMenu(true);
        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), InvitationActivity.class);
                intent.putExtra("CALL_FOR","NEW");
                startActivity(intent);
            }
        });

        invitationList = root.findViewById(R.id.invitationList);
        noImageDataLayout=root.findViewById(R.id.noImageDataLayout);
        noDataLL = root.findViewById(R.id.noDataLL);
        nodatatext=root.findViewById(R.id.nodatatext);
        message = root.findViewById(R.id.messageText);
        progress_bar = root.findViewById(R.id.progress_bar);
        invitations = new ArrayList<>();
        adapter = new InvitationsAdapter(getActivity(), R.layout.invitation_row,invitations);
        invitationList.setAdapter(adapter);
        invitationList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(invitations.get(position) == null){
                    return;
                }
                Intent intent = new Intent(getActivity(), InvitationActivity.class);
                intent.putExtra("CALL_FOR","DETAIL");
                intent.putExtra("PURPOSE",invitations.get(position).getPurpose());
                intent.putExtra("DATE",invitations.get(position).getInvitedDate());
                intent.putExtra("ADD_INFO",invitations.get(position).getDescription());
                intent.putExtra("ID",invitations.get(position).getInvitationId());
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetData(this, CallFor.INVITATION_LIST, "").execute();
    }

    @Override
    public void onGetResponse(String response, String callFor) {
        if(callFor.equals(CallFor.INVITATION_LIST)){
            InvitationListResponse invitationListResponse = new Gson().fromJson(response, InvitationListResponse.class);
            if(invitationListResponse.getCode().equals("SUCCESS")){
                try {
                    invitations.clear();
                    invitations.addAll(invitationListResponse.getInvitationsResponse());
                } catch (Exception e){

                }
                setInvitationList();
            } else {

            }
        }
    }

    public void setInvitationList(){
        if(invitations.size()>0){
            if (invitations.get(invitations.size() - 1) != null) {
                invitations.add(null);
            }
            adapter.notifyDataSetChanged();
            invitationList.setVisibility(View.VISIBLE);
            noDataLL.setVisibility(View.GONE);
            noImageDataLayout.setVisibility(View.GONE);
        } else {
            noDataLL.setVisibility(View.GONE);
            nodatatext.setText("You have not created\nany invitation.");
            noImageDataLayout.setVisibility(View.VISIBLE);
//            nodatatext.setText("You have no pre approved\nvisitor.");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        Log.e("onCreateOptionsMenu","onCreateOptionsMenu");
//        menu.clear();
//        inflater.inflate(R.menu.main, menu);
//        setHasOptionsMenu(true);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
//        searchView.setVisibility(View.GONE);
//    }
}
