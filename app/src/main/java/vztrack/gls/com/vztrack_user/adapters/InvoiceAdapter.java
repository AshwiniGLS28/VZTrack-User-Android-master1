package vztrack.gls.com.vztrack_user.adapters;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clevertap.android.sdk.CleverTapAPI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.beans.Srvrsp;
import vztrack.gls.com.vztrack_user.fragment.InvoiceFragment;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.DataViewHolder> {
    InvoiceFragment context;
    String strInvoiceName, strStatus, strAmount, strCreatedDate, strBillDate, strDueDate;
    ArrayList<Srvrsp> invoices;
    CheckConnection cc;

    public class DataViewHolder extends RecyclerView.ViewHolder  {
        public TextView invoice_name, tvStatus, tvCreatedDate, tvBillDate, tvDueDate, tvAmount;
        public CardView cardView;
        public TextView payButton;

        public DataViewHolder(View view) {
            super(view);
            invoice_name = (TextView) view.findViewById(R.id.invoice_name);
            tvStatus = (TextView) view.findViewById(R.id.invoice_status);
            tvCreatedDate = (TextView) view.findViewById(R.id.created_date);
            tvDueDate = (TextView) view.findViewById(R.id.due_date);
            tvBillDate = (TextView) view.findViewById(R.id.bill_date);
            tvAmount = (TextView) view.findViewById(R.id.amount);
            cardView = (CardView) view.findViewById(R.id.card_view);
            payButton = (TextView) view.findViewById(R.id.btnPay);
        }
    }

    public InvoiceAdapter(InvoiceFragment con, ArrayList<Srvrsp> invoices) {
        this.context = con;
        this.invoices = invoices;
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        cc = new CheckConnection(context.getActivity());
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invoice_row, parent, false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DataViewHolder holder, int position) {


        strInvoiceName = invoices.get(position).getCycle_name().trim();
        strCreatedDate = invoices.get(position).getSent_date().trim();
        strBillDate = invoices.get(position).getBill_date().trim();
        strDueDate = invoices.get(position).getDue_date().trim();
        if (invoices.get(position).getAmount()!=null)
        strAmount = invoices.get(position).getAmount().trim();
        strStatus = invoices.get(position).getStatus().trim();

        if(strStatus.equalsIgnoreCase("submitted") || strStatus.equalsIgnoreCase("initiated")){
            holder.payButton.setText("Pay");
//            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.colorButton));
        }else if(strStatus.equalsIgnoreCase("paid online") || strStatus.equalsIgnoreCase("paid offline")){
            holder.payButton.setText("View");
//            holder.ca.rdView.setBackgroundColor(context.getResources().getColor(R.color.green));
        }else if(strStatus.equalsIgnoreCase("rejected") || strStatus.equalsIgnoreCase("failed")){
            if(strStatus.equalsIgnoreCase("rejected")){
                holder.payButton.setText("View");
            }else{
                holder.payButton.setText("Pay");
            }
//            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.red));
        }

        holder.invoice_name.setText(strInvoiceName);
        holder.tvCreatedDate.setText(ChnageDateFormat(strCreatedDate));
        holder.tvDueDate.setText(ChnageDateFormat(strDueDate));
        holder.tvAmount.setText(strAmount);
        holder.tvBillDate.setText(ChnageDateFormat(strBillDate));
        if(strStatus.equalsIgnoreCase("paid online") || strStatus.equalsIgnoreCase("paid offline")){
            holder.tvStatus.setText("Settled");
        }else if(strStatus.equalsIgnoreCase("rejected")){
            holder.tvStatus.setText(strStatus);
        }else if(strStatus.equalsIgnoreCase("failed")){
            holder.tvStatus.setText(strStatus);
        }else{
            holder.tvStatus.setText("Unsettled");
        }

        holder.payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String invoice_id = invoices.get(position).getInvoice_id();
                new GetData(context, CallFor.INVOICE_URL, invoice_id).execute();
                // CleverTap Event
                CleverTapAPI cleverTap = CleverTapAPI.getDefaultInstance(context.getActivity());
                HashMap<String, Object> action = new HashMap<String, Object>();
                action.put(Events.event_key_society_name, SheredPref.getSociety_Name(context.getActivity()));
                action.put(Events.event_key_flat_number, SheredPref.getFlat_No(context.getActivity()));
                action.put(Events.event_invoice_action, holder.payButton.getText());
                cleverTap.pushEvent(Events.event_invoice, action);
            }
        });

    }

    @Override
    public int getItemCount() {
        return invoices.size();
    }


    private String ChnageDateFormat(String strDate){
        String formatedDate = null;
        SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");
        try {
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dt.parse(strDate);

            SimpleDateFormat dt1 = new SimpleDateFormat("dd MMMM yyyy");
            formatedDate = dt1.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatedDate;
    }

}