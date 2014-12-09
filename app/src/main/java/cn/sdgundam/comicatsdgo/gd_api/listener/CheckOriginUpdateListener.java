package cn.sdgundam.comicatsdgo.gd_api.listener;

import cn.sdgundam.comicatsdgo.data_model.CheckOriginUpdateResult;

/**
 * Created by xhguo on 12/9/2014.
 */
public interface CheckOriginUpdateListener {
    void onReceiveCheckOriginUpdateResult(CheckOriginUpdateResult result);
    void onCheckOriginUpdateFail(Exception e);
}
