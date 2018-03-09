package com.exsample.maria.mobi2.manager;

import android.support.annotation.NonNull;

import com.exsample.maria.mobi2.R;
import com.exsample.maria.mobi2.mvp.present.i.IAuthPresenter;
import com.exsample.maria.mobi2.mvp.present.i.IMapPresenter;
import com.exsample.maria.mobi2.ui.activities.MapActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by maria on 10.03.2018
 */

public class AuthManager {
    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public boolean userExists() {
        return getCurrentUser() != null;
    }

    public String getEmail() {
        return getCurrentUser().getEmail();
    }

    public void signOut(final IMapPresenter presenter, final MapActivity activity) {
        if (getCurrentUser() != null) {
            AuthUI.getInstance()
                    .signOut(activity)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                presenter.changeText(activity, R.string.hello_world);
                            } else {
                                if (task.getException() != null) {
                                    presenter.error(task.getException().getMessage());
                                }
                            }
                        }
                    });
        }
    }

    public void register(final IAuthPresenter presenter, final String email, final String pass) {
        FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            signIn(presenter, email, pass);
                        } else {
                            showError(presenter, task);
                        }
                    }
                });
    }

    private void showError(IAuthPresenter presenter, Task<AuthResult> task) {
        if (task.getException() != null) {
            presenter.error(task.getException().getMessage());
        }
    }

    public void signIn(final IAuthPresenter presenter, String email, String pass) {
        FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            presenter.signInSuccessful();
                        } else {
                            showError(presenter, task);
                        }
                    }
                });
    }
}
