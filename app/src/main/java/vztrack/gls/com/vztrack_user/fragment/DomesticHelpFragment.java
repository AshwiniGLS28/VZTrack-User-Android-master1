package vztrack.gls.com.vztrack_user.fragment;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.view.MenuItemCompat;
import androidx.appcompat.widget.AppCompatSpinner;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.adapters.DomesticHelp_RecyclerViewAdapter;
import vztrack.gls.com.vztrack_user.adapters.Preapproval.MyAdapter;
import vztrack.gls.com.vztrack_user.responce.DomesticHelpResponceBean;
import vztrack.gls.com.vztrack_user.beans.domesticHelpProviders;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

public class DomesticHelpFragment extends Fragment {
    public static RecyclerView domestic_recycler_view;
    public static RecyclerView.Adapter domesticHelpAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public static LinearLayout NoDataLayout;
    private AppCompatSpinner spinner;
//    private  LinearLayout spinnerLayout;
    DomesticHelpResponceBean domesticHelpResponceBean;
    ArrayList<domesticHelpProviders> domesticHelpProviders = new ArrayList<>();
    private ArrayList<String> listPurpose =new ArrayList<>();
    boolean fromSearchFlag = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_domestic_help, null);
        String title= UtilityMethods.getTitleFromMenuJson(getResources().getString(R.string.knowyourmaid),getActivity());
        Log.e("titlevisitor",title+"");
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        domestic_recycler_view = (RecyclerView) root.findViewById(R.id.domestic_help_recycler_view);
        NoDataLayout = (LinearLayout) root.findViewById(R.id.NoDataLayout);
        domesticHelpResponceBean = MainActivity.domesticHelpResponceBean;
        spinner = (AppCompatSpinner) root.findViewById(R.id.spinner);
//        spinnerLayout = (LinearLayout) root.findViewById(R.id.spinnerLayout);

        listPurpose.clear();
        if (domesticHelpResponceBean!=null)
        {
            for(int i = 0; i < domesticHelpResponceBean.getDomesticHelpProviders().size(); i++){
                String purpose =  domesticHelpResponceBean.getDomesticHelpProviders().get(i).getPurpose();
                listPurpose.add(purpose);
            }

        }

        Set<String> hs = new HashSet<>();
        hs.addAll(listPurpose);
        listPurpose.clear();
        listPurpose.addAll(hs);
        listPurpose.add(0,"All");
        if(listPurpose.size()<=2){
            spinner.setVisibility(View.GONE);
            fromSearchFlag = false;
            changeDataSet("All", fromSearchFlag);
        }else{
            spinner.setVisibility(View.VISIBLE);
            domesticHelpResponceBean.getListOfPurpose().add(0,"ALL");
//            ArrayAdapter<String> adapter =new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item, listPurpose);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spinner.setAdapter(adapter);


            MyAdapter adapter1=new MyAdapter(this.getActivity(), R.layout.preapprovalspinner,listPurpose);
            spinner.setAdapter(adapter1);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                    String selectedValues = adapterView.getItemAtPosition(pos).toString();
                    fromSearchFlag = false;
                    changeDataSet(selectedValues, fromSearchFlag);
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
            });
        }
        return  root;
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
                try {
                    final String finalQuery = query;
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            fromSearchFlag = true;
                            spinner.setSelection(0);
                            changeDataSet(finalQuery, fromSearchFlag);
                        }
                    });
                } catch (Exception ex) {
                    Log.e("Ex ", " " + ex);
                }
                return true;
            }
        });
    }

    private void changeDataSet(String selectedValue, boolean fromSearchFlag){
        if (domesticHelpResponceBean!=null)
        {
            if(selectedValue.equalsIgnoreCase("All") && fromSearchFlag==false){
                domesticHelpAdapter = new DomesticHelp_RecyclerViewAdapter(getActivity(), domesticHelpResponceBean.getDomesticHelpProviders());
                ShowNoDataLayout(domesticHelpResponceBean.getDomesticHelpProviders());
            }else{
                domesticHelpProviders.clear();
                int size = domesticHelpResponceBean.getDomesticHelpProviders().size();
                int j = 0;
                for(int i = 0; i < size; i++){
                    String purpose = domesticHelpResponceBean.getDomesticHelpProviders().get(i).getPurpose();
                    String name = domesticHelpResponceBean.getDomesticHelpProviders().get(i).getFirstName()+" "+ domesticHelpResponceBean.getDomesticHelpProviders().get(i).getLastName();
                    if(fromSearchFlag==true){
                        if(name.toLowerCase().contains(selectedValue)){
                            domesticHelpProviders.add(j,domesticHelpResponceBean.getDomesticHelpProviders().get(i));
                            j++;
                        }
                    }else{
                        if(purpose.equalsIgnoreCase(selectedValue)){
                            domesticHelpProviders.add(j,domesticHelpResponceBean.getDomesticHelpProviders().get(i));
                            j++;
                        }
                    }
                }
                domesticHelpAdapter = new DomesticHelp_RecyclerViewAdapter(getActivity(), domesticHelpProviders);
                ShowNoDataLayout(domesticHelpProviders);
        }

       }

       mLayoutManager = new LinearLayoutManager(getActivity());
       domestic_recycler_view.setLayoutManager(mLayoutManager);
       domestic_recycler_view.setItemAnimator(new DefaultItemAnimator());
       domestic_recycler_view.setAdapter(domesticHelpAdapter);
       setHasOptionsMenu(true);



    }
    private void ShowNoDataLayout(ArrayList<domesticHelpProviders> domesticHelpProviders){
        if(domesticHelpProviders.size()==0){
            NoDataLayout.setVisibility(View.VISIBLE);
            domestic_recycler_view.setVisibility(View.GONE);
        }else{
            NoDataLayout.setVisibility(View.GONE);
            domestic_recycler_view.setVisibility(View.VISIBLE);
        }
    }
}
