package cn.sdgundam.comicatsdgo;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.imbryk.viewpager.LoopViewPager;
import com.viewpagerindicator.LinePageIndicator;
import com.viewpagerindicator.PageIndicator;

import cn.sdgundam.comicatsdgo.data_model.CarouselInfo;

/**
 * Created by xhguo on 9/28/2014.
 */
public class CarouselFragment extends Fragment {
    ViewPager pager;

    CarouselInfo[] carousel;

    public CarouselInfo[] getCarousel() {
        return carousel;
    }

    public void setCarousel(CarouselInfo[] carousel) {
        this.carousel = carousel;

        View rootView = getView();

        pager = (LoopViewPager) rootView.findViewById(R.id.the_view_pager);
        pager.setAdapter(new CarouselPagerAdapter(getFragmentManager()));
        // pager.setPageTransformer(true, new ZoomOutPageTransformer());

        PageIndicator indicator = (LinePageIndicator) rootView.findViewById(R.id.indicator);
        indicator.setViewPager(pager);
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

//    class CarouselPagerAdapter2 extends PagerAdapter {
//
//    }

    class CarouselPagerAdapter extends FragmentPagerAdapter {

        CarouselPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {
            int realPosition= (carousel.length + i - 1) % carousel.length;
            CarouselInfo ci = carousel[realPosition];

            CarouselItemFragment f = new CarouselItemFragment();

            Bundle args = new Bundle();
            args.putString("imageURL", ci.getImageURL());
            f.setArguments(args);

            return f;
        }

        @Override
        public int getCount() {
            return carousel.length;
        }
//
//        @Override
//        public void onScrollChanged() {
//            Log.d("Scroll", "ScrollChanged");
//        }
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
