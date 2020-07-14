package vztrack.gls.com.vztrack_user.adapters.Preapproval;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.beans.PreApprovalBean.PreApprovalPurpose;

public class CustomAdapterForPreapproval extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    ArrayList<PreApprovalPurpose> preApprovalPurposeBean;
    public CustomAdapterForPreapproval(Context applicationContext,  ArrayList<PreApprovalPurpose> preApprovalPurposeBean) {
        this.context = applicationContext;
        inflter = (LayoutInflater.from(applicationContext));
        this.preApprovalPurposeBean=preApprovalPurposeBean;
        Log.e("preApprovalPurposeBean",new Gson().toJson(preApprovalPurposeBean) +"");
    }

    @Override
    public int getCount() {
        Log.e("size",preApprovalPurposeBean.size()+"");
        return preApprovalPurposeBean.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.preapprovalspinner, null);
//        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.spinnerpreapproval);
        view.setTag(preApprovalPurposeBean.get(i).getPurposeId());
//        icon.setImageResource(flags[i]);
        names.setText(preApprovalPurposeBean.get(i).getPurposeName());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
//        return super.getDropDownView(position, convertView, parent);
        return getCustomView(position, convertView, parent);
    }


    public View getCustomView(int position, View convertView, ViewGroup parent) {
// TODO Auto-generated method stub
        View row=inflter.inflate(R.layout.preapprovalspinnerdropdown, parent, false);
        TextView label=(TextView)row.findViewById(R.id.spinnerpreapprovaldropdown);
        label.setText(preApprovalPurposeBean.get(position).getPurposeName());
        return row;
    }
}
