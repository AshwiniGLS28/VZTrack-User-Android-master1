package vztrack.gls.com.vztrack_user.responce;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.beans.CarPoolBean;
import vztrack.gls.com.vztrack_user.beans.ResponceBean;

public class CarPoolOfferResponceBean extends ResponceBean {
    @SerializedName("myCarPoolOffers")
    private ArrayList<CarPoolBean> myCarPoolOffers = new ArrayList<>();
    @SerializedName("societyCarPoolOffers")
    private ArrayList<CarPoolBean> societyCarPoolOffers = new ArrayList<>();

    public ArrayList<CarPoolBean> getMyCarPoolOffers() {
        return myCarPoolOffers;
    }
    public void setMyCarPoolOffers(ArrayList<CarPoolBean> myCarPoolOffers) {
        this.myCarPoolOffers = myCarPoolOffers;
    }
    public ArrayList<CarPoolBean> getSocietyCarPoolOffers() {
        return societyCarPoolOffers;
    }
    public void setSocietyCarPoolOffers(ArrayList<CarPoolBean> societyCarPoolOffers) {
        this.societyCarPoolOffers = societyCarPoolOffers;
    }
}
