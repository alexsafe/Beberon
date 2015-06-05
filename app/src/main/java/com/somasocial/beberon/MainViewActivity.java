package com.somasocial.beberon;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

/**
 * Created by SOMA on 05/06/15.
 */
public class MainViewActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view_layout);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MainViewMainFragment hello = new MainViewMainFragment();
        fragmentTransaction.replace(R.id.fragment_container, hello, "HELLO");
        fragmentTransaction.commit();
    }


    public void registerClick(View v)
    {
        Log.d("test", "click on register");
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RegisterFragment hello = new RegisterFragment();
        fragmentTransaction.replace(R.id.fragment_container, hello, "HELLO");
        fragmentTransaction.commit();
    }
}
