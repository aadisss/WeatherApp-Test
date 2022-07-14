package com.adeel.weatherapp.ui;


import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;


import com.adeel.weatherapp.MainActivity;
import com.adeel.weatherapp.R;
import com.adeel.weatherapp.ui.settings.SettingsFragment;
import com.adeel.weatherapp.ui.today.TodayFragment;
import com.adeel.weatherapp.ui.weekly.WeeklyFragment;

import javax.inject.Inject;

public class NavigationController {
    private final int containerId;
    private final FragmentManager fragmentManager;

    @Inject
    public NavigationController(MainActivity mainActivity) {
        this.containerId = R.id.nav_host_fragment;
        this.fragmentManager = mainActivity.getSupportFragmentManager();
    }

    public void navigateToHome() {
        String tag = "home";
        TodayFragment homeFragment = TodayFragment.create();
        fragmentManager.beginTransaction()
                .replace(containerId, homeFragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    public void navigateToWeekly() {
        String tag = "weekly";
        WeeklyFragment weeklyFragment = WeeklyFragment.create();
        fragmentManager.beginTransaction()
                .replace(containerId, weeklyFragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    public void navigateToSettings() {
        String tag = "settings";
        DialogFragment settingsFragment = SettingsFragment.create();
        settingsFragment.show(fragmentManager, tag);
    }
}
