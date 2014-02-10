package pervasive.delehellelassenberger.phone;

import android.annotation.TargetApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends FragmentActivity implements android.app.ActionBar.TabListener {
    final byte VOLUME_UP = 0;
    final byte VOLUME_DOWN = 1;
    final byte CHAN_UP = 2;
    final byte CHAN_DOWN = 3;

    @Override
    public void onTabSelected(android.app.ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(android.app.ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(android.app.ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    public class TabsPagerAdapter extends FragmentPagerAdapter {

        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {

            switch (index) {
                case 0:
                    return new RemoteFragment();
                case 1:
                    return new SettingsFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            // get item count - equal to number of tabs
            return 2;
        }

    }

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private android.app.ActionBar actionBar;
    // Tab titles
    private String[] tabs = {"Remote", "Settings"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Add the tabs
        for (String tab_name : tabs)
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }


    public void volUp(View v) {
        new TcpWorker().execute(VOLUME_UP);
    }

    public void volDown(View v) {
        new TcpWorker().execute(VOLUME_DOWN);
    }

    public void chanUp(View v) {
        new TcpWorker().execute(CHAN_UP);
    }

    public void chanDown(View v) {
        new TcpWorker().execute(CHAN_DOWN);
    }
}