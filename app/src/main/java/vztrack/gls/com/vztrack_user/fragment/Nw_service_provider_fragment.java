package vztrack.gls.com.vztrack_user.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import vztrack.gls.com.vztrack_user.BuildConfig;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.BaseFragment;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

public class Nw_service_provider_fragment extends BaseFragment {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    //Fragments
    NoDataFragment noDataFragment;
    NoInternetFragment noInternetFragment;

    Context context;
    CheckConnection cc;
    boolean isUserAdmin;
    MyPagerAdapter viewPagerAdapter;
    int app_version_code = BuildConfig.VERSION_CODE;
    private static int NUM_ITEMS = 2;
    String date;
    public Nw_service_provider_fragment() {

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.nw_service_provider, null);
        context = getActivity();
        cc = new CheckConnection(context);
        setHasOptionsMenu(true);
        String title= UtilityMethods.getTitleFromMenuJson(getResources().getString(R.string.serviceprovider),getActivity());
        Log.e("titlevisitor",title+"");
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);
        viewPager = root.findViewById(R.id.viewpager);
        tabLayout = root. findViewById(R.id.tablayout);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        date = df.format(c.getTime());
        Log.e("getapicall",date.compareTo(SheredPref.getDateForApi(context))+"--");
        Log.e("date",date+"--");
        Log.e("getDateForApi",(SheredPref.getDateForApi(context))+"--");
        if (date.compareTo(SheredPref.getDateForApi(context)) != 0) {

            String device_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            SheredPref.setDateForApi(context, date);
            new GetData(Nw_service_provider_fragment.this, CallFor.PROVIDER_LIST, "?data=" + device_id + "&versionCode=" + app_version_code).execute();
        }
            NUM_ITEMS = 2;
            tabLayout.setVisibility(View.VISIBLE);
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) viewPager.getLayoutParams();
            lp.setMargins(0, 0, 0, 50);

//        }
//        else if (invitationAccess||!approvalaccess) {
//            NUM_ITEMS = 1;
//            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) viewPager.getLayoutParams();
//            tabLayout.setVisibility(View.GONE);
//            lp.setMargins(0, 0, 0, 5);
//        }else if (!invitationAccess||approvalaccess) {
//            NUM_ITEMS = 1;
//            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) viewPager.getLayoutParams();
//            tabLayout.setVisibility(View.GONE);
//            lp.setMargins(0, 0, 0, 5);
//        }


//        try
//        {
        viewPager.setOffscreenPageLimit(2);
        viewPagerAdapter = new MyPagerAdapter(getFragmentManager(),NUM_ITEMS);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

//        }catch(Exception e)
//        {
//            e.printStackTrace();
//        }

//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            public void onPageScrollStateChanged(int state) {}
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
//
//            public void onPageSelected(int position) {
//                if(cc.isConnectingToInternet()){
//                    if(position==1){
//                        new GetData(MainActivity.mainActivity, CallFor.ADMIN_POLLS, "").execute();
//                    }else if(position==0){
//                        new GetData(MainActivity.mainActivity, CallFor.USER_POLLS, "").execute();
//                    }
//                }else{
//                    Toasty.info(context, "Please check internet connection", Toast.LENGTH_SHORT, true).show();
//                }
//            }
//        });

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
//        Log.e("ApprovalFragment","Resume");
        //Initializing viewPager

    }

    @Override
    public void onGetResponse(String response, String callFor) {
        Log.e("Nw_service_provider_fragment",response+"");
        if (callFor.equals(CallFor.PROVIDER_LIST)) {
            try {
               MainActivity.prov_list = new Gson().fromJson(response, String[].class);
                // Store the Provider List in SharedPreferences
                SharedPreferences prefs = context.getSharedPreferences("LIST", 0);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putInt("array_size",MainActivity.prov_list.length);
                for (int i = 0; i <MainActivity.prov_list.length; i++)
                    edit.putString("array_" + i,MainActivity.prov_list[i]);
                edit.commit();
            } catch (Exception e) {
                Log.e("Exception ProviderList", " " + e);
            }
        }


    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {
        int nooffrag=0;

        public MyPagerAdapter(FragmentManager fragmentManager, int numItems) {
            super(fragmentManager);
//            Log.e("MyPagerAdapter","MyPagerAdapter");
            nooffrag=numItems;
        }



        // Returns total number of pages
        @Override
        public int getCount() {
//            Log.e("retun",NUM_ITEMS+"--");
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
//            Log.e("position----",position+"");
            switch (position) {

                case 0:  return SearchProviderFragment.newInstance(0, "Service provider");

                case 1: return RatingFragment.newInstance(0, "Service feedback");

                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {

            String title=null;



//            if (nooffrag==2)
//            {
                if (position==0)
                    title="Service provider";
                else
                    title="Service feedback";
//            }


            return title;

        }

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.e("onCreateOptionsMenu","onCreateOptionsMenu");
        menu.clear();
//        inflater.inflate(R.menu.main, menu);
//        setHasOptionsMenu(true);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
//        searchView.setVisibility(View.GONE);
    }
}

