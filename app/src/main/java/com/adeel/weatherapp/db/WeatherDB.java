package com.adeel.weatherapp.db;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.adeel.weatherapp.model.SaveAustin;
import com.adeel.weatherapp.model.SaveChicago;
import com.adeel.weatherapp.model.SaveCurrent;
import com.adeel.weatherapp.model.SaveNewYork;
import com.adeel.weatherapp.model.SavedDailyForecast;
import com.adeel.weatherapp.model.UviDb;


@Database(entities = {SavedDailyForecast.class, SaveNewYork.class, SaveChicago.class,
        SaveAustin.class, SaveCurrent.class, UviDb.class},
        version = 1,
        exportSchema = false)

public abstract class WeatherDB extends RoomDatabase {
    abstract public ForecastDao forecastDao();
    abstract public NewyorkDao newyorkDao();
    abstract public ChicagoDao chicagoDao();
    abstract public AustinDao austinDao();
    abstract public CurrentDao currentDao();
}
