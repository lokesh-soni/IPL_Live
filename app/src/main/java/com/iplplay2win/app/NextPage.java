package com.iplplay2win.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NextPage extends AppCompatActivity {

    public static final String LOGIN_URL="http://www.api.iplplay2win.in/v1/exchange/create";
    ProgressDialog pDialog;

    Spinner sp;

    EditText name,phone,iwant,ihave;
    Button cancel,exchange;

    public static final String KEY_USERNAME="name";
    public static final String KEY_PHONE="phone";
    public static final String KEY_IWANT="iwant";
    public static final String KEY_IHAVE="ihave";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_page);

        name = (EditText) findViewById(R.id.et_name);
        phone = (EditText) findViewById(R.id.et_number);
        iwant = (EditText) findViewById(R.id.et_i_want);
        ihave = (EditText) findViewById(R.id.et_i_have);

        cancel = (Button) findViewById(R.id.btnCancel);
        exchange = (Button) findViewById(R.id.btnExchange);

        sp=(Spinner)findViewById(R.id.sp_match);

        ArrayList<String> list = new ArrayList<String>();
        list.add("All");
        list.add("Kolkata");
        list.add("Chennai");
        list.add("Punjab");
        list.add("Kochi");
        list.add("Hyderabad");
        list.add("Pune");
        list.add("Mumbai");

        // Custom ArrayAdapter with spinner item layout to set popup background

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(spinnerArrayAdapter);

        pDialog=new ProgressDialog(NextPage.this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);

        exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

    }

    private void userLogin() {

        showpDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // System.out.println("Volley String Response----" + response.toString());

                        Toast.makeText(NextPage.this, response.toString(), Toast.LENGTH_LONG).show();
                        openProfile();
                        hidepDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NextPage.this,error.toString(),Toast.LENGTH_LONG ).show();

                        hidepDialog();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> map = new HashMap<String,String>();
                //map.put("task", "userLogin");
                map.put("name", name.getText().toString());
                map.put("phone", phone.getText().toString());
                map.put("iwant",iwant.getText().toString());
                map.put("ihave",ihave.getText().toString());
                map.put("place","kolkata");
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
    private void openProfile(){
        Intent intent = new Intent(this, Tickets.class);
        startActivity(intent);
    }


    private void showpDialog(){
        if(!pDialog.isShowing())
            pDialog.show();
    }
    private void hidepDialog(){
        if(pDialog.isShowing())
            pDialog.dismiss();
    }
}
