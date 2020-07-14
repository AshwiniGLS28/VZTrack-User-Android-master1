package vztrack.gls.com.vztrack_user.beans;

import java.util.ArrayList;

public class CarPoolOfferResponceBean extends ResponceBean {

    ArrayList<CarPoolBean> myCarPoolOffers = new ArrayList<>();
    ArrayList<CarPoolBean> societyCarPoolOffers = new ArrayList<>();

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
