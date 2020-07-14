package vztrack.gls.com.vztrack_user.fcm;

import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import com.ale.rainbowsdk.RainbowSdk;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;

import vztrack.gls.com.vztrack_user.profile.UserBean;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Constants;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.ServerConnection;
import vztrack.gls.com.vztrack_user.utils.SheredPref;
import vztrack.gls.com.vztrack_user.utils.URL;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        RainbowSdk.instance().push().onTokenRefresh(refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        UserBean userBean = new UserBean();
        String userName = SheredPref.getUsername(this);
        String password = SheredPref.getPassword(this);
        userBean.setUser_name(userName);
        userBean.setUser_password(password);
        String device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        userBean.setUser_dev_id(device_id);
        userBean.setUser_gcm_id(token);
        userBean.setDevice_os("AND");
        final String data = new Gson().toJson(userBean);
        Log.e("Token@OnTokenRefresh",""+token);
        class sendRegistrationToServer extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] params) {
                String url = Constants.BASE_URL+ URL.LOGIN;
                String result = ServerConnection.giveResponse(url,data, null);
                Log.e("Responce OnTokenRefresh",""+result);
                return null;
            }
        }
        new sendRegistrationToServer().execute();
        CleverTap.cleverTap_Record_Event(this, Events.fcm);
    }
}