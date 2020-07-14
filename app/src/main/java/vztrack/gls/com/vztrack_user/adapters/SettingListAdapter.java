package vztrack.gls.com.vztrack_user.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.AddSecondaryUser;
import vztrack.gls.com.vztrack_user.activity.LoginScreenActivity;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.activity.SettingDetails;
import vztrack.gls.com.vztrack_user.profile.FamilyBean;
import vztrack.gls.com.vztrack_user.profile.UserBean;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.PostData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

import static vztrack.gls.com.vztrack_user.activity.MainActivity.mainActivity;

public class SettingListAdapter extends BaseAdapter {

    private Context mContext;
    private String[]  Title;
    private int[] imge;
    CheckConnection cc;
    boolean type;


    public SettingListAdapter(Context context, String[] title, int[] imageIds) {
        mContext = context;
        Title = title;
        imge = imageIds;

    }

    public int getCount() {
        // TODO Auto-generated method stub
        return Title.length;
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        cc = new CheckConnection(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View row, view;
        row = inflater.inflate(R.layout.settings_menu_item, parent, false);
        TextView title;
        ImageView icon;
        LinearLayout mainLinearLayout;
        icon = (ImageView) row.findViewById(R.id.imgMenuIcon);
        title = (TextView) row.findViewById(R.id.txtMenuTitle);
        view = (View) row.findViewById(R.id.view);
        mainLinearLayout = (LinearLayout) row.findViewById(R.id.mainLinearLayout);
        title.setText(Title[position]);
        icon.setImageResource(imge[position]);

        if(position==Title.length-1){
            view.setVisibility(View.GONE);
        }
        mainLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cc.isConnectingToInternet()) {
                    Intent I = new Intent(mContext, SettingDetails.class);
                    if(SheredPref.getType(mContext) == true){
                        switch (position) {
                            case 0:
                                I.putExtra("FLAG", "PROF");
                                mContext.startActivity(I);

                                break;

                            case 1:
                                I.putExtra("FLAG", "PASS");
                                mContext.startActivity(I);

                                break;

                            case 2:
                                I.putExtra("FLAG", "NOTI");
                                mContext.startActivity(I);

                                break;
                            case 3:
                                callLogout();
                                break;
                        }
                    }else{
                        if (MainActivity.isPrimaryUser == true) {
                            switch (position){
                                case 0:
                                    I.putExtra("FLAG", "PROF");
                                    mContext.startActivity(I);

                                    break;

                                case 1:
                                    I.putExtra("FLAG", "Mana");
                                    mContext.startActivity(I);

                                    break;

                                case 2:
                                    I.putExtra("FLAG", "PASS");
                                    mContext.startActivity(I);

                                    break;

                                case 3:
                                    I.putExtra("FLAG", "NOTI");
                                    mContext.startActivity(I);

                                    break;
                                case 4:
                                    callLogout();
                                    break;

                            }
                        }else {
                            switch (position) {
                                case 0:
                                    I.putExtra("FLAG", "PROF");
                                    mContext.startActivity(I);

                                    break;

                                case 1:
                                    I.putExtra("FLAG", "PASS");
                                    mContext.startActivity(I);

                                    break;

                                case 2:
                                    I.putExtra("FLAG", "NOTI");
                                    mContext.startActivity(I);

                                    break;
                                case 3:
                                    callLogout();
                                    break;
                            }
                        }
                    }
                }else{
                    CommonMethods.showToastError(mContext, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        return (row);
    }

    private void callLogout() {

//        outputBeanSearchProviders.clear();
        if (cc.isConnectingToInternet()) {


            //New Pop up alert
            android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(mContext);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View dialogView = inflater.inflate(R.layout.custum_alert_dialog, null);
            dialogBuilder.setView(dialogView);
            TextView txtalertheading = dialogView.findViewById(R.id.txtalertheading);
            TextView txtalertsubheading = dialogView.findViewById(R.id.txtalertsubheading);

            TextView btnegative = dialogView.findViewById(R.id.btnegative);
            TextView btnpositive = dialogView.findViewById(R.id.btnpositive);

            txtalertheading.setText("Logout");
            txtalertsubheading.setText("Are you sure you want to logout?");

            btnegative.setVisibility(View.VISIBLE);


            final android.app.AlertDialog b = dialogBuilder.create();
            b.setCanceledOnTouchOutside(false);
            b.setCancelable(false);
            b.show();

            btnpositive.setText("Yes");
            btnegative.setText("No");
            btnpositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    b.dismiss();
                    if (cc.isConnectingToInternet()) {

                        String device_id = Settings.Secure.getString(mainActivity.getContentResolver(), Settings.Secure.ANDROID_ID);
                        UserBean userBean = new UserBean();
                        userBean.setUser_dev_id(device_id);
                        new PostData(new Gson().toJson(userBean), MainActivity.mainActivity, CallFor.LOGOUT).execute();
                        LoginScreenActivity.splashFlag = false;
                    }
                    else{
                        CommonMethods.showToastError(mContext, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                    }
                }
            });
            btnegative.setOnClickListener(v -> b.dismiss());
            //end of alert

//            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(mContext, R.style.AppCompatAlertDialogStyle);
//        builder.setMessage("Are you sure? If you logout you will not receive visitor notification");
//        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//
//                String device_id = Settings.Secure.getString(mainActivity.getContentResolver(), Settings.Secure.ANDROID_ID);
//                UserBean userBean = new UserBean();
//                userBean.setUser_dev_id(device_id);
//                new PostData(new Gson().toJson(userBean), MainActivity.mainActivity, CallFor.LOGOUT).execute();
//                LoginScreenActivity.splashFlag = false;
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        builder.show();
    } else {
        CommonMethods.showToastSuccess(mContext, "Unable to logout, Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
    }
    }
}