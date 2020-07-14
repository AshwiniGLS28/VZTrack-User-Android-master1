package vztrack.gls.com.vztrack_user.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.ContactsActivity;
import vztrack.gls.com.vztrack_user.activity.PreApprovalActivity;
import vztrack.gls.com.vztrack_user.beans.ContactBean;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Santosh on 10/31/2018.
 */

public class ContactAdapter extends ArrayAdapter {
    ContactsActivity activity;
    int resource;
    ArrayList<ContactBean> contacts;
    LayoutInflater inflater;
    int isfrompreapproval=0;
    public ContactAdapter(@NonNull ContactsActivity activity, int resource, @NonNull ArrayList<ContactBean> contacts,int isfrompreapproval) {
        super(activity, resource, contacts);
        this.activity = activity;
        this.resource = resource;
        this.contacts = contacts;
        inflater = activity.getLayoutInflater();
        this.isfrompreapproval=isfrompreapproval;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = inflater.inflate(resource, null);
        ((TextView)row.findViewById(R.id.contactName)).setText(contacts.get(position).getContactName());
        ((TextView)row.findViewById(R.id.contactMobile)).setText(contacts.get(position).getMobileNo());
        CheckBox selected = row.findViewById(R.id.contactSelected);

        row.setTag(position);
        selected.setTag(position);
        selected.setChecked(contacts.get(position).isSelected());
        if (isfrompreapproval==1)
        {
            selected.setVisibility(View.GONE);
            row.setOnClickListener(v -> {
                int tag= (int) row.getTag();
                PreApprovalActivity.contactBean = contacts.get(tag);
                Log.e("conatct adapter contact bean",new Gson().toJson(contacts.get(tag)) +"");
                Log.e("PreApprovalActivity.contactBean",new Gson().toJson( PreApprovalActivity.contactBean)+"");
                activity.setResult(RESULT_OK, null);
                activity.finish();
            });
        }
        selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                Log.e("SELECTED", ((int)v.getTag())+"");
                contacts.get((int)checkBox.getTag()).setSelected(checkBox.isChecked());
                activity.setSelected( contacts.get((int)checkBox.getTag()), checkBox.isChecked());
            }
        });
        return row;
    }
}
