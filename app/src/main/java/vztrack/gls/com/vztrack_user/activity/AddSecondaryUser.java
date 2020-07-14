package vztrack.gls.com.vztrack_user.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.CustumView.CustomButton;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.beans.FeaturesBean;
import vztrack.gls.com.vztrack_user.beans.SecondaryUserBean;
import vztrack.gls.com.vztrack_user.responce.SecondaryUserResponseBean;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.PostData;

import androidx.appcompat.app.AlertDialog;

public class AddSecondaryUser extends BaseActivity{
    private EditText getEmailId;
    private EditText getName;
    private TextView featureNameLabel;
    private TextView counterHeading;
    private CheckBox featureAccess;
    private CheckConnection cc;
    private int isTenant = 2;
    private String emailId;
    private String userName;
    private String emailPattern;
    private SecondaryUserResponseBean secondaryUserResponseBean;
    private SecondaryUserBean secondaryUserBean;
    private SecondaryUserBean secondaryUserBeanTemp;
    private CustomButton btnAddUser;
    private ImageView backPress;
    private TextView title;
    private RadioButton radioTenant;
    private RadioButton radioFamilyMember;
    private final String TAG = "AddSecondaryUserAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary_user);

        initViews();
        init();

        backPress.setOnClickListener(v->{
            onBackPressed();
        });

        radioTenant.setOnClickListener(v->{
            radioTenant.setChecked(true);
            radioFamilyMember.setChecked(false);
            isTenant = 1;
        });

        radioFamilyMember.setOnClickListener(v->{
            radioFamilyMember.setChecked(true);
            radioTenant.setChecked(false);
            isTenant = 0;
        });

        getName.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                counterHeading.setText(s.length()+" / max 50");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void init() {
        cc = new CheckConnection(this);
        title.setText(getResources().getString(R.string.addaccount));
    }

    private void initViews() {
        backPress =findViewById(R.id.backpress);
        counterHeading = findViewById(R.id.counterUser);
        getEmailId = findViewById(R.id.emailIDSecondaryUser);
        getName = findViewById(R.id.nameSecondaryUser);
        featureAccess = findViewById(R.id.isFeatureAccess);
        featureNameLabel = findViewById(R.id.featureName);
        radioFamilyMember =findViewById(R.id.radiofamilymember);
        radioTenant =findViewById(R.id.radiotenant);
        title=findViewById(R.id.title);
    }

    public void addUser(View v){
        if(cc.isConnectingToInternet()) {
            secondaryUserBeanTemp = new SecondaryUserBean();
            emailId = getEmailId.getText().toString().trim();
            emailPattern = "[a-zA-Z0-9._%+-]+@[a-z.-]+\\.+[a-z]+";
            userName = getName.getText().toString();
            if(emailId.equals("") ){
                CommonMethods.showToastError(this, "Email Id should not be blank");
            }else if(userName.equals("")){
                CommonMethods.showToastError(this, "Name should not be blank");
            }else if(isTenant == 2){
                CommonMethods.showToastError(this, "No type selected");
            }else{
                String smallCaseEmail = emailId.toLowerCase();
                secondaryUserBeanTemp.setUsername(smallCaseEmail);
                secondaryUserBeanTemp.setName(userName);
                if(isTenant == 1){
                    secondaryUserBeanTemp.setTenant(true);
                }else {
                    secondaryUserBeanTemp.setTenant(false);
                }
                FeaturesBean bean = new FeaturesBean();
                ArrayList<FeaturesBean> features = new ArrayList<>();
                bean.setFeatureAccess(featureAccess.isChecked());
                bean.setFeatureName("Invoice");
                features.add(bean);
                secondaryUserBeanTemp.setFeatures(features);
                secondaryUserBean = secondaryUserBeanTemp;
                new PostData(new Gson().toJson(secondaryUserBean), this, CallFor.ADD_SECONDARY_USER).execute();
            }
        }else{
            CommonMethods.showToastSuccess(this, "Please check internet connection");
        }
    }
    @Override
    public void onGetResponse(String response, String callFor) {
        secondaryUserResponseBean = new  Gson().fromJson(response,SecondaryUserResponseBean.class);
        if(secondaryUserResponseBean.getCode().contains("SUCCESS")) {
            getEmailId.setText("");
            getName.setText("");
            featureAccess.setChecked(false);
            showSuccessDialog();
        }else{
            CommonMethods.showToastSuccess(this, "User Already Exists");
        }
    }

    private void showSuccessDialog() {
        try {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View dialogView = inflater.inflate(R.layout.custum_alert_dialog, null);
            dialogBuilder.setView(dialogView);
            TextView txtAlertHeading = dialogView.findViewById(R.id.txtalertheading);
            TextView txtAlertSubHeading = dialogView.findViewById(R.id.txtalertsubheading);
            TextView btnNegative = dialogView.findViewById(R.id.btnegative);
            TextView btnPositive = dialogView.findViewById(R.id.btnpositive);
            txtAlertHeading.setText(secondaryUserResponseBean.getMessage());
            txtAlertSubHeading.setText("Verification link has been sent this Email Id");

            btnNegative.setVisibility(View.GONE);
            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);
            alertDialog.show();
            btnPositive.setText("OK");
            btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    AddSecondaryUser.this.finish();
                }
            });
        } catch (Exception e) {
            Log.e(TAG," Exception To Show Dialog : "+e);
        }
    }
}