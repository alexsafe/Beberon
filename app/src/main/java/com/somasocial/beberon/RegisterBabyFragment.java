package com.somasocial.beberon;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by SOMA on 05/06/15.
 */
public class RegisterBabyFragment extends Fragment {
    ImageView babyRegPhoto;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register_baby, null);
        babyRegPhoto = (ImageView) view.findViewById(R.id.baby_reg_photo);
        Bundle bundle = this.getArguments();
        String myImg = bundle.getString("img", "null");
        if (!myImg.equals("null")) {
            babyRegPhoto.setBackground(null);
            File image = new File(myImg);

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();

            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
            babyRegPhoto.setImageBitmap(bitmap);
//        bitmap = Bitmap.createScaledBitmap(bitmap,parent.getWidth(),parent.getHeight(),true);
        }
        return view;
    }
}
