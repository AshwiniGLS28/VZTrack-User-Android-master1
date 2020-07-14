package vztrack.gls.com.vztrack_user.fragment;

/**
 * Created by sandeep on 14/3/16.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;


import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.CustumView.CustomButton;
import vztrack.gls.com.vztrack_user.CustumView.CustomTextView;
import vztrack.gls.com.vztrack_user.activity.BaseFragment;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.adapters.Complain_RecyclerViewAdapter;
import vztrack.gls.com.vztrack_user.adapters.Preapproval.MyAdapter;
import vztrack.gls.com.vztrack_user.beans.ComplainsBean;
import vztrack.gls.com.vztrack_user.beans.ProfilePhotoBean;
import vztrack.gls.com.vztrack_user.profile.FamilyBean;
import vztrack.gls.com.vztrack_user.profile.UserBean;
import vztrack.gls.com.vztrack_user.responce.ComplainResponceBean;
import vztrack.gls.com.vztrack_user.responce.LoginResponse;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.DbHelper;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.GetData;
import vztrack.gls.com.vztrack_user.utils.PermissionConstant;
import vztrack.gls.com.vztrack_user.utils.Permissions;
import vztrack.gls.com.vztrack_user.utils.PostData;
import vztrack.gls.com.vztrack_user.utils.SheredPref;
import vztrack.gls.com.vztrack_user.utils.UtilityMethods;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

import static com.android.volley.VolleyLog.TAG;
import static vztrack.gls.com.vztrack_user.activity.MainActivity.complian_result;
import static vztrack.gls.com.vztrack_user.activity.MainActivity.mainActivity;
import static vztrack.gls.com.vztrack_user.activity.MainActivity.pageNo;

public class ComplaintFragment extends BaseFragment {
    private static int PICK_IMAGE_REQUEST = 1;
    private final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 1;
    Context context;
    public static RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList results;
    DbHelper dbHelper;
    String img_URL, heading, description, noticeStartdate, noticeEndDate;
    CheckConnection cc;
    public static SwipeRefreshLayout mSwipeRefreshLayout;
    private Button fab;
    public static LinearLayout NoDataLayout;
    public static TextView NoDataText;
    String strSelectProvider = "Select Category";
    String selectedItem, date;
    int position;
    Dialog dialog;
    ImageView imgcomaplaintView;
    public static String encodedImageUrl = null;

    public ComplaintFragment() {
    }

    String[] list = new String[1];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_complaint, null);
        dbHelper = new DbHelper(getActivity());
        cc = new CheckConnection(getActivity());
        String title = UtilityMethods.getTitleFromMenuJson(getResources().getString(R.string.complaintregister), getActivity());
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);
        setHasOptionsMenu(true);
        context = getActivity();
        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.activity_main_swipe_refresh_layout);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.complain_recycler_view);
        fab = (Button) root.findViewById(R.id.fab);
        NoDataLayout = (LinearLayout) root.findViewById(R.id.NoDataLayout);
        NoDataText = (TextView) root.findViewById(R.id.NoDataText);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        SheredPref.setExecute(context, "Not");
        SheredPref.setComplaintCount(getActivity(), 0);             // To maintain complaint count in drawer


        SharedPreferences prefs = context.getSharedPreferences("COMP_LIST", 0);
        int size = prefs.getInt("comp_array_size", 0);
        list = new String[size + 1];
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                list[i] = strSelectProvider;
                list[i + 1] = prefs.getString("comp_array_" + i, null);
            } else {
                list[i + 1] = prefs.getString("comp_array_" + i, null);
            }
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (cc.isConnectingToInternet()) {
                    complian_result.clear();
                    pageNo = 0;
                    String extendedUrl = "?pageNo=" + pageNo + "&newVersion=true";
                    SheredPref.setRun(mainActivity, "DONT RUN");
                    new GetData(ComplaintFragment.this, CallFor.GET_COMPLAIN, extendedUrl).execute();
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    CommonMethods.showToastError(context, "Unable to refresh, Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        fab.setOnClickListener(view -> {
            if (cc.isConnectingToInternet()) {
                dialog = new Dialog(context);
                dialog.setCancelable(true);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_add_complain);
                Spinner comp_category_spinner = dialog.findViewById(R.id.spinner_comp_providers_list);
                final EditText description = (EditText) dialog.findViewById(R.id.description);
                final TextView counter = (TextView) dialog.findViewById(R.id.tvCounterDesc);
                Button fancyButton = (CustomButton) dialog.findViewById(R.id.btn_Add_Comp);

                ImageView imgcomaplaint = dialog.findViewById(R.id.imgcomaplaint);
                imgcomaplaintView = dialog.findViewById(R.id.imgcomaplaintView);

                TextView titleactivity = dialog.findViewById(R.id.title);
                ImageView backpress = dialog.findViewById(R.id.backpress);
                imgcomaplaint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        browsePhotoForcomaplint();
                    }
                });
                titleactivity.setText("New Complaint");
                backpress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.hide();
                    }
                });
                ArrayList<String> listtoadapter = setPurposeSpinner(list);
                if(listtoadapter.isEmpty()){
                    new GetData(this, CallFor.GET_COMPLAIN_CATEGORY, "").execute();
                }
                MyAdapter adapter1 = new MyAdapter(context, R.layout.preapprovalspinner, listtoadapter);
                comp_category_spinner.setAdapter(adapter1);
                dialog.show();
                comp_category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                        position = pos;
                        try {
                            selectedItem = adapterView.getItemAtPosition(pos).toString();
                        }catch (Exception ex){
                            Log.e(TAG, ""+ex);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> adapterView) {
                        return;
                    }
                });

                description.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        counter.setText(editable.toString().length() + " / max 500");
                    }
                });

                fancyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (position == 0) {
                            CommonMethods.showToastError(getActivity(), "Please select category");//, Toast.LENGTH_SHORT, true).show();
                        } else if (description.getText().toString().trim().equals("")) {
                            CommonMethods.showToastError(getActivity(), "Description should not be blank");//, Toast.LENGTH_SHORT, true).show();
                        } else {
                            CleverTap.cleverTap_Record_Event(getActivity(), Events.event_send_complaint_button);
                            ComplainsBean complainsBean = new ComplainsBean();
                            complainsBean.setCategory(list[position]);
                            complainsBean.setNewVersion(true);
                            complainsBean.setDescription(description.getText().toString());
                            complainsBean.setSocity_id(Integer.parseInt(SheredPref.getSocietyId(context)));
                            complainsBean.setFamily_id(Integer.parseInt(SheredPref.getSocietyId(context)));
                            if (encodedImageUrl != null) {
                                complainsBean.setComplainPhoto(encodedImageUrl);
                            } else {
//                                complainsBean.setComplainPhoto("NOT ADDED");
                            }
                            new PostData(new Gson().toJson(complainsBean), mainActivity, CallFor.ADD_COMPLAIN).execute();
                            dialog.hide();
                        }
                    }
                });

                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.setOnKeyListener((dialog, keyCode, event) -> {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
                        dialog.dismiss();
                    return false;
                });
            } else {
                CommonMethods.showToastError(getActivity(), "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
            }
        });
        return root;
    }

    private ArrayList<String> setPurposeSpinner(String[] preApprovalPurposeBean) {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(preApprovalPurposeBean));
        return list;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    @Override
    public void onGetResponse(String response, String callFor) {
        Log.e("response", response + "--");
        if (callFor.equals(CallFor.GET_COMPLAIN_CATEGORY)) {
            MainActivity.complainResponceBean = new Gson().fromJson(response, ComplainResponceBean.class);
            if (MainActivity.complainResponceBean.getCode().equalsIgnoreCase("SUCCESS")) {
                ComplainResponceBean comp = new Gson().fromJson(response, ComplainResponceBean.class);
                comp.getSocietyComplainCategories();
                // Store the Provider List in SharedPreferences
                SharedPreferences prefs = context.getSharedPreferences("COMP_LIST", 0);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putInt("comp_array_size", comp.getSocietyComplainCategories().size());
                for (int i = 0; i < comp.getSocietyComplainCategories().size(); i++)
                    edit.putString("comp_array_" + i, comp.getSocietyComplainCategories().get(i));
                edit.commit();

                // get the provoder list from SharedPreference
                SharedPreferences prefs1 = context.getSharedPreferences("COMP_LIST", 0);
                int size = prefs1.getInt("comp_array_size", 0);
                list = new String[size + 1];
                for (int i = 0; i < size; i++) {
                    if (i == 0) {
                        list[i] = strSelectProvider;
                        list[i + 1] = prefs1.getString("comp_array_" + i, null);
                    } else {
                        list[i + 1] = prefs1.getString("comp_array_" + i, null);
                    }
                }
            } else {
                CommonMethods.showToastError(context, "Something went wrong, Please try again");
            }
        }

        if (callFor.equals(CallFor.LOGIN)) {
            LoginResponse loginResponse = new Gson().fromJson(response, LoginResponse.class);
            if (loginResponse.getCode().equals("SUCCESS")) {
                UtilityMethodsAndroid.updateSheredPreferenceValues(context, loginResponse);
                SheredPref.setPhotoURLOfUser(context, loginResponse.getFamily().getUserPhoto());
                SheredPref.setSocietyApproval(context, loginResponse.getFamily().isApproval());
                boolean isPrimaryUser = loginResponse.getFamily().isPrimary();
                SheredPref.setPrimary(context, isPrimaryUser);
                SheredPref.setPhotoURLOfUser(context, loginResponse.getFamily().getUserPhoto());
                SheredPref.setName(context, loginResponse.getFamily().getName());
                complian_result.clear();
                pageNo = 0;
                String extendedUrl = "?pageNo=" + pageNo + "&newVersion=true";
                SheredPref.setRun(mainActivity, "RUN");
                new GetData(this, CallFor.GET_COMPLAIN, extendedUrl).execute();
            }
        }
        if (callFor.equals(CallFor.GET_COMPLAIN)) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            date = df.format(c.getTime());
            SheredPref.setDateForCompApi(context, date);
            MainActivity.complainResponceBean = new Gson().fromJson(response, ComplainResponceBean.class);
            try {
                if (MainActivity.complainResponceBean.getCode().equalsIgnoreCase("SUCCESS")) {
                    try {
                        Log.e("comres", response);
                        complian_result.addAll(MainActivity.complainResponceBean.getComplains());
                        Log.e("complian_result", "come");
                        ComplaintFragment.mSwipeRefreshLayout.setRefreshing(false);
                        if (pageNo == 0) {
                            ComplaintFragment.mAdapter = new Complain_RecyclerViewAdapter(context, complian_result);
                            ComplaintFragment.mRecyclerView.setHasFixedSize(true);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                            ComplaintFragment.mRecyclerView.setLayoutManager(mLayoutManager);
                            ComplaintFragment.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            ComplaintFragment.mRecyclerView.setAdapter(ComplaintFragment.mAdapter);
                            ComplaintFragment.mAdapter.notifyDataSetChanged();
                        } else {
                            ComplaintFragment.mAdapter.notifyDataSetChanged();
                        }
                        if (pageNo != 0) {
                            ComplaintFragment.mRecyclerView.scrollToPosition(pageNo * 9);
                        }
                        if (((MainActivity.complainResponceBean == null || MainActivity.complainResponceBean.getComplains().size() == 0)) && pageNo == 0) {
                            ComplaintFragment.NoDataLayout.setVisibility(View.VISIBLE);
                            ComplaintFragment.NoDataText.setText("No complaints to display");
                            ComplaintFragment.mRecyclerView.setVisibility(View.GONE);
                        } else {
                            ComplaintFragment.NoDataLayout.setVisibility(View.GONE);
                            ComplaintFragment.mRecyclerView.setVisibility(View.VISIBLE);
                        }

                    } catch (Exception ex) {
                        Log.e(TAG, " Exception In Adapter Setting " + ex);
                    }
                } else if (MainActivity.complainResponceBean.getCode().equalsIgnoreCase("NEED_LOGIN")) {
//                    drawer.closeDrawer(GravityCompat.START);
                    UserBean userBean = new UserBean();
                    userBean.setUser_name(SheredPref.getUsername(context));
                    userBean.setUser_password(SheredPref.getPassword(context));
                    new PostData(new Gson().toJson(userBean), this, CallFor.LOGIN).execute();
                } else {
//                    drawer.closeDrawer(GravityCompat.START);
                    CommonMethods.showToastError(context, "Something went wrong, Please try again");
                }
            } catch (Exception ex) {
                Log.e("Exception Complain", " " + ex);
            }
        }
    }

    public void browsePhotoForcomaplint() {
        final BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_bottom_camera_and_gallary_picker);
        dialog.findViewById(R.id.cameraLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean cameraPermission = Permissions.askPermission(context, PermissionConstant.PERMISSION_CAMERA, PermissionConstant.REQ_CODE_CAMERA);
                if (cameraPermission) {
                    EasyImage.openCamera(ComplaintFragment.this, PICK_IMAGE_REQUEST);
                }
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.gallaryLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    askPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_READ_STORAGE);
                } else {
                    openGallary();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void askPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            // We dont have permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
        } else {
            openGallary();
        }
    }

    public void openGallary() {
        EasyImage.openGallery(ComplaintFragment.this, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.e("onActivityResult", "ComplaintFrgament");
        EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(), new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                Log.e("Exception : ", " " + e);
                CommonMethods.showToastError(context, "Unable to select image");//, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                Bitmap bitmap = UtilityMethods.imagePathToBitmap(imageFile.getAbsolutePath());
                bitmap = UtilityMethods.getResizedBitmap(bitmap, 720);
                try {
                    bitmap = UtilityMethods.rotateImageIfRequired(context, bitmap, imageFile.getAbsolutePath());
                } catch (IOException e) {
                    Log.e("Complaint", "EXCEPTION UPLOAD GETTING ROTATED IMAGE : " + e);
                }
                if (bitmap != null) {
                    encodedImageUrl = UtilityMethods.BitmapToString(bitmap);
                    Glide.with(context)
                            .load(bitmap)
                            .placeholder(R.drawable.ic_avatar)
                            .skipMemoryCache(false)
                            .thumbnail(0.4f)
                            .into(imgcomaplaintView);
                } else {
                    Log.e("Complaint", " BITMAP IS NULL NOT ABLE TO UPLOAD");
                    CommonMethods.showToastError(context, "Unable to upload profile photo");
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (cc.isConnectingToInternet()) {
            complian_result.clear();
            pageNo = 0;
            String extendedUrl = "?pageNo=" + pageNo + "&newVersion=true";
            SheredPref.setRun(mainActivity, "RUN");
            new GetData(this, CallFor.GET_COMPLAIN, extendedUrl).execute();
            new GetData(this, CallFor.GET_COMPLAIN_CATEGORY, "").execute();
        }
    }
}
