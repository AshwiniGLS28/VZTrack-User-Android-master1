package vztrack.gls.com.vztrack_user.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.beans.TutorialBean;

public class TutorialFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    static TutorialBean tutorialBeanFragment;//=new TutorialBean();


    // newInstance constructor for creating fragment with arguments
    public static TutorialFragment newInstance(TutorialBean tutorialBean) {
        TutorialFragment fragmentFirst = new TutorialFragment();
        Bundle args = new Bundle();
        tutorialBeanFragment=new TutorialBean();
        tutorialBeanFragment=tutorialBean;
//        args.putInt("someInt", page);
//        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tutorial_fragment, container, false);
        TextView tutorialheading = (TextView) view.findViewById(R.id.tutorialheading);
        TextView tutorialsubheading = (TextView) view.findViewById(R.id.tutorialsubheading);
        TextView tutoailimageheading = (TextView) view.findViewById(R.id.tutoailimageheading);
        ImageView tutorialimage =  view.findViewById(R.id.tutorialimage);
        tutorialheading.setText(tutorialBeanFragment.getTutorialheading());
        tutorialsubheading.setText(tutorialBeanFragment.getTutorialsubheading());
        tutoailimageheading.setText(tutorialBeanFragment.getImagheading());
        tutorialimage.setImageDrawable(tutorialBeanFragment.getTutorialimage());
        return view;
    }
}
