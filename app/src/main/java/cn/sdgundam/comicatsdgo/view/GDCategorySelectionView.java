package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Arrays;
import java.util.List;

import cn.sdgundam.comicatsdgo.R;
import it.sephiroth.android.library.widget.AbsHListView;

/**
 * Created by xhguo on 10/17/2014.
 */
public class GDCategorySelectionView extends HorizontalScrollView {

    private List<Integer> gdCategories;

    private int iconWidth;
    private int iconHeight;
    private int iconMargin;

    private LinearLayout iconsContainer;
    private Button showAllButton;
    private View selectionIndicator;

    private boolean once = true;

    private OnGDCategorySelectionListener gdCategorySelectionListener;

    public GDCategorySelectionView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.gd_category_selection_view, this, true);

        iconsContainer = (LinearLayout)findViewById(R.id.icons_container);
        showAllButton = (Button)findViewById(R.id.show_all_button);
        selectionIndicator = findViewById(R.id.selection_indicator);

        Resources resources = context.getResources();

        iconWidth = resources.getDimensionPixelSize(R.dimen.gd_category_icon_l_width);
        iconHeight= resources.getDimensionPixelSize(R.dimen.gd_category_icon_l_height);
        iconMargin = resources.getDimensionPixelSize(R.dimen.gd_category_icon_l_margin);

        // hide scroll bar
        this.setHorizontalScrollBarEnabled(false);
    }

    public List<Integer> getGDCategories() {
        return gdCategories;
    }

    public void setGDCategories(List<Integer> gdCategories) {
        this.gdCategories = gdCategories;

        iconsContainer.removeAllViews();

        Resources resources = getContext().getResources();

        for (int i = 0; i < gdCategories.size(); i++) {
            ImageView iconImageView = new ImageView(getContext());

            final Integer gdc = gdCategories.get(i);

            iconImageView.setImageResource(resources.getIdentifier("gdc_icon_l_" + gdc.toString(), "drawable", getContext().getPackageName()));
            iconImageView.setPadding(0, 0, 0, 0);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(iconWidth, iconHeight);
            layoutParams.rightMargin = iconMargin;
            iconImageView.setLayoutParams(layoutParams);

            iconsContainer.addView(iconImageView);

            iconImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    whenGDCategorySelected(gdc);
                }
            });

        }

        showAllButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                selectionIndicator.setVisibility(INVISIBLE);
                if (gdCategorySelectionListener != null) {
                    gdCategorySelectionListener.onShowAllClicked();
                }
            }
        });

        this.invalidate();
        this.requestLayout();
    }

    public void setGDCategorySelectionListener(OnGDCategorySelectionListener gdCategorySelectionListener) {
        this.gdCategorySelectionListener = gdCategorySelectionListener;
    }

    void whenGDCategorySelected(Integer gdc) {
        selectionIndicator.setVisibility(VISIBLE);

        // re locate indicator
        int indexOfGDC = gdCategories.indexOf(gdc);
        int left = iconMargin + indexOfGDC * (iconWidth + iconMargin);
        ((LinearLayout.LayoutParams) selectionIndicator.getLayoutParams()).leftMargin = left;

        selectionIndicator.requestLayout();

        if (gdCategorySelectionListener != null) {
            // notify listener
            gdCategorySelectionListener.onGDCategorySelected(gdc);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (once) {
            this.scrollTo(showAllButton .getWidth(), 0);
            once = false;
        }
    }

    public interface OnGDCategorySelectionListener {
        public void onShowAllClicked();
        public void onGDCategorySelected(int gdCategory);
    }
}
