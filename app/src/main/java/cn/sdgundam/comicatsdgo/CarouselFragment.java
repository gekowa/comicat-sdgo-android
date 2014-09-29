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
    ViewPager pager;

    CarouselInfo[] carousel;

    public CarouselInfo[] getCarousel() {
        return carousel;
    }

    public void setCarousel(CarouselInfo[] carousel) {
        this.carousel = carousel;

        View rootView = getView();

        pager = (ViewPager) rootView.findViewById(R.id.the_view_pager);

        pager.setAdapter(new CarouselPagerAdapter(getFragmentManager()));

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

    class CarouselPagerAdapter extends FragmentPagerAdapter
        implements ViewTreeObserver.OnScrollChangedListener {

        CarouselPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int i) {
            CarouselInfo ci = carousel[i];

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

        @Override
        public void onScrollChanged() {
            Log.d("Scroll", "ScrollChanged");
        }
    }
}
