package com.adeel.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.adeel.weatherapp.di.Injectable;
import com.adeel.weatherapp.util.SharedPreferences;
import com.adeel.weatherapp.viewmodel.ForecastViewModel;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;

public class Home extends AppCompatActivity implements Injectable {
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    CardView crd1,crd2;
    TextView citye;
    private ForecastViewModel forecastViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        crd1 = (CardView)findViewById(R.id.card1);
        crd2 = (CardView)findViewById(R.id.card2);
        citye = (TextView)findViewById(R.id.cityName);
      //  fetchData("Karachi","9");
        crd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.getInstance(getApplicationContext()).putStringValue(SharedPreferences.CITY, "Karachi");
                SharedPreferences.getInstance(getApplicationContext()).putStringValue(SharedPreferences.NUM_DAYS, "7");

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        crd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.getInstance(getApplicationContext()).putStringValue(SharedPreferences.CITY, "Lahore");
                SharedPreferences.getInstance(getApplicationContext()).putStringValue(SharedPreferences.NUM_DAYS, "7");

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }


}