package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sandeep on 6/4/16.
 */
public class FlatResponce {
    @SerializedName("availFlats")
    ArrayList<AvailFlats> availFlats=new ArrayList<>();

    public ArrayList<AvailFlats> getAvailFlats() {
        return availFlats;
    }

    public void setAvailFlats(ArrayList<AvailFlats> availFlats) {
        this.availFlats = availFlats;
    }
}
