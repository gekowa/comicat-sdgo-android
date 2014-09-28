package cn.sdgundam.comicatsdgo;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.viewpagerindicator.LinePageIndicator;
import com.viewpagerindicator.PageIndicator;

import cn.sdgundam.comicatsdgo.data_model.CarouselInfo;

/**
 * Created by xhguo on 9/28/2014.
 */
public class CarouselFragment extends Fragment {
    static final int INDEX_SEED = 500;

    ViewPager pager;

    CarouselInfo[] carousel;

    public CarouselInfo[] getCarousel() {
        return carousel;
    }

    public void setCarousel(CarouselInfo[] carousel) {
        this.carousel = carousel;

        pager.setAdapter(new CarouselPagerAdapter(getFragmentManager()));
        pager.setCurrentItem(INDEX_SEED * carousel.length);
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
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_carousel, container, false);

        pager = (ViewPager) root.findViewById(R.id.the_view_pager);

        PageIndicator indicator = (LinePageIndicator) root.findViewById(R.id.indicator);

        return root;

    }

    class CarouselPagerAdapter extends FragmentPagerAdapter
        implements ViewTreeObserver.OnScrollChangedListener {

        static final int BIG_NUMBER_OF_ITEMS = 10000;

        CarouselPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {
            int realIndex = i % carousel.length;
            CarouselInfo ci = carousel[realIndex];

            CarouselItemFragment f = new CarouselItemFragment();

            Bundle args = new Bundle();
            args.putString("imageURL", ci.getImageURL());
            f.setArguments(args);

            return f;
        }

        @Override
        public int getCount() {
            return BIG_NUMBER_OF_ITEMS;
        }

        @Override
        public void onScrollChanged() {
            Log.d("Scroll", "ScrollChanged");
        }
    }
}
