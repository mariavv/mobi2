package com.exsample.maria.mobi2.mvp.present;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.exsample.maria.mobi2.R;
import com.exsample.maria.mobi2.manager.AuthManager;
import com.exsample.maria.mobi2.mvp.present.i.IMapPresenter;
import com.exsample.maria.mobi2.ui.activities.MapActivity;
import com.exsample.maria.mobi2.mvp.view.MapView;
import com.firebase.ui.auth.IdpResponse;


/**
 * Created by maria on 28.02.2018
 */

@InjectViewState
public class MapPresenter extends MvpPresenter<MapView> implements IMapPresenter{

    private static final int SIGN_IN = 11;

    @Override
    public void changeText(MapActivity activity, int resGreeting) {
        AuthManager manager = new AuthManager();
        if (manager.userExists()) {
            getViewState().sayHi(String.format(activity.getString(resGreeting), manager.getEmail()));
        } else {
            getViewState().sayHi(activity.getString(resGreeting)/*resGreeting*/);
        }
    }

    @Override
    public void error(String message) {
        getViewState().showError(message);
    }

    public void signOutBtnPressed(MapActivity activity) {
        signOut(activity);
    }

    private void signOut(final MapActivity activity) {
        (new AuthManager()).signOut(this, activity);
    }

    public void signInBtnPressed(MapActivity activity) {
        signOut(activity);
        getViewState().startAuthActivity(SIGN_IN);
    }

    @SuppressLint("RestrictedApi")
    public void activityResult(MapActivity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == Activity.RESULT_OK) {
                changeText(activity, R.string.hello_user);
            } else {
                if (response != null) {
                    if (response.getException() != null) {
                        getViewState().showError(response.getException().getMessage());
                    }
                }
            }
        }
    }

    public void onActivityCreate(MapActivity activity, int resGreeting) {
        changeText(activity, resGreeting);
    }

    public void profileBtnPressed() {
        getViewState().startProfileActivity();
    }
}
