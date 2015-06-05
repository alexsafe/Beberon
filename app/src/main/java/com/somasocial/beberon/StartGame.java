package com.somasocial.beberon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by SOMA on 04/06/15.
 */
public class StartGame extends Activity {

//    static Integer[] mThumbIds = {R.raw.bebe_sample, R.raw.bebe_sample,
//            R.raw.bebe_sample, R.raw.bebe_sample};
//    static Integer[] mThumbIds1 = {R.raw.bebe_sample, R.raw.bebe_sample,
//            R.raw.bebe_sample, R.raw.bebe_sample};
//    static Integer[] mThumbIds2 = {R.raw.bebe_sample, R.raw.bebe_sample,
//            R.raw.bebe_sample, R.raw.bebe_sample};
    GridView gridview;
    ProgressBar progressBar;
    TextView messageText,subText;
    int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game_layout);
        page = 0;
        gridview = (GridView) findViewById(R.id.gridView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        messageText=(TextView) findViewById(R.id.textView3);
        subText=(TextView) findViewById(R.id.smalltext);

        progressBar.setProgress(0);
        progressBar.setMax(4);

        Log.d("test", "gridview width:" + gridview.getMeasuredWidth());
//        gridview.setColumnWidth(200);
        gridview.setAdapter(new ImageAdapter(this, 0));

        //set invisible
        progressBar.setVisibility(View.INVISIBLE);
        subText.setVisibility(View.INVISIBLE);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //change adapter and UI

                Intent intent=new Intent(StartGame.this, MainViewActivity.class);
//                    Intent intent=new Intent(StartGame.this, MainActivity.class);
                startActivity(intent);
                page++;
                int picturesRemaining=4-page;

                Log.d("test", "clicked on " + position + " page: " + page);
                if (picturesRemaining==0)
                {

                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    subText.setVisibility(View.VISIBLE);
                    progressBar.setProgress(page);
                    messageText.setText("Great Choice");

                    subText.setText("choose " + picturesRemaining + "more cute pictures to grab a bebecoin");
                    gridview.setAdapter(new ImageAdapter(StartGame.this.getApplicationContext(), page));
                }
            }
        });
    }

    //    our custom adapter
    private class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private int mNo;

        public ImageAdapter(Context context, int no) {
            mContext = context;
            mNo = no;
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

//            Picasso.with(StartGame.this).setLoggingEnabled(true);
            Integer[] mThumbIds = null;
            if (mNo<4) {
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
            Picasso.with(StartGame.this)
                    .load(mThumbIds[position])
//                    .placeholder(R.raw.place_holder)
//                    .error(R.raw.big_problem)
                    .noFade()
                    .resize(250, 250)
                    .centerInside()
//                    .fit()
                    .into(imageView);
            return imageView;
        }
    }
}
