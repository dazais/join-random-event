package com.daz.joinrandomevent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {
    // Statics
    private static final String EMAIL = "email";
    private static final String TAG = "MainActivity";
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<Event> data;
    private static ArrayList<Integer> removedItems;



    CallbackManager callbackManager;
    LoginButton loginButton, eventsDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialisation du LayoutManager
        layoutManager = new LinearLayoutManager(this);

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
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));



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
        ArrayList userEvents = new ArrayList();


    // code applicatif
    // from FB Graph API tool
    GraphRequest request = GraphRequest.newMeRequest(
            accessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    Log.d(TAG, response.toString());

                    if (object != null) {
                        try {
                            String name = object.getString("name");
                            String fbUserID = object.getString("id");
                            String userEvents = object.getString("events");

                            // Display results
                            //eventsDisplay = findViewById(R.id.events);

                            recyclerView = (RecyclerView) findViewById(R.id.events_recycler_view);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());

                            data = new ArrayList<Event>();
                            for (int i = 0; i < MyData.nameArray.length; i++) {
                                data.add(new Event(
                                        MyData.nameArray[i],
                                        MyData.versionArray[i],
                                        MyData.id_[i]
                                        //MyData.drawableArray[i]
                                ));
                            }

                            adapter = new CustomAdapter(data);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException | NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    // Define request parameters
    Bundle parameters = new Bundle();
    parameters.putString("fields", "id,name,events");
    request.setParameters(parameters);
    Log.d(TAG, request.toString());
    request.executeAsync();

    // test avec Spring Social
//    Facebook facebook = new FacebookTemplate(accessToken.getToken());
//    String email = facebook.userOperations().getUserProfile().getEmail();
//    Log.d(TAG, "Retrieved user email using Spring Social: "+email);

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


//
//    private static class MyOnClickListener implements View.OnClickListener {
//
//        private final Context context;
//
//        private MyOnClickListener(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        public void onClick(View v) {
//            removeItem(v);
//        }
//
//        private void removeItem(View v) {
//            int selectedItemPosition = recyclerView.getChildPosition(v);
//            RecyclerView.ViewHolder viewHolder
//                    = recyclerView.findViewHolderForPosition(selectedItemPosition);
//            TextView textViewName
//                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewName);
//            String selectedName = (String) textViewName.getText();
//            int selectedItemId = -1;
//            for (int i = 0; i < MyData.nameArray.length; i++) {
//                if (selectedName.equals(MyData.nameArray[i])) {
//                    selectedItemId = MyData.id_[i];
//                }
//            }
//            removedItems.add(selectedItemId);
//            data.remove(selectedItemPosition);
//            adapter.notifyItemRemoved(selectedItemPosition);
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        //getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        super.onOptionsItemSelected(item);
////        if (item.getItemId() == R.id.add_item) {
////            //check if any items to add
////            if (removedItems.size() != 0) {
////                addRemovedItemToList();
////            } else {
////                Toast.makeText(this, "Nothing to add", Toast.LENGTH_SHORT).show();
////            }
////        }
//        return true;
//    }
//
//    private void addRemovedItemToList() {
//        int addItemAtListPosition = 3;
//        data.add(addItemAtListPosition, new Event(
//                MyData.nameArray[removedItems.get(0)],
//                MyData.versionArray[removedItems.get(0)],
//                MyData.id_[removedItems.get(0)]
//                //MyData.drawableArray[removedItems.get(0)]
//        ));
//        adapter.notifyItemInserted(addItemAtListPosition);
//        removedItems.remove(0);
//    }
//


}