package com.adeel.weatherapp.viewmodel;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.adeel.weatherapp.model.Resource;
import com.adeel.weatherapp.model.UviDb;
import com.adeel.weatherapp.repository.ForecastRepository;

import javax.inject.Inject;

public class UviViewModel extends ViewModel {

    private ForecastRepository forecastRepository;

    @SuppressWarnings("unchecked")
    @Inject
    public UviViewModel(ForecastRepository forecastRepository) {
        this.forecastRepository = forecastRepository;
    }

    @VisibleForTesting
    public LiveData<Resource<UviDb>> fetchUvi(Double lat, Double lon) {
        return forecastRepository.fetchUvi(lat, lon);
    }
}
