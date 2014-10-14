package cn.sdgundam.comicatsdgo.gd_api;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;

import cn.sdgundam.comicatsdgo.data_model.HomeInfo;

/**
 * Created by xhguo on 9/25/2014.
 */
public class FetchHomeInfoAsyncTask extends AsyncTask<Boolean, Void, HomeInfo> {
    @Override
    protected HomeInfo doInBackground(Boolean... voids) {
        Map<String, String> parameters = new HashMap<String, String>();
        // stub params
        parameters.put("p", "1");
        parameters.put("s", "2");

        String json = Communicator.requestApi("home", parameters);
        return GDInfoBuilder.buildHomeInfo(json);
    }
}
