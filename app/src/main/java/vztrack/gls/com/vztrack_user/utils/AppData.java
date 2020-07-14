package vztrack.gls.com.vztrack_user.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

/**
 * Created by Santosh on 20-Jan-18.
 */

public class AppData {
    public final String SP_NAME = "RAINBOW";
    public final String EMAIL = "EMAIL";
    public final String PASS = "PASS";
    public final String RAINBOW_MSG = "RAINBOW_MSG";

    //constants for approval/deny story

    public static final String WFA="Not approved";
    public static final String SA="Society approved";
    public static final String SGA="Approved by guard";
    public static final String SGD="Denied by guard";
    public static final String APP="Approved";
    public static final String DEN="Denied";
    public static final String NI="Not installed";
    public static final String OA="Offline approved";

    Context context;
    SharedPreferences sp;
    public AppData(Context context){
        this.context = context;
        sp = this.context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public String getUserEmail(){
        return sp.getString(EMAIL,"");
    }

    public void setUserEmail(String email){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(EMAIL,email);
        editor.commit();
    }

    public String getPassword(){
        return sp.getString(PASS,"");
    }

    public void setPassword(String pass){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PASS,pass);
        editor.commit();
    }

    public void setRainbowMessage(RemoteMessage remoteMessage){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(RAINBOW_MSG,new Gson().toJson(remoteMessage));
        editor.commit();
    }

    public RemoteMessage getRainbowMessage(){
        try{
            return new Gson().fromJson(sp.getString(RAINBOW_MSG,""), RemoteMessage.class);
        }catch (Exception e){
            return null;
        }

    }
}
