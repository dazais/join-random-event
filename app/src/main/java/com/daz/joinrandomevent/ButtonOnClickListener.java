package com.daz.joinrandomevent;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultRegistryOwner;
import androidx.fragment.app.Fragment;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import javax.security.auth.callback.Callback;

// A transformer en Factory
// ajouter extends Fragment ?
public class ButtonOnClickListener implements View.OnClickListener {
    LoginButton b;
    CallbackManager cm;

    public ButtonOnClickListener(LoginButton loginButton, CallbackManager callbackManager) {
        b = loginButton;
        cm = callbackManager;
    }

//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//
//            View v = inflater.inflate(R.layout.fragment_start, container, false);
//
//            b.setOnClickListener(this);
//            return v;
//        }

        @Override
        // Usable for multiple buttons
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_button:
                    LoginManager.getInstance().logInWithReadPermissions((ActivityResultRegistryOwner) this, cm, Arrays.asList("public_profile"));
                    break;
            }
        }
    }

