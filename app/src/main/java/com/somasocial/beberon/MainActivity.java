package com.somasocial.beberon;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import android.widget.BaseAdapter;
import android.media.tv.TvInputService.Session;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

//import android.content.pm.PackageInstaller.Session;


public class MainActivity extends BaseActivity {

    LoginButton loginButton;
    TextView message;
    CallbackManager callbackManager;
    ProfileTracker profileTracker;
    private AccessTokenTracker accessTokenTracker;
    private boolean isResumed = false;
    private DrawerLayout drawer;
    ListView mDrawerList;
    String[] mDrawerListItems;
    ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        loginButton = (LoginButton) findViewById(R.id.login_button);
        message = (TextView) findViewById(R.id.message);
        Log.d("tesat","oncreate");
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
        setActionBarIcon(R.drawable.ic_action_menu);
        callbackManager = CallbackManager.Factory.create();
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


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);

        if (toolbar != null) {


        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);

//        setContentView(R.layout.activity_login);

//        mDrawerList = (ListView)findViewById(android.R.id.list);
//        mDrawerListItems = getResources().getStringArray(R.array.drawer_list);
//        mDrawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDrawerListItems));
//        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                int editedPosition = position+1;
//                Toast.makeText(MainActivity.this, "You selected item " + editedPosition, Toast.LENGTH_SHORT).show();
//                drawer.closeDrawer(mDrawerList);
//            }
//        });
//        mDrawerToggle = new ActionBarDrawerToggle(this,
//                drawer,
//                toolbar,
//                R.string.drawer_open,
//                R.string.drawer_close){
//            public void onDrawerClosed(View v){
//                super.onDrawerClosed(v);
//                invalidateOptionsMenu();
//                syncState();
//            }
//            public void onDrawerOpened(View v){
//                super.onDrawerOpened(v);
//                invalidateOptionsMenu();
//                syncState();
//            }
//        };
//        drawer.setDrawerListener(mDrawerToggle);
//
//        mDrawerToggle.syncState();
 }

    @Override protected int getLayoutResource() {
        return R.layout.activity_main;
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
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(Gravity.START);
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
        accessTokenTracker.startTracking();
        Log.d("test","accessTokenTracker.isTracking:"+accessTokenTracker.isTracking());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("test","onpause");
        isResumed = false;
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    public void testBulb(View v)
    {
        Log.d("test","test bulb");
    }

    public  void testClick(View v)
    {
        Log.d("test","test clicked");
    }
}
