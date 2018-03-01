package com.exsample.maria.mobi2.present;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.exsample.maria.mobi2.R;
import com.exsample.maria.mobi2.ui.AuthActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by maria on 24.02.2018
 */

public class AuthPresenter {

    private static final int MIN_LENGTH = 8;

    private AuthActivity activity;

    public AuthPresenter(AuthActivity activity) {
        this.activity = activity;
    }

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
            activity.showError(task.getException().getMessage());
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
                            activity.close(Activity.RESULT_OK);
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
            activity.setUpLoginBtn(R.string.auth_btn_text_login,
                    activity.getResources().getColor(R.color.colorAuthLoginBtnGreen), true);
        } else {
            activity.setUpLoginBtn(R.string.auth_btn_text_not_fill,
                    activity.getResources().getColor(R.color.colorAuthLoginBtn), false);
        }
    }

    private boolean isAuthParamsCorrect(int emailLen, int loginLen) {
        return emailLen >= MIN_LENGTH && loginLen >= MIN_LENGTH;
    }

    public void backBtnPressed() {
        activity.close(Activity.RESULT_CANCELED);
    }
}
