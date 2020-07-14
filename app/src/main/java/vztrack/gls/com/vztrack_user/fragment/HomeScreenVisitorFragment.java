package vztrack.gls.com.vztrack_user.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.adapters.Visitors_RecyclerViewAdapter;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.DbHelper;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

public class HomeScreenVisitorFragment extends Fragment {
    private static Context context;
    DbHelper dbHelper;
    CheckConnection cc;
    public HomeScreenVisitorFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.homescreenvisitor, null);
        context = getActivity();
        CleverTap.cleverTap_Record_Event(getActivity(), Events.event_visitor_screen);
//        UtilityMethodsAndroid.setActionBarTitle(getActivity());
        String title= UtilityMethods.getTitleFromMenuJson(getResources().getString(R.string.home),context);
        Log.e("titlevisitor",title+"");
        ((MainActivity) context).getSupportActionBar().setTitle(title);
        dbHelper = new DbHelper(context);
        setHasOptionsMenu(true);
        cc = new CheckConnection(getActivity());
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        setHasOptionsMenu(true);
        final MenuItem item = menu.findItem(R.id.action_search);
        if(MainActivity.isVisitorAccessMainScreen==false){
            item.setVisible(false);
        }
    }
}
