package com.adeel.weatherapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.adeel.weatherapp.model.SaveNewYork;
import com.adeel.weatherapp.model.UviDb;

import java.util.List;
@Dao
public interface NewyorkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNewYorkForecastList(List<SaveNewYork> savedNewYorkForecasts);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUvi(UviDb uviDb);

    @Query("SELECT * FROM savenewyork ORDER BY mdate ASC")
    LiveData<List<SaveNewYork>> loadNewYorkForecast();

    @Query("SELECT * FROM uvidb ")
    LiveData<UviDb> loadUvi();

    @Query("DELETE FROM savenewyork")
    void deleteNewsTable();

    @Query("DELETE FROM uvidb")
    void deleteUvi();
}
