package vztrack.gls.com.vztrack_user.fragment;

/**
 * Created by sandeep on 14/3/16.
 */

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.adapters.ViewPagerAdapter;
import vztrack.gls.com.vztrack_user.responce.PollResponce;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

public class PollsFragment extends Fragment{

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    //Fragments
    MyPollsFragment myPollsFragment;
    CreateEditPollsFragment viewEditPollsFragment;
    NoDataFragment noDataFragment;
    NoInternetFragment noInternetFragment;

    Context context;
    CheckConnection cc;
    boolean isUserAdmin;

    public static PollResponce pollResponce = new PollResponce();

    public PollsFragment() {

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_polls, null);

        context = getActivity();
        String title= UtilityMethods.getTitleFromMenuJson(getResources().getString(R.string.polls),context);
        Log.e("titlevisitor",title+"");
        ((MainActivity) context).getSupportActionBar().setTitle(title);
        cc = new CheckConnection(context);

        //Initializing viewPager
        viewPager = (ViewPager) root.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);

        isUserAdmin = SheredPref.getAdminAccess(context);

        //Initializing the tablayout
        tabLayout = (TabLayout) root.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSaveFromParentEnabled(false);
        setupViewPager(viewPager);

        if(!isUserAdmin){
            tabLayout.setVisibility(View.GONE);
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                if(cc.isConnectingToInternet()){
                    if(position==1){
                        new GetData(MainActivity.mainActivity, CallFor.ADMIN_POLLS, "").execute();
                    }else if(position==0){
                        new GetData(MainActivity.mainActivity, CallFor.USER_POLLS, "").execute();
                    }
                }else{
                    CommonMethods.showToastError(context, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        return root;
    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        myPollsFragment=new MyPollsFragment();
        viewEditPollsFragment=new CreateEditPollsFragment();
        noDataFragment = new NoDataFragment();
        if(cc.isConnectingToInternet()){
            if(pollResponce.getObjPollBeans().size()==0){
                adapter.addFragment(noDataFragment,"Polls");
            }else{
                adapter.addFragment(myPollsFragment,"Polls");
            }
        }else{
            adapter.addFragment(noInternetFragment,"Polls");
        }

        if(isUserAdmin){
            if(cc.isConnectingToInternet()){
                adapter.addFragment(viewEditPollsFragment,"Create/Edit Poll");
            }else{
                adapter.addFragment(noInternetFragment,"Polls");
            }
        }
        viewPager.setAdapter(adapter);

        if(MainActivity.callFromAdmin){
            MainActivity.callFromAdmin = false;
            new GetData(MainActivity.mainActivity, CallFor.ADMIN_POLLS, "").execute();
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    }
}