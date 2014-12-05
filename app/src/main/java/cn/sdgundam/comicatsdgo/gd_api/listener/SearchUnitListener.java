package cn.sdgundam.comicatsdgo.gd_api.listener;

import cn.sdgundam.comicatsdgo.data_model.UnitList;

/**
 * Created by xhguo on 12/5/2014.
 */
public interface SearchUnitListener {
    void onReceiveUnitSearchResult(UnitList unitList);
    void onSearchUnitFail(Exception e);
}
