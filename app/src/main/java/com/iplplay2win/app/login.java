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
    LoginButton loginButton;
    TextView textView;
    CallbackManager callbackManager;
    String full_name = "";
    String fb_id = "";
    String email,scheduleid;
    SharedPreferences sharedpreferences;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        FacebookSdk.sdkInitialize(getApplicationContext());
////        setContentView(R.layout.activity_login);
////        loginButton=(LoginButton)findViewById(R.id.fb_login_btn);
////        LoginButton loginfbbutton = (LoginButton)findViewById(R.id.fb_login_btn);
////        loginButton.setReadPermissions("email");
//////        callbackManager=CallbackManager.Factory.create();
////        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
////            @Override
////            public void onSuccess(LoginResult loginResult) {
////                Profile fbprofile= Profile.getCurrentProfile();
////                if (fbprofile != null){
////
////                    fb_id= fbprofile.getId();
////                    full_name=fbprofile.getName();
////                }
////                Toast ts = new Toast(login.this);
////                ts.makeText(login.this,"Login Success",Toast.LENGTH_SHORT);
////                ts.show();
////               // Intent intent=new Intent(login.this,p2w.class);
////                loginResult.getAccessToken().getUserId();
////                loginResult.getAccessToken().getToken();
////                GraphRequest.newMeRequest(
////                        AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
////                            @Override
////                            public void onCompleted(JSONObject me, GraphResponse response) {
////try {
////    email = me.optString("email");
////   long fb_id = me.getLong("id");
////
////    Intent intent=new Intent(login.this,p2w.class);
////    intent.putExtra("useremail",email);
////    startActivity(intent);
////    finish();
////} catch (JSONException e) {
////    e.printStackTrace();
////}
//////                                if (response.getError() != null) {
//////                                    // handle errorlog
//////                                    Log.e("fberror", "onCompleted:"+ response.getError().getErrorMessage() );
//////                                } else {
//////                                    String email = me.optString("email");
//////                                    Log.e("fbemail", "onCompleted:"+email );
//////
//////                                }
////                               /* Intent intent=new Intent(login.this,p2w.class);
////                                startActivity(intent);*/
////                            }
////                        }).executeAsync();
////               // startActivity(intent);
////            }
////
////            @Override
////            public void onCancel() {
////
////                AlertDialog.Builder builder1 = new AlertDialog.Builder(login.this);
////                builder1.setMessage("Facebook Login process Cancelled");
////                builder1.setCancelable(true);
////
////                builder1.setPositiveButton(
////                        "Try Again",
////                        new DialogInterface.OnClickListener() {
////                            public void onClick(DialogInterface dialog, int id) {
////                                dialog.cancel();
////                            }
////                        });
////
////                builder1.setNegativeButton(
////                        "Cancel",
////                        new DialogInterface.OnClickListener() {
////                            public void onClick(DialogInterface dialog, int id) {
////                                dialog.cancel();
////                            }
////                        });
////
////                AlertDialog alert11 = builder1.create();
////                alert11.show();
////            }
////
////            @Override
////            public void onError(FacebookException error) {
////                Log.e("FBError", "onError: "+ error );
////            }
////        });
//////        loginfbbutton.setOnClickListener(new View.OnClickListener() {
//////            @Override
//////            public void onClick(View v) {
//////
//////            }
//////        });
//////        textView=(TextView)findViewById(R.id.textView);
////
////        callbackManager=CallbackManager.Factory.create();
////    }
////
////    @Override
////    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        // super.onActivityResult(requestCode, resultCode, data);
////        callbackManager.onActivityResult(requestCode,resultCode,data);
////    }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       // FacebookSdk.sdkInitialize(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        scheduleid = extras.getString("SCHEDULEID");

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        callbackManager = CallbackManager.Factory.create();

    LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_btn);
    final TextView textView = (TextView) findViewById(R.id.text);
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
                                String email = object.getString("email");
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("LOGIN",email);
                                editor.apply();

                                Log.e("TAG", "onCompleted: "+email );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email");
            request.setParameters(parameters);
            request.executeAsync();

            Intent intentone = new Intent(login.this, p2w.class);
            startActivity(intentone);
            finish();

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
    email = sharedpreferences.getString("LOGIN","email");
    Log.e("COOL", "onCreate: "+ email );
    Intent intentone = new Intent(login.this, p2w.class);
    intentone.putExtra("EMAIL",email);
    intentone.putExtra("SCHEDULEID",scheduleid);
    startActivity(intentone);
    finish();

}

}
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