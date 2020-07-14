package vztrack.gls.com.vztrack_user.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.OpenableColumns;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn.pix.Pix;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.models.sort.SortingTypes;
import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.CustumView.CustomTextView;
import vztrack.gls.com.vztrack_user.R;
import vztrack.gls.com.vztrack_user.beans.NoticeBean;
import vztrack.gls.com.vztrack_user.responce.LoginResponse;
import vztrack.gls.com.vztrack_user.utils.CallFor;
import vztrack.gls.com.vztrack_user.utils.CheckConnection;
import vztrack.gls.com.vztrack_user.utils.CleverTap;
import vztrack.gls.com.vztrack_user.utils.Events;
import vztrack.gls.com.vztrack_user.utils.PermissionConstant;
import vztrack.gls.com.vztrack_user.utils.Permissions;
import vztrack.gls.com.vztrack_user.utils.PostDataForFile;
import vztrack.gls.com.vztrack_user.utils.SheredPref;
import vztrack.gls.com.vztrack_user.utils.UtilityMethodsAndroid;

public class SendNotice extends BaseActivity {

    //    Button startDate,endDate;
    boolean fileSelectionFlag;
    EditText heading, input_description;
    TextView counterHeading, counterDesc;
    ImageView imgSelectedImage;
    String fileName;
    private static int RESULT_LOAD_IMAGE = 1;
    TextView tvAddPhoto;
    CheckConnection cc;
    TextView tvFileName;//, selectedFileTitle;
    String fileExtension;
    File file;
    String filePath;
    TextView titleactivity;
    ImageView backpress;
    private ArrayList<String> docPaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notice);
        titleactivity = findViewById(R.id.title);
        backpress = findViewById(R.id.backpress);
        titleactivity.setText("Send Notice");
        backpress.setOnClickListener(v -> onBackPressed());
        cc = new CheckConnection(this);

        heading = (EditText) findViewById(R.id.input_heading);
        input_description = (EditText) findViewById(R.id.input_description);
        counterHeading = (TextView) findViewById(R.id.counterHeading);
        counterDesc = (TextView) findViewById(R.id.counterDesc);
        imgSelectedImage = (ImageView) findViewById(R.id.imgSelectedImage);
        tvAddPhoto = (TextView) findViewById(R.id.tvAddPhoto);
        tvFileName = (TextView) findViewById(R.id.fileName);

        heading.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                counterHeading.setText(editable.toString().length() + " / max 100");
            }
        });

        input_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                counterDesc.setText(editable.toString().length() + " / max 5000");
            }
        });
    }

    public void Send(View v) {
        Log.e("fileSelectionFlag : ", " " + fileSelectionFlag);
        CleverTap.cleverTap_Record_Event(this, Events.event_send_notice_button);
        String strHeading = heading.getText().toString().trim();
        String strDescription = input_description.getText().toString().trim();

        if (strHeading.equals("")) {
            CommonMethods.showToastSuccess(this, "Notice heading should not be blank");//, Toast.LENGTH_SHORT, true).show();
        } else if (!fileSelectionFlag && strDescription.equals("")) {
            CommonMethods.showToastSuccess(this, "Please add file to upload or description");//, Toast.LENGTH_SHORT, true).show();
        } else if (!cc.isConnectingToInternet()) {
            CommonMethods.showToastSuccess(this, "Please check internet connection");//, Toast.LENGTH_SHORT, true).show();
        } else {
            try {
                NoticeBean notice = new NoticeBean();
                notice.setNoticeHeading(strHeading);
                notice.setNoticeDesc(strDescription);
                notice.setSocityId(Integer.parseInt(SheredPref.getSocietyId(this)));
                notice.setPostById(Integer.parseInt(SheredPref.getFamilyId(this)));
                if (fileSelectionFlag) {
                    notice.setFileType(fileExtension);
                    notice.setFile(file);
                    notice.setFileName(fileName);
                } else {
                    notice.setFileType("");
                    notice.setFileName("");
                }
                new PostDataForFile(notice, this, CallFor.SEND_NOTICE).execute();

            } catch (Exception ex) {
                Log.e("Ex Send Notice", "" + ex);
            }
        }
    }

    public void askForPermission(View v) {
        browseDocument();
    }

    public void browseDocument() {

        try {
            String manufacturer = Build.MANUFACTURER;
            List<Intent> targets = new ArrayList<Intent>();
            Intent intent = new Intent();
            intent.setType("file/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);


            List<ResolveInfo> candidates;// = getPackageManager().queryIntentActivities(intent, 0);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                candidates =
                        getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            } else {
                candidates = getPackageManager().queryIntentActivities(intent, 0);
                ;
            }
            Log.e("candidates", new Gson().toJson(candidates) + "-");

            for (ResolveInfo candidate : candidates) {
                Log.e("candidate", new Gson().toJson(candidate) + "----");
                String packageName = candidate.activityInfo.packageName;
                Log.e("packageName", packageName + "----");
//                && !packageName.equals("com.google.android.apps.docs")
//                && !packageName.equals("com.android.documentsui")
//                if (!packageName.equals("com.google.android.apps.photos") && !packageName.equals("com.google.android.apps.plus")  && packageName.equals("com.google.android.apps.docs")&& packageName.equals("com.android.documentsui") ) {
                Log.e("packageName in ", "come");
                Intent iWantThis = new Intent();
                iWantThis.setType("*/*");
                iWantThis.setAction(Intent.ACTION_GET_CONTENT);
                iWantThis.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                iWantThis.setPackage(packageName);
                targets.add(iWantThis);
//                }
            }
            Intent chooser;
            Log.e("targets", new Gson().toJson(targets) + "----");
            Log.e("manufacturer", manufacturer + "----");
            if (manufacturer.equals("samsung")) {
                chooser = new Intent("com.sec.android.app.myfiles.PICK_DATA");
            } else {

                chooser = Intent.createChooser(targets.remove(0), "Select");
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, targets.toArray(new Parcelable[targets.size()]));
//                showFileChooser();
            }
            startActivityForResult(chooser, RESULT_LOAD_IMAGE);


        } catch (Exception ex) {
            Log.e("Exception ", " " + ex);
            CommonMethods.showToastSuccess(this, "No application to browse a file");//.show();
        }
    }

    public void openFile(View v) {
//        permissionFor = 1;
        boolean resultCamera = Permissions.askPermission(this, PermissionConstant.PERMISSION_EXTERNAL_STORAGE, PermissionConstant.REQ_CODE_EXTERNAL_STORAGE);
        if (resultCamera) {
            openFileChooser();
        }else{
            UtilityMethodsAndroid.ShowPermissionToast(SendNotice.this);
        }
    }

    public void openFileChooser() {
        String[] pdfs = {".pdf"};
        String[] doc = {".doc", ".docx", ".txt"};
        String[] image = {".png", ".jpeg", "jpg"};
        String[] excel = {".xlsx", ".xls"};
        String[] ppt = {".ppt",".pptx"};
        docPaths.clear();

        int maxCount = 1;
        FilePickerBuilder filePickerBuilder = new FilePickerBuilder();
        filePickerBuilder.setMaxCount(maxCount);
        filePickerBuilder.setSelectedFiles(docPaths);
        filePickerBuilder.setActivityTheme(R.style.LibAppTheme);
        filePickerBuilder.setActivityTitle("Please select doc");
        filePickerBuilder.addFileSupport("PDF", pdfs, R.drawable.pdf);
        filePickerBuilder.addFileSupport("Doc", doc,R.drawable.doc);
        filePickerBuilder.addFileSupport("Image", image);
        filePickerBuilder.addFileSupport("Excel", excel,R.drawable.xls);
        filePickerBuilder.addFileSupport("PPT", ppt,R.drawable.ppt);
        filePickerBuilder.enableDocSupport(false);
        filePickerBuilder.enableSelectAll(true);
        filePickerBuilder.sortDocumentsBy(SortingTypes.name);
        filePickerBuilder.enableImagePicker(true);
        filePickerBuilder.pickFile(this, RESULT_LOAD_IMAGE);
    }


//    private void showFileChooser() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//        try {
//            startActivityForResult(
//                    Intent.createChooser(intent, "Select a File to Upload"),
//                    10);
//        } catch (android.content.ActivityNotFoundException ex) {
//            // Potentially direct the user to the Market with a Dialog
//            Toast.makeText(this, "Please install a File Manager.",
//                    Toast.LENGTH_SHORT).show();
//        }
//    }
   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE
                && resultCode == Activity.RESULT_OK) {

//            try {
                docPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));

//                if (returnValue.size() == 0) {
//                    CommonMethods.showToastSuccess(getApplicationContext(), "No file selected");//.show();
//                }else{
//                    Uri uri  = data.getData();
//
//                    file  = new File(getPath(this,uri));

                    ArrayList<File> files = new ArrayList<>();
                    for (int i = 0; i < docPaths.size(); i++) {
                        File file = new File(docPaths.get(i));
                        files.add(file);
                    }
                    file=files.get(0);
                    fileName = file.getName();
                    fileExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
                    float fileSizeInMb = filesize_in_megaBytes(file);
                    Log.e("file size",fileSizeInMb+"--");

                    if(fileSizeInMb<5){
                        fileSelectionFlag = true;
                        tvAddPhoto.setVisibility(View.GONE);
                        tvFileName.setText(fileName);
                        if(fileExtension.equalsIgnoreCase(".ppt") || fileExtension.equalsIgnoreCase(".pptx")){
                            imgSelectedImage.setImageResource(R.drawable.ppt);
                        }else if(fileExtension.equalsIgnoreCase(".pdf")){
                            imgSelectedImage.setImageResource(R.drawable.pdf);
                        }else if(fileExtension.equalsIgnoreCase(".doc") || fileExtension.equalsIgnoreCase(".docx")){
                            imgSelectedImage.setImageResource(R.drawable.doc);
                        }else if(fileExtension.equalsIgnoreCase(".xls") || fileExtension.equalsIgnoreCase(".xlsx")){
                            imgSelectedImage.setImageResource(R.drawable.xls);
                        }else if(fileExtension.equalsIgnoreCase(".txt")){
                            imgSelectedImage.setImageResource(R.drawable.txt);
                        }else if(fileExtension.equalsIgnoreCase(".jpg") || fileExtension.equalsIgnoreCase(".png") || fileExtension.equalsIgnoreCase(".jpeg")){
                            try{
//                                final InputStream imageStream = getContentResolver().openInputStream(file.getAbsolutePath());
//                                Bitmap selectedImage = BitmapFactory.decodeStream(file.getAbsolutePath().to);
//                                selectedImage = getResizedBitmap(selectedImage, 400);
//                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                                selectedImage.compress(Bitmap.CompressFormat.WEBP, 100, stream);
//                                imgSelectedImage.setImageBitmap(selectedImage);
                            }catch (Exception ex){
                                Log.e("Exception ",""+ex);
                            }
                        }else{
                            fileSelectionFlag = false;
                            tvAddPhoto.setVisibility(View.VISIBLE);
                            imgSelectedImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_action_upload));
                            tvFileName.setText("Upload a file of type image(png, jpg, jpeg), doc, pdf, ppt");
                            CommonMethods.showToastSuccess(this, "Invalid File Selected");//, Toast.LENGTH_SHORT, true).show();
                        }
                    }
                    else{
                        fileSelectionFlag = false;
                        tvAddPhoto.setVisibility(View.VISIBLE);
//                    selectedFileTitle.setVisibility(View.GONE);
                        imgSelectedImage.setImageResource(R.drawable.no_photo_icon);
                        imgSelectedImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_action_upload));
                        tvFileName.setText("Upload a file of type image(png,jpg), doc, pdf, ppt");
                        CommonMethods.showToastSuccess(this, "File size is more than 5 Mb");//, Toast.LENGTH_SHORT, true).show();
                    }
                }

//            }
//            catch (Exception ex){
//                fileSelectionFlag = false;
//                tvAddPhoto.setVisibility(View.VISIBLE);
//                imgSelectedImage.setImageResource(R.drawable.no_photo_icon);
//                imgSelectedImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_action_upload));
//                tvFileName.setText("Upload a file of type image(png,jpg), doc, pdf, ppt");
//                CommonMethods.showToastSuccess(this,"Please select file from file manager");//.show();
//                Log.e("Ex "," "+ex);
//            }
//        }
    }



    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onGetResponse(String response, String callFor) {
        LoginResponse loginResponse=new Gson().fromJson(response,LoginResponse.class);
        if (loginResponse.getCode().equals("SUCCESS")){
            this.finish();
            CommonMethods.showToastSuccess(this,"Notice sent successfully");//,Toast.LENGTH_LONG).show();
        }
        else{
            CommonMethods.showToastError(this,"Error in sending notice");//,Toast.LENGTH_LONG).show();;
        }
    }
    private static float filesize_in_megaBytes(File file) {
        return (float) file.length()/(1024*1024);
    }
    public float  getFileSizeInMb(Uri uri){
        float fileSizeInBytes = 0;
        float fileSizeInMb = 0;
        String scheme = uri.getScheme();
        if(scheme.equals(ContentResolver.SCHEME_CONTENT))
        {
            int dataSize = 0;
            try {
                InputStream fileInputStream=getApplicationContext().getContentResolver().openInputStream(uri);
                dataSize = fileInputStream.available();
            } catch (Exception e) {
                e.printStackTrace();
            }
            fileSizeInBytes = (float) dataSize;

        }

        else if(scheme.equals(ContentResolver.SCHEME_FILE))
        {
            File f = null;
            String path = uri.getPath();
            try {
                f = new File(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
            fileSizeInBytes = (float) f.length();
        }
        fileSizeInMb  = fileSizeInBytes/1048576;
        return  fileSizeInMb;
    }
}
