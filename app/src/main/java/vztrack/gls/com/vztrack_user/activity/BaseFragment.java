package vztrack.gls.com.vztrack_user.activity;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {
    public abstract void onGetResponse(String response, String callFor);

}
