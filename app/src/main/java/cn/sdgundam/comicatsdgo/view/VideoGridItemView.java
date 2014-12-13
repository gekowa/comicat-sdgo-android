package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import cn.sdgundam.comicatsdgo.BuildConfig;
import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.Utility;
import cn.sdgundam.comicatsdgo.api_model.VideoListItem;

/**
 * Created by xhguo on 10/8/2014.
 */
public class VideoGridItemView extends View {
    // 视频图宽高比
    static final float IMAGE_ASPECT = 1.785714285714286f;
    // 每个视频View的宽高比
    static final float VIEW_ASPECT = 1.11f;
    // 视频图上的文字黑色底色的比例
    static final float TEXT_STRIP_ASPECT = 0.2f;

    static TextPaint titlePaint;
    static TextPaint title2Paint;
    static TextPaint datePaint;

    static Bitmap placeHolderBitmap;

    VideoListItem vli;
    Target imageTarget;
    Bitmap image;
    Bitmap gdCategoryDrawingCache;

    public VideoGridItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        imageTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                VideoGridItemView.this.image = bitmap;

                VideoGridItemView.this.requestLayout();
                VideoGridItemView.this.invalidate();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        configurePaints();
    }

    void configurePaints() {
        float titleTextSize = getResources().getDimensionPixelSize(R.dimen.video_grid_title_text_size);
        float dateTextSize = getResources().getDimensionPixelSize(R.dimen.video_grid_date_text_size);

        if (titlePaint == null) {
            titlePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            titlePaint.setColor(Color.BLACK);
            titlePaint.setTextSize(titleTextSize);
        }

        if (title2Paint == null) {
            title2Paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            title2Paint.setColor(Color.WHITE);
            title2Paint.setTextSize(titleTextSize);
        }

        if (datePaint == null) {
            datePaint = new TextPaint();
            datePaint.setColor(Color.GRAY);
            datePaint.setTextSize(dateTextSize);
        }
    }

    public void setVli(VideoListItem vli) {
        if (this.vli != null && vli != null) {
            Log.d("setVli", "same as previous? " + (this.vli.getPostId() == vli.getPostId()) + " previous PostId " + this.vli.getPostId());
        }
        // only set if not equal or new
        if (this.vli == null || this.vli.getPostId() != vli.getPostId()) {
            this.vli = vli;

            // set to null before loading image in case the image is cached
            // also prepare for reuse
            image = null;

            Picasso.with(getContext()).load(vli.getImageURL()).into(imageTarget);

            GDCategorySmallIconView iconView = new GDCategorySmallIconView(getContext(), null);
            iconView.setGdCategory(vli.getGdPostCategory());
            gdCategoryDrawingCache = Utility.convertViewToBitmap(iconView);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (vli == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) ((float)width / VIEW_ASPECT);

            setMeasuredDimension(width, height);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = canvas.getWidth();
        int imageHeight = (int)((float)width / IMAGE_ASPECT);


        if (image == null) {
            if (placeHolderBitmap == null) {
                placeHolderBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.placeholde_video);
            }
            canvas.drawBitmap(placeHolderBitmap , null, new Rect(0, 0, width, imageHeight), null);
        } else {
            // image
            // Bitmap resizedImage = Utility.getResizedBitmap(image, width, imageHeight);
            canvas.drawBitmap(image, null, new Rect(0, 0, width, imageHeight), null);
        }

        int textStripHeight = (int)((float)imageHeight * TEXT_STRIP_ASPECT);

        // overlay
        Paint transGreyPaint = new Paint();
        transGreyPaint.setColor(Color.argb(100, 30, 30, 30));
        canvas.drawRect(0, imageHeight - textStripHeight, width, imageHeight, transGreyPaint);

        float textPaddingBottom  = Utility.convertDpToPixel(4.8f, getContext());

        // title2
        String title2Ellipsized = (String) TextUtils.ellipsize(vli.getTitle2(), title2Paint, width, TextUtils.TruncateAt.END);
        float title2Width = title2Paint.measureText(title2Ellipsized);

        canvas.drawText(title2Ellipsized, width - title2Width, imageHeight - textPaddingBottom , title2Paint);

        // title
        String titleEllipsized = (String) TextUtils.ellipsize(vli.getTitle(), titlePaint, width, TextUtils.TruncateAt.END);
        float titleTop = imageHeight + titlePaint.getTextSize() + textPaddingBottom / 2 /* Magic number */;
        canvas.drawText(titleEllipsized, 0, titleTop, titlePaint);

        float gdCategoryIconWidth = 0;
        if (gdCategoryDrawingCache != null) {
            // category icons
            canvas.drawBitmap(gdCategoryDrawingCache, 0, titleTop + Utility.convertDpToPixel(6, getContext()), null);
            gdCategoryIconWidth = gdCategoryDrawingCache.getWidth();
        }

        // date
        canvas.drawText(Utility.dateStringByDay(getContext(), vli.getCreated()),
                gdCategoryIconWidth + Utility.convertDpToPixel(6, getContext()),
                titleTop + Utility.convertDpToPixel(6, getContext()) + datePaint.getTextSize() + textPaddingBottom / 4 /* Magic number */,
                datePaint);

        if (BuildConfig.DEBUG) {
            TextPaint debugPaint = new TextPaint();
            debugPaint.setColor(Color.RED);
            debugPaint.setTextSize(100);
            canvas.drawText(vli.getPostId() + "", 10f, 100f, debugPaint);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        Picasso.with(getContext()).cancelRequest(imageTarget);
    }

//    class ImgaeLoaderAsyncTask extends AsyncTask<String, Void, Void> {
//        @Override
//        protected Void doInBackground(String... strings) {
//
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//        }
//
//        @Override
//        protected void onCancelled(Void aVoid) {
//            super.onCancelled(aVoid);
//        }
//    }
}
