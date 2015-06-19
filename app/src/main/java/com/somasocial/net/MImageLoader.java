package com.somasocial.net;

/**
 * Created by SOMA on 16/06/15.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Stack;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

public class MImageLoader {

    // the simplest in-memory cache implementation. This should be replaced with
    // something like SoftReference or BitmapOptions.inPurgeable(since 1.6)
    private HashMap<String, Drawable> cache = new HashMap<String, Drawable>();

    private File cacheDir;

    public MImageLoader(Context context) {
        // Make the background thead low priority. This way it will not affect
        // the UI performance
        photoLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);
        // Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
            cacheDir = new File(
                    android.os.Environment.getExternalStorageDirectory(),
                    "LokeBoost/Cache/");
        else
            cacheDir = context.getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();
    }

//	final int stub_id = R.drawable.stub_image;

    public void DisplayImage(String url, Activity activity, ImageView imageView) {
        queuePhoto(url, activity, imageView);
//			imageView.setImageResource(stub_id);
    }

    private void queuePhoto(String url, Activity activity, ImageView imageView) {
        photosQueue.Clean(imageView);
        PhotoToLoad p = new PhotoToLoad(url, imageView);
        synchronized (photosQueue.photosToLoad) {
            photosQueue.photosToLoad.push(p);
            photosQueue.photosToLoad.notifyAll();
        }
        if (photoLoaderThread.getState() == Thread.State.NEW)
            photoLoaderThread.start();
    }

    private Drawable getBitmap(String url) {
        try {
            String filename = String.valueOf(url.hashCode());
            File f = new File(cacheDir, filename);
            // from SD cache
            Drawable b = decodeFile(f);
            if (b != null)
                return b;
            // from web
            try {
                Drawable bitmap = null;
                InputStream is = new URL(url).openStream();
                OutputStream os = new FileOutputStream(f);
                M.CopyStream(is, os);
                os.close();
                bitmap = decodeFile(f);
                return bitmap;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        } catch (NullPointerException m) {
            m.printStackTrace();
            return null;
        }
    }

    // decodes image and scales it to reduce memory consumption
    @SuppressWarnings("deprecation")
    private Drawable decodeFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 10000;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = 6;
            o2.inPurgeable = true;
            o2.inPreferredConfig = Bitmap.Config.RGB_565;
            return new BitmapDrawable(BitmapFactory.decodeStream(
                    new FileInputStream(f), null, o2));
        } catch (FileNotFoundException e) {
            return null;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    // Task for the queue
    private class PhotoToLoad {
        public String url;
        public ImageView imageView;

        public PhotoToLoad(String u, ImageView i) {
            url = u;
            imageView = i;
        }
    }

    PhotosQueue photosQueue = new PhotosQueue();

    public void stopThread() {
        photoLoaderThread.interrupt();
    }

    // stores list of photos to download
    class PhotosQueue {
        private Stack<PhotoToLoad> photosToLoad = new Stack<PhotoToLoad>();

        // removes all instances of this ImageView
        public void Clean(ImageView image) {
            for (int j = 0; j < photosToLoad.size();) {
                if (photosToLoad.get(j).imageView == image)
                    photosToLoad.remove(j);
                else
                    ++j;
            }
        }
    }

    class PhotosLoader extends Thread {
        public void run() {
            try {
                while (true) {
                    // thread waits until there are any images to load in the
                    // queue
                    if (photosQueue.photosToLoad.size() == 0)
                        synchronized (photosQueue.photosToLoad) {
                            photosQueue.photosToLoad.wait();
                        }
                    if (photosQueue.photosToLoad.size() != 0) {
                        PhotoToLoad photoToLoad;
                        synchronized (photosQueue.photosToLoad) {
                            photoToLoad = photosQueue.photosToLoad.pop();
                        }
                        Drawable bmp = getBitmap(photoToLoad.url);
                        cache.put(photoToLoad.url, bmp);
                        if (((String) photoToLoad.imageView.getTag())
                                .equals(photoToLoad.url)) {
                            BitmapDisplayer bd = new BitmapDisplayer(bmp,
                                    photoToLoad.imageView);
                            Activity a = (Activity) photoToLoad.imageView
                                    .getContext();
                            a.runOnUiThread(bd);
                        }
                    }
                    if (Thread.interrupted())
                        break;
                }
            } catch (InterruptedException e) {
                // allow thread to exit
            } catch (NullPointerException m) {
                m.printStackTrace();
            }
        }
    }
    PhotosLoader photoLoaderThread = new PhotosLoader();
    // Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {
        Drawable bitmap;
        ImageView imageView;

        public BitmapDisplayer(Drawable b, ImageView i) {
            bitmap = b;
            imageView = i;
        }

        public void run() {
            if (bitmap != null)
                imageView.setImageDrawable(bitmap);
        }
    }

    public void clearCache() {
        cache.clear();
        File[] files = cacheDir.listFiles();
        for (File f : files)
            f.delete();
    }

    @SuppressWarnings("deprecation")
    public Drawable resizeBitmapAndScale(String url, int displayWidth,
                                         int displayHeight) throws FileNotFoundException {
        String filename = String.valueOf(url.hashCode());
        File f = new File(cacheDir, filename);
        Log.v("string TAG", "display width is:" + displayWidth + "h:" + displayHeight);
        // decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        o.inScaled = false;
        int density = o.inDensity;
        try {
            new BitmapDrawable(BitmapFactory.decodeStream(
                    new FileInputStream(f), null, o));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int bmpWidth = o.outWidth;
        int bmpHeight = o.outHeight;

		/*A) if dx is smaller than x:
		    1) Resize the image propotionally.
		    2) So for x geting value of dx, y will be (x*dy)/dx
		    3) After resizecheck if dy is smaller than the new y and if yes then crop the rest height of the photo.

		B) If dx is equal to x:
		    1) We dont resize.
		    2) Check if dy is smaller than the new y and if yes then crop the rest height of the photo.

		C) If dx is greater than x:
		    1) We dont resize.
		    2) Calculate the new drawing position of the display where the photo will be put on. This is different than (0,0) unlike in the other cases.
		        posx=(dx-x)/2;
		        So this will give us a point (posx,0). EXAMPLE: dx=100; x=50; posx is 25 leaving the same empty space from the left and the right.
		    3) Check if dy is smaller than the new y and if yes then crop the rest height of the photo.
*/
        // resizing image proportionally
        SoftReference<Bitmap> bmp = new SoftReference<Bitmap> (BitmapFactory.decodeStream(new FileInputStream(f)));
        if (displayWidth<bmpWidth) {
//		    1) Resize the image propotionally.
//		    2) So for x geting value of dx, y will be (x*dy)/dx
//			bmpHeight = (bmpHeight * displayHeight) / bmpWidth;
            bmpHeight = (displayWidth * bmpHeight) / bmpWidth;
            bmpWidth = displayWidth;
            Log.v("string TAG","CASE A");
            Log.v("string TAG", "image H:"+bmpHeight+"image W: "+bmpWidth);
            try {
                Bitmap x=null;
//			    3) After resizecheck if dy is smaller than the new y and if yes then crop the rest height of the photo.
                if(bmpHeight>displayHeight) {
                    Log.v("string TAG","CASE A1");
                    Rect rectangle = new Rect(0,0,displayWidth,displayHeight);
                    x = cropBitmap(Bitmap.createScaledBitmap(bmp.get(), bmpWidth, bmpHeight,
                            true),rectangle,density);
                    Log.v("string TAG","returned bitmap is W:" + x.getWidth());
                    Log.v("string TAG","W:"+x.getWidth()+"H:"+x.getHeight());
                    bmp.get().recycle();
                } else {
                    Log.v("string TAG","CASE A2");
                    x = Bitmap.createScaledBitmap(bmp.get(), bmpWidth, bmpHeight,
                            true);
                    Log.v("string TAG","returned bitmap is W:" + x.getWidth() + " H: "+x.getHeight());
                    x.setDensity(density);
                    bmp.get().recycle();

                }

                return new BitmapDrawable(x);
            } catch (OutOfMemoryError m) {
                m.printStackTrace();
                return null;
            }
//		    1) We dont resize.
        }	else if (bmpWidth==displayWidth){
            Log.v("string TAG","CASE B");

            // photo width equals display width
//		    2) Check if dy is smaller than the new y and if yes then crop the rest height of the photo.
            if (displayHeight<bmpHeight){
                Log.v("string TAG","W:"+displayWidth + " H: "+displayHeight);
                Rect rectangle = new Rect(0,0,displayWidth,displayHeight);
                BitmapDrawable m = new BitmapDrawable(cropBitmap(bmp.get(),rectangle,density));
                bmp.get().recycle();

                return m;
            } else return new BitmapDrawable(bmp.get());
        }
        else if (bmpWidth < displayWidth) {
            // no need to resize if image width lorew than display width
//		    2) Calculate the new drawing position of the display where the photo will be put on. This is different than (0,0) unlike in the other cases.
//	        posx=(dx-x)/2;
            Log.v("string TAG","CASE C");
            bmp.get().recycle();
            Bitmap ret=null;
            int posX = (displayWidth-bmpWidth)/2;

            if (displayHeight<bmpHeight){
                ret = Bitmap.createBitmap(bmpWidth+posX,displayHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(ret);
                Paint paint = new Paint();
                paint.setColor(Color.TRANSPARENT);
                Rect rectangle = new Rect(0,0,bmpWidth,bmpHeight);
                canvas.drawRect(rectangle, paint);
                canvas.drawBitmap(cropBitmap(bmp.get(),rectangle,density),posX,0,null); } else
            {
                ret = Bitmap.createBitmap(bmpWidth+posX,bmpHeight, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(ret);
                Paint paint = new Paint();
                paint.setColor(Color.TRANSPARENT);
                Rect rectangle = new Rect(0,0,bmpWidth,bmpHeight);
                canvas.drawRect(rectangle, paint);
                canvas.drawBitmap(BitmapFactory.decodeStream(new FileInputStream(f)),posX,0,null);

            }
            ret.setDensity(density);
            return new BitmapDrawable(ret);
            //return new BitmapDrawable(Bitmap.createBitmap(bmp, posX, 0, bmp.getWidth()+posX+1, bmpHeight));
        } else return null;
    }
    public static Bitmap cropBitmap(Bitmap bitmap, Rect rect, int density) {
        int w = rect.right - rect.left;
        int h = rect.bottom - rect.top;
        Bitmap ret = Bitmap.createBitmap(w, h, bitmap.getConfig());
        Canvas canvas = new Canvas(ret);
        canvas.drawBitmap(bitmap, -rect.left, -rect.top, null);
        ret.setDensity(density);
        bitmap.recycle();
        return ret;
    }
}

