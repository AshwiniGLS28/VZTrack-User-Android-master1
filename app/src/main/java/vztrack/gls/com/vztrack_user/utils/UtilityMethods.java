package vztrack.gls.com.vztrack_user.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.beans.DrawerConfigBean.DrawerConfigParent;
import vztrack.gls.com.vztrack_user.beans.UserBean;
import vztrack.gls.com.vztrack_user.profile.FamilyBean;
import android.media.ExifInterface;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.google.gson.Gson;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

/**
 * Created by sandeep on 16/3/16.
 */
public class UtilityMethods {

//    dd MMM yyyy - 02 Jun 2018
//    G	Era designator	AD
//    y	Year	2018(yyyy),18(yy)
//    M	Month in year	July(MMMM), Jul(MMM), 07(MM)
//    w	Results in week in year	16
//    W	Results in week in month	3
//    D	Gives the day count in the year	266
//    d	Day of the month	09(dd), 9(d)
//    F	Day of the week in month	4
//    E	Day name in the week	Tuesday, Tue
//    u	Day number of week where 1 represents Monday, 2 represents Tuesday and so on	2
//    a	AM or PM marker	AM
//    H	Hour in the day (0-23)	12
//    k	Hour in the day (1-24)	23
//    K	Hour in am/pm for 12 hour format (0-11)	0
//    h	Hour in am/pm for 12 hour format (1-12)	12
//    m	Minute in the hour	59
//    s	Second in the minute	35
//    S	Millisecond in the minute	978
//    z	Timezone	Pacific Standard Time; PST; GMT-08:00
//    Z	Timezone offset in hours (RFC pattern)	-0800
//    X	Timezone offset in ISO format	-08; -0800; -08:00

//    MM/dd/yyyy	01/02/2018
//    dd-M-yyyy hh:mm:ss	02-1-2018 06:07:59
//    dd MMMM yyyy	02 January 2018
//    dd MMMM yyyy zzzz	02 January 2018 India Standard Time
//    E, dd MMM yyyy HH:mm:ss z	Tue, 02 Jan 2018 18:07:59 IST


    public static Bitmap imagePathToBitmap(String path){
        Bitmap bitmap = null;
        File imgFile = new  File(path);
        if(imgFile.exists()){
            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return bitmap;
    }

    /**
     * reduces the size of the image
     * @param image
     * @param maxSize
     * @return
     */
    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 50, bao);
        byte[] ba = bao.toByteArray();
        return Base64.encodeToString(ba, Base64.DEFAULT);
    }

    public static String formatDate(Date date, String pattern){
        String strDate = null;
        SimpleDateFormat dt1 = new SimpleDateFormat( pattern, Locale.ENGLISH );
        strDate = dt1.format(date);
        return strDate;
    }


    public static Date DateToDate(Date date, String pattern){
        Date formatedDate = null;
        try {
            SimpleDateFormat sm = new SimpleDateFormat(pattern, Locale.ENGLISH);
            // myDate is the java.util.Date in yyyy-mm-dd format
            // Converting it into String using formatter
            String strDate = sm.format(date);
            //Converting the String back to java.util.Date
            formatedDate = sm.parse(strDate);
        } catch (ParseException e) {
            Log.e("Date Parse Excp",""+e);
        }
        return  formatedDate;
    }

    public static String ChangeDateFormat(String strDate, String sourceFormat, String destinationFormat){
        String dateString = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(sourceFormat);
        Date date;
        try {
            date = dateFormat.parse(strDate);
            dateFormat = new SimpleDateFormat(destinationFormat);
            dateString = dateFormat.format(date);
            Log.d("Date", dateString);

        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return dateString;
    }


    public static int compareTwoDates(String strDate1, String strDate2, String dateFormat1, String dateFormat2){
        int dateCompareFlag = 0;
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat1);
            SimpleDateFormat sdf2 = new SimpleDateFormat(dateFormat2);
            Date date1 = sdf1.parse(strDate1);
            Date date2 = sdf2.parse(strDate2);

            if (date1.after(date2)) {
                //System.out.println("Date1 is after Date2");
                dateCompareFlag = 1;
            }
            if (date1.before(date2)) {
                //System.out.println("Date1 is before Date2");
                dateCompareFlag = -1;
            }

            if (date1.equals(date2)) {
                //System.out.println("Date1 is equal Date2");
                dateCompareFlag = 0;
            }
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return dateCompareFlag;
    }

    public static boolean validateEmail(String strEmailId){
        boolean res = false;
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(strEmailId);
        res = matcher.find();
        return res;
    }



    public static String getFileSizeMegaBytes(File file) {
        return (double) file.length() / (1024 * 1024) + " mb";
    }

    public static String getFileSizeKiloBytes(File file) {
        return (double) file.length() / 1024 + "  kb";
    }

    public static String getFileSizeBytes(File file) {
        return file.length() + " bytes";
    }


    /*
     * Convert Given String to Camel Case i.e.
     * Capitalize first letter of every word to upper case
     */
    public static String camelCase(String str)
    {
        StringBuilder builder = new StringBuilder(str);
        // Flag to keep track if last visited character is a
        // white space or not
        boolean isLastSpace = true;

        // Iterate String from beginning to end.
        for(int i = 0; i < builder.length(); i++)
        {
            char ch = builder.charAt(i);

            if(isLastSpace && ch >= 'a' && ch <='z')
            {
                // Character need to be converted to uppercase
                builder.setCharAt(i, (char)(ch + ('A' - 'a') ));
                isLastSpace = false;
            }
            else if (ch != ' ')
                isLastSpace = false;
            else
                isLastSpace = true;
        }

        return builder.toString();
    }


    public static int getRandomColor() {
        Random random = new Random();
        int colorCode = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        return colorCode;
    }

    public static boolean between(int i, int minValueInclusive, int maxValueInclusive) {
        if (i >= minValueInclusive && i <= maxValueInclusive)
            return true;
        else
            return false;
    }

    public static String getFlatNumberFromEmail(String email) {
        String flatNumber = "";
        ArrayList<FamilyBean> rainbowUsers = MainActivity.rainbowUsers;
        if (rainbowUsers != null) {
            try {
                for (int i = 0; i < rainbowUsers.size(); i++) {
                    if (rainbowUsers.get(i).getRainbowEmailId().equals(email)) {
                        flatNumber = rainbowUsers.get(i).getFlatNo();
                        break;
                    }
                }
            } catch (Exception ex) {
                Log.e("Exception ", " " + ex);
            }
        }
        if(!flatNumber.equals("")){
            flatNumber = " ("+ flatNumber+")";
        }
        return flatNumber;
    }

    public static String getUserProfilePhoto(Context context){
        return SheredPref.getPhotoURLOfUser(context) + "?randomNumber=" +SheredPref.getRandomNumber(context);
    }

    public static Bitmap rotateImageIfRequired(Context context, Bitmap bitmap, String photoPath) throws IOException {

        ExifInterface ei = new ExifInterface(photoPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch(orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }
        return rotatedBitmap;
    }

    public static String getTitleFromMenuJson(String menuName,Context context)
    {

        String title=null;
        ArrayList<DrawerConfigParent> drawerConfigParents = new Gson().fromJson(SheredPref.getDrawerMenuList(context), new TypeToken<ArrayList<DrawerConfigParent>>() {
        }.getType());
        if (drawerConfigParents!=null)
        {
            for (int i=1;i<drawerConfigParents.size();i++ )
            {
                for (int j=0;j<drawerConfigParents.get(i).getContents().size();j++)
                {
                    if (menuName.equalsIgnoreCase(drawerConfigParents.get(i).getContents().get(j).getName()))
                    {
                        title=drawerConfigParents.get(i).getContents().get(j).getTitle();
                    }
                }
            }
        }
        return title;
    }

    public static String getPostData(Context context) {
        String data = "data="+PrepareUserBean(context);
        return data;
    }

    public static String PrepareUserBean(Context context) {
        UserBean userBean = new UserBean();
        userBean.setUser_name(SheredPref.getUsername(context));
        userBean.setUser_password(SheredPref.getPassword(context));
        userBean.setSocityId(Integer.parseInt(SheredPref.getSocietyId(context)));
        return new Gson().toJson(userBean);
    }

    public static int getVersionCode(Context ctx) {
        int versionCode = 0;
        try {
            PackageInfo pInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            versionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

}
