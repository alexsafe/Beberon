package com.somasocial.beberon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
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
import com.somasocial.utils.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;
//import com.facebook.login.widget.ProfilePictureView;

/**
 * Created by SOMA on 16/06/15.
 */
public class FaceBookLoginActivity  extends Activity{
    private CallbackManager callbackManager;
    private PendingAction pendingAction = PendingAction.NONE;
    private ProfilePictureView profilePictureView;
    private TextView greeting;
    private ProfileTracker profileTracker;
    SharedPreferences sharedPrefs;
    private final String PENDING_ACTION_BUNDLE_KEY =
            "com.somasocial.beberon:PendingAction";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        sharedPrefs = getSharedPreferences("fbDetails", MODE_PRIVATE);
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
//                        handlePendingAction();
                        Log.d("test", "LoginManager onSuccess");
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,last_name,link,email,picture");

                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                                String id = null;
                                String name = "";
                                Log.d("test", "LoginManager onSuccess json:" + jsonObject);
                                Log.d("test", "LoginManager onSuccess graphResponse:" + graphResponse);
//                                if (jsonObject != null) {
//                                    try {
//                                        id = jsonObject.getString("id");
//                                        name = jsonObject.getString("name");
//                                        message.setText("Logged in as " + name);
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
                            }
                        });
                        request.setParameters(parameters);
                        request.executeAsync();
                        updateUI();

                    }

                    @Override
                    public void onCancel() {
                        Log.d("test", "LoginManager onCancel");
                        if (pendingAction != PendingAction.NONE) {
                            showAlert();
                            pendingAction = PendingAction.NONE;
                        }
                        updateUI();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d("test", "LoginManager onError");
                        if (pendingAction != PendingAction.NONE
                                && exception instanceof FacebookAuthorizationException) {
                            showAlert();
                            pendingAction = PendingAction.NONE;
                        }
                        updateUI();
                    }

                    private void showAlert() {
                        new AlertDialog.Builder(FaceBookLoginActivity.this)
                                .setTitle(R.string.cancelled)
                                .setMessage(R.string.permission_not_granted)
                                .setPositiveButton(R.string.ok, null)
                                .show();
                    }
                });
        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }

        setContentView(R.layout.activity_facebook_login);

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                updateUI();
                // It's possible that we were waiting for Profile to be populated in order to
                // post a status update.
                handlePendingAction();
            }
        };
        profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
        greeting = (TextView) findViewById(R.id.greeting);
    }


    private void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;
        // These actions may re-set pendingAction if they are still pending, but we assume they
        // will succeed.
        pendingAction = PendingAction.NONE;

        switch (previouslyPendingAction) {
            case NONE:
                break;
        }
    }

    private void updateUI() {


        boolean enableButtons = AccessToken.getCurrentAccessToken() != null;

        Profile profile = Profile.getCurrentProfile();
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("name", profile.getName());
        editor.putString("id", profile.getId());

        editor.commit();
        if (enableButtons && profile != null) {
            profilePictureView.setProfileId(profile.getId());
            greeting.setText(getString(R.string.hello_user, profile.getFirstName()));
        } else {
            profilePictureView.setProfileId(null);
            greeting.setText(null);
        }

        Intent intent=new Intent(FaceBookLoginActivity.this, MainViewActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Call the 'activateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onResume methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.activateApp(this);

        updateUI();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);



    }


    @Override
    public void onPause() {
        super.onPause();

        // Call the 'deactivateApp' method to log an app event for use in analytics and advertising
        // reporting.  Do so in the onPause methods of the primary Activities that an app may be
        // launched into.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

    private enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
    }

}
