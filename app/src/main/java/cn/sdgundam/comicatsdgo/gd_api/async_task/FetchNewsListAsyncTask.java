package cn.sdgundam.comicatsdgo.gd_api.async_task;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;

import cn.sdgundam.comicatsdgo.api_model.ApiResultWrapper;
import cn.sdgundam.comicatsdgo.api_model.PostInfo;
import cn.sdgundam.comicatsdgo.api_model.PostList;
import cn.sdgundam.comicatsdgo.gd_api.Communicator;
import cn.sdgundam.comicatsdgo.gd_api.GDInfoBuilder;

/**
 * Created by xhguo on 10/29/2014.
 */
public class FetchNewsListAsyncTask extends AsyncTask<Integer, Void, ApiResultWrapper<PostList<PostInfo>>> {
    @Override
    protected ApiResultWrapper<PostList<PostInfo>> doInBackground(Integer... parameters) {
        Integer gdCategory = parameters[0];
        Integer pageSize = parameters[1];
        Integer pageIndex = parameters[2];

        Map<String, String> apiParams = new HashMap<String, String>();
        apiParams.put("cat", gdCategory.toString());
        apiParams.put("size", pageSize.toString());
        apiParams.put("page", pageIndex.toString());

        ApiResultWrapper<PostList<PostInfo>> result;

        try {
            String json = Communicator.requestApi("post-list", apiParams);
            PostList<PostInfo> postList = GDInfoBuilder.buildPostList(json);
            return new ApiResultWrapper<PostList<PostInfo>>(postList);
        } catch (Exception e) {
            result = new ApiResultWrapper<PostList<PostInfo>>(e);
            return result;
        }
    }
}
