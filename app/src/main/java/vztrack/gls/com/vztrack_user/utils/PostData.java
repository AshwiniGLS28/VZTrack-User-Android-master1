package vztrack.gls.com.vztrack_user.utils;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import com.google.android.material.snackbar.Snackbar;
import android.util.Log;
import android.view.View;

import java.net.URLEncoder;

import vztrack.gls.com.vztrack_user.activity.BaseActivity;
import vztrack.gls.com.vztrack_user.activity.BaseFragment;
import vztrack.gls.com.vztrack_user.activity.BaseFragment;
import vztrack.gls.com.vztrack_user.activity.MainActivity;

/**
 * Created by Santosh on 06-Oct-15.
 */
public class PostData extends AsyncTask {
    String url,url_rating;
    String result = "";
    String callFor = "";
    String data = "";
    BaseActivity activity;
    ProgressDialog progressDialog;
    String check,extendedUr;
    BaseFragment fragment;
   // LoginResult response;

    public PostData(String data, BaseActivity activity, String callFor){
        this.activity = activity;
        this.callFor = callFor;
        this.data = data;
        url = createURL(callFor);
    }
    public PostData(String data, BaseFragment fragment, String callFor){
        this.fragment = fragment;
        this.callFor = callFor;
        this.data = data;
        url = createURL(callFor);
    }

    public PostData(BaseActivity activity,String callFor, String extendedUr)
    {
        this.activity = activity;
        this.callFor = callFor;
        this.extendedUr = extendedUr;
        url = createURL(callFor);
    }
//    public PostData(String data, BaseFragment fragment, String callFor){
//        this.fragment = fragment;
//        this.callFor = callFor;
//        this.data = data;
//        url = createURL(callFor);
//    }

    private String createURL(String callFor) {
        url = Constants.BASE_URL;
        url_rating = Constants.BASE_URL_FOR_RATING;

        if(callFor==CallFor.LOGIN){
            url = url+ URL.LOGIN;
        }
        if(callFor==CallFor.LOGOUT){
            url = url+URL.LOGOUT;
        }
        if(callFor==CallFor.PENDING_RATING){
            url = url_rating+URL.PENDING_RATINGS;

        }
        if(callFor==CallFor.CANCLE_RATING){
            url = url_rating+URL.CANCLE_RATING;
        }
        if(callFor==CallFor.SAVE_RATING){
            url = url_rating+URL.SAVE_RATINGS;
        }
        if(callFor==CallFor.PROVIDERS_DATA){
            url = url_rating+URL.PROVIDERS_DATA;
        }
        if(callFor==CallFor.PROVIDER_DEATILS_DATA){
            url = url_rating+URL.PROVIDER_DETAIL_DATA;
        }
        if(callFor==CallFor.SEND_NOTICE){
            url = url+URL.SEND_NOTICE;
        }
        if(callFor==CallFor.SOS){
            url = url+URL.SOS;
        }
        if(callFor==CallFor.FEEDBACK){
            url = url+URL.FEEDBACK;
        }
        if(callFor==CallFor.ADD_COMPLAIN){
            url = url+URL.ADD_COMPLAINS;
        }
        if(callFor==CallFor.SAVE_COMPLAINT_COMMENT){
            url = url+URL.SAVE_COMPLAINT_COMMENT;
        }
        if(callFor==CallFor.CHANGE_COMPLAINT_STATUS){
            url = url+URL.CHANGE_COMPLAINT_STATUS;
        }
        if(callFor==CallFor.SEND_MESSAGE){
            url = url+URL.SEND_MESSAGE;
        }
        if(callFor==CallFor.SEND_MESSAGE_NOTIFICATION){
            url = url+URL.SEND_MESSAGE_NOTIFICATION;
        }
        if(callFor==CallFor.SEND_VEHICLE_MESSAGE_NOTIFICATION){
            url = url+URL.SEND_VEHICLE_MESSAGE_NOTIFICATION;
        }

        // Poll URL's
        if(callFor==CallFor.POLL_OPERATION){
            url = url+URL.POLL_OPERATIONS;
        }

        // Car Pool URL's
        if(callFor==CallFor.ADD_CAR_POOL_REQUEST){
            url = url+URL.CAR_POOL_ADD_REQUEST;
        }
        if(callFor==CallFor.ADD_CAR_POOL_OFFER){
            url = url+URL.CAR_POOL_ADD_OFFER;
        }
        if(callFor==CallFor.ADD_NEW_MARKET_PLACE_COMMENT){
            url = url+URL.SAVE_NEW_MARKET_PLACE_COMMENT;
        }

         if(callFor==CallFor.CREATE_EDIT_CLOSE_ADV){
            url = url+URL.CREATE_EDIT_CLOSE_ADV;
        }
        if(callFor == CallFor.SAVE_INVITATION){
            url = url+URL.SAVE_INVITATION;
        }

        if(callFor == CallFor.ADD_SECONDARY_USER){
            url = url+URL.ADD_SECONDARY_USER;
        }

        if(callFor == CallFor.SAVE_NOTIFICATION_OPTION){
            url = url+URL.SAVE_NOTIFICATION_OPTION;
        }
        if (callFor==CallFor.ADD_PREAPPROVAL)
        {
            url=url+URL.ADD_PREAPPROVAL;
        }
        if (callFor==CallFor.SAVE_USER_PHOTO)
        {
            url = url+URL.SAVE_USER_PHOTO;
        }

        if (callFor==CallFor.REOPENCOMPLAINT)
        {
            url=url+URL.REOPENCOMPLAINT;
        }

        if(callFor == CallFor.DELETEPREAPPROVAL){
            url = url+URL.DELETEPREAPPROVAL;
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
    if(check.equals("RUN"))
    {
        progressDialog = ProgressDialog.show(activity,"","Loading...");
    }
}

    @Override
    protected Object doInBackground(Object[] params) {
        Log.e("POST URL ===>",url);
        Log.e("POST Data ===>",data);
        try {
            data = StringFormatter.convertStringToUTF8(data);
            result = ServerConnection.giveResponse(url,data,null);
            Log.e("POST Result ===>",result);
        } catch (Exception e){
            Log.e("Error doInBackground",e.toString());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {

        try
        {
            super.onPostExecute(o);

            if(check==null)
            {
                check="";
            }
            if(check.equals("RUN"))
            {
                progressDialog.dismiss();
            }
            if(fragment!=null){
                fragment.onGetResponse(result, callFor);
            }
            if(activity !=null) {
                activity.onGetResponse(result,callFor);
            }
//            activity.onGetResponse(result, callFor);
        }catch (Exception ex)
        {
            View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
            final Snackbar snackBar = Snackbar.make(rootView, "Something Went Wrong" , 2000);
            snackBar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackBar.dismiss();
                }
            });
            snackBar.show();
            Log.e("EX "," "+ex);
        }

    }
}
