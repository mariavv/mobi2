package com.exsample.maria.mobi2.present;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.exsample.maria.mobi2.R;
import com.exsample.maria.mobi2.ui.AuthActivity;
import com.exsample.maria.mobi2.ui.MainActivity;
import com.exsample.maria.mobi2.ui.ProfileActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by maria on 28.02.2018
 */

public class MainPresenter {

    private static final int SIGN_IN = 11;

    private void changeText(MainActivity activity) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            activity.sayHi(String.format(activity.getString(R.string.hello_user), user.getEmail()));
        }
    }

    public void signOutBtnPressed(MainActivity activity) {
        signOut(activity);
    }

    private void signOut(final MainActivity activity) {
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
                                if (task.getException() != null) {
                                    activity.showError(task.getException().getMessage());
                                }
                            }
                        }
                    });
        }
    }

    public void signInBtnPressed(MainActivity activity) {
        signOut(activity);
        activity.startActivityForResult(AuthActivity.start(activity), SIGN_IN);
    }

    @SuppressLint("RestrictedApi")
    public void activityResult(MainActivity activity, int requestCode, int resultCode, Intent data) {
        if (requestCode == SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == Activity.RESULT_OK) {
                changeText(activity);
            } else {
                if (response != null) {
                    if (response.getException() != null) {
                        activity.showError(response.getException().getMessage());
                    }
                }
            }
        }
    }

    public void onCreateActivity(MainActivity activity) {
        changeText(activity);
    }

    public void profileBtnPressed(MainActivity activity) {
        activity.startActivity(ProfileActivity.start(activity));
    }
}
