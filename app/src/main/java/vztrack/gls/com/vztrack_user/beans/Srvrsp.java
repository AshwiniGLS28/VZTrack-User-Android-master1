package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

public class Srvrsp {
	@SerializedName("customer_name")
	private String customer_name;
	@SerializedName("customer_id")
    private String customer_id;
	@SerializedName("customer_code")
    private String customer_code;
	@SerializedName("code")
    private String code;
	@SerializedName("email")
    private String email;
	@SerializedName("invoice_id")
    private String invoice_id;
	@SerializedName("bill_date")
    private String bill_date;
	@SerializedName("short_url")
    private String short_url;
	@SerializedName("invoice_number")
    private String invoice_number;
	@SerializedName("due_date")
    private String due_date;
	@SerializedName("sent_date")
    private String sent_date;
	@SerializedName("state")
    private String state;
	@SerializedName("mobile")
    private String mobile;
	@SerializedName("address")
    private String address;
	@SerializedName("city")
    private String city;
	@SerializedName("zipcode")
    private String zipcode;
	@SerializedName("amount")
    private String amount;
	@SerializedName("status")
    private String status;
	@SerializedName("cycle_name")
    private String cycle_name;
	@SerializedName("franchise_id")
    private String franchise_id;
	@SerializedName("franchise_name")
    private String franchise_name;
	@SerializedName("created_by")
    private String created_by;
	@SerializedName("transaction_id")
    private String transaction_id;
	@SerializedName("paid_date")
    private String paid_date;
	@SerializedName("email_id")
    private String email_id;
	@SerializedName("patron_name")
    private String patron_name;

    public String getCustomer_name ()
    {
        return customer_name;
    }

    public void setCustomer_name (String customer_name)
    {
        this.customer_name = customer_name;
    }

    public String getCustomer_id ()
    {
        return customer_id;
    }

    public void setCustomer_id (String customer_id)
    {
        this.customer_id = customer_id;
    }

    public String getCustomer_code ()
    {
        return customer_code;
    }

    public void setCustomer_code (String customer_code)
    {
        this.customer_code = customer_code;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

	public String getInvoice_id() {
		return invoice_id;
	}

	public void setInvoice_id(String invoice_id) {
		this.invoice_id = invoice_id;
	}

	public String getBill_date() {
		return bill_date;
	}

	public void setBill_date(String bill_date) {
		this.bill_date = bill_date;
	}

	public String getShort_url() {
		return short_url;
	}

	public void setShort_url(String short_url) {
		this.short_url = short_url;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getInvoice_number() {
		return invoice_number;
	}

	public void setInvoice_number(String invoice_number) {
		this.invoice_number = invoice_number;
	}

	public String getDue_date() {
		return due_date;
	}

	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}

	public String getSent_date() {
		return sent_date;
	}

	public void setSent_date(String sent_date) {
		this.sent_date = sent_date;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCycle_name() {
		return cycle_name;
	}

	public void setCycle_name(String cycle_name) {
		this.cycle_name = cycle_name;
	}

	public String getFranchise_id() {
		return franchise_id;
	}

	public void setFranchise_id(String franchise_id) {
		this.franchise_id = franchise_id;
	}

	public String getFranchise_name() {
		return franchise_name;
	}

	public void setFranchise_name(String franchise_name) {
		this.franchise_name = franchise_name;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getPaid_date() {
		return paid_date;
	}

	public void setPaid_date(String paid_date) {
		this.paid_date = paid_date;
	}

	public String getEmail_id() {
		return email_id;
	}

	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}

	public String getPatron_name() {
		return patron_name;
	}

	public void setPatron_name(String patron_name) {
		this.patron_name = patron_name;
	}	
}
