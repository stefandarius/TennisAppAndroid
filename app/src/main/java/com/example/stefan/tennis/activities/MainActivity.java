package com.example.stefan.tennis.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.stefan.tennis.R;
import com.example.stefan.tennis.adapters.FragmentViewPagerAdapter;
import com.example.stefan.tennis.adapters.ViewPagerAdapter;
import com.example.stefan.tennis.fragments.AbonamenteListFragment;
import com.example.stefan.tennis.fragments.AntrenorListFragment;
import com.example.stefan.tennis.fragments.BaseFragment;
import com.example.stefan.tennis.fragments.CalendarFragment;
import com.example.stefan.tennis.fragments.NotificariFragment;
import com.example.stefan.tennis.fragments.SetariFragment;
import com.example.stefan.tennis.fragments.SportivListFragment;
import com.example.stefan.tennis.fragments.TipuriAntrenamenteListFragment;
import com.example.stefan.tennis.utils.ProjectUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import devlight.io.library.ntb.NavigationTabBar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {

    private static final String TAG = "MainActivity";
    @BindView(R.id.viewPager)
    ViewPagerAdapter viewPager;
    @BindView(R.id.navBar)
    NavigationTabBar navBar;
    private Unbinder binder;
    private FragmentViewPagerAdapter adapter;

    private String[] titles;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private View fab;

    //casa de piatra visanca si lucica

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binder = ButterKnife.bind(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        titles = getResources().getStringArray(R.array.numeTabrui);

        fab = findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        toggle.setDrawerIndicatorEnabled(true);
        toolbar.setTitle(titles[0]);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportFragmentManager().addOnBackStackChangedListener(this);

        //ProjectUtils.addFragmentToActivity(this, CalendarFragment.newInstance(), false, CalendarFragment.TAG);

        setupViewPager(viewPager);
        initializeNavigationBottom();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            ProjectUtils.addFragmentToActivity(this, new SetariFragment(), true, SetariFragment.TAG);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if (id == R.id.nav_camera) {
            TipuriAntrenamenteListFragment tipuriAntrenamenteListFragment = new TipuriAntrenamenteListFragment();
            ProjectUtils.addFragmentToActivity(this, tipuriAntrenamenteListFragment, true, TipuriAntrenamenteListFragment.TAG);
        } else if (id == R.id.nav_abonamente) {
            AbonamenteListFragment abonamenteListFragment = new AbonamenteListFragment();
            ProjectUtils.addFragmentToActivity(this, abonamenteListFragment, true, AbonamenteListFragment.TAG);
        } else if (id == R.id.nav_slideshow) {
            SportivListFragment sportivListFragment = new SportivListFragment();
            ProjectUtils.addFragmentToActivity(this, sportivListFragment, true, SportivListFragment.TAG);
        } else if (id == R.id.nav_manage) {
            ProjectUtils.addFragmentToActivity(this, new AntrenorListFragment(), true, AntrenorListFragment.TAG);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public BaseFragment getTopFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        int count = fragmentManager.getBackStackEntryCount();
        if (count > 0) {
            String tag = fragmentManager.getBackStackEntryAt(count - 1).getName();
            return (BaseFragment) fragmentManager.findFragmentByTag(tag);
        }
        return null;
    }


    @Override
    public void onBackStackChanged() {
        BaseFragment b = getTopFragment();
        if (b != null) {
            Log.v(TAG, b.getClass().getSimpleName());
            b.setupTollbar();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        int count = fragmentManager.getBackStackEntryCount();
        if (count > 0) {
            navBar.setVisibility(View.GONE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toggle.setDrawerIndicatorEnabled(false);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(v -> {
                onBackPressed();
            });
        } else {
            toolbar.setTitle(adapter.getPageTitle(viewPager.getCurrentItem()));
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toggle.setDrawerIndicatorEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
            ((BaseFragment) adapter.getItem(viewPager.getCurrentItem())).setupTollbar();
            navBar.setVisibility(View.VISIBLE);
            toolbar.setNavigationOnClickListener(v -> {
                drawer.openDrawer(GravityCompat.START);
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binder.unbind();
    }

    private void initializeNavigationBottom() {
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(this, R.drawable.calendar),
                        Color.parseColor("#0c92c1")
                ).title("Calendar")
                        .badgeTitle("CAL")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(this, R.drawable.person),
                        Color.parseColor("#0c92c1")
                ).title("Sportivi")
                        .badgeTitle("SPORT")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(this, R.drawable.trending),
                        Color.parseColor("#0c92c1")
                ).title("Statistici")
                        .badgeTitle("STATS")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        ContextCompat.getDrawable(this, R.drawable.ic_notifications_white),
                        Color.parseColor("#0c92c1")
                ).title("Notificari")
                        .badgeTitle("NOTIF")
                        .build()
        );


        navBar.setModels(models);
        navBar.setViewPager(viewPager, 0);
        toolbar.setTitle(adapter.getPageTitle(0).toString());
        navBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(final NavigationTabBar.Model model, final int index) {
                toolbar.setTitle(titles[index]);
                ((BaseFragment) adapter.getItem(index)).setupTollbar();
            }

            @Override
            public void onEndTabSelected(final NavigationTabBar.Model model, final int index) {
                //  Toast.makeText(MainActivity.this, String.format("onEndTabSelected #%d", index), Toast.LENGTH_SHORT).show();
            }
        });

//        navigationTabBar.post(new Runnable() {
//            @Override
//            public void run() {
//                final View viewPager = findViewById(R.id.viewPager);
//                ((ViewGroup.MarginLayoutParams) viewPager.getLayoutParams()).topMargin =
//                        (int) -navigationTabBar.getBadgeMargin();
//                viewPager.requestLayout();
//            }
//        });
        //navigationTabBar.setIsSwiped(false);
        //  navigationTabBar.setTitleMode(NavigationTabBar.TitleMode.ACTIVE);
//        navigationTabBar.setBadgeGravity(NavigationTabBar.BadgeGravity.BOTTOM);
        //navigationTabBar.setBadgePosition(NavigationTabBar.BadgePosition.RIGHT);
//        // navigationTabBar.setTypeface("fonts/custom_font.ttf");
        //navigationTabBar.setIsBadged(true);
        //  navigationTabBar.setIsTitled(true);
//        navigationTabBar.setIsTinted(true);
//        navigationTabBar.setIsBadgeUseTypeface(true);
//        navigationTabBar.setBadgeBgColor(Color.RED);
//        navigationTabBar.setBadgeTitleColor(Color.WHITE);
//        navigationTabBar.setIsSwiped(true);
        //navigationTabBar.setBgColor(Color.BLACK);
        //navigationTabBar.setBadgeSize(10);
        //navigationTabBar.setTitleSize(10);
        //navigationTabBar.setIconSizeFraction(0.5);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new FragmentViewPagerAdapter(getSupportFragmentManager()) {
            @Override
            public void onFinishLoading() {
//                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//                toggle.setDrawerIndicatorEnabled(true);
//                toolbar.setNavigationOnClickListener(v -> {
//                    drawer.openDrawer(GravityCompat.START);
//                });
//                toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
                toolbar.setTitle("Calendar");
            }
        };

        adapter.addFragment(CalendarFragment.newInstance(), titles[0]);
        adapter.addFragment(SportivListFragment.newInstance(), titles[1]);
        adapter.addFragment(AbonamenteListFragment.newInstance(), titles[2]);
        adapter.addFragment(NotificariFragment.newInstance(), titles[3]);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
    }

}