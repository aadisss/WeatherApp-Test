package com.adeel.weatherapp.di;

import android.app.Application;

import androidx.room.Room;


import com.adeel.weatherapp.api.WeatherService;
import com.adeel.weatherapp.db.AustinDao;
import com.adeel.weatherapp.db.ChicagoDao;
import com.adeel.weatherapp.db.CurrentDao;
import com.adeel.weatherapp.db.ForecastDao;
import com.adeel.weatherapp.db.NewyorkDao;
import com.adeel.weatherapp.db.WeatherDB;
import com.adeel.weatherapp.util.Constants;
import com.adeel.weatherapp.util.LiveDataCallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
class AppModule {
    @Singleton
    @Provides
    WeatherService provideWeatherService() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(httpClient.build())
                .build();

        return retrofit.create(WeatherService.class);
    }

    @Singleton @Provides
    WeatherDB provideDb(Application app) {
        return Room.databaseBuilder(app, WeatherDB.class,"weather.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton @Provides
    ForecastDao provideForecastDao(WeatherDB db) {
        return db.forecastDao();
    }

    @Singleton @Provides
    NewyorkDao provideNewYorkDao(WeatherDB db) {
        return db.newyorkDao();
    }

    @Singleton @Provides
    ChicagoDao provideChicagoDao(WeatherDB db) {
        return db.chicagoDao();
    }

    @Singleton @Provides
    AustinDao provideAustinDao(WeatherDB db) {
        return db.austinDao();
    }

    @Singleton @Provides
    CurrentDao provideCurrentDao(WeatherDB db) {
        return db.currentDao();
    }

}
