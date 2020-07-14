package vztrack.gls.com.vztrack_user.utils;

import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import vztrack.gls.com.vztrack_user.application.RainbowApplication;

import static org.webrtc.ContextUtils.getApplicationContext;

public class ServerConnection {
    public static final int HTTP_TIMEOUT = 2 * 60 * 1000; // public static final int HTTP_TIMEOUT = 45000;

    public static HttpClient mHttpClient;
    public static String cookieString;

    private static HttpClient getHttpClient() {
        if (mHttpClient == null) {
            mHttpClient = RainbowApplication.getClient();
            final HttpParams params = mHttpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
            ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
        }
        return mHttpClient;
    }

    public static String executeHttpPost(String url,ArrayList<NameValuePair> postParameters)throws Exception{
        BufferedReader in = null;
        try {
            HttpClient client = getHttpClient();
            GetSessionId(url,  RainbowApplication.getClient());
            HttpPost request = new HttpPost(url);
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
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
                    System.out.println(""+e);
                }
            }
        }
    }

    public static String executeHttpGet(String url) throws Exception {
        BufferedReader in = null;
        try {
            HttpClient client = getHttpClient();
            GetSessionId(url,  RainbowApplication.getClient());
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

    public static String giveResponse(String url,String data, InputStream inputStream){
        try{
            HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            DefaultHttpClient client = new DefaultHttpClient();
            SchemeRegistry registry = new SchemeRegistry();
            SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
            socketFactory.setHostnameVerifier(new AllowAllHostnameVerifier());
            registry.register(new Scheme("https", socketFactory, 443));
            SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
            DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

            // Set verifier
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

            if((data==null || data.equals(""))) {
                Log.e("data is null","data is null");
                String response = executeHttpGet(url);
                return response;
            }
            else {
                if(inputStream!=null){
                    String response = executeHttpPost(url,data,inputStream);
                    return response;
                }else{
                    ArrayList<NameValuePair> postParameters = new ArrayList<>();
                    postParameters.add(new BasicNameValuePair("data", data));
                    String response = executeHttpPost(url, postParameters);
                    return response;
                }

            }
        }catch(Exception e){
            Log.e("Error giveResponse",e+"");
            return "error_con "+e;
        }
    }

    public static String executeHttpPost(String url, String data, InputStream inputStream)throws Exception{
        BufferedReader in = null;
        try {
            HttpClient client = getHttpClient();
            HttpPost request = new HttpPost(url);
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            if (data!=null)
            entityBuilder.addTextBody("data", data);
            if(inputStream!=null){
                entityBuilder.addBinaryBody
                        ("file", inputStream, ContentType.parse("image/jpeg"),"image");
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

    private static void GetSessionId(String url, DefaultHttpClient mClient) {
        Cookie sessionInfo;
        List<Cookie> cookies = mClient.getCookieStore().getCookies();
        if (! cookies.isEmpty()){
            for(Cookie cookie : cookies){
                sessionInfo = cookie;
                cookieString = sessionInfo.getName() + "=" + sessionInfo.getValue();
            }
        }
    }
}