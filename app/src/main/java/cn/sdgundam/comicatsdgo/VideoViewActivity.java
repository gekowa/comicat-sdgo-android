package cn.sdgundam.comicatsdgo;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import android.widget.VideoView;
import android.widget.MediaController;

import cn.sdgundam.comicatsdgo.video.GetYoukuVideoInfoAsyncTask;
import cn.sdgundam.comicatsdgo.video.OnReceivedYoukuVideoSrc;

import io.vov.vitamio.LibsChecker;
//import io.vov.vitamio.widget.MediaController;
//import io.vov.vitamio.widget.VideoView;


public class VideoViewActivity extends Activity implements OnReceivedYoukuVideoSrc {
    private int postId;
    private String videoHost;
    private String videoId;
    private String videoId2;

    private String videoURL;

    private VideoView videoView;
    private WebView webView;

    private int orientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extra = getIntent().getExtras();
        postId = extra.getInt("id");
        videoHost = extra.getString("videoHost");
        videoId = extra.getString("videoId");
        videoId2 = extra.getString("videoId2");

        setContentView(R.layout.activity_video_view);

        videoView = (VideoView)findViewById(R.id.video_view);
        prepareVideoPlay(videoHost, videoId, videoId2);

        webView = (WebView)findViewById(R.id.web_view);
        prepareWebView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        this.orientation = newConfig.orientation;

        configureVideoViewOnOrientation();
    }

    void configureVideoViewOnOrientation() {
        if (this.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            videoView.requestLayout();

//            getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
            getActionBar().hide();

            // Hide the status bar.
            if (Build.VERSION.SDK_INT < 16) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else if (Build.VERSION.SDK_INT >= 19) {
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE;
                decorView.setSystemUiVisibility(uiOptions);
            } else {
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }



            ((LinearLayout.LayoutParams)videoView.getLayoutParams()).gravity = Gravity.CENTER_VERTICAL;
        }
        else if (this.orientation == Configuration.ORIENTATION_PORTRAIT) {
            videoView.requestLayout();

            getActionBar().show();

            ((LinearLayout.LayoutParams)videoView.getLayoutParams()).gravity = Gravity.NO_GRAVITY;
        }
    }

    void prepareVideoPlay(String videoHost, String videoId, String videoId2) {
        if (videoHost.equals("2")) {
            // 17173
            videoURL = getVideoURL17173(videoId);
            play();
        } else if (videoHost.equals("4")) {
            // youku
            prepareYoukuVideoPlay(videoId);
        }
    }

    void prepareYoukuVideoPlay(String videoId) {
        GetYoukuVideoInfoAsyncTask task = new GetYoukuVideoInfoAsyncTask() {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                VideoViewActivity.this.onReceivedYoukuVideoInfo(s);
            }
        };
        task.execute(videoId);
    }

    void onReceivedYoukuVideoInfo(String json) {
        YoukuJSInterface youkuJSInterface = new YoukuJSInterface(this);
        youkuJSInterface.setVideoJSON(json);

        // prepare WebView
        WebView webView = new WebView(this);
//        WebChromeClient webClient = new WebChromeClient() {
//            @Override
//            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
//                Toast.makeText(VideoViewActivity.this, message, Toast.LENGTH_SHORT).show();
//                return super.onJsAlert(view, url, message,  result);
//            }
//        };
//        webView.setWebChromeClient(webClient);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(youkuJSInterface, "YoukuJSInterface");
        webView.loadUrl("file:///android_asset/youku.html");
    }

    @Override
    public void onReceivedYoukuVideoSrc(final String videoSrc) {
        if (!LibsChecker.checkVitamioLibs(VideoViewActivity.this))
            return;

        videoURL = videoSrc;
        play();
    }

    void play () {
        if (this.videoURL != null && this.videoView != null) {
            // play it
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                MediaController mc = new MediaController(VideoViewActivity.this);
                mc.setAnchorView(videoView);
                videoView.setMediaController(mc);
                videoView.setVideoPath(videoURL);
                videoView.requestFocus();
                }
            });
        }
    }

    String getVideoURL17173(String videoId) {
        return String.format("http://v.17173.com/api/%s-4.m3u8", videoId);
    }

    void prepareWebView() {

    }

    public class YoukuJSInterface {
        OnReceivedYoukuVideoSrc vva;
        public YoukuJSInterface(OnReceivedYoukuVideoSrc vva) {
            this.vva = vva;
        }

        String videoJSON;

        @JavascriptInterface
        public String getVideoJSON() {
            return videoJSON;
        }

        public void setVideoJSON(String videoJSON) {
            this.videoJSON = videoJSON;
        }

        String videoSrc3gphd;

        public String getVideoSrc3gphd() {
            return videoSrc3gphd;
        }

        @JavascriptInterface
        public void setVideoSrc3gphd(String videoSrc3gphd) {
            this.videoSrc3gphd = videoSrc3gphd;

            vva.onReceivedYoukuVideoSrc(videoSrc3gphd);
        }
    }

}

