package vztrack.gls.com.vztrack_user.fragment;

/**
 * Created by sandeep on 14/3/16.
 */

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.activity.BaseActivity;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.activity.MarketPlacePlaceNewAd;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.responce.MarketPlaceResponceBean;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

public class MarketPlaceFragment extends Fragment {

    public static BaseActivity context;
    public static RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    CheckConnection cc;
    public static SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton fab;
    public static LinearLayout NoDataLayout;
    public  static TextView NoDataText;
    public static MarketPlaceResponceBean filteredMarketPlaceResponceBean;
    public MarketPlaceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_market_place, null);
        cc = new CheckConnection(getActivity());
        setHasOptionsMenu(true);
        String title= UtilityMethods.getTitleFromMenuJson(getResources().getString(R.string.marketplace),getActivity());
        Log.e("titlevisitor",title+"");
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);
        context = MainActivity.mainActivity;
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.activity_main_swipe_refresh_layout);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.complain_recycler_view);
        fab = (FloatingActionButton) root.findViewById(R.id.fab);
        NoDataLayout = (LinearLayout) root.findViewById(R.id.NoDataLayout);
        NoDataText = (TextView) root.findViewById(R.id.NoDataText);
        CleverTap.cleverTap_Record_Event(getActivity(), Events.market_place_screen);

        if(cc.isConnectingToInternet()){
            new GetData(context, CallFor.GET_ALL_MARKET_PLACE_ADD, "").execute();
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                context.supportInvalidateOptionsMenu();
                if(cc.isConnectingToInternet())
                {
                    new GetData(context, CallFor.GET_ALL_MARKET_PLACE_ADD, "").execute();
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
                if (cc.isConnectingToInternet()) {
                    Intent I = new Intent(context, MarketPlacePlaceNewAd.class);
                    I.putExtra("ID", ""+0);
                    startActivity(I);
                }else{
                    CommonMethods.showToastError(getActivity(), "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                }
            }
        });
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.filter_action, menu);
        setHasOptionsMenu(true);
        final MenuItem item = menu.findItem(R.id.filter);
        //Get a reference to your item by id
        //MenuItem item = menu.findItem(R.id.menu_pick_color);
        //Here, you get access to the view of your item, in this case, the layout of the item has a FrameLayout as root view but you can change it to whatever you use
        LinearLayout rootView = (LinearLayout) item.getActionView();
        //Then you access to your control by finding it in the rootView
        final TextView title = (TextView) rootView.findViewById(R.id.title);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strTitle = title.getText().toString();
                if(strTitle.contains("All")){
                    title.setText("My ads");
                    filteredMarketPlaceResponceBean.getAdvertises().clear();
                    filteredMarketPlaceResponceBean.getAdvertises().addAll(MainActivity.marketPlaceResponceBean.getAdvertises());
                    if(filteredMarketPlaceResponceBean.getAdvertises().size() == 0){
                        CommonMethods.showToastError(context,"No ad posted yet");//,Toast.LENGTH_LONG).show();
                    }
                }else if(strTitle.contains("My")){
                    title.setText("All ads");
                    filteredMarketPlaceResponceBean.getAdvertises().clear();
                    int familyId  = Integer.parseInt(SheredPref.getFamilyId(context));
                    int size = MainActivity.marketPlaceResponceBean.getAdvertises().size();
                    for(int i = 0; i < size; i++){
                        int fid = MainActivity.marketPlaceResponceBean.getAdvertises().get(i).getFamilyId();
                        if(familyId == fid){
                            filteredMarketPlaceResponceBean.getAdvertises().add(MainActivity.marketPlaceResponceBean.getAdvertises().get(i));
                        }
                    }
                    if(filteredMarketPlaceResponceBean.getAdvertises().size() == 0){
                        CommonMethods.showToastError(context,"You are not posted any ad yet");//,Toast.LENGTH_LONG).show();
                    }
                }
                if(filteredMarketPlaceResponceBean.getAdvertises().size() == 0){
                    NoDataLayout.setVisibility(View.VISIBLE);
                    mSwipeRefreshLayout.setVisibility(View.GONE);
                }else{
                    NoDataLayout.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(cc.isConnectingToInternet()){
            new GetData(context, CallFor.GET_ALL_MARKET_PLACE_ADD, "").execute();
        }
    }
}
