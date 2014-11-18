package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xhguo on 11/17/2014.
 */
public class ScrollingTextView extends View {
    private String text;
    private int speed;

    private Timer scroller;
    private float stringWidth;
    private Rect textBounds;
    private boolean running;
    private float position;

    private static TextPaint textPaint;

    private Handler taskHandler;

    private static final int SCROLL_TEXT_GAP = 37;

    public ScrollingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        taskHandler = new Handler();
    }

    static TextPaint getTextPaint() {
        if (textPaint == null) {
            textPaint = new TextPaint();
            textPaint.setTextSize(30);
        }
        return textPaint;
    }

    public void setText(String text) {
        this.text = text;

        position = 0;
        stringWidth = getTextPaint().measureText(text);
        textBounds = new Rect();
        getTextPaint().getTextBounds(text, 0, text.length(), textBounds);

        if (stringWidth >= this.getWidth()) {
            setupScroller();
        }
    }

    public void setSpeed(int speed) {
        this.speed = speed;

        scroller = null;
        if (stringWidth >= this.getWidth()) {
            setupScroller();
        }
    }

    void setupScroller() {
        if (scroller == null && speed > 0 && text != null) {
            scroller = new Timer("Scroller");
            scroller.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (running) {
                        position -= 0.5f;

                        if (position + stringWidth < 0) {
                            position = SCROLL_TEXT_GAP;
                        }

                        if (position == 0) {
                            running = false;
                            taskHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    running = true;
                                }
                            }, 2000);
                        }

                        ScrollingTextView.this.post(new Runnable() {
                            @Override
                            public void run() {
                                invalidate();
                            }
                        });
                    }
                }
            }, 0, speed);
            running = true;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawText(text, position, -textBounds.top, textPaint);

        if (stringWidth > this.getWidth() &&
                position < this.getWidth() - stringWidth) {
            float position2 = position + stringWidth + SCROLL_TEXT_GAP;
            canvas.drawText(text, position2, -textBounds.top, textPaint);
        }
    }
}
