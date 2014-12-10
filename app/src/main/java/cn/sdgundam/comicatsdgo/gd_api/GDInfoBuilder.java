package cn.sdgundam.comicatsdgo.gd_api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.sdgundam.comicatsdgo.Utility;
import cn.sdgundam.comicatsdgo.data_model.CarouselInfo;
import cn.sdgundam.comicatsdgo.data_model.CheckOriginUpdateResult;
import cn.sdgundam.comicatsdgo.data_model.HomeInfo;
import cn.sdgundam.comicatsdgo.data_model.OriginInfo;
import cn.sdgundam.comicatsdgo.data_model.PostInfo;
import cn.sdgundam.comicatsdgo.data_model.PostList;
import cn.sdgundam.comicatsdgo.data_model.UnitInfo;
import cn.sdgundam.comicatsdgo.data_model.UnitInfoShort;
import cn.sdgundam.comicatsdgo.data_model.UnitList;
import cn.sdgundam.comicatsdgo.data_model.UnitMixMaterial;
import cn.sdgundam.comicatsdgo.data_model.VideoListItem;

/**
 * Created by xhguo on 9/25/2014.
 */
public class GDInfoBuilder {
    static final String LOG_TAG = GDInfoBuilder.class.getSimpleName();

    public static HomeInfo buildHomeInfo (String json) {
        if (json == "") {
            return null;
        }

        HomeInfo homeInfo = new HomeInfo();
        try {
            JSONObject rootObject = new JSONObject(json);

            String generatedString = rootObject.getString("generated");
            Date generated = Utility.parseDateSafe(generatedString);
            homeInfo.setGenerated(generated);

            // carousel
            JSONArray carouselJSONArray = rootObject.getJSONArray("carousel");
            List<CarouselInfo> carousel = new ArrayList<CarouselInfo>(carouselJSONArray.length());
            for (int i = 0; i < carouselJSONArray.length(); i++) {
                JSONObject c = carouselJSONArray.getJSONObject(i);
                CarouselInfo tempCI = new CarouselInfo(
                    c.getString("title"),
                    c.getString("imageURL"),
                    c.getInt("gdPostType"),
                    c.getInt("postId")
                );

                tempCI.setVideoHost(c.getString("videoHost"));
                tempCI.setVideoId(c.getString("videoId"));
                tempCI.setVideoId2(c.getString("videoId2"));

                carousel.add(tempCI);
            }

            homeInfo.setCarousel(carousel.toArray(new CarouselInfo[0]));

            // video list
            JSONArray videoListJSONArray = rootObject.getJSONArray("videoList");
            List<VideoListItem> videoList = new ArrayList<VideoListItem>(videoListJSONArray.length());
            for (int i = 0; i < videoListJSONArray.length(); i++) {
                JSONObject d = videoListJSONArray.getJSONObject(i);
                Date created = Utility.parseDateSafe(d.getString("created"));
                VideoListItem vli = new VideoListItem(
                    d.getInt("postId"),
                    d.getString("title"),
                    d.getString("title2"),
                    d.getString("imageURL"),
                    d.getInt("gdPostCategory"),
                    created
                );

                vli.setVideoHost(d.getString("videoHost"));
                vli.setVideoId(d.getString("videoId"));
                vli.setVideoId2(d.getString("videoId2"));

                videoList.add(vli);
            }
            homeInfo.setVideoList(videoList.toArray(new VideoListItem[0]));


            // post list
            JSONArray postListJSONArray = rootObject.getJSONArray("postList");
            List<PostInfo> postList = new ArrayList<PostInfo>(postListJSONArray.length());
            for (int i = 0; i < postListJSONArray.length(); i++) {
                JSONObject d = postListJSONArray.getJSONObject(i);
                Date created = Utility.parseDateSafe(d.getString("created"));
                PostInfo pi = new PostInfo(
                    d.getInt("postId"),
                    d.getString("title"),
                    d.getInt("gdPostCategory"),
                    created,
                    d.getInt("style")
                );
                postList.add(pi);
            }
            homeInfo.setPostList(postList.toArray(new PostInfo[0]));

            // units
            JSONArray unitsJSONArray = rootObject.getJSONArray("units");
            List<UnitInfoShort> units = new ArrayList<UnitInfoShort>(unitsJSONArray.length());
            for (int i = 0; i < unitsJSONArray.length(); i++) {
                JSONObject d = unitsJSONArray.getJSONObject(i);
                UnitInfoShort uis = new UnitInfoShort(
                    d.getString("unitId")
                );
                units.add(uis);
            }
            homeInfo.setUnits(units.toArray(new UnitInfoShort[0]));
        }
        catch(JSONException e) {
            Log.e(LOG_TAG, "JSON parse error: " + e.getMessage());
        }

        return homeInfo;
    }

    public static PostList<VideoListItem> buildVideoList(String json) {

        if (json == "") {
            return null;
        }

        PostList<VideoListItem> result = null;
        try {
            JSONObject rootObject = new JSONObject(json);

            Integer gdCategory = rootObject.getInt("category");

            result = new PostList<VideoListItem>(gdCategory);

            List<VideoListItem> vliList = new ArrayList<VideoListItem>();

            JSONArray postsJSONArray = rootObject.getJSONArray("posts");
            for (int i = 0; i < postsJSONArray.length(); i++) {
                JSONObject d = postsJSONArray.getJSONObject(i);
                Date created = Utility.parseDateSafe(d.getString("created"));
                VideoListItem vli = new VideoListItem(
                        d.getInt("postId"),
                        d.getString("title"),
                        d.getString("title2"),
                        d.getString("imageURL"),
                        d.getInt("gdPostCategory"),
                        created
                );

                vli.setVideoHost(d.getString("videoHost"));
                vli.setVideoId(d.getString("videoId"));
                vli.setVideoId2(d.getString("videoId2"));

                vliList.add(vli);
            }

            result.setPostListItems(vliList);

        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON parse error: " + e.getMessage());
        }

        return result;
    }

    public static PostList<PostInfo> buildPostList(String json) {
        if (json == "") {
            return null;
        }

        PostList<PostInfo> result = null;
        try {
            JSONObject rootObject = new JSONObject(json);

            Integer gdCategory = rootObject.getInt("category");

            result = new PostList<PostInfo>(gdCategory);

            List<PostInfo> postInfoList = new ArrayList<PostInfo>();

            JSONArray postsJSONArray = rootObject.getJSONArray("posts");
            for (int i = 0; i < postsJSONArray.length(); i++) {
                JSONObject d = postsJSONArray.getJSONObject(i);
                Date created = Utility.parseDateSafe(d.getString("created"));
                PostInfo p = new PostInfo(
                        d.getInt("postId"),
                        d.getString("title"),
                        d.getInt("gdPostCategory"),
                        created,
                        0
                );

                postInfoList.add(p);
            }

            result.setPostListItems(postInfoList);

        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON parse error: " + e.getMessage());
        }

        return result;
    }

    static ArrayList<String> allUnitInfoSetterNames;
    static ArrayList<String> getAllUnitInfoSetterNames() {
        if (allUnitInfoSetterNames == null) {
            Method[] allDeclardMethods = UnitInfo.class.getDeclaredMethods();
            allUnitInfoSetterNames = new ArrayList<String>();
            // filter for all setters
            for (Method m : allDeclardMethods) {
                if (m.getName().startsWith("set") &&
                        m.getParameterTypes().length == 1) {
                    allUnitInfoSetterNames.add(m.getName() + "_" + m.getParameterTypes()[0].toString());
                }
            }
        }
        return allUnitInfoSetterNames;
    }

    public static UnitInfo buildUnitInfo(String json) {
        if (json == "") {
            return null;
        }

        UnitInfo unitInfo = new UnitInfo();
        try {
            JSONObject rootObject = new JSONObject(json);

            String generatedString = rootObject.getString("generated");
            Date generated = Utility.parseDateSafe(generatedString);
            unitInfo.setGenerated(generated);

            JSONObject unitJSONOBject = rootObject.getJSONObject("unit");
            Iterator<String> allKeys = unitJSONOBject.keys();

            while(allKeys.hasNext()) {
                String key = allKeys.next();
                // Log.d("buildUnitInfo: ", key);
                try {
                    String firstLetter = key.substring(0, 1).toUpperCase();
                    String keyForReflector = "set" + firstLetter + key.substring(1, key.length());

                    for (Class _class : new Class[] {String.class, Integer.class, Float.class, Boolean.class}) {
                        if (getAllUnitInfoSetterNames().indexOf(keyForReflector + "_" + _class.toString()) >= 0) {
                            Method setter = UnitInfo.class.getDeclaredMethod(keyForReflector, _class);
                            if (setter != null) {
                                if (_class.toString().equals(String.class.toString())) {
                                    setter.invoke(unitInfo, unitJSONOBject.getString(key));
                                } else if (_class.toString().equals(Integer.class.toString())) {
                                    setter.invoke(unitInfo, unitJSONOBject.getInt(key));
                                } else if (_class.toString().equals(Float.class.toString())) {
                                    setter.invoke(unitInfo, (float)unitJSONOBject.getDouble(key));
                                } else if (_class.toString().equals(Boolean.class.toString())) {
                                    setter.invoke(unitInfo, (boolean)unitJSONOBject.getBoolean(key));
                                }
                            }
                            break;
                        }
                    }
                } catch(NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            // video
            JSONArray videoListFromJSON = rootObject.getJSONArray("videoList");
            List<VideoListItem> vliList = new ArrayList<VideoListItem>();
            for (int i = 0; i < videoListFromJSON.length(); i++) {
                JSONObject d = videoListFromJSON.getJSONObject(i);
                Date created = Utility.parseDateSafe(d.getString("created"));
                VideoListItem vli = new VideoListItem(
                        d.getInt("postId"),
                        d.getString("title"),
                        d.getString("title2"),
                        d.getString("imageURL"),
                        d.getInt("gdPostCategory"),
                        created
                );

                vli.setVideoHost(d.getString("videoHost"));
                vli.setVideoId(d.getString("videoId"));
                vli.setVideoId2(d.getString("videoId2"));

                vliList.add(vli);
            }

            unitInfo.setVideoList(vliList.toArray(new VideoListItem[0]));

            // mix
            JSONObject mixingFromJSON = rootObject.getJSONObject("mixing");
            if (mixingFromJSON.has("_G")) {
                JSONObject mixingG = mixingFromJSON.getJSONObject("_G");
                if (mixingG != null) {
                    unitInfo.setMixingKeyUnit(extractSingleUMM(mixingG.getJSONObject("keyUnit")));
                    unitInfo.setMixingMaterialUnits(extractManyUMMs(mixingG.getJSONArray("materialUnits")).toArray(new UnitMixMaterial[0]));
                }
            }

            if (mixingFromJSON.has("CN")) {
                JSONObject mixingCN = mixingFromJSON.getJSONObject("CN");
                if (mixingCN != null) {
                    unitInfo.setMixingKeyUnitCN(extractSingleUMM(mixingCN.getJSONObject("keyUnit")));
                    unitInfo.setMixingMaterialUnitsCN(extractManyUMMs(mixingCN.getJSONArray("materialUnits")).toArray(new UnitMixMaterial[0]));
                }
            }

            unitInfo.setCanMixAsKey(extractManyUMMs(mixingFromJSON.getJSONArray("canMixAsKey")).toArray(new UnitMixMaterial[0]));
            unitInfo.setCanMixAsMaterial(extractManyUMMs(mixingFromJSON.getJSONArray("canMixAsMaterial")).toArray(new UnitMixMaterial[0]));

        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON parse error: " + e.getMessage());
        }

        return unitInfo;
    }

    private static UnitMixMaterial extractSingleUMM(JSONObject jsonObject)
            throws JSONException {

        if (jsonObject == null) {
            return null;
        }

        UnitMixMaterial umm = new UnitMixMaterial();
        umm.setUnitId(jsonObject.getString("unitId"));
        umm.setModelName(jsonObject.getString("modelName"));
        umm.setLevel(jsonObject.getString("level"));

        return umm;
    }

    private static ArrayList<UnitMixMaterial> extractManyUMMs(JSONArray jsonArray)
            throws JSONException {

        if (jsonArray == null || jsonArray.length() == 0) {
            return new ArrayList<UnitMixMaterial>();
        }

        ArrayList<UnitMixMaterial> otherUMMS = new ArrayList<UnitMixMaterial>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            otherUMMS.add(extractSingleUMM(obj));
        }

        return otherUMMS;
    }

    public static UnitList buildUnitList(String json) {
        if (json == "") {
            return null;
        }

        UnitList list = new UnitList();
        try {
            JSONObject rootObject = new JSONObject(json);

            String generatedString = rootObject.getString("generated");
            Date generated = Utility.parseDateSafe(generatedString);

            list.setGenerated(generated);

            if (rootObject.has("origin")) {
                list.setOrigin(rootObject.getString("origin"));
            }

            if (rootObject.has("keyword")) {
                list.setSearchKeyword(rootObject.getString("keyword"));
            }

            List<UnitInfoShort> uisList = new ArrayList<UnitInfoShort>();
            JSONArray unitsFromJSON = rootObject.getJSONArray("units");

            for (int i = 0; i < unitsFromJSON.length(); i++) {
                JSONObject u = unitsFromJSON.getJSONObject(i);
                UnitInfoShort uis = new UnitInfoShort();
                uis.setUnitId(u.getString("unitId"));
                uis.setModelName(u.getString("modelName"));
                uis.setRank(u.getString("rank"));
                uis.setRating((float)u.getDouble("rating"));

                uisList.add(uis);
            }

            list.setUnits(uisList);

        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON parse error: " + e.getMessage());
        }

        return list;
    }

    public static CheckOriginUpdateResult buildOriginInfoList(String json) {
        if (json == "") {
            return null;
        }

        CheckOriginUpdateResult result = new CheckOriginUpdateResult();
        try {
            JSONObject rootObject = new JSONObject(json);

            result.setHasUpdate(rootObject.getBoolean("result"));

            if (rootObject.has("origins")) {
                List<OriginInfo> origins = new ArrayList<OriginInfo>();

                JSONArray originJSONArray = rootObject.getJSONArray("origins");
                for (int i = 0; i < originJSONArray.length(); i++) {
                    JSONObject o = originJSONArray.getJSONObject(i);

                    OriginInfo oi = new OriginInfo();
                    oi.setOriginIndex(o.getString("origin"));
                    oi.setTitle(o.getString("title"));
                    oi.setShortTitle(o.getString("shortTitle"));
                    oi.setNumberOfUnits(o.getInt("units"));
                    oi.setDisplayOrder(o.getInt("displayOrder"));

                    origins.add(oi);
                }

                result.setOrigins(origins.toArray(new OriginInfo[0]));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON parse error: " + e.getMessage());
        }

        return result;
    }
}
