package vztrack.gls.com.vztrack_user.fragment;



import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.adapters.LocalStoreAdapter_RecyclerViewAdapter;
import vztrack.gls.com.vztrack_user.beans.LocalStroreBean;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

public class LocalStoreFragment extends Fragment {
    public static RecyclerView local_store_recycler_view;
    public static ArrayList<LocalStroreBean> localStores =new ArrayList<>();
    public static RecyclerView.Adapter localStoreAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static LinearLayout NoDataLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_local_store, null);
        local_store_recycler_view = (RecyclerView) root.findViewById(R.id.local_store_recycler_view);
        NoDataLayout = (LinearLayout) root.findViewById(R.id.NoDataLayout);
//        UtilityMethodsAndroid.setActionBarTitle(getActivity());
        String title= UtilityMethods.getTitleFromMenuJson(getResources().getString(R.string.localstores),getActivity());
        Log.e("titlevisitor",title+"");
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);
        localStores = new ArrayList<>();
        localStoreAdapter = new LocalStoreAdapter_RecyclerViewAdapter(getActivity(), localStores);
        mLayoutManager = new LinearLayoutManager(getActivity());
        local_store_recycler_view.setLayoutManager(mLayoutManager);
        local_store_recycler_view.setItemAnimator(new DefaultItemAnimator());
        local_store_recycler_view.setAdapter(localStoreAdapter);
        setHasOptionsMenu(true);
        return  root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }
}
