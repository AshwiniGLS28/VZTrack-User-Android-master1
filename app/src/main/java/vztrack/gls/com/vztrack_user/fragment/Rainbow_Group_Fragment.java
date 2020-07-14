package vztrack.gls.com.vztrack_user.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.ale.infra.list.IItemListChangeListener;
import com.ale.rainbowsdk.RainbowSdk;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.activity.Rainbow_Create_Group;
import vztrack.gls.com.vztrack_user.adapters.Rainbow_Groups_Recyclerview_Adapter;

public class Rainbow_Group_Fragment extends Fragment {

    Button fab;
    Context context;
    RecyclerView recyclerView;
    LinearLayout NoDataLayout;
    private LinearLayoutManager layoutManager;
    private Rainbow_Groups_Recyclerview_Adapter adapter;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_rainbow_group, null);

        progressDialog = progressDialog.show(getActivity(),"","Loading...");

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.bottom_menu_item2));
        fab = root.findViewById(R.id.fab);
        recyclerView = (RecyclerView) root.findViewById(R.id.group_recycler_view);
        NoDataLayout = (LinearLayout) root.findViewById(R.id.NoDataLayout);

        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoNextActivity();
            }
        });
        context = getActivity();
        setHasOptionsMenu(true);
        adapter = new Rainbow_Groups_Recyclerview_Adapter(getActivity());
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        RainbowSdk.instance().bubbles().getAllBubbles().registerChangeListener(m_changeListener);
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                recyclerView.removeOnLayoutChangeListener(this);
                progressDialog.dismiss();
                updateLayout();
            }
        });
        return root;
    }

    @Override
    public void onDestroy() {
        try {
            RainbowSdk.instance().bubbles().getAllBubbles().unregisterChangeListener(m_changeListener);
        }catch (Exception ex){
            Log.e("Group_Fragment"," Unable to unregister listener");
        }
        super.onDestroy();
    }

    public void gotoNextActivity(){
        Intent intent = new Intent(context, Rainbow_Create_Group.class);
        intent.putExtra("CallFrom","CREATE_GROUP");
        startActivity(intent);
    }

    private IItemListChangeListener m_changeListener = new IItemListChangeListener() {
        @Override
        public void dataChanged() {
            MainActivity.mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.updateGroupList();
                    recyclerView.smoothScrollToPosition(ScrollView.FOCUS_UP);
                    updateLayout();
                }
            });
        }
    };

    public void setDataLayout(){
        recyclerView.setVisibility(View.VISIBLE);
        NoDataLayout.setVisibility(View.INVISIBLE);
    }

    private void setNoDataLayout() {
        NoDataLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    private void updateLayout(){
        int count = 0;
        if (adapter != null) {
            count = adapter.getItemCount();
        }
        if(count==0){
            setNoDataLayout();
        }else{
            setDataLayout();
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        setHasOptionsMenu(true);
        menu.findItem(R.id.action_call).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        searchView.setVisibility(View.GONE);

    }
}