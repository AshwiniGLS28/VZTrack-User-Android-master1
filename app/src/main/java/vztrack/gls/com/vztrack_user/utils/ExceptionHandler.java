package vztrack.gls.com.vztrack_user.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionHandler implements java.lang.Thread.UncaughtExceptionHandler {
    Context context;
    public ExceptionHandler(Activity context) {
        context = context;
    }
    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {

        String exceptionAsString = getPrintStackTraceInString(e);
        exceptionAsString = "\nEXCEPTION --:-- "+exceptionAsString;
        CrashLogs.writeInLogFile(exceptionAsString);
    }

    private String getPrintStackTraceInString(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
