package com.iplplay2win.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity {

//    private FirebaseAnalytics mFirebaseAnalytics;mFirebaseAnalytics

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        TextView p2wbtn = (TextView) findViewById(R.id.p2wbtn);
        p2wbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Our New Feature, Wait for IPL matches",
                        Toast.LENGTH_LONG).show();
            }
        });
        LinearLayout ttypbtn = (LinearLayout) findViewById(R.id.ttypbtn);
        ttypbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ttypintent = new Intent(MainActivity.this,team.class);
//                ttypintent.putExtra("Select Team","Select Team");
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
                Toast.makeText(MainActivity.this, "Our New Feature, Wait for IPL matches",
                        Toast.LENGTH_LONG).show();
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
        TextView teambtn = (TextView) findViewById(R.id.teambtn);
        teambtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent teamintent = new Intent(MainActivity.this,team.class);
                startActivity(teamintent);
            }
        });
    }

}
