package cn.sdgundam.comicatsdgo.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.animation.ResizeAnimation;

/**
 * Created by xhguo on 11/17/2014.
 */
public class GaugeBarView extends RelativeLayout {

    public static final int GAUGE_TYPE_ATTACK = 0;
    public static final int GAUGE_TYPE_DEFENSE = 1;
    public static final int GAUGE_TYPE_MOBILITY = 2;
    public static final int GAUGE_TYPE_CONTROL = 3;

    public static final int ANIMATION_DURATION = 610;

    ImageView gaugeImageView;
    TextView gaugeTextView;

    public GaugeBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GaugeBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (isInEditMode()) return;

        LayoutInflater.from(context).inflate(R.layout.view_gauge_bar, this, true);
        gaugeImageView = (ImageView) findViewById(R.id.gauge_image_view);
        gaugeTextView = (TextView) findViewById(R.id.gauge_text_view);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GaugeBarView, defStyle, 0);
        gaugeType = a.getInt(R.styleable.GaugeBarView_gaugeType, GAUGE_TYPE_ATTACK);

        a.recycle();

        // setup view
        setGaugeImage();
    }

    void setGaugeImage() {
        switch(gaugeType) {
            case GAUGE_TYPE_ATTACK:
                gaugeImageView.setImageResource(R.drawable.gauge_attack);
                break;
            case GAUGE_TYPE_DEFENSE:
                gaugeImageView.setImageResource(R.drawable.gauge_defense);
                break;
            case GAUGE_TYPE_MOBILITY:
                gaugeImageView.setImageResource(R.drawable.gauge_mobility);
                break;
            case GAUGE_TYPE_CONTROL:
                gaugeImageView.setImageResource(R.drawable.gauge_control);
                break;
        }
    }

    String textOnGauge;
    int gaugeType;
    Float gaugePercent;

    public void setTextOnGauge(String textOnGauge) {
        this.textOnGauge = textOnGauge;
        gaugeTextView.setText(textOnGauge);
    }

    public void setGaugePercent(Float gaugePercent) {
        this.gaugePercent = gaugePercent;
    }

    public void playAnimation() {
        ResizeAnimation ani1 = new ResizeAnimation(gaugeImageView, (int)((float)getWidth() * gaugePercent));
        ani1.setDuration(ANIMATION_DURATION);
        gaugeImageView.startAnimation(ani1);

        gaugeTextView.setVisibility(VISIBLE);
        AlphaAnimation ani2 = new AlphaAnimation(0f, 1.0f);
        ani2.setDuration(ANIMATION_DURATION);
        gaugeTextView.startAnimation(ani2);
    }
}

