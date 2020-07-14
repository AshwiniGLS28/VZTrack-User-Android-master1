package vztrack.gls.com.vztrack_user.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vztrack.gls.com.vztrack_user.R;

/**
 * Created by Santosh on 19-Dec-17.
 */

public class NoInternetFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_no_internet, null);
        return root;
    }
}
