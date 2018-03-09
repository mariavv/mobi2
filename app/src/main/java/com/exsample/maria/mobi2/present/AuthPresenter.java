package com.exsample.maria.mobi2.present;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.exsample.maria.mobi2.R;
import com.exsample.maria.mobi2.view.AuthView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by maria on 24.02.2018
 */

@InjectViewState
public class AuthPresenter extends MvpPresenter<AuthView> {

    private static final int MIN_LENGTH = 8;

    public void regBtnPressed(final String email, final String pass) {
        FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            login(email, pass);
                        } else {
                            showError(task);
                        }
                    }
                });
    }

    private void showError(Task<AuthResult> task) {
        if (task.getException() != null) {
            getViewState().showError(task.getException().getMessage());
        }
    }

    private void login(String email, String pass) {
        FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            getViewState().close(Activity.RESULT_OK);
                        } else {
                            showError(task);
                        }
                    }
                });
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
}
