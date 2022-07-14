package com.adeel.weatherapp.repository;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;


import com.adeel.weatherapp.AppExecutors;
import com.adeel.weatherapp.BuildConfig;
import com.adeel.weatherapp.api.ApiResponse;
import com.adeel.weatherapp.api.WeatherService;
import com.adeel.weatherapp.db.AustinDao;
import com.adeel.weatherapp.db.ChicagoDao;
import com.adeel.weatherapp.db.CurrentDao;
import com.adeel.weatherapp.db.ForecastDao;
import com.adeel.weatherapp.db.NewyorkDao;
import com.adeel.weatherapp.model.Resource;
import com.adeel.weatherapp.model.SaveAustin;
import com.adeel.weatherapp.model.SaveChicago;
import com.adeel.weatherapp.model.SaveCurrent;
import com.adeel.weatherapp.model.SaveNewYork;
import com.adeel.weatherapp.model.SavedDailyForecast;
import com.adeel.weatherapp.model.Uvi;
import com.adeel.weatherapp.model.UviDb;
import com.adeel.weatherapp.model.WeatherForecast;
import com.adeel.weatherapp.util.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ForecastRepository {

    private final ForecastDao forecastDao;
    private final NewyorkDao newyorkDao;
    private final ChicagoDao chicagoDao;
    private final AustinDao austinDao;
    private final CurrentDao currentDao;
    private final WeatherService weatherService;
    private final AppExecutors appExecutors;

    @Inject
    ForecastRepository(AppExecutors appExecutors, ForecastDao forecastDao,NewyorkDao newyorkDao,
                      ChicagoDao chicagoDao,AustinDao austinDao,CurrentDao currentDao, WeatherService weatherService) {
        this.forecastDao = forecastDao;
        this.weatherService = weatherService;
        this.appExecutors = appExecutors;
        this.newyorkDao = newyorkDao;
        this.chicagoDao = chicagoDao;
        this.austinDao = austinDao;
        this.currentDao = currentDao;
    }

    public LiveData<Resource<List<SavedDailyForecast>>> loadForecast(String city, String numDays) {
        return new NetworkBoundResource<List<SavedDailyForecast>, WeatherForecast>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull WeatherForecast item) {
                if (item != null && item.getDailyForecasts() != null) {
                    List<SavedDailyForecast> savedDailyForecasts = new ArrayList<SavedDailyForecast>();
                    for (int i = 0; i < item.getDailyForecasts().size(); i++) {
                        SavedDailyForecast savedDailyForecast = new SavedDailyForecast();
                        savedDailyForecast.setLat(item.getCity().getCoord().getLat());
                        savedDailyForecast.setLon(item.getCity().getCoord().getLon());
                        savedDailyForecast.setDate(item.getDailyForecasts().get(i).getDt());
                        savedDailyForecast.setMaxTemp(item.getDailyForecasts().get(i).getTemp().getMax());
                        savedDailyForecast.setMinTemp(item.getDailyForecasts().get(i).getTemp().getMin());
                        savedDailyForecast.setDayTemp(item.getDailyForecasts().get(i).getTemp().getDay());
                        savedDailyForecast.setEveningTemp(item.getDailyForecasts().get(i).getTemp().getEve());
                        savedDailyForecast.setMorningTemp(item.getDailyForecasts().get(i).getTemp().getMorn());
                        savedDailyForecast.setNightTemp(item.getDailyForecasts().get(i).getTemp().getNight());
                        savedDailyForecast.setFeelslikeDay(item.getDailyForecasts().get(i).getFeelsLike().getDay());
                        savedDailyForecast.setFeelslikeEve(item.getDailyForecasts().get(i).getFeelsLike().getEve());
                        savedDailyForecast.setFeelslikeMorning(item.getDailyForecasts().get(i).getFeelsLike().getMorn());
                        savedDailyForecast.setFeelslikeNight(item.getDailyForecasts().get(i).getFeelsLike().getNight());
                        savedDailyForecast.setHumidity(item.getDailyForecasts().get(i).getHumidity());
                        savedDailyForecast.setWind(item.getDailyForecasts().get(i).getSpeed());
                        savedDailyForecast.setDescription(item.getDailyForecasts().get(i).getWeather().get(0).getDescription());
                        savedDailyForecast.setWeatherid(item.getDailyForecasts().get(i).getWeather().get(0).getId());
                        savedDailyForecast.setImageUrl(item.getDailyForecasts().get(i).getWeather().get(0).getIcon());
                        savedDailyForecasts.add(savedDailyForecast);
                    }
                    forecastDao.insertForecastList(savedDailyForecasts);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<SavedDailyForecast> data) {
                return data == null || data.isEmpty();
            }

            @NonNull
            @Override
            protected LiveData<List<SavedDailyForecast>> loadFromDb() {
                return forecastDao.loadForecast();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<WeatherForecast>> createCall() {
                return weatherService.getWeatherForecast(city, numDays, Constants.UNIT_SYSTEM, BuildConfig.WEATHER_API_KEY);
            }

            @Override
            protected void onFetchFailed() {

            }
        }.asLiveData();
    }

    public LiveData<Resource<List<SavedDailyForecast>>> fetchForecast(String city, String numDays) {
        return new NetworkBoundResource<List<SavedDailyForecast>, WeatherForecast>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull WeatherForecast item) {
                forecastDao.deleteNewsTable();
                if (item != null && item.getDailyForecasts() != null) {
                    List<SavedDailyForecast> savedDailyForecasts = new ArrayList<SavedDailyForecast>();
                    for (int i = 0; i < item.getDailyForecasts().size(); i++) {
                        SavedDailyForecast savedDailyForecast = new SavedDailyForecast();
                        savedDailyForecast.setLat(item.getCity().getCoord().getLat());
                        savedDailyForecast.setLon(item.getCity().getCoord().getLon());
                        savedDailyForecast.setDate(item.getDailyForecasts().get(i).getDt());
                        savedDailyForecast.setMaxTemp(item.getDailyForecasts().get(i).getTemp().getMax());
                        savedDailyForecast.setMinTemp(item.getDailyForecasts().get(i).getTemp().getMin());
                        savedDailyForecast.setDayTemp(item.getDailyForecasts().get(i).getTemp().getDay());
                        savedDailyForecast.setEveningTemp(item.getDailyForecasts().get(i).getTemp().getEve());
                        savedDailyForecast.setMorningTemp(item.getDailyForecasts().get(i).getTemp().getMorn());
                        savedDailyForecast.setNightTemp(item.getDailyForecasts().get(i).getTemp().getNight());
                        savedDailyForecast.setFeelslikeDay(item.getDailyForecasts().get(i).getFeelsLike().getDay());
                        savedDailyForecast.setFeelslikeEve(item.getDailyForecasts().get(i).getFeelsLike().getEve());
                        savedDailyForecast.setFeelslikeMorning(item.getDailyForecasts().get(i).getFeelsLike().getMorn());
                        savedDailyForecast.setFeelslikeNight(item.getDailyForecasts().get(i).getFeelsLike().getNight());
                        savedDailyForecast.setHumidity(item.getDailyForecasts().get(i).getHumidity());
                        savedDailyForecast.setWind(item.getDailyForecasts().get(i).getSpeed());
                        savedDailyForecast.setDescription(item.getDailyForecasts().get(i).getWeather().get(0).getDescription());
                        savedDailyForecast.setWeatherid(item.getDailyForecasts().get(i).getWeather().get(0).getId());
                        savedDailyForecast.setImageUrl(item.getDailyForecasts().get(i).getWeather().get(0).getIcon());
                        savedDailyForecasts.add(savedDailyForecast);
                    }
                    forecastDao.insertForecastList(savedDailyForecasts);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<SavedDailyForecast> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<SavedDailyForecast>> loadFromDb() {
                return forecastDao.loadForecast();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<WeatherForecast>> createCall() {
                return weatherService.getWeatherForecast(city, numDays, Constants.UNIT_SYSTEM, BuildConfig.WEATHER_API_KEY);
            }

            @Override
            protected void onFetchFailed() {

            }
        }.asLiveData();
    }
    public LiveData<Resource<List<SaveNewYork>>> fetchForecastNewYork(String city, String numDays) {
        return new NetworkBoundResource<List<SaveNewYork>, WeatherForecast>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull WeatherForecast item) {
                forecastDao.deleteNewsTable();
                if (item != null && item.getDailyForecasts() != null) {
                    List<SaveNewYork> savedDailyForecasts = new ArrayList<SaveNewYork>();
                    for (int i = 0; i < item.getDailyForecasts().size(); i++) {
                        SaveNewYork savedDailyForecast = new SaveNewYork();
                        savedDailyForecast.setLat(item.getCity().getCoord().getLat());
                        savedDailyForecast.setLon(item.getCity().getCoord().getLon());
                        savedDailyForecast.setDate(item.getDailyForecasts().get(i).getDt());
                        savedDailyForecast.setMaxTemp(item.getDailyForecasts().get(i).getTemp().getMax());
                        savedDailyForecast.setMinTemp(item.getDailyForecasts().get(i).getTemp().getMin());
                        savedDailyForecast.setDayTemp(item.getDailyForecasts().get(i).getTemp().getDay());
                        savedDailyForecast.setEveningTemp(item.getDailyForecasts().get(i).getTemp().getEve());
                        savedDailyForecast.setMorningTemp(item.getDailyForecasts().get(i).getTemp().getMorn());
                        savedDailyForecast.setNightTemp(item.getDailyForecasts().get(i).getTemp().getNight());
                        savedDailyForecast.setFeelslikeDay(item.getDailyForecasts().get(i).getFeelsLike().getDay());
                        savedDailyForecast.setFeelslikeEve(item.getDailyForecasts().get(i).getFeelsLike().getEve());
                        savedDailyForecast.setFeelslikeMorning(item.getDailyForecasts().get(i).getFeelsLike().getMorn());
                        savedDailyForecast.setFeelslikeNight(item.getDailyForecasts().get(i).getFeelsLike().getNight());
                        savedDailyForecast.setHumidity(item.getDailyForecasts().get(i).getHumidity());
                        savedDailyForecast.setWind(item.getDailyForecasts().get(i).getSpeed());
                        savedDailyForecast.setDescription(item.getDailyForecasts().get(i).getWeather().get(0).getDescription());
                        savedDailyForecast.setWeatherid(item.getDailyForecasts().get(i).getWeather().get(0).getId());
                        savedDailyForecast.setImageUrl(item.getDailyForecasts().get(i).getWeather().get(0).getIcon());
                        savedDailyForecasts.add(savedDailyForecast);
                    }
                    newyorkDao.insertNewYorkForecastList(savedDailyForecasts);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<SaveNewYork> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<SaveNewYork>> loadFromDb() {
                return newyorkDao.loadNewYorkForecast();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<WeatherForecast>> createCall() {
                return weatherService.getWeatherForecast(city, numDays, Constants.UNIT_SYSTEM, BuildConfig.WEATHER_API_KEY);
            }

            @Override
            protected void onFetchFailed() {

            }
        }.asLiveData();
    }
    public LiveData<Resource<List<SaveChicago>>> fetchForecastChicago(String city, String numDays) {
        return new NetworkBoundResource<List<SaveChicago>, WeatherForecast>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull WeatherForecast item) {
                forecastDao.deleteNewsTable();
                if (item != null && item.getDailyForecasts() != null) {
                    List<SaveChicago> savedDailyForecasts = new ArrayList<SaveChicago>();
                    for (int i = 0; i < item.getDailyForecasts().size(); i++) {
                        SaveChicago savedDailyForecast = new SaveChicago();
                        savedDailyForecast.setLat(item.getCity().getCoord().getLat());
                        savedDailyForecast.setLon(item.getCity().getCoord().getLon());
                        savedDailyForecast.setDate(item.getDailyForecasts().get(i).getDt());
                        savedDailyForecast.setMaxTemp(item.getDailyForecasts().get(i).getTemp().getMax());
                        savedDailyForecast.setMinTemp(item.getDailyForecasts().get(i).getTemp().getMin());
                        savedDailyForecast.setDayTemp(item.getDailyForecasts().get(i).getTemp().getDay());
                        savedDailyForecast.setEveningTemp(item.getDailyForecasts().get(i).getTemp().getEve());
                        savedDailyForecast.setMorningTemp(item.getDailyForecasts().get(i).getTemp().getMorn());
                        savedDailyForecast.setNightTemp(item.getDailyForecasts().get(i).getTemp().getNight());
                        savedDailyForecast.setFeelslikeDay(item.getDailyForecasts().get(i).getFeelsLike().getDay());
                        savedDailyForecast.setFeelslikeEve(item.getDailyForecasts().get(i).getFeelsLike().getEve());
                        savedDailyForecast.setFeelslikeMorning(item.getDailyForecasts().get(i).getFeelsLike().getMorn());
                        savedDailyForecast.setFeelslikeNight(item.getDailyForecasts().get(i).getFeelsLike().getNight());
                        savedDailyForecast.setHumidity(item.getDailyForecasts().get(i).getHumidity());
                        savedDailyForecast.setWind(item.getDailyForecasts().get(i).getSpeed());
                        savedDailyForecast.setDescription(item.getDailyForecasts().get(i).getWeather().get(0).getDescription());
                        savedDailyForecast.setWeatherid(item.getDailyForecasts().get(i).getWeather().get(0).getId());
                        savedDailyForecast.setImageUrl(item.getDailyForecasts().get(i).getWeather().get(0).getIcon());
                        savedDailyForecasts.add(savedDailyForecast);
                    }
                    chicagoDao.insertChicagoForecastList(savedDailyForecasts);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<SaveChicago> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<SaveChicago>> loadFromDb() {
                return chicagoDao.loadChicagoForecast();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<WeatherForecast>> createCall() {
                return weatherService.getWeatherForecast(city, numDays, Constants.UNIT_SYSTEM, BuildConfig.WEATHER_API_KEY);
            }

            @Override
            protected void onFetchFailed() {

            }
        }.asLiveData();
    }
    public LiveData<Resource<List<SaveAustin>>> fetchForecastAustin(String city, String numDays) {
        return new NetworkBoundResource<List<SaveAustin>, WeatherForecast>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull WeatherForecast item) {
                forecastDao.deleteNewsTable();
                if (item != null && item.getDailyForecasts() != null) {
                    List<SaveAustin> savedDailyForecasts = new ArrayList<SaveAustin>();
                    for (int i = 0; i < item.getDailyForecasts().size(); i++) {
                        SaveAustin savedDailyForecast = new SaveAustin();
                        savedDailyForecast.setLat(item.getCity().getCoord().getLat());
                        savedDailyForecast.setLon(item.getCity().getCoord().getLon());
                        savedDailyForecast.setDate(item.getDailyForecasts().get(i).getDt());
                        savedDailyForecast.setMaxTemp(item.getDailyForecasts().get(i).getTemp().getMax());
                        savedDailyForecast.setMinTemp(item.getDailyForecasts().get(i).getTemp().getMin());
                        savedDailyForecast.setDayTemp(item.getDailyForecasts().get(i).getTemp().getDay());
                        savedDailyForecast.setEveningTemp(item.getDailyForecasts().get(i).getTemp().getEve());
                        savedDailyForecast.setMorningTemp(item.getDailyForecasts().get(i).getTemp().getMorn());
                        savedDailyForecast.setNightTemp(item.getDailyForecasts().get(i).getTemp().getNight());
                        savedDailyForecast.setFeelslikeDay(item.getDailyForecasts().get(i).getFeelsLike().getDay());
                        savedDailyForecast.setFeelslikeEve(item.getDailyForecasts().get(i).getFeelsLike().getEve());
                        savedDailyForecast.setFeelslikeMorning(item.getDailyForecasts().get(i).getFeelsLike().getMorn());
                        savedDailyForecast.setFeelslikeNight(item.getDailyForecasts().get(i).getFeelsLike().getNight());
                        savedDailyForecast.setHumidity(item.getDailyForecasts().get(i).getHumidity());
                        savedDailyForecast.setWind(item.getDailyForecasts().get(i).getSpeed());
                        savedDailyForecast.setDescription(item.getDailyForecasts().get(i).getWeather().get(0).getDescription());
                        savedDailyForecast.setWeatherid(item.getDailyForecasts().get(i).getWeather().get(0).getId());
                        savedDailyForecast.setImageUrl(item.getDailyForecasts().get(i).getWeather().get(0).getIcon());
                        savedDailyForecasts.add(savedDailyForecast);
                    }
                    austinDao.insertAustinForecastList(savedDailyForecasts);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<SaveAustin> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<SaveAustin>> loadFromDb() {
                return austinDao.loadAustinForecast();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<WeatherForecast>> createCall() {
                return weatherService.getWeatherForecast(city, numDays, Constants.UNIT_SYSTEM, BuildConfig.WEATHER_API_KEY);
            }

            @Override
            protected void onFetchFailed() {

            }
        }.asLiveData();
    }
    public LiveData<Resource<List<SaveCurrent>>> fetchForecastCurrent(String city, String numDays) {
        return new NetworkBoundResource<List<SaveCurrent>, WeatherForecast>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull WeatherForecast item) {
                forecastDao.deleteNewsTable();
                if (item != null && item.getDailyForecasts() != null) {
                    List<SaveCurrent> savedDailyForecasts = new ArrayList<SaveCurrent>();
                    for (int i = 0; i < item.getDailyForecasts().size(); i++) {
                        SaveCurrent savedDailyForecast = new SaveCurrent();
                        savedDailyForecast.setLat(item.getCity().getCoord().getLat());
                        savedDailyForecast.setLon(item.getCity().getCoord().getLon());
                        savedDailyForecast.setDate(item.getDailyForecasts().get(i).getDt());
                        savedDailyForecast.setMaxTemp(item.getDailyForecasts().get(i).getTemp().getMax());
                        savedDailyForecast.setMinTemp(item.getDailyForecasts().get(i).getTemp().getMin());
                        savedDailyForecast.setDayTemp(item.getDailyForecasts().get(i).getTemp().getDay());
                        savedDailyForecast.setEveningTemp(item.getDailyForecasts().get(i).getTemp().getEve());
                        savedDailyForecast.setMorningTemp(item.getDailyForecasts().get(i).getTemp().getMorn());
                        savedDailyForecast.setNightTemp(item.getDailyForecasts().get(i).getTemp().getNight());
                        savedDailyForecast.setFeelslikeDay(item.getDailyForecasts().get(i).getFeelsLike().getDay());
                        savedDailyForecast.setFeelslikeEve(item.getDailyForecasts().get(i).getFeelsLike().getEve());
                        savedDailyForecast.setFeelslikeMorning(item.getDailyForecasts().get(i).getFeelsLike().getMorn());
                        savedDailyForecast.setFeelslikeNight(item.getDailyForecasts().get(i).getFeelsLike().getNight());
                        savedDailyForecast.setHumidity(item.getDailyForecasts().get(i).getHumidity());
                        savedDailyForecast.setWind(item.getDailyForecasts().get(i).getSpeed());
                        savedDailyForecast.setDescription(item.getDailyForecasts().get(i).getWeather().get(0).getDescription());
                        savedDailyForecast.setWeatherid(item.getDailyForecasts().get(i).getWeather().get(0).getId());
                        savedDailyForecast.setImageUrl(item.getDailyForecasts().get(i).getWeather().get(0).getIcon());
                        savedDailyForecasts.add(savedDailyForecast);
                    }
                    currentDao.insertCurrentForecastList(savedDailyForecasts);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<SaveCurrent> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<SaveCurrent>> loadFromDb() {
                return currentDao.loadCurrentForecast();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<WeatherForecast>> createCall() {
                return weatherService.getWeatherForecast(city, numDays, Constants.UNIT_SYSTEM, BuildConfig.WEATHER_API_KEY);
            }

            @Override
            protected void onFetchFailed() {

            }
        }.asLiveData();
    }
    public LiveData<Resource<UviDb>> fetchUvi(Double lat, Double lon) {
        return new NetworkBoundResource<UviDb, Uvi>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull Uvi item) {
                forecastDao.deleteUvi();
                if (item != null) {
                    UviDb uviDb = new UviDb();
                    uviDb.setLat(item.getLat());
                    uviDb.setLon(item.getLon());
                    uviDb.setValue(item.getValue());
                    forecastDao.insertUvi(uviDb);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable UviDb data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<UviDb> loadFromDb() {
                return forecastDao.loadUvi();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Uvi>> createCall() {
                return weatherService.getUvi(lat, lon, BuildConfig.WEATHER_API_KEY);
            }

            @Override
            protected void onFetchFailed() {

            }

        }.asLiveData();
    }
}
