package com.exsample.maria.mobi2.present;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.exsample.maria.mobi2.R;
import com.exsample.maria.mobi2.ui.AuthActivity;
import com.exsample.maria.mobi2.ui.MainActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by maria on 28.02.2018.
 */

public class MainPresenter {

    private static final int AUTH_ACTIVITY = 11;

    private MainActivity activity;

    public MainPresenter(MainActivity activity) {
        this.activity = activity;
    }

    private void changeText() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            activity.sayHi(String.format(activity.getString(R.string.hello_user), user.getEmail()));
        }
    }

    public void signOutBtnPressed() {
        signOut();
    }

    private void signOut() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            AuthUI.getInstance()
                    .signOut(activity)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                                    activity.sayHi(R.string.hello_world);
                                }
                            } else {
                                activity.showError(task.getException().getMessage());
                            }
                        }
                    });
        }
    }

    public void signInBtnPressed() {
        signOut();
        activity.startActivityForResult(AuthActivity.start(activity), AUTH_ACTIVITY);
    }

    @SuppressLint("RestrictedApi")
    public void activityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTH_ACTIVITY) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == Activity.RESULT_OK) {
                changeText();
            } else {
                if (response != null) {
                    activity.showError(response.getException().getMessage());
                }
            }
        }
    }

    public void onCreateActivity() {
        changeText();
    }
}
