package cn.sdgundam.comicatsdgo;

import android.app.ActionBar;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import net.youmi.android.offers.OffersManager;

import cn.sdgundam.comicatsdgo.top_view_fragment.HomeFragment;
import cn.sdgundam.comicatsdgo.top_view_fragment.NewsFragment;
import cn.sdgundam.comicatsdgo.top_view_fragment.OriginFragment;
import cn.sdgundam.comicatsdgo.top_view_fragment.VideoFragment;


public class MainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private int currentPosition = -1;

    boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        UmengUpdateAgent.update(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        MobclickAgent.onPause(this);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.press_back_again_to_exit), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the title
        String[] allTitles = getResources().getStringArray(R.array.top_view_titles);
        mTitle = allTitles[position];

        if (position != currentPosition) {
            // fade out the current
            AlphaAnimation anim = new AlphaAnimation(1f, 0f);
            anim.setDuration(500);
            findViewById(R.id.container).startAnimation(anim);
        }
    }

    @Override
    public void onNavigationDrawerClosedForItemSelected(int position) {
        if(position != currentPosition) {
            // update the main content by replacing fragments
            FragmentManager fm = getSupportFragmentManager();
            switch (position) {
                case 0:
                    fm.beginTransaction()
                            .replace(R.id.container, HomeFragment.getInstance())
                            .commit();

//                fm.beginTransaction()
//                    .replace(R.id.container, VideoFragment.getInstance())
//                    .commit();

//                    fm.beginTransaction()
//                            .replace(R.id.container, NewsFragment.getInstance())
//                            .commit();
                    break;
                case 1:
                    fm.beginTransaction()
                            .replace(R.id.container, VideoFragment.getInstance())
                            .commit();
                    break;
                case 2:
                    fm.beginTransaction()
                            .replace(R.id.container, NewsFragment.getInstance())
                            .commit();
                    break;
                case 3:
                    fm.beginTransaction()
                        .replace(R.id.container, OriginFragment.getInstance())
                        .commit();
                    break;
            }

            currentPosition = position;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }
//
//    /**
//     * A placeholder fragment containing a simple view.
//     */
//    public static class PlaceholderFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        public PlaceholderFragment() {
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//            return rootView;
//        }
//
//        @Override
//        public void onAttach(Activity activity) {
//            super.onAttach(activity);
//            ((MainActivity) activity).onSectionAttached(
//                    getArguments().getInt(ARG_SECTION_NUMBER));
//        }
//    }

}
