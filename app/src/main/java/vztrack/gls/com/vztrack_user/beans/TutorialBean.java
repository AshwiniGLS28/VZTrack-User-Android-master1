package vztrack.gls.com.vztrack_user.beans;

import android.graphics.drawable.Drawable;

public class TutorialBean {
    String tutorialheading;
    String tutorialsubheading;
    String imagheading;
    Drawable tutorialimage;

    public String getTutorialheading() {
        return tutorialheading;
    }

    public void setTutorialheading(String tutorialheading) {
        this.tutorialheading = tutorialheading;
    }

    public String getTutorialsubheading() {
        return tutorialsubheading;
    }

    public void setTutorialsubheading(String tutorialsubheading) {
        this.tutorialsubheading = tutorialsubheading;
    }

    public String getImagheading() {
        return imagheading;
    }

    public void setImagheading(String imagheading) {
        this.imagheading = imagheading;
    }

    public Drawable getTutorialimage() {
        return tutorialimage;
    }

    public void setTutorialimage(Drawable tutorialimage) {
        this.tutorialimage = tutorialimage;
    }
}
