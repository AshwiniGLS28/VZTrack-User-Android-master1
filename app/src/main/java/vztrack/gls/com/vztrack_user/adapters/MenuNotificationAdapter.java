package vztrack.gls.com.vztrack_user.adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.SettingDetails;
import vztrack.gls.com.vztrack_user.beans.NotificationMenuBeans;

public class MenuNotificationAdapter extends ArrayAdapter<NotificationMenuBeans> {
    SettingDetails context;
    int resource;
    ArrayList<NotificationMenuBeans> menuNotificationBean;
    public static CheckBox access;

    public MenuNotificationAdapter(@NonNull SettingDetails context, int resource, @NonNull ArrayList<NotificationMenuBeans> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.menuNotificationBean = objects;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(resource, null);
        ((TextView) v.findViewById(R.id.menuName)).setText(menuNotificationBean.get(position).getName());
        access = (CheckBox) v.findViewById(R.id.notificationCheck);
        access.setChecked(menuNotificationBean.get(position).isAccess());
        access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuNotificationBean.get(position).isAccess())
                    menuNotificationBean.get(position).setAccess(false);
                else
                    menuNotificationBean.get(position).setAccess(true);
            }
        });
        return v;
    }

}
