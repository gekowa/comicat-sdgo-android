package cn.sdgundam.comicatsdgo.gd_api;

import android.os.AsyncTask;
import android.os.Bundle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sdgundam.comicatsdgo.data_model.ApiResultWrapper;
import cn.sdgundam.comicatsdgo.data_model.HomeInfo;
import cn.sdgundam.comicatsdgo.data_model.VideoList;
import cn.sdgundam.comicatsdgo.data_model.VideoListItem;

/**
 * Created by xhguo on 10/20/2014.
 */
public class FetchVideoListAsyncTask extends AsyncTask<Integer, Void, ApiResultWrapper<VideoList>> {
    @Override
    protected ApiResultWrapper<VideoList> doInBackground(Integer... parameters) {
        Integer gdCategory = parameters[0];
        Integer pageIndex = parameters[1];
        Integer pageSize = parameters[2];

        Map<String, String> apiParams = new HashMap<String, String>();
        apiParams.put("cat", gdCategory.toString());
        apiParams.put("size", pageSize.toString());
        apiParams.put("page", pageIndex.toString());

        ApiResultWrapper<VideoList> result;

        try {
            String json = Communicator.requestApi("video-list", apiParams);
            VideoList videoList = GDInfoBuilder.buildVideoList(json);
            return new ApiResultWrapper<VideoList>(videoList);
        }
        catch(Exception e) {
            result = new ApiResultWrapper<VideoList>(e);
            return result;
        }
    }
}
