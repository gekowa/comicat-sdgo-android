package cn.sdgundam.comicatsdgo;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import cn.sdgundam.comicatsdgo.data_model.UnitInfo;
import cn.sdgundam.comicatsdgo.data_model.UnitInfoShort;
import it.sephiroth.android.library.widget.AbsHListView;
import it.sephiroth.android.library.widget.HListView;

/**
 * Created by xhguo on 9/30/2014.
 * This fragment is mainly for usage on Home view
 */
public class UnitListType1Fragment extends Fragment {
    static int unitImageSize = 0;

    HListView listView;

    UnitInfoShort[] units;

    public void setUnits(UnitInfoShort[] units) {
        this.units = units;

        listView.setAdapter(new UnitListType1Adapter(getActivity()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (unitImageSize == 0) {
            unitImageSize = getResources().getDimensionPixelSize(R.dimen.unit_image_size);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_unit_list_type1, container, false);

        listView = (HListView) root.findViewById(R.id.hlistview);

        return root;
    }

    class UnitListType1Adapter extends BaseAdapter {
        static final int PADDING = 16;

        Context context;

        public UnitListType1Adapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return units.length;
        }

        @Override
        public Object getItem(int i) {
            return units[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ImageView view = (ImageView) convertView;
            if (view == null) {
                view = new ImageView(context);
                view.setLayoutParams(new AbsHListView.LayoutParams(unitImageSize + 2 * PADDING, unitImageSize + 2 * PADDING));
                view.setPadding(PADDING, PADDING, PADDING, PADDING);

                // draw border
                GradientDrawable gd = new GradientDrawable();
                gd.setColor(0xFFFFFFFF);
                // gd.setCornerRadius(0);
                gd.setStroke(1, 0x341A1A1A);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(gd);
                }
                else {
                    view.setBackgroundDrawable(gd);
                }
            }

            UnitInfoShort unit = (UnitInfoShort)getItem(i);

            Picasso.with(context).load(Utility.getUnitImageURLByUnitId(unit.getUnitId())).resize(unitImageSize, unitImageSize).into(view);

            return view;
        }
    }
}
