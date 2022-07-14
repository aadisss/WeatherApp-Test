package com.adeel.weatherapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.adeel.weatherapp.model.SaveCurrent;
import com.adeel.weatherapp.model.UviDb;

import java.util.List;
@Dao
public interface CurrentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCurrentForecastList(List<SaveCurrent> savedNewYorkForecasts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUvi(UviDb uviDb);

    @Query("SELECT * FROM savecurrent ORDER BY mdate ASC")
    LiveData<List<SaveCurrent>> loadCurrentForecast();

    @Query("SELECT * FROM uvidb ")
    LiveData<UviDb> loadUvi();

    @Query("DELETE FROM savecurrent")
    void deleteNewsTable();

    @Query("DELETE FROM uvidb")
    void deleteUvi();
}
