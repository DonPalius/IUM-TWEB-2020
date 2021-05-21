package com.ripetizioni.app.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.ripetizioni.app.R;
import com.ripetizioni.app.adapter.ViewPagerAdapter;
import com.ripetizioni.app.api.RetrofitClient;
import com.ripetizioni.app.model.User;
import com.ripetizioni.app.utils.LoginManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    private ViewPager pager;
    private TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pager = findViewById(R.id.pager);
        tabs = findViewById(R.id.tab_layout);
        pager.setOffscreenPageLimit(0);
        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), 0));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 1) {
                    try {
                        BadgeDrawable badge = tabs.getTabAt(1).getOrCreateBadge();
                        badge.setVisible(false);
                    } catch(Exception ignored) {}
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        User user = LoginManager.getUser();
        if(user != null) {
            menu.findItem(R.id.nav_logout).setVisible(true);
            menu.findItem(R.id.nav_login).setVisible(false);
        } else {
            menu.findItem(R.id.nav_logout).setVisible(false);
            menu.findItem(R.id.nav_login).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_logout:
                logout();
                break;
            case R.id.nav_login:
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        RetrofitClient.getApi().logout().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((u, e) -> {
                            LoginManager.clear();
                            finish();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                );
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 1) {
            pager.setCurrentItem(0,true);
        } else super.onBackPressed();
    }
}
