package vztrack.gls.com.vztrack_user.utils;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import com.google.android.material.snackbar.Snackbar;
import android.util.Log;
import android.view.View;

import java.io.InputStream;

import vztrack.gls.com.vztrack_user.activity.BaseActivity;

/**
 * Created by Santosh on 06-Oct-15.
 */
public class PostDataForFileNew extends AsyncTask {
    String url;
    String result = "";
    String callFor = "";
    String data = "";
    BaseActivity activity;
    ProgressDialog progressDialog;
    InputStream inputStream;

    public PostDataForFileNew(String data, InputStream inputStream, BaseActivity activity, String callFor){
        this.activity = activity;
        this.callFor = callFor;
        this.data = data;
        this.inputStream = inputStream;
        url = createURL(callFor);
    }

    private String createURL(String callFor) {
        url = Constants.BASE_URL;
        if(callFor==CallFor.CREATE_EDIT_CLOSE_ADV){
            url = url+URL.CREATE_EDIT_CLOSE_ADV;
        }
        if(callFor==CallFor.LOGIN){
            url = url+ URL.LOGIN;
        }

        return url;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(activity,"","Loading...");
    }

    @Override
    protected Object doInBackground(Object[] params) {
        Log.e("POST UR.L ===>",url+"----------------");
        Log.e("POST Data ===>",data+"-------------");
        try {
            if(callFor==CallFor.CREATE_EDIT_CLOSE_ADV){
                result = ServerConnection.giveResponse(url, data, inputStream);
            }
//            if(callFor==CallFor.SAVE_USER_PHOTO){
//                result = ServerConnection.giveResponse(url, data, inputStream);
//            }
            Log.e("POST Result ===>",result);

        }catch (Exception e){
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
            progressDialog.dismiss();
            activity.onGetResponse(result, callFor);
        }
        catch (Exception ex)
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
