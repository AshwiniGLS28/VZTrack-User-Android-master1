package vztrack.gls.com.vztrack_user.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sandeep on 19/3/18.
 */

public class OptionsDetailsBean {
	@SerializedName("optionId")
	private int optionId;
	@SerializedName("optionText")
	private String optionText;
	@SerializedName("votePercent")
	private double votePercent;

	public int getOptionId() {
		return optionId;
	}

	public void setOptionId(int optionId) {
		this.optionId = optionId;
	}

	public String getOptionText() {
		return optionText;
	}

	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}

	public double getVotePercent() {
		return votePercent;
	}

	public void setVotePercent(double votePercent) {
		this.votePercent = votePercent;
	}

}