package cn.sdgundam.comicatsdgo;

import android.app.Activity;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

//import android.widget.VideoView;
//import android.widget.MediaController;

import cn.sdgundam.comicatsdgo.video.GetYoukuVideoInfoAsyncTask;
import cn.sdgundam.comicatsdgo.video.OnReceivedYoukuVideoSrc;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;


public class VideoViewActivity extends Activity implements OnReceivedYoukuVideoSrc {
    private int postId;
    private String videoHost;
    private String videoId;
    private String videoId2;

    private String videoURL;
    private VideoView videoView;

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
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            videoView.requestLayout();
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            videoView.requestLayout();
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
                videoView.setMediaController(new MediaController(VideoViewActivity.this));
                videoView.setVideoPath(videoURL);
                videoView.requestFocus();
                }
            });
        }
    }

    String getVideoURL17173(String videoId) {
        return String.format("http://v.17173.com/api/%s-4.m3u8", videoId);
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

