package vztrack.gls.com.vztrack_user.responce;

/**
 * Created by sandeep on 18/9/17.
 */

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import vztrack.gls.com.vztrack_user.beans.SearchVehicleBean;
import vztrack.gls.com.vztrack_user.profile.VehicleBean;

public class VehicleResponce {
    @SerializedName("code")
    private String code;
    @SerializedName("message")
    private String message;
    @SerializedName("vehicleBeans")
    private ArrayList<VehicleBean> vehicleBeans = new ArrayList<>();
    @SerializedName("searchVehicleBeans")
    private ArrayList<SearchVehicleBean> searchVehicleBeans =  new ArrayList<>();
    @SerializedName("vehiclePattern")
    private ArrayList<String> vehiclePattern = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public ArrayList<VehicleBean> getVehicleBeans() {
        return vehicleBeans;
    }
    public void setVehicleBeans(ArrayList<VehicleBean> vehicleBeans) {
        this.vehicleBeans = vehicleBeans;
    }
    public ArrayList<String> getVehiclePattern() {
        return vehiclePattern;
    }
    public void setVehiclePattern(ArrayList<String> vehiclePattern) {
        this.vehiclePattern = vehiclePattern;
    }
    public ArrayList<SearchVehicleBean> getSearchVehicleBeans() {
        return searchVehicleBeans;
    }
    public void setSearchVehicleBeans(ArrayList<SearchVehicleBean> searchVehicleBeans) {
        this.searchVehicleBeans = searchVehicleBeans;
    }
}
