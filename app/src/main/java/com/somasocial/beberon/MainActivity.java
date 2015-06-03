package com.somasocial.beberon;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import android.media.tv.TvInputService.Session;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

//import android.content.pm.PackageInstaller.Session;


public class MainActivity extends ActionBarActivity {

    LoginButton loginButton;
    TextView message;
    CallbackManager callbackManager;
    ProfileTracker profileTracker;
    private AccessTokenTracker accessTokenTracker;
    private boolean isResumed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());



//        LoginManager.getInstance().logOut();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       final AccessToken currentAccessToken) {
                Log.d("test","accessTokenTracker oldAccessToken:"+oldAccessToken);
                Log.d("test","accessTokenTracker currentAccessToken:"+currentAccessToken);
                if (currentAccessToken == null){
                    message.setText("Simon says login! ");
                }
                else {
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,last_name,link,email,picture");

                    GraphRequest request = GraphRequest.newMeRequest(currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                            String id = null;
                            String name = "";
                            Log.d("test", "LoginManager onSuccess json:" + jsonObject);
                            Log.d("test", "LoginManager onSuccess graphResponse:" + graphResponse);
                            if (jsonObject != null) {
                                try {
                                    id = jsonObject.getString("id");
                                    name = jsonObject.getString("name");
                                    message.setText("Logged in as " + name);
                                    Log.d("test","permissions:"+currentAccessToken.getPermissions());
//                                    LoginManager.getInstance().logInWithPublishPermissions(
//                                            MainActivity.this,
//                                            Arrays.asList("publish_actions"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    request.setParameters(parameters);
                    request.executeAsync();

                }
                if (isResumed) {
                    Log.d("test","accessTokenTracker isResumed");
                }
            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                Log.d("test","currentProfile:"+currentProfile);
                Log.d("test","oldProfile:"+oldProfile);
                if (oldProfile!=null)
                {
                    Log.d("test","descr  oldProfile:"+oldProfile.describeContents());
                }
                if (currentProfile!=null)
                {
                    Log.d("test","descr currentProfile:"+currentProfile.describeContents());
                }
//
//                Log.d("test","currentProfile:"+currentProfile.describeContents());

                // App code
            }
        };
//        loginButton.setReadPermissions("user_friends", "email");
       // Callback registration
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                // App code
//                Log.d("test", "loginButton onSuccess:" + loginResult);
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//                Log.d("test", "loginButton onCancel:");
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                // App code
//                Log.d("test", "loginButton onException:" + exception);
//            }
//        });
        setContentView(R.layout.activity_main);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        message = (TextView) findViewById(R.id.message);

//
//        if(AccessToken.getCurrentAccessToken()!=null)
//        {
//            Log.v("User is login","YES");
//
//        }
//        else
//        {
//            Log.v("User is not login","OK");
//            LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, (Arrays.asList("public_profile", "user_friends","user_birthday","user_about_me","email")));
//        }

//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email", "user_friends"));
    }

    public void GoLogin(View v)
    {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.d("test", "LoginManager onSuccess:" + loginResult.getAccessToken());
                        Log.d("test", "LoginManager onSuccess:" + loginResult.getRecentlyDeniedPermissions());
                        Log.d("test", "LoginManager onSuccess:" + loginResult.getRecentlyGrantedPermissions());
//                        message.setText("Logged in as pules!");
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,last_name,link,email,picture");

                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                                String id = null;
                                String name = "";
                                Log.d("test", "LoginManager onSuccess json:" + jsonObject);
                                Log.d("test", "LoginManager onSuccess graphResponse:" + graphResponse);
                                if (jsonObject != null) {
                                    try {
                                        id = jsonObject.getString("id");
                                        name = jsonObject.getString("name");
                                        message.setText("Logged in as " + name);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.d("test", "LoginManager onCancel:");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.d("test", "LoginManager onException:" + exception);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d("test", "onActivityResult requestCode:" + requestCode);
        Log.d("test", "onActivityResult resultCode:" + resultCode);
        Log.d("test", "onActivityResult data:" + data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("test","onresume");
        isResumed = true;
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("test","onpause");
        isResumed = false;
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
}
