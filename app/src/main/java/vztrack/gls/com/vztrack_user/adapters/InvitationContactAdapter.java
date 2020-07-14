package vztrack.gls.com.vztrack_user.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.InvitationActivity;
import vztrack.gls.com.vztrack_user.beans.ContactBean;

/**
 * Created by Santosh on 10/31/2018.
 */

public class InvitationContactAdapter extends ArrayAdapter {
    InvitationActivity activity;
    int resource;
    ArrayList<ContactBean> contacts;
    LayoutInflater inflater;
    public InvitationContactAdapter(@NonNull InvitationActivity activity, int resource, @NonNull ArrayList<ContactBean> contacts) {
        super(activity, resource, contacts);
        this.activity = activity;
        this.resource = resource;
        this.contacts = contacts;
        inflater = activity.getLayoutInflater();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = inflater.inflate(resource, null);
        ((TextView)row.findViewById(R.id.contactName)).setText(contacts.get(position).getContactName());
        ((TextView)row.findViewById(R.id.contactMobile)).setText(contacts.get(position).getMobileNo());
        Button selected = row.findViewById(R.id.deleteContact);
        if(contacts.get(position).getOtp()!=null && !contacts.get(position).getOtp().equals("")){
            ((TextView)row.findViewById(R.id.invOtp)).setText(""+contacts.get(position).getOtp());
            selected.setVisibility(View.GONE);
        } else {
            ((TextView)row.findViewById(R.id.invOtp)).setVisibility(View.GONE);
            ((TextView)row.findViewById(R.id.entryCodeLabel)).setVisibility(View.GONE);
            selected.setTag(position);
            selected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contacts.remove((int) v.getTag());
                    activity.dataChanged();
                }
            });
        }
        return row;
    }
}
