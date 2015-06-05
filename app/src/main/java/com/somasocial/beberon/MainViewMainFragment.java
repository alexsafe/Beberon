package com.somasocial.beberon;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by SOMA on 05/06/15.
 */
public class MainViewMainFragment extends Fragment {
    ImageButton register,vote,get_more,invite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,Bundle savedInstanceState)
    {

        View view =  inflater.inflate(R.layout.fragment_main_view_main,  null);
        register=(ImageButton) view.findViewById(R.id.register_but);
        return view;
    }

}
