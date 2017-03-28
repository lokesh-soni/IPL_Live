package com.iplplay2win.app;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator;


public class Schedule extends AppCompatActivity {
    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mScheduleRV;
    private AdapterSchedule mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Make call to AsyncTask
        new AsyncFetch().execute();
    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(Schedule.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL(Urls.URL_SCHEDULE);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                //conn.setReadTimeout(READ_TIMEOUT);
                //conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                //conn.setDoOutput(true);

            /*} catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {*/

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        Log.e("line", line);
                        result.append(line);
                    }
                    reader.close();

                    // Pass data to onPostExecute method
                    return result.toString();

                }
                else {

                    return "unsuccessful";
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("result", result);

            //this method will be running on UI thread

            pdLoading.dismiss();
            List<ScheduleData> data=new ArrayList<>();

            pdLoading.dismiss();
            try {
                JSONObject jObj = new JSONObject("{\"results\":" + result + "}");
                JSONArray jArray = jObj.optJSONArray("results");
                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    ScheduleData scheduleData = new ScheduleData();
                    scheduleData.teamAlogo= json_data.getString("team_A_logo");
                    scheduleData.teamBlogo= json_data.getString("team_B_logo");
                    scheduleData.teamAShort_name= json_data.getString("team_A_short_name");
                    scheduleData.teamBShort_name= json_data.getString("team_B_short_name");
                    scheduleData.day= json_data.getString("day");
                    scheduleData.date= json_data.getString("date");
                    scheduleData.time=json_data.getString("time");
                    scheduleData.place=json_data.getString("place");

                    data.add(scheduleData);
                }

                // Setup and Handover data to recyclerview
                mScheduleRV = (RecyclerView)findViewById(R.id.schedule_rv);
                mScheduleRV.setItemAnimator(new ScaleInTopAnimator());
                mAdapter = new AdapterSchedule(Schedule.this, data);

                mScheduleRV.setAdapter(new ScaleInAnimationAdapter(mAdapter));
                mScheduleRV.setLayoutManager(new LinearLayoutManager(Schedule.this));

            } catch (JSONException e) {
                Toast.makeText(Schedule.this, e.toString(), Toast.LENGTH_LONG).show();
                Log.e("JSONException", "onPostExecute:"+e.toString()+"" );
            }

        }

    }
}
