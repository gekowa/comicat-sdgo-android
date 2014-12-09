package cn.sdgundam.comicatsdgo;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cn.sdgundam.comicatsdgo.data_model.OriginInfo;
import cn.sdgundam.comicatsdgo.gd_api.GDApiService;


public class OriginListActivity extends Activity {
    GDApiService apiService;

    OriginInfo[] origins;

    ListView originListView;

    OriginListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_origin_list);

        apiService = new GDApiService(this);
        origins = apiService.getOrigins();

        adapter = new OriginListAdapter(this);
        originListView = (ListView)findViewById(R.id.origin_list_view);
        originListView.setAdapter(adapter);
    }

    class OriginListAdapter extends BaseAdapter {
        Context context;

        public OriginListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return origins.length;
        }

        @Override
        public Object getItem(int position) {
            return origins[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            ImageView originImageView;
            TextView originTitleTV;
            TextView unitCountTV;

            if (convertView != null) {
                view = convertView;

                originImageView = (ImageView)view.getTag(R.id.origin_image_view);
                originTitleTV = (TextView)view.getTag(R.id.origin_title_text_view);
                unitCountTV = (TextView)view.getTag(R.id.unit_count_text_view);
            } else {
                view = LayoutInflater.from(context).inflate(R.layout.view_single_origin, null, false);
                originImageView = (ImageView)view.findViewById(R.id.origin_image_view);
                originTitleTV = (TextView)view.findViewById(R.id.origin_title_text_view);
                unitCountTV = (TextView)view.findViewById(R.id.unit_count_text_view);

                view.setTag(R.id.origin_image_view, originImageView);
                view.setTag(R.id.origin_title_text_view, originTitleTV);
                view.setTag(R.id.unit_count_text_view, unitCountTV);
            }

            OriginInfo oi = (OriginInfo)getItem(position);

            Picasso.with(context)
                    .load(Uri.parse(String.format("http://cdn.sdgundam.cn/images/origin/app/origin-%s.png", oi.getOriginIndex())))
                    .into(originImageView);

            originTitleTV.setText(oi.getTitle());
            unitCountTV.setText(oi.getNumberOfUnits() + "");

            return view;
        }
    }
}
