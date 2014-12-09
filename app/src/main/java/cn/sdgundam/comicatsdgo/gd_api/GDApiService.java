package cn.sdgundam.comicatsdgo.gd_api;

import android.content.Context;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import cn.sdgundam.comicatsdgo.Utility;
import cn.sdgundam.comicatsdgo.data_model.ApiResultWrapper;
import cn.sdgundam.comicatsdgo.data_model.CheckOriginUpdateResult;
import cn.sdgundam.comicatsdgo.data_model.HomeInfo;
import cn.sdgundam.comicatsdgo.data_model.OriginInfo;
import cn.sdgundam.comicatsdgo.data_model.PostInfo;
import cn.sdgundam.comicatsdgo.data_model.PostList;
import cn.sdgundam.comicatsdgo.data_model.UnitInfo;
import cn.sdgundam.comicatsdgo.data_model.UnitList;
import cn.sdgundam.comicatsdgo.data_model.VideoListItem;
import cn.sdgundam.comicatsdgo.gd_api.async_task.CheckOriginUpdateAsyncTask;
import cn.sdgundam.comicatsdgo.gd_api.async_task.FetchHomeInfoAsyncTask;
import cn.sdgundam.comicatsdgo.gd_api.async_task.FetchNewsListAsyncTask;
import cn.sdgundam.comicatsdgo.gd_api.async_task.FetchUnitInfoAsyncTask;
import cn.sdgundam.comicatsdgo.gd_api.async_task.FetchUnitsByOriginAsyncTask;
import cn.sdgundam.comicatsdgo.gd_api.async_task.FetchVideoListAsyncTask;
import cn.sdgundam.comicatsdgo.gd_api.async_task.SearchUnitAsyncTask;
import cn.sdgundam.comicatsdgo.gd_api.listener.CheckOriginUpdateListener;
import cn.sdgundam.comicatsdgo.gd_api.listener.FetchGeneralListListener;
import cn.sdgundam.comicatsdgo.gd_api.listener.FetchHomeInfoListener;
import cn.sdgundam.comicatsdgo.gd_api.listener.FetchUnitInfoListener;
import cn.sdgundam.comicatsdgo.gd_api.listener.FetchUnitsByOriginListener;
import cn.sdgundam.comicatsdgo.gd_api.listener.SearchUnitListener;

/**
 * Created by xhguo on 10/14/2014.
 */
public class GDApiService {
    Context context;

    public GDApiService(Context context) {
        this.context = context;
    }

    // region HomeInfo

    static final int HOME_INFO_LIFETIME = 300;  // seconds 5 min
    static final String HOME_INFO_CACHE_FILE = "home_info.cache";

    FetchHomeInfoListener homeInfoListener;
    public void setHomeInfoListener(FetchHomeInfoListener homeInfoListener) {
        this.homeInfoListener = homeInfoListener;
    }

    void createFetchHomeInfoTaskAndExecute() {
        FetchHomeInfoAsyncTask task = new FetchHomeInfoAsyncTask() {
            @Override
            protected void onPostExecute(ApiResultWrapper<HomeInfo> result) {
                super.onPostExecute(result);

                if (homeInfoListener != null) {
                    if (result.getE() != null) {
                        homeInfoListener.onFetchingHomeInfoFail(result.getE());
                    } else {
                        HomeInfo homeInfo = result.getPayload();
                        if (homeInfo == null) {
                            homeInfoListener.onFetchingHomeInfoFail(new Exception("未知错误"));
                        } else {
                            ObjectCache.saveObjectToFile(context, homeInfo, HOME_INFO_CACHE_FILE);
                            homeInfoListener.onReceiveHomeInfo(homeInfo);
                        }
                    }
                }
            }
        };

        task.execute();
    }

    public final void fetchHomeInfo(Boolean force) {
        if (force) {
            createFetchHomeInfoTaskAndExecute();
        } else {
            HomeInfo cachedHomeInfo = ObjectCache.loadObjectFromFile(context, HOME_INFO_CACHE_FILE);
            if (cachedHomeInfo != null) {
                homeInfoListener.onReceiveHomeInfo(cachedHomeInfo);

                if (Math.abs(Utility.getDateDiff(cachedHomeInfo.getGenerated(), new Date(), TimeUnit.SECONDS)) > HOME_INFO_LIFETIME) {
                    createFetchHomeInfoTaskAndExecute();
                }
            } else {
                createFetchHomeInfoTaskAndExecute();
            }
        }
    }

    // endregion

    // region PostList

    FetchGeneralListListener postListListener;
    public void setPostListListener(FetchGeneralListListener postListListener) {
        this.postListListener = postListListener;
    }

    public final void fetchVideoList(int gdCategory, int pageSize, int pageIndex) {
        FetchVideoListAsyncTask task = new FetchVideoListAsyncTask() {
            @Override
            protected void onPostExecute(ApiResultWrapper<PostList<VideoListItem>> result) {
                super.onPostExecute(result);

                if (postListListener != null) {
                    if (result.getE() != null) {
                        postListListener.onFetchingPostListFail(result.getE());
                    } else {
                        PostList<VideoListItem> info = result.getPayload();
                        if (info == null) {
                            postListListener.onFetchingPostListFail(new Exception("未知错误"));
                        } else {
                            postListListener.onReceivePostList(result.getPayload());
                        }
                    }
                }
            }
        };

        task.execute(gdCategory, pageSize, pageIndex);
    }


    public final void fetchNewsList(int gdCategory, int pageSize, int pageIndex) {
        FetchNewsListAsyncTask task = new FetchNewsListAsyncTask() {
            @Override
            protected void onPostExecute(ApiResultWrapper<PostList<PostInfo>> result) {
                super.onPostExecute(result);

                if (postListListener != null) {
                    if (result.getE() != null) {
                        postListListener.onFetchingPostListFail(result.getE());
                    } else {
                        PostList<PostInfo> info = result.getPayload();
                        if (info == null) {
                            postListListener.onFetchingPostListFail(new Exception("未知错误"));
                        } else {
                            postListListener.onReceivePostList(result.getPayload());
                        }
                    }
                }
            }
        };

        task.execute(gdCategory, pageSize, pageIndex);
    }

    // endregion

    // region UnitInfo

    static final int UNIT_INFO_LIFETIME = 43200;  // seconds 5 min
    static final String UNIT_INFO_CACHE_FILE = "unit-%s.cache";

    FetchUnitInfoListener unitInfoListener;
    public void setUnitInfoListener(FetchUnitInfoListener unitInfoListener) {
        this.unitInfoListener = unitInfoListener;
    }

    private void createFetchUnitInfoTaskAndExecute(final String unitId) {
        FetchUnitInfoAsyncTask task = new FetchUnitInfoAsyncTask() {
            @Override
            protected void onPostExecute(ApiResultWrapper<UnitInfo> result) {
                super.onPostExecute(result);

                if (unitInfoListener != null) {
                    if (result.getE() != null) {
                        unitInfoListener.onFetchingUnitInfoFail(result.getE());
                    } else {
                        UnitInfo unitInfo = result.getPayload();
                        if (unitInfo == null) {
                            unitInfoListener.onFetchingUnitInfoFail(new Exception("未知错误"));
                        } else {
                            ObjectCache.saveObjectToFile(context, unitInfo, String.format(UNIT_INFO_CACHE_FILE, unitId));
                            unitInfoListener.onReceiveUnitInfo(unitInfo);
                        }
                    }
                }
            }
        };

        task.execute(unitId);
    }

    public void fetchUnitInfo(String unitId, Boolean force) {
        if (force) {
            createFetchUnitInfoTaskAndExecute(unitId);
        } else {
            UnitInfo cached = ObjectCache.loadObjectFromFile(context, String.format(UNIT_INFO_CACHE_FILE, unitId));
            if (cached != null) {
                unitInfoListener.onReceiveUnitInfo(cached);

                if (Math.abs(Utility.getDateDiff(cached.getGenerated(), new Date(), TimeUnit.SECONDS)) > UNIT_INFO_LIFETIME) {
                    createFetchUnitInfoTaskAndExecute(unitId);
                }
            } else {
                createFetchUnitInfoTaskAndExecute(unitId);
            }
        }
    }

    // endregion

    // region SearchUnit

    SearchUnitListener searchUnitListener;
    public void setSearchUnitListener(SearchUnitListener searchUnitListener) {
        this.searchUnitListener = searchUnitListener;
    }

    public void searchUnit(String keyword, String rank) {
        SearchUnitAsyncTask task = new SearchUnitAsyncTask() {
            @Override
            protected void onPostExecute(ApiResultWrapper<UnitList> result) {
                super.onPostExecute(result);

                if (searchUnitListener != null) {
                    if (result.getE() != null) {
                        searchUnitListener.onSearchUnitFail(result.getE());
                    } else {
                        UnitList unitList = result.getPayload();
                        if (unitList == null) {
                            searchUnitListener.onSearchUnitFail(new Exception("未知错误"));
                        } else {
                            searchUnitListener.onReceiveUnitSearchResult(unitList);
                        }
                    }
                }
            }
        };

        task.execute(keyword, rank);
    }

    // endregion

    // region CheckOriginUpdate

    final static String ORIGINS_FILE = "origins.cache";

    CheckOriginUpdateListener checkOriginUpdateListener;

    public void setCheckOriginUpdateListener(CheckOriginUpdateListener checkOriginUpdateListener) {
        this.checkOriginUpdateListener = checkOriginUpdateListener;
    }

    public void checkOriginUpdate(boolean force) {
        if (force) {
            createCheckOriginUpdateAsyncTaskAndExecute(0);
        } else {
            createCheckOriginUpdateAsyncTaskAndExecute(getOrigins().length);
        }
    }

    private void createCheckOriginUpdateAsyncTaskAndExecute(final int originCount) {
        CheckOriginUpdateAsyncTask task = new CheckOriginUpdateAsyncTask() {
            @Override
            protected void onPostExecute(ApiResultWrapper<CheckOriginUpdateResult> resultWrapper) {
                super.onPostExecute(resultWrapper);

                if (checkOriginUpdateListener != null) {
                    if (resultWrapper.getE() != null) {
                        checkOriginUpdateListener.onCheckOriginUpdateFail(resultWrapper.getE());
                    } else {
                        CheckOriginUpdateResult result = resultWrapper.getPayload();
                        if (result == null) {
                            checkOriginUpdateListener.onCheckOriginUpdateFail(new Exception("未知错误"));
                        } else if (result.isHasUpdate() && result.getOrigins() != null) {
                            ObjectCache.saveObjectToFile(context, result, ORIGINS_FILE);
                        }
                    }
                }
            }
        };

        task.execute(originCount);
    }

    public OriginInfo[] getOrigins() {
        CheckOriginUpdateResult cached = ObjectCache.loadObjectFromFile(context, ORIGINS_FILE);

        if (cached != null) {
            return cached.getOrigins();
        } else {
            return OriginInfo.builtInOrigins;
        }
    }

    // endregion

    // region UnitsByOrigin

    static final String UNITS_BY_ORIGIN_CACHE_FILE = "units-by-origin-%@.cache";

    FetchUnitsByOriginListener fetchUnitsByOriginListener;

    public void setFetchUnitsByOriginListener(FetchUnitsByOriginListener fetchUnitsByOriginListener) {
        this.fetchUnitsByOriginListener = fetchUnitsByOriginListener;
    }

    public void fetchUnitsByOrigin(final String originIndex, boolean force) {
        if (force) {
            createFetchUnitsByOriginTaskAndExecute(originIndex);
        } else {
            UnitList cached = ObjectCache.loadObjectFromFile(context, String.format(UNITS_BY_ORIGIN_CACHE_FILE, originIndex));
            if (cached != null) {
                fetchUnitsByOriginListener.onReceiveUnitListResult(cached, originIndex);

                if (Math.abs(Utility.getDateDiff(cached.getGenerated(), new Date(), TimeUnit.SECONDS)) > UNIT_INFO_LIFETIME) {
                    createFetchUnitInfoTaskAndExecute(originIndex);
                }
            } else {
                createFetchUnitInfoTaskAndExecute(originIndex);
            }
        }
    }

    void createFetchUnitsByOriginTaskAndExecute(final String originIndex) {
        FetchUnitsByOriginAsyncTask task = new FetchUnitsByOriginAsyncTask() {
            @Override
            protected void onPostExecute(ApiResultWrapper<UnitList> result) {
                super.onPostExecute(result);

                if (fetchUnitsByOriginListener != null) {
                    if (result.getE() != null) {
                        fetchUnitsByOriginListener.onFetchingUnitsFail(result.getE());
                    } else {
                        UnitList unitList = result.getPayload();
                        if (unitList == null) {
                            fetchUnitsByOriginListener.onFetchingUnitsFail(new Exception("未知错误"));
                        } else {
                            fetchUnitsByOriginListener.onReceiveUnitListResult(unitList, originIndex);
                        }
                    }
                }
            }
        };

        task.execute(originIndex);
    }

    // endregion
}
