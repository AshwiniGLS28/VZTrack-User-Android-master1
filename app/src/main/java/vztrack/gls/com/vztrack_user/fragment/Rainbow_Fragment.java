package vztrack.gls.com.vztrack_user.fragment;

/**
 * Created by sandeep on 14/3/16.
 */

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.utils.SheredPref;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

public class Rainbow_Fragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener{

    public Rainbow_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_rainbow, null);
        SheredPref.setRun(getActivity(), "DONT");
//        UtilityMethodsAndroid.setActionBarTitle(getActivity());
        //loading the default fragment
        SheredPref.setRainbowCount(getActivity(), 0);
        if(MainActivity.invitationNotification){
            loadFragment(new Rainbow_Group_Fragment());
            MainActivity.invitationNotification = false;
        }else{
            loadFragment(new Rainbow_Conversation_Fragment());
        }

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = root.findViewById(R.id.navigation);
        if(MainActivity.invitationNotification){
            navigation.setSelectedItemId(R.id.action_group);
        }else{
            navigation.setSelectedItemId(R.id.action_conversation);
        }

        navigation.setOnNavigationItemSelectedListener(this);

        return root;
    }

    /**
     * To load fragments for sample
     * @param fragment fragment which want to replace
     */
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        MainActivity.rainbowFragmentFlag = false;
        switch (item.getItemId()) {
            case R.id.action_conversation:
                fragment = new Rainbow_Conversation_Fragment();
                MainActivity.rainbowFragmentFlag = true;
                break;
            case R.id.action_group:
                fragment = new Rainbow_Group_Fragment();
                break;
            case R.id.action_contact:
                fragment = new Rainbow_Contact_Fragment();
                break;
            case R.id.action_profile:
                fragment = new Rainbow_Profile_Fragment();
                break;
        }
        return loadFragment(fragment);
    }

    @Override
    public void onPause() {
        MainActivity.rainbowFragmentFlag = false;
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.e("rainboe fragment","resume");
        MainActivity.rainbowFragmentFlag = true;
        super.onResume();
    }

    @Override
    public void onDestroy() {
        SheredPref.setRun(getActivity(), "RUN");
        super.onDestroy();
    }
}