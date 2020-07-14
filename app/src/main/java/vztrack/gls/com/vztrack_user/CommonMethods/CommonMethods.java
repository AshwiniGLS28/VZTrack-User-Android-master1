package vztrack.gls.com.vztrack_user.CommonMethods;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;

import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.BaseActivity;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.beans.DrawerConfig;
import vztrack.gls.com.vztrack_user.beans.DrawerConfigBean.ConfigBean;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

public class CommonMethods {

    public static void showToastSuccess(Context context,String msg)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_toast, null);
        LinearLayout toast_layout_root=layout.findViewById(R.id.toast_layout_root);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(msg);
        text.setTextColor(context.getResources().getColor(R.color.colorWhite));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public static void showToastError(Context context,String msg)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_toast, null);
        LinearLayout toast_layout_root=layout.findViewById(R.id.toast_layout_root);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(msg);
        text.setTextColor(context.getResources().getColor(R.color.colorWhite));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public static ArrayList<ConfigBean> sortDrawerMenu(ArrayList<ConfigBean> drawerConfigs)
    {
        ArrayList<ConfigBean> AllConfigs=new ArrayList<>();
        AllConfigs.addAll(drawerConfigs);
        ArrayList<ConfigBean> frequentlyUsedConfigs=new ArrayList<>();
        Collections.sort(AllConfigs);

        for (int i=0;i<AllConfigs.size();i++)
        {
            if (frequentlyUsedConfigs.size()<3)
            {
               if (AllConfigs.get(i).isIsshow())
               {
                   frequentlyUsedConfigs.add(AllConfigs.get(i));

               }
            }else {
                break;
            }
        }
        return frequentlyUsedConfigs;
    }

    public static void updatefrequentlyaddedcount(Context context, ConfigBean drawerConfig)
    {
        ArrayList<ConfigBean> drawerConfigs= new Gson().fromJson(SheredPref.getFrequentlyUsedMenuList(context), new TypeToken<ArrayList<ConfigBean>>(){}.getType());

        for (int i=0; i< drawerConfigs.size();i++)
        {
            if (drawerConfigs.get(i).getName().equalsIgnoreCase(drawerConfig.getName()))
            {
                ConfigBean drawerConfig1=new ConfigBean();
                drawerConfig1.setName(drawerConfigs.get(i).getName());
                drawerConfig1.setTitle(drawerConfigs.get(i).getTitle());
                drawerConfig1.setMenu_image(drawerConfigs.get(i).getMenu_image());
                drawerConfig1.setIsshow(drawerConfigs.get(i).isIsshow());
                drawerConfig1.setNotificationcount(drawerConfigs.get(i).isNotificationcount());

                int count=drawerConfigs.get(i).getFrequentUsedCount();
                Log.e("BeforeCount: ", count+"");
                count=count+1;
                Log.e("position: ", i+"");
                drawerConfig1.setFrequentUsedCount(count);
                drawerConfigs.set(i,drawerConfig1);
                Log.e("size of bean",drawerConfigs.size()+"--");

            }
        }

        ArrayList<ConfigBean> configBeanArrayList=new ArrayList<>();
        configBeanArrayList.addAll(drawerConfigs);
//        drawerConfigArrayList.setDrawerConfigArrayList(MainActivity.drawerConfigs);
        SheredPref.setFrequentlyUsedMenuList(context,configBeanArrayList);

//        for (int i=0; i< MainActivity.drawerConfigs.size();i++)
//        {
//            if (MainActivity.drawerConfigs.get(i).getMenuname().equalsIgnoreCase(drawerConfig.getMenuname()))
//            {
//               Log.e("count ",MainActivity.drawerConfigs.get(i).getMenuname()+"");
//                Log.e("count ",MainActivity.drawerConfigs.get(i).getFrequentUsedCount()+"");
//
//            }
//        }

    }

//    public static ConfigBean updateBean(ConfigBean drawerConfig, boolean isNotificationCountUpdate ,boolean isvisibilityiupdate)
//    {
//
//        ConfigBean newlyDrawerconfig=new ConfigBean();
//
//        newlyDrawerconfig.setIsshow(isvisibilityiupdate);
//        newlyDrawerconfig.setFrequentUsedCount(drawerConfig.getFrequentUsedCount());
//        newlyDrawerconfig.setImage(drawerConfig.getImage());
//        newlyDrawerconfig.setNotificationcount(isNotificationCountUpdate);
//        newlyDrawerconfig.setName(drawerConfig.getName());
//        return newlyDrawerconfig;
//
//    }

    public static void updateConfigBean(ConfigBean drawerConfig ,Context context)
    {
      ArrayList<ConfigBean>  drawerConfigArrayList= new Gson().fromJson(SheredPref.getFrequentlyUsedMenuList(context), new TypeToken<ArrayList<ConfigBean>>(){}.getType());
        ConfigBean newlyDrawerconfig=null;


        int noticecount= SheredPref.getNoticeConut(context);
        int complanitcount=  SheredPref.getComplaintConut(context);
        int messagecount= SheredPref.getMessageCount(context);
        int rainbowCount= SheredPref.getRainbowCount(context);
        for (int i=0;i<drawerConfigArrayList.size();i++)
      {
          if (drawerConfigArrayList.get(i).getName().equalsIgnoreCase(context.getResources().getString(R.string.rainbowresidents)))
          {
              if (rainbowCount!=0) {
                  newlyDrawerconfig = new ConfigBean();
                  newlyDrawerconfig.setFrequentUsedCount(drawerConfig.getFrequentUsedCount());
                  newlyDrawerconfig.setImage(drawerConfig.getImage());
                  newlyDrawerconfig.setName(drawerConfig.getName());
                  newlyDrawerconfig.setNotificationcount(true);
                  newlyDrawerconfig.setTitle(drawerConfig.getTitle());
                  drawerConfigArrayList.set(i,newlyDrawerconfig);
              }

          }
          if (drawerConfigArrayList.get(i).getName().equalsIgnoreCase(context.getResources().getString(R.string.complaintregister)))
          {
              if (complanitcount!=0) {
                  newlyDrawerconfig = new ConfigBean();
                  newlyDrawerconfig.setFrequentUsedCount(drawerConfig.getFrequentUsedCount());
                  newlyDrawerconfig.setImage(drawerConfig.getImage());
                  newlyDrawerconfig.setName(drawerConfig.getName());
                  newlyDrawerconfig.setNotificationcount(true);
                  newlyDrawerconfig.setTitle(drawerConfig.getTitle());
                  drawerConfigArrayList.set(i,newlyDrawerconfig);
              }

          }
          if (drawerConfigArrayList.get(i).getName().equalsIgnoreCase(context.getResources().getString(R.string.message)))
          {
              if (messagecount!=0) {
                  newlyDrawerconfig = new ConfigBean();
                  newlyDrawerconfig.setFrequentUsedCount(drawerConfig.getFrequentUsedCount());
                  newlyDrawerconfig.setImage(drawerConfig.getImage());
                  newlyDrawerconfig.setName(drawerConfig.getName());
                  newlyDrawerconfig.setNotificationcount(true);newlyDrawerconfig.setTitle(drawerConfig.getTitle());
                  drawerConfigArrayList.set(i,newlyDrawerconfig);

              }

          }
          if (drawerConfigArrayList.get(i).getName().equalsIgnoreCase(context.getResources().getString(R.string.noticenminutes)))
          {
              if (noticecount!=0) {
                  newlyDrawerconfig = new ConfigBean();
                  newlyDrawerconfig.setFrequentUsedCount(drawerConfig.getFrequentUsedCount());
                  newlyDrawerconfig.setImage(drawerConfig.getImage());
                  newlyDrawerconfig.setName(drawerConfig.getName());
                  newlyDrawerconfig.setNotificationcount(true);
                  newlyDrawerconfig.setTitle(drawerConfig.getTitle());
                  drawerConfigArrayList.set(i,newlyDrawerconfig);
              }

          }
      }

//        return newlyDrawerconfig;

    }

//    public void RemoveDuplicates(Context context)
//    {
//        public boolean equals(Object object2) {
//        return object2 instanceof MyClass && a.equals(((MyClass)object2).a);
//    }
//    }
}
