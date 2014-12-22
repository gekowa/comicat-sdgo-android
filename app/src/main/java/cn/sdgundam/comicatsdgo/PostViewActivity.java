package cn.sdgundam.comicatsdgo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.umeng.analytics.MobclickAgent;

import cn.sdgundam.comicatsdgo.video.VideoInfoListener;


public class PostViewActivity extends Activity
    implements VideoInfoListener {

    private int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            postId = extra.getInt("id");
        } else {
            finish();
        }

        setContentView(R.layout.activity_post_view);

        VideoInfoInterface vii = new VideoInfoInterface(this);

        WebView webView = (WebView)findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                PostViewActivity.this.findViewById(R.id.loading).setVisibility(View.GONE);
            }
        });
        webView.loadUrl(String.format("http://www.sdgundam.cn/pages/app/post-view-android.aspx?id=%d&page=0", postId));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(vii, "$VLI");

        loadAds();
    }

    private void loadAds() {
        InterstitialAdsManager.getInstance().displayNextAds(this);
    }

    @Override
    public void clickedOnUnit(String unitId) {
        Intent intent = Utility.makeUnitViewActivityIntent(this, unitId);
        this.startActivity(intent);
    }

    @Override
    public void clickedOnVideo(int postId, String videoHost, String videoId, String videoId2) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("新闻详细");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("新闻详细");
        MobclickAgent.onPause(this);
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



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.post_view, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
