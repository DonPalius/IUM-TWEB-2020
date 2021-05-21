package com.ripetizioni.app.adapter;

import com.ripetizioni.app.utils.LoginManager;
import com.ripetizioni.app.views.BookingsFragment;
import com.ripetizioni.app.views.CalendarFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private boolean loggedIn;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        loggedIn = LoginManager.isLoggedIn();
    }

    @Override
    public int getCount() {
        return loggedIn ? 2 : 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if(position == 0) return "Calendario";
        return "Prenotazioni";

    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        if(position == 0) return new CalendarFragment();
        return new BookingsFragment();
    }
}