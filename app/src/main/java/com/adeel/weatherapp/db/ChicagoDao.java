package com.adeel.weatherapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.adeel.weatherapp.model.SaveChicago;
import com.adeel.weatherapp.model.UviDb;

import java.util.List;
@Dao
public interface ChicagoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChicagoForecastList(List<SaveChicago> savedNewYorkForecasts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUvi(UviDb uviDb);

    @Query("SELECT * FROM savechicago ORDER BY mdate ASC")
    LiveData<List<SaveChicago>> loadChicagoForecast();

    @Query("SELECT * FROM uvidb ")
    LiveData<UviDb> loadUvi();

    @Query("DELETE FROM savechicago")
    void deleteNewsTable();

    @Query("DELETE FROM uvidb")
    void deleteUvi();
}
