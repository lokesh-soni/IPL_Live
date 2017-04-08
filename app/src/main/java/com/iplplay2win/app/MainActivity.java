package com.iplplay2win.app;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kila.apprater_dialog.lars.AppRater;
import com.kobakei.ratethisapp.RateThisApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidmads.updatehandler.app.UpdateHandler;
import androidmads.updatehandler.app.UpdateListener;
import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity {

    TextView batsmanname, batsmanscore, batsmanwicket, batsmanover;
    TextView bowlername, bowlersrun, bowlersidemess,bowlerswicket;

    TextView mBatsman1,mBatsman2, mBowler;

    RelativeLayout livescore;
    LinearLayout matchdetails;

    TextView previousmatchstatus,comingmatchstatus;
    TextView matchislive;
//    ImageView poweredlink;

    private FirebaseDatabase mFirebaseInstance;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference batsmanovers = mRootRef.child("batsmanover");
    DatabaseReference batsmanscores = mRootRef.child("batsmanscores");
    DatabaseReference batsmanteam = mRootRef.child("batsmanteam");
    DatabaseReference batsman_wicket = mRootRef.child("batsmanwicket");
    DatabaseReference bowler_message = mRootRef.child("bowler_message");
    DatabaseReference bowler_scores = mRootRef.child("bowler_scores");
    DatabaseReference bowler_team = mRootRef.child("bowler_team");
    DatabaseReference bowler_wicket = mRootRef.child("bowler_wicket");
    DatabaseReference upcomingmatch = mRootRef.child("Upcoming_Match");
    DatabaseReference previousmatch = mRootRef.child("previousmatch");
    DatabaseReference match_status = mRootRef.child("matchstatus");
    DatabaseReference batsman1 = mRootRef.child("batsman1");
    DatabaseReference batsman2 = mRootRef.child("batsman2");
    DatabaseReference bowler = mRootRef.child("bowler");

    TextView linksspons;
    String getUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        UpdateHandler updateHandler = new UpdateHandler(MainActivity.this);
// to start version checker
        updateHandler.start();
// prompting intervals
        updateHandler.setCount(2);
// to print new features added automatically
        updateHandler.setWhatsNew(true);
// to enable or show default dialog prompt for version update
        updateHandler.showDefaultAlert(true);
// listener for custom update prompt
        updateHandler.setOnUpdateListener(new UpdateListener() {
            @Override
            public void onUpdateFound(boolean newVersion, String whatsNew) {
                Log.v("Update", String.valueOf(newVersion));
                Log.v("Update", whatsNew);
            }


        });
//        linksspons = (TextView)findViewById(R.id.adlink);

     //   poweredlink = (ImageView)findViewById(R.id.sponslogo);

        // Custom condition: 3 days and 5 launches
        RateThisApp.Config config = new RateThisApp.Config(3, 3);
        config.setTitle(R.string.my_own_title);
        config.setMessage(R.string.my_own_message);
        config.setYesButtonText(R.string.my_own_rate);
        config.setNoButtonText(R.string.my_own_thanks);
        config.setCancelButtonText(R.string.my_own_cancel);
        config.setUrl("https://play.google.com/store/apps/details?id=com.iplplay2win.app&hl=en");
        RateThisApp.init(config);

        RateThisApp.setCallback(new RateThisApp.Callback() {
           // @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onYesClicked() {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.iplplay2win.app&hl=en" + getApplicationContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                }
            }

            @Override
            public void onNoClicked() {
               // Toast.makeText(MainActivity.this, "No event", Toast.LENGTH_SHORT).show();
                RateThisApp.Config config = new RateThisApp.Config(3, 3);
                RateThisApp.init(config);
            }

            @Override
            public void onCancelClicked() {
               // Toast.makeText(MainActivity.this, "Cancel event", Toast.LENGTH_SHORT).show();
                RateThisApp.stopRateDialog(getApplicationContext());
            }
        });
        ApplicationAnalytics.getInstance().trackScreenView("Home Screen");

        batsmanname = (TextView)findViewById(R.id.Batsman);
        batsmanscore = (TextView)findViewById(R.id.batsmanscores);
        batsmanwicket = (TextView)findViewById(R.id.batsmanwicket);
        batsmanover = (TextView)findViewById(R.id.batsmanovers);

        bowlername = (TextView)findViewById(R.id.bowlername);
        bowlersrun = (TextView)findViewById(R.id.bowlerscores);
        bowlersidemess = (TextView)findViewById(R.id.bowlersidemess);
        bowlerswicket = (TextView)findViewById(R.id.bowlerwicket);

        previousmatchstatus = (TextView)findViewById(R.id.previousmatchstatus);
        comingmatchstatus = (TextView)findViewById(R.id.comingmatchstatus);

        livescore = (RelativeLayout)findViewById(R.id.livescore);
        matchdetails= (LinearLayout) findViewById(R.id.matchdetails);

        matchislive = (TextView)findViewById(R.id.matchislive);

        mBatsman1 = (TextView)findViewById(R.id.batsman1);
        mBatsman2 = (TextView)findViewById(R.id.batsman2);
        mBowler = (TextView)findViewById(R.id.bowler);


        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("FIREBASE TOKEN", "onCreate: " +refreshedToken);

//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        TextView p2wbtn = (TextView) findViewById(R.id.p2wbtn);
        p2wbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent p2w= new Intent(MainActivity.this,Schedule.class);
                startActivity(p2w);
//                Toast.makeText(MainActivity.this, "Our New Feature, Wait for IPL matches",
//                        Toast.LENGTH_LONG).show();
            }
        });
        TextView ttypbtn = (TextView) findViewById(R.id.ttypbtn);
        ttypbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ttypintent = new Intent(MainActivity.this,team.class);

                startActivity(ttypintent);
            }
        });
        TextView videofeed = (TextView) findViewById(R.id.youtubevideos);
        videofeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent watchvideos= new Intent(MainActivity.this,youtubevideolist.class);
                startActivity(watchvideos);
            }
        });
        TextView exchangeticket = (TextView)findViewById(R.id.exchangeurticket);
        exchangeticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tickets= new Intent(MainActivity.this,Tickets.class);
                startActivity(tickets);
//                Toast.makeText(MainActivity.this, "Our New Feature, Wait for IPL matches",
//                        Toast.LENGTH_LONG).show();
            }
        });
        TextView schedulebtn = (TextView) findViewById(R.id.schedulebtn);
        schedulebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scheduleintent = new Intent(MainActivity.this,Schedule.class);
                startActivity(scheduleintent);
            }
        });
        TextView stats = (TextView) findViewById(R.id.livestats);
        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent livest = new Intent(MainActivity.this,livestats.class);
                startActivity(livest);
            }
        });
////Powered By // TODO: 07-04-2017 PoweredBy Advt. in front screen...
//        makeStringRequest();

//        poweredlink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                linksspons.performClick();
//                String link= linksspons.getText().toString();
//                Intent callIntent = new Intent(Intent.ACTION_VIEW);
//                callIntent.setData(Uri.parse(link));
//                //  callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(callIntent);
//            }
//        });
    }
 /*   public void makeStringRequest() {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, Urls.SPONS_LINK, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonObject;
//                        JSONObject jsonobject = null;
                        try {
//                            jsonArray = response.getJSONArray("spons");
                            jsonObject = response.getJSONObject("spons");
//                            Spons_Model sponsmodel = new Spons_Model();
                            String ImageLink = jsonObject.getString("imagelink");
                            String link = jsonObject.getString("link");
//                            ticketList.add(sponsmodel);

                            Glide.with(MainActivity.this).load(ImageLink)
                                    .placeholder(R.drawable.ic_img_placeholder)
                                    .error(R.drawable.ic_img_error)
                                    .into(poweredlink);
                            linksspons.setText(link);

//                            hidepDialog();

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
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(req);
    }*/

    @Override
    protected void onStart(){
        super.onStart();

        // Monitor launch times and interval from installation
        RateThisApp.onStart(this);
        // If the condition is satisfied, "Rate this app" dialog will be shown
        RateThisApp.showRateDialogIfNeeded(this);

        match_status.addValueEventListener(new ValueEventListener(){


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        String message = dataSnapshot.getValue(String.class);
if (message.equals("live")){
            livescore.setVisibility(View.VISIBLE);
            matchdetails.setVisibility(View.GONE);
            matchislive.setVisibility(View.VISIBLE);
        }
else {
        livescore.setVisibility(View.GONE);
        matchdetails.setVisibility(View.VISIBLE);
        matchislive.setVisibility(View.INVISIBLE);
}
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});
        batsmanovers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String message = dataSnapshot.getValue(String.class);
                batsmanover.setText(message);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        batsmanscores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String message = dataSnapshot.getValue(String.class);
                batsmanscore.setText(message);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        batsmanteam.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String message = dataSnapshot.getValue(String.class);
                batsmanname.setText(message);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        batsman_wicket.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String message = dataSnapshot.getValue(String.class);
                batsmanwicket.setText(message);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        bowler_message.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String message = dataSnapshot.getValue(String.class);
                bowlersidemess.setText(message);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        bowler_scores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String message = dataSnapshot.getValue(String.class);
                bowlersrun.setText(message);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        bowler_team.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String message = dataSnapshot.getValue(String.class);
                bowlername.setText(message);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        bowler_wicket.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String message = dataSnapshot.getValue(String.class);
                bowlerswicket.setText(message);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        upcomingmatch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String message = dataSnapshot.getValue(String.class);
                comingmatchstatus.setText(message);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        previousmatch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String message = dataSnapshot.getValue(String.class);
                previousmatchstatus.setText(message);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        batsman1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String message = dataSnapshot.getValue(String.class);
                mBatsman1.setText(message);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        batsman2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String message = dataSnapshot.getValue(String.class);
                mBatsman2.setText(message);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        bowler.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String message = dataSnapshot.getValue(String.class);
                mBowler.setText(message);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    // [END on_start_add_listener]

}
