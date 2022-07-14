package com.adeel.weatherapp.di;


import com.adeel.weatherapp.ui.settings.SettingsFragment;
import com.adeel.weatherapp.ui.today.TodayFragment;
import com.adeel.weatherapp.ui.weekly.WeeklyFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract TodayFragment contributeTodayFragment();

    @ContributesAndroidInjector
    abstract WeeklyFragment contributeWeeklyFragment();

    @ContributesAndroidInjector
    abstract SettingsFragment contributeSettingsFragment();
}
