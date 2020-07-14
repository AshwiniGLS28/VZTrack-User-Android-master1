package vztrack.gls.com.vztrack_user.utils;

import android.Manifest;
import android.Manifest.permission;

/**
 * Created by sandeep on 16/3/16.
 */
public class PermissionConstant {
//    public static final String PERMISSION_CALL =  Manifest.permission.CALL_PHONE;   // Telephone
//    public static final int REQ_CODE_CALL = 3;

    public static final String PERMISSION_CAMERA =  Manifest.permission.CAMERA;     // Camera
    public static final int REQ_CODE_CAMERA = 4;


    public static final String PERMISSION_AUDIO =  Manifest.permission.RECORD_AUDIO;    //
    public static final int REQ_CODE_AUDIO = 5;

    public static final String PERMISSION_EXTERNAL_STORAGE =  permission.WRITE_EXTERNAL_STORAGE;    //Storage
    public static final int REQ_CODE_EXTERNAL_STORAGE = 6;

    public static final String PERMISSION_READ_CONTACT =  permission.READ_CONTACTS;    //Storage
    public static final int REQ_CODE_READ_CONTACT  = 7;


    public static final String PERMISSION_READ_PHONE_STATE =  Manifest.permission.READ_PHONE_STATE;    //Phone State
    public static final int REQ_CODE_READ_PHONE_STATE = 8;

    public static final String PERMISSION_CALL_PHONE =  Manifest.permission.CALL_PHONE;;    //CALL
    public static final int REQ_CODE_CALL_PHONE = 9;
}
