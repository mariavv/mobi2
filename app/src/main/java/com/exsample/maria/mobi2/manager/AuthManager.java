package com.exsample.maria.mobi2.manager;

import android.content.Context;
import android.support.annotation.NonNull;

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
    private Listener listener;
    private SignInListener signInListener;
    private SignOutListener signOutListener;

    public interface Listener {
        void error(String message);
    }

    public interface SignInListener extends Listener {
        void signInSuccessful();

        void error(String message);
    }

    public interface SignOutListener extends Listener {
        void signOutSuccessful();

        void error(String message);
    }

    public AuthManager(Listener listener) {
        this.listener = listener;
    }

    public AuthManager(SignInListener signInListener) {
        this.signInListener = signInListener;
    }

    public AuthManager(SignOutListener signOutListener) {
        this.signOutListener = signOutListener;
    }

    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public boolean userExists() {
        return getCurrentUser() != null;
    }

    public void signOut(final Context context) {
        if (getCurrentUser() != null) {
            AuthUI.getInstance()
                    .signOut(context)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                signOutListener.signOutSuccessful();
                            } else {
                                if (task.getException() != null) {
                                    listener.error(task.getException().getMessage());
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
            listener.error(task.getException().getMessage());
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
                            signInListener.signInSuccessful();
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
