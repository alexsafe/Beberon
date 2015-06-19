package com.somasocial.net;

/**
 * Created by SOMA on 16/06/15.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.LightingColorFilter;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.LightingColorFilter;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.View;

public class M {


    public M(String m) {
        Log.d("String TAG", "% -- " + m);
    }
//	Activity act;
//	public static SharedPreferences getPrefMgr( ) {
//		SharedPreferences prefs = act.getSharedPreferences(FILE,
//				Context.MODE_PRIVATE);
//		return prefs;
//	}
//
//	public static String getLoginStatus() {
//		return getPrefMgr().getString(LOGIN_STATUS, "");
//	}
//
//	public static String getLanguage() {
//		return getPrefMgr().getString(LANGUAGE, null);
//	}
//
//	public static String getFbToken() {
//		return getPrefMgr().getString(FACEBOOKTOKEN, "");
//	}
//
//	public static String getLocale() {
//		String lang = Locale.getDefault().getLanguage();
//		if (lang.equals("tr"))
//			lang = "mk";
//		return lang;
//
//	}

    public static void setColorFilter(View v, Integer filter) {
        if (filter == null)
            v.getBackground().clearColorFilter();
        else {
            // To lighten instead of darken, try this:
            // LightingColorFilter lighten = new LightingColorFilter(0xFFFFFF,
            // filter);
            LightingColorFilter darken = new LightingColorFilter(filter, 0x000000);
            v.getBackground().setColorFilter(darken);
        }
        // required on Android 2.3.7 for filter change to take effect (but not
        // on 4.0.4)
        v.getBackground().invalidateSelf();
    }
    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (;;) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
    public static String getDeviceId(Activity act) {
        return Settings.Secure.getString(act.getContentResolver(),
                Settings.Secure.ANDROID_ID);

    }
//
//	public static String getCountry() {
//		TelephonyManager tm = (TelephonyManager) GL.get().getSystemService(
//				Context.TELEPHONY_SERVICE);
//		String countryCode = tm.getSimCountryIso();
//		return countryCode;
//	}

//	public static double getLongitude() {
//		return Double.parseDouble(getPrefMgr().getString(LONGITUDE, "0"));
//	}
//
//	public static double getLatitude() {
//		return Double.parseDouble(getPrefMgr().getString(LATITUDE, "0"));
//	}
//
//	public static String getUserName() {
//		return getPrefMgr().getString(USERNAME, "");
//	}
//
//	public static String getPassword() {
//		return getPrefMgr().getString(PASSWORD, "");
//	}
//
//	public static String getToken() {
//		return getPrefMgr().getString(TOKEN, "");
//	}

    public static void doErrorCheck(Activity context, JSONObject object) {

        try {
            JSONObject errorObj = object.getJSONObject("error");

            String key = null;
            String value = null;
            Map<String, String> map = new HashMap<String, String>();
            Iterator<?> iter = errorObj.keys();
            while (iter.hasNext()) {
                key = (String) iter.next();
                value = errorObj.getString(key);
                map.put(key, value);
            }

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setTitle(key);

            dialogBuilder.setMessage(value);

            dialogBuilder.setPositiveButton(
                    "Dismiss",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            // current activity
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

