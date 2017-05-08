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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
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
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        AdRequest adRequest1 = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest1);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });

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

    JsonArrayRequest req = new JsonArrayRequest(Urls.URL_SCHEDULE+"all",
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
    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

}
