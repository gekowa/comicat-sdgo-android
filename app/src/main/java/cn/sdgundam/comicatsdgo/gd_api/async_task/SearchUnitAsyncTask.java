package cn.sdgundam.comicatsdgo.gd_api.async_task;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;

import cn.sdgundam.comicatsdgo.api_model.ApiResultWrapper;
import cn.sdgundam.comicatsdgo.api_model.UnitList;
import cn.sdgundam.comicatsdgo.gd_api.Communicator;
import cn.sdgundam.comicatsdgo.gd_api.GDInfoBuilder;

/**
 * Created by xhguo on 12/4/2014.
 */
public class SearchUnitAsyncTask extends AsyncTask<String, Void, ApiResultWrapper<UnitList>> {
    @Override
    protected ApiResultWrapper<UnitList> doInBackground(String... strings) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("k", strings[0]);
        // stub params
        parameters.put("p", "1");
        parameters.put("s", "2");

        ApiResultWrapper<UnitList> result;

        try {
            String json = Communicator.requestApi("search-units", parameters);
            UnitList unitList = GDInfoBuilder.buildUnitList(json);
            return new ApiResultWrapper<UnitList>(unitList);
        }
        catch(Exception e) {
            result = new ApiResultWrapper<UnitList>(e);
            return result;
        }
    }
}
