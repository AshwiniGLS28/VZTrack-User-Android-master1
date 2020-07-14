package vztrack.gls.com.vztrack_user.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.BaseFragment;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.activity.WebviewActivity;
import vztrack.gls.com.vztrack_user.adapters.InvoiceAdapter;
import vztrack.gls.com.vztrack_user.beans.InvoiceResponce;
import vztrack.gls.com.vztrack_user.beans.ResponceBean;
import vztrack.gls.com.vztrack_user.beans.Srvrsp;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

public class InvoiceFragment extends BaseFragment {
    RecyclerView invoiceRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Srvrsp> invoiceList;
    private Context context;
    InvoiceAdapter adapter;
    LinearLayout noDataLL;
    ProgressDialog progressDialog;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    CheckConnection cc;
    InvoiceResponce invoiceResponce;
    private String TAG = "Invoice_Fragment";

    public InvoiceFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_invoice, null);

        CleverTapAPI cleverTap = CleverTapAPI.getDefaultInstance(getActivity());
        String title= UtilityMethods.getTitleFromMenuJson(getResources().getString(R.string.invoice),getActivity());
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);
        HashMap<String, Object> action = new HashMap<String, Object>();
        action.put(Events.event_key_society_name, SheredPref.getSociety_Name(getActivity()));
        action.put(Events.event_key_flat_number, SheredPref.getFlat_No(getActivity()));
        cleverTap.pushEvent(Events.event_invoice_screen, action);

        progressDialog = ProgressDialog.show(getActivity(), "", "Loading...");
        setHasOptionsMenu(true);
        context = getActivity();
        invoiceRecyclerView = root.findViewById(R.id.invoice_recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.activity_main_swipe_refresh_layout);
        noDataLL = root.findViewById(R.id.NoDataLayout);
        invoiceList = new ArrayList<>();
        setAdapterToView(invoiceList);

        cc = new CheckConnection(getActivity());
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cc.isConnectingToInternet()) {
                    new GetData(InvoiceFragment.this, CallFor.INVOICE_LIST, "").execute();
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    CommonMethods.showToastError(context, "Unable to refresh, Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                }
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetData(this, CallFor.INVOICE_LIST, "").execute();
    }

    @Override
    public void onGetResponse(String response, String callFor) {
        if(callFor.equals(CallFor.INVOICE_LIST)){
            invoiceResponce = new Gson().fromJson(response, InvoiceResponce.class);
            if(progressDialog!=null && progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            if(mSwipeRefreshLayout.isRefreshing()){
                mSwipeRefreshLayout.setRefreshing(false);
            }
            if(invoiceResponce.getErrcode()==null || invoiceResponce.getErrcode().equals("0")){     // Which Mean Success
                setInvoiceList(invoiceResponce.getSrvrsp());
            }else{
                CommonMethods.showToastError(context, invoiceResponce.getErrmsg());//, Toast.LENGTH_LONG, true).show();
                invoiceRecyclerView.setVisibility(View.GONE);
                noDataLL.setVisibility(View.VISIBLE);
            }
        }
        if(callFor.equals(CallFor.INVOICE_URL)){
            ResponceBean responceBean = new Gson().fromJson(response, ResponceBean.class);
            if(responceBean.getCode().equals("SUCCESS")){
                Intent intent = new Intent(context, WebviewActivity.class);
                intent.putExtra("URL", responceBean.getMessage());
                startActivity(intent);
            }else{
                CommonMethods.showToastError(context, "Error In getting Invoice");
            }

        }
    }

    public void setInvoiceList(ArrayList<Srvrsp> invoices){
        try {
            if(invoices.size() == 0){
                invoiceRecyclerView.setVisibility(View.GONE);
                noDataLL.setVisibility(View.VISIBLE);
            }else{
                invoiceList.clear();
                invoiceList.addAll(invoices);
                adapter.notifyDataSetChanged();
                invoiceRecyclerView.setVisibility(View.VISIBLE);
                noDataLL.setVisibility(View.GONE);
            }
        } catch (Exception e){
            Log.e(TAG," Exception "+e);
        }
    }

    public void setAdapterToView(ArrayList<Srvrsp> invoices){
        adapter = new InvoiceAdapter(this, invoices);
        invoiceRecyclerView.setAdapter(adapter);
        invoiceRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        invoiceRecyclerView.setLayoutManager(mLayoutManager);
        invoiceRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }
}