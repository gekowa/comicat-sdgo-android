package cn.sdgundam.comicatsdgo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xhguo on 9/30/2014.
 */
public class GDCategoryFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO: hide unnecessary icons
        return inflater.inflate(R.layout.gd_category_icon_small, container, false);
    }
}
