package com.exsample.maria.mobi2.mvp.present;

import android.util.Patterns;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.exsample.maria.mobi2.R;
import com.exsample.maria.mobi2.manager.AuthManager;
import com.exsample.maria.mobi2.mvp.view.ProfileView;

/**
 * Created by maria on 01.03.2018
 */

@InjectViewState
public class ProfilePresenter extends MvpPresenter<ProfileView> {
    public void onActivityCreate() {
        AuthManager manager = new AuthManager();
        getViewState().fillFields( manager.getEmail(), manager.getDisplayName(), manager.getPhoneNumber() );
    }

    public void onSaveBtnPressed(String email, String displayName, String phoneNumber) {
        AuthManager manager = new AuthManager();
        if ( (Patterns.EMAIL_ADDRESS.matcher(email).matches()) && (displayName.length() > 0) ) {
            //manager.updateEmail(email);
            getViewState().say(R.string.saved);
        }
    }
}
