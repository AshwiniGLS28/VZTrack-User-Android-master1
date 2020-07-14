package vztrack.gls.com.vztrack_user.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.beans.DrawerConfig;
import vztrack.gls.com.vztrack_user.beans.DrawerConfigBean.ConfigBean;

/**
 * Created by sandeep on 16/3/16.
 */
public class SheredPref {

    public static String getLoginInfo(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.VALIDATION, Context.MODE_PRIVATE);
        return sp.getString(Finals.LOGIN_CHECKED,"");
    }
    public static void setLoginInfo(Context context,String city){
        SharedPreferences sp = context.getSharedPreferences(Finals.VALIDATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.LOGIN_CHECKED,city);
        editor.commit();
    }


    public static String getSociety_Name(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.SOCIETYNAME, Context.MODE_PRIVATE);
        return sp.getString(Finals.SOCIETYNAME,"");
    }
    public static void setSociety_Name(Context context,String city){
        SharedPreferences sp = context.getSharedPreferences(Finals.SOCIETYNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.SOCIETYNAME,city);
        editor.commit();
    }


    public static String getFlat_No(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.FLATNO, Context.MODE_PRIVATE);
        return sp.getString(Finals.FLATNO,"");
    }
    public static void setFlat_No(Context context,String city){
        SharedPreferences sp = context.getSharedPreferences(Finals.FLATNO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.FLATNO,city);
        editor.commit();
    }


    public static String getUsername(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.USERNAME, Context.MODE_PRIVATE);
        return sp.getString(Finals.USERNAME,"");
    }
    public static void setUSername(Context context,String username){
        SharedPreferences sp = context.getSharedPreferences(Finals.USERNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.USERNAME,username);
        editor.commit();
    }


    public static String getPassword(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.PASSWORD, Context.MODE_PRIVATE);
        return sp.getString(Finals.PASSWORD,"");
    }
    public static void setPassword(Context context,String city){
        SharedPreferences sp = context.getSharedPreferences(Finals.PASSWORD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.PASSWORD,city);
        editor.commit();
    }

    public static String getExecute(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.Exe, Context.MODE_PRIVATE);
        return sp.getString(Finals.Exe,"");
    }
    public static void setExecute(Context context,String city){
        SharedPreferences sp = context.getSharedPreferences(Finals.Exe, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.Exe,city);
        editor.commit();
    }

    public static String getExecuteOffline(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.ExeOffline, Context.MODE_PRIVATE);
        return sp.getString(Finals.Exe,"");
    }
    public static void setExecuteOffline(Context context,String city){
        SharedPreferences sp = context.getSharedPreferences(Finals.ExeOffline, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.Exe,city);
        editor.commit();
    }

    public static String getFirstExecute(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.FirstExe, Context.MODE_PRIVATE);
        return sp.getString(Finals.FirstExe,"");
    }
    public static void setFirstExecute(Context context,String city){
        SharedPreferences sp = context.getSharedPreferences(Finals.FirstExe, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.FirstExe,city);
        editor.commit();
    }

    public static String getDate(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.DATE, Context.MODE_PRIVATE);
        return sp.getString(Finals.DATE,"");
    }
    public static void setDate(Context context,String Sname){
        SharedPreferences sp = context.getSharedPreferences(Finals.DATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.DATE,Sname);
        editor.commit();
    }

    public static String getDateFor_Visitors(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.DATE_VISITORS, Context.MODE_PRIVATE);
        return sp.getString(Finals.DATE,"");
    }
    public static void setDateFor_Visitors(Context context,String Sname){
        SharedPreferences sp = context.getSharedPreferences(Finals.DATE_VISITORS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.DATE,Sname);
        editor.commit();
    }


    public static String getRun(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.RUN, Context.MODE_PRIVATE);
        return sp.getString(Finals.RUN,"");
    }
    public static void setRun(Context context,String check){
        SharedPreferences sp = context.getSharedPreferences(Finals.RUN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.RUN,check);
        editor.commit();
    }

    public static String getNotification(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.NOTIFICATION, Context.MODE_PRIVATE);
        return sp.getString(Finals.NOTIFICATION,"");
    }
    public static void setNotification(Context context,String check){
        SharedPreferences sp = context.getSharedPreferences(Finals.NOTIFICATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.NOTIFICATION,check);
        editor.commit();
    }

    public static String getSound(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.SOUND, Context.MODE_PRIVATE);
        return sp.getString(Finals.SOUND,"");
    }
    public static void setSound(Context context,String check){
        SharedPreferences sp = context.getSharedPreferences(Finals.SOUND, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.SOUND,check);
        editor.commit();
    }

    public static String getVibration(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.VIBRATION, Context.MODE_PRIVATE);
        return sp.getString(Finals.VIBRATION,"");
    }
    public static void setVibration(Context context,String check){
        SharedPreferences sp = context.getSharedPreferences(Finals.VIBRATION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.VIBRATION,check);
        editor.commit();
    }

    public static String getWingName(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.WING, Context.MODE_PRIVATE);
        return sp.getString(Finals.WING,"");
    }
    public static void setWingName(Context context,String check){
        SharedPreferences sp = context.getSharedPreferences(Finals.WING, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.WING,check);
        editor.commit();
    }

    public static String getSocietyId(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.SOCIETYID, Context.MODE_PRIVATE);
        return sp.getString(Finals.SOCIETYID,"");
    }
    public static void setSocietyId(Context context,String check){
        SharedPreferences sp = context.getSharedPreferences(Finals.SOCIETYID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.SOCIETYID,check);
        editor.commit();
    }

    public static String getFamilyId(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.FAMILYID, Context.MODE_PRIVATE);
        return sp.getString(Finals.FAMILYID,"");
    }
    public static void setFamilyId(Context context,String check){
        SharedPreferences sp = context.getSharedPreferences(Finals.FAMILYID, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.FAMILYID,check);
        editor.commit();
    }

    public static String getDateForApi(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.DATE_FOR_API, Context.MODE_PRIVATE);
        return sp.getString(Finals.DATE_FOR_API,"");
    }
    public static void setDateForApi(Context context,String date){
        SharedPreferences sp = context.getSharedPreferences(Finals.DATE_FOR_API, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.DATE_FOR_API,date);
        editor.commit();
    }

    public static String getDateForCompApi(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.DATE_FOR__COMP_API, Context.MODE_PRIVATE);
        return sp.getString(Finals.DATE_FOR__COMP_API,"");
    }
    public static void setDateForCompApi(Context context,String date){
        SharedPreferences sp = context.getSharedPreferences(Finals.DATE_FOR__COMP_API, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.DATE_FOR__COMP_API,date);
        editor.commit();
    }


    public static boolean getTutorialFlag(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.TUTORIAL_FLAG, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.TUTORIAL_FLAG,false);
    }
    public static void setTutorialFlag(Context context,Boolean flag){
        SharedPreferences sp = context.getSharedPreferences(Finals.TUTORIAL_FLAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.TUTORIAL_FLAG,flag);
        editor.commit();
    }

    public static int getLatestAppVersion(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.APP_VERSION, Context.MODE_PRIVATE);
        return sp.getInt(Finals.APP_VERSION,0);
    }
    public static void setLatestAppVersion(Context context,int val){
        SharedPreferences sp = context.getSharedPreferences(Finals.APP_VERSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Finals.APP_VERSION,val);
        editor.commit();
    }

    public static boolean getAdminAccess(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.ADMIN, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.ADMIN,false);
    }
    public static void setAdminAccess(Context context,boolean flag){
        SharedPreferences sp = context.getSharedPreferences(Finals.ADMIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.ADMIN,flag);
        editor.commit();
    }
    public static String getPhoneNo(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.PHONE, Context.MODE_PRIVATE);
        return sp.getString(Finals.PHONE,"");
    }
    public static void setPhoneNo(Context context,String data){
        SharedPreferences sp = context.getSharedPreferences(Finals.PHONE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.PHONE,data);
        editor.commit();
    }


    public static boolean getSOSAccess(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.SOS, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.SOS,false);
    }
    public static void setSOSAccess(Context context,boolean flag){
        SharedPreferences sp = context.getSharedPreferences(Finals.SOS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.SOS,flag);
        editor.commit();
    }

    public static boolean getType(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.TYPE, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.TYPE,false);
    }
    public static void setType(Context context,Boolean type){
        SharedPreferences sp = context.getSharedPreferences(Finals.TYPE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.TYPE,type);
        editor.commit();
    }

    public static boolean getComplain(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.COMPLAINT, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.COMPLAINT,false);
    }
    public static void setComplaint(Context context,Boolean complaint){
        SharedPreferences sp = context.getSharedPreferences(Finals.COMPLAINT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.COMPLAINT,complaint);
        editor.commit();
    }

    public static boolean getLocalStores(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.LOCAL_STORES, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.LOCAL_STORES,false);
    }
    public static void setLocalStores(Context context,Boolean isLocalStore){
        SharedPreferences sp = context.getSharedPreferences(Finals.LOCAL_STORES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.LOCAL_STORES,isLocalStore);
        editor.commit();
    }

    public static boolean getAccess_KnowYourMain(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.KNOW_YOUR_MAID_ACCESS, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.KNOW_YOUR_MAID_ACCESS,false);
    }
    public static void setAccess_KnowYourMain(Context context,Boolean value){
        SharedPreferences sp = context.getSharedPreferences(Finals.KNOW_YOUR_MAID_ACCESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.KNOW_YOUR_MAID_ACCESS, value);
        editor.commit();
    }

    public static boolean getAccess_Raibow(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.RAINBOW_ACCESS, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.RAINBOW_ACCESS,false);
    }
    public static void setAccess_Rainbow(Context context,Boolean value){
        SharedPreferences sp = context.getSharedPreferences(Finals.RAINBOW_ACCESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.RAINBOW_ACCESS, value);
        editor.commit();
    }
    public static boolean getAccess_MarketPlace(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.MARKET_PLACE_ACCESS, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.MARKET_PLACE_ACCESS,false);
    }
    public static void setAccess_MarketPlace(Context context,Boolean value){
        SharedPreferences sp = context.getSharedPreferences(Finals.MARKET_PLACE_ACCESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.MARKET_PLACE_ACCESS, value);
        editor.commit();
    }

    public static boolean getAccess_SearchVehicle(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.SEARCH_VEHICLE_ACCESS, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.SEARCH_VEHICLE_ACCESS,false);
    }
    public static void setAccess_SearchVehicle(Context context,Boolean value){
        SharedPreferences sp = context.getSharedPreferences(Finals.SEARCH_VEHICLE_ACCESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.SEARCH_VEHICLE_ACCESS, value);
        editor.commit();
    }

    public static boolean getAccess_Visitor(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.VISITOR_ACCESS, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.VISITOR_ACCESS,false);
    }
    public static void setAccess_Visitor(Context context,Boolean value){
        SharedPreferences sp = context.getSharedPreferences(Finals.VISITOR_ACCESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.VISITOR_ACCESS, value);
        editor.commit();
    }

    public static boolean getAccess_Message(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.MESSAGE_ACCESS, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.MESSAGE_ACCESS,false);
    }
    public static void setAccess_Message(Context context,Boolean value){
        SharedPreferences sp = context.getSharedPreferences(Finals.MESSAGE_ACCESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.MESSAGE_ACCESS, value);
        editor.commit();
    }

    public static boolean getAccess_Notice(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.NOTICE_AND_MINUTES_ACCESS, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.NOTICE_AND_MINUTES_ACCESS,false);
    }
    public static void setAccess_Notice(Context context,Boolean value){
        SharedPreferences sp = context.getSharedPreferences(Finals.NOTICE_AND_MINUTES_ACCESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.NOTICE_AND_MINUTES_ACCESS, value);
        editor.commit();
    }

    public static boolean getAccess_Search_Provider(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.SEARCH_PROVIDER_ACCESS, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.SEARCH_PROVIDER_ACCESS,false);
    }
    public static void setAccess_Search_Provider(Context context,Boolean value){
        SharedPreferences sp = context.getSharedPreferences(Finals.SEARCH_PROVIDER_ACCESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.SEARCH_PROVIDER_ACCESS, value);
        editor.commit();
    }


    public static boolean getAccess_Setting(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.SETTING_ACCESS, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.SETTING_ACCESS,false);
    }
    public static void setAccess_Setting(Context context,Boolean value){
        SharedPreferences sp = context.getSharedPreferences(Finals.SETTING_ACCESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.SETTING_ACCESS, value);
        editor.commit();
    }

    public static boolean getAccess_Refer(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.REFER_ACCESS, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.REFER_ACCESS,false);
    }
    public static void setAccess_Refer(Context context,Boolean value){
        SharedPreferences sp = context.getSharedPreferences(Finals.REFER_ACCESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.REFER_ACCESS, value);
        editor.commit();
    }

    public static boolean getAccess_Car_Pool(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.CAR_POOL_ACCESS, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.CAR_POOL_ACCESS,false);
    }
    public static void setAccess_Car_Pool(Context context,Boolean value){
        SharedPreferences sp = context.getSharedPreferences(Finals.CAR_POOL_ACCESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.CAR_POOL_ACCESS, value);
        editor.commit();
    }

    public static boolean getAccess_Support(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.SUPPORT_ACCESS, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.SUPPORT_ACCESS,false);
    }
    public static void setAccess_Support(Context context,Boolean value){
        SharedPreferences sp = context.getSharedPreferences(Finals.SUPPORT_ACCESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.SUPPORT_ACCESS, value);
        editor.commit();
    }

    public static boolean getAccess_Invoice(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.INVOICE, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.INVOICE,false);
    }
    public static void setAccess_Invoice(Context context,Boolean value){
        SharedPreferences sp = context.getSharedPreferences(Finals.INVOICE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.INVOICE, value);
        editor.commit();
    }

    public static boolean isCompany(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.COMPANY, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.COMPANY,false);
    }
    public static void setCompany(Context context,Boolean isCompany){
        SharedPreferences sp = context.getSharedPreferences(Finals.COMPANY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.COMPANY,isCompany);
        editor.commit();
    }

    public static boolean isPrimary(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.PRIMARY, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.PRIMARY,false);
    }
    public static void setPrimary(Context context,Boolean isPrimary){
        SharedPreferences sp = context.getSharedPreferences(Finals.PRIMARY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.PRIMARY,isPrimary);
        editor.commit();
    }

    public static String getOwnerName(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.OWNER, Context.MODE_PRIVATE);
        return sp.getString(Finals.OWNER,"");
    }
    public static void setOwnerName(Context context,String data){
        SharedPreferences sp = context.getSharedPreferences(Finals.OWNER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.OWNER,data);
        editor.commit();
    }

    public static String getName(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.NAME, Context.MODE_PRIVATE);
        return sp.getString(Finals.NAME,"");
    }
    public static void setName(Context context,String name){
        SharedPreferences sp = context.getSharedPreferences(Finals.NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.NAME,name);
        editor.commit();
    }
    public static String getPhotoURLOfUser(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.NAME, Context.MODE_PRIVATE);
        return sp.getString(Finals.PHOTOURLOFUSER,"");
    }
    public static void setPhotoURLOfUser(Context context,String name){
        Log.e("userphoto update",name+"--");
        SharedPreferences sp = context.getSharedPreferences(Finals.NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.PHOTOURLOFUSER,name);
        editor.commit();
    }

    public static int getNoticeConut(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.NOTICE, Context.MODE_PRIVATE);
        return sp.getInt(Finals.NOTICE,0);
    }
    public static void setNoticeCount(Context context,int val){
        SharedPreferences sp = context.getSharedPreferences(Finals.NOTICE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Finals.NOTICE,val);
        editor.commit();
    }

    public static int getComplaintConut(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.COMPLAINT_COUNT, Context.MODE_PRIVATE);
        return sp.getInt(Finals.COMPLAINT_COUNT,0);
    }
    public static void setComplaintCount(Context context, int val){
        SharedPreferences sp = context.getSharedPreferences(Finals.COMPLAINT_COUNT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Finals.COMPLAINT_COUNT,val);
        editor.commit();
    }

    // message drawer count
    public static int getMessageCount(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.MESSAGE_COUNT, Context.MODE_PRIVATE);
        return sp.getInt(Finals.MESSAGE_COUNT,0);
    }
    public static void setMessageCount(Context context, int val){
        SharedPreferences sp = context.getSharedPreferences(Finals.MESSAGE_COUNT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Finals.MESSAGE_COUNT,val);
        editor.commit();
    }

    // message drawer count
    public static int getRainbowCount(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.RAINBOW_COUNT, Context.MODE_PRIVATE);
        return sp.getInt(Finals.RAINBOW_COUNT,0);
    }
    public static void setRainbowCount(Context context, int val){
        SharedPreferences sp = context.getSharedPreferences(Finals.RAINBOW_COUNT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Finals.RAINBOW_COUNT,val);
        editor.commit();
    }


    public static String getVehicleNo_for_add(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.VEHICLENOFORADD, Context.MODE_PRIVATE);
        return sp.getString(Finals.VEHICLENOFORADD,"");

    }
    public static void setVehicleNo_for_add(Context context,String val){
        SharedPreferences sp = context.getSharedPreferences(Finals.VEHICLENOFORADD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.VEHICLENOFORADD,val);
        editor.commit();
    }

    public static String getVehicleNo_for_search(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.VEHICLENOFORSEARCH, Context.MODE_PRIVATE);
        return sp.getString(Finals.VEHICLENOFORSEARCH,"");
    }
    public static void setVehicleNo_for_search(Context context,String val){
        SharedPreferences sp = context.getSharedPreferences(Finals.VEHICLENOFORSEARCH, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.VEHICLENOFORSEARCH,val);
        editor.commit();
    }

    public static String getVehiclePatternNo(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.PATTERN, Context.MODE_PRIVATE);
        return sp.getString(Finals.PATTERN,"");
    }
    public static void setVehiclePatternNo(Context context,String val){
        SharedPreferences sp = context.getSharedPreferences(Finals.PATTERN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.PATTERN,val);
        editor.commit();
    }

    public static String getVehicleFlag(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.VEHICLE_FLAG, Context.MODE_PRIVATE);
        return sp.getString(Finals.VEHICLE_FLAG,"");
    }
    public static void setVehicleFlag(Context context,String val){
        SharedPreferences sp = context.getSharedPreferences(Finals.VEHICLE_FLAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.VEHICLE_FLAG,val);
        editor.commit();
    }

    public static String getDateForVehiclePattern(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.DATE_VEHICLE, Context.MODE_PRIVATE);
        return sp.getString(Finals.DATE_VEHICLE,"");
    }
    public static void setDateForVehiclePattern(Context context,String Sname){
        SharedPreferences sp = context.getSharedPreferences(Finals.DATE_VEHICLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.DATE_VEHICLE,Sname);
        editor.commit();
    }

    public static String getNotificationRingtone(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.NOTIFICATION_TONE, Context.MODE_PRIVATE);
        return sp.getString(Finals.NOTIFICATION_TONE,"");
    }
    public static void setNotificationRingtone(Context context,String notificationTone){
        SharedPreferences sp = context.getSharedPreferences(Finals.NOTIFICATION_TONE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.NOTIFICATION_TONE,notificationTone);
        editor.commit();
    }


    public static String getPhoneNotificationRingtone(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getString(Finals.PHONE_NOTIFICATION_TONE,"");
    }
    public static void setPhoneNotificationRingtone(Context context,String notificationTone){
        SharedPreferences sp = context.getSharedPreferences(Finals.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.PHONE_NOTIFICATION_TONE, notificationTone);
        editor.commit();
    }

    public static String getRingtoneTitle(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.RINGTONE_TITLE, Context.MODE_PRIVATE);
        return sp.getString(Finals.RINGTONE_TITLE,"");
    }
    public static void setRingtoneTitle(Context context,String ringtoneTitle){
        SharedPreferences sp = context.getSharedPreferences(Finals.RINGTONE_TITLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.RINGTONE_TITLE,ringtoneTitle);
        editor.commit();
    }

    public static String getPhoneRingtoneTitle(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getString(Finals.PHONE_RINGTONE_TITLE,"");
    }
    public static void setPhoneRingtoneTitle(Context context,String ringtoneTitle){
        SharedPreferences sp = context.getSharedPreferences(Finals.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.PHONE_RINGTONE_TITLE,ringtoneTitle);
        editor.commit();
    }

    public static boolean getPoll(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.POLL, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.POLL,false);
    }
    public static void setPoll(Context context,Boolean flag){
        SharedPreferences sp = context.getSharedPreferences(Finals.POLL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.POLL,flag);
        editor.commit();
    }

    public static String getUserEmail(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.EMAIL, Context.MODE_PRIVATE);
        return sp.getString(Finals.EMAIL,"");
    }
    public static void setUserEmail(Context context,String email){
        SharedPreferences sp = context.getSharedPreferences(Finals.EMAIL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.EMAIL,email);
        editor.commit();
    }

    public static boolean getLoginByEmail(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.LOGIN_BY_EMAIL, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.LOGIN_BY_EMAIL,false);
    }
    public static void setLoginByEmail(Context context,Boolean value){
        SharedPreferences sp = context.getSharedPreferences(Finals.LOGIN_BY_EMAIL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.LOGIN_BY_EMAIL,value);
        editor.commit();
    }

    public static String getRainbowEmailId(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.RAINBOW_EMAIL, Context.MODE_PRIVATE);
        return sp.getString(Finals.RAINBOW_EMAIL,"");
    }
    public static void setRainbowEmailId(Context context,String email){
        SharedPreferences sp = context.getSharedPreferences(Finals.RAINBOW_EMAIL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.RAINBOW_EMAIL,email);
        editor.commit();
    }

    public static String getRainbowPassword(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.RAINBOW_PASWORD, Context.MODE_PRIVATE);
        return sp.getString(Finals.RAINBOW_PASWORD,"");
    }
    public static void setRainbowPassword(Context context,String password){
        SharedPreferences sp = context.getSharedPreferences(Finals.RAINBOW_PASWORD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.RAINBOW_PASWORD, password);
        editor.commit();
    }

    public static boolean getRainbowLogin(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.RAINBOW_LOGIN, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.RAINBOW_LOGIN,false);
    }
    public static void setRainbowLogin(Context context,Boolean flag){
        SharedPreferences sp = context.getSharedPreferences(Finals.RAINBOW_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.RAINBOW_LOGIN,flag);
        editor.commit();
    }

    public static int getMaxMobileLen(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.MAX_MOBILE_LEN, Context.MODE_PRIVATE);
        return sp.getInt(Finals.MAX_MOBILE_LEN,0);
    }
    public static void setMaxMobileLen(Context context, int val){
        SharedPreferences sp = context.getSharedPreferences(Finals.MAX_MOBILE_LEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Finals.MAX_MOBILE_LEN,val);
        editor.commit();
    }

    public static int getMinMobileLen(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.MIN_MOBILE_LEN, Context.MODE_PRIVATE);
        return sp.getInt(Finals.MIN_MOBILE_LEN,0);
    }
    public static void setMinMobileLen(Context context, int val){
        SharedPreferences sp = context.getSharedPreferences(Finals.MIN_MOBILE_LEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Finals.MIN_MOBILE_LEN,val);
        editor.commit();
    }

    public static boolean getVehicleServicingAccess(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.VEHICLE_SERVICING_ACCESS, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.VEHICLE_SERVICING_ACCESS,false);
    }
    public static void setVehicleServicingAccess(Context context,boolean flag){
        SharedPreferences sp = context.getSharedPreferences(Finals.VEHICLE_SERVICING_ACCESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.VEHICLE_SERVICING_ACCESS,flag);
        editor.commit();
    }

    public static void setInvitationAccess(Context context,boolean flag){
        SharedPreferences sp = context.getSharedPreferences(Finals.INVITATION_ACCESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.INVITATION_ACCESS,flag);
        editor.commit();
    }

    public static boolean getInvitationAccess(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.INVITATION_ACCESS, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.INVITATION_ACCESS,false);
    }

    public static void setPreApprovalsAccess(Context context,boolean flag){
        Log.e("preapproval",flag+"----");
        SharedPreferences sp = context.getSharedPreferences(Finals.PREAPPROVAL_ACCESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.PREAPPROVAL_ACCESS,flag);
        editor.commit();
    }

    public static boolean getPreApprovalsAccess(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.PREAPPROVAL_ACCESS, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.PREAPPROVAL_ACCESS,false);
    }

    public static void setThermalAccess(Context context,boolean flag){
        SharedPreferences sp = context.getSharedPreferences(Finals.THERMAL_ACCESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.THERMAL_ACCESS,flag);
        editor.commit();
    }

    public static boolean getThermalAccess(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.THERMAL_ACCESS, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.THERMAL_ACCESS,false);
    }

    public static void setMyPhoneRingtoneAsNotificationTone(Context context,boolean flag){
        SharedPreferences sp = context.getSharedPreferences(Finals.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.VISITOR_NOTIFICATION_TONE,flag);
        editor.commit();
    }

    public static boolean getMyPhoneRingtoneAsNotificationTone(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.VISITOR_NOTIFICATION_TONE,true);
    }

    public static void setCustomNotificationSound(Context context,boolean flag){
        SharedPreferences sp = context.getSharedPreferences(Finals.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.NOTIFICATION_CUSTOM_TONE,flag);
        editor.commit();
    }

    public static boolean getCustomNotificationSound(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.NOTIFICATION_CUSTOM_TONE,false);
    }


    public static void setNotificationChannelId(Context context, String channelId){
        SharedPreferences sp = context.getSharedPreferences(Finals.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.NOTIFICATION_CHANNEL_ID,channelId);
        editor.commit();
    }

    public static String getNotificationChannelId(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getString(Finals.NOTIFICATION_CHANNEL_ID,"");
    }

    public static int getRainbowAccountCount(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getInt(Finals.RAINBOW_ACCT_COUNT,0);
    }
    public static void setRainbowAccountCount(Context context, int cnt){
        SharedPreferences sp = context.getSharedPreferences(Finals.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Finals.RAINBOW_ACCT_COUNT, cnt);
        editor.commit();
    }


    public static void setNotificationMessageId(Context context, String channelId){
        SharedPreferences sp = context.getSharedPreferences(Finals.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.MESSAGE_ID,channelId);
        editor.commit();
    }

    public static String getNotificationMessageId(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sp.getString(Finals.MESSAGE_ID,"");
    }

    public static boolean getNotificationAccessDialogFlag(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.NOTIFICATION_ACCESS_FLAG, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.NOTIFICATION_ACCESS_FLAG,true);
    }
    public static void setNotificationAccessDialogFlag(Context context,Boolean flag){
        SharedPreferences sp = context.getSharedPreferences(Finals.NOTIFICATION_ACCESS_FLAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.NOTIFICATION_ACCESS_FLAG,flag);
        editor.commit();
    }

    public static boolean getAppBackgroundStatus(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.APP_BACKGROUND, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.APP_BACKGROUND,true);
    }
    public static void setAppBackgroundStatus(Context context, Boolean flag){
        SharedPreferences sp = context.getSharedPreferences(Finals.APP_BACKGROUND, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.APP_BACKGROUND,flag);
        editor.commit();
    }

    public static boolean getSocietyApproval(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.SOCIETY_APPROVAL, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.SOCIETY_APPROVAL,false);
    }
    public static void setSocietyApproval(Context context, Boolean flag){
        Log.e("Society approval",flag+"--");
        SharedPreferences sp = context.getSharedPreferences(Finals.SOCIETY_APPROVAL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.SOCIETY_APPROVAL,flag);
        editor.commit();
    }


    public static boolean getUserManualSeenValue(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.USERMANUAL, Context.MODE_PRIVATE);
        return sp.getBoolean(Finals.ADMIN,false);
    }
    public static void setUserManualSeenValue(Context context,boolean flag){
        SharedPreferences sp = context.getSharedPreferences(Finals.USERMANUAL, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Finals.ADMIN,flag);
        editor.commit();
    }

    //Drawer frequemtly Used beans

    public static String getFrequentlyUsedMenuList(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.ExeOffline, Context.MODE_PRIVATE);
        return sp.getString(Finals.FREQUENTMENU,null);
    }
    public static void setFrequentlyUsedMenuList(Context context, ArrayList<ConfigBean> drawerConfigs){
        String frequentlyusedmenu=new Gson().toJson(drawerConfigs);
        SharedPreferences sp = context.getSharedPreferences(Finals.ExeOffline, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.FREQUENTMENU,frequentlyusedmenu);
        editor.commit();
    }

    public static String getDrawerMenuList(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.NAME, Context.MODE_PRIVATE);
        return sp.getString(Finals.DRAWERMWNULIST,null);
    }
    public static void setDrawerMenuList(Context context,String name){
        SharedPreferences sp = context.getSharedPreferences(Finals.NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Finals.DRAWERMWNULIST,name);
        editor.commit();
    }

    public static int getRandomNumber(Context context){
        SharedPreferences sp = context.getSharedPreferences(Finals.NUMBER, Context.MODE_PRIVATE);
        return sp.getInt(Finals.NUMBER,1);
    }
    public static void setRandomNumber(Context context, int number){
        SharedPreferences sp = context.getSharedPreferences(Finals.NUMBER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Finals.NUMBER, number);
        editor.commit();
    }
}