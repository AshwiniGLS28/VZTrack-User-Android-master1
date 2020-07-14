//package vztrack.gls.com.vztrack_user.activity;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentPagerAdapter;
//import androidx.viewpager.widget.ViewPager;
//
//import android.os.Bundle;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import com.google.android.material.tabs.TabLayout;
//
//import java.util.ArrayList;
//
//import vztrack.gls.com.vztrack_user.R;
//import vztrack.gls.com.vztrack_user.beans.TutorialBean;
//import vztrack.gls.com.vztrack_user.fragment.TutorialFragment;
//
//public class TutorialActivity extends AppCompatActivity {
//    ViewPager mImageViewPager;
//    LinearLayout sliderDotspanel;
//    int  dotscount = 5;
//    private ImageView[] dots;
//    ArrayList<TutorialBean> tutorialBeans=new ArrayList<>();
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tutorial);
//        mImageViewPager = (ViewPager) findViewById(R.id.pager);
////        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
//
//        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
////        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabDots);
////        tabLayout.setupWithViewPager(mImageViewPager, true);
//
//        TutorialBean tutorialBean=new TutorialBean();
//        tutorialBean.setTutorialheading("Getting started");
//        tutorialBean.setTutorialsubheading("Make your society a vibrant community with VZTrack");
//        tutorialBean.setImagheading("Guard makes an entry at right gate");
//        tutorialBean.setTutorialimage(getResources().getDrawable(R.drawable.ic_gate));
//        tutorialBeans.add(tutorialBean);
//
//        TutorialBean tutorialBean1=new TutorialBean();
//        tutorialBean1.setTutorialheading("Call-Based Notifications");
//        tutorialBean1.setTutorialsubheading("Get Call-Based Notifications Of Your Visitors Along With Their Details");
//        tutorialBean1.setImagheading("Get Call-Based Notifications Of Your Visitors");
//        tutorialBean1.setTutorialimage(getResources().getDrawable(R.drawable.ic_callbasenotification));
//        tutorialBeans.add(tutorialBean1);
//
//
//        TutorialBean tutorialBean2=new TutorialBean();
//        tutorialBean2.setTutorialheading("Features");
//        tutorialBean2.setTutorialsubheading("Swipe right to see the huge list of features!");
//        tutorialBean2.setImagheading("Explore more features");
//        tutorialBean2.setTutorialimage(getResources().getDrawable(R.drawable.ic_swipe_svg));
//        tutorialBeans.add(tutorialBean2);
//
//
//        TutorialBean tutorialBean3=new TutorialBean();
//        tutorialBean3.setTutorialheading("How To Use");
//        tutorialBean3.setTutorialsubheading("Click here to check out our user manual in the menu to learn how to use the app successfully!");
//        tutorialBean3.setImagheading(" check out our user manual ");
//        tutorialBean3.setTutorialimage(getResources().getDrawable(R.drawable.ic_manual_svg));
//        tutorialBeans.add(tutorialBean3);
//
//        TutorialBean tutorialBean4=new TutorialBean();
//        tutorialBean4.setTutorialheading("How To Use");
//        tutorialBean4.setTutorialsubheading("Click here to check out our user manual in the menu to learn how to use the app successfully!");
//        tutorialBean4.setImagheading(" check out our user manual ");
//        tutorialBean4.setTutorialimage(getResources().getDrawable(R.drawable.ic_manual_svg));
//        tutorialBeans.add(tutorialBean4);
//
//
//
//        MyPagerAdapter adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
//        mImageViewPager.setAdapter(adapterViewPager);
//
//
//
//        dots = new ImageView[dotscount];
//
//        for(int i = 0; i < dotscount; i++){
//
//            dots[i] = new ImageView(this);
//            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_indicator_default));
//
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//            params.setMargins(8, 0, 8, 0);
//
//            sliderDotspanel.addView(dots[i], params);
//
//        }
//
//        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_indicator_selected));
//
//        mImageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//                for(int i = 0; i< dotscount; i++){
//                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_indicator_default));
//                }
//
//                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_indicator_selected));
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//
//    }
//
//
//
//
//
//    public class MyPagerAdapter extends FragmentPagerAdapter {
//        private int NUM_ITEMS = tutorialBeans.size();
//
//        public MyPagerAdapter(FragmentManager fragmentManager) {
//            super(fragmentManager);
//        }
//
//        // Returns total number of pages
//        @Override
//        public int getCount() {
//            return NUM_ITEMS;
//        }
//
//        // Returns the fragment to display for that page
//        @Override
//        public Fragment getItem(int position) {
////            switch (position) {
////                case 0: // Fragment # 0 - This will show FirstFragment
//            TutorialBean tutorialBean=tutorialBeans.get(position);
//                    return TutorialFragment.newInstance(tutorialBean);
////                case 1: // Fragment # 0 - This will show FirstFragment different title
////                    return FirstFragment.newInstance(1, "Page # 2");
////                case 2: // Fragment # 1 - This will show SecondFragment
////                    return SecondFragment.newInstance(2, "Page # 3");
////                default:
////                    return null;
//            }
//
//
//        // Returns the page title for the top indicator
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return "";
//        }
//
//    }
//
//}
