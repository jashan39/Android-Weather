package com.example.weather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import static android.R.attr.data;
import static android.R.attr.dateTextAppearance;
import static com.example.weather.R.id.text;
import static com.example.weather.R.id.textView;

public class MainActivity extends AppCompatActivity {

    JSONObject data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                getJSON("Houston");
            }
        });
          }
    public void getJSON(final String city) {

        new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID=ea574594b9d36ab688642d5fbeab847e");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer json = new StringBuffer(1024);
                    String tmp = "";

                    while((tmp = reader.readLine()) != null)
                        json.append(tmp).append("\n");
                    reader.close();

                    data = new JSONObject(json.toString());

                    if(data.getInt("cod") != 200) {
                        System.out.println("Cancelled");
                        return null;
                    }


                } catch (Exception e) {

                    System.out.println("Exception "+ e.getMessage());
                    return null;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void Void) {
                if(data!=null){
                    Log.d("my weather received",data.toString());



                    final TextView textView = (TextView) findViewById(R.id.textView);
                    String temperature = "Nothing";

                    try {

                        JSONObject jso = (JSONObject) data.get("main");
                        double temper = (double)jso.get("temp");
                        temper = (double) (temper - 273.15);
                        DecimalFormat numberFormat = new DecimalFormat("#.00");
                        temperature =  "" + numberFormat.format(temper) + "\u00B0" + "C";
                        if(temperature != null)
                        textView.setText(temperature);

                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }

            }
        }.execute();

    }

}
