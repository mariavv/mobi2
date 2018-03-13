package com.exsample.maria.mobi2.mvp.present;

import android.app.Activity;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.exsample.maria.mobi2.R;
import com.exsample.maria.mobi2.manager.AuthManager;
import com.exsample.maria.mobi2.mvp.view.AuthView;

/**
 * Created by maria on 24.02.2018
 */

@InjectViewState
public class AuthPresenter extends MvpPresenter<AuthView> implements AuthManager.AuthManagerWatcher {

    private static final int MIN_LENGTH = 8;

    public void regBtnPressed(final String email, final String pass) {
        (new AuthManager(this)).register(email, pass);
    }

    private void login(String email, String pass) {
        (new AuthManager(this)).signIn(email, pass);
    }

    public void loginBtnPressed(String email, String pass) {
        login(email, pass);
    }

    public void textChanged(int emailLen, int loginLen) {
        if (isAuthParamsCorrect(emailLen, loginLen)) {
            getViewState().setUpLoginBtn(R.string.auth_btn_text_login,
                    R.color.colorAuthLoginBtnGreen, true);
        } else {
            getViewState().setUpLoginBtn(R.string.auth_btn_text_not_fill,
                    R.color.colorAuthLoginBtn, false);
        }
    }

    private boolean isAuthParamsCorrect(int emailLen, int loginLen) {
        return emailLen >= MIN_LENGTH && loginLen >= MIN_LENGTH;
    }

    public void backBtnPressed() {
        getViewState().close(Activity.RESULT_CANCELED);
    }

    @Override
    public void error(String message) {
        getViewState().say(message);
    }

    @Override
    public void signInSuccessful() {
        getViewState().close(Activity.RESULT_OK);
    }

    @Override
    public void signOutSuccessful() {

    }
}
