package cn.sdgundam.comicatsdgo;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by xhguo on 9/28/2014.
 */
public class CarouselItemFragment extends android.support.v4.app.Fragment {
    String imageURL;

    ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_carousel_item, container, false);

        imageView = (ImageView) root.findViewById(R.id.imageView);

        ImageLoader loader = ImageLoader.getInstance();
        loader.displayImage(this.imageURL, imageView);

        return root;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);

        this.imageURL = args.getString("imageURL");
    }
}
