package vztrack.gls.com.vztrack_user.profile;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 11/4/16.
 */
public class Visitors {
    @SerializedName("building_id")
    String building_id;
    @SerializedName("doc_url")
    String doc_url;
    @SerializedName("first_name")
    String first_name;
    @SerializedName("flat_no")
    String flat_no;
    @SerializedName("from")
    String from;
    @SerializedName("gate_no")
    String gate_no;
    @SerializedName("in_time")
    String in_time;
    @SerializedName("out_time")
    String out_time;
    @SerializedName("is_verified")
    String is_verified;
    @SerializedName("last_name")
    String last_name;
    @SerializedName("mobile_no")
    String mobile_no;
    @SerializedName("no_of_visitors")
    String no_of_visitors;
    @SerializedName("old")
    boolean old;
    @SerializedName("photo_url")
    String photo_url;
    @SerializedName("socity_id")
    String socity_id;
    @SerializedName("socity_name")
    String socity_name;
    @SerializedName("vehicle_no")
    String vehicle_no;
    @SerializedName("visit_purpose")
    String visit_purpose;
    @SerializedName("visitor_id")
    String visitor_id;
    @SerializedName("watchman_id")
    String watchman_id;
    @SerializedName("watchmen_mobile")
    String watchmen_mobile;
    @SerializedName("watchmen_name")
    String watchmen_name;
    @SerializedName("whomeToVisit")
    String whomeToVisit;
    @SerializedName("error_flag")
    boolean error_flag;
    @SerializedName("badgeNumber")
    String badgeNumber;
    @SerializedName("askForApproval")
    boolean askForApproval;
    @SerializedName("approvalStatus")
    String approvalStatus;
    @SerializedName("approvalCount")
     int approvalCount;
    @SerializedName("visitorTemperature")
    String visitorTemperature;
    @SerializedName("mask")
    boolean mask;

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public String getBuilding_id() {
        return building_id;
    }

    public void setBuilding_id(String building_id) {
        this.building_id = building_id;
    }

    public String getDoc_url() {
        return doc_url;
    }

    public void setDoc_url(String doc_url) {
        this.doc_url = doc_url;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getFlat_no() {
        return flat_no;
    }

    public void setFlat_no(String flat_no) {
        this.flat_no = flat_no;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getGate_no() {
        return gate_no;
    }

    public void setGate_no(String gate_no) {
        this.gate_no = gate_no;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(String is_verified) {
        this.is_verified = is_verified;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getNo_of_visitors() {
        return no_of_visitors;
    }

    public void setNo_of_visitors(String no_of_visitors) {
        this.no_of_visitors = no_of_visitors;
    }

    public boolean isOld() {
        return old;
    }

    public void setOld(boolean old) {
        this.old = old;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getSocity_id() {
        return socity_id;
    }

    public void setSocity_id(String socity_id) {
        this.socity_id = socity_id;
    }

    public String getSocity_name() {
        return socity_name;
    }

    public void setSocity_name(String socity_name) {
        this.socity_name = socity_name;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public String getVisit_purpose() {
        return visit_purpose;
    }

    public void setVisit_purpose(String visit_purpose) {
        this.visit_purpose = visit_purpose;
    }

    public String getVisitor_id() {
        return visitor_id;
    }

    public void setVisitor_id(String visitor_id) {
        this.visitor_id = visitor_id;
    }

    public String getWatchman_id() {
        return watchman_id;
    }

    public void setWatchman_id(String watchman_id) {
        this.watchman_id = watchman_id;
    }

    public String getWatchmen_mobile() {
        return watchmen_mobile;
    }

    public void setWatchmen_mobile(String watchmen_mobile) {
        this.watchmen_mobile = watchmen_mobile;
    }

    public String getWatchmen_name() {
        return watchmen_name;
    }

    public void setWatchmen_name(String watchmen_name) {
        this.watchmen_name = watchmen_name;
    }

    public String getWhomeToVisit() {
        return whomeToVisit;
    }

    public void setWhomeToVisit(String whomeToVisit) {
        this.whomeToVisit = whomeToVisit;
    }

    public boolean isError_flag() {
        return error_flag;
    }
    public void setError_flag(boolean error_flag) {
        this.error_flag = error_flag;
    }

    public String getBadgeNumber() {
        return badgeNumber;
    }

    public void setBadgeNumber(String badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

    public boolean isAskForApproval() {
        return askForApproval;
    }

    public void setAskForApproval(boolean askForApproval) {
        this.askForApproval = askForApproval;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public int getApprovalCount() {
        return approvalCount;
    }

    public void setApprovalCount(int approvalCount) {
        this.approvalCount = approvalCount;
    }

    public String getVisitorTemperature() {
        return visitorTemperature;
    }

    public void setVisitorTemperature(String visitorTemperature) {
        this.visitorTemperature = visitorTemperature;
    }

    public boolean isMask() {
        return mask;
    }

    public void setMask(boolean mask) {
        this.mask = mask;
    }
}
