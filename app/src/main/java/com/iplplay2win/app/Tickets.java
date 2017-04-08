package com.iplplay2win.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.dialogLayout;


public class Tickets extends AppCompatActivity {

    InterstitialAd mInterstitialAd;

    TextView tv_name,tv_phone,ihave,iwant;

    ProgressDialog pDiaolg;
    String getUrl = "http://www.api.iplplay2win.in/v1/exchange";



    RecyclerView ticket_recycler;
    private AdapterTickets mAdapter;
    private ArrayList<TicketModel> ticketList;

    Spinner spinner_nav;
    String item;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*MobileAds.initialize(getApplicationContext(), "ca-app-pub-4161588401571941/2137544315");

        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();

        }



        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });*/



        //BANNER
        MobileAds.initialize(getApplicationContext(),Urls.ADMOB_CODE);

        AdView adView=(AdView)findViewById(R.id.adViewTickets);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        tv_name=(TextView)findViewById(R.id.tv_name);
        tv_phone=(TextView)findViewById(R.id.tv_phone);
        ihave=(TextView)findViewById(R.id.tv_ihave);
        iwant=(TextView)findViewById(R.id.tv_iwant);

        pDiaolg =new ProgressDialog(Tickets.this);
        pDiaolg.setMessage("Please Wait...");
        pDiaolg.setCancelable(true);

        ticketList=new ArrayList<>();

        spinner_nav = (Spinner) findViewById(R.id.spinner_nav);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Tickets.this,NextPage.class);
                startActivity(i);
            }
        });
        addItemsToSpinner();
//        makeStringRequest();

    }
    public void addItemsToSpinner() {

        ArrayList<String> list = new ArrayList<String>();
      //  list.add("All");
        list.add("Kolkata");
        list.add("Chennai");
        list.add("Punjab");
        list.add("Kochi");
        list.add("Hyderabad");
        list.add("Pune");
        list.add("Mumbai");

        // Custom ArrayAdapter with spinner item layout to set popup background

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_nav.setAdapter(spinnerArrayAdapter);

        spinner_nav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//             boolean isSpinnerInitial = false;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//             if(isSpinnerInitial) {
                 item = spinnerArrayAdapter.getItem(position);
                 // Showing selected spinner item
//                 Toast.makeText(getApplicationContext(), "Selected  : " + item,
//                         Toast.LENGTH_LONG).show();
                showpDialog();
                 makeStringRequest();


//             }else{
//                 isSpinnerInitial=true;
             }
//            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showpDialog();
                makeStringRequest();
            }
        });
    }

    public void makeStringRequest() {
ticketList.clear();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, getUrl+"/"+item, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("tickets");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                TicketModel ticketModel = new TicketModel();

                                ticketModel.name = jsonObject.getString("name");
                                ticketModel.phone = jsonObject.getString("phone");
                                ticketModel.iwant = jsonObject.getString("iwant");
                                ticketModel.ihave = jsonObject.getString("ihave");
                                ticketModel.id = jsonObject.getString("id") + "";

                                ticketList.add(ticketModel);
                            }
                            ticket_recycler = (RecyclerView) findViewById(R.id.ticket_recycler);
                            mAdapter = new AdapterTickets(ticketList);

                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            ticket_recycler.setLayoutManager(mLayoutManager);

                            ticket_recycler.setAdapter(mAdapter);
                            hidepDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                hidepDialog();
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
        RequestQueue requestQueue = Volley.newRequestQueue(Tickets.this);
        requestQueue.add(req);
    }

    private void showpDialog() {
        if (!pDiaolg.isShowing()) {
            pDiaolg.show();
        }
    }

    private void hidepDialog() {
        if (pDiaolg.isShowing()) {
            pDiaolg.dismiss();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }



}
