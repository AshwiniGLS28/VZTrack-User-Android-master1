package vztrack.gls.com.vztrack_user.CustumView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

public class CustomRadioButton extends RadioButton {
    public CustomRadioButton(Context context) {
            super(context);
            Typeface face=Typeface.createFromAsset(context.getAssets(), "robotoregular.ttf");
            this.setTypeface(face);
        }

    public CustomRadioButton(Context context, AttributeSet attrs) {
            super(context, attrs);
            Typeface face=Typeface.createFromAsset(context.getAssets(), "robotoregular.ttf");
            this.setTypeface(face);
        }

    public CustomRadioButton(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            Typeface face=Typeface.createFromAsset(context.getAssets(), "robotoregular.ttf");
//        Typeface face=Typeface.createFromAsset(context.getAssets(), "helveticaneuelight.ttf");


            this.setTypeface(face);
            this.setTextSize(10);
        }

        protected void onDraw (Canvas canvas) {
            super.onDraw(canvas);


        }
}
