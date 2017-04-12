package com.iplplay2win.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class login extends AppCompatActivity {

    public static final String MyPREFERENCES = "LOGINPrefs" ;
String activityname;
String an;
    CallbackManager callbackManager;
    String email,scheduleid,name;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       // FacebookSdk.sdkInitialize(getApplicationContext());
        getSupportActionBar().hide();
        Bundle extras = getIntent().getExtras();
        activityname = extras.getString("Activity");
        if (activityname.equals("p2w")){
            an= activityname;
            scheduleid = extras.getString("SCHEDULEID");

        }else
        {an=activityname;}
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        callbackManager = CallbackManager.Factory.create();

    LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_btn);
    //TextView textView = (TextView) findViewById(R.id.text);
        if (!isLoggedIn()){
    String[] permissionList = {"public_profile", "email"};
    loginButton.setReadPermissions(permissionList);

    loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.e("Login Result", loginResult.getAccessToken().getToken());
            Log.e("Login Result", loginResult.getAccessToken().getUserId());

            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            Log.e("LoginActivity", response.toString());

                            // Application code
                            try {
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                email = object.getString("email");
                                name = object.getString("name");
                                editor.putString("EMAIL",email);
                                editor.putString("NAME",name);
                                editor.apply();
//                                editor.commit();

                                Log.e("COOL TAG", "onCompleted: "+email +" name :"+name);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email");
            request.setParameters(parameters);
            request.executeAsync();

//            email = sharedpreferences.getString("EMAIL","email");
//            email = sharedpreferences.getString("EMAIL","email");
//            name = sharedpreferences.getString("NAME","name");
            Log.e("COOL1", "onCreate: "+ email +" name: " +name );
//    Log.e("COOL", "onCreate: "+ email );

            if( an.equals("p2w")){
                Intent intentone = new Intent(login.this, p2w.class);
                intentone.putExtra("EMAIL",email);
                intentone.putExtra("SCHEDULEID",scheduleid);
                startActivity(intentone);
                finish();
            }else {
                Intent intentone = new Intent(login.this, IPL_Chattings.class);
                intentone.putExtra("EMAIL",email);
                intentone.putExtra("NAME", name);
                startActivity(intentone);
                finish();
            }

//            Profile fbprofile= Profile.getCurrentProfile();
//            fb_id= fbprofile.getId();
//                    full_name=fbprofile.getName();

//            Log.e("Login Result", "onSuccess:"+ fb_id + " "+full_name );
//            textView.setText(loginResult.getAccessToken().getToken()
//                    + "\n"
//                    + loginResult.getAccessToken().getUserId().toString());

        }

        @Override
        public void onCancel() {
            Log.e("Login Result", "Login Cancelled");
           // textView.setText("Login Cancelled");
        }

        @Override
        public void onError(FacebookException error) {
            Log.e("Login Result", error.toString());
           // textView.setText(error.toString());
        }
    });
} else {
            email = sharedpreferences.getString("EMAIL","email");
            name = sharedpreferences.getString("NAME","name");
            Log.e("COOL2", "onCreate: "+ email +" name: " +name );
//    Log.e("COOL", "onCreate: "+ email );

    if( an.equals("p2w")){
        Intent intentone = new Intent(login.this, p2w.class);
        intentone.putExtra("EMAIL",email);
        intentone.putExtra("SCHEDULEID",scheduleid);
        startActivity(intentone);
        finish();
    }else {
        Intent intentone = new Intent(login.this, IPL_Chattings.class);
        intentone.putExtra("EMAIL",email);
        intentone.putExtra("NAME", name);
        startActivity(intentone);
        finish();
    }

}}
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /*public void printHashKey(Context pContext) {
        try {
            PackageManager pm = pContext.getPackageManager();
            PackageInfo info = pm.getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }*/

}