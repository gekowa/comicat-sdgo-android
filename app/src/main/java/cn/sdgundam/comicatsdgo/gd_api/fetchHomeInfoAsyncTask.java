package cn.sdgundam.comicatsdgo.gd_api;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;

import cn.sdgundam.comicatsdgo.data_model.ApiResultWrapper;
import cn.sdgundam.comicatsdgo.data_model.HomeInfo;

/**
 * Created by xhguo on 9/25/2014.
 */
public class FetchHomeInfoAsyncTask extends AsyncTask<Void, Void, ApiResultWrapper<HomeInfo>> {
    @Override
    protected ApiResultWrapper<HomeInfo> doInBackground(Void... voids) {
        Map<String, String> parameters = new HashMap<String, String>();
        // stub params
        parameters.put("p", "1");
        parameters.put("s", "2");

        ApiResultWrapper<HomeInfo> result;

        try {
            String json = Communicator.requestApi("home", parameters);
            HomeInfo homeInfo = GDInfoBuilder.buildHomeInfo(json);
            return new ApiResultWrapper<HomeInfo>(homeInfo);
        }
        catch(Exception e) {
            result = new ApiResultWrapper<HomeInfo>(e);
            return result;
        }
    }
}
