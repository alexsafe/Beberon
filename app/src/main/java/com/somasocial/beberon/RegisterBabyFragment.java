package com.somasocial.beberon;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by SOMA on 05/06/15.
 */
public class RegisterBabyFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register_baby, null);
        return view;
    }
}
