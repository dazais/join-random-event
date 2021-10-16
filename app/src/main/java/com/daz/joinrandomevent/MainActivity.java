package com.daz.joinrandomevent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoginStatusCallback;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    // Statics
    private static final String EMAIL = "email";
    private static final String TAG = "MainActivity";


    CallbackManager callbackManager;
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkLoginStatus();
    }

    private void checkLoginStatus(){
        LoginManager.getInstance().retrieveLoginStatus(this, new LoginStatusCallback() {
            @Override
            public void onCompleted(AccessToken accessToken) {
                // User was previously logged in, can log them in directly here.
                // If this callback is called, a popup notification appears that says
                // "Logged in as <User Name>"

                //AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

                if (isLoggedIn) {
                    // already logged in
                    Log.d(TAG, "Logged in");
                    displayHomePage(accessToken);
               }
            }
            @Override
            public void onFailure() {
                // No access token could be retrieved for the user : Set up FB connection page
                setContentView(R.layout.fb_connect);
                facebookLogin();
            }
            @Override
            public void onError(Exception exception) {
                Log.e(TAG, "Error during FB login");
            }
        });
    }

    private void facebookLogin(){
        LoginManager loginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();

        loginButton = findViewById(R.id.login_button);
        loginButton.setPermissions(Arrays.asList(EMAIL,LOCATION_SERVICE,USER_SERVICE));


        // Callback registration
        loginManager.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        displayHomePage(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
        });

    }

    private void displayHomePage(AccessToken accessToken){
        setContentView(R.layout.my_events);
        getUserEvents(accessToken);
    }

    private void getUserEvents(AccessToken accessToken) {
        ArrayList data

    // code applicatif
    // from FB Graph API tool
    GraphRequest request = GraphRequest.newMeRequest(
            accessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.d(TAG, response.toString());
                    //data =
                }
            });

    Bundle parameters = new Bundle();
    parameters.putString("fields", "id,name");
    request.setParameters(parameters);



//                        GraphRequest request = GraphRequest.newMeRequest(
//
//                                loginResult.getAccessToken(),
//
//                                new GraphRequest.GraphJSONObjectCallback() {
//
//                                    @Override
//                                    public void onCompleted(JSONObject object,
//                                                            GraphResponse response)
//                                    {
//
//                                        if (object != null) {
//                                            try {
//                                                String name = object.getString("name");
//                                                String email = object.getString("email");
//                                                String fbUserID = object.getString("id");
//
//                                                // disconnectFromFacebook();
//
//                                                // do action after Facebook login success
//                                                // or call your API
//                                            }
//                                            catch (JSONException | NullPointerException e) {
//                                                e.printStackTrace();
//                                            }
//                                        }
//                                    }
//                                });
    Log.d(TAG, request.toString());
    request.executeAsync();


}

    private void setUpListeners() {
            loginButton.setOnClickListener(new ButtonOnClickListener(loginButton, callbackManager));
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        }


    @Override
    // Forward activityResult to the callbackManager
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


}