package cn.sdgundam.comicatsdgo;

import android.app.ActionBar;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.imbryk.viewpager.LoopViewPager;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.LinePageIndicator;
import com.viewpagerindicator.PageIndicator;

import java.util.ArrayList;

import cn.sdgundam.comicatsdgo.data_model.CarouselInfo;

/**
 * Created by xhguo on 9/28/2014.
 */
public class CarouselFragment extends Fragment {
    static final int MESSAGE_SCROLL = 0;
    static final int AUTO_SCROLL_INTERVAL = 3000;
    Handler autoScrollHandler;

    ViewPager pager;

    CarouselInfo[] carousel;

    public CarouselInfo[] getCarousel() {
        return carousel;
    }

    public void setCarousel(CarouselInfo[] carousel) {
        this.carousel = carousel;

        View rootView = getView();

        pager = (LoopViewPager) rootView.findViewById(R.id.the_view_pager);
        pager.setAdapter(new CarouselPageAdapter2());
        // pager.setPageTransformer(true, new ZoomOutPageTransformer());
        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                    stopAutoScroll();
                } else if (ev.getAction() == MotionEvent.ACTION_UP) {
                    startAutoScroll(AUTO_SCROLL_INTERVAL);
                }
                return false;
            }
        });

        PageIndicator indicator = (LinePageIndicator) rootView.findViewById(R.id.indicator);
        indicator.setViewPager(pager);

        autoScrollHandler = new AutoScrollHandler();
        startAutoScroll(AUTO_SCROLL_INTERVAL);
    }

    public CarouselFragment() { }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_carousel, container, false);
    }

    public void startAutoScroll(int delayTimeInMills) {
        sendScrollMessage(delayTimeInMills);
    }

    /**
     * stop auto scroll
     */
    public void stopAutoScroll() {
        autoScrollHandler.removeMessages(MESSAGE_SCROLL);
    }

    void scrollOnce() {
        PagerAdapter adapter = pager.getAdapter();
        int currentItem = pager.getCurrentItem();
        if (adapter == null || carousel.length == 0) {
            return;
        }

        int nextItem = currentItem + 1;
        if (nextItem == carousel.length) {
            pager.setCurrentItem(0, true);
        } else {
            pager.setCurrentItem(nextItem, true);
        }
    }

    void sendScrollMessage(long delayTimeInMills) {
        autoScrollHandler.removeMessages(MESSAGE_SCROLL);
        autoScrollHandler.sendEmptyMessageDelayed(MESSAGE_SCROLL, delayTimeInMills);
    }

    class CarouselPageAdapter2 extends PagerAdapter {
        ArrayList<ImageView> imageViews;

        public CarouselPageAdapter2() {
            super();

            imageViews = new ArrayList<ImageView>();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(getActivity());

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );

            imageView.setLayoutParams(layoutParams);

            int realPosition= (carousel.length + position) % carousel.length;
            CarouselInfo ci = carousel[realPosition];

            Picasso.with(getActivity()).setIndicatorsEnabled(true);
            Picasso.with(getActivity()).load(ci.getImageURL()).into(imageView);

            container.addView(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView)object);
        }

        @Override
        public int getCount() {
            return carousel.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return (ImageView)view == (ImageView)o;
        }
    }

    class AutoScrollHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch(msg.what) {
                case MESSAGE_SCROLL:
                    scrollOnce();
                    sendScrollMessage(AUTO_SCROLL_INTERVAL);
            }
        }
    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
