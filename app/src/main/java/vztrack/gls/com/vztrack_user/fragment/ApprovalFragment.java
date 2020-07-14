package vztrack.gls.com.vztrack_user.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;


import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

public class ApprovalFragment extends Fragment{

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
//    public static PollResponce pollResponce = new PollRespResumonce();
     boolean invitationAccess;
     boolean approvalaccess;
    private static int NUM_ITEMS = 2;
    public ApprovalFragment() {

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.approval_fragment, null);
        context = getActivity();
        cc = new CheckConnection(context);
//        UtilityMethodsAndroid.setActionBarTitle(getActivity());
        String title= UtilityMethods.getTitleFromMenuJson(getResources().getString(R.string.preapproval),context);
        Log.e("titlevisitor",title+"");
        ((MainActivity) context).getSupportActionBar().setTitle(title);
        setHasOptionsMenu(true);
        viewPager = root.findViewById(R.id.viewpager);
        tabLayout = root. findViewById(R.id.tablayout);
//        Log.e("ApprovalFragment","onCreateView");
        invitationAccess = SheredPref.getInvitationAccess(context);
         approvalaccess = SheredPref.getPreApprovalsAccess(context);
         Log.e("invitationAccess",invitationAccess+"--");
        Log.e("approvalaccess",approvalaccess+"--");
         if (invitationAccess&&approvalaccess) {
             NUM_ITEMS = 2;
//             tabLayout.setVisibility(View.VISIBLE);
             ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) viewPager.getLayoutParams();
             lp.setMargins(0, 0, 0, 50);

         }
         else if (invitationAccess||!approvalaccess) {
             NUM_ITEMS = 1;
             ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) viewPager.getLayoutParams();
             tabLayout.setVisibility(View.GONE);
             lp.setMargins(0, 0, 0, 5);
         }else if (!invitationAccess||approvalaccess) {
             NUM_ITEMS = 1;
             ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) viewPager.getLayoutParams();
             tabLayout.setVisibility(View.GONE);
             lp.setMargins(0, 0, 0, 5);
         }


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

    public class MyPagerAdapter extends FragmentStatePagerAdapter {
    int nooffrag=0;

        public MyPagerAdapter(FragmentManager fragmentManager,int numItems) {
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

                case 0: // Fragment # 0 - This will show FirstFragment
//                    Log.e("position----",position+"");
//                    Log.e("approval list","fragment");
//                    Log.e("approvalaccess",approvalaccess+"");
//                    Log.e("invitationAccess",invitationAccess+"");
                    if (nooffrag==2)
                    {
//                        View v = LayoutInflater.from(context).inflate(R.layout.tab_view, null);
//                        ImageView tabicon=v.findViewById(R.id.tabicon);
//                        TextView tabname=v.findViewById(R.id.tabname);
//                        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                        tabLayout.getTabAt(position).setCustomView(v);
//                        tabname.setText("Pre approval");
                        return ApprovalListFragment.newInstance(0, "Pre approvals");
                    }else
                    {
                        if (approvalaccess) {
//                            View v = LayoutInflater.from(context).inflate(R.layout.tab_view, null);
//                            ImageView tabicon=v.findViewById(R.id.tabicon);
//                            TextView tabname=v.findViewById(R.id.tabname);
//                            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                            tabLayout.getTabAt(position).setCustomView(v);
//                            tabname.setText("Pre approval");
                            return ApprovalListFragment.newInstance(0, "Pre approvals");
                        }
                        else if (invitationAccess && !approvalaccess)
                        {
//                            View v = LayoutInflater.from(context).inflate(R.layout.tab_view, null);
//                            ImageView tabicon=v.findViewById(R.id.tabicon);
//                            TextView tabname=v.findViewById(R.id.tabname);
//                            v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                            tabLayout.getTabAt(position).setCustomView(v);
//                            tabname.setText("invite guest");
                            return InviteGuestFragment.newInstance(1,"invite guest");

                        }
                    }


                case 1: // Fragment # 0 - This will show FirstFragment different title
//                    Log.e("invite guest ","fragment");
//                    Log.e("invitationAccess",invitationAccess+"");
                    if (invitationAccess) {
//                        View v = LayoutInflater.from(context).inflate(R.layout.tab_view, null);
//                        ImageView tabicon=v.findViewById(R.id.tabicon);
//                        TextView tabname=v.findViewById(R.id.tabname);
//                        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                        tabLayout.getTabAt(position).setCustomView(v);
//                        tabname.setText("invite guest");
                        return InviteGuestFragment.newInstance(1, "invite guest");
                    }
                    else
                        return null;

                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {

            String title=null;



            if (nooffrag==2)
            {
                if (position==0)
                    title="Pre approvals";
                else
                    title="Invite guest";
            }else
            {
                if (approvalaccess)
                    title="Pre approvals";
                else if (invitationAccess && !approvalaccess)
                    title="Invite guest";
            }


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
