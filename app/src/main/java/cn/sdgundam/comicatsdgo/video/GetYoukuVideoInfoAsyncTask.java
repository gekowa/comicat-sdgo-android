package cn.sdgundam.comicatsdgo.video;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by xhguo on 10/12/2014.
 */
public class GetYoukuVideoInfoAsyncTask extends AsyncTask<String, Void, String> {
    public static final String LOG_TAG = GetYoukuVideoInfoAsyncTask.class.getSimpleName();

    @Override
    protected String doInBackground(String... strings) {
        String videoID = strings[0];
        String videoInfoJSON = requestVideoInfo(videoID);
        return videoInfoJSON ;
    }

    static String requestWebPage(String urlString) {
        StringBuffer sb = new StringBuffer();

        URL url = null;
        HttpURLConnection conn = null;
        try {
            url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(6000);
            conn.setUseCaches(true);

            conn.connect();

            int responseCode = conn.getResponseCode();

            int bufferSize = 1024;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                char[] buffer = new char[bufferSize];

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                int read = 0;
                while((read = br.read(buffer, 0, bufferSize)) > 0) {
                    sb.append(buffer, 0, read);
                }

                br.close();
            } else {
                Log.e(LOG_TAG, "Http Response Code: " + responseCode);
            }
        }
        catch(MalformedURLException e) {
            Log.e(LOG_TAG, "Error: " + e.getMessage());
        }
        catch(IOException e) {
            Log.e(LOG_TAG, "Error: " + e.getMessage());
        }
        finally {
            conn.disconnect();
        }

        String result = sb.toString();
        Log.v(LOG_TAG, "API Result: " + result);
        return result;
    }

    static String requestVideoInfo(String videoId) {
        return requestWebPage(
                    String.format("http://v.youku.com/player/getPlayList/VideoIDS/%shtml/Pf/4/ctype/12/ev/1", videoId));
    }

//    public static String getVideoSrc(String videoInfoJSON) {
//        try {
//            // parse json
//            JSONObject rootObject = new JSONObject(videoInfoJSON);
//
//            JSONObject data = rootObject.getJSONArray("data").getJSONObject(0);
//
//            String seed = data.getString("seed");
//
//            String _3gphdFileId = data.getJSONObject("streamfileids").getString("3gphd");
//
//            JSONObject _3gphdSeg = data.getJSONObject("segs").getJSONArray("3gphd").getJSONObject(0);
//
//
//            String k = _3gphdSeg.getString("k");
//            // String seconds = _3gphdSeg.getString("seconds");
//
//            Date now = new Date();
//            String sid = String.format("%d%d%d",
//                    now.getTime(),
//                    Integer.parseInt(new SimpleDateFormat("SSS").format(now)) + 1000,
//                    (int)(Math.random() * 9000));
//
//            String videoSrcUrl = String.format("http://k.youku.com/player/getFlvPath/sid/%s_00/st/mp4/fileid/%s?K=%s&hd=1&myp=0&ts=1156&ypp=0&ymovie=1&callback=",
//                    sid, getFileID(_3gphdFileId, seed), k);
//
//            return videoSrcUrl;
//
//        }
//        catch (JSONException e) {
//            Log.e(LOG_TAG, "JSON parse error: " + e.getMessage());
//        }
//
//        return "";
//    }
//
//    public static String getFileIDMixString(String seed) {
//        final String sourceString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ/\\:._-1234567890";
//
//        List<String> sourceArrayTemp = Arrays.asList(sourceString.split(""));
//        ArrayList<String> source = new ArrayList<String>();
//        source.addAll(sourceArrayTemp);
//        source.remove(0);
//
//        List<String> mixed = new ArrayList<String>();
//
//        float seedInt = Integer.parseInt(seed);
//        for (int i = 0, len = source.size(); i < len; i++) {
//            seedInt = (seedInt * 211 + 30031) % 65536;
//            int index = (int)Math.floor(seedInt / 65536 * source.size());
//            mixed.add(source.get(index));
//            source.remove(index);
//        }
//
//        return join(mixed, "");
//    }
//
//    public static String getFileID(String fileid, String seed) {
//        if (fileid.endsWith("*")) {
//            fileid += "0";
//        }
//        String mixed = getFileIDMixString(seed);
//        String[] ids = fileid.split("\\*");
//        ArrayList<Character> realId = new ArrayList<Character>();
//        for (int i = 0; i < ids.length; i++) {
//            int idx = Integer.parseInt(ids[i]);
//            if (idx < mixed.length()) {
//                realId.add(mixed.charAt(idx));
//            }
//        }
//        return join(realId, "");
//    }

//    public static String join(Iterable<? extends Object> elements, CharSequence separator) {
//        StringBuilder builder = new StringBuilder();
//
//        if (elements != null) {
//            Iterator<? extends Object> iter = elements.iterator();
//            if (iter.hasNext()) {
//                builder.append(String.valueOf(iter.next()));
//                while (iter.hasNext()) {
//                    builder.append(separator).append(String.valueOf(iter.next()));
//                }
//            }
//        }
//
//        return builder.toString();
//    }
//
//    static String readFile(InputStream inputStream) throws IOException {
//        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
//        try {
//            StringBuilder sb = new StringBuilder();
//            String line = br.readLine();
//
//            while (line != null) {
//                sb.append(line);
//                sb.append("\n");
//                line = br.readLine();
//            }
//            return sb.toString();
//        } finally {
//            br.close();
//        }
//    }

}
