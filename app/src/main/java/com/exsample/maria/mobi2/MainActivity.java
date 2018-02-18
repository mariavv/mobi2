package com.exsample.maria.mobi2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private static final int AUTH_ACTIVITY = 11;

    TextView helloView;
    Button signInBtn, signOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        sayHi();
    }

    private void initViews() {
        helloView = findViewById(R.id.helloView);
        signOutBtn = findViewById(R.id.signOutBtn);
        signInBtn = findViewById(R.id.signInBtn);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.signInBtn:
                        signIn();
                        break;
                    case R.id.signOutBtn:
                        signOut();
                        break;
                }
            }
        };

        signInBtn.setOnClickListener(onClickListener);
        signOutBtn.setOnClickListener(onClickListener);
    }

    private void signIn() {
        signOut();
        startActivityForResult(AuthActivity.start(MainActivity.this), AUTH_ACTIVITY);
    }

    private void signOut() {
        AuthUI.getInstance()
                .signOut(MainActivity.this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                                helloView.setText(R.string.hello_world);
                                Toast.makeText(MainActivity.this, R.string.signed_out, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTH_ACTIVITY) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                sayHi();
            } else {
                if (response != null) {
                    Toast.makeText(this, response.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void sayHi() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            helloView.setText(String.format(getString(R.string.hello_user), user.getEmail()));
            Toast.makeText(this, user.getEmail(), Toast.LENGTH_SHORT).show();
        }
    }
}