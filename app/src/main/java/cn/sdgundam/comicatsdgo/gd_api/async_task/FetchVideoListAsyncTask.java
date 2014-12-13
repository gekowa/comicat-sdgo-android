package cn.sdgundam.comicatsdgo.gd_api.async_task;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;

import cn.sdgundam.comicatsdgo.api_model.ApiResultWrapper;
import cn.sdgundam.comicatsdgo.api_model.PostList;
import cn.sdgundam.comicatsdgo.api_model.VideoListItem;
import cn.sdgundam.comicatsdgo.gd_api.Communicator;
import cn.sdgundam.comicatsdgo.gd_api.GDInfoBuilder;

/**
 * Created by xhguo on 10/20/2014.
 */
public class FetchVideoListAsyncTask extends AsyncTask<Integer, Void, ApiResultWrapper<PostList<VideoListItem>>> {
    @Override
    protected ApiResultWrapper<PostList<VideoListItem>> doInBackground(Integer... parameters) {
        Integer gdCategory = parameters[0];
        Integer pageSize = parameters[1];
        Integer pageIndex = parameters[2];

        Map<String, String> apiParams = new HashMap<String, String>();
        apiParams.put("cat", gdCategory.toString());
        apiParams.put("size", pageSize.toString());
        apiParams.put("page", pageIndex.toString());

        ApiResultWrapper<PostList<VideoListItem>> result;

        try {
            String json = Communicator.requestApi("video-list", apiParams);
            PostList<VideoListItem> videoList = GDInfoBuilder.buildVideoList(json);
            return new ApiResultWrapper<PostList<VideoListItem>>(videoList);
        }
        catch(Exception e) {
            result = new ApiResultWrapper<PostList<VideoListItem>>(e);
            return result;
        }
    }
}
