package vztrack.gls.com.vztrack_user.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Calendar;

import vztrack.gls.com.vztrack_user.BuildConfig;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.utils.CleverTapRegisterEvents;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.LoadImage;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

/**
 * Created by  on 19-Dec-17.
 */

public class VehicleServicing extends Fragment {

    private ImageView imageView;
    private LinearLayout progressLayout;
    private RelativeLayout bookNowButtonLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_vehicle_servicing, null);
        Button btnBook = root.findViewById(R.id.btnBook);
        imageView = root.findViewById(R.id.image);
        progressLayout = root.findViewById(R.id.progressLayout);
        bookNowButtonLayout = root.findViewById(R.id.controls);
        String title= UtilityMethods.getTitleFromMenuJson(getResources().getString(R.string.vehicleservice),getActivity());
        Log.e("titlevisitor",title+"");
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        new LoadImage(this).loadImage(getActivity(), R.drawable.image_background, BuildConfig.GARAGE_WORK_IMAGE +"?day="+day, imageView, null);

        btnBook.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                CleverTapRegisterEvents.addCleverTapEvent(getActivity(), Events.event_vehicle_servicing_call);
                String phoneNumber = getResources().getString(R.string.garage_work_mobile_number);
                UtilityMethodsAndroid.makeCall(getActivity(), phoneNumber);
            }
        });
        setHasOptionsMenu(true);
        return root;
    }

    public void manageView(){
        imageView.setVisibility(View.VISIBLE);
        bookNowButtonLayout.setVisibility(View.VISIBLE);
        progressLayout.setVisibility(View.GONE);
        imageView.setBackgroundColor(R.drawable.no_photo_icon);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }
}
