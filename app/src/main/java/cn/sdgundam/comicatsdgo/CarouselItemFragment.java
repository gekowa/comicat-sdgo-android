package cn.sdgundam.comicatsdgo;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.lang.annotation.Target;

/**
 * Created by xhguo on 9/28/2014.
 */
public class CarouselItemFragment extends android.support.v4.app.Fragment {
    static final String LOG_TAG = CarouselItemFragment.class.getSimpleName();

    String imageURL;

    ImageView imageView;
    ViewGroup rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        imageView = (ImageView) inflater.inflate(R.layout.fragment_carousel_item, container, false);

        Picasso.with(getActivity()).setIndicatorsEnabled(true);
        Picasso.with(getActivity()).load(imageURL).into(imageView);

        return imageView;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

        this.imageURL = args.getString("imageURL");
    }
}
