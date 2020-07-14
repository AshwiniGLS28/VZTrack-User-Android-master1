package vztrack.gls.com.vztrack_user.responce;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.beans.RainbowDetailsBean;
import vztrack.gls.com.vztrack_user.beans.ResponceBean;

public class RainbowDetailsResponce extends ResponceBean {
    @SerializedName("rainbowDetailsBeans")
    private ArrayList<RainbowDetailsBean> rainbowDetailsBeans;

    public ArrayList<RainbowDetailsBean> getRainbowDetailsBeans() {
        return rainbowDetailsBeans;
    }

    public void setRainbowDetailsBeans(ArrayList<RainbowDetailsBean> rainbowDetailsBeans) {
        this.rainbowDetailsBeans = rainbowDetailsBeans;
    }
}
