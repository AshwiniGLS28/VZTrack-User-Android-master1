package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InvoiceResponce {
    @SerializedName("reqtime")
	String reqtime;
    @SerializedName("resptime")
	String resptime;
    @SerializedName("srvrsp")
	ArrayList<Srvrsp> srvrsp;
	@SerializedName("errcode")
	String errcode;
	@SerializedName("errmsg")
	String errmsg;
	@SerializedName("errlist")
	String errlist;

	public String getReqtime() {
		return reqtime;
	}

	public void setReqtime(String reqtime) {
		this.reqtime = reqtime;
	}

	public String getResptime() {
		return resptime;
	}

	public void setResptime(String resptime) {
		this.resptime = resptime;
	}

	public ArrayList<Srvrsp> getSrvrsp() {
		return srvrsp;
	}

	public void setSrvrsp(ArrayList<Srvrsp> srvrsp) {
		this.srvrsp = srvrsp;
	}

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getErrlist() {
		return errlist;
	}

	public void setErrlist(String errlist) {
		this.errlist = errlist;
	}
}
