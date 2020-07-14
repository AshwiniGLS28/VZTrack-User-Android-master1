package vztrack.gls.com.vztrack_user.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.beans.InvitationBean;

/**
 * Created by Santosh on 10/31/2018.
 */

public class InvitationsAdapter extends ArrayAdapter{
    FragmentActivity context;
    int resource;
    ArrayList<InvitationBean> invitations;
    LayoutInflater inflater;
    public InvitationsAdapter(@NonNull FragmentActivity context, int resource, @NonNull ArrayList<InvitationBean> invitations) {
        super(context, resource, invitations);
        this.context = context;
        this.resource = resource;
        this.invitations = invitations;
        inflater = context.getLayoutInflater();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = inflater.inflate(resource, null);
        if(invitations.get(position) == null){
            row.findViewById(R.id.card_view).setVisibility(View.INVISIBLE);
        } else {
            ((TextView)row.findViewById(R.id.invitationType)).setText(invitations.get(position).getPurpose());
            ((TextView) row.findViewById(R.id.invitationDate)).setText(invitations.get(position).getInvitedDate());
            ((TextView)row.findViewById(R.id.guestCount)).setText(invitations.get(position).getGuestCount()+"");
            if(invitations.get(position).getDescription().trim().length() == 0){
                row.findViewById(R.id.addInfo).setVisibility(View.GONE);
            } else {
                ((TextView) row.findViewById(R.id.addInfo)).setText(invitations.get(position).getDescription());
            }
            try {
                if (invitations.get(position).getGuestCount() == 1) {
                    ((TextView) row.findViewById(R.id.invConName)).setText(invitations.get(position).getContactgroup().get(0).getContactName());
                    ((TextView) row.findViewById(R.id.invConMobile)).setText(invitations.get(position).getContactgroup().get(0).getMobileNo());
                } else {
                    row.findViewById(R.id.addContactLL).setVisibility(View.GONE);
                }
            } catch (Exception e){
                row.findViewById(R.id.addContactLL).setVisibility(View.GONE);
            }
        }
        return row;
    }
}
