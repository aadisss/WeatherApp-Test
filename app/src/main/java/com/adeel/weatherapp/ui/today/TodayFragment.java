package com.adeel.weatherapp.ui.today;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adeel.weatherapp.DetailActivity;
import com.adeel.weatherapp.R;
import com.adeel.weatherapp.di.Injectable;
import com.adeel.weatherapp.model.SaveAustin;
import com.adeel.weatherapp.model.SaveChicago;
import com.adeel.weatherapp.model.SaveCurrent;
import com.adeel.weatherapp.model.SaveNewYork;
import com.adeel.weatherapp.model.SavedDailyForecast;
import com.adeel.weatherapp.model.UviDb;
import com.adeel.weatherapp.util.StaticMembers;
import com.adeel.weatherapp.util.Utility;
import com.adeel.weatherapp.viewmodel.ForecastViewModel;
import com.adeel.weatherapp.viewmodel.UviViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodayFragment extends Fragment implements Injectable {
    private ForecastViewModel forecastViewModel;
    private UviViewModel uviViewModel;
    FusedLocationProviderClient client;
    ArrayList <Item> itemList;
    ItemArrayAdapter itemArrayAdapter;
    @BindView(R.id.city) TextView mcity;
    @BindView(R.id.date) TextView date;
    @BindView(R.id.condition) TextView condition;
    @BindView(R.id.weather_resource) ImageView weather_resource;
    @BindView(R.id.temp_condition) TextView temp_condition;
    @BindView(R.id.temperature) TextView temperature;
    @BindView(R.id.humidity_value) TextView humidity_value;
    @BindView(R.id.wind_value) TextView wind_value;
    @BindView(R.id.uv_value) TextView uv_value;
    @BindView(R.id.cNewyork) TextView cNewyork;
    @BindView(R.id.cChicago) TextView cChicago;
    @BindView(R.id.cBoston) TextView cBoston;
    @BindView(R.id.cAustin) TextView cAustin;
    @BindView(R.id.cCurrent) TextView cCurrent;
    @BindView(R.id.item_list) RecyclerView rv;
    @BindView(R.id.cardCurrent) CardView crdCurrent;
    @BindView(R.id.cardAustin) CardView crdAustin;
    @BindView(R.id.card1) CardView crdNewYork;
    @BindView(R.id.card2) CardView crdCChicago;
    @BindView(R.id.cardBoston) CardView crdBoston;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public static TodayFragment create() {
        TodayFragment todayFragment = new TodayFragment();
        return todayFragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Initialize location client
        client = LocationServices
                .getFusedLocationProviderClient(
                        getActivity());
        // check condition
        if (ContextCompat.checkSelfPermission(
                getActivity(),
                Manifest.permission
                        .ACCESS_FINE_LOCATION)
                == PackageManager
                .PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                getActivity(),
                Manifest.permission
                        .ACCESS_COARSE_LOCATION)
                == PackageManager
                .PERMISSION_GRANTED) {
            // When permission is granted
            // Call method
            getCurrentLocation();
        }
        else {
            // When permission is not granted
            // Call method
            requestPermissions(
                    new String[] {
                            Manifest.permission
                                    .ACCESS_FINE_LOCATION,
                            Manifest.permission
                                    .ACCESS_COARSE_LOCATION },
                    100);
        }

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, root);
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(),R.color.colorPrimaryToday));
        itemList = new ArrayList<Item>();
         itemArrayAdapter = new ItemArrayAdapter(R.layout.list_item, itemList);
        ArrayList<String> cities = new ArrayList<>();
        cities.add("New York");
        cities.add("Chicago");
        cities.add("San Francisco");
        cities.add("Boston");
        cities.add("Seattle");
        cities.add("Houston");
        cities.add("Austin");
        cities.add("Portland");
        cities.add("Dallas");





       fetchData2("Boston","7");
        fetchData("New York","7");
        fetchDataChicago("Chicago","7");
        fetchDataAustin("Austin","7");





        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(itemArrayAdapter);

        // Populating list items
//        for(int i=0; i<100; i++) {
//            itemList.add(new Item("Item " + i));
//        }
        crdCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticMembers.clicked = "current";
                Intent i = new Intent(getActivity(), DetailActivity.class);
                startActivity(i);
            }
        });
        crdNewYork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticMembers.clicked = "n";
                Intent i = new Intent(getActivity(), DetailActivity.class);
                startActivity(i);
            }
        });
        crdCChicago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticMembers.clicked = "g";
                Intent i = new Intent(getActivity(), DetailActivity.class);
                startActivity(i);
            }
        });
        crdBoston.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticMembers.clicked = "b";
                Intent i = new Intent(getActivity(), DetailActivity.class);
                startActivity(i);
            }
        });
        crdAustin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticMembers.clicked = "a";
                Intent i = new Intent(getActivity(), DetailActivity.class);
                startActivity(i);
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    private void fetchData2(String cityt,String numDayss) {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        String city = cityt;
        String numDays = numDayss;
        Log.e("City is ",city);

        forecastViewModel = new ViewModelProvider(this, viewModelFactory).get(ForecastViewModel.class);
        LiveData d=  forecastViewModel.fetchResults(city,numDays);

        forecastViewModel.fetchResults(city, numDays).observe(getViewLifecycleOwner(), result -> {
            List<SavedDailyForecast> dailyForecasts = result.data;

            mcity.setText(Utility.toTitleCase(city));
            if (dailyForecasts != null && dailyForecasts.size() > 0) {

                //fetchUvi(dailyForecasts.get(0).getLat(), dailyForecasts.get(0).getLon());

                weather_resource.setImageResource(Utility.getArtResourceForWeatherCondition(dailyForecasts.get(0).getWeatherid()));
                condition.setText(Utility.toTitleCase(dailyForecasts.get(0).getDescription()));
                date.setText(String.format("%s, %s", Utility.format(dailyForecasts.get(0).getDate()), Utility.formatDate(dailyForecasts.get(0).getDate())));
                humidity_value.setText(dailyForecasts.get(0).getHumidity() + "%");
                wind_value.setText(Utility.getFormattedWind(getContext(), Float.parseFloat(String.valueOf(dailyForecasts.get(0).getWind()))));
                Log.e("Temp is",dailyForecasts.get(0).getDescription()+" "+city);
                //SharedPreferences.getInstance(getContext()).putStringValue(SharedPreferences.DESC, dailyForecasts.get(0).getDescription());
                if(timeOfDay >= 0 && timeOfDay < 12){
                    String item = city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%";
                    itemList.add(new Item(item));

                        cBoston.setText(city + " " + "Temp " + Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()) + " " + "Humidity " + dailyForecasts.get(0).getHumidity() + "%");

                    temp_condition.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()));
                    //SharedPreferences.getInstance(getContext()).putStringValue(SharedPreferences.TEMP, Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()));
                    temperature.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getFeelslikeMorning()));
                }else if(timeOfDay >= 12 && timeOfDay < 16){
                    String item = city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getDayTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%";
                    itemList.add(new Item(item));
                    cBoston.setText(city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getDayTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%");

                    temp_condition.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getDayTemp()));
                    temperature.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getFeelslikeDay()));
                    // SharedPreferences.getInstance(getContext()).putStringValue(SharedPreferences.TEMP, Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()));
                }else if(timeOfDay >= 16 && timeOfDay < 21){
                    String item = city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getEveningTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%";
                    itemList.add(new Item(item));
                    cBoston.setText(city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getEveningTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%");
                    temp_condition.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getEveningTemp()));
                    temperature.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getEveningTemp()));
                    //SharedPreferences.getInstance(getContext()).putStringValue(SharedPreferences.TEMP, Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()));
                }else if(timeOfDay >= 21 && timeOfDay < 24){
                    String item = city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getNightTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%";
                    itemList.add(new Item(item));
                    cBoston.setText(city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getNightTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%");
                    temp_condition.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getNightTemp()));
                    temperature.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getFeelslikeNight()));
                   // SharedPreferences.getInstance(getContext()).putStringValue(SharedPreferences.TEMP, Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()));
                }
                StaticMembers.Bcondition = Utility.toTitleCase(dailyForecasts.get(0).getDescription());
                StaticMembers.Bdate = String.format("%s, %s", Utility.format(dailyForecasts.get(0).getDate()), Utility.formatDate(dailyForecasts.get(0).getDate()));
                StaticMembers.Bcity = city;
                StaticMembers.Btemperature = cBoston.getText().toString();
                StaticMembers.BconditionImage = Utility.getArtResourceForWeatherCondition(dailyForecasts.get(0).getWeatherid());
            }
           // itemArrayAdapter.notifyDataSetChanged();
        });
    }
    private void fetchData(String cityt,String numDayss) {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        String city = cityt;
        String numDays = numDayss;
        Log.e("City is ",city);

        forecastViewModel = new ViewModelProvider(this, viewModelFactory).get(ForecastViewModel.class);
        forecastViewModel.fetchResultsNewYork(city, numDays).observe(getViewLifecycleOwner(), result -> {
            List<SaveNewYork> dailyForecasts = result.data;
            mcity.setText(Utility.toTitleCase(city));
            if (dailyForecasts != null && dailyForecasts.size() > 0) {

                //fetchUvi(dailyForecasts.get(0).getLat(), dailyForecasts.get(0).getLon());

                weather_resource.setImageResource(Utility.getArtResourceForWeatherCondition(dailyForecasts.get(0).getWeatherid()));
                condition.setText(Utility.toTitleCase(dailyForecasts.get(0).getDescription()));
                date.setText(String.format("%s, %s", Utility.format(dailyForecasts.get(0).getDate()), Utility.formatDate(dailyForecasts.get(0).getDate())));
                humidity_value.setText(dailyForecasts.get(0).getHumidity() + "%");
                wind_value.setText(Utility.getFormattedWind(getContext(), Float.parseFloat(String.valueOf(dailyForecasts.get(0).getWind()))));
                Log.e("Temp is",dailyForecasts.get(0).getDescription()+" "+city);
             //   SharedPreferences.getInstance(getContext()).putStringValue(SharedPreferences.DESC, dailyForecasts.get(0).getDescription());
                if(timeOfDay >= 0 && timeOfDay < 12) {
                    String item = city + " " + "Temp " + Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()) + " " + "Humidity " + dailyForecasts.get(0).getHumidity() + "%";
                    itemList.add(new Item(item));

                        cNewyork.setText(city + " " + "Temp " + Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()) + " " + "Humidity " + dailyForecasts.get(0).getHumidity() + "%");

                    temp_condition.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()));
                    //SharedPreferences.getInstance(getContext()).putStringValue(SharedPreferences.TEMP, Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()));
                    temperature.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getFeelslikeMorning()));
                }else if(timeOfDay >= 12 && timeOfDay < 16){
                    String item = city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getDayTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%";
                    itemList.add(new Item(item));
                    cNewyork.setText(city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getDayTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%");

                    temp_condition.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getDayTemp()));
                    temperature.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getFeelslikeDay()));
                   // SharedPreferences.getInstance(getContext()).putStringValue(SharedPreferences.TEMP, Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()));
                }else if(timeOfDay >= 16 && timeOfDay < 21){
                    String item = city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getEveningTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%";
                    itemList.add(new Item(item));
                    cNewyork.setText(city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getEveningTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%");
                    temp_condition.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getEveningTemp()));
                    temperature.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getEveningTemp()));
                    //SharedPreferences.getInstance(getContext()).putStringValue(SharedPreferences.TEMP, Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()));
                }else if(timeOfDay >= 21 && timeOfDay < 24){
                    String item = city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getNightTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%";
                    itemList.add(new Item(item));
                    cNewyork.setText(city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getNightTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%");
                    temp_condition.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getNightTemp()));
                    temperature.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getFeelslikeNight()));
                  //  SharedPreferences.getInstance(getContext()).putStringValue(SharedPreferences.TEMP, Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()));
                }
                StaticMembers.Ncondition = Utility.toTitleCase(dailyForecasts.get(0).getDescription());
                StaticMembers.Ndate = String.format("%s, %s", Utility.format(dailyForecasts.get(0).getDate()), Utility.formatDate(dailyForecasts.get(0).getDate()));
                StaticMembers.Ncity = city;
                StaticMembers.Ntemperature = cNewyork.getText().toString();
                StaticMembers.NconditionImage = Utility.getArtResourceForWeatherCondition(dailyForecasts.get(0).getWeatherid());
            }

           // itemArrayAdapter.notifyDataSetChanged();

        });

    }

    private void fetchDataChicago(String cityt,String numDayss) {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        String city = cityt;
        String numDays = numDayss;
        Log.e("City is ",city);

        forecastViewModel = new ViewModelProvider(this, viewModelFactory).get(ForecastViewModel.class);
        forecastViewModel.fetchResultsChicago(city, numDays).observe(getViewLifecycleOwner(), result -> {
            List<SaveChicago> dailyForecasts = result.data;
            mcity.setText(Utility.toTitleCase(city));
            if (dailyForecasts != null && dailyForecasts.size() > 0) {

                //fetchUvi(dailyForecasts.get(0).getLat(), dailyForecasts.get(0).getLon());

                weather_resource.setImageResource(Utility.getArtResourceForWeatherCondition(dailyForecasts.get(0).getWeatherid()));
                condition.setText(Utility.toTitleCase(dailyForecasts.get(0).getDescription()));
                date.setText(String.format("%s, %s", Utility.format(dailyForecasts.get(0).getDate()), Utility.formatDate(dailyForecasts.get(0).getDate())));
                humidity_value.setText(dailyForecasts.get(0).getHumidity() + "%");
                wind_value.setText(Utility.getFormattedWind(getContext(), Float.parseFloat(String.valueOf(dailyForecasts.get(0).getWind()))));
                Log.e("Temp is",dailyForecasts.get(0).getDescription()+" "+city);
                //   SharedPreferences.getInstance(getContext()).putStringValue(SharedPreferences.DESC, dailyForecasts.get(0).getDescription());
                if(timeOfDay >= 0 && timeOfDay < 12) {
                    String item = city + " " + "Temp " + Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()) + " " + "Humidity " + dailyForecasts.get(0).getHumidity() + "%";
                    itemList.add(new Item(item));

                    cChicago.setText(city + " " + "Temp " + Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()) + " " + "Humidity " + dailyForecasts.get(0).getHumidity() + "%");

                    temp_condition.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()));
                    //SharedPreferences.getInstance(getContext()).putStringValue(SharedPreferences.TEMP, Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()));
                    temperature.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getFeelslikeMorning()));
                }else if(timeOfDay >= 12 && timeOfDay < 16){
                    String item = city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getDayTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%";
                    itemList.add(new Item(item));
                    cChicago.setText(city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getDayTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%");

                    temp_condition.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getDayTemp()));
                    temperature.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getFeelslikeDay()));
                    // SharedPreferences.getInstance(getContext()).putStringValue(SharedPreferences.TEMP, Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()));
                }else if(timeOfDay >= 16 && timeOfDay < 21){
                    String item = city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getEveningTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%";
                    itemList.add(new Item(item));
                    cChicago.setText(city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getEveningTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%");
                    temp_condition.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getEveningTemp()));
                    temperature.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getEveningTemp()));
                    //SharedPreferences.getInstance(getContext()).putStringValue(SharedPreferences.TEMP, Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()));
                }else if(timeOfDay >= 21 && timeOfDay < 24){
                    String item = city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getNightTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%";
                    itemList.add(new Item(item));
                    cChicago.setText(city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getNightTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%");
                    temp_condition.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getNightTemp()));
                    temperature.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getFeelslikeNight()));
                    //  SharedPreferences.getInstance(getContext()).putStringValue(SharedPreferences.TEMP, Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()));
                }
                StaticMembers.Gcondition = Utility.toTitleCase(dailyForecasts.get(0).getDescription());
                StaticMembers.Gdate = String.format("%s, %s", Utility.format(dailyForecasts.get(0).getDate()), Utility.formatDate(dailyForecasts.get(0).getDate()));
                StaticMembers.Gcity = city;
                StaticMembers.Gtemperature = cChicago.getText().toString();
                StaticMembers.GconditionImage = Utility.getArtResourceForWeatherCondition(dailyForecasts.get(0).getWeatherid());
            }

            // itemArrayAdapter.notifyDataSetChanged();

        });

    }
    private void fetchDataAustin(String cityt,String numDayss) {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        String city = cityt;
        String numDays = numDayss;
        Log.e("City is ",city);

        forecastViewModel = new ViewModelProvider(this, viewModelFactory).get(ForecastViewModel.class);
        forecastViewModel.fetchResultsAustin(city, numDays).observe(getViewLifecycleOwner(), result -> {
            List<SaveAustin> dailyForecasts = result.data;
            mcity.setText(Utility.toTitleCase(city));
            if (dailyForecasts != null && dailyForecasts.size() > 0) {

                //fetchUvi(dailyForecasts.get(0).getLat(), dailyForecasts.get(0).getLon());

                weather_resource.setImageResource(Utility.getArtResourceForWeatherCondition(dailyForecasts.get(0).getWeatherid()));
                condition.setText(Utility.toTitleCase(dailyForecasts.get(0).getDescription()));
                date.setText(String.format("%s, %s", Utility.format(dailyForecasts.get(0).getDate()), Utility.formatDate(dailyForecasts.get(0).getDate())));
                humidity_value.setText(dailyForecasts.get(0).getHumidity() + "%");
                wind_value.setText(Utility.getFormattedWind(getContext(), Float.parseFloat(String.valueOf(dailyForecasts.get(0).getWind()))));
                Log.e("Temp is",dailyForecasts.get(0).getDescription()+" "+city);
                //   SharedPreferences.getInstance(getContext()).putStringValue(SharedPreferences.DESC, dailyForecasts.get(0).getDescription());
                if(timeOfDay >= 0 && timeOfDay < 12) {
                    String item = city + " " + "Temp " + Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()) + " " + "Humidity " + dailyForecasts.get(0).getHumidity() + "%";
                    itemList.add(new Item(item));

                    cAustin.setText(city + " " + "Temp " + Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()) + " " + "Humidity " + dailyForecasts.get(0).getHumidity() + "%");

                    temp_condition.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()));
                    //SharedPreferences.getInstance(getContext()).putStringValue(SharedPreferences.TEMP, Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()));
                    temperature.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getFeelslikeMorning()));
                }else if(timeOfDay >= 12 && timeOfDay < 16){
                    String item = city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getDayTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%";
                    itemList.add(new Item(item));
                    cAustin.setText(city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getDayTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%");

                    temp_condition.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getDayTemp()));
                    temperature.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getFeelslikeDay()));
                    // SharedPreferences.getInstance(getContext()).putStringValue(SharedPreferences.TEMP, Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()));
                }else if(timeOfDay >= 16 && timeOfDay < 21){
                    String item = city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getEveningTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%";
                    itemList.add(new Item(item));
                    cAustin.setText(city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getEveningTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%");
                    temp_condition.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getEveningTemp()));
                    temperature.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getEveningTemp()));
                    //SharedPreferences.getInstance(getContext()).putStringValue(SharedPreferences.TEMP, Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()));
                }else if(timeOfDay >= 21 && timeOfDay < 24){
                    String item = city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getNightTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%";
                    itemList.add(new Item(item));
                    cAustin.setText(city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getNightTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%");
                    temp_condition.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getNightTemp()));
                    temperature.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getFeelslikeNight()));
                    //  SharedPreferences.getInstance(getContext()).putStringValue(SharedPreferences.TEMP, Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()));
                }
                StaticMembers.Acondition = Utility.toTitleCase(dailyForecasts.get(0).getDescription());
                StaticMembers.Adate = String.format("%s, %s", Utility.format(dailyForecasts.get(0).getDate()), Utility.formatDate(dailyForecasts.get(0).getDate()));
                StaticMembers.Acity = city;
                StaticMembers.Atemperature = cAustin.getText().toString();
                StaticMembers.AconditionImage = Utility.getArtResourceForWeatherCondition(dailyForecasts.get(0).getWeatherid());
            }

            // itemArrayAdapter.notifyDataSetChanged();

        });

    }

    private void fetchDataCurrent(String cityt,String numDayss) {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        String city = cityt;
        String numDays = numDayss;
        Log.e("City is ",city);

        forecastViewModel = new ViewModelProvider(this, viewModelFactory).get(ForecastViewModel.class);
        forecastViewModel.fetchResultsCurrent(city, numDays).observe(getViewLifecycleOwner(), result -> {
            List<SaveCurrent> dailyForecasts = result.data;
            mcity.setText(Utility.toTitleCase(city));
            if (dailyForecasts != null && dailyForecasts.size() > 0) {

                //fetchUvi(dailyForecasts.get(0).getLat(), dailyForecasts.get(0).getLon());

                weather_resource.setImageResource(Utility.getArtResourceForWeatherCondition(dailyForecasts.get(0).getWeatherid()));
                condition.setText(Utility.toTitleCase(dailyForecasts.get(0).getDescription()));
                date.setText(String.format("%s, %s", Utility.format(dailyForecasts.get(0).getDate()), Utility.formatDate(dailyForecasts.get(0).getDate())));
                humidity_value.setText(dailyForecasts.get(0).getHumidity() + "%");
                wind_value.setText(Utility.getFormattedWind(getContext(), Float.parseFloat(String.valueOf(dailyForecasts.get(0).getWind()))));
                Log.e("Temp is",dailyForecasts.get(0).getDescription()+" "+city);
                //   SharedPreferences.getInstance(getContext()).putStringValue(SharedPreferences.DESC, dailyForecasts.get(0).getDescription());
                if(timeOfDay >= 0 && timeOfDay < 12) {
                    String item = city + " " + "Temp " + Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()) + " " + "Humidity " + dailyForecasts.get(0).getHumidity() + "%";
                    itemList.add(new Item(item));

                    cCurrent.setText(city + " " + "Temp " + Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()) + " " + "Humidity " + dailyForecasts.get(0).getHumidity() + "%");

                    temp_condition.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()));
                    //SharedPreferences.getInstance(getContext()).putStringValue(SharedPreferences.TEMP, Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()));
                    temperature.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getFeelslikeMorning()));
                }else if(timeOfDay >= 12 && timeOfDay < 16){
                    String item = city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getDayTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%";
                    itemList.add(new Item(item));
                    cCurrent.setText(city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getDayTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%");

                    temp_condition.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getDayTemp()));
                    temperature.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getFeelslikeDay()));
                    // SharedPreferences.getInstance(getContext()).putStringValue(SharedPreferences.TEMP, Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()));
                }else if(timeOfDay >= 16 && timeOfDay < 21){
                    String item = city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getEveningTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%";
                    itemList.add(new Item(item));
                    cCurrent.setText(city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getEveningTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%");
                    temp_condition.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getEveningTemp()));
                    temperature.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getEveningTemp()));
                    //SharedPreferences.getInstance(getContext()).putStringValue(SharedPreferences.TEMP, Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()));
                }else if(timeOfDay >= 21 && timeOfDay < 24){
                    String item = city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getNightTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%";
                    itemList.add(new Item(item));
                    cCurrent.setText(city+" "+"Temp "+Utility.formatTemperature(getContext(), dailyForecasts.get(0).getNightTemp())+" "+"Humidity "+dailyForecasts.get(0).getHumidity() + "%");
                    temp_condition.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getNightTemp()));
                    temperature.setText(Utility.formatTemperature(getContext(), dailyForecasts.get(0).getFeelslikeNight()));
                    //  SharedPreferences.getInstance(getContext()).putStringValue(SharedPreferences.TEMP, Utility.formatTemperature(getContext(), dailyForecasts.get(0).getMorningTemp()));
                }

                StaticMembers.Ccondition = Utility.toTitleCase(dailyForecasts.get(0).getDescription());
                StaticMembers.Cdate = String.format("%s, %s", Utility.format(dailyForecasts.get(0).getDate()), Utility.formatDate(dailyForecasts.get(0).getDate()));
                StaticMembers.Ccity = city;
                StaticMembers.Ctemperature = cCurrent.getText().toString();
                StaticMembers.CconditionImage = Utility.getArtResourceForWeatherCondition(dailyForecasts.get(0).getWeatherid());

            }

            // itemArrayAdapter.notifyDataSetChanged();

        });

    }

    private void fetchUvi(Double lat, Double lon) {
        forecastViewModel.fetchUvi(lat, lon).observe(getViewLifecycleOwner(), result -> {
            UviDb uviDb = result.data;
            if (uviDb !=null) {
                uv_value.setText(uviDb.getValue() + "");
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
        // Check condition
        if (requestCode == 100 && (grantResults.length > 0)
                && (grantResults[0] + grantResults[1]
                == PackageManager.PERMISSION_GRANTED)) {
            // When permission are granted
            // Call  method
            getCurrentLocation();
        }
        else {
            // When permission are denied
            // Display toast
            Toast
                    .makeText(getActivity(),
                            "Permission denied",
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }
    @SuppressLint("MissingPermission")
    private void getCurrentLocation()
    {
        // Initialize Location manager
        LocationManager locationManager
                = (LocationManager)getActivity()
                .getSystemService(
                        Context.LOCATION_SERVICE);
        // Check condition
        if (locationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)) {
            // When location service is enabled
            // Get last location
            client.getLastLocation().addOnCompleteListener(
                    new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(
                                @NonNull Task<Location> task)
                        {

                            // Initialize location
                            Location location
                                    = task.getResult();
                            // Check condition
                            if (location != null) {
                                // When location result is not
                                // null set latitude
                                Log.e("Lati ",String.valueOf(location.getLatitude()));
                                Log.e("long ",String.valueOf(location.getLongitude()));
                                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                String cityName = addresses.get(0).getLocality();
                                String stateName = addresses.get(0).getAddressLine(1);
                                String countryName = addresses.get(0).getAddressLine(2);
                                for(int i=0;i<addresses.size();i++){
                                    Log.e("City name ",addresses.get(i).getAddressLine(0));
                                }
                                try {
                                    Log.e("local name ",cityName);
                                    fetchDataCurrent(cityName,"7");
                                }
                                catch (Exception ex){}



                            }
                            else {
                                // When location result is null
                                // initialize location request
                                LocationRequest locationRequest
                                        = new LocationRequest()
                                        .setPriority(
                                                LocationRequest
                                                        .PRIORITY_HIGH_ACCURACY)
                                        .setInterval(10000)
                                        .setFastestInterval(
                                                1000)
                                        .setNumUpdates(1);

                                // Initialize location call back
                                LocationCallback
                                        locationCallback
                                        = new LocationCallback() {
                                    @Override
                                    public void
                                    onLocationResult(
                                            LocationResult
                                                    locationResult)
                                    {
                                        // Initialize
                                        // location
                                        Location location1
                                                = locationResult
                                                .getLastLocation();


                                    }
                                };

                                // Request location updates
                                client.requestLocationUpdates(
                                        locationRequest,
                                        locationCallback,
                                        Looper.myLooper());
                            }
                        }
                    });
        }
        else {
            // When location service is not enabled
            // open location setting
            startActivity(
                    new Intent(
                            Settings
                                    .ACTION_LOCATION_SOURCE_SETTINGS)
                            .setFlags(
                                    Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}


