package vztrack.gls.com.vztrack_user.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.text.InputFilter;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.responce.LoginResponse;

/**
 * Created by sandeep on 16/3/16.
 */
public class UtilityMethodsAndroid {

    public static void makeCall(Context context, String mobileNo) {
        Intent call = new Intent(Intent.ACTION_DIAL);
        call.setData(Uri.parse("tel:" +mobileNo));
        context.startActivity(call);
    }

    public static void CloseKeyBoard(Context con){
        InputMethodManager imm = (InputMethodManager) con.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public static void openKeyboard(Context con) {
        InputMethodManager imm = (InputMethodManager) con.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null){
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }
    }

    public static InputStream ConvertBitmapToInptuStream(Bitmap bitmap){
        InputStream inputStream;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 60, out);
        byte[] bitmapdata = out.toByteArray();
        inputStream = new ByteArrayInputStream(bitmapdata);
        return inputStream;
    }

    public static Bitmap decodeSampledBitmapFromFile(String path,
                                                     int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }

        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }


        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }
    
    public static Bitmap rotateImage(File imageFile, Bitmap bitmap){
        try{
            ExifInterface exif=new ExifInterface(imageFile.getAbsolutePath());
            if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("6")){
                bitmap=rotate(bitmap, 90);
            }else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("8")){
                bitmap=rotate(bitmap, 270);
            }else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("3")){
                bitmap=rotate(bitmap, 180);
            }
        }catch (Exception e){

        }
        return bitmap;
    }


    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix mtx = new Matrix();
        mtx.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    public static void setEditTextMaxLength(int length, EditText etPhoneNumber) {
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(length);
        etPhoneNumber.setFilters(filterArray);
    }

    public static void updateSheredPreferenceValues(Context context, LoginResponse loginResponse) {
        SheredPref.setAdminAccess(context,loginResponse.getFamily().isExtraAccess());
        SheredPref.setSOSAccess(context,loginResponse.getConfigBean().isSos());
        SheredPref.setType(context,loginResponse.getConfigBean().isCompany());
        SheredPref.setComplaint(context,loginResponse.getConfigBean().isComplain());
        SheredPref.setCompany(context, loginResponse.getConfigBean().isCompany());
        SheredPref.setLocalStores(context, loginResponse.getConfigBean().isLocalStore());
        SheredPref.setOwnerName(context, loginResponse.getFamily().getFlatOwnerName());
        SheredPref.setPoll(context, loginResponse.getConfigBean().isPolls());
        SheredPref.setLoginByEmail(context, loginResponse.getFamily().isLoginByEmail());

        SheredPref.setAccess_KnowYourMain(context, loginResponse.getConfigBean().isKnowYourMaid());
        SheredPref.setAccess_Rainbow(context, loginResponse.getConfigBean().isRainbow());
        SheredPref.setAccess_MarketPlace(context, loginResponse.getConfigBean().isMarketplace());
        SheredPref.setAccess_SearchVehicle(context, loginResponse.getConfigBean().isSearchVehicle());
        SheredPref.setAccess_Car_Pool(context, loginResponse.getConfigBean().isCarpool());
        SheredPref.setAccess_KnowYourMain(context, loginResponse.getConfigBean().isKnowYourMaid());
        SheredPref.setAccess_Visitor(context, loginResponse.getConfigBean().isVisitors());
        SheredPref.setAccess_Message(context, loginResponse.getConfigBean().isMessages());
        SheredPref.setAccess_Notice(context, loginResponse.getConfigBean().isNoticesAndMinuts());
        SheredPref.setAccess_Setting(context, loginResponse.getConfigBean().isSetting());
        SheredPref.setAccess_Refer(context, loginResponse.getConfigBean().isReferVz());
        SheredPref.setAccess_Search_Provider(context, loginResponse.getConfigBean().isServiceProvider());
        SheredPref.setAccess_Support(context, loginResponse.getConfigBean().isSupport());
        SheredPref.setAccess_Invoice(context, loginResponse.getConfigBean().isInvoice());
        SheredPref.setMaxMobileLen(context, loginResponse.getSocity().getMaxMobLen());
        SheredPref.setMinMobileLen(context, loginResponse.getSocity().getMinMobLen());
        SheredPref.setVehicleServicingAccess(context, loginResponse.getConfigBean().isGarageWorks());
        SheredPref.setInvitationAccess(context, loginResponse.getConfigBean().isInviteGuest());
        SheredPref.setRainbowAccountCount(context, loginResponse.getFamily().getNumberOfRainbowAcct());
        SheredPref.setPreApprovalsAccess(context, loginResponse.getConfigBean().isPreApproval());
        SheredPref.setThermalAccess(context, loginResponse.getConfigBean().isThermal());
        if(SheredPref.getPhoneNotificationRingtone(context).isEmpty()){
            Ringtone defaultRingtone = RingtoneManager.getRingtone(context,
                    Settings.System.DEFAULT_RINGTONE_URI);
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            SheredPref.setPhoneNotificationRingtone(context, uri.toString());
            SheredPref.setPhoneRingtoneTitle(context, defaultRingtone.getTitle(context));
        }
    }

    public static String getIntitialLetter(String name){
        String first_letter = null;
        String last_letter = null;
        try{
            name = name.trim();
            if(name.split(" ").length>1){
                first_letter = name.split(" ")[0].substring(0,1).toUpperCase().trim();
                last_letter = name.split(" ")[1].substring(0,1).toUpperCase().trim();
            }else if(name.equals("")){
                first_letter="";
                last_letter="";
            }else{
                first_letter = name.substring(0,1).toUpperCase().trim();
                last_letter = "";
            }
        }catch (Exception ex){
            Log.e("Exception "," In Getting Initial Letter "+ex);
        }


        return first_letter+last_letter;
    }

    public static void ShowNoInternetToast(Context context){
        CommonMethods.showToastError(context, context.getResources().getString(R.string.pleaseCheckInternet));//, Toast.LENGTH_SHORT, true).show();
    }

    public static void   setActionBarTitle(FragmentActivity context){
        String title = ((MainActivity)context).getMenuTitle();
        Log.e("setActionBarTitle ",title+"--");
        if (title!=null) {
            if (!title.isEmpty())
                ((MainActivity) context).getSupportActionBar().setTitle(title);
        }
    }
    public static void ShowPermissionToast(Context context){
        CommonMethods.showToastSuccess(context, context.getResources().getString(R.string.allow_permission));//.show();
    }

    public static int GetVersionCode(Context context, String TAG){
        int version = 0;
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionCode;;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Exception "+e);
        }
        return version;
    }
}
