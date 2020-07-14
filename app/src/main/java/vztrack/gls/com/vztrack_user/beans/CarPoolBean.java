package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CarPoolBean implements Serializable {
    @SerializedName("id")
    int id;
    @SerializedName("flatNo")
    private String flatNo;
    @SerializedName("source")
    private String source;
    @SerializedName("destination")
    private String destination;
    @SerializedName("tripDate")
    private String tripDate;
    @SerializedName("noOfSeatsAvailable")
    private int noOfSeatsAvailable;
    @SerializedName("remark")
    private String remark;
    @SerializedName("mobileNo")
    private String mobileNo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public int getNoOfSeatsAvailable() {
        return noOfSeatsAvailable;
    }

    public void setNoOfSeatsAvailable(int noOfSeatsAvailable) {
        this.noOfSeatsAvailable = noOfSeatsAvailable;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
