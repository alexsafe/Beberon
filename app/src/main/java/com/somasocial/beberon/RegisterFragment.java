package com.somasocial.beberon;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

/**
 * Created by SOMA on 05/06/15.
 */
public class RegisterFragment extends Fragment {
    ProgressBar progressBar;
    Button takephoto;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,Bundle savedInstanceState)
    {

        View view =  inflater.inflate(R.layout.fragment_main_view_register,  null);
        progressBar=(ProgressBar) view.findViewById(R.id.progressBarS1);
        takephoto=(Button) view.findViewById(R.id.takephoto);
        progressBar.setProgress(1);
        progressBar.setMax(4);

        Button button = (Button) view.findViewById(R.id.takephoto);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //TODO go to photo app
                Log.d("test", "click on register");
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                RegisterBabyFragment hello = new RegisterBabyFragment();
                fragmentTransaction.replace(R.id.fragment_container, hello, "HELLO");
                fragmentTransaction.commit();
            }
        });
        return view;
    }
}
