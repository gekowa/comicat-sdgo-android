package cn.sdgundam.comicatsdgo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import android.media.MediaPlayer;
//import android.widget.VideoView;
//import android.widget.MediaController;

import cn.sdgundam.comicatsdgo.video.GetYoukuVideoInfoAsyncTask;
import cn.sdgundam.comicatsdgo.video.OnReceivedYoukuVideoSrc;

import cn.sdgundam.comicatsdgo.video.VideoInfoListener;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import io.vov.vitamio.MediaPlayer;


public class VideoViewActivity extends Activity implements
        OnReceivedYoukuVideoSrc,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener,
        View.OnSystemUiVisibilityChangeListener,
        VideoInfoListener {

    static final String LOG_TAG = VideoViewActivity.class.getSimpleName();

    static final int ORIENTATION_THRESHOLD = 20;

    static final int SYSTEM_UI_FLAG_MY_FULLSCREEN =
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

    private int postId;
    private String videoHost;
    private String videoId;
    private String videoId2;

    private String videoURL;

    private View decorView;
    private VideoView videoView;
    private WebView infoWebView;
    private MediaController mediaController;
    private View rootView;
    private FrameLayout videoContainer;

    private OrientationEventListener orientationEventListener;

    private float videoAspectRatio;

    private FrameLayout.LayoutParams layoutParamsPortrait;

    private int orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    private boolean isVideoPrepared;
    private boolean isOrientationLocked;
    private boolean isRestarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();

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

        Log.d(LOG_TAG, "onStart: " + postId + "-" + videoHost + "-" + videoId + "-" + videoId2);

        setContentView(R.layout.activity_video_view);

        Resources resources = getResources();

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(this);

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

        TextView videoHostedByTextView = (TextView) findViewById(R.id.text_view_video_hosted_by);
        try {
            videoHostedByTextView.setText(resources.getString(R.string.video_hosted_by) +
                    resources.getString(resources.getIdentifier("video_host_" + videoHost, "string", this.getPackageName())));
        } catch (Resources.NotFoundException e) {
            videoHostedByTextView.setVisibility(View.INVISIBLE);
        }

        ImageView playButton = (ImageView)findViewById(R.id.btn_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                prepareVideoPlay(videoHost, videoId, videoId2);
            }
        });
        
        videoView = (VideoView)findViewById(R.id.video_view);
       
        // Debug
        if (BuildConfig.DEBUG) {
            getActionBar().setTitle(postId + "");
        }

        Log.d(LOG_TAG, "onStart End " + postId);
    }


    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume begin" + postId);
        super.onResume();

        if (orientationEventListener.canDetectOrientation() && isVideoPrepared) {
            orientationEventListener.enable();
        }

        Log.d(LOG_TAG, "onResume end" + postId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        orientationEventListener.disable();
    }

    @Override
    protected void onStop() {
        super.onStop();
        videoView.stopPlayback();
    @Override
    protected void onRestart() {
        super.onRestart();

        isRestarted = true;
    }

    @Override
    public void onSystemUiVisibilityChange(int i) {
        if ((i & SYSTEM_UI_FLAG_MY_FULLSCREEN) != i) {
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
        Log.d(LOG_TAG, "configureVideoViewOnOrientation orientation:" + orientation);

        if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {

            getActionBar().hide();

            layoutParamsPortrait = (FrameLayout.LayoutParams)videoView.getLayoutParams();
            videoView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

            hideSystemUI();

            infoWebView.setVisibility(View.INVISIBLE);
        }
        else if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {

            getActionBar().show();

            if (layoutParamsPortrait != null) {
                videoView.setLayoutParams(layoutParamsPortrait);
            }

            showSystemUI();

            infoWebView.setVisibility(View.VISIBLE);
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

            if (isRestarted) {
                // delay the play if restarts
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        play();
                    }
                }, 1000);
            } else {
                play();
            }

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
        // webView.loadUrl("file:///android_asset/youku.html");
        webView.loadUrl("http://www.sdgundam.cn/pages/app/video-interface/youku.html");
    }

    @Override
    public void onReceivedYoukuVideoSrc(final String videoSrc) {
        if (!LibsChecker.checkVitamioLibs(VideoViewActivity.this))
            return;

        videoURL = videoSrc;
        play();
    }

    void play () {
        Log.d(LOG_TAG, "play begin" + postId);
        if (this.videoURL != null && this.videoView != null) {
            // play it
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(LOG_TAG, "play run start");
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

                    Log.d(LOG_TAG, "play run end");
                }
            });
        }
    }

    // Vitamio
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d(LOG_TAG, "onPrepared");

        blinkMediaController();

        View progressBarContainer = findViewById(R.id.loading);
        progressBarContainer.setVisibility(View.GONE);

        ViewGroup controllerAnchor = (ViewGroup) findViewById(R.id.video_container);
        mediaController.setAnchorView(controllerAnchor);

        videoAspectRatio = mediaPlayer.getVideoAspectRatio();

        isVideoPrepared = true;

        ((LinearLayout.LayoutParams)videoContainer.getLayoutParams()).weight = 0;

        videoView.start();

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
        Log.d(LOG_TAG, "blinkMediaController" + postId);
        if (mediaController != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mediaController != null) {
                        mediaController.show();
                    }
                }
            }, 500);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mediaController != null) {
                        mediaController.setAlpha(0);
                        mediaController.hide();
                    }
                }
            }, 600);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mediaController != null) {
                        mediaController.show();
                        mediaController.setAlpha(1);
                    }
                }
            }, 800);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
//        videoView.setVisibility(View.GONE);
        Toast.makeText(this, "Video Error: " + what + "," + extra, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    String getVideoURL17173(String videoId) {
        return String.format("http://v.17173.com/api/%s-4.m3u8", videoId);
    }

    void prepareWebView() {
        VideoInfoInterface vii = new VideoInfoInterface(this);

        webView.loadUrl(String.format("http://www.sdgundam.cn/pages/app/post-view-video-android.aspx?id=%d", postId));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(vii, "$VLI");
    }

    @Override
    public void clickedOnUnit(String unitId) {
        Toast.makeText(VideoViewActivity.this, "Unit: " + unitId, Toast.LENGTH_SHORT).show();

        this.videoView.stopPlayback();
    }

    @Override
    public void clickedOnVideo(int postId, String videoHost, String videoId, String videoId2) {
        Log.d(LOG_TAG, "clickedOnVideo: " + postId + "-" + videoHost + "-" + videoId + "-" + videoId2);

        Intent intent = Utility.makeVideoViewActivityIntent(this, postId, videoHost, videoId, videoId2);
        this.startActivity(intent);
    }

    class YoukuJSInterface {
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

    class VideoInfoInterface {
        VideoInfoListener vil;
        public VideoInfoInterface(VideoInfoListener vil) {
            this.vil = vil;
        }

        @JavascriptInterface
        public void gotoUnit(String unitId) {
            vil.clickedOnUnit(unitId);
        }

        @JavascriptInterface
        public void gotoVideo(int postId, String videoHost, String videoId, String videoId2) {
            vil.clickedOnVideo(postId, videoHost, videoId, videoId2);
        }
    }

}

/*
* 1. 垂直进入
* 2. 手动全屏, 旋转全屏 (要记住全屏的原因) (视频prepare好以� 才允许旋�
* 3. 锁定全屏, 当手动全屏时自动锁定
* 4. 全凭状态下按返回键, 返回垂直状�
* 5.
*
* */

/*
* TODOs
* 1. 页面的响� 选择机体和视�
* 2. Loading时显示视频来自哪, 17173还是优酷
* DONE 3. 添加Loading超时
* */