package com.iplplay2win.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.inputmethodservice.Keyboard;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator;

import static com.iplplay2win.app.login.MyPREFERENCES;

public class IPL_Chattings extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageAdapter mAdapter;
    ArrayList<MessageModel> data;
    ProgressDialog pDialog;
    SharedPreferences sharedpreferences;

    String messageUrl= Urls.CHATTING;
String message,email,username;

    EditText et_message;
    String postmessageUrl="http://www.api.iplplay2win.in/v1/chatlogs/create";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iplchattings);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        data=new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        username = extras.getString("NAME");
        email = extras.getString("EMAIL");

        et_message=(EditText)findViewById(R.id.et_message);
        FloatingActionButton sendmess = (FloatingActionButton)findViewById(R.id.sendmessage);
        sendmess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = et_message.getText().toString().trim();

                if (message.equals("")) {
                    et_message.setError("Blank Message cannot be sent");
                } else {
                    makePostRequest();
                    et_message.clearFocus();
                    et_message.setText("");
                }
            }
        });
        Timer timer = new Timer();
        TimerTask timerTask;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                makeStringRequest();
                //refresh your textview
            }
        };
        timer.schedule(timerTask, 0, 2000);

        recyclerView=(RecyclerView)findViewById(R.id.chatRV);
        recyclerView.setItemAnimator(new ScaleInTopAnimator());

        mAdapter=new MessageAdapter(data,getApplicationContext());
        recyclerView.setAdapter(new AlphaInAnimationAdapter(mAdapter));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void makePostRequest(){

//        showpDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, postmessageUrl,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        et_message.clearComposingText();
//                        et_message.clearFocus();
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(et_message.getWindowToken(), 0);
                        recyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (getCurrentFocus()==et_message) {
                                recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                            }}
                        }, 100);
                        makeStringRequest();
//                        hidepDialog();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(IPL_Chattings.this,"Something went wrong ! Please Try Again "+ error,Toast.LENGTH_LONG ).show();
//                        hidepDialog();
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> map = new HashMap<String,String>();
                //map.put("task", "userLogin");
                map.put("message", message);
                map.put("email", email);
                map.put("name", username);

              //  map.put("status","msg" );
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

    public void makeStringRequest(){

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, messageUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray=null;
                        try {
                            jsonArray=response.getJSONArray("chatters");
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject=(JSONObject) jsonArray.get(i);

                                MessageModel messageModel=new MessageModel();
                                messageModel.name=jsonObject.optString("name");
                                messageModel.message=jsonObject.optString("message");
                                messageModel.timing=jsonObject.optString("timing");

                                data.add(messageModel);
                            }

                            mAdapter.newMessage(data);
                            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);

//                            recyclerView.setItemAnimator(new ScaleInTopAnimator());
//
//                            mAdapter=new MessageAdapter(data);
//                            recyclerView.setAdapter(new AlphaInAnimationAdapter(mAdapter));
//                            recyclerView.setLayoutManager(new LinearLayoutManager(IPL_Chattings.this));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSONException", "onPostExecute:"+e.toString()+"" );

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("chat Volley Request", "onErrorResponse: "+error+"" );
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
        RequestQueue requestQueue= Volley.newRequestQueue(IPL_Chattings.this);
        requestQueue.add(jsonObjectRequest);
    }

//    private void showpDialog(){
//        if(!pDialog.isShowing())
//            pDialog.show();
//    }


//    private void hidepDialog(){
//        try {
//            if ((pDialog != null) && pDialog.isShowing()) {
//                pDialog.dismiss();
//            }
//        } catch (final IllegalArgumentException e) {
//            // Handle or log or ignore
//        } catch (final Exception e) {
//            // Handle or log or ignore
//        } finally {
//            pDialog = null;
//        }
//    }

//    public void onResume(){
//        super.onResume();
//
//    }

}