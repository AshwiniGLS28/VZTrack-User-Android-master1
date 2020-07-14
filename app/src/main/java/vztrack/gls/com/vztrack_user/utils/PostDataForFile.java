package vztrack.gls.com.vztrack_user.utils;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import com.google.android.material.snackbar.Snackbar;
import android.util.Log;
import android.view.View;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.activity.BaseActivity;
import vztrack.gls.com.vztrack_user.beans.NoticeBean;

/**
 * Created by Santosh on 06-Oct-15.
 */
public class PostDataForFile extends AsyncTask {
    String url,url_rating;
    String result = "";
    String callFor = "";
    NoticeBean noticeBean;
    BaseActivity activity;
    ProgressDialog progressDialog;
    String fileName;

    public PostDataForFile(NoticeBean noticeBean, BaseActivity activity, String callFor){
        this.activity = activity;
        this.callFor = callFor;
        this.noticeBean = noticeBean;
        url = createURL(callFor);
    }

    private String createURL(String callFor) {
        url = Constants.BASE_URL;
        if(callFor==CallFor.SEND_NOTICE){
            url = url+URL.NOTICES_UPDATED;
        }
        if(callFor==CallFor.DOWNLOAD_NOTICE_FILE){
            url = url+URL.DOWNLOAD_NOTICE_FILE+"?noticeId="+noticeBean.getNoticeId();;
        }

        return url;
    }

    @Override
    protected void onPreExecute() {
        if(callFor==CallFor.SEND_NOTICE){
            progressDialog = ProgressDialog.show(activity,"","Uploading File, Please Wait...");
        }
        if(callFor==CallFor.DOWNLOAD_NOTICE_FILE){
            progressDialog = ProgressDialog.show(activity,"","Downloading File, Please Wait...");
        }

    }

    @Override
    protected Object doInBackground(Object[] params) {
        Log.e("POST URL ===>",url);
        try {

            if(callFor==CallFor.SEND_NOTICE){
                result = ServerConnectionForFile.giveResponse(url,noticeBean);
            }
            if(callFor==CallFor.DOWNLOAD_NOTICE_FILE){
                fileName = ServerConnectionForFile.downloadFile(url, activity);
            }
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

            if(callFor==CallFor.DOWNLOAD_NOTICE_FILE){
                if(fileName.equals("")){
                    CommonMethods.showToastError(activity,"Error In Downloading File");//.show();
                }else{
                    CommonMethods.showToastSuccess(activity,"Downloaded File Successfully");//.show();
                }
            }
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
