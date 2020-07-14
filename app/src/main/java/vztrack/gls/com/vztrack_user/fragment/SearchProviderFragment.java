package vztrack.gls.com.vztrack_user.fragment;

/**
 * Created by sandeep on 14/3/16.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.google.gson.Gson;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.HashMap;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.adapters.Preapproval.MyAdapter;
import vztrack.gls.com.vztrack_user.beans.InputBeanSearchProvider;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.DbHelper;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.PostData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

public class SearchProviderFragment extends Fragment {

    Context context;
    public static RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    DbHelper dbHelper;
    CheckConnection cc;
    TextView tvNearBySoc;//tvQuality
    private AppCompatSpinner purposes_spinner;
    private RadioButton checkbox_quality,checkbox_price,checkbox_punctuality;
    private Switch switch_btn;
    public static String strProviderVal,strRatingVal,strSocVal;
    String strSelectProvider="     SELECT PROVIDER     ";
    public static TextView tvNoProvider;
    LinearLayout noImageDataLayout;
    public SearchProviderFragment() {
        Log.e("SearchProviderFragment","SearchProviderFragment");
    }


    public static SearchProviderFragment newInstance(int page, String title) {
        SearchProviderFragment fragmentFirst = new SearchProviderFragment();
        return fragmentFirst;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_search_provider, null);
        CleverTap.cleverTap_Record_Event(getActivity(), Events.event_service_screen);
        setHasOptionsMenu(true);
//        UtilityMethodsAndroid.setActionBarTitle(getActivity());
        String title= UtilityMethods.getTitleFromMenuJson(getResources().getString(R.string.serviceprovider),getActivity());
        Log.e("titlevisitor",title+"");
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);
        dbHelper = new DbHelper(getActivity());
        cc = new CheckConnection(getActivity());
        setHasOptionsMenu(true);
        context = getActivity();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.search_provider_recycler_view);
        purposes_spinner = (AppCompatSpinner) root.findViewById(R.id.spinner_providers_list);

//        tvQuality = (TextView) root.findViewById(R.id.tvQuality);
//        tvPrice = (TextView) root.findViewById(R.id.tvPrice);
//        tvPunctuality = (TextView) root.findViewById(R.id.tvPunctuality);
        tvNearBySoc = (TextView) root.findViewById(R.id.tvNearBySoc);
        noImageDataLayout=root.findViewById(R.id.noImageDataLayout);

        checkbox_quality =  root.findViewById(R.id.checkbox_quality);
        checkbox_price =  root.findViewById(R.id.checkbox_price);
        checkbox_punctuality =  root.findViewById(R.id.checkbox_punctuality);

        switch_btn =  root.findViewById(R.id.switch_btn);

        tvNoProvider = (TextView) root.findViewById(R.id.TvNoProvider);

        tvNoProvider.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        tvNoProvider.setText("Please Select Provider, No Provider Selected !");

        checkbox_quality.setChecked(true);
        checkbox_price.setChecked(false);
        checkbox_punctuality.setChecked(false);
        strSocVal = "nearby";
        strRatingVal="quality";


        checkbox_quality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(strProviderVal.equals(strSelectProvider)){
                    CommonMethods.showToastError(context, "Please select provider");//, Toast.LENGTH_SHORT, true).show();
                    checkbox_quality.setChecked(false);
                    checkbox_price.setChecked(false);
                    checkbox_punctuality.setChecked(false);
                }
                else{
                    checkbox_quality.setChecked(true);
                    checkbox_price.setChecked(false);
                    checkbox_punctuality.setChecked(false);
                    strRatingVal="quality";
                    getProviderData();
                }
            }
        });
        checkbox_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(strProviderVal.equals(strSelectProvider)){
                    checkbox_quality.setChecked(false);
                    checkbox_price.setChecked(false);
                    checkbox_punctuality.setChecked(false);
                    CommonMethods.showToastError(context, "Please select provider");//, Toast.LENGTH_SHORT, true).show();

                }
                else{
                    checkbox_quality.setChecked(false);
                    checkbox_price.setChecked(true);
                    checkbox_punctuality.setChecked(false);
                    strRatingVal="price";
                    getProviderData();
                }
            }
        });
        checkbox_punctuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(strProviderVal.equals(strSelectProvider)){
                    checkbox_quality.setChecked(false);
                    checkbox_price.setChecked(false);
                    checkbox_punctuality.setChecked(false);
                    CommonMethods.showToastError(context, "Please select provider");//, Toast.LENGTH_SHORT, true).show();

                }
                else{
                    checkbox_quality.setChecked(false);
                    checkbox_price.setChecked(false);
                    checkbox_punctuality.setChecked(true);
                    strRatingVal="punctuality";
                    getProviderData();
                }
            }
        });

//
//        checkbox_quality.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (strProviderVal!=null) {
//                    if (strProviderVal.equals(strSelectProvider)) {
//                        CommonMethods.showToastError(context, "Please select provider");//, Toast.LENGTH_SHORT, true).show();
//                        checkbox_quality.setChecked(false);
//                        checkbox_price.setChecked(false);
//                        checkbox_punctuality.setChecked(false);
//                    } else {
//                        checkbox_quality.setChecked(true);
//                        checkbox_price.setChecked(false);
//                        checkbox_punctuality.setChecked(false);
//                        strRatingVal = "quality";
//                        getProviderData();
//                    }
//                }
//            }
//        });

        checkbox_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (strProviderVal!=null) {
                    if (strProviderVal.equals(strSelectProvider)) {
                        checkbox_quality.setChecked(false);
                        checkbox_price.setChecked(false);
                        checkbox_punctuality.setChecked(false);

                        CommonMethods.showToastError(context, "Please select provider");//, Toast.LENGTH_SHORT, true).show();
                        //Toast.makeText(context,"Please Select Provider First !",Toast.LENGTH_SHORT).show();
                    } else {
                        checkbox_quality.setChecked(false);
                        checkbox_price.setChecked(true);
                        checkbox_punctuality.setChecked(false);
                        strRatingVal = "price";
                        getProviderData();
                    }
                }
            }
        });
        checkbox_punctuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (strProviderVal!=null) {
                    if (strProviderVal.equals(strSelectProvider)) { checkbox_quality.setChecked(false);
                        checkbox_price.setChecked(false);
                        checkbox_punctuality.setChecked(false);

                        CommonMethods.showToastError(context, "Please select provider");//, Toast.LENGTH_SHORT, true).show();
                        //Toast.makeText(context,"Please Select Provider First !",Toast.LENGTH_SHORT).show();
                    } else {
                        checkbox_quality.setChecked(false);
                        checkbox_price.setChecked(false);
                        checkbox_punctuality.setChecked(true);
                        strRatingVal = "punctuality";
                        getProviderData();
                    }
                }
            }
        });

        // get the provoder list from SharedPreference
        SharedPreferences prefs = context.getSharedPreferences("LIST",0);
        int size = prefs.getInt("array_size", 0);
        ArrayList<String> newlist=new ArrayList<>();
        final String[] list = new String[size+1];
        for(int i=0; i<size; i++){
            if(i==0){
                list[i] = strSelectProvider;
                newlist.add(i, strSelectProvider);
                list[i+1] = prefs.getString("array_" + i, null);
                newlist.add(i+1, prefs.getString("array_" + i, null));
            }
            else{
                list[i+1] = prefs.getString("array_" + i, null);
                newlist.add(i+1, prefs.getString("array_" + i, null));
            }
        }

//        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item, list);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        purposes_spinner.setAdapter(adapter);


        MyAdapter adapter1=new MyAdapter(getActivity(), R.layout.preapprovalspinner,newlist);
        purposes_spinner.setAdapter(adapter1);// =adapter

        purposes_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                if(pos==0){
                    strProviderVal = adapterView.getItemAtPosition(pos).toString();
                    tvNoProvider.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    noImageDataLayout.setVisibility(View.VISIBLE);
                }
                else{
                    tvNoProvider.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    noImageDataLayout.setVisibility(View.GONE);
                    strProviderVal = adapterView.getItemAtPosition(pos).toString();
                    //CleverTap.cleverTap_Record_Event(getActivity(),"Selected Provider - "+strProviderVal);
                    getProviderData();
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        switch_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(strProviderVal!=null){
                    if (strProviderVal.equals(strSelectProvider)) {
                        CommonMethods.showToastError(context, "Please select provider");//, Toast.LENGTH_SHORT, true).show();
                        //Toast.makeText(context,"Please Select Provider First !",Toast.LENGTH_SHORT).show();
                        switch_btn.setChecked(false);
                    } else{
                        if(isChecked){
                            strSocVal = "OUT_SOC";
                        }else{
                            strSocVal = "nearby";
                        }
                        getProviderData();
                    }
                }
                else{
                    if(isChecked){
                        strSocVal = "OUT_SOC";
                    }else{
                        strSocVal = "nearby";
                    }
                    getProviderData();
                }
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        return root;
    }

    public void getProviderData(){
        if (cc.isConnectingToInternet()) {
            // CleverTap
            CleverTapAPI cleverTap = null;
            try {
                cleverTap = CleverTapAPI.getInstance(getContext());
                HashMap<String, Object> searchCriteria = new HashMap<String, Object>();
                searchCriteria.put(Events.event_key_type, strProviderVal);
                searchCriteria.put(Events.event_key_rate_type, strRatingVal);
                searchCriteria.put(Events.event_key_near_by, strSocVal);
                searchCriteria.put(Events.event_key_date, new java.util.Date());
                cleverTap.event.push(Events.event_service_search, searchCriteria);
            } catch (CleverTapMetaDataNotFoundException e) {
                e.printStackTrace();
            } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
                cleverTapPermissionsNotSatisfied.printStackTrace();
            }

            tvNoProvider.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            InputBeanSearchProvider inputBeanSearchProvider = new InputBeanSearchProvider();
            inputBeanSearchProvider.setSocietyId(Integer.parseInt(SheredPref.getSocietyId(context)));
            inputBeanSearchProvider.setServiceProvider(strProviderVal);
            inputBeanSearchProvider.setRatingInput(strRatingVal);
            inputBeanSearchProvider.setNearBy(strSocVal);
            new PostData(new Gson().toJson(inputBeanSearchProvider), MainActivity.mainActivity, CallFor.PROVIDERS_DATA).execute();
        }
        else{
            MainActivity.ShowErrorAlert();
            tvNoProvider.setVisibility(View.VISIBLE);
            tvNoProvider.setText("Please check internet connection");
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }
}
