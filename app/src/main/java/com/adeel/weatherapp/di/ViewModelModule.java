package com.adeel.weatherapp.di;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.adeel.weatherapp.viewmodel.ForecastViewModel;
import com.adeel.weatherapp.viewmodel.WeatherViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ForecastViewModel.class)
    abstract ViewModel bindForecastViewModel(ForecastViewModel forecastViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(WeatherViewModelFactory factory);
}
