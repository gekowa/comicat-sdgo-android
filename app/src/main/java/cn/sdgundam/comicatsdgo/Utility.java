package cn.sdgundam.comicatsdgo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xhguo on 9/25/2014.
 */
public class Utility {
    public static Date parseDateSafe(String dateString) {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            return dateFormat.parse(dateString);
        }
        catch(ParseException e) {
            return new Date();
        }
    }

    public static String getUnitImageURLByUnitId(String unitId) {
        return String.format("http://cdn.sdgundam.cn/data-source/acc/unit-3g/%s.png", unitId);
    }

    public static String dateStringByDay(Context ctx, Date date) {
        String result = "";

        Date now = new Date();
        float delta = (now.getTime() - date.getTime()) / 1000;

        if (delta < 86400) {
            result = ctx.getString(R.string.date_today);
        } else if (delta < (86400 * 2)) {
            result = ctx.getString(R.string.date_yesterday);
        } else if (delta< (86400 * 7)) {
            result = (int) Math.floor(delta / 86400.0) + ctx.getString(R.string.date_days_before);
        } else {
            result = SimpleDateFormat.getDateInstance().format(date);
        }

        return result;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public static Bitmap convertViewToBitmap(View view){
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        return bitmap;
    }

    public static void showNetworkErrorAlertDialog(Context context, Throwable e) {
        AlertDialog alert = new AlertDialog.Builder(context).create();
        alert.setTitle(context.getString(R.string.data_loading_failure));
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (Message)null);

        Class throwableType = e.getClass();

        if (IOException.class.isAssignableFrom(throwableType)) {
            alert.setMessage(context.getString(R.string.network_disconnected));
        }

        alert.show();
    }

    public static Intent makeVideoViewActivityIntent(Context context, int postId, String videoHost, String videoId, String videoId2) {
        Intent intent = new Intent(context, VideoViewActivity.class);

        Bundle b = new Bundle();
        b.putInt("id", postId);
        b.putString("videoHost", videoHost);
        b.putString("videoId", videoId);
        b.putString("videoId2", videoId2);

        intent.putExtras(b);

        return intent;
    }
}
