package com.somasocial.beberon;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.somasocial.utils.CropOption;
import com.somasocial.utils.CropOptionAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by SOMA on 05/06/15.
 */
public class RegisterFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String PERMISSION = "publish_actions";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int PICK_FROM_FILE = 1;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final int CROP_FROM_CAMERA = 2;
    ProgressBar progressBar;
    Button takePhoto, uploadPhoto;
    View view;
    private PendingAction pendingAction = PendingAction.NONE;
    private ShareDialog shareDialog;
    private boolean canPresentShareDialog;
    private CallbackManager callbackManager;
    private Uri fileUri;
    private Uri mImageCaptureUri;
    private String mCurrentPhotoPath;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
            Log.d("HelloFacebook", "Canceled");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
            String title = getString(R.string.error);
            String alertMessage = error.getMessage();
            showResult(title, alertMessage);
        }

        @Override
        public void onSuccess(Sharer.Result result) {
            Log.d("HelloFacebook", "Success!");
            if (result.getPostId() != null) {
                String title = getString(R.string.success);
                String id = result.getPostId();
                String alertMessage = getString(R.string.successfully_posted_post, id);
                showResult(title, alertMessage);
            }
        }

        private void showResult(String title, String alertMessage) {
            new AlertDialog.Builder(RegisterFragment.this.getActivity())
                    .setTitle(title)
                    .setMessage(alertMessage)
                    .setPositiveButton(R.string.ok, null)
                    .show();
        }
    };

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main_view_register, null);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarS1);
        takePhoto = (Button) view.findViewById(R.id.takephoto);
        progressBar.setProgress(1);
        progressBar.setMax(4);
        FacebookSdk.sdkInitialize(RegisterFragment.this.getActivity().getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO go to photo app
                Log.d("test", "click on take photo");


                //share on fb
//                performPublish(PendingAction.POST_STATUS_UPDATE, canPresentShareDialog);

                // create Intent to take a picture and return control to the calling application
                Context context = getActivity();
                PackageManager packageManager = context.getPackageManager();
                if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false) {
                    Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
/*
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());

                File f = null;

                try {
                    f = setUpPhotoFile();
                    mCurrentPhotoPath = f.getAbsolutePath();
                    takePictureIntent.putExtra("return-data", true);
                    mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory()+
                            "/Beberon/" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                } catch (IOException e) {
                    e.printStackTrace();
                    f = null;
                    mCurrentPhotoPath = null;
                }
                startActivityForResult(takePictureIntent,
                        CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                        */
                Intent intent 	 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory()+
                        "/Beberon/" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

                try {
                    intent.putExtra("return-data", true);

                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
//                Intent intent 	 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory()+
//                        "/Beberon/" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
//
//                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

//                try {
//                    intent.putExtra("return-data", true);
//
//                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//                } catch (ActivityNotFoundException e) {
//                    e.printStackTrace();
//                }
            }
        });

        uploadPhoto = (Button) view.findViewById(R.id.uploadphoto);
        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,
//                        "Select Picture"), 1);
//                Intent intentPick = new Intent("com.android.camera.action.CROP");
//
//                intentPick.setType("image/*");
//                startActivityForResult(intentPick, PICK_FROM_FILE);


                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, PICK_FROM_FILE);

            }
        });
        // Can we present the share dialog for photos?
        canPresentShareDialog = ShareDialog.canShow(
                SharePhotoContent.class);
        shareDialog = new ShareDialog(RegisterFragment.this.getActivity());
        shareDialog.registerCallback(
                callbackManager,
                shareCallback);
        mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("test", "reqCode:" + requestCode + " result:" + resultCode + " data" + data);
//        if (resultCode == Activity.RESULT_OK) {
            //upload photo from gallery
            if (requestCode == PICK_FROM_FILE) {
                Log.d("test","PICK_FROM_FILE");
                // Initialize intent
//                Intent intent = new Intent("com.android.camera.action.CROP");
// set data type to be sent
//                intent.setType("image/*");
//                doCrop();
                mImageCaptureUri = data.getData();
                String realPath=getRealPathFromURI(this.getActivity().getApplicationContext(),mImageCaptureUri);//getRealPathFromURI_API19(this.getActivity().getApplicationContext(), data.getData());

                Log.d("test","mImageCaptureUri:"+mImageCaptureUri);
                Log.d("test","mImageCaptureUri realPath:"+realPath);
                doCrop();
//                Bundle extras = data.getExtras();
//                if (extras != null) {
//                    Bitmap photo = extras.getParcelable("data");
//                    Log.d("test", "photo crepped:" + photo);
//                }
                /*
                String realPath=getRealPathFromURI_API19(this.getActivity().getApplicationContext(), data.getData());
                goToNext(realPath);
                Log.d("test","real path:"+getRealPathFromURI_API19(this.getActivity().getApplicationContext(), data.getData()));
                Uri selectedImageUri = data.getData();

                Uri selectedImage = data.getData();
                fileUri=data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
Log.d("test","selected image:"+selectedImageUri.getPath());
                Cursor cursor = RegisterFragment.this.getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                Log.d("test","selected image filePathColumn:"+filePathColumn[0]);

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                Log.d("test","selected image picturePath:"+picturePath);
                cursor.close();

                try {
                    Bitmap bitmap = decodeUri(selectedImage);
                    Log.d("test","selected image:"+selectedImage);
                } catch (FileNotFoundException e) {
                    Log.d("test","decodeUri exception "+e);
                }
                //goToNext(selectedImage);

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
                */
            }

            //take photo
            else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                if (resultCode == Activity.RESULT_OK) {
                    Log.d("test", "req camera photo");
                    doCrop();


                }
            }
            else if (requestCode == CROP_FROM_CAMERA)
            {
                Log.d("test","mImageCaptureUri do activ result:"+mImageCaptureUri);
                Log.d("test","data.getData() do activ result:"+data.getData());
                Bundle extras = data.getExtras();
                Uri imgData=data.getData();
                if (data.getData()==null)
                {
                    imgData=mImageCaptureUri;
                }
                Log.d("test","imgData:"+imgData);
                String realPath=getRealPathFromURI(this.getActivity().getApplicationContext(),imgData);//getRealPathFromURI_API19(this.getActivity().getApplicationContext(), data.getData());

                 goToNext(realPath);
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");


                    //mImageView.setImageBitmap(photo);
                }
                File f = new File(mImageCaptureUri.getPath());
                if (f.exists()) f.delete();
            }


//        }
    }

    private void doCrop() {
        Log.d("test","mImageCaptureUri do crop:"+mImageCaptureUri);
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = this.getActivity().getPackageManager().queryIntentActivities( intent, 0 );

        int size = list.size();

        if (size == 0) {
            Toast.makeText(this.getActivity(), "Can not find image crop app", Toast.LENGTH_SHORT).show();

            return;
        } else {
            intent.setData(mImageCaptureUri);

            intent.putExtra("outputX", 600);
            intent.putExtra("outputY", 600);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", false);

            if (size == 1) {
                Intent i 		= new Intent(intent);
                ResolveInfo res	= list.get(0);

                i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();

                    co.title 	= this.getActivity().getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon		= this.getActivity().getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent= new Intent(intent);

                    co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(this.getActivity().getApplicationContext(), cropOptions);

//                AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
//                builder.setTitle("Choose Crop App");
//                builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
//                    public void onClick( DialogInterface dialog, int item ) {
//                        startActivityForResult( cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
//                    }
//                });
                startActivityForResult( cropOptions.get(0).appIntent, CROP_FROM_CAMERA);
//                builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel( DialogInterface dialog ) {
//
//                        if (mImageCaptureUri != null ) {
//                            RegisterFragment.this.getActivity().getContentResolver().delete(mImageCaptureUri, null, null );
//                            mImageCaptureUri = null;
//                        }
//                    }
//                } );

//                AlertDialog alert = builder.create();
//
//                alert.show();
            }
        }
    }

    private void goToNext(String img) {
        // camera result

        Log.d("test","gotonext:"+img);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RegisterBabyFragment registerBabyFragment = new RegisterBabyFragment();
        Bundle args = new Bundle();
        args.putString("img", img);
        registerBabyFragment.setArguments(args);
        fragmentTransaction.replace(R.id.fragment_container, registerBabyFragment, "HELLO");
        fragmentTransaction.commit();
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(this.getActivity().getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 140;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(this.getActivity().getContentResolver().openInputStream(selectedImage), null, o2);

    }

    private void galleryAddPic() {
        Log.d("test", "galleryAddPic");
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);

        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.getActivity().sendBroadcast(mediaScanIntent);

        goToNext(mCurrentPhotoPath);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }


    /* Photo album for this application */
    private String getAlbumName() {
        return getString(R.string.album_name);
    }


    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
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

    private void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;
        // These actions may re-set pendingAction if they are still pending, but we assume they
        // will succeed.
        pendingAction = PendingAction.NONE;

        switch (previouslyPendingAction) {
            case NONE:
                break;
            case POST_PHOTO:
                postPhoto();
                break;
            case POST_STATUS_UPDATE:
                postStatusUpdate();
                break;
        }
    }

    private void postPhoto() {

    }

    private void postStatusUpdate() {
        Profile profile = Profile.getCurrentProfile();
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("Hello Facebook")
                .setContentDescription(
                        "The 'Hello Facebook' sample  showcases simple Facebook integration")
                .setContentUrl(Uri.parse("http://developers.facebook.com/docs/android"))
                .build();
        if (canPresentShareDialog) {
            shareDialog.show(linkContent);
        } else if (profile != null && hasPublishPermission()) {
            ShareApi.share(linkContent, shareCallback);
        } else {
            pendingAction = PendingAction.POST_STATUS_UPDATE;
        }
    }

    private boolean hasPublishPermission() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && accessToken.getPermissions().contains("publish_actions");
    }

    private void performPublish(PendingAction action, boolean allowNoToken) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            pendingAction = action;
            if (hasPublishPermission()) {
                // We can do the action right away.
                handlePendingAction();
                return;
            } else {
                // We need to get new permissions, then complete the action when we get called back.
                LoginManager.getInstance().logInWithPublishPermissions(
                        RegisterFragment.this.getActivity(), Arrays.asList(PERMISSION));
                return;
            }
        }

        if (allowNoToken) {
            pendingAction = action;
            handlePendingAction();
        }
    }

    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }
}
