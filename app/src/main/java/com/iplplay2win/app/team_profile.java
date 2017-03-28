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

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator;

public class team_profile extends AppCompatActivity {

//    ImageView TeamLogo;
//    TextView TeamName, HomeGround, Onwer, Link;
//    Button FacebookLink, TwitterLink;
Context context;
    int teamid;

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            teamid = Integer.parseInt(extras.getString("Teamid"));
            Log.e("Teamid", teamid + "");
        }
        Log.e("TeamID", String.valueOf(teamid));
        //Make call to AsyncTask
        //new team_profile.AsyncFetch().execute();
        new AsyncFetch().execute();
    }
    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(team_profile.this);
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
                //url = new URL(Urls.URL_PLAYERS_LIST_TEAMS);
                url = new URL(Urls.URL_PLAYERS_LIST_TEAMS +"/"+ teamid);
                Log.i("URL", "doInBackground:"+url);
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
            List<Team_ProfileData> data=new ArrayList<>();

            pdLoading.dismiss();
            try {
                // JSONObject jObj = new JSONObject("{\"results\":" + result + "}");
                JSONObject jObj = new JSONObject(result);
                JSONArray jArray = jObj.optJSONArray("players");
                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
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

            } catch (JSONException e) {
                Log.e("JSON PlayersList", result );
                Toast.makeText(team_profile.this, e.toString(), Toast.LENGTH_LONG).show();
                Log.e("JSONException", "onPostExecute:"+e.toString()+"" );
            }

        }

    }

}
