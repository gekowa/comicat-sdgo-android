package cn.sdgundam.comicatsdgo;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.FrameLayout;

//import android.media.MediaPlayer;
//import android.widget.VideoView;
//import android.widget.MediaController;

import org.lucasr.twowayview.TwoWayView;

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
        MediaPlayer.OnInfoListener,
        View.OnSystemUiVisibilityChangeListener {

    static final int ORIENTATION_THRESHOLD = 20;

    static final int SYSTEM_UI_FLAG_MY_FULLSCREEN =
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

    private int postId;
    private String videoHost;
    private String videoId;
    private String videoId2;

    private String videoURL;

    private View decorView;
    private VideoView videoView;
    private WebView webView;
    private MediaController mediaController;
    private View rootView;
    private FrameLayout videoContainer;

    private OrientationEventListener orientationEventListener;

    private float videoAspectRatio;

    private FrameLayout.LayoutParams layoutParamsPortrait;

    private int orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    private boolean isVideoPrepared;
    private boolean isOrientationLocked;

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

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(this);

        videoView = (VideoView)findViewById(R.id.video_view);
//        videoView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (VideoViewActivity.this.orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//                    hideSystemUI();
//                }
//            }
//        });
        prepareVideoPlay(videoHost, videoId, videoId2);

        webView = (WebView)findViewById(R.id.web_view);
        prepareWebView();

        videoContainer = (FrameLayout)findViewById(R.id.video_container);

        rootView = findViewById(R.id.root_view);

        orientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int degree) {
                int orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
                if (degree >= (90 - ORIENTATION_THRESHOLD) && degree <= (90 + ORIENTATION_THRESHOLD) ||
                        degree >= (270 - ORIENTATION_THRESHOLD) && degree <= (270 + ORIENTATION_THRESHOLD)) {
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                } else if ((degree>= (360 - ORIENTATION_THRESHOLD) && degree <= 360) ||
                        (degree >= 0 && degree <= ORIENTATION_THRESHOLD)) {
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                }

                if (orientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED &&
                        orientation != VideoViewActivity.this.orientation) {
                    VideoViewActivity.this.orientation = orientation;
                    if (isVideoPrepared) {
                        setRequestedOrientation(orientation);
                        configureVideoViewOnOrientation();
                    }
                }
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (orientationEventListener.canDetectOrientation() && isVideoPrepared) {
            orientationEventListener.enable();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        orientationEventListener.disable();
    }

    @Override
    public void onSystemUiVisibilityChange(int i) {
        if (i != SYSTEM_UI_FLAG_MY_FULLSCREEN) {
            mediaController.show();
        }
    }

    @Override
    public void onBackPressed() {
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            // disable sensor for a while (2.5s)
            orientationEventListener.disable();

            orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            setRequestedOrientation(orientation);
            configureVideoViewOnOrientation();

            rootView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    orientationEventListener.enable();
                }
            }, 2500);
        } else {
            super.onBackPressed();
        }
    }

    void configureVideoViewOnOrientation() {
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {

            getActionBar().hide();

            layoutParamsPortrait = (FrameLayout.LayoutParams)videoView.getLayoutParams();
            videoView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            hideSystemUI();

            webView.setVisibility(View.INVISIBLE);
        }
        else if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {

            getActionBar().show();

            if (layoutParamsPortrait != null) {
                videoView.setLayoutParams(layoutParamsPortrait);
            }

            showSystemUI();

            webView.setVisibility(View.VISIBLE);
        }

        blinkMediaController();
    }



    void hideSystemUI() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            decorView.setSystemUiVisibility(SYSTEM_UI_FLAG_MY_FULLSCREEN);
        }
    }

    void showSystemUI() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
        } else {
            decorView.setSystemUiVisibility(0);
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
                    mediaController = new MediaController(VideoViewActivity.this);
                    mediaController.setAlpha(0);
                    mediaController.hide();
                    mediaController.setOnHiddenListener(new MediaController.OnHiddenListener() {
                        @Override
                        public void onHidden() {
                            if (videoView.isPlaying() &&
                                    orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                                hideSystemUI();
                            }
                        }
                    });
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

        videoAspectRatio = mediaPlayer.getVideoAspectRatio();

        isVideoPrepared = true;

        // not to let rotation effect right now, avoid VideoView layout error
        rootView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (orientationEventListener.canDetectOrientation()) {
                    orientationEventListener.enable();
                }
            }
        }, 1000);
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

/*
* 1. 垂直进入
* 2. 手动全屏, 旋转全屏 (要记住全屏的原因) (视频prepare好以后, 才允许旋转)
* 3. 锁定全屏, 当手动全屏时自动锁定
* 4. 全凭状态下按返回键, 返回垂直状态
* 5.
*
* */