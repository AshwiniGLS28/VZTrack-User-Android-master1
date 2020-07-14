package vztrack.gls.com.vztrack_user.fragment;

/**
 * Created by sandeep on 14/3/16.
 */

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.adapters.ViewPagerAdapter;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

public class VehiclesFragment extends Fragment {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;

    //Fragments

    SearchVehicleFragment searchVehicleFragment;
    AddvehicleFragment addvehicleFragment;

    public VehiclesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_vehicles, null);
        setHasOptionsMenu(true);
//        UtilityMethodsAndroid.setActionBarTitle(getActivity());
        String title= UtilityMethods.getTitleFromMenuJson(getResources().getString(R.string.searchnaddvehicle),getActivity());
        Log.e("titlevisitor",title+"");
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);
        //Initializing viewPager
        viewPager = (ViewPager) root.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(2);

        //Initializing the tablayout
        tabLayout = (TabLayout) root.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSaveFromParentEnabled(false);
        setupViewPager(viewPager);
        return root;
    }

    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        searchVehicleFragment=new SearchVehicleFragment();
        addvehicleFragment=new AddvehicleFragment();
        adapter.addFragment(searchVehicleFragment,"Search Vehicle");
        adapter.addFragment(addvehicleFragment,"Add Vehicle");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }
}