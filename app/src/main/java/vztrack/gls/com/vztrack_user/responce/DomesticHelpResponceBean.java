package vztrack.gls.com.vztrack_user.responce;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DomesticHelpResponceBean {
    @SerializedName("code")
    String code;
    @SerializedName("message")
    String message;
    @SerializedName("domesticHelpProviders")
    ArrayList<vztrack.gls.com.vztrack_user.beans.domesticHelpProviders> domesticHelpProviders = new ArrayList<>();
    @SerializedName("listOfPurpose")
    ArrayList<String> listOfPurpose = new ArrayList<>();

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

    public ArrayList<vztrack.gls.com.vztrack_user.beans.domesticHelpProviders> getDomesticHelpProviders() {
        return domesticHelpProviders;
    }

    public void setDomesticHelpProviders(ArrayList<vztrack.gls.com.vztrack_user.beans.domesticHelpProviders> domesticHelpProviders) {
        this.domesticHelpProviders = domesticHelpProviders;
    }

    public ArrayList<String> getListOfPurpose() {
        return listOfPurpose;
    }

    public void setListOfPurpose(ArrayList<String> listOfPurpose) {
        this.listOfPurpose = listOfPurpose;
    }
}
