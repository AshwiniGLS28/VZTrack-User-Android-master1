package vztrack.gls.com.vztrack_user.responce;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.beans.CarPoolBean;
import vztrack.gls.com.vztrack_user.beans.ResponceBean;

public class CarPoolRequestResponceBean extends ResponceBean {
    @SerializedName("societyCarPoolRequests")
    ArrayList<CarPoolBean> societyCarPoolRequests = new ArrayList<>();
    @SerializedName("myCarPoolRequests")
    ArrayList<CarPoolBean> myCarPoolRequests = new ArrayList<>();

    public ArrayList<CarPoolBean> getSocietyCarPoolRequests() {
        return societyCarPoolRequests;
    }
    public void setSocietyCarPoolRequests(ArrayList<CarPoolBean> societyCarPoolRequests) {
        this.societyCarPoolRequests = societyCarPoolRequests;
    }
    public ArrayList<CarPoolBean> getMyCarPoolRequests() {
        return myCarPoolRequests;
    }
    public void setMyCarPoolRequests(ArrayList<CarPoolBean> myCarPoolRequests) {
        this.myCarPoolRequests = myCarPoolRequests;
    }
}
