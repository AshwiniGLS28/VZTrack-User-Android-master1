package vztrack.gls.com.vztrack_user.fragment;

/**
 * Created by sandeep on 14/3/16.
 */
import android.content.Context;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.activity.BaseFragment;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.adapters.Rating_RecyclerViewAdapter;
import vztrack.gls.com.vztrack_user.beans.DataObjectRating;
import vztrack.gls.com.vztrack_user.beans.OutputBeanSearchProvider;
import vztrack.gls.com.vztrack_user.beans.RatingBean;
import vztrack.gls.com.vztrack_user.responce.RatingResponseBean;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.DbHelper;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.PostData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

public class RatingFragment extends BaseFragment {
    BaseFragment baseFragment;
    TextView nolist;
    ArrayList searched_results = new ArrayList<DataObjectRating>();
    Context context;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList results;
    DbHelper dbHelper;
    String img_URL,heading,description,noticeStartdate,noticeEndDate;
    CheckConnection cc;
    public static SwipeRefreshLayout mSwipeRefreshLayout;
    public static int clearFlag = 0;
    public ArrayList<RatingBean> ratingBeanArrayList = new ArrayList<RatingBean>();
//    public ArrayList<OutputBeanSearchProvider> outputBeanSearchProviders = new ArrayList<OutputBeanSearchProvider>();
    public  ArrayList<DataObjectRating> Updated_result_rating = new ArrayList<>();
    public RatingFragment() {

    }
    public static RatingFragment newInstance(int page, String title) {
        RatingFragment fragmentFirst = new RatingFragment();
        return fragmentFirst;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_rating, null);

        CleverTap.cleverTap_Record_Event(getActivity(), Events.event_rating_screen);
baseFragment=this;
        dbHelper = new DbHelper(getActivity());
        cc = new CheckConnection(getActivity());
        setHasOptionsMenu(true);
        context = getActivity();
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.activity_main_swipe_refresh_layout);
        nolist=root.findViewById(R.id.nolist);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.rating_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        if (clearFlag == 1) {
            Updated_result_rating.clear();
            clearFlag = 0;
        }
        try
        {
            mAdapter = new Rating_RecyclerViewAdapter(context, getDataSet("All"));
        }catch (Exception ex)
        {
            Log.e("Ex in Rating Frag "," "+ex);
        }

        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(cc.isConnectingToInternet())
                {
                    Updated_result_rating.clear();
                    clearFlag = 1;
                    MainActivity.fragment_flag=1;
                    RatingResponseBean ratingResponseBean = new RatingResponseBean();
                    ratingResponseBean.setSocietyId(Integer.parseInt(SheredPref.getSocietyId(context)));
                    ratingResponseBean.setFamilyId(Integer.parseInt(SheredPref.getFamilyId(context)));
                    new PostData(new Gson().toJson(ratingResponseBean),baseFragment , CallFor.PENDING_RATING).execute();
                }
                else
                {
                    mSwipeRefreshLayout.setRefreshing(false);
                    CommonMethods.showToastError(context, "Unable to refresh, Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                   // Toast.makeText(context,"Unable To Refresh , No Internet Connection",Toast.LENGTH_LONG).show();
                }
            }
        });

        if (cc.isConnectingToInternet()) {
            MainActivity.fragment_flag = 1;
            Updated_result_rating.clear();
            RatingResponseBean ratingResponseBean = new RatingResponseBean();
            ratingResponseBean.setSocietyId(Integer.parseInt(SheredPref.getSocietyId(context)));
            ratingResponseBean.setFamilyId(Integer.parseInt(SheredPref.getFamilyId(context)));
            new PostData(new Gson().toJson(ratingResponseBean), baseFragment, CallFor.PENDING_RATING).execute();
        } else {
            CommonMethods.showToastSuccess(getActivity(),"No internet connection");
        }
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        setHasOptionsMenu(true);
        menu.findItem(R.id.action_call).setVisible(false);
        final MenuItem item = menu.findItem(R.id.action_search);
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
                searched_results.clear();
                ArrayList filteredList;
                filteredList = getDataSet(query);
                try
                {
                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(context);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new Rating_RecyclerViewAdapter(context,filteredList);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
                catch (Exception ex)
                {
                    Log.e("Ex in Rating Frag "," "+ex);
                }
                return true;
            }
        });
    }


    private ArrayList<DataObjectRating> getDataSet(String startWith) {
        results = new ArrayList<DataObjectRating>();
        if (startWith.equals("All") || startWith.equals("")) {
            if (startWith.equals("")) {
                Updated_result_rating.addAll(results);
                return Updated_result_rating;
            } else {
                if (cc.isConnectingToInternet()) {
                    for (int index = 0; index < ratingBeanArrayList.size(); index++) {
                        DataObjectRating obj = new DataObjectRating(
                                ratingBeanArrayList.get(index).getVisitorName(),
                                ratingBeanArrayList.get(index).getVisitorMobile(),
                                ratingBeanArrayList.get(index).getVisitorPurpose(),
                                ratingBeanArrayList.get(index).getInTime(),
                                ratingBeanArrayList.get(index).getVisitorPhoto()
                        );
                        results.add(obj);
                    }
                } else {
                }
                Updated_result_rating.addAll(results);
                return Updated_result_rating;
            }
        } else {
            if (cc.isConnectingToInternet()) {
                for (int index = 0; index < Updated_result_rating.size(); index++) {
                    DataObjectRating obj = Updated_result_rating.get(index);
                    String check_name = obj.getMvisitorName();
                    if (check_name.toLowerCase().startsWith(startWith) || check_name.toLowerCase().contains(startWith)) {
                        DataObjectRating objNew = new DataObjectRating(
                                obj.getMvisitorName(),
                                obj.getMvisitorMobile(),
                                obj.getMvisitorPurpose(),
                                obj.getMinTime(),
                                obj.getMvisitorPhoto()
                        );
                        results.add(objNew);
                    }
                }
            } else {
            }
            searched_results.addAll(results);
            return searched_results;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        MainActivity.outputBeanSearchProviders.clear();
        if (cc.isConnectingToInternet()) {
                Updated_result_rating.clear();
                RatingResponseBean ratingResponseBean = new RatingResponseBean();
                ratingResponseBean.setSocietyId(Integer.parseInt(SheredPref.getSocietyId(context)));
                ratingResponseBean.setFamilyId(Integer.parseInt(SheredPref.getFamilyId(context)));
                new PostData(new Gson().toJson(ratingResponseBean), this, CallFor.PENDING_RATING).execute();
        } else {
//            ft.replace(R.id.main, new NoInternetFragment(), "DATA").commitAllowingStateLoss();
        }
    }

    @Override
    public void onGetResponse(String response, String callFor) {

        if (callFor.equals(CallFor.PENDING_RATING)) {
//            getSupportActionBar().setTitle("Service Feedback");
            try {
                ratingBeanArrayList= new ArrayList<RatingBean>();
                ratingBeanArrayList = new Gson().fromJson(response, new TypeToken<ArrayList<RatingBean>>() {
                }.getType());
                if (MainActivity.fragment_flag == 1) {
                    Log.e("bvbb","hh");
                    RatingFragment.mSwipeRefreshLayout.setRefreshing(false);
                    MainActivity.fragment_flag = 0;
                    nolist.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    try
                    {
                        mAdapter = new Rating_RecyclerViewAdapter(context, getDataSet("All"));
                    }catch (Exception ex)
                    {
                        Log.e("Ex in Rating Frag "," "+ex);
                    }
                }
                if (ratingBeanArrayList.size() == 0) {
//                    MainActivity.NoVisiterLayout.setVisibility(View.GONE);
                    nolist.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                    nolist.setText("No provider available for feedback");
                } else {
                    nolist.setVisibility(View.GONE);
//                    MainActivity.NoVisiterLayout.setVisibility(View.VISIBLE);
//                    MainActivity.NoDataLayout.setVisibility(View.GONE);
                }
//                ft = fragmentManager.beginTransaction();
//                ft.replace(R.id.main, new Nw_service_provider_fragment(), "Data").commitAllowingStateLoss();
            } catch (Exception ex) {
                Log.e("EXCEPTION P Rating", " " + ex);
            }

        }
    }
}
