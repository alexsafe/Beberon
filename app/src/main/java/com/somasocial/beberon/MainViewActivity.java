package com.somasocial.beberon;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.somasocial.utils.ProfilePictureView;

/**
 * Created by SOMA on 05/06/15.
 */
public class MainViewActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view_layout);

        ProfilePictureView profilePic=(ProfilePictureView) findViewById(R.id.fbPic);


        SharedPreferences sharedPrefs = getSharedPreferences("fbDetails", MODE_PRIVATE);
//After referencing your Views, add this.
        String nameStr = sharedPrefs.getString("name", null);
        String idStr = sharedPrefs.getString("id", null);
        profilePic.setProfileId(idStr);
//        AccessToken token = AccessToken.getCurrentAccessToken();
//        if(token != null)
//        {
//            if(nameStr != null)
//            {
//                name.setText(nameStr);
//            }
//            if(idStr != null)
//            {
//                profilePictureView.setProfileId(id);
//            }
//        }
        Log.d("test", "MainViewActivity idstr:" + idStr);


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MainViewMainFragment hello = new MainViewMainFragment();
        fragmentTransaction.add(R.id.fragment_container, hello, "HELLO");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    public void registerClick(View v) {
        Log.d("test", "click on register impl in activity");
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RegisterFragment hello = new RegisterFragment();
        fragmentTransaction.replace(R.id.fragment_container, hello, "HELLO");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        Log.d("test", "getFragmentManager().getBackStackEntryCount():" + getFragmentManager().getBackStackEntryCount());
        if (getFragmentManager().getBackStackEntryCount() == 1) {
            this.finish();
        } else {
            getFragmentManager().popBackStack();
        }
    }
}
