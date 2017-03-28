package com.iplplay2win.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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


public class team extends AppCompatActivity {
    Context context;

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mTeamRV;
    private AdapterTeam mTeamAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String select_title = intent.getStringExtra("Select Title");
        toolbar.setTitle(select_title);
        //Make call to AsyncTask
        new AsyncFetch().execute();

    }
    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(team.this);
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
                url = new URL(Urls.URL_TEAM);

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
            List<TeamData> data=new ArrayList<TeamData>();

            pdLoading.dismiss();
            try {
                //JSONObject jObj = new JSONObject("{\"results\":" + result + "}");
                JSONObject jObj = new JSONObject(result);
                JSONArray jArray = jObj.optJSONArray("teams");
                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    TeamData teamData = new TeamData();
                    teamData.TeamLogo= json_data.optString("logo");
                    teamData.TeamName= json_data.optString("full_name");
                    //teamData.TeamID=json_data.getString("teamid");
                    teamData.TeamID=json_data.optInt("teamid")+"";

                    data.add(teamData);
                }

                Log.e("data count", data.size()+"");

                // Setup and Handover data to recyclerview
                mTeamRV = (RecyclerView)findViewById(R.id.teamRV);
                mTeamRV.setItemAnimator(new ScaleInTopAnimator());

                mTeamAdapter = new AdapterTeam(team.this, data);
                mTeamRV.setAdapter(new ScaleInAnimationAdapter(mTeamAdapter));
                mTeamRV.setLayoutManager(new LinearLayoutManager(team.this));

            } catch (JSONException e) {
                Toast.makeText(team.this, e.toString(), Toast.LENGTH_LONG).show();
                Log.e("JSONException", "onPostExecute:"+e.toString()+"" );
            }

        }

    }

}
