package vztrack.gls.com.vztrack_user.utils;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import com.google.android.material.snackbar.Snackbar;
import android.util.Log;
import android.view.View;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import vztrack.gls.com.vztrack_user.activity.BaseActivity;
import vztrack.gls.com.vztrack_user.activity.BaseFragment;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.adapters.AdminPoll_RecyclerViewAdapter;

/**
 * Created by Santosh on 06-Oct-15.
 */
public class GetData extends AsyncTask {
    String url,url_rating;
    String result = "";
    String callFor = "";
    String extendedUrl = "";
    BaseActivity activity;
    ProgressDialog progressDialog;
    String check;
    BaseFragment fragment;

    public GetData(BaseActivity activity, String callFor, String extendedUrl){
        this.activity = activity;
        this.callFor = callFor;
        this.extendedUrl = extendedUrl;
        url = createURL(callFor);
    }

    public GetData(BaseFragment fragment, String callFor, String extendedUrl){
        this.fragment = fragment;
        this.callFor = callFor;
        this.extendedUrl = extendedUrl;
        url = createURL(callFor);
    }


    private String createURL(String callFor) {
        url = Constants.BASE_URL;
        url_rating = Constants.BASE_URL_FOR_RATING;

        if(callFor==CallFor.NOTICES){
            url = url+URL.NOTICES;
        }
        if(callFor==CallFor.VISITORS){
            url = url+URL.VISITORS+extendedUrl;
        }
        if(callFor==CallFor.VISITOR_LIST){
            url = url+URL.VISIT_LIST+extendedUrl;
        }
        if(callFor==CallFor.UPDATE_VISITOR_STATUS){
            url = url+URL.UPDATE_VISITOR_STATUS+extendedUrl;
        }

        if(callFor==CallFor.CHANGE_PASSWORD){
            try {
                url = url+URL.CHANGE_PASSWORD+  URLEncoder.encode(extendedUrl, "US-ASCII");
            } catch (UnsupportedEncodingException e) {
               Log.e("Exception", "Encode Url");
            }
        }

        // COMPLAIN URL
        if(callFor==CallFor.GET_COMPLAIN){
            url = url+URL.GET_COMPLAINS+extendedUrl;
        }
        if(callFor==CallFor.CLOSE_COMPLAIN){
            url = url+URL.CLOSE_COMPLAINS+extendedUrl;
        }
        if(callFor==CallFor.GET_COMPLAIN_CATEGORY){
            url = url+URL.GET_COMPLAIN_CATEGORY;
        }
        if(callFor==CallFor.GET_COMPLAIN_DETAILS){
            url = url+URL.GET_COMPLAINT_DETAILS+extendedUrl;
        }

        if(callFor==CallFor.ERROR_ENTRY){
            url = url+URL.MARK_ERROR+extendedUrl;
        }
        // RATTING URL
        if(callFor==CallFor.PROVIDER_LIST){
            url = url_rating+URL.PROVIDER_LIST+extendedUrl;
        }

        // SEARCH VEHCILE
        if(callFor==CallFor.VEHICLENOPATTERN){
            url = url+URL.VEHICLE_NO_PATTERN;
        }
        if(callFor==CallFor.SEARCH_VEHICLE){
            url = url+URL.SEARCH_VEHICLE+extendedUrl;
        }
        if(callFor==CallFor.ADD_VEHICLE){
            url = url+URL.ADD_VEHICLE+extendedUrl;
        }
        if(callFor==CallFor.GET_VEHICLES){
            url = url+URL.GET_FLAT_VEHICLES;
        }
        if(callFor==CallFor.DELETE_VEHICLE){
            url = url+URL.DELETE_FLAT_VEHICLES+extendedUrl;
        }
        if(callFor==CallFor.MESSAGE){
            url = url+URL.MESSAGE+extendedUrl;
        }
        if(callFor==CallFor.GET_GROUPS){
            url = url+URL.GET_ALL_GROUPS;
        }
        if(callFor==CallFor.GET_FLATS){
            url = url+URL.GET_ALL_FLATS;
        }
        if(callFor == CallFor.LOCAL_STORES){
            url = url+URL.LOCAL_STORES;
        }
        // Poll Api's
        if(callFor == CallFor.USER_POLLS){
            url = url + URL.GET_USER_POLLS;
        }
        if(callFor == CallFor.ADMIN_POLLS){
            url = url + URL.GET_ADMIN_POLLS;
        }
        if(callFor == CallFor.SAVE_POLL){
            url = url + URL.SAVE_USER_POLL+extendedUrl;
        }

        if(callFor == CallFor.ADD_EMAIL_ID){
            url = url + URL.ADD_EMAIL+extendedUrl;
        }

        if(callFor == CallFor.DOMESTIC_HELP){
            url = url + URL.DOMESTIC_HELP+extendedUrl;
        }

        // CAR POOL APIS
        if(callFor == CallFor.GET_CAR_POOL_REQUEST){
            url = url + URL.GET_CAR_POOL_REQUEST;
        }
        if(callFor == CallFor.GET_CAR_POOL_OFFERS){
            url = url + URL.GET_CAR_POOL_OFFERS;
        }
        if(callFor == CallFor.CLOSE_CAR_POOL_OFFERS){
            url = url + URL.CLOSE_CAR_POOL_OFFERS+extendedUrl;
        }
        if(callFor == CallFor.CLOSE_CAR_POOL_REQUEST){
            url = url + URL.CLOSE_CAR_POOL_REQUEST+extendedUrl;
        }

        // Market Place
        if(callFor == CallFor.GET_ALL_MARKET_PLACE_ADD){
            url = url + URL.GET_ALL_ADV;
        }
        if(callFor == CallFor.GET_MARKET_PLACE_DETAILS){
            url = url + URL.GET_DETAILS_OF_ADV+extendedUrl;
        }
        if(callFor == CallFor.RAINBOW_USERS){
            url = url+URL.RAINBOW_USERS+extendedUrl;
        }

        if(callFor == CallFor.INVITATION_PURPOSE){
            url = url+URL.INVITATION_PURPOSE;
        }
        if(callFor == CallFor.INVITATION_LIST){
            url = url+URL.INVITATION_LIST;
        }
        if(callFor == CallFor.INVITATION_DETAIL){
            url = url+URL.INVITATION_DETAIL+extendedUrl;
        }

        if(callFor == CallFor.RAINBOW_ACCOUNT_DETAILS){
            url = url+URL.RAINBOW_ACCOUNT_DETAILS;
        }

        if(callFor == CallFor.INVOICE_LIST){
            url = url+URL.INVOICE_LIST;
        }

        if(callFor == CallFor.INVOICE_URL){
            url = url+URL.GET_INVOICE_URL+extendedUrl;
        }

        if(callFor == CallFor.DUE_INVOICE_INFO){
            url = url+URL.DUE_INVOICE_INFO;
        }

        if(callFor == CallFor.SENT_MESSAGE_API){
            url = url+URL.SENT_MESSAGE_API+extendedUrl;
        }

        if(callFor == CallFor.SENT_MESSAGE_DETAIL){
            url = url+URL.SENT_MESSAGE_DETAIL+extendedUrl;
        }

        //api secondary user
        if(callFor == CallFor.GET_SECONDARY_USER){
            url = url+URL.GET_SECONDARY_USER;
        }
        if(callFor == CallFor.DELETE_SECONDARY_USER){
            url = url+URL.DELETE_SECONDARY_USER+extendedUrl;
        }

        //api call for notification menu
        if(callFor == CallFor.GET_NOTIFICATION_MENU){
            url = url+URL.GET_NOTIFICATION_MENU;
        }
        //api call for notification menu
        if(callFor == CallFor.GETPREAPPROVALPURPOSE){
            url = url+URL.GETPREAPPROVALPURPOSE;
        }
        //getPreApprovalList
        if(callFor == CallFor.GETPREAPPROVALIST){
            url = url+URL.GETPREAPPROVALIST;
        }
        //DELETE PRE APPROVAL
        if(callFor == CallFor.DELETEPREAPPROVAL){
            url = url+URL.DELETEPREAPPROVAL+extendedUrl;
        }
        //api call for notification menu
        if(callFor == CallFor.GETPREAPPROVALPURPOSE){
            url = url+URL.GETPREAPPROVALPURPOSE;
        }
        //getPreApprovalList
        if(callFor == CallFor.GETPREAPPROVALIST){
            url = url+URL.GETPREAPPROVALIST;
        }
        if(callFor == CallFor.EXTRA_FIELD_ONE){
            url = url+URL.EXTRA_FIELD_ONE;
        }
        if(callFor == CallFor.EXTRA_FIELD_TWO){
            url = url+URL.EXTRA_FIELD_TWO;
        }

        return url;
    }

    @Override
    protected void onPreExecute() {
        try {
            check = SheredPref.getRun(activity);
        } catch (Exception e){
            check = null;
        }
        if(check == null) {
            check="";
        }
        if(check.equals("RUN")) {
            if(MainActivity.fragment_flag==1){

            } else {
                if(activity !=null) {
                    progressDialog = ProgressDialog.show(activity, "", "Loading...");
                }
            }
        }
    }

    @Override
    protected Object doInBackground(Object[] params) {
        Log.e("GET URL ===>",url);
        try {
            result = ServerConnection.giveResponse(url,"", null);
           Log.e("GET RESULT "," "+result);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        try {
            super.onPostExecute(o);
            Log.e("check",check+"--");
            if(check==null) {
                check="";
            }
            if(check.equals("RUN")) {
                if(MainActivity.fragment_flag==1) {

                } else {
                    if(activity !=null) {
                        progressDialog.dismiss();
                    }
                }
            }
            if(activity !=null) {
                activity.onGetResponse(result,callFor);
            }
            if(fragment!=null){
                fragment.onGetResponse(result, callFor);
            }

        }catch (Exception ex) {
            Log.e("Exception In GetData "," "+ex);
            if(activity!=null) {
                View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
                final Snackbar snackBar = Snackbar.make(rootView, "Something Went Wrong", 2000);
                snackBar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackBar.dismiss();
                    }
                });
                snackBar.show();
            }
        }
    }
}
