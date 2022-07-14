package com.adeel.weatherapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.adeel.weatherapp.model.SaveAustin;
import com.adeel.weatherapp.model.UviDb;

import java.util.List;
@Dao
public interface AustinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAustinForecastList(List<SaveAustin> savedNewYorkForecasts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUvi(UviDb uviDb);

    @Query("SELECT * FROM saveaustin ORDER BY mdate ASC")
    LiveData<List<SaveAustin>> loadAustinForecast();

    @Query("SELECT * FROM uvidb ")
    LiveData<UviDb> loadUvi();

    @Query("DELETE FROM saveaustin")
    void deleteNewsTable();

    @Query("DELETE FROM uvidb")
    void deleteUvi();
}
