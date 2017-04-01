package com.iplplay2win.app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class p2w extends AppCompatActivity {

    ArrayList <String> list;
    ProgressDialog pDialog;
    String predictscheduleURL="http://www.api.iplplay2win.in/v1/schedule/292";
    public static final String postPredictdataURL="http://www.api.iplplay2win.in/v1/predict/create/1";

    public static final String statusURL="http://www.api.iplplay2win.in/v1/predict/user/";

    LinearLayout predict_winner , predict_mom, predict_hwt, predict_hrg;
    TextView teamA,teamB,matchNo,place1;
    TextView mPredictedWinningTeam,mPredictedMomPlayer,
            mPredictedHwtPlayer,mPredictedHrgPlayer;
    Button reset,submit_prediction;
    String useremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p2w);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
              //  selectteamoption(predict_mom,mPredictedMomPlayer);
            }
        });

       makeStringRequest();
    }

   

    public void makeStringRequest() {
        hidepDialog();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, predictscheduleURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                  //      belowLayout();

                        try {
                            JSONObject jsonObject = response.getJSONObject("schedule");
                            String schedule_id = jsonObject.getString("schedule_id");
                            String team_A_logo = jsonObject.getString("team_A_logo");
                            String team_B_logo = jsonObject.getString("team_B_logo");
                            String team_A_short_name = jsonObject.getString("team_A_short_name");
                            String team_B_short_name = jsonObject.getString("team_B_short_name");
                            String day = jsonObject.getString("day");
                            String place = jsonObject.getString("place");
                            String stadium = jsonObject.getString("stadium");

                            teamA.setText(team_A_short_name);
                            teamB.setText(team_B_short_name);
                            matchNo.setText(day);
                            place1.setText(stadium+", "+place );

                            belowLayout(schedule_id,useremail);

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

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                p2w.this,
                R.layout.dialog_list);
        
        //// TODO: 01-04-2017  Make arrayAdapter Dynamic 
        arrayAdapter.add("Kolkata Knight Rider");
        arrayAdapter.add("Royal Challenger Bangalore");

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
//    private void selectteamoption(LinearLayout LL, final TextView itemText) {
//
//        android.app.AlertDialog.Builder builderSingle = new android.app.AlertDialog.Builder(p2w.this);
//
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                p2w.this,
//                R.layout.dialog_list);
//        arrayAdapter.add("Kolkata Knight Rider");
//        arrayAdapter.add("Royal Challenger Bangalore");
//
//        builderSingle.setAdapter(
//                arrayAdapter,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which ) {
//
//
//
//                        String teamselected = arrayAdapter.getItem(which);
//
//                        teamplayersoption(teamselected,itemText);
////                        itemText.setText(teamselected);
////                        itemText.setVisibility(View.VISIBLE);
////                        Toast.makeText(p2w.this, teamselected +" Selected", Toast.LENGTH_SHORT).show();
////                        dialog.dismiss();
//                    }
//                });
//
//        builderSingle.show();
//    }
//
//    private void teamplayersoption(String teamid, final TextView itemText) {
//
//        android.app.AlertDialog.Builder builderSingle = new android.app.AlertDialog.Builder(p2w.this);
//
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                p2w.this,
//                R.layout.dialog_list);
//        arrayAdapter.add("Gautam Gambhir");
//        arrayAdapter.add("Virat Kohli");
//
//        builderSingle.setAdapter(
//                arrayAdapter,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which ) {
//                        String teamselected = arrayAdapter.getItem(which);
//                        itemText.setText(teamselected);
//                        itemText.setVisibility(View.VISIBLE);
//                        Toast.makeText(p2w.this, teamselected +" Selected", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
//                    }
//                });
//
//        builderSingle.show();
//    }

    private void showpDialog(){
        if(!pDialog.isShowing())
            pDialog.show();
    }
    private void hidepDialog(){
        if(pDialog.isShowing()){
            pDialog.dismiss();
        }
    }
    public void makeStatusUrl(){

        hidepDialog();


        JsonObjectRequest jsonreq = new JsonObjectRequest(Request.Method.GET, statusURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            response.getString("status");
                            JSONObject jsonObject = response.getJSONObject("predict");
                            String useremail = jsonObject.getString("useremail");
                            String scheduleid = jsonObject.getString("scheduleid");
                            String predictwinner = jsonObject.getString("predictwinner");
                            String predictmom = jsonObject.getString("predictmom");
                            String prediumhrg = jsonObject.getString("prediumhrg");
                            String predictswt = jsonObject.getString("predictswt");


                            mPredictedWinningTeam.setText(predictwinner);
                            mPredictedMomPlayer.setText(predictmom);
                            mPredictedHrgPlayer.setText(prediumhrg);
                            mPredictedHwtPlayer.setText(predictswt);


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
    public void belowLayout(String scheduleid ,String userid){
        hidepDialog();


        JsonObjectRequest jsonreq = new JsonObjectRequest(Request.Method.GET, predictscheduleURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject jsonObject = response.getJSONObject("schedule");
                            String schedule_id = jsonObject.getString("schedule_id");
                            String team_A_logo = jsonObject.getString("team_A_logo");
                            String team_B_logo = jsonObject.getString("team_B_logo");
                            String team_A_short_name = jsonObject.getString("team_A_short_name");
                            String team_B_short_name = jsonObject.getString("team_B_short_name");
                            String day = jsonObject.getString("day");
                            String place = jsonObject.getString("place");
                            String stadium = jsonObject.getString("stadium");

                            mPredictedWinningTeam.setText(team_A_short_name);
                            mPredictedMomPlayer.setText(team_B_short_name);
                            mPredictedHrgPlayer.setText(day);
                            mPredictedHwtPlayer.setText(stadium+", "+place );


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
    private void postpredictdata() {
        // TODO: 01-04-2017 Add POST method to set or clear texts.
        showpDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, postPredictdataURL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // System.out.println("Volley String Response----" + response.toString());

                        Toast.makeText(p2w.this, response.toString(), Toast.LENGTH_LONG).show();
                       // openProfile();
                        hidepDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(p2w.this,error.toString(),Toast.LENGTH_LONG ).show();
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
//                map.put("userid",userid);
//                map.put("scheduleid",scheduleid );
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

    }