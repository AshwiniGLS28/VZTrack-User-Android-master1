package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.beans.DrawerConfigBean.DrawerConfigParent;

public class SocietyFeaturesConfigBean {
	@SerializedName("outTime")
	private boolean outTime;
	@SerializedName("company")
	private boolean company;
	@SerializedName("sos")
	private boolean sos;
	@SerializedName("complain")
	private boolean complain;
	@SerializedName("localStore")
	private boolean localStore;
	@SerializedName("polls")
	private boolean polls;
	@SerializedName("carpool")
	private boolean carpool;
	@SerializedName("knowYourMaid")
	private boolean knowYourMaid;
	@SerializedName("rainbow")
	private boolean rainbow;
	@SerializedName("marketplace")
	private boolean marketplace;
	@SerializedName("searchVehicle")
	private boolean searchVehicle;
	@SerializedName("visitors")
	private boolean visitors;
	@SerializedName("messages")
	private boolean messages;
	@SerializedName("noticesAndMinuts")
	private boolean noticesAndMinuts;
	@SerializedName("setting")
	private boolean setting;
	@SerializedName("referVz")
	private boolean referVz;
	@SerializedName("serviceProvider")
	private boolean serviceProvider;
	@SerializedName("support")
	private boolean support;
	@SerializedName("garageWorks")
	private boolean garageWorks;
	@SerializedName("inviteGuest")
	private boolean inviteGuest;
	@SerializedName("invoice")
	private boolean invoice;
	@SerializedName("preApproval")
	private boolean preApproval;
	@SerializedName("thermal")
	private boolean thermal;
	@SerializedName("userAppMenuJsonObject")
	private ArrayList<DrawerConfigParent> userAppMenuJsonObject;

	public boolean isOutTime() {
		return outTime;
	}

	public void setOutTime(boolean outTime) {
		this.outTime = outTime;
	}

	public boolean isCompany() {
		return company;
	}

	public void setCompany(boolean company) {
		this.company = company;
	}

	public boolean isSos() {
		return sos;
	}

	public void setSos(boolean sos) {
		this.sos = sos;
	}

	public boolean isComplain() {
		return complain;
	}

	public void setComplain(boolean complain) {
		this.complain = complain;
	}

	public boolean isLocalStore() {
		return localStore;
	}

	public void setLocalStore(boolean localStore) {
		this.localStore = localStore;
	}

	public boolean isPolls() {
		return polls;
	}

	public void setPolls(boolean polls) {
		this.polls = polls;
	}

	public boolean isCarpool() {
		return carpool;
	}

	public void setCarpool(boolean carpool) {
		this.carpool = carpool;
	}

	public boolean isKnowYourMaid() {
		return knowYourMaid;
	}

	public void setKnowYourMaid(boolean knowYourMaid) {
		this.knowYourMaid = knowYourMaid;
	}

	public boolean isRainbow() {
		return rainbow;
	}

	public void setRainbow(boolean rainbow) {
		this.rainbow = rainbow;
	}

	public boolean isMarketplace() {
		return marketplace;
	}

	public void setMarketplace(boolean marketplace) {
		this.marketplace = marketplace;
	}

	public boolean isSearchVehicle() {
		return searchVehicle;
	}

	public void setSearchVehicle(boolean searchVehicle) {
		this.searchVehicle = searchVehicle;
	}

	public boolean isVisitors() {
		return visitors;
	}

	public void setVisitors(boolean visitors) {
		this.visitors = visitors;
	}

	public boolean isMessages() {
		return messages;
	}

	public void setMessages(boolean messages) {
		this.messages = messages;
	}

	public boolean isNoticesAndMinuts() {
		return noticesAndMinuts;
	}

	public void setNoticesAndMinuts(boolean noticesAndMinuts) {
		this.noticesAndMinuts = noticesAndMinuts;
	}

	public boolean isSetting() {
		return setting;
	}

	public void setSetting(boolean setting) {
		this.setting = setting;
	}

	public boolean isReferVz() {
		return referVz;
	}

	public void setReferVz(boolean referVz) {
		this.referVz = referVz;
	}

	public boolean isServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(boolean serviceProvider) {
		this.serviceProvider = serviceProvider;
	}


	public boolean isSupport() {
		return support;
	}

	public void setSupport(boolean support) {
		this.support = support;
	}

    public boolean isGarageWorks() {
        return garageWorks;
    }

    public void setGarageWorks(boolean garageWorks) {
        this.garageWorks = garageWorks;
    }

	public boolean isInviteGuest() {
		return inviteGuest;
	}

	public void setInviteGuest(boolean inviteGuest) {
		this.inviteGuest = inviteGuest;
	}

	public boolean isInvoice() {
		return invoice;
	}

	public void setInvoice(boolean invoice) {
		this.invoice = invoice;
	}

	public boolean isPreApproval() {
		return preApproval;
	}

	public void setPreApproval(boolean preApproval) {
		this.preApproval = preApproval;
	}

	public ArrayList<DrawerConfigParent> getUserAppMenuJsonObject() {
		return userAppMenuJsonObject;
	}

	public void setUserAppMenuJsonObject(ArrayList<DrawerConfigParent> userAppMenuJsonObject) {
		this.userAppMenuJsonObject = userAppMenuJsonObject;
	}

	public boolean isThermal() {
		return thermal;
	}

	public void setThermal(boolean thermal) {
		this.thermal = thermal;
	}
}
