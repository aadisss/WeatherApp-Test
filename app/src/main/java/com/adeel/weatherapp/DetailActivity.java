package com.adeel.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.adeel.weatherapp.util.StaticMembers;

public class DetailActivity extends AppCompatActivity {
    TextView txtCity,txtDate,txtConddition,txtTemp;
    ImageView weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        txtCity = (TextView) findViewById(R.id.city);
        txtDate = (TextView) findViewById(R.id.date);
        txtConddition = (TextView) findViewById(R.id.condition);
        txtTemp = (TextView) findViewById(R.id.temp_condition);
        weather = (ImageView) findViewById(R.id.weather_resource);

        if(StaticMembers.clicked.equals("current")) {
            txtCity.setText(StaticMembers.Ccity);
            txtDate.setText(StaticMembers.Cdate);
            txtConddition.setText(StaticMembers.Ccondition);
            txtTemp.setText(StaticMembers.Ctemperature);
            weather.setImageResource(StaticMembers.CconditionImage);
        }
        else if(StaticMembers.clicked.equals("n")) {
            txtCity.setText(StaticMembers.Ncity);
            txtDate.setText(StaticMembers.Ndate);
            txtConddition.setText(StaticMembers.Ncondition);
            txtTemp.setText(StaticMembers.Ntemperature);
            weather.setImageResource(StaticMembers.NconditionImage);
        }
        else if(StaticMembers.clicked.equals("g")) {
            txtCity.setText(StaticMembers.Gcity);
            txtDate.setText(StaticMembers.Gdate);
            txtConddition.setText(StaticMembers.Gcondition);
            txtTemp.setText(StaticMembers.Gtemperature);
            weather.setImageResource(StaticMembers.GconditionImage);
        }
        else if(StaticMembers.clicked.equals("b")) {
            txtCity.setText(StaticMembers.Bcity);
            txtDate.setText(StaticMembers.Bdate);
            txtConddition.setText(StaticMembers.Bcondition);
            txtTemp.setText(StaticMembers.Btemperature);
            weather.setImageResource(StaticMembers.BconditionImage);
        }
        else if(StaticMembers.clicked.equals("a")) {
            txtCity.setText(StaticMembers.Acity);
            txtDate.setText(StaticMembers.Adate);
            txtConddition.setText(StaticMembers.Acondition);
            txtTemp.setText(StaticMembers.Atemperature);
            weather.setImageResource(StaticMembers.AconditionImage);
        }
    }
}