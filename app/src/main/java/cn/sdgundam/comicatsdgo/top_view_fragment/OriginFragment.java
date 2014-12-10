package cn.sdgundam.comicatsdgo.top_view_fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cn.sdgundam.comicatsdgo.R;
import cn.sdgundam.comicatsdgo.UnitsByOriginActivity;
import cn.sdgundam.comicatsdgo.data_model.OriginInfo;
import cn.sdgundam.comicatsdgo.gd_api.GDApiService;

public class OriginFragment extends Fragment {
    GDApiService apiService;

    OriginInfo[] origins;

    ListView originListView;

    OriginListAdapter adapter;

    static OriginFragment instance = null;
    public static OriginFragment getInstance() {
        if (instance == null) {
            instance = new OriginFragment();
        }
        return instance;
    }

    public OriginFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_origin_list, null, false);

        apiService = new GDApiService(this.getActivity());
        origins = apiService.getOrigins();

        adapter = new OriginListAdapter(this.getActivity());
        originListView = (ListView)view.findViewById(R.id.origin_list_view);
        originListView.setAdapter(adapter);

        originListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OriginInfo oi = (OriginInfo)adapter.getItem(position);

                Intent intent = new Intent(OriginFragment.this.getActivity(), UnitsByOriginActivity.class);
                intent.putExtra("origin", oi.getOriginIndex());
                intent.putExtra("shortTitle", oi.getShortTitle());

                OriginFragment.this.startActivity(intent);
            }
        });

        return view;
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
