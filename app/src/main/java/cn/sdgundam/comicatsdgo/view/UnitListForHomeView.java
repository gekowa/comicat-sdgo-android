package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.Utility;
import cn.sdgundam.comicatsdgo.data_model.UnitInfoShort;
import it.sephiroth.android.library.widget.AbsHListView;
import it.sephiroth.android.library.widget.AdapterView;
import it.sephiroth.android.library.widget.HListView;

/**
 * Created by xhguo on 10/8/2014.
 */
public class UnitListForHomeView extends HListView {
    static int unitImageSize;

    public UnitListForHomeView(Context context) {
        super(context);
    }

    public UnitListForHomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnitListForHomeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    UnitInfoShort[] units;

    public void setUnits(UnitInfoShort[] units) {
        this.units = units;

        if (unitImageSize == 0) {
            unitImageSize = getResources().getDimensionPixelSize(R.dimen.unit_image_size);
        }

        this.setAdapter(new UnitListType1Adapter(getContext()));

        this.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UnitInfoShort unit = (UnitInfoShort) adapterView.getAdapter().getItem(i);

                Toast.makeText(getContext(),
                        String.format("UnitId: %s (index: %s) clicked", unit.getUnitId(), i), Toast.LENGTH_SHORT).show();
            }
        });
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
                gd.setStroke(1, 0x341A1A1A);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    view.setBackground(gd);
                }
                else {
                    view.setBackgroundDrawable(gd);
                }
            }

            UnitInfoShort unit = (UnitInfoShort)getItem(i);

            Picasso.with(context)
                    .load(Utility.getUnitImageURLByUnitId(unit.getUnitId()))
                    .resize(unitImageSize, unitImageSize)
                    .placeholder(R.drawable.placeholder_unit)
                    .into(view);

            return view;
        }
    }

}
