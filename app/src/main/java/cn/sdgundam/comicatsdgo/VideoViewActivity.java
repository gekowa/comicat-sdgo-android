package cn.sdgundam.comicatsdgo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.VideoView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import cn.sdgundam.comicatsdgo.video_enabled.VideoEnabledWebChromeClient;
import cn.sdgundam.comicatsdgo.video_enabled.VideoEnabledWebView;


public class VideoViewActivity extends Activity {
    int postId;
    String videoHost;
    String videoId;
    String videoId2;

    String videoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extra = getIntent().getExtras();
        postId = extra.getInt("id");
        videoHost = extra.getString("videoHost");
        videoId = extra.getString("videoId");
        videoId2 = extra.getString("videoId2");

        videoURL = getVideoURL(videoHost, videoId, videoId2);

        setContentView(R.layout.activity_video_view);

        VideoView vv = (VideoView)findViewById(R.id.video_view);

        MediaController mc = new MediaController(this);

        vv.setVideoURI(Uri.parse(videoURL));
        vv.setMediaController(mc);
        vv.requestFocus();
        vv.start();

//
//        VideoEnabledWebView webView = (VideoEnabledWebView)findViewById(R.id.web_view);
//
//        // Initialize the VideoEnabledWebChromeClient and set event handlers
//        View nonVideoLayout = findViewById(R.id.nonVideoLayout); // Your own view, read class comments
//        ViewGroup videoLayout = (ViewGroup) findViewById(R.id.videoLayout); // Your own view, read class comments
//        // View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null); // Your own view, read class comments
//        VideoEnabledWebChromeClient webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, null, webView) // See all available constructors...
//        {
//            // Subscribe to standard events, such as onProgressChanged()...
//            @Override
//            public void onProgressChanged(WebView view, int progress)
//            {
//                // Your code...
//            }
//        };
//        webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback()
//        {
//            @Override
//            public void toggledFullscreen(boolean fullscreen)
//            {
//                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
//                if (fullscreen)
//                {
//                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
//                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
//                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
//                    getWindow().setAttributes(attrs);
//                    if (android.os.Build.VERSION.SDK_INT >= 14)
//                    {
//                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
//                    }
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                }
//                else
//                {
////                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
////                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
////                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
////                    getWindow().setAttributes(attrs);
////                    if (android.os.Build.VERSION.SDK_INT >= 14)
////                    {
////                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
////                    }
////                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                }
//
//            }
//        });
//        webView.setWebChromeClient(webChromeClient);
//
//        // Navigate everywhere you want, this classes have only been tested on YouTube's mobile site
//        // webView.loadUrl(String.format("http://www.sdgundam.cn/pages/app/post-view-video-2.aspx?id=%d", postId));
//        webView.loadUrl("http://www.sdgundam.cn/pages/app/post-view-video-2.aspx?id=33220");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.video_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static String getVideoURL(String videoHost, String videoId, String videoId2) {
        if (videoHost.equals("2")) {
            // 17173
            return getVideoURL17173(videoId);
        } else if (videoHost.equals("4")) {
            // youku
            return getVideoURLYouku(videoId2);
        }

        return "";
    }

    static String getVideoURLYouku(String videoId) {
        Date now = new Date();
        String nowString = String.valueOf((now.getTime() / 1000));

        return String.format("http://v.youku.com/player/getM3U8/vid/%s/type/hd2/ts/%s/v.m3u8",
                videoId, nowString);
    }

    static String getVideoURL17173(String videoId) {
        return String.format("http://v.17173.com/api/%s-4.m3u8", videoId);
    }
}
