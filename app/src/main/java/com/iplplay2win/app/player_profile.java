package com.iplplay2win.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class player_profile extends AppCompatActivity {

    Context context;

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    Toolbar toolbar;

     ImageView PlayerDP;
     TextView PlayersName ,Format_bat_ball , mCountry , age;
     Button facebook_link_btn, twitter_link_btn;
     int playerid;
    String PlayerName, Age, country, bat_bowl, facebook_link, twitter_link, image_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //BANNER
        MobileAds.initialize(getApplicationContext(),"ca-app-pub-4161588401571941/6846945512");

        AdView adView=(AdView)findViewById(R.id.adViewPlayers);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playerid = Integer.parseInt(extras.getString("PlayerID"));
            Log.e("PlayerID", playerid + "");
        }
        new AsyncFetch().execute();
    }
    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(player_profile.this);
        HttpURLConnection conn;
        URL url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI threliad
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

                url = new URL(Urls.URL_PLAYER_INFO + playerid);
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

            try {

                // JSONObject jObj = new JSONObject("{\"results\":" + result + "}");
                JSONObject jObj = new JSONObject(result);
                JSONObject json_data = jObj.optJSONObject("players");
                // Extract data from json and store into ArrayList as class objects

//                    JSONObject json_data = jArray.getJSONObject(1);

                PlayersName=(TextView) findViewById(R.id.players_name);
                Format_bat_ball=(TextView)findViewById(R.id.bat_bowl_all);
                mCountry=(TextView)findViewById(R.id.country);
                age=(TextView)findViewById(R.id.age);

                facebook_link_btn=(Button) findViewById(R.id.facebook_link);
                twitter_link_btn=(Button) findViewById(R.id.twitter_link);

                PlayerDP=(ImageView) findViewById(R.id.player_image_dp);

                    image_link= json_data.getString("player_image");
                    PlayerName= json_data.getString("player_name");
                    Age=json_data.getString("age");
                    country=json_data.getString("country");
                    bat_bowl=json_data.getString("bat_bowler");
                    facebook_link=json_data.getString("facebook");
                    twitter_link=json_data.getString("twitter");

                Log.e("Profile_Links", "onPostExecute: " + "Player Name" + PlayerName + "country" + country );

                    PlayersName.setText(PlayerName);
                    age.setText(Age);
                    mCountry.setText(country);
                    Format_bat_ball.setText(bat_bowl);
                    toolbar.setTitle(PlayerName);

                    Glide.with(player_profile.this).load(image_link)
                            .placeholder(R.drawable.ic_img_placeholder)
                            .error(R.drawable.ic_img_error)
                            .into(PlayerDP);

                    facebook_link_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent fblink = new Intent(Intent.ACTION_VIEW);
                            fblink.setData(Uri.parse(facebook_link));
                            startActivity(fblink);
                        }
                    });
                    twitter_link_btn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent fblink = new Intent(Intent.ACTION_VIEW);
                            fblink.setData(Uri.parse(twitter_link));
                            startActivity(fblink);
                        }
                    });

//                    data.add(team_profileData);

                // Setup and Handover data to recyclerview
//                mTeamProfileRV = (RecyclerView)findViewById(R.id.players_list_rv);
//                mTeamProfileAdapter = new AdapterTeamProfile(team_profile.this, data);
//                mTeamProfileRV.setAdapter(mTeamProfileAdapter);
//                mTeamProfileRV.setLayoutManager(new LinearLayoutManager(team_profile.this));

            } catch (JSONException e) {
                Log.e("JSON PlayersList", result );
                Toast.makeText(player_profile.this, e.toString(), Toast.LENGTH_LONG).show();
                Log.e("JSONException", "onPostExecute:"+e.toString()+"" );
            }

        }

    }

}
