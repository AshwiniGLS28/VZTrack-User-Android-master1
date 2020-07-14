package vztrack.gls.com.vztrack_user.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;

public class NoInternetConnection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_connection);
        getSupportActionBar().hide();
        CleverTap.cleverTap_Record_Event(this,"No Internet Connection Activity");
    }

    public void Refresh(View v)
    {

        CheckConnection cc = new CheckConnection(getApplicationContext());

        Boolean isConnectingToInternet = cc.isConnectingToInternet();

        if(isConnectingToInternet)
        {
            CheckConnection ccAccess = new CheckConnection(getApplicationContext());

            Boolean isInternetAvailable = ccAccess.isConnectingToInternet();

            if(isInternetAvailable)
            {
                Intent ConnCheck = new Intent(this,MainActivity.class);
                ConnCheck.addCategory(Intent.CATEGORY_HOME);
                ConnCheck.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(ConnCheck);
            }
        }
    }

    public void Close(View v)
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(NoInternetConnection.this);
        alert.setMessage("Are you sure exit from application?");
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        alert.show();

    }
}
