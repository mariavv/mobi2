package com.exsample.maria.mobi2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthActivity extends AppCompatActivity {

    ScrollView scrollView;
    EditText emailEd;
    EditText passEd;
    Button regBtn;
    Button loginBtn;

    public static Intent start(Context context) {
        return new Intent(context, AuthActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        scrollView = findViewById(R.id.scrollView);
        emailEd = findViewById(R.id.enter_email);
        passEd = findViewById(R.id.enter_pass);
        regBtn = findViewById(R.id.regBtn);
        loginBtn = findViewById(R.id.loginBtn);

        //TODO del
        checkBtnEnabled();

        emailEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //scrollView.scrollTo(0,0);

            }
        });

        emailEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkBtnEnabled();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        passEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkBtnEnabled();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailEd.getText().toString(),
                        passEd.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(emailEd.getText().toString(),
                                    passEd.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        success();
                                    } else {
                                        showError(task.getException().getMessage());
                                    }
                                }
                            });
                        } else {
                            showError(task.getException().getMessage());
                        }
                    }
                });
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailEd.getText().toString(),
                        passEd.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            success();
                        } else {
                            showError(task.getException().getMessage());
                        }
                    }
                });
            }
        });
    }

    private void success() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

    private void checkBtnEnabled() {
        if (emailEd.getText().length() > 0 && passEd.getText().length() > 0) {
            loginBtn.setText(R.string.auth_btn_text_login);
            loginBtn.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            loginBtn.setEnabled(true);
            regBtn.setEnabled(true);
        } else if (emailEd.getText().length() == 0 || passEd.getText().length() == 0) {
            loginBtn.setText(R.string.auth_btn_text_not_fill);
            loginBtn.setBackgroundColor(getResources().getColor(R.color.colorLoginBtn));
            loginBtn.setEnabled(false);
            regBtn.setEnabled(false);
        }
    }

    private void showError(String message) {
        Toast.makeText(AuthActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // как называется эта кнопка?
        if (item.getItemId() == 16908332) {
            setResult(RESULT_CANCELED, new Intent());
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
