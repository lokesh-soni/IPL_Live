package com.iplplay2win.app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator;

public class livestats extends AppCompatActivity {

    ProgressDialog pDialog;
    ArrayList<CapsNAll> data;
    ArrayList<PointsData> pointdata;
    private RecyclerView mCapsnAllRV;
    private RecyclerView mPointsTableRV;
    private AdapterPoints mAdapterPoints;
    private AdapterCapsnALL mAdapterCaps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livestats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //BANNER
        MobileAds.initialize(getApplicationContext(),Urls.ADMOB_CODE);

        AdView adView=(AdView)findViewById(R.id.adViewStats);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        ApplicationAnalytics.getInstance().trackScreenView("Live Stats");

        pDialog=new ProgressDialog(livestats.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        data=new ArrayList<>();
        pointdata=new ArrayList<>();
        makeStringRequest();
    }

    public void makeStringRequest(){
        showpDialog();

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Urls.TEAM_STATS , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray=null;
                        try {
//                            String teamnamelist,
// totalmatch_list, totalwin_list, totallost_list,totaldraw_list,totalnrr_list,totalpoints_list,teamlogoslist;

                            jsonArray=response.getJSONArray("tables");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject json_data=(JSONObject) jsonArray.get(i);
                                PointsData pointsData = new PointsData();
                                pointsData.teamnamelist= json_data.getString("teamshortname");
                                pointsData.totalmatch_list= json_data.getString("matches");
                                pointsData.totalwin_list= json_data.getString("wins");
                                pointsData.totallost_list= json_data.getString("losses");
                                pointsData.totaldraw_list= json_data.getString("draw");
                                pointsData.totalnrr_list= json_data.getString("nrr");
                                pointsData.totalpoints_list= json_data.getString("points");

                                pointdata.add(pointsData);
                            }
                            // Setup and Handover data to recyclerview
//                            private RecyclerView mPointsTableRV;
//                            private AdapterPoints mAdapterPoints;
                            mPointsTableRV = (RecyclerView)findViewById(R.id.pointtablerv);
//                            mTeamProfileRV.setItemAnimator(new ScaleInTopAnimator());

                            mAdapterPoints = new AdapterPoints(livestats.this, pointdata);
                            mPointsTableRV.setAdapter(mAdapterPoints);

                            mPointsTableRV.setLayoutManager(new LinearLayoutManager(livestats.this));
//                            hidepDialog();
                            makecapsRequest();

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
        RequestQueue requestQueue= Volley.newRequestQueue(livestats.this);
        requestQueue.add(jsonObjectRequest);
    }
    public void makecapsRequest(){
//        showpDialog();

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Urls.CAPS_N_ALL , null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray=null;
                        try {
                            jsonArray=response.getJSONArray("allcapsnall");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject json_data=(JSONObject) jsonArray.get(i);
                                CapsNAll capsnalldata = new CapsNAll();
                                capsnalldata.Heading= json_data.getString("heading");
                                capsnalldata.ImageInfo= json_data.getString("image_link");

                                data.add(capsnalldata);
                            }
                            // Setup and Handover data to recyclerview

                            mCapsnAllRV = (RecyclerView)findViewById(R.id.capsandall);
                            mCapsnAllRV.setItemAnimator(new ScaleInTopAnimator());

                            mAdapterCaps = new AdapterCapsnALL(livestats.this, data);
                            mCapsnAllRV.setAdapter(new ScaleInAnimationAdapter(mAdapterCaps));
                            LinearLayoutManager lla = new LinearLayoutManager(livestats.this,LinearLayoutManager.HORIZONTAL,false);

                            mCapsnAllRV.setLayoutManager(lla);
                            hidepDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSONException", "onPostExecute:"+e.toString()+"" );
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LivestatsVolleyRequest", "onErrorResponse: "+error+"" );
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
        RequestQueue requestQueue= Volley.newRequestQueue(livestats.this);
        requestQueue.add(jsonObjectRequest);
    }

    private void showpDialog(){
        if(!pDialog.isShowing()){
            pDialog.show();
        }
    }

    private void hidepDialog(){
        if(pDialog.isShowing()){
            pDialog.dismiss();
        }
    }

}
