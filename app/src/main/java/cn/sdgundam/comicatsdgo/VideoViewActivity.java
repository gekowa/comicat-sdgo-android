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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

//import android.media.MediaPlayer;
//import android.widget.VideoView;
//import android.widget.MediaController;

import java.util.logging.Handler;

import cn.sdgundam.comicatsdgo.video.GetYoukuVideoInfoAsyncTask;
import cn.sdgundam.comicatsdgo.video.OnReceivedYoukuVideoSrc;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import io.vov.vitamio.MediaPlayer;


public class VideoViewActivity extends Activity implements
        OnReceivedYoukuVideoSrc,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener {
    private int postId;
    private String videoHost;
    private String videoId;
    private String videoId2;

    private String videoURL;

    private VideoView videoView;
    private WebView webView;
    private MediaController mediaController;
    private View rootView;
    private FrameLayout videoContainer;

    private FrameLayout.LayoutParams layoutPortrait;

    private int orientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            postId = extra.getInt("id");
            videoHost = extra.getString("videoHost");
            videoId = extra.getString("videoId");
            videoId2 = extra.getString("videoId2");
        } else {
            // 17173
//            videoHost = "2";
//            videoId = "18408422";

            // youku
            videoHost = "4";
            videoId = "XODAzNzY5MzE2";
        }


        setContentView(R.layout.activity_video_view);

        videoView = (VideoView)findViewById(R.id.video_view);
        prepareVideoPlay(videoHost, videoId, videoId2);

        webView = (WebView)findViewById(R.id.web_view);
        prepareWebView();

        videoContainer = (FrameLayout)findViewById(R.id.video_container);

        rootView = findViewById(R.id.root_view);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        this.orientation = newConfig.orientation;

        configureVideoViewOnOrientation();
    }

    void configureVideoViewOnOrientation() {
        if (this.orientation == Configuration.ORIENTATION_LANDSCAPE) {

//            getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
            getActionBar().hide();

//            ((FrameLayout.LayoutParams)videoView.getLayoutParams()).gravity =
//                    Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
//            videoContainer.setForegroundGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
//            videoView.setPadding(0, 0, 0, 0);

            layoutPortrait = (FrameLayout.LayoutParams)videoView.getLayoutParams();
            videoView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            // Uncomment code below to enable full screen mode, need more tuning
//            if (Build.VERSION.SDK_INT < 16) {
//                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            }
////            else if (Build.VERSION.SDK_INT >= 19) {
////                View decorView = getWindow().getDecorView();
////                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
////                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
////                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
////                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
////                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
////                        | View.SYSTEM_UI_FLAG_IMMERSIVE;
////                decorView.setSystemUiVisibility(uiOptions);
////            }
//            else {
//                View decorView = getWindow().getDecorView();
//                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN |
//                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION; // hide nav bar
//                decorView.setSystemUiVisibility(uiOptions);
//            }

            webView.setVisibility(View.INVISIBLE);
        }
        else if (this.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getActionBar().show();

            webView.setVisibility(View.VISIBLE);

            videoView.setLayoutParams(layoutPortrait);

            // Uncomment code below to enable full screen mode
//            if (Build.VERSION.SDK_INT < 16) {
//                getWindow().setFlags(WindowManager.LayoutParams.MATCH_PARENT,
//                        WindowManager.LayoutParams.MATCH_PARENT);
//            } else {
//                View decorView = getWindow().getDecorView();
//                decorView.setSystemUiVisibility(0);
//            }
        }

        blinkMediaController();
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
                    mediaController = new MediaController(VideoViewActivity.this);
                    mediaController.setAlpha(0);
                    mediaController.hide();
                    videoView.setMediaController(mediaController);

                    videoView.setOnPreparedListener(VideoViewActivity.this);
                    videoView.setOnErrorListener(VideoViewActivity.this);
                    videoView.setOnInfoListener(VideoViewActivity.this);

                    videoView.setVideoPath(videoURL);
                }
            });
        }
    }

    // Vitamio
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        blinkMediaController();

        View progressBarContainer = findViewById(R.id.loading);
        progressBarContainer.setVisibility(View.GONE);

        ViewGroup controllerAnchor = (ViewGroup) findViewById(R.id.video_container);
        mediaController.setAnchorView(controllerAnchor);
    }

    /**
     * Hack to make the media controller position right
     */
    private void blinkMediaController() {
        videoView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaController.show();
            }
        }, 500);
        videoView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaController.setAlpha(0);
                mediaController.hide();
            }
        }, 600);
        videoView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaController.show();

                mediaController.setAlpha(1);
            }
        }, 800);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        videoView.setVisibility(View.GONE);
        finish();
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    String getVideoURL17173(String videoId) {
        return String.format("http://v.17173.com/api/%s-4.m3u8", videoId);
    }

    void prepareWebView() {
        webView.loadUrl(String.format("http://www.sdgundam.cn/pages/app/post-view-video-android.aspx?id=%d", postId));
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