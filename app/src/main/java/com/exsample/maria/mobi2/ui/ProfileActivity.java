package com.exsample.maria.mobi2.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.exsample.maria.mobi2.R;
import com.exsample.maria.mobi2.mvp.present.ProfilePresenter;
import com.exsample.maria.mobi2.mvp.view.ProfileView;

public class ProfileActivity extends MvpAppCompatActivity implements ProfileView {

    private EditText emailEd;
    private TextView displayNameEd;
    private TextView phoneNumberEd;

    @InjectPresenter
    ProfilePresenter presenter;

    public static Intent start(Context context) {
        return new Intent(context, ProfileActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        presenter.onActivityCreate();
    }

    private void initViews() {
        emailEd = findViewById(R.id.emailEd);
        displayNameEd = findViewById(R.id.displayNameEd);
        phoneNumberEd = findViewById(R.id.phoneNumberEd);
        Button saveBtn = findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSaveBtnPressed(emailEd.getText().toString(),
                        displayNameEd.getText().toString(),
                        phoneNumberEd.getText().toString());
            }
        });
    }

    @Override
    public void fillFields(String email, String displayName, String phoneNumber) {
        emailEd.setText(email);
        displayNameEd.setText(displayName);
        phoneNumberEd.setText(phoneNumber);
    }

    @Override
    public void say(int messageRes) {
        Toast.makeText(this, getString(messageRes), Toast.LENGTH_SHORT).show();
    }
}
