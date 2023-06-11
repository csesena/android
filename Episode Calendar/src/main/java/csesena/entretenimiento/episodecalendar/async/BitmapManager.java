package csesena.entretenimiento.episodecalendar.async;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
//import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Csesena on 05/03/2015.
 */
public enum BitmapManager {
    INSTANCE;

    private final Map<String, SoftReference<Bitmap>> cache;
    private final ExecutorService pool;
    private Map<ImageView, String> imageViews = Collections
            .synchronizedMap(new WeakHashMap<ImageView, String>());
    private Bitmap placeholder;
    private Context context;

    BitmapManager() {
        cache = new HashMap<String, SoftReference<Bitmap>>();
        pool = Executors.newFixedThreadPool(5);
    }

    public void setPlaceholder(Bitmap bmp) {
        placeholder = bmp;
    }

    public Bitmap getBitmapFromCache(String url) {
        if (cache.containsKey(url)) {
            return cache.get(url).get();
        }

        return null;
    }

    public void queueJob(final String url, final ImageView imageView,
                         final int height) {
  /* Create handler in UI thread. */
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String tag = imageViews.get(imageView);
                if (tag != null && tag.equals(url)) {
                    if (msg.obj != null) {
                        imageView.setImageBitmap((Bitmap) msg.obj);
                    } else {
                        imageView.setImageBitmap(placeholder);
                      //  Log.d(null, "fail " + url);
                    }
                }
            }
        };

        pool.submit(new Runnable() {
            @Override
            public void run() {
                final Bitmap bmp = downloadBitmap(url, height);
                Message message = Message.obtain();
                message.obj = bmp;
               // Log.d(null, "Item downloaded: " + url);

                handler.sendMessage(message);
            }
        });
    }

    public void loadBitmap(final String url, final ImageView imageView,
                           final int height) {
        context = imageView.getContext();
        imageViews.put(imageView, url);
        Bitmap bitmap = getBitmapFromCache(url);

        // check in UI thread, so no concurrency issues
        if (bitmap != null) {
           // Log.d(null, "Item loaded from cache: " + url);
            imageView.setImageBitmap(bitmap);
        } else {
            bitmap = retrieveBitmapFromCacheDirectory(url);
            if (bitmap != null) {
              //  Log.d(null, "Item loaded from cache directory: " + url);
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageBitmap(placeholder);
                queueJob(url, imageView, height);
            }
        }
    }

    private Bitmap downloadBitmap(String url, int height) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(
                    url).getContent());
            int width = height * bitmap.getWidth() / bitmap.getHeight();
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            cache.put(url, new SoftReference<>(bitmap));
            storeBitmap(url,bitmap);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void storeBitmap(String url, Bitmap bitmap) {
        String filename = String.valueOf(url.hashCode());
        File f = new File(getCacheDirectory(context), filename);

        writeFile(bitmap, f);
    }

    private Bitmap retrieveBitmapFromCacheDirectory(String url) {
        String filename = String.valueOf(url.hashCode());
        File f = new File(getCacheDirectory(context), filename);

        Bitmap bitmap = null;

        if (f.exists()) {
            bitmap = BitmapFactory.decodeFile(f.getPath());
            cache.put(url, new SoftReference<>(bitmap));
        }

        return bitmap;
    }

    // Find the dir to save cached images
    private static File getCacheDirectory(Context context){
        String sdState = android.os.Environment.getExternalStorageState();
        File cacheDir;

        if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
            File sdDir = android.os.Environment.getExternalStorageDirectory();

            //Directory to store the pics
            cacheDir = new File(sdDir,"csesena/episode_calendar");
        }
        else
            cacheDir = context.getCacheDir();

        if(!cacheDir.exists())
            cacheDir.mkdirs();
        return cacheDir;
    }

    private void writeFile(Bitmap bmp, File f) {
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG, 80, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try { if (out != null ) out.close(); }
            catch(Exception ignored) {}
        }
    }
}