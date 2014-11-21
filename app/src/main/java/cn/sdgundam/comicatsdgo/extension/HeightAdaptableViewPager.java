package cn.sdgundam.comicatsdgo.extension;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xhguo on 11/20/2014.
 */
public class HeightAdaptableViewPager extends ViewPager {
    public HeightAdaptableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    int lastHeight;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View view = findViewWithTag(getCurrentItem());
        if (view != null) {
            view.measure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(getMeasuredWidth(), measureHeight(heightMeasureSpec, view));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private int measureHeight(int measureSpec, View view) {
        int height = lastHeight;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            height = specSize;
        } else {
            // set the height from the base view if available
            if (view != null) {
                height = view.getMeasuredHeight();
            }
            if (specMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, specSize);
            }
        }
        return height;
    }
}
