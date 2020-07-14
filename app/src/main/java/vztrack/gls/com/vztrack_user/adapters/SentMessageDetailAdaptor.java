package vztrack.gls.com.vztrack_user.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.beans.MessageDetailBean;

public class SentMessageDetailAdaptor extends ArrayAdapter<MessageDetailBean> {
    Context context;
    int resource;
    ArrayList<MessageDetailBean> detailList;
    LayoutInflater inflater;
    public SentMessageDetailAdaptor(@NonNull Context context, int resource, @NonNull ArrayList<MessageDetailBean> detailList) {
        super(context, resource, detailList);
        this.context = context;
        this.resource = resource;
        this.detailList = detailList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.sent_msg_detail_row,null);
        ((TextView)view.findViewById(R.id.flatNo)).setText(detailList.get(position).getFlatNo());
        ((TextView)view.findViewById(R.id.readBy)).setText(detailList.get(position).getReadBy());
        if (detailList.get(position).isReadStatus()){
            ((ImageView)view.findViewById(R.id.readStatusTrue)).setVisibility(View.VISIBLE);
        }else {
            ((ImageView)view.findViewById(R.id.readStatusFalse)).setVisibility(View.VISIBLE);
        }
        return  view;
    }
}
