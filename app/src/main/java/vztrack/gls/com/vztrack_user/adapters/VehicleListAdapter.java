package vztrack.gls.com.vztrack_user.adapters;

/**
 * Created by sandeep on 14/3/16.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.profile.VehicleBean;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.GetData;

public class VehicleListAdapter extends ArrayAdapter<VehicleBean> implements View.OnClickListener{

    private ArrayList<VehicleBean> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView tvVehicleNumber,txtparkingType,txtparkingNo,txtparkinglevel,txtparkingstickerNo;;
        ImageView tvVehicleType;
        ImageView imgDelete;
    }

    public VehicleListAdapter(ArrayList<VehicleBean> data, Context context) {
        super(context, R.layout.vehicles_list, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        VehicleBean dataModel=(VehicleBean)object;
        switch (v.getId())
        {
            case R.id.imgDelete:
                break;
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final VehicleBean dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            convertView = inflater.inflate(R.layout.vehicles_list, parent, false);

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicles_list, parent, false);
            viewHolder.tvVehicleNumber = (TextView) convertView.findViewById(R.id.vehicleNumber);
            viewHolder.tvVehicleType = (ImageView) convertView.findViewById(R.id.vehicleType);
            viewHolder.imgDelete = (ImageView) convertView.findViewById(R.id.imgDelete);

            viewHolder.txtparkingstickerNo=convertView.findViewById(R.id.txtparkingstickerNo);
            viewHolder.txtparkinglevel=convertView.findViewById(R.id.txtparkinglevel);
            viewHolder.txtparkingNo=convertView.findViewById(R.id.txtparkingNo);
            viewHolder.txtparkingType=convertView.findViewById(R.id.txtparkingType);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Log.e("dataModel",new Gson().toJson(dataModel)+"-----");
        if (dataModel!=null) {
            viewHolder.tvVehicleNumber.setText(dataModel.getVehicleNumber());
            if (dataModel.getParkingLevel()!=null) {
                if (!dataModel.getParkingLevel().equalsIgnoreCase("not added"))
                viewHolder.txtparkinglevel.setText("Parking Level : " + dataModel.getParkingLevel());
                else
                    viewHolder.txtparkinglevel.setText("Parking Level : NA" );

            }else
                viewHolder.txtparkinglevel.setVisibility(View.GONE);

            if (dataModel.getParkingStickerNo()!=null) {
                if (!dataModel.getParkingStickerNo().equalsIgnoreCase("not added"))
                viewHolder.txtparkingstickerNo.setText("Parking Sticker No. : " + dataModel.getParkingStickerNo());
                else
                    viewHolder.txtparkingstickerNo.setText("Parking Sticker No. : NA" );
            }
            else
                viewHolder.txtparkingstickerNo.setVisibility(View.GONE);

            if (dataModel.getParkingNo()!=null) {
                if (!dataModel.getParkingNo().equalsIgnoreCase("not added"))
                viewHolder.txtparkingNo.setText("Parking No. : " + dataModel.getParkingNo());
                else
                    viewHolder.txtparkingNo.setText("Parking No. : NA ");
            }
            else
                viewHolder.txtparkingNo.setVisibility(View.GONE);

            if (dataModel.getParkingType()!=null) {
                if (!dataModel.getParkingType().equalsIgnoreCase("not added"))
                viewHolder.txtparkingType.setText("Parking Type : " + dataModel.getParkingType());
                else
                    viewHolder.txtparkingType.setText("Parking Type : NA ");
            }else
                viewHolder.txtparkingType.setVisibility(View.GONE);
            if (dataModel.getVehicleType().trim().equalsIgnoreCase("2")) {
                viewHolder.tvVehicleType.setImageDrawable(mContext.getResources().getDrawable(R.drawable.img_twowheeler));
            } else {
                viewHolder.tvVehicleType.setImageDrawable(mContext.getResources().getDrawable(R.drawable.img_fourwheeler));
            }
        }

        viewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String param = "?vehicleNumber="+dataModel.getVehicleNumber().replaceAll("\\s+","%20");
                new GetData(MainActivity.mainActivity, CallFor.DELETE_VEHICLE,""+param).execute();
            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
}