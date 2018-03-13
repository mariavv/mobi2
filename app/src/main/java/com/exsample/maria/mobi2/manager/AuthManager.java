package com.exsample.maria.mobi2.manager;

import android.support.annotation.NonNull;

import com.exsample.maria.mobi2.ui.MapActivity;
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
    private AuthManagerWatcher watcher;

    public interface AuthManagerWatcher {
        void signInSuccessful();

        void signOutSuccessful();

        void error(String message);
    }
    public interface i {
        void signInSuccessful();
    }

    public AuthManager(AuthManagerWatcher watcher) {
        this.watcher = watcher;
    }

    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public boolean userExists() {
        return getCurrentUser() != null;
    }

    public void signOut(final MapActivity activity) {
        if (getCurrentUser() != null) {
            AuthUI.getInstance()
                    .signOut(activity)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                watcher.signOutSuccessful();
                            } else {
                                if (task.getException() != null) {
                                    watcher.error(task.getException().getMessage());
                                }
                            }
                        }
                    });
        }
    }

    public void register(final String email, final String pass) {
        FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            signIn(email, pass);
                        } else {
                            showError(task);
                        }
                    }
                });
    }

    private void showError(Task<AuthResult> task) {
        if (task.getException() != null) {
            watcher.error(task.getException().getMessage());
        }
    }

    public void signIn(String email, String pass) {
        FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            watcher.signInSuccessful();
                        } else {
                            showError(task);
                        }
                    }
                });
    }

    public String getEmail() {
        if (userExists()) {
            return getCurrentUser().getEmail();
        }
        return null;
    }

    public String getDisplayName() {
        if (userExists()) {
            return getCurrentUser().getDisplayName();
        }
        return null;
    }

    public String getPhoneNumber() {
        if (userExists()) {
            return getCurrentUser().getPhoneNumber();
        }
        return null;
    }

    public void updateEmail(String email) {
        getCurrentUser().updateEmail(email);
    }
}
