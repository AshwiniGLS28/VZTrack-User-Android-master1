package vztrack.gls.com.vztrack_user.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permissions {

    public static boolean askPermission(Context context, String permission, int requestCode) {
        boolean perm = true;
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            // We dont have permission
            ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
            perm = false;
        }
        return perm;
    }


    public static boolean askPermissions(Activity context, String permission, int requestCode) {
        boolean perm = true;
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            // We dont have permission
            ActivityCompat.requestPermissions(context, new String[]{permission}, requestCode);
            perm = false;
        }

        return perm;
    }
}

