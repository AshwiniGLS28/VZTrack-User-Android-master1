//package vztrack.gls.com.vztrack_user.adapters;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//
//import vztrack.gls.com.vztrack_user.R;
//import vztrack.gls.com.vztrack_user.beans.DrawerConfig;
//
//public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemViewHolder> {
//
//    class ItemViewHolder extends RecyclerView.ViewHolder {
//        private TextView itemLabel;
//        private ImageView imgmenu,ic_notification;
//        LinearLayout lldrawergrid;
//
//        public ItemViewHolder(View itemView) {
//            super(itemView);
//            itemLabel = (TextView) itemView.findViewById(R.id.item_label);
//            imgmenu=itemView.findViewById(R.id.imgmenu);
//            ic_notification=itemView.findViewById(R.id.notification_indicator);
//            lldrawergrid=itemView.findViewById(R.id.lldrawergrid);
//        }
//    }
//
//    private Context context;
//    private ArrayList<DrawerConfig> arrayList;
//
//    public ItemRecyclerViewAdapter(Context context, ArrayList<DrawerConfig> arrayList) {
//        this.context = context;
//        this.arrayList = arrayList;
//    }
//
//    @Override
//    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_row_layout, parent, false);
//        return new ItemViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(ItemViewHolder holder, int position) {
//        holder.itemLabel.setText(arrayList.get(position).getMenuname());
//        holder.imgmenu.setImageResource(arrayList.get(position).getImage());
//        if (position==1)
//        holder.ic_notification.setVisibility(View.VISIBLE);
//        else
//            holder.ic_notification.setVisibility(View.INVISIBLE);
//
//        if (!arrayList.get(position).isIsshow())
//        {
//            holder.lldrawergrid.setAlpha((float) 0.4);
//
//        }else
//            holder.lldrawergrid.setAlpha((float)1);
//    }
//
//    @Override
//    public int getItemCount() {
//        return arrayList.size();
//    }
//
//
//}