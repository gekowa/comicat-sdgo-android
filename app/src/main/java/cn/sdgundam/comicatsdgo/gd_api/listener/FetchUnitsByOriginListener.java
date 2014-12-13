package cn.sdgundam.comicatsdgo.gd_api.listener;

import cn.sdgundam.comicatsdgo.api_model.UnitList;

/**
 * Created by xhguo on 12/9/2014.
 */
public interface FetchUnitsByOriginListener {
    void onReceiveUnitListResult(UnitList unitList, String originIndex);
    void onFetchingUnitsFail(Exception e);
}
