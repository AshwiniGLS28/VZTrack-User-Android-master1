package vztrack.gls.com.vztrack_user.utils;

import android.content.Context;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;

import java.util.HashMap;

/**
 * Created by sandeep on 16/3/16.
 */
public class CleverTapRegisterEvents {

    public static void addCleverTapEvent(Context context, String mainEvent) {
        String society_name = SheredPref.getSociety_Name(context);
        String flat_number = SheredPref.getFlat_No(context);
        CleverTapAPI cleverTap = null;
        try {
            cleverTap = CleverTapAPI.getInstance(context);
            HashMap<String, Object> keys = new HashMap<String, Object>();
            keys.put(Events.event_vehicle_servicing_key_society_name, society_name);
            keys.put(Events.event_vehicle_servicing_key_flat_number, flat_number);
            cleverTap.event.push(mainEvent, keys);
        } catch (CleverTapMetaDataNotFoundException e) {
            e.printStackTrace();
        } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
            cleverTapPermissionsNotSatisfied.printStackTrace();
        }
    }

    public static void LoginActionEvent(Context context){
        String society_name = SheredPref.getSociety_Name(context);
        try {
            CleverTapAPI cleverTap = CleverTapAPI.getInstance(context);
            HashMap<String, Object> loginAction = new HashMap<String, Object>();
            loginAction.put(Events.event_key_society_name, society_name);
            cleverTap.event.push(Events.event_login_action, loginAction);
        } catch (CleverTapMetaDataNotFoundException e) {
            e.printStackTrace();
        } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
            cleverTapPermissionsNotSatisfied.printStackTrace();
        }
    }

}
