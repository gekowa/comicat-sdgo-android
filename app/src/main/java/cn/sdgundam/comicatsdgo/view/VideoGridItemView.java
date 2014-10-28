package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.Utility;
import cn.sdgundam.comicatsdgo.data_model.VideoListItem;

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

    VideoListItem vli;
    Target imageTarget;
    Bitmap image;
    Bitmap gdCategoryDrawingCache;

    public VideoGridItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setWillNotDraw(false);

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

        titlePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        titlePaint.setColor(Color.BLACK);
        titlePaint.setTextSize(titleTextSize);

        title2Paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        title2Paint.setColor(Color.WHITE);
        title2Paint.setTextSize(titleTextSize);

        datePaint = new TextPaint();
        datePaint.setColor(Color.LTGRAY);
        datePaint.setTextSize(dateTextSize);
    }

    public void setVli(VideoListItem vli) {
        this.vli = vli;

        Picasso.with(getContext()).load(vli.getImageURL()).into(imageTarget);

        GDCategorySmallIconView iconView = new GDCategorySmallIconView(getContext(), null);
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT
//        );
//        iconView.setLayoutParams(layoutParams);

        iconView.setGdCategory(vli.getGdPostCategory());
        gdCategoryDrawingCache = Utility.convertViewToBitmap(iconView);

//        TextView titleView = (TextView)findViewById(R.id.title);
//        titleView.setText(vli.getTitle());
//
//        TextView title2View = (TextView)findViewById(R.id.title2);
//        title2View.setText(vli.getTitle2());
//
//        ImageView imageView = (ImageView)findViewById(R.id.image_view);
//        // imageView.getLayoutParams().width
//
//        Picasso.with(getContext()).load(vli.getImageURL()).into(imageView);
//
//        GDCategorySmallIconView iconView = (GDCategorySmallIconView)findViewById(R.id.gd_icon_small);
//        iconView.setGdCategory(vli.getGdPostCategory());
//
//        TextView dateView = (TextView)findViewById(R.id.date_text_view);
//        dateView.setText(Utility.dateStringByDay(getContext(), vli.getCreated()));
//
//        requestLayout();
//        invalidate();

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
            // TODO: draw place holder
        } else {
            // image
            // Bitmap resizedImage = Utility.getResizedBitmap(image, width, imageHeight);
            canvas.drawBitmap(image, null, new Rect(0, 0, width, imageHeight), null);
        }

        int textStripHeight = (int)((float)imageHeight * TEXT_STRIP_ASPECT);

        // overlay
        Paint transGreyPaint = new Paint();
        transGreyPaint.setColor(Color.argb(60, 100, 100, 100));
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
