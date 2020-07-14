package vztrack.gls.com.vztrack_user.responce;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.beans.CommentBean;
import vztrack.gls.com.vztrack_user.beans.MarketplaceBean;
import vztrack.gls.com.vztrack_user.beans.ResponceBean;

public class MarketPlaceResponceBean extends ResponceBean {

    @SerializedName("advertises")
    ArrayList<MarketplaceBean> advertises = new ArrayList<>();
    @SerializedName("details")
    MarketplaceBean details = new MarketplaceBean();
    @SerializedName("comments")
    ArrayList<CommentBean> comments = new ArrayList<>();


    public ArrayList<MarketplaceBean> getAdvertises() {
        return advertises;
    }

    public void setAdvertises(ArrayList<MarketplaceBean> advertises) {
        this.advertises = advertises;
    }

    public MarketplaceBean getDetails() {
        return details;
    }

    public void setDetails(MarketplaceBean details) {
        this.details = details;
    }

    public ArrayList<CommentBean> getComments() {
        return comments;
    }

    public void setComments(ArrayList<CommentBean> comments) {
        this.comments = comments;
    }
}
