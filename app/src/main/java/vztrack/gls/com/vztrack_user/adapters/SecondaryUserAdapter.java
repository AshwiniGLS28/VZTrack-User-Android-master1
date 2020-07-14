package vztrack.gls.com.vztrack_user.adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.SettingDetails;
import vztrack.gls.com.vztrack_user.beans.SecondaryUserBean;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.GetData;

public class SecondaryUserAdapter extends ArrayAdapter<SecondaryUserBean> {
    private SettingDetails mContext;
    private int resource;
    private ArrayList<SecondaryUserBean> secondaryUserList;
    private final String TAG = "SecondaryUserAdapter";

    public SecondaryUserAdapter(@NonNull SettingDetails settingDetails, int resource, @NonNull ArrayList<SecondaryUserBean> secondaryUserList) {
        super(settingDetails, resource, secondaryUserList);
        this.mContext = settingDetails;
        this.resource = resource;
        this.secondaryUserList = secondaryUserList;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(resource, null);
        ((TextView) v.findViewById(R.id.userHeading)).setText(secondaryUserList.get(position).getName());
        ((TextView) v.findViewById(R.id.userEmail)).setText(secondaryUserList.get(position).getUsername());

        if (secondaryUserList.get(position).isVerified()) {
            ((TextView) v.findViewById(R.id.accountStatus)).setText("Verified");
        } else {
            ((TextView) v.findViewById(R.id.accountStatus)).setText("Not Verified");
        }

        if(secondaryUserList.get(position).isTenant()){
            ((TextView)v.findViewById(R.id.userHeadingLable)).setText("Tenant");
        }else{
            ((TextView)v.findViewById(R.id.userHeadingLable)).setText("Family Member");
        }
        ImageView deleteUser = (ImageView) v.findViewById(R.id.deleteSecondaryUser);
        deleteUser.setId(position);
        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View dialogView = inflater.inflate(R.layout.custum_alert_dialog, null);
                    dialogBuilder.setView(dialogView);
                    TextView txtAlertHeading = dialogView.findViewById(R.id.txtalertheading);
                    TextView txtAlertSubheading = dialogView.findViewById(R.id.txtalertsubheading);

                    TextView btnNegative = dialogView.findViewById(R.id.btnegative);
                    TextView btnPositive = dialogView.findViewById(R.id.btnpositive);

                    txtAlertHeading.setText("Delete Account");
                    txtAlertSubheading.setText("Are you sure you want delete this user account?");

                    btnNegative.setVisibility(View.VISIBLE);

                    btnNegative.setText("Yes");
                    btnPositive.setText("No");

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.setCancelable(false);
                    alertDialog.show();

                    btnNegative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new GetData(mContext, CallFor.DELETE_SECONDARY_USER, "" + secondaryUserList.get(position).getLoginId()).execute();
                            new SettingDetails().getSecondaryUser();
                            alertDialog.dismiss();

                        }
                    });
                    btnPositive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                   Log.e(TAG, "EXCEPTION "+e);
                }
            }
        });
        return v;
    }
}
