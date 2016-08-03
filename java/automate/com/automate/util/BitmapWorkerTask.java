package automate.com.automate.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by varampra.
 */
public class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
    private Context context;
    private WeakReference<ImageView> imageViewWeakReference;
    private int requiredWidth;
    private int requiredHeight;
    private int data = 0;

    public BitmapWorkerTask(Context appContext,
                            ImageView imageView,
                            int width,
                            int height) {
        context = appContext;
        imageViewWeakReference = new WeakReference<ImageView>(imageView);
        requiredWidth = width;
        requiredHeight = height;
    }

    public static int calculateSampleSize(BitmapFactory.Options options,
                                          int requiredWidth,
                                          int requiredHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int sampleSize = 1;

        if (height > requiredHeight || width > requiredWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / sampleSize) > requiredHeight
                    && (halfWidth / sampleSize) > requiredWidth) {
                sampleSize *= 2;
            }
        }

        return sampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId,
                                                         int reqWidth,
                                                         int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calculateSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    @Override
    protected Bitmap doInBackground(Integer... params) {
        data = params[0];
        return decodeSampledBitmapFromResource(context.getResources(),
                data,
                requiredWidth,
                requiredHeight);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewWeakReference != null && bitmap != null) {
            final ImageView imageView = imageViewWeakReference.get();
            if(imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
