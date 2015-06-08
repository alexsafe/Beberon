package com.somasocial.beberon;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

/**
 * Created by SOMA on 05/06/15.
 */
public class RegisterFragment extends Fragment {
    ProgressBar progressBar;
    Button takePhoto, uploadPhoto;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main_view_register, null);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarS1);
        takePhoto = (Button) view.findViewById(R.id.takephoto);
        progressBar.setProgress(1);
        progressBar.setMax(4);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO go to photo app
                Log.d("test", "click on register");
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                RegisterBabyFragment hello = new RegisterBabyFragment();
                fragmentTransaction.replace(R.id.fragment_container, hello, "HELLO");
                fragmentTransaction.commit();
            }
        });

        uploadPhoto = (Button) view.findViewById(R.id.uploadphoto);
        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), 1);
            }
        });
        return view;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("test","reqCode:"+requestCode+" result:"+resultCode+" data"+data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Uri selectedImageUri = data.getData();

               Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = RegisterFragment.this.getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();

                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
//                ImageView iv=new ImageView();
//                iv.setImageBitmap(bitmap);
//                iv.setLayoutParams(new FrameLayout.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        RadioGroup.LayoutParams.WRAP_CONTENT));
//
//                //adding view to layout
//
//                Log.d("test", "img:"+picturePath);
//                Log.d("test", "img:"+bitmap);
//                Log.d("test", "img:"+filePathColumn);
            }
        }
    }

    public String getPath(Uri uri) {
//        String[] projection = {MediaStore.Images.Media.DATA};
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
//        if (cursor != null) {
//            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
//            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
//            int column_index = cursor
//                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            return cursor.getString(column_index);
//        } else return null;
        return "muie";
    }
}
