package vztrack.gls.com.vztrack_user.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import vztrack.gls.com.vztrack_user.utils.ExceptionHandler;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

public abstract class BaseActivity extends AppCompatActivity {
    public abstract void onGetResponse(String response, String callFor);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
    }

    @Override
    protected void onResume() {
        SheredPref.setAppBackgroundStatus(this, false);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        SheredPref.setAppBackgroundStatus(this, true);
        super.onDestroy();
    }


}
