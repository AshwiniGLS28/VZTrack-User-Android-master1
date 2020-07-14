package vztrack.gls.com.vztrack_user.utils;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;


import vztrack.gls.com.vztrack_user.CommonMethods.CommonMethods;
import vztrack.gls.com.vztrack_user.activity.MainActivity;
import vztrack.gls.com.vztrack_user.beans.NoticeBean;

public class ServerConnectionForFile {
    private static HttpClient mHttpClient = ServerConnection.mHttpClient;

    public static String executeHttpPost(String url, NoticeBean noticeBean)throws Exception{
        BufferedReader in = null;
        try {
            File uploaded_file = noticeBean.getFile();
            HttpClient client = mHttpClient;
            HttpPost request = new HttpPost(url);
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
          //  entityBuilder.addBinaryBody("file", uploaded_file);
            entityBuilder.addTextBody("NoticeHeading", StringFormatter.convertStringToUTF8(noticeBean.getNoticeHeading()));
            entityBuilder.addTextBody("noticeDesc", StringFormatter.convertStringToUTF8(noticeBean.getNoticeDesc()));
            entityBuilder.addTextBody("society_id",""+noticeBean.getSocityId());
            entityBuilder.addTextBody("postBy_id",""+noticeBean.getPostById());
            entityBuilder.addTextBody("fileType",""+noticeBean.getFileType());
            entityBuilder.addTextBody("fileName",""+noticeBean.getFileName());
            //entityBuilder.addTextBody("NoticePhoto",noticeBean.getNoticeBanner());
            if(uploaded_file!=null){
                InputStream inputStream = new FileInputStream(uploaded_file);
                entityBuilder.addBinaryBody
                        ("file", inputStream,
                                ContentType.create( URLConnection.guessContentTypeFromName(noticeBean.getFile().getName())),
                                uploaded_file.getName());
            }
            HttpEntity entity = entityBuilder.build();
            request.setEntity(entity);
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            String result = sb.toString();
            return result;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println("Ex : "+e);
                }
            }
        }
    }

    public static String downloadFile(String  strUrl, final Context context) throws IOException
    {
        int count;
        String fileName = "";
        try {
            URL url = new URL(strUrl);
            URLConnection conection = url.openConnection();
            conection.connect();
            conection.setConnectTimeout(2 * 60 * 1000);

            String disposition = conection.getHeaderField("Content-Disposition");
            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            }

            // this will be useful so that you can show a tipical 0-100% progress bar
            //int lenghtOfFile = conection.getContentLength();

            // download the file
            InputStream input =  conection.getInputStream();
            String result = android.os.Environment.getExternalStorageDirectory().toString() + "/VZ/" + fileName;

            // Output stream
            OutputStream output = new FileOutputStream(result);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

            File file = new File(result);
            MimeTypeMap map = MimeTypeMap.getSingleton();
            String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
            String type = map.getMimeTypeFromExtension(ext);

            openDownloadedFile(context, type, file);

        }
        catch(ActivityNotFoundException e){
            Log.e("Activity Not Found : ", e.getMessage());
            MainActivity.mainActivity.runOnUiThread(new Runnable() {
                public void run() {
                    CommonMethods.showToastError(context,"No default application found to open this file, Downloaded path /storage/VZ/");//.show();
                }
            });
        }
        catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
        return fileName;
    }

    private static void openDownloadedFile(Context context, String type, File file) {
        try{
            if (type == null)
                type = "*/*";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.fromFile(file);
            intent.setDataAndType(data, type);
            context.startActivity(intent);
        }catch (Exception ex){
            CommonMethods.showToastError(context,"Unable to open file");//.show();
            Log.e("Error : ", " In File Opening "+ex);
        }
    }

    public static String executeHttpGet(String url) throws Exception {
        BufferedReader in = null;
        try {
            HttpClient client = mHttpClient;
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse response = client.execute(request);

            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            String result = sb.toString();
            result=result.trim();
            return result;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println(""+e);
                }
            }
        }
    }

    public static String giveResponse(String url, NoticeBean noticeBean){
        try{

            HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

            DefaultHttpClient client = new DefaultHttpClient();

            SchemeRegistry registry = new SchemeRegistry();
            SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
            socketFactory.setHostnameVerifier(new AllowAllHostnameVerifier());
            registry.register(new Scheme("https", socketFactory, 443));
            SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
            //DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

            // Set verifier
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

            if(noticeBean==null || noticeBean.equals("")) {
                String response = executeHttpGet(url);
                return response;
            } else {
               // ArrayList<NameValuePair> postParameters = new ArrayList<>();
                String response = executeHttpPost(url, noticeBean);
                return response;
            }
        }catch(Exception e){
            Log.e("Error giveResponse",e+"");
            return "error_con "+e;
        }
    }

}