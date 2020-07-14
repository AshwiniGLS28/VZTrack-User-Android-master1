package vztrack.gls.com.vztrack_user.activity;

import androidx.appcompat.app.AppCompatActivity;
 import androidx.core.content.ContextCompat;
 import androidx.viewpager.widget.ViewPager;

 import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
 import android.widget.LinearLayout;

 import vztrack.gls.com.vztrack_user.R;
 import vztrack.gls.com.vztrack_user.adapters.ViewPagerAdapter;
import vztrack.gls.com.vztrack_user.adapters.ViewPagerAdapterUserManual;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

public class UserManualActivity extends AppCompatActivity {
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manual);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

        ViewPagerAdapterUserManual viewPagerAdapter = new ViewPagerAdapterUserManual(UserManualActivity.this);

        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_indicator_default));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_indicator_selected));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_indicator_default));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.tab_indicator_selected));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    public void skipManual(View view) {
        SheredPref.setUserManualSeenValue(UserManualActivity.this,true);
        this.finish();
    }
}


