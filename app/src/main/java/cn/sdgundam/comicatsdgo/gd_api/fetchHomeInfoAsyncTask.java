package cn.sdgundam.comicatsdgo.gd_api;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;

import cn.sdgundam.comicatsdgo.data_model.HomeInfo;

/**
 * Created by xhguo on 9/25/2014.
 */
public class FetchHomeInfoAsyncTask extends AsyncTask<Void, Void, HomeInfo> {
    @Override
    protected HomeInfo doInBackground(Void... voids) {
        Map<String, String> parameters = new HashMap<String, String>();
        // stub params
        parameters.put("p", "1");
        parameters.put("s", "2");

        HomeInfo homeInfo;
        try {
            String json = Communicator.requestApi("home", parameters);
            homeInfo = GDInfoBuilder.buildHomeInfo(json);
        }
        catch(Exception e) {
            homeInfo = new HomeInfo();
            homeInfo.setE(e);
        }
        return homeInfo;
    }
}
