package com.google.firebase.udacity.weatherapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Calendar;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {


    final static String STRING_URL = "https://api.darksky" +
            ".net/forecast/4e8e2146bd7f344f963e89c356fc0ac8/";

    LocationManager locationManager;
    LocationListener locationListener;
    Location mLocation = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar calendar = Calendar.getInstance();
        String dayName = "";
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        day = (day + 2)%7;
        switch (day){
            case 2:
                dayName = "Monday";
                break;
            case 3:
                dayName = "Tuesday";
                break;
            case 4:
                dayName = "Wednesday";
                break;
            case 5:
                dayName = "Thursday";
                break;
            case 6:
                dayName = "Friday";
                break;
            case 7:
                dayName = "Sat";
                break;
            case 1:
                dayName = "Sunday";
                break;

        }
        TextView dayAfterTom = findViewById(R.id.day_after_tom);
        dayAfterTom.setText(dayName);
        showLocation();

    }

    private class WeatherTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpsURLConnection httpsURLConnection;
                httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.connect();

                InputStream inputStream = httpsURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer stringBuffer = new StringBuffer();

                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                return stringBuffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            TextView current_temp = findViewById(R.id.temp_currently);
            TextView today_temp = findViewById(R.id.temp_today);
            TextView tomorrow_temp = findViewById(R.id.temp_tomorrow);
            TextView dat_temp = findViewById(R.id.temp_dat);
            DecimalFormat df2 = new DecimalFormat("##");
            DecimalFormat decimalFormat = new DecimalFormat(".#");
            if (s == null){
                Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
            else {
                try {
                    JSONObject root = new JSONObject(s);
                    JSONObject currently = root.getJSONObject("currently");
                    Double temp_current = currently.getDouble("temperature");
                    current_temp.setText(decimalFormat.format(f2c(temp_current)) + "\u2103");

                    String icon = currently.getString("icon");
                    String replacedIcon = icon.replace("-","_");
                    TextView currently_Textview = findViewById(R.id.currently);
                    currently_Textview.setText(icon);
                    Toast.makeText(MainActivity.this, icon, Toast.LENGTH_SHORT).show();
                    String uri = "@drawable/" + replacedIcon;
                    int imageResource = getResources().getIdentifier(uri,null,getPackageName());
                    ImageView imageView = findViewById(R.id.weather_image);
                    Drawable res = getResources().getDrawable(imageResource);
                    imageView.setImageDrawable(res);

                    JSONObject daily = root.getJSONObject("daily");
                    JSONArray data = daily.getJSONArray("data");
                    JSONObject today = data.getJSONObject(1);
                    JSONObject tomorrow = data.getJSONObject(2);
                    JSONObject dayAfterTomorrow = data.getJSONObject(3);

                    Double tempHigh_today = today.getDouble("temperatureHigh");
                    Double tempLow_today = today.getDouble("temperatureLow");
                    today_temp.setText(df2.format(f2c(tempHigh_today)));
                    today_temp.append("/" + df2.format(f2c(tempLow_today)) + "\u2103");

                    Double tempHigh_tomorrow = tomorrow.getDouble("temperatureHigh");
                    Double tempLow_tomorrow = tomorrow.getDouble("temperatureLow");
                    dat_temp.setText(df2.format(f2c(tempHigh_tomorrow)));
                    dat_temp.append("/" + df2.format(f2c(tempLow_tomorrow)) + "\u2103");

                    Double tempHigh_dat = dayAfterTomorrow.getDouble("temperatureHigh");
                    Double tempLow_dat = dayAfterTomorrow.getDouble("temperatureLow");
                    tomorrow_temp.setText(df2.format(f2c(tempHigh_dat)));
                    tomorrow_temp.append("/" + df2.format(f2c(tempLow_dat)) + "\u2103");

                    Double windSpeed = today.getDouble("windSpeed");
                    TextView windSpeedTextView = findViewById(R.id.windSpeed);
                    windSpeedTextView.setText(decimalFormat.format(windSpeed) + "Km/h");

                    Double humidity = today.getDouble("humidity");
                    TextView humidityTextView = findViewById(R.id.humidity);
                    humidityTextView.setText(decimalFormat.format(humidity));

                    Double uvIndex = today.getDouble("uvIndex");
                    TextView uvIndexTextView = findViewById(R.id.uvIndex);
                    uvIndexTextView.setText(decimalFormat.format(uvIndex));

                    Double pressure = today.getDouble("pressure");
                    TextView pressureTextView = findViewById(R.id.pressure);
                    pressureTextView.setText(decimalFormat.format(pressure) + "mb");




                } catch (JSONException e) {
                    e.printStackTrace();
                    current_temp.setText("N/A");
                }
            }

        }
    }

    Double f2c(Double temp) {
        return (5.0 / 9.0) * (temp - 32);
    }

    String createUrlString(Location l) {
        String query = null;
        if (mLocation != null) {
            query = STRING_URL + l.getLatitude() + "," + l.getLongitude();
            Toast.makeText(MainActivity.this, "Url is ready", Toast.LENGTH_SHORT).show();
        }
        return query;
    }

    void taskExecute(String query) {
        if (query != null) {
            new WeatherTask().execute(query);
        } else {
            Toast.makeText(MainActivity.this, "queryUrl is null", Toast.LENGTH_SHORT)
                    .show();
        }
    }
    void showLocation(){

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLocation = new Location(location);
                Toast.makeText(MainActivity.this, "Location Set", Toast.LENGTH_SHORT)
                        .show();
                locationManager.removeUpdates(locationListener);
                taskExecute(createUrlString(mLocation));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent settingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(settingIntent);
            }
        };
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission
                .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(MainActivity.this, Manifest.permission
                        .ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
            }
            return;
        }
        locationManager.requestLocationUpdates("gps", 0, 100,
                locationListener);
    }
}