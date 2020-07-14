package vztrack.gls.com.vztrack_user.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import vztrack.gls.com.vztrack_user.BuildConfig;

public class CrashLogs  {
    static String APP_NAME = "VZTrack";
    static String FILE_NAME = "/Logs";
    static String FILE_EXTENSION = ".txt";
    static String OLD_FILE_NAME = "/OldLogs";
    private static final boolean DEBUG = BuildConfig.DEBUG;
    private static final String CLASSTAG = "VZTrack";
    public static void writeInLogFile( String log) {
        Log.e("log",log+"");
        if (DEBUG) {
            try {
                File root = new File(Environment.getExternalStorageDirectory(), "vzt_log");
                if (!root.exists()) {
                    root.mkdirs();
                }
                File gpxfile = new File(root, FILE_NAME + FILE_EXTENSION);
                File oldfile = new File(root, OLD_FILE_NAME + FILE_EXTENSION);
                // Get length of file in bytes
                long fileSizeInBytes = gpxfile.length();
                // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                long fileSizeInKB = fileSizeInBytes / 1024;
                // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                long fileSizeInMB = fileSizeInKB / 1024;
//                Log.e("fileSizeInMB",fileSizeInMB+"");
                Log.e("fileSizeInKB",fileSizeInKB+"----");
                Log.e("fileSizeInMB",fileSizeInMB+"----");
                if(fileSizeInKB>=2048)
                {
                    Log.e("create","new file");
                    gpxfile.renameTo(oldfile);
                    gpxfile = new File(root, FILE_NAME + FILE_EXTENSION);
                }
                FileWriter writer = new FileWriter(gpxfile, true);
                String format = getCurrentDateAndTime("dd-MM-yyyy HH:mm:ss-: ") +":"+ log;
                writer.append(format + "\n\n");
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static String getCurrentDateAndTime(String format) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(calendar.getTime());
    }
    /**
     * print error message
     */
    public static void e(String msg) {
        if (DEBUG) {
            if (msg.length() > 4000) {
                Log.e(CLASSTAG, msg);
                e(msg.substring(4000));
            } else {
                Log.e(CLASSTAG, msg);
            }
        }
    }
    /**
     * print debug message
     */
    public static void d(String msg) {
        if (msg.length() > 4000) {
            Log.d(CLASSTAG, msg);
            d(msg.substring(4000));
        } else {
            Log.d(CLASSTAG, msg);
        }
    }
    /**
     * print warning message
     */
    public static void w(String msg) {
        if (DEBUG) {
            if (msg.length() > 4000) {
                Log.w(CLASSTAG, msg);
                w(msg.substring(4000));
            } else {
                Log.w(CLASSTAG, msg);
            }
        }
    }
    /**
     * print info message
     */
    public static void i(String msg) {
        if (DEBUG) {
            if (msg.length() > 4000) {
                Log.i(CLASSTAG, msg);
                i(msg.substring(4000));
            } else {
                Log.i(CLASSTAG, msg);
            }
        }
    }
}
