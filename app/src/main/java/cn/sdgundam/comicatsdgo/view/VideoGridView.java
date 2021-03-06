package cn.sdgundam.comicatsdgo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.api_model.VideoListItem;

/**
 * Created by xhguo on 10/8/2014.
 */
public class VideoGridView extends GridView {
    static final float VIEW_ASPECT = 1.11f;

    VideoListItem[] videos;

    OnVideoClickListener videoClickListener;

    public void setOnVideoClickListener(OnVideoClickListener videoClickListener) {
        this.videoClickListener = videoClickListener;
    }

    public VideoGridView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setNumColumns(2);
        this.setDrawSelectorOnTop(true);

        // float horSpacingPx = Utility.convertDpToPixel(9, context);
        this.setHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.video_grid_horizontal_spacing));

        // float verSpacingPx = Utility.convertDpToPixel(6, context);
        this.setVerticalSpacing(getResources().getDimensionPixelSize(R.dimen.video_grid_vertical_spacing));
    }

    public void setVideos(VideoListItem[] videos) {
        this.videos = videos;

        this.setAdapter(new VideoGridViewAdapter(getContext()));

        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (videoClickListener != null) {
                    VideoListItem vli = (VideoListItem) adapterView.getAdapter().getItem(i);

                    videoClickListener.onItemClick(vli.getPostId(),
                            vli.getVideoHost(),
                            vli.getVideoId(),
                            vli.getVideoId2());
                }
            }
        });

        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (videos != null) {
            float width = (float)MeasureSpec.getSize(widthMeasureSpec);
            int videoLengthAdjusted = videos.length + videos.length % 2;
            int verticalSpacing = getResources().getDimensionPixelSize(R.dimen.video_grid_vertical_spacing);
            int horizontalSpacing = getResources().getDimensionPixelSize(R.dimen.video_grid_horizontal_spacing);
            int height = (int) ((((width - horizontalSpacing * (this.getNumColumns() - 1)) / VIEW_ASPECT / 2 + verticalSpacing)) * videoLengthAdjusted  / 2);

            setMeasuredDimension((int)width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec /*MeasureSpec.UNSPECIFIED*/ );
        }
    }

    class VideoGridViewAdapter extends BaseAdapter {
        Context context;

        public VideoGridViewAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return videos.length;
        }

        @Override
        public Object getItem(int i) {
            return videos[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            VideoGridItemView view = (VideoGridItemView)convertView;
            if (view == null) {
                view = new VideoGridItemView(getContext(), null);
            }

            VideoListItem item = (VideoListItem) getItem(i);
            view.setVli(item);

            return view;
        }
    }

    public interface OnVideoClickListener {
        void onItemClick(int postId, String videoHost, String videoId, String videoId2);
    }
}
