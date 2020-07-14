package vztrack.gls.com.vztrack_user.fragment;

/**
 * Created by sandeep on 14/3/16.
 */

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.SendNotice;
import vztrack.gls.com.vztrack_user.adapters.Notices_RecyclerViewAdapter;
import vztrack.gls.com.vztrack_user.beans.DataObjectNotices;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Constants;
import vztrack.gls.com.vztrack_user.utils.DbHelper;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

public class NoticesFragment extends Fragment {

    Context context;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList results;
    DbHelper dbHelper;
    String img_URL, heading, description, noticeStartdate, noticeEndDate, fileType;
    CheckConnection cc;
    public static SwipeRefreshLayout mSwipeRefreshLayout;
    private Button fab;
    private final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 1;
    public static TextView NoDataText;
    public static LinearLayout NoDataLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_notices, null);

        CleverTap.cleverTap_Record_Event(getActivity(), Events.event_notice_screen);
//        UtilityMethodsAndroid.setActionBarTitle(getActivity());
        String title= UtilityMethods.getTitleFromMenuJson(getResources().getString(R.string.noticenminutes),getActivity());
        Log.e("titlevisitor",title+"");
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);
        dbHelper = new DbHelper(getActivity());
        cc = new CheckConnection(getActivity());
        setHasOptionsMenu(true);
        context = getActivity();
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.activity_main_swipe_refresh_layout);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.notice_recycler_view);
        fab = (Button) root.findViewById(R.id.fab);
        NoDataLayout = (LinearLayout) root.findViewById(R.id.NoDataLayout);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        NoDataText=root.findViewById(R.id.NoDataText);
        mRecyclerView.setLayoutManager(mLayoutManager);
        NoDataLayout=root.findViewById(R.id.NoDataLayout);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        SheredPref.setNoticeCount(getActivity(), 0);             // To maintain notice count in drawer

        if (MainActivity.isNoticeZero) {
            NoDataLayout.setVisibility(View.VISIBLE);
            NoDataText.setText("No notices and minutes found");
            mRecyclerView.setVisibility(View.GONE);
        } else {
            NoDataLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

//        try {
            mAdapter = new Notices_RecyclerViewAdapter(context, getDataSet("All"));
//        } catch (Exception ex) {
//            Log.e("Ex in Notice Frag", " " + ex);
//        }

        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cc.isConnectingToInternet()) {
                    MainActivity.fragment_flag = 1;
                    new GetData(MainActivity.mainActivity, CallFor.NOTICES, "").execute();
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    CommonMethods.showToastError(context, "Unable to refresh, Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                    //Toast.makeText(context,"Unable To Refresh , No Internet Connection",Toast.LENGTH_LONG).show();
                }
            }
        });

        if (cc.isConnectingToInternet()) {
            String currentDateString = DateFormat.getDateInstance().format(new Date());
            if ( MainActivity.noticesResponse==null||(currentDateString.equals(SheredPref.getDate(context)) && MainActivity.noticesResponse.getNotices().size() == dbHelper.getNumberOfRowsOfNotices())) {
            } else {
                dbHelper.deleteDataFromNotices();

                for (int index = 0; index < MainActivity.noticesResponse.getNotices().size(); index++) {

                    heading = MainActivity.noticesResponse.getNotices().get(index).getNoticeHeading();
                    description = MainActivity.noticesResponse.getNotices().get(index).getNoticeDesc();
                    noticeStartdate = MainActivity.noticesResponse.getNotices().get(index).getNoticeStartDate();
                    noticeEndDate = MainActivity.noticesResponse.getNotices().get(index).getNoticeEndDate();
                    img_URL = Constants.BASE_IMG_URL + "/" + MainActivity.noticesResponse.getNotices().get(index).getPath();
                    fileType = MainActivity.noticesResponse.getNotices().get(index).getFileType();
                    if (dbHelper.insertDataInNotices(heading, description, noticeStartdate, noticeEndDate, img_URL, fileType)) {
                    }
                }
                SheredPref.setDate(context, currentDateString);
            }
        }
        fab.setOnClickListener(view -> {
            if (cc.isConnectingToInternet()) {
                gotoSendNoticeActivity();
            } else {
                CommonMethods.showToastError(context, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
            }
        });

        if (!SheredPref.getAdminAccess(getActivity())) {
            fab.setVisibility(View.GONE);
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

                ArrayList filteredList = new ArrayList<DataObjectNotices>();
                filteredList = getDataSet(query);

                try {
                    mRecyclerView.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(context);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mAdapter = new Notices_RecyclerViewAdapter(context, filteredList);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } catch (Exception ex) {
                    Log.e("Ex ", " " + ex);
                }
                return true;
            }
        });
    }


    private ArrayList<DataObjectNotices> getDataSet(String startWith) {
        results = new ArrayList<DataObjectNotices>();
        if (startWith.equals("All") || startWith.equals("")) {
            if (cc.isConnectingToInternet()) {
                if (MainActivity.noticesResponse!=null) {
                    for (int index = 0; index < MainActivity.noticesResponse.getNotices().size(); index++) {
                        DataObjectNotices obj = new DataObjectNotices(MainActivity.noticesResponse.getNotices().get(index).getNoticeHeading(),
                                MainActivity.noticesResponse.getNotices().get(index).getNoticeDesc(),
                                MainActivity.noticesResponse.getNotices().get(index).getNoticeStartDate(),
                                MainActivity.noticesResponse.getNotices().get(index).getNoticeEndDate(),
                                MainActivity.noticesResponse.getNotices().get(index).getPath(),
                                MainActivity.noticesResponse.getNotices().get(index).getNoticeId(),
                                MainActivity.noticesResponse.getNotices().get(index).getFileType()
                        );
                        results.add(index, obj);
                    }
                }
            } else {
                int row_count = dbHelper.getNumberOfRowsOfNotices();
                if (row_count == 0) {
                    NoDataLayout.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    NoDataLayout.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
                Cursor rs = dbHelper.getDataFromNoticesTable();
                rs.moveToFirst();

                for (int i = 0; i < row_count; i++) {
                    String title = rs.getString(rs.getColumnIndex(dbHelper.NOTICES_TITLE));
                    String desc = rs.getString(rs.getColumnIndex(dbHelper.NOTICES_DESCRIPTION));
                    String noticeStartDate = rs.getString(rs.getColumnIndex(dbHelper.NOTICES_START_DATE));
                    String noticeEndDate = rs.getString(rs.getColumnIndex(dbHelper.NOTICES_END_DATE));
                    String path = rs.getString(rs.getColumnIndex(dbHelper.NOTICES_PHOTO_STRING));
                    String noticeId = rs.getString(rs.getColumnIndex(dbHelper.NOTICES_ID));
                    String fileType = rs.getString(rs.getColumnIndex(dbHelper.NOTICES_FILE_TYPE));
                    DataObjectNotices obj = new DataObjectNotices(title, desc, noticeStartDate, noticeEndDate, path, noticeId, fileType);
                    results.add(i, obj);
                    rs.moveToNext();
                }
            }
        } else {
            if (cc.isConnectingToInternet()) {
                for (int i = 0; i < MainActivity.noticesResponse.getNotices().size(); i++) {
                    String check = MainActivity.noticesResponse.getNotices().get(i).getNoticeHeading().trim();
                    if (check.toLowerCase().contains(startWith)) {
                        DataObjectNotices obj = new DataObjectNotices(MainActivity.noticesResponse.getNotices().get(i).getNoticeHeading(),
                                MainActivity.noticesResponse.getNotices().get(i).getNoticeDesc(),
                                MainActivity.noticesResponse.getNotices().get(i).getNoticeStartDate(),
                                MainActivity.noticesResponse.getNotices().get(i).getNoticeEndDate(),
                                MainActivity.noticesResponse.getNotices().get(i).getPath(),
                                MainActivity.noticesResponse.getNotices().get(i).getNoticeId(),
                                MainActivity.noticesResponse.getNotices().get(i).getFileType()
                        );
                        results.add(obj);
                    }
                }
            } else {
                int row_count = dbHelper.getNumberOfRowsOfNotices();
                Cursor rs = dbHelper.getDataFromNoticesTable();
                rs.moveToFirst();
                for (int i = 0; i < row_count; i++) {
                    String title = rs.getString(rs.getColumnIndex(dbHelper.NOTICES_TITLE));
                    String desc = rs.getString(rs.getColumnIndex(dbHelper.NOTICES_DESCRIPTION));
                    String noticeStartDate = rs.getString(rs.getColumnIndex(dbHelper.NOTICES_START_DATE));
                    String noticeEndDate = rs.getString(rs.getColumnIndex(dbHelper.NOTICES_END_DATE));
                    String path = rs.getString(rs.getColumnIndex(dbHelper.NOTICES_PHOTO_STRING));
                    String notice_id = rs.getString(rs.getColumnIndex(dbHelper.NOTICES_PHOTO_STRING));
                    String file_type = rs.getString(rs.getColumnIndex(dbHelper.NOTICES_FILE_TYPE));
                    if (title.toLowerCase().contains(startWith)) {
                        DataObjectNotices obj = new DataObjectNotices(title, desc, noticeStartDate, noticeEndDate, path, notice_id, file_type);
                        results.add(obj);
                    }
                    rs.moveToNext();
                }
            }
        }
        return results;
    }

    public void gotoSendNoticeActivity(){
        Intent intent = new Intent(context, SendNotice.class);
        startActivity(intent);
    }
}
