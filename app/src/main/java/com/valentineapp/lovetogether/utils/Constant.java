package com.valentineapp.lovetogether.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by lequy on 2/6/2017.
 */

public class Constant {
    public static String DISPLAY_NOTI = "display_noti";
    public static String FINISH_ACTIVITY = "finish_activity";
    public static String CREATE_LOCK_VIEW = "create_lock_view";
    public static String CHANGE_WALLPAPER = "change_wallpaper";
    public static String CHANGE_NOTI = "change_noti";

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static int convertDpIntoPixels(int sizeInDp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sizeInDp, context.getResources().getDisplayMetrics());
        return px;
    }

    public static long daysBetween(Date one, Date two) {
        long diff = two.getTime() - one.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public static Bitmap decodeUri(Activity context, Uri selectedImage) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 500;

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
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o2);

    }

    public static void hideNavigationBar(Activity context) {
        final View decorView = context.getWindow().getDecorView();
        final int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
        {

            context.getWindow().getDecorView().setSystemUiVisibility(uiOptions);

            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(uiOptions);
                    }
                }
            });
        }
        decorView.setSystemUiVisibility(uiOptions);
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static String encodeImage(Bitmap selectedImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArr = baos.toByteArray();
        String encodedImage = Base64.encodeToString(byteArr, Base64.DEFAULT);
        return encodedImage;
    }

    public static Bitmap getBase64Img(String base64) {
        byte[] imageAsBytes = Base64.decode(base64, Base64.DEFAULT);
        Bitmap selectedImage = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        return selectedImage;
    }

    public static Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

    public static void initShareIntent(Context context, String type, Bitmap shareBody) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        shareBody.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), shareBody, "Title", null);
        Uri imageUri = Uri.parse(path);

        boolean found = false;
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("image/jpeg");

        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(share, 0);
        if (!resInfo.isEmpty()){
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains(type) ||
                        info.activityInfo.name.toLowerCase().contains(type)) {
                    share.putExtra(Intent.EXTRA_STREAM, imageUri);
                    share.setPackage(info.activityInfo.packageName);
                    found = true;
                    break;
                }
            }
            if (!found) {
                Toast.makeText(context, "Ứng dụng này chưa được cài đặt", Toast.LENGTH_SHORT).show();
                return;
            }

            context.startActivity(Intent.createChooser(share, "Share via"));
        }
    }

    public static Typeface setRobotoFont(Context context) {
        Typeface mRobotoFont = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        return mRobotoFont;
    }

    public static String formatTime(int time) {
        String formatedTime = "";

        if (time < 10) {
            formatedTime = "0" + time;
        } else {
            formatedTime = "" + time;
        }

        return formatedTime;
    }

    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
}
