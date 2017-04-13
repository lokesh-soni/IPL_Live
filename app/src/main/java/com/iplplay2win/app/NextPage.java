package com.iplplay2win.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
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
import com.liuguangqiang.ripplelayout.Point;
import com.liuguangqiang.ripplelayout.Ripple;
import com.liuguangqiang.ripplelayout.RippleLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NextPage extends AppCompatActivity {

    public static final String LOGIN_URL="http://www.api.iplplay2win.in/v1/exchange/create";
    ProgressDialog pDialog;

    Spinner sp;
    String item;
    private RippleLayout rippleLayout;
    EditText name,phone,iwant,ihave;
    Button cancel,exchange;
    private Point point;

    CardView cardform;

    /*public static final String KEY_USERNAME="name";
    public static final String KEY_PHONE="phone";
    public static final String KEY_IWANT="iwant";
    public static final String KEY_IHAVE="ihave";*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_page);

        rippleLayout = (RippleLayout) findViewById(R.id.ripple);
        rippleLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

        Bundle bundle = getIntent().getExtras();
        point = bundle.getParcelable(Ripple.ARG_START_LOCATION);
        rippleLayout.setOnStateChangedListener(new RippleLayout.OnStateChangedListener() {
            @Override
            public void onOpened() {
                startIntoAnimation();
            }

            @Override
            public void onClosed() {
                finish();
                overridePendingTransition(0, 0);
            }
        });

        rippleLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                rippleLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                rippleLayout.start(point);
                return true;
            }
        });
        cardform=(CardView)findViewById(R.id.cardform);
        cardform.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                cardform.getViewTreeObserver().removeOnPreDrawListener(this);
                cardform.setTranslationY(-cardform.getHeight());

                return true;
            }
        });

        name = (EditText) findViewById(R.id.et_name);
        phone = (EditText) findViewById(R.id.et_number);
        iwant = (EditText) findViewById(R.id.et_i_want);
        ihave = (EditText) findViewById(R.id.et_i_have);

        sp=(Spinner)findViewById(R.id.sp_match);

        ArrayList<String> list = new ArrayList<String>();
      //  list.add("Select Match Venue");
//        list.add("Kolkata");
        list.add("Kolkata");
        list.add("Mumbai");
        list.add("Delhi");
        list.add("Pune");
        list.add("Hyderabad");
        list.add("Bangalore");
        list.add("Punjab");
        list.add("Gujarat");

        // Custom ArrayAdapter with spinner item layout to set popup background

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(spinnerArrayAdapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              boolean isSpinnerInitial = false;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              if(isSpinnerInitial) {
                  item = spinnerArrayAdapter.getItem(position);

                  // Showing selected spinner item
                  Toast.makeText(getApplicationContext(), "Selected  : " + item,
                          Toast.LENGTH_LONG).show();
              }else{
                  isSpinnerInitial=true;
              }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(NextPage.this,"Please select match venue",Toast.LENGTH_LONG).show();

            }
        });
        pDialog=new ProgressDialog(NextPage.this);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);

    }
    private void startIntoAnimation() {
        cardform.animate().translationY(0).setDuration(400).setInterpolator(new DecelerateInterpolator());
    }

    private void userLogin() {

        showpDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // System.out.println("Volley String Response----" + response.toString());

                        //  Toast.makeText(NextPage.this, response.toString(), Toast.LENGTH_LONG).show();
                        openProfile();
                        hidepDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      //  Toast.makeText(NextPage.this,error.toString(),Toast.LENGTH_LONG ).show();

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
                map.put("place",item);
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
        finish();
    }

    private void showpDialog(){
        if(!pDialog.isShowing())
            pDialog.show();
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

    public void register(){
        initialize();
        if(!validate()){
            Toast.makeText(this,"Please Enter All Fields Correctly",Toast.LENGTH_LONG).show();
        }else{
            userLogin();
        }
    }

    public boolean validate(){
        boolean valid=true;
        if(name.getText().toString().isEmpty()){
            name.setError("Please Enter Valid Name");
            valid=false;
        }
        if(phone.getText().toString().isEmpty() || phone.length()<10){
            phone.setError("Please Enter Valid 10 Digit Number");
            valid=false;
        }
        if(iwant.getText().toString().isEmpty()){
            iwant.setError("Please Enter What You Want");
            valid=false;
        }
        if(ihave.getText().toString().isEmpty()){
            ihave.setError("Please Enter what you have");
            valid=false;
        }

        return valid;
    }

    public void initialize(){
        name.getText().toString();
        phone.getText().toString();
        iwant.getText().toString();
        ihave.getText().toString();
    }
    @Override

    public void onBackPressed() {
        if (rippleLayout.canBack()) {
            if (rippleLayout.isAnimationEnd()) {
                startOutAnimation();
                rippleLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rippleLayout.back();
                    }
                }, 300);
            }
        } else {
            super.onBackPressed();
        }
    }

    private void startOutAnimation() {
        cardform.animate()
                .translationY(-cardform.getHeight())
                .alpha(0.0f)
                .setDuration(400)
                .setInterpolator(new DecelerateInterpolator());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_team, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_exchange :
                register();

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
