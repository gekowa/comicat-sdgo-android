package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import cn.sdgundam.comicatsdgo.R;

/**
 * Created by xhguo on 10/15/2014.
 */
public class NetworkErrorView extends LinearLayout {
    public NetworkErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.network_error_view, this, true);

        Button reloadButton = (Button)findViewById(R.id.reload_button);
//        reloadButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NetworkErrorView.this.onTouchEvent(null);
//            }
//        });

        reloadButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return NetworkErrorView.this.onTouchEvent(motionEvent);
            }
        });
    }
}
