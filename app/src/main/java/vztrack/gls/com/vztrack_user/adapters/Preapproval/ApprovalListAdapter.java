package vztrack.gls.com.vztrack_user.adapters.Preapproval;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.google.gson.Gson;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.activity.PreApprovalActivity;
import vztrack.gls.com.vztrack_user.beans.PreApprovalBean.ApprovalBean;
import vztrack.gls.com.vztrack_user.profile.FamilyBean;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.PostData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

import static vztrack.gls.com.vztrack_user.activity.MainActivity.REQUEST_PREAPPROVAL;
import static vztrack.gls.com.vztrack_user.fragment.ApprovalListFragment.getCallFroDelete;

public class ApprovalListAdapter extends ArrayAdapter {
    FragmentActivity context;
    int resource;
    ArrayList<ApprovalBean> approvalBeans;
    LayoutInflater inflater;
    ImageView imgdelete,ideditaapproval;
    TextView txtenddate,txtstartdate;
    String[] months;
    public ApprovalListAdapter(@NonNull FragmentActivity context, int resource, @NonNull ArrayList<ApprovalBean> approvalBeans) {
        super(context, resource, approvalBeans);
        this.context = context;
        this.resource = resource;
        this.approvalBeans = approvalBeans;
//        Log.e("list adapter",new Gson().toJson(approvalBeans)+"");
        inflater = context.getLayoutInflater();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = inflater.inflate(resource, null);
//        Log.e("approvaladpater",new Gson().toJson(approvalBeans)+"--");
        imgdelete=row.findViewById(R.id.imgdelete);
        ideditaapproval=row.findViewById(R.id.ideditaapproval);
        txtenddate=row.findViewById(R.id.txtenddate);
        txtstartdate=row.findViewById(R.id.txtstartdate);
        months = context.getResources().getStringArray(R.array.months);
        if(approvalBeans.get(position) == null){
            row.findViewById(R.id.card_view).setVisibility(View.INVISIBLE);
        } else {
            if (approvalBeans.get(position).getPurposeId()==0)
            {
                ((TextView)row.findViewById(R.id.txtheading)).setText(approvalBeans.get(position).getMobileNo());
            }else
            {
                ((TextView)row.findViewById(R.id.txtheading)).setText(approvalBeans.get(position).getPurposeName());
            }
            try {
                if (approvalBeans.get(position).getVisitorName().equalsIgnoreCase("Not added"))
                {
//                    row.findViewById(R.id.txtsubheading).settee(View.GONE);
                    ((TextView) row.findViewById(R.id.txtsubheading)).setText("Visitor");
                }else {
                    row.findViewById(R.id.txtsubheading).setVisibility(View.VISIBLE);
                    String name = approvalBeans.get(position).getVisitorName();
                    name = name.substring(0, 1).toUpperCase() + name.substring(1);
                    ((TextView) row.findViewById(R.id.txtsubheading)).setText(name);
                }
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            Date startdate=converttodate(approvalBeans.get(position).getStartDate());
            if (startdate!=null)
            {
               // String next = "<font color='#1976D2'>From : </font>";
//                txtstartdate.setText(Html.fromHtml(next +showDate(startdate)));
                txtstartdate.setText(showDate(startdate)+"");

            }

            Date enddate=converttodate(approvalBeans.get(position).getEndDate());
            if (enddate!=null)
            {
//                String next = "<font color='#1976D2'>To : </font>";
//                txtenddate.setText(Html.fromHtml(next +showDate(enddate)));
                txtenddate.setText(showDate(enddate)+"");
            }
//            txtenddate.setText(approvalBeans.get(position).getEndDate());
            imgdelete.setTag(approvalBeans.get(position).getId());
            String beanapproval=new Gson().toJson(approvalBeans.get(position));
            ideditaapproval.setTag(beanapproval);
            imgdelete.setOnClickListener(v1 -> {


//                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
//                builder1.setMessage("Do you want to delete?");
//                builder1.setCancelable(true);
//
//                builder1.setPositiveButton(
//                        "Yes",
//                        (dialog, id) -> {
//                            dialog.cancel();
//                            int id1= (int) v.getTag();
//                            getCallFroDelete(id1);
//                        });
//
//                builder1.setNegativeButton(
//                        "No",
//                        (dialog, id) -> dialog.cancel());
//
//                AlertDialog alert11 = builder1.create();
//                alert11.show();




                //New Pop up alert
                android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.custum_alert_dialog, null);
                dialogBuilder.setView(dialogView);
                TextView txtalertheading = dialogView.findViewById(R.id.txtalertheading);
                TextView txtalertsubheading = dialogView.findViewById(R.id.txtalertsubheading);

                TextView btnegative = dialogView.findViewById(R.id.btnegative);
                TextView btnpositive = dialogView.findViewById(R.id.btnpositive);

                txtalertheading.setText("Pre Approval");
                txtalertsubheading.setText("Do you want to delete?");

                btnegative.setVisibility(View.VISIBLE);


                final android.app.AlertDialog b = dialogBuilder.create();
                b.setCanceledOnTouchOutside(false);
                b.setCancelable(false);
                b.show();

                btnpositive.setText("Yes");
                btnegative.setText("No");
                btnpositive.setOnClickListener(v -> {
                    b.dismiss();
//                        if (cc.isConnectingToInternet()) {
                        int id1= (int) v1.getTag();
                        getCallFroDelete(id1);
//                        }
//                        else{
//                            CommonMethods.showToastError(context, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
//                        }
                });
                btnegative.setOnClickListener(v2 -> b.dismiss());
                //end of alert

            });
            ideditaapproval.setOnClickListener(v->{
                String bean= (String) v.getTag();
                Intent i=new Intent(context, PreApprovalActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                i.putExtra("foredit",1);
                i.putExtra("preaaprovalbean",bean);
                ((Activity) context).startActivityForResult(i, REQUEST_PREAPPROVAL);
            });
//            ((TextView) row.findViewById(R.id.invitationDate)).setText(approvalBeans.get(position).getVisitorName());
//            ((TextView)row.findViewById(R.id.guestCount)).setText(invitations.get(position).getGuestCount()+"");
//            if(invitations.get(position).getDescription().trim().length() == 0){
//                row.findViewById(R.id.addInfo).setVisibility(View.GONE);
//            } else {
//                ((TextView) row.findViewById(R.id.addInfo)).setText(invitations.get(position).getDescription());
//            }
//            try {
//                if (invitations.get(position).getGuestCount() == 1) {
//                    ((TextView) row.findViewById(R.id.invConName)).setText(invitations.get(position).getContactgroup().get(0).getContactName());
//                    ((TextView) row.findViewById(R.id.invConMobile)).setText(invitations.get(position).getContactgroup().get(0).getMobileNo());
//                } else {
//                    row.findViewById(R.id.addContactLL).setVisibility(View.GONE);
//                }
//            } catch (Exception e){
//                row.findViewById(R.id.addContactLL).setVisibility(View.GONE);
            }
//        }
        return row;
    }

    public Date converttodate(String datestr)
    {
        Date date=null;
        if (datestr!=null &&!datestr.isEmpty()) {
           String sd = datestr.substring(0, datestr.length() - 2);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {

                date  = formatter.parse(sd);
                System.out.println(date);
                System.out.println(formatter.format(date));

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return date;
    }

    public String showDate(Date date)
    {
        int dayOfMonth=date.getDate();
        int monthOfYear=date.getMonth();
        int year=date.getYear();
//        Log.e("day",dayOfMonth+" ");
//        Log.e("monthOfYear",monthOfYear+" ");
//        Log.e("year",year+" ");
       String strDateAndTime = convertTo2D(dayOfMonth) + " " + months[monthOfYear] + " " + String.valueOf(year).substring(1);

        String strTime =  strDateAndTime+" "+updateTime(date.getHours() ,date.getMinutes());
        return strTime;
    }
    public String convertTo2D(int num){
        return String.format("%02d", num);

    }


    // Used to convert 24hr format to 12hr format with AM/PM values
    private String updateTime(int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        String hrs = "";
        if (hours < 10)
            hrs = "0" + hours;
        else
            hrs = String.valueOf(hours);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hrs).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
        return aTime;
    }
}
