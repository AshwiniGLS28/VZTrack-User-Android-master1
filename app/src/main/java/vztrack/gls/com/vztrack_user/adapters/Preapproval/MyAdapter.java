package vztrack.gls.com.vztrack_user.adapters.Preapproval;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.beans.PreApprovalBean.PreApprovalPurpose;

public class MyAdapter extends ArrayAdapter<String> {

    ArrayList<String> animalList = new ArrayList<>();
    LayoutInflater inflater;
    public MyAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
        super(context, textViewResourceId, objects);
        animalList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.preapprovalspinner, null);
        v.setTag(animalList.get(position));
        TextView textView = (TextView) v.findViewById(R.id.spinnerpreapproval);
        textView.setText(animalList.get(position));
        Typeface font = Typeface.createFromAsset(getContext().getAssets(),
                "roboto_medium.ttf");
        textView.setTypeface(font);
        return v;

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
//        return super.getDropDownView(position, convertView, parent);
        return getCustomView(position, convertView, parent);
    }


    public View getCustomView(int position, View convertView, ViewGroup parent) {
// TODO Auto-generated method stub
        View row = inflater.inflate(R.layout.preapprovalspinnerdropdown, parent, false);
        TextView label = (TextView) row.findViewById(R.id.spinnerpreapprovaldropdown);
        label.setText(animalList.get(position));
        return row;
    }
}