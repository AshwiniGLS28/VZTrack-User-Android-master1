package vztrack.gls.com.vztrack_user.profile;

import com.google.gson.annotations.SerializedName;

public class Socity {
	@SerializedName("is_active")
	private String is_active;
	@SerializedName("secretory_mobile")
	private String secretory_mobile;
	@SerializedName("secretory_name")
	private String secretory_name;
	@SerializedName("society_code")
	private String society_code;
	@SerializedName("socity_addr")
	private String socity_addr;
	@SerializedName("socity_id")
	private String socity_id;
	@SerializedName("socity_name")
	private String socity_name;
	@SerializedName("minMobLen")
	private int minMobLen;
	@SerializedName("maxMobLen")
	private int maxMobLen;

	public String getIs_active() {
		return is_active;
	}

	public void setIs_active(String is_active) {
		this.is_active = is_active;
	}

	public String getSecretory_mobile() {
		return secretory_mobile;
	}

	public void setSecretory_mobile(String secretory_mobile) {
		this.secretory_mobile = secretory_mobile;
	}

	public String getSecretory_name() {
		return secretory_name;
	}

	public void setSecretory_name(String secretory_name) {
		this.secretory_name = secretory_name;
	}

	public String getSociety_code() {
		return society_code;
	}

	public void setSociety_code(String society_code) {
		this.society_code = society_code;
	}

	public String getSocity_addr() {
		return socity_addr;
	}

	public void setSocity_addr(String socity_addr) {
		this.socity_addr = socity_addr;
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

	public int getMinMobLen() {
		return minMobLen;
	}

	public void setMinMobLen(int minMobLen) {
		this.minMobLen = minMobLen;
	}

	public int getMaxMobLen() {
		return maxMobLen;
	}

	public void setMaxMobLen(int maxMobLen) {
		this.maxMobLen = maxMobLen;
	}
}
