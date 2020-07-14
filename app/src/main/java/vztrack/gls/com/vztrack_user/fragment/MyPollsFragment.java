package vztrack.gls.com.vztrack_user.fragment;

/**
 * Created by sandeep on 14/3/16.
 */

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.core.view.MenuItemCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.activity.BaseActivity;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.adapters.UserPoll_RecyclerViewAdapter;
import vztrack.gls.com.vztrack_user.responce.PollResponce;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

public class MyPollsFragment extends Fragment {

    BaseActivity context;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    CheckConnection cc;
    public static SwipeRefreshLayout mSwipeRefreshLayout;
    private String TAG = "User Poll Activity";
    PollResponce pollResponce;
    PollResponce ChangingPollResponce;

    public MyPollsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_my_polls, null);
        context = MainActivity.mainActivity;
        CleverTap.cleverTap_Record_Event(getActivity(), Events.poll_screen_user);

        pollResponce = PollsFragment.pollResponce;
        ChangingPollResponce = PollsFragment.pollResponce;

        cc = new CheckConnection(getActivity());
        setHasOptionsMenu(true);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.activity_main_swipe_refresh_layout);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.mypoll_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        try
        {
            mAdapter = new UserPoll_RecyclerViewAdapter(context, ChangingPollResponce);
        }catch (Exception ex)
        {
            Log.e(TAG," Exception In Adapter Setting "+ex);
        }

        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(cc.isConnectingToInternet()) {
                    SheredPref.setRun(context, "DONT RUN");
                    new GetData(MainActivity.mainActivity, CallFor.USER_POLLS, "").execute();
                }
                else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    CommonMethods.showToastError(context, "Unable to refresh, Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                }
            }
        });


        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        setHasOptionsMenu(true);
        menu.findItem(R.id.action_call).setVisible(false);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setQueryHint("Search By Poll Name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                query = query.toLowerCase();
                try {
                    final String finalQuery = query;
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            ChangingPollResponce = getFilteredDataSet(finalQuery);
                            mRecyclerView.setHasFixedSize(true);
                            mLayoutManager = new LinearLayoutManager(context);
                            mRecyclerView.setLayoutManager(mLayoutManager);
                            mAdapter = new UserPoll_RecyclerViewAdapter(context, ChangingPollResponce);
                            mRecyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (Exception ex) {
                    Log.e("Ex ", " " + ex);
                }
                return true;
            }
        });
    }

    public PollResponce getFilteredDataSet(String query){
        PollResponce filteredPollResponce = new PollResponce();
        if(query.equals("")){
            filteredPollResponce = pollResponce;
        }else{
            for(int i=0;i<pollResponce.getObjPollBeans().size();i++){
                String pollName = pollResponce.getObjPollBeans().get(i).getName().toLowerCase();
                if(pollName.contains(query)){
                    //Log.e("1 = "+pollResponce.getObjPollBeans().get(i).getName(),"2 = "+query);
                    filteredPollResponce.getObjPollBeans().add(pollResponce.getObjPollBeans().get(i));
                }
            }
        }
        return filteredPollResponce;
    }

}