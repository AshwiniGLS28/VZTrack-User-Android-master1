package vztrack.gls.com.vztrack_user.CustumView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextViewMedium extends TextView {


    public CustomTextViewMedium(Context context) {
        super(context);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "roboto_medium.ttf");
        this.setTypeface(face);
    }

    public CustomTextViewMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "roboto_medium.ttf");
        this.setTypeface(face);
    }

    public CustomTextViewMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "roboto_medium.ttf");
//        Typeface face=Typeface.createFromAsset(context.getAssets(), "helveticaneuelight.ttf");


        this.setTypeface(face);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);


    }

}

