package com.somasocial.net;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by SOMA on 17/06/15.
 */
public class JSONParser extends AsyncTask<String, Void, JSONObject> {
    AlertDialog progressDialog;
    GetJSONListener getJSONListener;
    Activity curContext;
    boolean result, blurryBackground;
    String loadingDialog;
    boolean showDialog;
    boolean showHart = false;
    long timeStart;


    public JSONParser(Activity context, GetJSONListener listener){
        this.getJSONListener = listener;
        this.curContext = context;
    }

    public JSONParser(Activity context, GetJSONListener listener, boolean blurryBackground, String loadingDialog, boolean showHart, boolean showDialog) {
        this.getJSONListener = listener;
        this.curContext = context;
        this.blurryBackground = blurryBackground;
        this.loadingDialog = loadingDialog;
        this.showDialog = showDialog;
        this.showHart = showHart;
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            // Log.d("test", "convertStreamToString IOException:" + e);
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // Log.d("test", "convertStreamToString finally IOException:"
                // +
                // e);
                e.printStackTrace();
            }
        }
        // Log.d("test", "json sbtostring:" + sb.toString());

        return sb.toString();
    }

    @Override
    public void onPreExecute() {
        progressDialog = new ProgressDialog(curContext);
        progressDialog.setMessage("Getting new photos please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        // if (showDialog)
        // handler.sendEmptyMessage(0);
    }

    @Override
    protected JSONObject doInBackground(String... urls) {
        try {
            Log.d("test", "JSONParser doInBackground");
            return connect(urls[0]);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.d("test", "JSONParser doInBackground exception:" + e);
            e.printStackTrace();
        }

        return null;
        // Log.d("test", "slept");
        // try {
        // return connect(urls[0]);
        // Thread.sleep(3000);
        //
        //
        // } catch (Exception m) {
        // m.printStackTrace();
        // return null;
        // }
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        super.onPostExecute(json);
        Log.d("test", "JSONParser complete: " + json);
        progressDialog.dismiss();
        JSONObject mockJson=null;

//        try {
//            mockJson=new JSONObject("{\n" +
//                    "    \"babies\": [\n" +
//                    "        {\n" +
//                    "            \"_id\": {\n" +
//                    "                \"$id\": \"5571c4fa1babd64e16c9918c\"\n" +
//                    "            },\n" +
//                    "            \"fb_user_id\": \"238892475374\",\n" +
//                    "            \"src\": \"5571c4f9cedb0_2015_06_05__18_49_13.jpg\",\n" +
//                    "            \"url\": \"photo-5571c4fa26a33\",\n" +
//                    "            \"title\": \"rest_api\",\n" +
//                    "            \"date\": {\n" +
//                    "                \"sec\": 1433519354,\n" +
//                    "                \"usec\": 158000\n" +
//                    "            },\n" +
//                    "            \"reports\": [],\n" +
//                    "            \"reports_count\": 0,\n" +
//                    "            \"votes\": [],\n" +
//                    "            \"votes_count\": 3,\n" +
//                    "            \"baby_id\": {\n" +
//                    "                \"$id\": \"53e4ac10b155c125256b2079\"\n" +
//                    "            }\n" +
//                    "        },\n" +
//                    "        {\n" +
//                    "            \"_id\": {\n" +
//                    "                \"$id\": \"545be3bcb155c1a409181d5b\"\n" +
//                    "            },\n" +
//                    "            \"fb_user_id\": \"238892475374\",\n" +
//                    "            \"src\": \"545be3ba52768_2014_11_06__23_10_18.jpg\",\n" +
//                    "            \"url\": \"photo-545be3bcbdae1\",\n" +
//                    "            \"title\": \"test\",\n" +
//                    "            \"date\": {\n" +
//                    "                \"sec\": 1433358373,\n" +
//                    "                \"usec\": 101000\n" +
//                    "            },\n" +
//                    "            \"reports\": [],\n" +
//                    "            \"reports_count\": 0,\n" +
//                    "            \"votes\": [],\n" +
//                    "            \"votes_count\": 0,\n" +
//                    "            \"baby_id\": {\n" +
//                    "                \"$id\": \"545be3bab155c1a409181d5a\"\n" +
//                    "            },\n" +
//                    "            \"archived\": false,\n" +
//                    "            \"end_date\": null,\n" +
//                    "            \"disabled\": false\n" +
//                    "        }\n" +
//                    "}\n");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        getJSONListener.onRemoteCallComplete(json, true);
        // handler.sendEmptyMessage(1);
        // Log.d("test", "onPostExecute : " + json);
        // Log.d("test", "onPostExecute getJSONListener: " +
        // getJSONListener);
        // if (getJSONListener != null)
        // if (json != null)
        // getJSONListener.onRemoteCallComplete(json, true);

    }

    protected void onCancelled() {
        // do something, inform user etc.
        // if (isCancelled()) {
        Log.d("test", "JSONParser canceled");
        progressDialog.dismiss();
        getJSONListener.onRemoteCallComplete(null, false);

        // }
    }

    public JSONObject connect(String url) {
        // HttpClient httpclient = new DefaultHttpClient();
        Log.d("test", "JSONParser in connect");
        SharedPreferences sharedpreferences = curContext.getApplicationContext().getSharedPreferences("ig-user", 0);
        String timeout = sharedpreferences.getString("timeout", "15");
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = Integer.parseInt(timeout) * 1000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = Integer.parseInt(timeout) * 1000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        HttpClient httpclient = new DefaultHttpClient(httpParameters);
        HttpGet httpget = new HttpGet(url);

        new M(url);

        // Execute the request
        HttpResponse response = null;
        Log.d("test", "JSONParser vonnect url:" + url);
        try {
            Log.d("test", "JSONParser in try:");
//			Timer timer = new Timer();
//			timer.schedule(new TaskKiller(this), 1000);

            response = httpclient.execute(httpget);

            // Examine the response status
            Log.i("Praeda", response.getStatusLine().toString());

            // Get hold of the response entity
            HttpEntity entity = response.getEntity();

            if (entity != null) {

                // A Simple JSON Response Read
                InputStream instream = entity.getContent();
                // Log.d("test", "instreamtoStringlength:" +
                // instream.toString());
                // Log.d("test", "instreamtoStringlength:" +
                // instream.toString().length());
                String result = convertStreamToString(instream);
                new M(" =================================================================================================================================");
                new M(" =================================================================================================================================");
                new M(" =================================================================================================================================");
                new M(" ========================================================= RESULT ================================================================");
                new M(" Response " + result);
                new M(" ========================================================== END ==================================================================");
                new M(" =================================================================================================================================");
//				Log.d("JSONClient " + url, result);
                Log.d("test", "JSONParser response: url" + url);
                Log.d("test", "JSONParser response: result:" + result);
                // A Simple JSONObject Creation
                JSONObject json = null;

                if (isJSONValid(result))
                    json=new JSONObject(result);

                // Closing the input stream will trigger connection release
                instream.close();
//				timer.cancel();
                return json;
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            Log.d("test", "JSONParser ClientProtocolException: " + e);
            cancel(true);
            // this.cancel(true);
            e.printStackTrace();
        } catch (IOException e) {

            Log.d("test", "JSONParser IOException: " + e);
            // TODO Auto-generated catch block
            e.printStackTrace();
            cancel(true);
        } catch (JSONException e) {

            Log.d("test", "JSONParser JSONException: " + e);// TODO
            // Auto-generated
            // catch block
            e.printStackTrace();
            cancel(true);
        } catch (Exception m) {
            Log.d("test", "JSONParser Exception: " + m);
            m.printStackTrace();
            cancel(true);
        }

        return null;
    }

    public interface GetJSONListener {
        public void onRemoteCallComplete(JSONObject jsonFromNet, boolean result);
    }

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
/*
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 0) {
                if (curContext != null) {
                    progressDialog = new ProgressDialog(curContext);
                    progressDialog.setCancelable(true);
                    progressDialog.show();

                    if (!showHart)
                        progressDialog.setMessage(loadingDialog);

                    if (showHart) {
                        progressDialog.setContentView(R.layout.widget_loading_dialog);
                    }
                }

            }

            if (msg.what == 1) {
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        try {
                            progressDialog.dismiss();
                            progressDialog = null;
                        } catch (Exception e) {
                            Log.d("test", "dialog exception:" + e);
                        }
                    }
                    // progressDialog.dismiss();
                }
            }

        }
    };*/
}

