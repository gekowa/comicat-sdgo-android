package cn.sdgundam.comicatsdgo.gd_api;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;

import cn.sdgundam.comicatsdgo.data_model.ApiResultWrapper;
import cn.sdgundam.comicatsdgo.data_model.HomeInfo;
import cn.sdgundam.comicatsdgo.data_model.UnitInfo;

/**
 * Created by xhguo on 11/17/2014.
 */
public class FetchUnitInfoAsyncTask extends AsyncTask<String, Void, ApiResultWrapper<UnitInfo>> {
    @Override
    protected ApiResultWrapper<UnitInfo> doInBackground(String... strings) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("id", strings[0]);
        // stub params
        parameters.put("p", "1");
        parameters.put("s", "2");

        ApiResultWrapper<UnitInfo> result;

        try {
            String json = Communicator.requestApi("unit-info", parameters);
            UnitInfo unitInfo = GDInfoBuilder.buildUnitInfo(json);
            return new ApiResultWrapper<UnitInfo>(unitInfo);
        }
        catch(Exception e) {
            result = new ApiResultWrapper<UnitInfo>(e);
            return result;
        }
    }
}
