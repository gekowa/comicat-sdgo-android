package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import cn.sdgundam.comicatsdgo.data_model.PostInfo;

/**
 * Created by xhguo on 10/4/2014.
 */
public class PostListForHomeItemView extends View {
    PostInfo post;

    public PostInfo getPost() {
        return post;
    }

    public void setPost(PostInfo post) {
        this.post = post;

        invalidate();
        requestLayout();
    }

    public PostListForHomeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (post == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            setMeasuredDimension(1000, 200);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        TextPaint paint = new TextPaint();
        paint.setColor(Color.parseColor("#000000"));
        paint.setTextSize(12);

        if (post != null) {
            canvas.drawText(post.getTitle(), 0, 0, paint);
        }
    }
}
