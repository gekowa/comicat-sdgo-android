package cn.sdgundam.comicatsdgo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.sdgundam.comicatsdgo.data_model.HomeInfo;
import cn.sdgundam.comicatsdgo.gd_api.FetchHomeInfoAsyncTask;


public class HomeFragment extends Fragment {

    static final String LOG_TAG = HomeFragment.class.getSimpleName();

    static HomeFragment instance = null;
    public static HomeFragment getInstance() {
        if (instance == null) {
            instance = new HomeFragment();
        }
        return instance;
    }

    HomeInfo homeInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "onCreated");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.v(LOG_TAG, "onResume");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.v(LOG_TAG, "onCreateView");

        if (this.homeInfo == null) {
            // load api
            FetchHomeInfoAsyncTask task = new FetchHomeInfoAsyncTask() {
                @Override
                protected void onPostExecute(HomeInfo homeInfo) {
                    super.onPostExecute(homeInfo);
                    if (homeInfo != null) {
                        onReceiveHomeInfo(homeInfo);
                        Log.v(LOG_TAG, "Got 'HomeInfo'" + homeInfo.toString());
                    } else {
                        onFetchingHomeInfoWithError();
                    }
                }
            };
            task.execute();

            // TODO: show loading
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    void onReceiveHomeInfo(HomeInfo homeInfo) {
        this.homeInfo = homeInfo;
    }

    void onFetchingHomeInfoWithError() {
        // TODO: Toast?
        // TODO: display network not available fragment
    }
}
