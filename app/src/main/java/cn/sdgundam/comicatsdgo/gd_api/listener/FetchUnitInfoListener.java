package cn.sdgundam.comicatsdgo.gd_api.listener;

import cn.sdgundam.comicatsdgo.data_model.HomeInfo;
import cn.sdgundam.comicatsdgo.data_model.UnitInfo;

/**
 * Created by xhguo on 11/17/2014.
 */
public interface FetchUnitInfoListener {
    void onReceiveUnitInfo(UnitInfo unitInfo);
    void onFetchingUnitInfoFail(Exception e);
}
