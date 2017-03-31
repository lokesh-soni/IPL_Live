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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator;

public class youtubevideolist extends AppCompatActivity {

    ArrayList<YouTubeData> list;
    ProgressDialog pDialog;
    private RecyclerView rv;
    private AdapterYouTubeVideos adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtubevideolist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //BANNER
        MobileAds.initialize(getApplicationContext(),Urls.ADMOB_CODE);

        AdView adView=(AdView)findViewById(R.id.adViewYoutube);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        list=new ArrayList<>();
        pDialog=new ProgressDialog(youtubevideolist.this);
        pDialog.setMessage("Please Wait..");
        pDialog.setCancelable(true);

        makeStringRequest();
    }
    public void makeStringRequest(){
        showpDialog();

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Urls.URL_VIDEO_LIST, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray=null;
                        try {
                            jsonArray=response.getJSONArray("videos");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject=(JSONObject) jsonArray.get(i);
                                YouTubeData youTubeData=new YouTubeData();
                                youTubeData.thumbnail=jsonObject.optString("thumbnail");
                                youTubeData.title=jsonObject.optString("video_name");

                                list.add(youTubeData);
                            }
                            rv=(RecyclerView)findViewById(R.id.RVYouTubeList);
                            rv.setItemAnimator(new ScaleInTopAnimator());

                            adapter=new AdapterYouTubeVideos(youtubevideolist.this,list);
                            rv.setAdapter(new ScaleInAnimationAdapter(adapter));
                            rv.setLayoutManager(new LinearLayoutManager(youtubevideolist.this));
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
        RequestQueue requestQueue= Volley.newRequestQueue(youtubevideolist.this);
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