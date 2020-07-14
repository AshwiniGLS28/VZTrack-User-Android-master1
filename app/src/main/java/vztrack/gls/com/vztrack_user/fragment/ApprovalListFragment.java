package vztrack.gls.com.vztrack_user.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import java.util.ArrayList;

import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.activity.BaseFragment;
import vztrack.gls.com.vztrack_user.activity.PreApprovalActivity;
import vztrack.gls.com.vztrack_user.adapters.Preapproval.ApprovalListAdapter;
import vztrack.gls.com.vztrack_user.beans.PreApprovalBean.ApprovalBean;
import vztrack.gls.com.vztrack_user.beans.PreApprovalBean.PreApprovalListBean;
import vztrack.gls.com.vztrack_user.profile.UserBean;
import vztrack.gls.com.vztrack_user.responce.LoginResponse;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.PostData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;

import static vztrack.gls.com.vztrack_user.activity.MainActivity.REQUEST_PREAPPROVAL;

public class ApprovalListFragment extends BaseFragment {
    ListView preapprovallist;
    LinearLayout noDataLL,noImageDataLayout;
    TextView message;//,txtnopreapprovallist;
    ProgressBar progress_bar;
    ArrayList<ApprovalBean> preapprovalBeans;//=new ArrayList<>();
    ApprovalListAdapter adapter;
    static CheckConnection cc;
    static int callfor=0;
    static ApprovalBean approvalBean;
    TextView nodatatext;
//    TextView txtstartdate,txtenddate;
    public static ApprovalListFragment newInstance(int page, String title) {
        ApprovalListFragment fragmentFirst = new ApprovalListFragment();
//        Bundle args = new Bundle();
////        args.putInt("someInt", page);
////        args.putString("someTitle", title);
////        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }
    public static BaseFragment context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.approvallistfragment, null);
        setHasOptionsMenu(true);
        FloatingActionButton fab = root.findViewById(R.id.idshowpreaaproval);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), PreApprovalActivity.class);
            intent.putExtra("CALL_FOR","NEW");
//            startActivity(intent);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
           context.startActivityForResult(intent, REQUEST_PREAPPROVAL);
        });
        context=this;
        cc = new CheckConnection(getActivity());
        preapprovallist = root.findViewById(R.id.preapprovallist);
        noDataLL = root.findViewById(R.id.noDataLL);
        message = root.findViewById(R.id.messageText);
        progress_bar = root.findViewById(R.id.progress_bar);
        noImageDataLayout=root.findViewById(R.id.noImageDataLayout);
        nodatatext=root.findViewById(R.id.nodatatext);

        preapprovalBeans = new ArrayList<>();
//        txtnopreapprovallist=root.findViewById(R.id.txtnopreapprovallist);

//        invitationList.setOnItemClickListener(new ListView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(invitations.get(position) == null){
//                    return;
//                }
//                Intent intent = new Intent(getActivity(), InvitationActivity.class);
//                intent.putExtra("CALL_FOR","DETAIL");
//                intent.putExtra("PURPOSE",invitations.get(position).getPurpose());
//                intent.putExtra("DATE",invitations.get(position).getInvitedDate());
//                intent.putExtra("ADD_INFO",invitations.get(position).getDescription());
//                intent.putExtra("ID",invitations.get(position).getInvitationId());
//                startActivity(intent);
//            }
//        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.e("call","get approval list");
        if(cc.isConnectingToInternet()){
            new GetData(this, CallFor.GETPREAPPROVALIST, "").execute();
            callfor=0;

        }else
        {
            CommonMethods.showToastError(getActivity(), "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
            return;
        }
    }

    @Override
    public void onGetResponse(String response, String callFor) {
//        Log.e("sever call for",callFor+"");
//        Log.e("response",response+"");
//        Log.e("callFor",CallFor.GETPREAPPROVALIST+"");
        if(callFor.equals(CallFor.GETPREAPPROVALIST)){
            PreApprovalListBean preApprovalListBean = new Gson().fromJson(response, PreApprovalListBean.class);
//            Log.e("preApprovalListBean",new Gson().toJson(preApprovalListBean)+"");
            if(preApprovalListBean.getCode().equals("SUCCESS")){
                try {
//                Log.e("list",new Gson().toJson(preApprovalListBean.getPreApprovalList())+"--");
                    preapprovalBeans=new ArrayList<>();
                    if (preApprovalListBean.getPreApprovalList().size()!=0)
                    {

                        preapprovalBeans.addAll(preApprovalListBean.getPreApprovalList());
                        preapprovallist.setVisibility(View.VISIBLE);
//                        txtnopreapprovallist.setVisibility(View.GONE);

                    }else
                    {
//                        preapprovalBeans.addAll()
                        preapprovallist.setVisibility(View.GONE);
//                        txtnopreapprovallist.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e){

                }
                setApprovalList();
            }else if (preApprovalListBean.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                UserBean userBean = new UserBean();
                userBean.setUser_name(SheredPref.getUsername(getActivity()));
                userBean.setUser_password(SheredPref.getPassword(getActivity()));
                new PostData(new Gson().toJson(userBean), this, CallFor.LOGIN).execute();
            }
        }
        if(callFor.equals(CallFor.DELETEPREAPPROVAL)){
            PreApprovalListBean preApprovalListBean = new Gson().fromJson(response, PreApprovalListBean.class);
            if(preApprovalListBean.getCode().equals("SUCCESS")){
//                Toast.makeText(getActivity(),"Deleted successfully",Toast.LENGTH_SHORT).show();
                CommonMethods.showToastSuccess(getActivity(), "Deleted successfully");//, Toast.LENGTH_SHORT, true).show();
                preapprovalBeans.clear();
                new GetData(this, CallFor.GETPREAPPROVALIST, "").execute();
                callfor=0;
            }else if (preApprovalListBean.getCode().equalsIgnoreCase("NEED_LOGIN")) {
                UserBean userBean = new UserBean();
                userBean.setUser_name(SheredPref.getUsername(getActivity()));
                userBean.setUser_password(SheredPref.getPassword(getActivity()));
                new PostData(new Gson().toJson(userBean), this, CallFor.LOGIN).execute();
            }
        }

        if (callFor == CallFor.LOGIN) {
            LoginResponse loginResponse = new Gson().fromJson(response, LoginResponse.class);
            if (loginResponse!=null)
            {
                if (loginResponse.getCode().equalsIgnoreCase("SUCCESS"))
                {
                    if (callfor==0)
                    {
                        preapprovalBeans.clear();
                        new GetData(this, CallFor.GETPREAPPROVALIST, "").execute();
                        callfor=0;

                    }else if (callfor==1)
                    {
                        if (approvalBean!=null) {
                            new PostData(new Gson().toJson(approvalBean), context, CallFor.ADD_PREAPPROVAL).execute();
                            callfor = 1;
                        }else
                        {
//                            Toast.makeText(getActivity(),"Please perfom action again",Toast.LENGTH_SHORT).show();
                            CommonMethods.showToastError(getActivity(), "Please perfom action again");//, Toast.LENGTH_SHORT, true).show();
                            return;
                        }
                    }
                }
            }
        }
    }
    public void setApprovalList(){
        Log.e("setApprovalList","setApprovalList");
        if(preapprovalBeans.size()>0){
            if (preapprovalBeans.get(preapprovalBeans.size() - 1) != null) {
                preapprovalBeans.add(null);
            }
//            Log.e("preapprovalBeans",new Gson().toJson(preapprovalBeans)+"--");
            preapprovallist.setVisibility(View.VISIBLE);
            adapter = new ApprovalListAdapter(getActivity(), R.layout.approvallistlayout,preapprovalBeans);
            preapprovallist.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
//            preapprovallist.setVisibility(View.VISIBLE);
            noDataLL.setVisibility(View.GONE);
            noImageDataLayout.setVisibility(View.GONE);
        } else {
            Log.e("no","list");
            noDataLL.setVisibility(View.GONE);
            progress_bar.setVisibility(View.GONE);
            noImageDataLayout.setVisibility(View.VISIBLE);
            nodatatext.setText("You have no pre approved\nvisitor.");
        }
    }
    public static void getCallFroDelete(int ID)
    {


//        Log.e("ID",ID+"");

        if(cc.isConnectingToInternet()){
            approvalBean=new ApprovalBean();
            approvalBean.setId(ID);
            new GetData(context, CallFor.DELETEPREAPPROVAL,ID+"").execute();
            callfor=1;

        }else
        {
//            Toast.makeText(this,"Please check internet connection",Toast.LENGTH_SHORT).show();
            return;
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.e("onCreateOptionsMenu","onCreateOptionsMenu");
        menu.clear();
//        inflater.inflate(R.menu.main, menu);
//        setHasOptionsMenu(true);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
//        searchView.setVisibility(View.GONE);
    }
}
