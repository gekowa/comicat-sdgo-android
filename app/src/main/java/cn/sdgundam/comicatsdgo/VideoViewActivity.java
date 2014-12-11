package cn.sdgundam.comicatsdgo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import android.media.MediaPlayer;
//import android.widget.VideoView;
//import android.widget.MediaController;

import com.umeng.analytics.MobclickAgent;

import java.util.Arrays;

import cn.sdgundam.comicatsdgo.video.GetYoukuVideoInfoAsyncTask;
import cn.sdgundam.comicatsdgo.video.OnReceivedYoukuVideoSrc;

import cn.sdgundam.comicatsdgo.video.VideoInfoListener;
import cn.sdgundam.comicatsdgo.video.VitamioLibsChecker;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import io.vov.vitamio.MediaPlayer;


public class VideoViewActivity extends Activity implements
        OnReceivedYoukuVideoSrc,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnInfoListener,
        MediaPlayer.OnBufferingUpdateListener,
        View.OnSystemUiVisibilityChangeListener,
        VideoInfoListener {

    static final String LOG_TAG = VideoViewActivity.class.getSimpleName();

    static final int ORIENTATION_THRESHOLD = 20;
    static final int SYSTEM_UI_FLAG_MY_FULLSCREEN =
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
    static final int VIDEO_PREPARE_TIMEOUT = 10000;  // ms
    static final String[] SUPPORTED_VIDEO_HOSTS = new String[] {"2", "4"};

    static final String VIDEO_POSITION_KEY = "VideoPosition";

    // state variables
    private int postId;
    private String videoHost;
    private String videoId;
    private String videoId2;

    private String videoURL;

    private float videoAspectRatio;

    private int orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    private boolean isVideoPrepared;
    private boolean isOrientationLocked;
    private boolean isRestarted = false;

    private Bundle myState;

    // Views
    private View decorView;
    private VideoView videoView;
    private WebView infoWebView;
    private MediaController mediaController;
    private View rootView;
    private FrameLayout videoContainer;
    private FrameLayout.LayoutParams layoutParamsPortraitForVideoView;
    private LinearLayout.LayoutParams layoutParamsPortraitForVideoContainer;
    private View uiBlocker;
    private ImageView playButton;
    private ViewGroup playButtonContainer;
    private ViewGroup loadingView;
    private ViewGroup videoOverlay;

    private OrientationEventListener orientationEventListener;

    private Handler videoTimeoutHandler;
    private Runnable videoTimeoutActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialize(getIntent().getExtras());

        // check supported video hosts
        if (Arrays.binarySearch(SUPPORTED_VIDEO_HOSTS, videoHost) < 0) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.unsupported_video_host))
                    .setMessage(getString(R.string.watch_this_video_on_pc))
                    .setPositiveButton(getString(R.string.button_text_OK), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            VideoViewActivity.this.finish();
                        }
                    })
                    .show();
        }

        setContentView(R.layout.activity_video_view);

        initializeViews();

        prepareInfoWebView();

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

        // Debug
        if (BuildConfig.DEBUG) {
            getActionBar().setTitle(postId + "");
        }
    }

    private void initialize(Bundle extra) {
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

        Log.d(LOG_TAG, "initialize: " + postId + "-" + videoHost + "-" + videoId + "-" + videoId2);
    }

    private void initializeViews() {
        Resources resources = getResources();

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(this);

        infoWebView = (WebView)findViewById(R.id.web_view);

        videoContainer = (FrameLayout)findViewById(R.id.video_container);

        rootView = findViewById(R.id.root_view);

        loadingView = (ViewGroup)findViewById(R.id.loading);

        TextView videoHostedByTextView = (TextView) findViewById(R.id.text_view_video_hosted_by);
        try {
            videoHostedByTextView.setText(resources.getString(R.string.video_hosted_by) +
                    resources.getString(resources.getIdentifier("video_host_" + videoHost, "string", this.getPackageName())));
        } catch (Resources.NotFoundException e) {
            videoHostedByTextView.setVisibility(View.INVISIBLE);
        }

        playButtonContainer = (ViewGroup)findViewById(R.id.btn_play_container);

        playButton = (ImageView)findViewById(R.id.btn_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.INVISIBLE);
                playButtonContainer.setVisibility(View.INVISIBLE);

                showVideoLoading();

                startVideoPlay();
            }
        });

        videoOverlay = (ViewGroup)findViewById(R.id.video_overlay);

        videoView = (VideoView)findViewById(R.id.video_view);
        videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE_VERTICALLY, 0);

        uiBlocker = findViewById(R.id.ui_blocker);
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume begin " + postId);
        super.onResume();

        MobclickAgent.onPageStart("视频观看");
        MobclickAgent.onResume(this);

        if (orientationEventListener.canDetectOrientation() && isVideoPrepared) {
            orientationEventListener.enable();
        }

        if (isVideoPrepared && isRestarted) {
            // showVideoLoading();
            // blockUI();
            mediaController.show();
        }

        Log.d(LOG_TAG, "onResume end " + postId);
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "onPause begin " + postId);
        super.onPause();

        MobclickAgent.onPageEnd("视频观看");
        MobclickAgent.onPause(this);

        orientationEventListener.disable();
        disableVideoTimeout();

        if (videoView.isPlaying()) {
            videoView.pause();
        }

        myState = new Bundle();
        // save the playback progress
        myState.putLong(VIDEO_POSITION_KEY, videoView.getCurrentPosition());
        // save if is playing


        Log.d(LOG_TAG, "onPause end " + postId);
    }

    private void disableVideoTimeout() {
        if (videoTimeoutHandler != null) {
            videoTimeoutHandler.removeCallbacks(videoTimeoutActions);
        }
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "onStop begin " + postId);

        super.onStop();
        videoView.stopPlayback();

        Log.d(LOG_TAG, "onStop end " + postId);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        isRestarted = true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // TODO: infoWebView scroll position
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
        } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            // TODO: pop from stack
            // TODO: if none, use super
            super.onBackPressed();
        }
    }

    void configureVideoViewOnOrientation() {
        Log.d(LOG_TAG, "configureVideoViewOnOrientation orientation:" + orientation);

        if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {

            getActionBar().hide();

//            layoutParamsPortraitForVideoContainer = (LinearLayout.LayoutParams)videoContainer.getLayoutParams();
//            LinearLayout.LayoutParams landscapeLayoutForVideoContainer = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            landscapeLayoutForVideoContainer.weight = 1;
//            videoContainer.setLayoutParams(landscapeLayoutForVideoContainer);

            layoutParamsPortraitForVideoView = (FrameLayout.LayoutParams)videoView.getLayoutParams();
            FrameLayout.LayoutParams landscapeLayout = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            landscapeLayout.gravity = Gravity.CENTER_HORIZONTAL;
            videoView.setLayoutParams(landscapeLayout);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
                    videoView.requestLayout();
                }
            }, 500);

            hideSystemUI();

            infoWebView.setVisibility(View.INVISIBLE);
        }
        else if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {

            getActionBar().show();

            if (layoutParamsPortraitForVideoView != null) {
                videoView.setLayoutParams(layoutParamsPortraitForVideoView);
            }

            videoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE_VERTICALLY, 0);

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

    void blockUI() {
        uiBlocker.setVisibility(View.VISIBLE);
    }

    void unblockUI() {
        uiBlocker.setVisibility(View.INVISIBLE);
    }

    private void hideVideoLoading() {
        loadingView.setVisibility(View.INVISIBLE);
    }

    private void showVideoLoading() {
        loadingView.setVisibility(View.VISIBLE);
    }

    private void showVideoOverlay() {
        videoOverlay.setVisibility(View.VISIBLE);
    }

    private void hideVideoOverlay() {
        videoOverlay.setVisibility(View.INVISIBLE);
    }

    void prepareVideoPlay() {
        isVideoPrepared = false;

        showVideoOverlay();

        orientationEventListener.disable();

        if (mediaController != null) {
            mediaController.hide();
        }

        // ui things
        // display play button
        showPlayButton();
        hideVideoLoading();
    }

    private void showPlayButton() {
        playButton.setVisibility(View.VISIBLE);
        playButtonContainer.setVisibility(View.VISIBLE);
    }

    void startVideoPlay() {
        blockUI();

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
            startYoukuVideoPlay(videoId);
        }
    }

    void startYoukuVideoPlay(String videoId) {
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
        videoURL = videoSrc;
        play();
    }

    void play () {
        Log.d(LOG_TAG, "play begin" + postId);

//        if (!LibsChecker.checkVitamioLibs(VideoViewActivity.this)) {
//            // TODO: prompt vitamio fails
//            return;
//        }

        VitamioLibsChecker.checkVitamioLibs(this);

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
//                    mediaController.setOnShownListener(new MediaController.OnShownListener() {
//                        @Override
//                        public void onShown() {
//                            if (videoView.isPlaying() &&
//                                    orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//                                showSystemUI();
//                            }
//                        }
//                    });

                    videoView.setMediaController(mediaController);

                    videoView.setOnPreparedListener(VideoViewActivity.this);
                    videoView.setOnErrorListener(VideoViewActivity.this);
                    videoView.setOnInfoListener(VideoViewActivity.this);
                    videoView.setOnBufferingUpdateListener(VideoViewActivity.this);

                    videoView.setVideoPath(videoURL);

                    Log.d(LOG_TAG, "play run end");
                }
            });

            videoTimeoutActions = new Runnable() {
                @Override
                public void run() {
                    // when timeout

                    int videoViewState = videoView.getCurrentState();

                    if (!isVideoPrepared || videoViewState == VideoView.STATE_ERROR) {
                        Log.d(LOG_TAG, "video prepare timeout.");

                        try {
                            new AlertDialog.Builder(VideoViewActivity.this)
                                    .setTitle(VideoViewActivity.this.getString(R.string.data_loading_failure))
                                    .setPositiveButton(getString(R.string.button_text_OK), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            VideoViewActivity.this.finish();
                                        }
                                    })
                                    .setMessage(getString(R.string.network_error_check_try_again))
                                    .show();
                        } catch (WindowManager.BadTokenException e) {
                            // do nothing...
                        }

//                        videoView.suspend();
                    }
                }
            };

            videoTimeoutHandler = new Handler();
            videoTimeoutHandler.postDelayed(videoTimeoutActions, VIDEO_PREPARE_TIMEOUT);
        }
    }

    // Vitamio
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d(LOG_TAG, "onPrepared");

        unblockUI();
        hideVideoLoading();
        hideVideoOverlay();

        blinkMediaController();

        ViewGroup controllerAnchor = (ViewGroup) findViewById(R.id.video_container);
        mediaController.setAnchorView(controllerAnchor);

        videoAspectRatio = mediaPlayer.getVideoAspectRatio();

        isVideoPrepared = true;

        // ((LinearLayout.LayoutParams)videoContainer.getLayoutParams()).weight = 0;

        if (isRestarted) {
            long position = myState.getLong(VIDEO_POSITION_KEY);
            videoView.seekTo(position);
        }
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
        // Toast.makeText(this, "Video Error: " + what + "," + extra, Toast.LENGTH_SHORT).show();

        disableVideoTimeout();

        new AlertDialog.Builder(VideoViewActivity.this)
                .setTitle(VideoViewActivity.this.getString(R.string.data_loading_failure))
                .setPositiveButton(getString(R.string.button_text_OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VideoViewActivity.this.finish();
                    }
                })
                .setMessage(getString(R.string.network_error_check_try_again))
                .show();
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        // Log.d(LOG_TAG, "Buffering..." + percent);
    }

    String getVideoURL17173(String videoId) {
        return String.format("http://v.17173.com/api/%s-4.m3u8", videoId);
    }

    void prepareInfoWebView() {
        VideoInfoInterface vii = new VideoInfoInterface(this);

        infoWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                unblockUI();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                // TODO: alert some error...

                unblockUI();
            }
        });

        // blockUI here is not because the info webview needs ui to be blocked, for the VideoView actually
        blockUI();

        infoWebView.loadUrl(String.format("http://www.sdgundam.cn/pages/app/post-view-video-android.aspx?id=%d", postId));
        infoWebView.getSettings().setJavaScriptEnabled(true);
        infoWebView.addJavascriptInterface(vii, "$VLI");
    }

    @Override
    public void clickedOnUnit(String unitId) {
        // Toast.makeText(VideoViewActivity.this, "Unit: " + unitId, Toast.LENGTH_SHORT).show();

        // this.videoView.stopPlayback();

        Intent intent = Utility.makeUnitViewActivityIntent(this, unitId);
        this.startActivity(intent);
    }

    @Override
    public void clickedOnVideo(int postId, String videoHost, String videoId, String videoId2) {
        Log.d(LOG_TAG, "clickedOnVideo: " + postId + "-" + videoHost + "-" + videoId + "-" + videoId2);

        this.postId = postId;
        this.videoHost = videoHost;
        this.videoId = videoId;
        this.videoId2 = videoId2;

        disableVideoTimeout();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                blockUI();

                if (mediaController != null) {
                    mediaController.hide();
                }

                videoView.pause();
                // videoView.setVisibility(View.INVISIBLE);
                prepareInfoWebView();

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        prepareVideoPlay();
                    }
                });
            }
        }, 500);
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