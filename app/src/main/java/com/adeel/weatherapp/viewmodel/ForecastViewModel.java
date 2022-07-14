package com.adeel.weatherapp.viewmodel;


import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.adeel.weatherapp.model.Resource;
import com.adeel.weatherapp.model.SaveAustin;
import com.adeel.weatherapp.model.SaveChicago;
import com.adeel.weatherapp.model.SaveCurrent;
import com.adeel.weatherapp.model.SaveNewYork;
import com.adeel.weatherapp.model.SavedDailyForecast;
import com.adeel.weatherapp.model.UviDb;
import com.adeel.weatherapp.repository.ForecastRepository;

import java.util.List;

import javax.inject.Inject;

public class ForecastViewModel extends ViewModel {

    private ForecastRepository forecastRepository;

    @SuppressWarnings("unchecked")
    @Inject
    public ForecastViewModel(ForecastRepository forecastRepository) {
        this.forecastRepository = forecastRepository;
    }

    @VisibleForTesting
    public LiveData<Resource<List<SavedDailyForecast>>> fetchResults(String city, String numDays) {
        return forecastRepository.fetchForecast(city,numDays);
    }
    @VisibleForTesting
    public LiveData<Resource<List<SaveNewYork>>> fetchResultsNewYork(String city, String numDays) {
        return forecastRepository.fetchForecastNewYork(city,numDays);
    }
    @VisibleForTesting
    public LiveData<Resource<List<SaveChicago>>> fetchResultsChicago(String city, String numDays) {
        return forecastRepository.fetchForecastChicago(city,numDays);
    }

    @VisibleForTesting
    public LiveData<Resource<List<SaveAustin>>> fetchResultsAustin(String city, String numDays) {
        return forecastRepository.fetchForecastAustin(city,numDays);
    }

    @VisibleForTesting
    public LiveData<Resource<List<SaveCurrent>>> fetchResultsCurrent(String city, String numDays) {
        return forecastRepository.fetchForecastCurrent(city,numDays);
    }


    @VisibleForTesting
    public LiveData<Resource<UviDb>> fetchUvi(Double lat, Double lon) {
        return forecastRepository.fetchUvi(lat, lon);
    }
}
