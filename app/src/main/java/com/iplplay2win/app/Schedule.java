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

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator;


public class Schedule extends AppCompatActivity {
    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mScheduleRV;
    private AdapterSchedule mAdapter;
    ArrayList<ScheduleData> data;
    ProgressDialog pDialog;
    String select_title;
    String scheduleid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ApplicationAnalytics.getInstance().trackScreenView("SCHEDULE");

        //BANNER
        MobileAds.initialize(getApplicationContext(),Urls.ADMOB_CODE);
        AdView adView=(AdView)findViewById(R.id.adViewSchedule);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        //Make call to AsyncTask
      //  new AsyncFetch().execute();
        data=new ArrayList<>();
        pDialog=new ProgressDialog(Schedule.this);
        pDialog.setMessage("Loading");
        pDialog.setCancelable(false);

        makeStringRequest();
    }
    public void makeStringRequest(){
        showpDialog();

    JsonArrayRequest req = new JsonArrayRequest(Urls.URL_SCHEDULE,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                   // Log.d(TAG, response.toString());

                    try {
                        // Parsing json array response
                        // loop through each json object

                        for (int i = 0; i < response.length(); i++) {

                            JSONObject person = (JSONObject) response
                                    .get(i);
                            ScheduleData scheduleData = new ScheduleData();
                            scheduleid = scheduleData.schedule_id = person.getString("schedule_id");
                            scheduleData.teamAlogo= person.getString("team_A_logo");
                            scheduleData.teamBlogo= person.getString("team_B_logo");
                            scheduleData.teamAShort_name= person.getString("team_A_short_name");
                            scheduleData.teamBShort_name= person.getString("team_B_short_name");
                            scheduleData.day= person.getString("day");
                            scheduleData.date= person.getString("date");
                            scheduleData.time=person.getString("time");
                            scheduleData.place=person.getString("place");

                            data.add(scheduleData);

                            hidepDialog();
                        }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSONException", "onPostExecute:"+e.toString()+"" );

                        }
                    // Setup and Handover data to recyclerview
                    mScheduleRV = (RecyclerView)findViewById(R.id.schedule_rv);
                    mScheduleRV.setItemAnimator(new ScaleInTopAnimator());
                    mAdapter = new AdapterSchedule(Schedule.this, data);

                    mScheduleRV.setAdapter(new ScaleInAnimationAdapter(mAdapter));
                    mScheduleRV.setLayoutManager(new LinearLayoutManager(Schedule.this));

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("YouTube Volley Request", "onErrorResponse: "+error+"" );
                        hidepDialog();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params=new HashMap<String, String>();
                        return params;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded";
                    }
                };
                RequestQueue requestQueue= Volley.newRequestQueue(Schedule.this);
        requestQueue.add(req);
            }
    @Override
    public void onPause() {
        super.onPause();

        if ((pDialog != null) && pDialog.isShowing())
            pDialog.dismiss();
        pDialog = null;
    }


    private void showpDialog(){
//        if(!pDialog.isShowing()){
//            pDialog.show();
//        }
        try {
            if ((this.pDialog == null) && !pDialog.isShowing()) {
                this.pDialog.show();
            }
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
        } catch (final Exception e) {
            // Handle or log or ignore
        } finally {
            this.pDialog = null;
        }
    }
    private void hidepDialog(){
        try {
            if ((this.pDialog != null) && this.pDialog.isShowing()) {
                this.pDialog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore 
        } catch (final Exception e) {
            // Handle or log or ignore 
        } finally {
            this.pDialog = null;
        }
    }


   /* private class AsyncFetch extends AsyncTask<String, String, String> {
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

            *//*} catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {*//*

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
                    scheduleid = scheduleData.schedule_id = json_data.getString("schedule_id");
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

    }*/
}
