package cn.sdgundam.comicatsdgo.extension;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TableRow;

/**
 * Created by xhguo on 10/4/2014.
 */
public class BorderedTableRow extends TableRow {
    public static final int BORDER_NONE = 0;
    public static final int BORDER_TOP = 1;
    public static final int BORDER_BOTTOM = 2;
    public static final int BORDER_CENTER_SPLIT = 4;

    int border;
    int borderColor = Color.LTGRAY;
    int borderWidth = 1;


    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }

    public BorderedTableRow(Context context) {
        this(context, null);
    }

    public BorderedTableRow(Context context, AttributeSet attrs) {
        super(context);

        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        Paint paint = new Paint();
        paint.setColor(borderColor);
        paint.setStrokeWidth(borderWidth);

        if ((border & BORDER_TOP) == BORDER_TOP) {
            canvas.drawLine(0, 0, width, 0, paint);
        }

        if ((border & BORDER_BOTTOM) == BORDER_BOTTOM) {
            canvas.drawLine(0, height - 1, width - 1, height - 1, paint);
        }

        if ((border & BORDER_CENTER_SPLIT) == BORDER_CENTER_SPLIT) {
            canvas.drawLine(width / 2, 0, width / 2, height, paint);
        }
    }
}
