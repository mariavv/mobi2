package com.exsample.maria.mobi2.mvp.present;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.exsample.maria.mobi2.R;
import com.exsample.maria.mobi2.manager.AuthManager;
import com.exsample.maria.mobi2.mvp.view.MapView;
import com.firebase.ui.auth.IdpResponse;


/**
 * Created by maria on 28.02.2018
 */

@InjectViewState
public class MapPresenter extends MvpPresenter<MapView> implements AuthManager.SignOutListener {

    private static final int SIGN_IN = 11;

    private void sayHi(int resGreeting) {
        AuthManager manager = new AuthManager(this);
        if (manager.userExists()) {
            getViewState().changeText(manager.getEmail());
        } else {
            getViewState().changeText(resGreeting);
        }
    }

    @Override
    public void error(String message) {
        getViewState().say(message);
    }

    @Override
    public void signOutSuccessful() {
        sayHi(R.string.hello_world);
    }

    public void signOutBtnPressed(Context context) {
        signOut(context);
    }

    private void signOut(final Context context) {
        (new AuthManager(this)).signOut(context);
    }

    public void signInBtnPressed(Context context) {
        signOut(context);
        getViewState().startAuthActivity(SIGN_IN);
    }

    @SuppressLint("RestrictedApi")
    public void activityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == Activity.RESULT_OK) {
                sayHi(R.string.hello_user);
            } else {
                if (response != null) {
                    if (response.getException() != null) {
                        getViewState().say(response.getException().getMessage());
                    }
                }
            }
        }
    }

    public void onActivityCreate(int resGreeting) {
        sayHi(resGreeting);
    }

    public void profileBtnPressed() {
        if ((new AuthManager(this)).userExists()) {
            getViewState().startProfileActivity();
        } else {
            getViewState().say("Вы не авторизованы");
        }
    }
}
