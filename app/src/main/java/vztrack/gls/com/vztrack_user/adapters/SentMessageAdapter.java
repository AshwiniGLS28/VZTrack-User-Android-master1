package vztrack.gls.com.vztrack_user.adapters;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.SentMessageDetailActivity;
import vztrack.gls.com.vztrack_user.beans.MessageBean;
import vztrack.gls.com.vztrack_user.fragment.MessageFragment;


public class SentMessageAdapter extends ArrayAdapter<MessageBean> {
    MessageFragment context;
    int resource;
    ArrayList<MessageBean> messages;
    LayoutInflater inflater;
    public SentMessageAdapter(@NonNull MessageFragment context, int resource, @NonNull ArrayList<MessageBean> messages) {
        super(context.getActivity(), resource, messages);
        this.context = context;
        this.resource = resource;
        this.messages = messages;
        inflater = (LayoutInflater) context.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(messages.get(position) != null) {
            RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.sent_msg_row, null);
            ((TextView) view.findViewById(R.id.sentBy)).setText("Sent By : " + messages.get(position).getFlatNo()+" "+messages.get(position).getSentBy());
            ((TextView) view.findViewById(R.id.readBy)).setText("Read By : " + messages.get(position).getReadCount() + "/" + messages.get(position).getTotalCount());
            ((TextView) view.findViewById(R.id.messageText)).setText(messages.get(position).getMessage());
            ((TextView) view.findViewById(R.id.sentDate)).setText(messages.get(position).getSent_date());
            ((TextView) view.findViewById(R.id.sentTo)).setText("Sent To : "+getCommaSeparatedArray(messages.get(position).getGroupName()).toString());
            Button viewBtn = view.findViewById(R.id.viewBtn);
            viewBtn.setId(position);
            viewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = v.getId();
                    Intent detailIntent = new Intent(context.getActivity(), SentMessageDetailActivity.class);
                    detailIntent.putExtra("ID", messages.get(pos).getMessageId());
                    detailIntent.putExtra("MESSAGE_TEXT", messages.get(pos).getMessage());
                    detailIntent.putExtra("MESSAGE_DATE", messages.get(pos).getSent_date());
                    detailIntent.putExtra("SENT_BY",messages.get(pos).getFlatNo()+" "+ messages.get(pos).getSentBy());
                    detailIntent.putExtra("READ_BY", messages.get(position).getReadCount() + "/" + messages.get(position).getTotalCount());
                    detailIntent.putExtra("SENT_TO", getCommaSeparatedArray(messages.get(position).getGroupName()).toString());
                    context.startActivityForResult(detailIntent,100);
                }
            });
            return view;
        } else {
            View view = inflater.inflate(R.layout.load_more_row, null);
            Button button = view.findViewById(R.id.load_more);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.loadMore();
                }
            });
            return  view;
        }
    }

    public String getCommaSeparatedArray(String[] name){
        StringBuilder sb = new StringBuilder();
        for (String n : name) {
            if (sb.length() > 0) sb.append(',');
            sb.append(n);
        }
        return sb.toString();
    }
}
