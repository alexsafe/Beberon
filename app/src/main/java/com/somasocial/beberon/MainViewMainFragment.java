package com.somasocial.beberon;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by SOMA on 05/06/15.
 */
public class MainViewMainFragment extends Fragment {
    ImageButton register,vote,get_more,invite;
    TextView hello;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,Bundle savedInstanceState)
    {
        SharedPreferences sharedPrefs = MainViewMainFragment.this.getActivity().getSharedPreferences("fbDetails", Context.MODE_PRIVATE);
//After referencing your Views, add this.
        String nameStr = sharedPrefs.getString("name", null);
        String idStr = sharedPrefs.getString("id", null);
        View view =  inflater.inflate(R.layout.fragment_main_view_main,  null);
        register=(ImageButton) view.findViewById(R.id.register_but);
        hello=(TextView) view.findViewById(R.id.hello);
        hello.append(", "+nameStr.substring(0,nameStr.indexOf(" ")));
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", "click on register impl in fragment");
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                RegisterFragment hello = new RegisterFragment();
                fragmentTransaction.replace(R.id.fragment_container, hello, "HELLO");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        return view;
    }



}
