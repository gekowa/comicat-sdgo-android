package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
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
public class VideoGridItemView extends RelativeLayout {
    static final float IMAGE_ASPECT = 1.785714285714286f;
    static final float VIEW_ASPECT = 1.11f;

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
            Bitmap resizedImage = Utility.getResizedBitmap(image, width, imageHeight);
            canvas.drawBitmap(resizedImage, 0, 0, null);
        }
        int textStripHeight = (int)((float)imageHeight * 0.2);

        // overlay
        Paint transGreyPaint = new Paint();
        transGreyPaint.setColor(Color.argb(100, 100, 100, 100));
        canvas.drawRect(0, imageHeight - textStripHeight, width, imageHeight, transGreyPaint);

        float textPaddingBottom  = Utility.convertDpToPixel(4.8f, getContext());
        float textSize = Utility.convertDpToPixel(12.5f, getContext());
        // title2
        TextPaint title2Paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        title2Paint.setColor(Color.WHITE);
        title2Paint.setTextSize(textSize);
        // title2Paint.setTextAlign(Paint.Align.RIGHT);

        String title2Ellipsized = (String) TextUtils.ellipsize(vli.getTitle2(), title2Paint, width, TextUtils.TruncateAt.END);
        float title2Width = title2Paint.measureText(title2Ellipsized);

        canvas.drawText(title2Ellipsized, width - title2Width, imageHeight - textPaddingBottom , title2Paint);

        // title
        TextPaint titlePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        titlePaint.setColor(Color.BLACK);
        titlePaint.setTextSize(textSize);
        String titleEllipsized = (String) TextUtils.ellipsize(vli.getTitle(), titlePaint, width, TextUtils.TruncateAt.END);
        float titleTop = imageHeight + titlePaint.getTextSize() + textPaddingBottom / 2;
        canvas.drawText(titleEllipsized, 0, titleTop, titlePaint);

        // category icons
        canvas.drawBitmap(gdCategoryDrawingCache, 0, titleTop + Utility.convertDpToPixel(6, getContext()), null);
    }

    @Override
    protected void onDetachedFromWindow() {
        Picasso.with(getContext()).cancelRequest(imageTarget);
    }
}
