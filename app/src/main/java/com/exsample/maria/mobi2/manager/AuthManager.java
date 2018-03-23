package com.exsample.maria.mobi2.manager;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by maria on 10.03.2018
 */

public class AuthManager {
    private SignInListener signInListener;
    private SignOutListener signOutListener;

    public interface AuthErrorListener {
        void error(String message);
    }

    public interface Listener extends SignInListener, SignOutListener {
    }

    public interface SignInListener extends AuthErrorListener {
        void signInSuccessful();
    }

    public interface SignOutListener  extends AuthErrorListener{
        void signOutSuccessful();
    }

    public AuthManager(Listener listener) {
        this.signInListener = listener;
        this.signOutListener = listener;
    }

    public AuthManager(SignInListener signInListener) {
        this.signInListener = signInListener;
    }

    public AuthManager(SignOutListener signOutListener) {
        this.signOutListener = signOutListener;
    }

    private static FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static boolean userExists() {
        return getCurrentUser() != null;
    }

    public static String getUserId() {
        if (userExists()) {
            return getCurrentUser().getUid();
        } else {
            return null;
        }
    }

    public void signOut(final Context context) {
        if (getCurrentUser() != null) {
            AuthUI
                    .getInstance()
                    .signOut(context)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                signOutListener.signOutSuccessful();
                            } else {
                                if (task.getException() != null) {
                                    signOutListener.error(task.getException().getMessage());
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
            signInListener.error(task.getException().getMessage());
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

    public static String getEmail() {
        if (userExists()) {
            return getCurrentUser().getEmail();
        }
        return null;
    }

    public static String getDisplayName() {
        if (userExists()) {
            return getCurrentUser().getDisplayName();
        }
        return null;
    }

    public static String getPhoneNumber() {
        if (userExists()) {
            return getCurrentUser().getPhoneNumber();
        }
        return null;
    }

    public static void updateUser(String email, String password) {
        getCurrentUser().updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
        if (password != null && password.length() > 3){
            getCurrentUser().updatePassword(password).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }
}
