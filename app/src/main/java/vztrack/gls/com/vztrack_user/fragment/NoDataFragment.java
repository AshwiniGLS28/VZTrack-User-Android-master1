package vztrack.gls.com.vztrack_user.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vztrack.gls.com.vztrack_user.R;

/**
 * Created by Santosh on 19-Dec-17.
 */

public class NoDataFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_no_data, null);
        return root;
    }
}
