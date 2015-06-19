package com.somasocial.beberon;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.somasocial.net.ApiRequests;
import com.somasocial.types.Items;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;

import java.io.File;
import java.util.ArrayList;

import static com.somasocial.utils.CommonUtils.BASE_URL;

/**
 * Created by SOMA on 04/06/15.
 */
public class StartGame extends Activity implements Animation.AnimationListener{

    //    static Integer[] mThumbIds = {R.raw.bebe_sample, R.raw.bebe_sample,
//            R.raw.bebe_sample, R.raw.bebe_sample};
//    static Integer[] mThumbIds1 = {R.raw.bebe_sample, R.raw.bebe_sample,
//            R.raw.bebe_sample, R.raw.bebe_sample};
//    static Integer[] mThumbIds2 = {R.raw.bebe_sample, R.raw.bebe_sample,
//            R.raw.bebe_sample, R.raw.bebe_sample};
    GridView gridview;
    Animation animZoomOut,animZoomIn;
    private Animator mCurrentAnimator;
    ProgressBar progressBar;
    TextView messageText, subText;
    int page, count;
    ArrayList<File> files = null;
    ArrayList<Items> items = new ArrayList<Items>();
    ArrayList<Uri> uris = new ArrayList<Uri>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        loadImages();
        setContentView(R.layout.activity_start_game_layout);
        page = 1;
        count = 0;
        gridview = (GridView) findViewById(R.id.gridView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        messageText = (TextView) findViewById(R.id.textView3);
        subText = (TextView) findViewById(R.id.smalltext);

        progressBar.setProgress(0);
        progressBar.setMax(4);

        final Integer[] mHeartsds = new Integer[]{R.drawable.inima, R.drawable.inima,
                R.drawable.inima, R.drawable.inima};

        loadImages(page);

        Log.d("test", "gridview width:" + gridview.getMeasuredWidth());
//        gridview.setColumnWidth(200);


        //set invisible
        progressBar.setVisibility(View.INVISIBLE);
        subText.setVisibility(View.INVISIBLE);
        animZoomOut = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.zoom_out);
        animZoomOut.setAnimationListener(this);
        animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.zoom_in);
        animZoomIn.setAnimationListener(this);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                ImageView imageView = (ImageView) v;
                imageView.setImageResource(mHeartsds[position]);

                // load the animation
                animZoomOut = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.zoom_out);

                // set animation listener


                //change adapter and UI
//                ImageView imageView = (ImageView) v;
                imageView.startAnimation(animZoomOut);
                imageView.startAnimation(animZoomIn);


                Log.d("test", "clicked position:" + position);
//                ImageView imageView = (ImageView) v;
//                imageView.setImageResource(ImageAdapter.mHeartsds[position]);
//                Log.d("test", "clicked uri:" + items.get(position).media_id);
//                Log.d("test", "clicked uri:" + items.get(position).fb_user_id);

                page++;
//                loadImages(page);

/*
                Intent intent = new Intent(StartGame.this, FaceBookLoginActivity.class);
//                    Intent intent=new Intent(StartGame.this, MainActivity.class);
                startActivity(intent);
*/
/*
                page++;
                int picturesRemaining=4-page;

                Log.d("test", "clicked on " + position + " page: " + page);
                Log.d("test", "count " + count);
                if (picturesRemaining==0)
                {

                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    subText.setVisibility(View.VISIBLE);
                    progressBar.setProgress(page);
                    messageText.setText("Great Choice");

                    subText.setText("choose " + picturesRemaining + "more cute pictures to grab a bebecoin");
                    gridview.setAdapter(new ImageAdapter(StartGame.this.getApplicationContext(), page, count));
                }
*/
            }
        });
    }

    private void zoomImageFromThumb(final View thumbView, int imageResId) {
    // If there's an animation in progress, cancel it immediately and
    // proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }
    }

    private void loadImages(final int page) {
        Log.d("test", "loadImages");
        ApiRequests.getPhotoItems(StartGame.this, 4, new ApiRequests.GetCompletionListener() {
            @Override
            public void OnCompletion(ArrayList<Items> data) {
                if (data.size() > 0) {
                    Log.d("test", "getPhotoItems data :" + data);
                    Log.d("test", "getPhotoItems data :" + data.size());
                    Log.d("test", "page:" + page);
                    if (data != null) {
                        for (int i = 0; i < data.size(); i++) {
                            if (data.size() > 0) {
                                if (i < data.size())
                                    items.add(data.get(i));
                                else
                                    items.add(data.get(data.size() - 1));
                            }
                        }
                        Log.d("test", "getPhotoItems items.size():" + items.size());
                        for (int i = 0; i < items.size(); i++) {
                            String url = "";
                            if (page==1)
                            {
                                url = "http://beta.beberon.ro:8090/assets/votemybaby/uploads/524898723/facebook/5572ecd8dd15d_2015_06_06__15_51_36.jpg";
                            }
                            else if(page==2){
                                url = "http://beta.beberon.ro:8090/assets/votemybaby/uploads/524898723/photo_page/554cd0d9866d5_2015_05_08__18_06_01.jpg";
                            }
                            items.get(i).media_id = url;
                            Log.d("test", "getPhotoItems prepareImages item campaign_id:" + i + " " + items.get(i).media_id);
                        }
                        for (int i = 0; i < uris.size(); i++) {
                            Log.d("test", "uris:" + i + " " + uris.get(i));
                        }

                    }
                }
                gridview.setAdapter(new ImageAdapter(StartGame.this.getApplicationContext(), page, count, items));
            }
        });
    }

    @Override
    public void onAnimationStart(Animation animation) {
        Log.d("test","onAnimationStart :"+animation.toString());
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Log.d("test","onAnimationEnd :"+animation);
        mCurrentAnimator = null;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        Log.d("test","onAnimationRepeat :"+animation);
        mCurrentAnimator = null;
    }

    //    our custom adapter
    private class ImageAdapter extends BaseAdapter {
        ArrayList<Items> objects;
        private Context mContext;
        private int mNo, mCount;

//        public ImageAdapter(Context context, int no, int count){// ArrayList<File> files) {
//            mContext = context;
//            mNo = no;
//            mCount=count;
//            this.files=files;
//        }

        public ImageAdapter(Context context, int no, int count, ArrayList<Items> objects) {
            mContext = context;
            mNo = no;
            mCount = count;
            this.objects = objects;
        }

//        public int getmNo() {
//            return mNo;
//        }
//        int mNo=getmNo();


        @Override
        public int getCount() {

            return 4;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView,
                            ViewGroup parent) {
            ImageView imageView;
//            check to see if we have a view
            if (convertView == null) {
//                no view - so create a new one
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(250, 250));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setPadding(8, 8, 8, 8);
            } else {
//                use the recycled view object
                imageView = (ImageView) convertView;
            }
            Log.d("test", "adapter count:" + mCount);
            Log.d("test", "adapter files:" + files);
//            Picasso.with(StartGame.this).setLoggingEnabled(true);
            Integer[] mThumbIds = null;
            Integer[] mHeartsds = null;
            mHeartsds = new Integer[]{R.drawable.inima, R.drawable.inima,
                    R.drawable.inima, R.drawable.inima};
            if (mNo < 4) {
                switch (mNo) {
                    case 0:
                        mThumbIds = new Integer[]{R.raw.bebe_sample, R.raw.bebe_sample,
                                R.raw.bebe_sample, R.raw.bebe_sample};
                        break;
                    case 1:
                        mThumbIds = new Integer[]{R.raw.bebe_sample2, R.raw.bebe_sample2,
                                R.raw.bebe_sample2, R.raw.bebe_sample2};

                        break;
                    case 2:
                        mThumbIds = new Integer[]{R.raw.bebe_sample2, R.raw.bebe_sample,
                                R.raw.bebe_sample2, R.raw.bebe_sample};
                        break;
                    default:
                        mThumbIds = new Integer[]{R.raw.bebe_sample, R.raw.bebe_sample,
                                R.raw.bebe_sample, R.raw.bebe_sample};
                        break;
                }
            }
            if (objects.size()>0) {
                Log.d("test", "adapter position:" + position + " uri:" + objects.get(position).media_id.toString());
                Picasso.with(StartGame.this)
                        .load(objects.get(position).media_id.toString())
                        .noFade()
                        .resize(250, 250)
//                    .noPlaceholder()
                        .placeholder(R.raw.bebe_sample)
                        .centerInside()
                        .into(imageView);
            }
            else{
                Picasso.with(StartGame.this)
                        .load(R.raw.bebe_sample)
                        .noFade()
                        .resize(250, 250)
//                    .noPlaceholder()
                        .placeholder(R.raw.bebe_sample)
                        .centerInside()
                        .into(imageView);
            }
//            Picasso.with(StartGame.this)
//                    .load(R.raw.bebe_sample)
//                    .noFade()
//                    .resize(250, 250)
////                    .noPlaceholder()
//                    .placeholder(R.raw.bebe_sample)
//                    .centerInside()
//                    .into(imageView);
//            Picasso.with(StartGame.this)
//                    .load(mHeartsds[position])
//                    .noFade()
//                    .resize(200, 200)
//                    .noPlaceholder()
////                    .placeholder(R.raw.bebe_sample)
//                    .centerCrop()
//                    .into(imageView);

//            Picasso.with(StartGame.this)
//                    .load(mThumbIds[position])
////                    .placeholder(R.raw.place_holder)
////                    .error(R.raw.big_problem)
//                    .noFade()
//                    .resize(250, 250)
//                    .centerInside()
////                    .fit()
//                    .into(imageView);
            return imageView;
        }
    }
}
