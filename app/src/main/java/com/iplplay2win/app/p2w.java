package com.iplplay2win.app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class p2w extends AppCompatActivity {

    ArrayList <String> list;

    ProgressDialog pDialog;
    String predictscheduleURL="http://www.api.iplplay2win.in/v1/schedule/";

    String postPredictdataURL="http://www.api.iplplay2win.in/v1/predict/create";

    String statusURL="http://www.api.iplplay2win.in/v1/predict/user/";

    String teamplayerslist="http://www.api.iplplay2win.in/v1/players/all/";

    LinearLayout predict_winner , predict_mom, predict_hwt, predict_hrg;
    TextView teamA,teamB,matchNo,place1;
    TextView mPredictedWinningTeam,mPredictedMomPlayer,
            mPredictedHwtPlayer,mPredictedHrgPlayer;
    Button reset,submit_prediction;
    String useremail,scheduleid;
    String team_B_logo, team_A_short_name, team_B_short_name, day, place, stadium, team_A_logo, schedule_id;

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p2w);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        scheduleid = extras.getString("SCHEDULEID");
        useremail = extras.getString("EMAIL");

       // predictscheduleURL= predictscheduleURL+scheduleid;

        ApplicationAnalytics.getInstance().trackScreenView("P2W");

        list=new ArrayList<>();
        pDialog=new ProgressDialog(p2w.this);
        pDialog.setMessage("Please Wait");
        pDialog.setCancelable(false);

        predict_winner = (LinearLayout)findViewById(R.id.Predict_winner);
        predict_mom = (LinearLayout)findViewById(R.id.Predict_mom);
        predict_hrg = (LinearLayout)findViewById(R.id.Predict_hrg);
        predict_hwt = (LinearLayout)findViewById(R.id.Predict_hwt);

        teamA = (TextView)findViewById(R.id.teama_p2w);
        teamB = (TextView)findViewById(R.id.teamb_p2w);
        matchNo = (TextView)findViewById(R.id.matchno_predict);
        place1 = (TextView)findViewById(R.id.ma_predict);

        mPredictedWinningTeam = (TextView)findViewById(R.id.PredictedWinningTeam);
        mPredictedMomPlayer = (TextView)findViewById(R.id.PredictedMomPlayer);
        mPredictedHwtPlayer = (TextView)findViewById(R.id.PredictedHwtPlayer);
        mPredictedHrgPlayer = (TextView)findViewById(R.id.PredictedHrgPlayer);

        reset=(Button)findViewById(R.id.bReset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleartexts();
            }
        });

        submit_prediction=(Button)findViewById(R.id.submit_prediction);
        submit_prediction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postpredictdata();
            }
        });

        predict_winner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectdialogoption(predict_winner,mPredictedWinningTeam);

            }
        });

        predict_mom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectteamoption(predict_mom,mPredictedMomPlayer);
            }
        });
        predict_hrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectteamoption(predict_hrg,mPredictedHrgPlayer);
            }
        });
        predict_hwt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectteamoption(predict_hwt,mPredictedHwtPlayer);
            }
        });

       makeStringRequest();
    }

    public void makeStringRequest() {
        hidepDialog();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, predictscheduleURL+scheduleid, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                  //      belowLayout();

                        try {
                            JSONObject jsonObject = response.getJSONObject("schedule");
                            schedule_id = jsonObject.getString("schedule_id");
                            team_A_logo = jsonObject.getString("team_A_logo");
                            team_B_logo = jsonObject.getString("team_B_logo");
                            team_A_short_name = jsonObject.getString("team_A_short_name");
                            team_B_short_name = jsonObject.getString("team_B_short_name");
                            day = jsonObject.getString("day");
                            place = jsonObject.getString("place");
                            stadium = jsonObject.getString("stadium");

                            teamA.setText(team_A_short_name);
                            teamB.setText(team_B_short_name);
                            matchNo.setText(day);
                            place1.setText(stadium+", "+place );

                            makeStatusUrl(schedule_id,useremail);

                        }
                        catch (JSONException e1) {
                            e1.printStackTrace();
                    }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(p2w.this);
        requestQueue.add(req);

    }

    private void cleartexts() {
        mPredictedWinningTeam.setText(null);
        mPredictedWinningTeam.setVisibility(View.GONE);
        mPredictedMomPlayer.setText(null);
        mPredictedMomPlayer.setVisibility(View.GONE);
        mPredictedHwtPlayer.setText(null);
        mPredictedHwtPlayer.setVisibility(View.GONE);
        mPredictedHrgPlayer.setText(null);
        mPredictedHrgPlayer.setVisibility(View.GONE);
        
        postpredictdata();
    }
    private void selectdialogoption(LinearLayout clickeditem, final TextView itemText) {

        android.app.AlertDialog.Builder builderSingle = new android.app.AlertDialog.Builder(p2w.this);

        arrayAdapter = new ArrayAdapter<String>(
                p2w.this,
                R.layout.dialog_list);

        //// TODO: 01-04-2017  Make arrayAdapter Dynamic 
//        arrayAdapter.add("Kolkata Knight Rider");
//        arrayAdapter.add("Royal Challenger Bangalore");
        arrayAdapter.add(team_A_short_name);
        arrayAdapter.add(team_B_short_name);

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which ) {
                        String teamselected = arrayAdapter.getItem(which);
                        itemText.setText(teamselected);
                        itemText.setVisibility(View.VISIBLE);
                        Toast.makeText(p2w.this, teamselected+"Selected", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

        builderSingle.show();
    }

private void selectteamoption(final LinearLayout clickeditem, final TextView itemText) {

    android.app.AlertDialog.Builder builderSingle = new android.app.AlertDialog.Builder(p2w.this);

    arrayAdapter = new ArrayAdapter<String>(
            p2w.this,
            R.layout.dialog_list);

    //// TODO: 01-04-2017  Make arrayAdapter Dynamic

    arrayAdapter.add(team_A_short_name);
    arrayAdapter.add(team_B_short_name);

    builderSingle.setAdapter(
            arrayAdapter,
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which ) {
//                    String teamselected = arrayAdapter.getItem(which);
//                    itemText.setText(teamselected);
//                    itemText.setVisibility(View.VISIBLE);
//                    Toast.makeText(p2w.this, teamselected+" Selected", Toast.LENGTH_SHORT).show();
                    String teamselect= arrayAdapter.getItem(which);
                    Log.e("TeamSelected", "onClick:"+ teamselect );
                    selectteamplayer(clickeditem,itemText,teamselect);
                    dialog.dismiss();
                }
            });

    builderSingle.show();
}

    private void selectteamplayer(final LinearLayout clickedteam, final TextView itemText,final String team) {

        android.app.AlertDialog.Builder builderSingle = new android.app.AlertDialog.Builder(p2w.this);

        arrayAdapter = new ArrayAdapter<String>(
                p2w.this,
                R.layout.dialog_list);

        maketeamlistrequest(team);

//        arrayAdapter.add("Kolkata Knight Rider");
//        arrayAdapter.add("Royal Challenger Bangalore");

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int player ) {
                    String teamselected = arrayAdapter.getItem(player);
                    itemText.setText(teamselected);
                    itemText.setVisibility(View.VISIBLE);
                    Toast.makeText(p2w.this, teamselected+" Selected", Toast.LENGTH_SHORT).show();
//                        selectteamplayer(clickedteam,itemText);
                        dialog.dismiss();
                    }
                });

        builderSingle.show();
    }

    private void showpDialog(){
        try {
            if ((pDialog == null) && !pDialog.isShowing()) {
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
    public void makeStatusUrl(String scheduleid ,String userid){

        hidepDialog();
//        statusURL= statusURL+userid+"/"+scheduleid;


        JsonObjectRequest jsonreq = new JsonObjectRequest(Request.Method.GET, statusURL+userid+"/"+scheduleid, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //response.getString("status");
                            JSONObject jsonObject = response.getJSONObject("predict");
//                            String useremail = jsonObject.getString("useremail");
//                            String scheduleid = jsonObject.getString("scheduleid");
                            String predictwinner = jsonObject.getString("predictwinner");
                            String predictmom = jsonObject.getString("predictmom");
                            String prediumhrg = jsonObject.getString("prediumhrg");
                            String predictswt = jsonObject.getString("predictswt");

                            mPredictedWinningTeam.setText(predictwinner);
                            if (predictwinner != null) {
                                mPredictedWinningTeam.setVisibility(View.VISIBLE);
                            }
                            mPredictedMomPlayer.setText(predictmom);
                            if (predictmom != null) {
                                mPredictedMomPlayer.setVisibility(View.VISIBLE);
                            }
                            mPredictedHrgPlayer.setText(prediumhrg);
                            if (prediumhrg != null) {
                                mPredictedHrgPlayer.setVisibility(View.VISIBLE);
                            }
                            mPredictedHwtPlayer.setText(predictswt);
                            if (predictswt != null) {
                                mPredictedHwtPlayer.setVisibility(View.VISIBLE);
                            }

                        }
                        catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("statusURL", "onErrorResponse: "+error );
                hidepDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(p2w.this);
        requestQueue.add(jsonreq);
    }

    private void postpredictdata() {
        // TODO: 01-04-2017 Add POST method to set or clear texts.

        showpDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, postPredictdataURL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        if ((mPredictedWinningTeam.getText().toString().equals("")
                                & mPredictedMomPlayer.getText().toString().equals("")
                                & mPredictedHrgPlayer.getText().toString().equals("")
                                & mPredictedHwtPlayer.getText().toString().equals(""))) {
                                    Toast.makeText(p2w.this, "Reset Done", Toast.LENGTH_LONG).show();
                                } else {
                            Toast.makeText(p2w.this, "You will received the mail if you were one of lucky contestant, to predict Right", Toast.LENGTH_LONG).show();
                        }
                        Log.e("POST PREDICT DATA", "onResponse: "+ response );
                        hidepDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(p2w.this,"Something went wrong ! Please Try Again ",Toast.LENGTH_LONG ).show();
                        hidepDialog();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> map = new HashMap<String,String>();
                //map.put("task", "userLogin");
                map.put("predictwinner", mPredictedWinningTeam.getText().toString());
                map.put("predictmom", mPredictedMomPlayer.getText().toString());
                map.put("prediumhrg",mPredictedHrgPlayer.getText().toString());
                map.put("predictswt",mPredictedHwtPlayer.getText().toString());
                map.put("userid",useremail);
                map.put("scheduleid",scheduleid );
                return map;
            }
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void maketeamlistrequest(String teamshortname){

        hidepDialog();

        JsonObjectRequest jsonreq = new JsonObjectRequest(Request.Method.GET, teamplayerslist+teamshortname, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            //response.getString("status");
                            JSONArray jArray = response.optJSONArray("players");

                            for(int i=0;i<jArray.length();i++){
                                JSONObject json_data = jArray.getJSONObject(i);
//                                Team_ProfileData team_profileData = new Team_ProfileData();
//                                team_profileData.player_image= json_data.getString("player_image");
                               String playerslist = json_data.getString("player_name");
//                                team_profileData.playerid=json_data.getString("player_id");

                                arrayAdapter.add(playerslist);

//                                return arrayAdapter.add(playerslist);
                            }

                        }
                        catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hidepDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(p2w.this);
        requestQueue.add(jsonreq);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_logout :
                LoginManager.getInstance().logOut();
                Intent logout = new Intent (this,MainActivity.class);
                startActivity(logout);

                break;
        }

        return super.onOptionsItemSelected(item);
    }
    }