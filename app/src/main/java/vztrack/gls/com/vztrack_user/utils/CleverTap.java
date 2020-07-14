package vztrack.gls.com.vztrack_user.utils;

import android.content.Context;
import android.util.Log;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;

/**
 * Created by sandeep on 31/12/16.
 */

public class CleverTap {

    public static CleverTapAPI cleverTap;

    public static void cleverTap_Record_Event(Context context, String eventName){
        // Clever Tap CleverTapAPI ct;
        try {
            cleverTap = CleverTapAPI.getInstance(context);
            cleverTap.event.push(eventName);
        } catch (CleverTapMetaDataNotFoundException e) {
            // handle appropriately
            Log.e("CleverTap Exception "," "+e);
        } catch (CleverTapPermissionsNotSatisfied e) {
            // handle appropriately
            Log.e("CleverTap Exception "," "+e);
        }
    }
}
