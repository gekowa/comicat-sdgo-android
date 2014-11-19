package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.textservice.TextServicesManager;

import java.util.Timer;
import java.util.TimerTask;

import cn.sdgundam.comicatsdgo.Utility;

/**
 * Created by xhguo on 11/17/2014.
 */
public class ScrollingTextView extends View {
    private String text;
    private int textSize = 15;
    private int textColor = Color.BLACK;
    private int speed;

    private Timer scroller;
    private float stringWidth;
    private Rect textBounds;
    private boolean running;
    private float textPosition;

    private static TextPaint textPaint;

    private Handler taskHandler;

    private static final String GAP_TEXT = "GDRO";
    private static int textGap = 60;

    public ScrollingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        taskHandler = new Handler();

        textPaint = new TextPaint();
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
    }

    public void setText(String text) {
        this.text = text;

        textPosition = 0;
        stringWidth = textPaint.measureText(text);

        textBounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), textBounds);

        if (stringWidth >= this.getWidth()) {
            setupScroller();
        }
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        textPaint.setTextSize(textSize);
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        textPaint.setColor(textColor);
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
            Rect gapTextBounds = new Rect();
            textPaint.getTextBounds(GAP_TEXT, 0, GAP_TEXT.length(), gapTextBounds);
            textGap = gapTextBounds.right - gapTextBounds.left;

            scroller = new Timer("ScrollingText");
            scroller.schedule(new TimerTask() {

                @Override
                public void run() {
                    if (running) {
                        textPosition -= 1.8f;

                        if (textPosition + stringWidth < 0) {
                            textPosition = textGap;
                        }

                        if ((int)textPosition == 0) {
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
            }, 2000, speed);

            running = true;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (textBounds != null) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
                    textBounds.bottom - textBounds.top + getPaddingTop() + getPaddingBottom());
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawText(text, textPosition, -textBounds.top, textPaint);

        if (stringWidth > this.getWidth() &&
                textPosition < this.getWidth() - stringWidth) {
            float position2 = textPosition + stringWidth + textGap;
            canvas.drawText(text, position2, -textBounds.top, textPaint);
        }
    }
}
