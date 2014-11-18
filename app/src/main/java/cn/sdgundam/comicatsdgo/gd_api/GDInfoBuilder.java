package cn.sdgundam.comicatsdgo.gd_api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.sdgundam.comicatsdgo.Utility;
import cn.sdgundam.comicatsdgo.data_model.CarouselInfo;
import cn.sdgundam.comicatsdgo.data_model.HomeInfo;
import cn.sdgundam.comicatsdgo.data_model.PostInfo;
import cn.sdgundam.comicatsdgo.data_model.PostList;
import cn.sdgundam.comicatsdgo.data_model.UnitInfo;
import cn.sdgundam.comicatsdgo.data_model.UnitInfoShort;
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

                    for (Class _class : new Class[] {String.class, Integer.class, Float.class}) {
                        if (getAllUnitInfoSetterNames().indexOf(keyForReflector + "_" + _class.toString()) >= 0) {
                            Method setter = UnitInfo.class.getDeclaredMethod(keyForReflector, _class);
                            if (setter != null) {
                                if (_class.toString().equals(String.class.toString())) {
                                    setter.invoke(unitInfo, unitJSONOBject.getString(key));
                                } else if (_class.toString().equals(Integer.class.toString())) {
                                    setter.invoke(unitInfo, unitJSONOBject.getInt(key));
                                } else if (_class.toString().equals(Float.class.toString())) {
                                    setter.invoke(unitInfo, (float)unitJSONOBject.getDouble(key));
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

        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSON parse error: " + e.getMessage());
        }

        return unitInfo;
    }
}
