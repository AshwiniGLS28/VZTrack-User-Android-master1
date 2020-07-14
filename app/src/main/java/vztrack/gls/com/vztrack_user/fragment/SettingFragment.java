package vztrack.gls.com.vztrack_user.fragment;

/**
 * Created by sandeep on 14/3/16.
 */

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import vztrack.gls.com.vztrack_user.activity.BaseActivity;
import vztrack.gls.com.vztrack_user.BuildConfig;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.adapters.SettingListAdapter;
import vztrack.gls.com.vztrack_user.profile.FamilyBean;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

public class SettingFragment extends Fragment {

    BaseActivity context;
    CheckConnection cc;
    private TextView tv_version;
    ListView menuList;
    SettingListAdapter adapter;

    String[] textString = {"Profile", "Password", "Notification","Logout"};
    int[] drawableIds = {R.drawable.nw_admin,R.drawable.nw_password, R.drawable.nw_notification, R.drawable.nw_logout};

    String[] textStringPrimaryUser = {"Profile","Manage Account", "Password", "Notification","Logout"};
    int[] drawableIdsFalsePrimaryUser = {R.drawable.nw_admin, R.drawable.manage_account, R.drawable.nw_password, R.drawable.nw_notification, R.drawable.nw_logout};

    public SettingFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_setting, null);

        CleverTap.cleverTap_Record_Event(getActivity(), Events.event_setting_screen);
        FamilyBean bean = new FamilyBean();
        context = MainActivity.mainActivity;
        cc = new CheckConnection(context);
        setHasOptionsMenu(true);
        menuList = (ListView) root.findViewById(R.id.menuList);
        tv_version = (TextView) root.findViewById(R.id.version);
        String versionName = BuildConfig.VERSION_NAME;
        tv_version.setText("Version : "+versionName);
        if(SheredPref.getType(context) == true){
            adapter = new SettingListAdapter(getActivity(),  textString, drawableIds);
        }else{
            if(MainActivity.isPrimaryUser == true){
                adapter = new SettingListAdapter(getActivity(),  textStringPrimaryUser, drawableIdsFalsePrimaryUser);
            }else {
                adapter = new SettingListAdapter(getActivity(),  textString, drawableIds);
            }
        }
        menuList.setAdapter(adapter);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }
}