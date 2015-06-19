package com.somasocial.net;

/**
 * Created by SOMA on 17/06/15.
 */

import android.app.Activity;
import android.util.Log;

import com.somasocial.types.Items;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import static com.somasocial.utils.CommonUtils.BASE_URL;

public class ApiRequests {
    //        static String serverCall = BASE_URL + "/%method.php?";
    static String serverCall = BASE_URL + "/api/%jsonReq.json?";

    public static boolean getPhotoItems(final Activity activity, int limit, final GetCompletionListener listener) {
        Log.d("test", "in getPhotos");
        StringBuffer link = new StringBuffer();
        link.append(serverCall.replace("%jsonReq", "babies"));
        link.append("&limit=");
        link.append(limit);
//        link.append(serverCall.replace("%limit", "4"));
        Log.d("test", "link:" + link);
        JSONParser client = new JSONParser(activity, new JSONParser.GetJSONListener() {

            @Override
            public void onRemoteCallComplete(JSONObject json, boolean success) {
                Log.d("test", "getPhotoItems json:" + json);
                Log.d("test", "getPhotoItems result:" + success);
                final ArrayList<Items> mdata = new ArrayList<Items>();
                final ArrayList<Items> mdataFail = null;

                if (success) {
                    try {
                        if (json != null) {
                            JSONArray array = null;
                            try {

                                array = json.getJSONArray("babies");
                            } catch (JSONException e1) {

                                e1.printStackTrace();
                            }
                            Log.d("test", "getPhotoItems array:" + array);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject mJson = null;
                                try {
                                    mJson = array.getJSONObject(i);
                                } catch (JSONException e1) {

                                    e1.printStackTrace();
                                }
                                Log.d("test", "getPhotoItems mJson.getString(\"src\"):" + mJson.getString("src"));
                                Log.d("test", "getPhotoItems mJson.optString(\"src\"):" + mJson.optString("src"));
                                final Items data = new Items();
//                                data.campaign_id = mJson.getString("campaign_id");
                                data.media_id = mJson.getString("src");
                                data.media_url = mJson.getString("url");

                                data.fb_user_id = mJson.getString("fb_user_id");
//                                data.user_name = mJson.getString("user_name");
//                                data.type = mJson.getString("type");
                                data.title = mJson.getString("title");
//                                LB.getInstagramUser().points = json.getInt("coins");
//                                LB.getInstagramUser().syncUserPoints();
                                mdata.add(data);
                            }
                        }
                        listener.OnCompletion(mdata);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (!success) {
                    Log.d("test", "getPhotoItems not success");
                    // M.doErrorCheck(activity, json);
                    listener.OnCompletion(mdataFail);
                }
            }

        });
        client.execute(link.toString());
        return true;
    }

    public static boolean getJsonPhotos(final Activity activity, int limit, final GetSaveData listener) {
        Log.d("test", "in getPhotos");
        StringBuffer link = new StringBuffer();
        link.append(serverCall.replace("%jsonReq", "babies"));
        link.append("&limit=");
        link.append(limit);
//        link.append(serverCall.replace("%limit", "4"));
        Log.d("test", "link:" + link);
        JSONParser client = new JSONParser(activity, new JSONParser.GetJSONListener() {

            @Override
            public void onRemoteCallComplete(JSONObject jsonFromNet, boolean result) {


                Log.d("test", "getPhotos json:" + jsonFromNet);
                Log.d("test", "getPhotos result:" + result);
                if (listener != null) {
                    listener.OnCompletion(jsonFromNet);
                }
            }

        });
        client.execute(link.toString());
        return true;
    }

    public interface GetFileCompletionListener {
        public void OnCompletion(ArrayList<File> data);
    }

    public interface GetCompletionListener {
        public void OnCompletion(ArrayList<Items> data);
    }

    public interface GetSaveData {
        public void OnCompletion(JSONObject json);

        public void comple(String json);

    }

}
