package vztrack.gls.com.vztrack_user.fragment;

/**
 * Created by sandeep on 14/3/16.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.google.gson.Gson;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.livechatinc.inappchat.ChatWindowActivity;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.profile.FamilyBean;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.PostData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

public class SupportFragment extends Fragment {
//    CardView cardView_faq, cardView_Chat;
//    CardView cardView_feedback, cardView_contact;
    TextView tvFaq,tvFeedback,tvContact,tvChat;
    CheckConnection cc;
    public static String name;
    public static int level;
    public static String emailId;
    public static String message;
    private TextView title,faq,feedback,contact;
    Context context;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public SupportFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_support, null);
        context = getActivity();
        String title1= UtilityMethods.getTitleFromMenuJson(getResources().getString(R.string.supprot_chat),getActivity());
        Log.e("titlevisitor",title+"");
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title1);
//        UtilityMethodsAndroid.setActionBarTitle(getActivity());
//        cardView_faq = (CardView) root.findViewById(R.id.card_view_faq);
//        cardView_feedback = (CardView) root.findViewById(R.id.card_view_feedback);
//        cardView_contact = (CardView) root.findViewById(R.id.card_view_contact);

        tvFaq=root.findViewById(R.id.tvFaq);
        tvFeedback=root.findViewById(R.id.tvFeedback);
        tvContact=root.findViewById(R.id.tvContact);
        tvChat=root.findViewById(R.id.tvChat);

//        cardView_Chat = (CardView) root.findViewById(R.id.card_view_chat);
        title = (TextView) root.findViewById(R.id.tvTitle);
        faq = (TextView) root.findViewById(R.id.tvFaq);
        feedback = (TextView) root.findViewById(R.id.tvFeedback);
        contact = (TextView) root.findViewById(R.id.tvContact);

        cc = new CheckConnection(getActivity());
        setHasOptionsMenu(true);
        tvFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cc.isConnectingToInternet()) {
                    Intent viewIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse("https://vztrack.net/VZ-FAQ/"));
                    startActivity(viewIntent);
                }else{
                    CommonMethods.showToastError(getActivity(), "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        tvFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cc.isConnectingToInternet()) {
                    // Create custom dialog object
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setCancelable(false);
                    // Include dialog.xml file
                    dialog.setContentView(R.layout.dialog_feedback);
                    // Set dialog title
                    //dialog.setTitle("Feedback");
                    Button cancle =  dialog.findViewById(R.id.btn_cancle);
                    final Button save =  dialog.findViewById(R.id.btn_save);

                    final EditText et_name = (EditText) dialog.findViewById(R.id.etName);
                    final EditText et_EmailId = (EditText) dialog.findViewById(R.id.etEmailId);
                    final EditText et_message = (EditText) dialog.findViewById(R.id.etMessage);
                    final SmileRating smileRating = (SmileRating) dialog.findViewById(R.id.smile_rating);
                    smileRating.setSelectedSmile(BaseRating.OKAY);
                    String owner_name = SheredPref.getOwnerName(getActivity());
                    et_name.setText(owner_name);
                    dialog.show();


                    // if decline button is clicked, close the custom dialog
                    cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });


                    // if decline button is clicked, close the custom dialog
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String emailId = et_EmailId.getText().toString().trim();
                            boolean emailValidate =validate(emailId);
                            if(emailId.equals("")){
//                                new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
//                                        .setTitleText("")
//                                        .setContentText("Please provide your Email ID so that we can get back to you with a response regarding your queries\n")
//                                        .show();


                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final View dialogView = inflater.inflate(R.layout.custum_alert_dialog, null);
                                dialogBuilder.setView(dialogView);
                                TextView txtalertheading = dialogView.findViewById(R.id.txtalertheading);
                                TextView txtalertsubheading = dialogView.findViewById(R.id.txtalertsubheading);

                                TextView btnegative = dialogView.findViewById(R.id.btnegative);
                                TextView btnpositive = dialogView.findViewById(R.id.btnpositive);

                                txtalertheading.setVisibility(View.GONE);
                                txtalertsubheading.setText("Please provide your Email ID so that we can get back to you with a response regarding your queries\n");

                                btnegative.setVisibility(View.GONE);


                                final AlertDialog b = dialogBuilder.create();
                                b.setCanceledOnTouchOutside(false);
                                b.setCancelable(false);
                                b.show();

                                btnpositive.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        b.dismiss();

                                    }
                                });
                                btnegative.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        b.dismiss();
                                    }
                                });
                            }
                            else if(!emailValidate){
                                CommonMethods.showToastError(getActivity(), "Please enter valid email address");//, Toast.LENGTH_SHORT, true).show();
                            }
                            else if(et_message.getText().toString().trim().equals("")){
                                CommonMethods.showToastError(getActivity(), "Please enter message");//, Toast.LENGTH_SHORT, true).show();
                            }else{
                                    level = smileRating.getRating(); //evel is from 1 to 5
                                    name = et_name.getText().toString().trim();
                                    emailId = et_EmailId.getText().toString().trim();
                                    message = et_message.getText().toString().trim();
                                    String society_id = SheredPref.getSocietyId(getActivity());
                                    String flat_number = SheredPref.getFlat_No(getActivity());
                                    String family_id = SheredPref.getFamilyId(getActivity());

                                    FamilyBean familyBean = new FamilyBean();
                                    familyBean.setFlatOwnerName(name);
                                    familyBean.setFlatOwnerMobile(message);
                                    familyBean.setSocietyId(Integer.parseInt(society_id));
                                    familyBean.setFlatNo(flat_number);
                                    familyBean.setFamilyId(Integer.parseInt(family_id));
                                    familyBean.setFlatFamilySize(level);
                                    familyBean.setWing(emailId);
                                    new PostData(new Gson().toJson(familyBean), MainActivity.mainActivity, CallFor.FEEDBACK).execute();
                                    dialog.dismiss();
                            }
                        }
                    });

                }else{
                    CommonMethods.showToastError(getActivity(), "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                }
            }
        });
        tvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cc.isConnectingToInternet()) {

                    // Create custom dialog object
                    final Dialog dialog = new Dialog(getActivity());
                    dialog.setCancelable(true);
                    // Include dialog.xml file
                    dialog.setContentView(R.layout.contact_dialog);
                    // Set dialog title
                    //dialog.setTitle("Feedback");
                    LinearLayout web = (LinearLayout) dialog.findViewById(R.id.web);
                    LinearLayout email = (LinearLayout) dialog.findViewById(R.id.email);
                    LinearLayout email2 = (LinearLayout) dialog.findViewById(R.id.email2);
                    LinearLayout phone = (LinearLayout) dialog.findViewById(R.id.phone);

                    dialog.show();

                    web.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent viewIntent =
                                    new Intent("android.intent.action.VIEW",
                                            Uri.parse("https://vztrack.net/"));
                            startActivity(viewIntent);
                        }
                    });

                    email.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent emailIntent = new Intent(Intent.ACTION_SEND);

                            emailIntent.setData(Uri.parse("mailto:"));
                          //  emailIntent.setType("text/plain");
                            emailIntent.setType("message/rfc822");
                            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { "sales@vztrack.in" });
                           /// emailIntent.putExtra(Intent.EXTRA_EMAIL, "sales@vztrack.in");
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "VZTrack ");

                            try {
                                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                               // finish();
                                //Log.i("Finished sending email...", "");
                            } catch (android.content.ActivityNotFoundException ex) {
                                CommonMethods.showToastError(getActivity(), "There is no email client installed.");//, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    email2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent emailIntent = new Intent(Intent.ACTION_SEND);

                            emailIntent.setData(Uri.parse("mailto:"));
                            //  emailIntent.setType("text/plain");
                            emailIntent.setType("message/rfc822");
                            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { "support@vztrack.in" });
                            /// emailIntent.putExtra(Intent.EXTRA_EMAIL, "sales@vztrack.in");
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "VZTrack ");

                            try {
                                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                                // finish();
                                //Log.i("Finished sending email...", "");
                            } catch (android.content.ActivityNotFoundException ex) {
                                CommonMethods.showToastError(getActivity(), "There is no email client installed.");//, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Uri number = Uri.parse("tel:" + "9075016367");
                            Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                            startActivity(callIntent);
                        }
                    });

                }else{
                    CommonMethods.showToastError(getActivity(), "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                }
            }
        });
        tvChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cc.isConnectingToInternet()) {
                    // CleverTap
                    CleverTapAPI cleverTap = null;
                    try {
                        cleverTap = CleverTapAPI.getInstance(getActivity());
                        HashMap<String, Object> loginAction = new HashMap<String, Object>();
                        cleverTap.event.push(Events.event_support_chat, loginAction);
                    } catch (CleverTapMetaDataNotFoundException e) {
                        e.printStackTrace();
                    } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
                        cleverTapPermissionsNotSatisfied.printStackTrace();
                    }
                    Intent intent = new Intent(context, com.livechatinc.inappchat.ChatWindowActivity.class);
                    intent.putExtra(com.livechatinc.inappchat.ChatWindowActivity.KEY_GROUP_ID, "");
                    intent.putExtra(ChatWindowActivity.KEY_LICENCE_NUMBER, "9226615");
                    context.startActivity(intent);
                }else{
                    CommonMethods.showToastError(getActivity(), "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                }
            }
        });
        return root;
    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

}