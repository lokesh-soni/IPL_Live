package com.iplplay2win.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;

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

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator;

public class team_profile extends AppCompatActivity {

    InterstitialAd mInterstitialAd;

    ProgressDialog pDialog;
    ArrayList<Team_ProfileData> data;

//    ImageView TeamLogo;
//    TextView TeamName, HomeGround, Onwer, Link;
//    Button FacebookLink, TwitterLink;
Context context;
    int teamid;
    String  url;

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mTeamProfileRV;
    private AdapterTeamProfile mTeamProfileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ApplicationAnalytics.getInstance().trackScreenView("Team Profile");

        inter();

        //BANNER
        MobileAds.initialize(getApplicationContext(),"ca-app-pub-4161588401571941/6846945512");

        AdView adView=(AdView)findViewById(R.id.adViewProfile);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
            teamid = Integer.parseInt(extras.getString("Teamid"));
            Log.e("Teamid", teamid + "");
        Log.e("TeamID", String.valueOf(teamid));
        //Make call to AsyncTask
        //new team_profile.AsyncFetch().execute();
      //  new AsyncFetch().execute();
        pDialog=new ProgressDialog(team_profile.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        data=new ArrayList<>();

        makeStringRequest();
    }

    public void makeStringRequest(){
        showpDialog();

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Urls.URL_PLAYERS_LIST_TEAMS +"/"+ teamid , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray=null;
                        try {
                            jsonArray=response.getJSONArray("players");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject json_data=(JSONObject) jsonArray.get(i);
                                Team_ProfileData team_profileData = new Team_ProfileData();
                                team_profileData.player_image= json_data.getString("player_image");
                                team_profileData.player_name= json_data.getString("player_name");
                                team_profileData.playerid=json_data.getString("player_id");

                                data.add(team_profileData);
                            }
                            // Setup and Handover data to recyclerview
                            mTeamProfileRV = (RecyclerView)findViewById(R.id.players_list_rv);
                            mTeamProfileRV.setItemAnimator(new ScaleInTopAnimator());

                            mTeamProfileAdapter = new AdapterTeamProfile(team_profile.this, data);
                            mTeamProfileRV.setAdapter(new ScaleInAnimationAdapter(mTeamProfileAdapter));

                            mTeamProfileRV.setLayoutManager(new LinearLayoutManager(team_profile.this));
                            hidepDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSONException", "onPostExecute:"+e.toString()+"" );
                        }
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
        RequestQueue requestQueue= Volley.newRequestQueue(team_profile.this);
        requestQueue.add(jsonObjectRequest);
    }
    private void showpDialog(){
        if(!pDialog.isShowing()){
            pDialog.show();
        }
    }
    private void hidepDialog(){
        try {
            if ((pDialog != null) && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        } catch (final IllegalArgumentException e) {
            // Handle or log or ignore
        } catch (final Exception e) {
            // Handle or log or ignore
        } finally {
            pDialog = null;
        }
    }
    public void inter(){



        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });

    }
    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
}