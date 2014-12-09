package cn.sdgundam.comicatsdgo.gd_api.async_task;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;

import cn.sdgundam.comicatsdgo.data_model.ApiResultWrapper;
import cn.sdgundam.comicatsdgo.data_model.CheckOriginUpdateResult;
import cn.sdgundam.comicatsdgo.data_model.OriginInfo;
import cn.sdgundam.comicatsdgo.gd_api.Communicator;
import cn.sdgundam.comicatsdgo.gd_api.GDInfoBuilder;

/**
 * Created by xhguo on 12/9/2014.
 */
public class CheckOriginUpdateAsyncTask extends AsyncTask<Integer, Void, ApiResultWrapper<CheckOriginUpdateResult>> {
    @Override
    protected ApiResultWrapper<CheckOriginUpdateResult> doInBackground(Integer... integers) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("origin-count", integers[0] + "");
        // stub params
        parameters.put("p", "1");
        parameters.put("s", "2");

        ApiResultWrapper<CheckOriginUpdateResult> resultWrapper;

        try {
            String json = Communicator.requestApi("has-new-origin", parameters);
            CheckOriginUpdateResult result = GDInfoBuilder.buildOriginInfoList(json);
            return new ApiResultWrapper<CheckOriginUpdateResult>(result);
        }
        catch(Exception e) {
            resultWrapper = new ApiResultWrapper<CheckOriginUpdateResult>(e);
            return resultWrapper;
        }
    }
}
