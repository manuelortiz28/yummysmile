package com.visiontech.yummysmile.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Utility class to process the logic on images.
 *
 * @author hetorres
 */
@Singleton
public class RenderImageHelper {
    private static final String TAG = RenderImageHelper.class.getName();
    private static final String IMAGE_EXTENSION = ".jpg";
    private static final String IMAGE_FORMAT = "JPEG";
    private static final String PHOTO_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String UNDER_SCORE = "_";
    private static final int DESIRE_SIZE = 600;
    private static final int QUALITY_PICTURE = 100;

    private Bitmap renderedBitmap;
    private File imageToUpload;

    @Inject
    public RenderImageHelper() {
    }

    public Bitmap getRenderedBitmap() {
        return renderedBitmap;
    }

    public File getImageToUpload() {
        return imageToUpload;
    }

    /**
     * Method that create the file to save on disk.
     */
    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat(PHOTO_TIME_FORMAT, Locale.getDefault()).format(new Date());
        StringBuilder imageFileName = new StringBuilder()
                .append(IMAGE_FORMAT)
                .append(timeStamp)
                .append(UNDER_SCORE);
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName.toString(),  /* prefix */
                IMAGE_EXTENSION, /* suffix */
                storageDir      /* directory */
        );

        Log.d(TAG, "createImageFile(): " + image.getAbsolutePath());
        return image;
    }

    /**
     * Method that resize the image before showing in preview.
     */
    public boolean resizeFileBitMap(String filePath) {
        Log.d(TAG, "6_setPicture()_");
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / DESIRE_SIZE, photoH / DESIRE_SIZE);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Log.d(TAG, "_currentPhotoPath:_" + filePath);
        renderedBitmap = BitmapFactory.decodeFile(filePath, bmOptions);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        renderedBitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY_PICTURE, bos);

        try {
            imageToUpload = new File(filePath);
            FileOutputStream fo = new FileOutputStream(imageToUpload);
            fo.write(bos.toByteArray());
            fo.close();
            return true;
        } catch (IOException e) {
            Log.d(TAG, "_IOException_Message" + e.getMessage());
            Log.d(TAG, "_IOException_Cause" + e.getCause());
            e.printStackTrace();
            return false;
        }
    }
}
