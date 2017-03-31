package com.iplplay2win.app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;

public class p2w extends AppCompatActivity {

    ArrayList <String> list;
    ProgressDialog pDialog;
    String p2wURL="";

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

//        makeJsonRequest();

    }

//    public void makeJsonRequest(){
//        showpDialog();
//
//        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, p2wURL, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                //todo
//            }
//            },Response.ErrorListener(){
//                @Override
//                        public void onErrorResponse(VolleyError error){
//                    hidepDialog();
//                }{
//
//                }
//            }
//        }

    private void showpDialog(){
        if(!pDialog.isShowing())
            pDialog.show();
    }
    private void hidepDialog(){
        if(pDialog.isShowing()){
            pDialog.dismiss();
        }
    }
}
