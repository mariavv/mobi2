package com.exsample.maria.mobi2.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.exsample.maria.mobi2.R;
import com.exsample.maria.mobi2.present.ProfilePresenter;

public class ProfileActivity extends AppCompatActivity {

    ProfilePresenter presenter;

    public static Intent start(Context context) {
        return new Intent(context, ProfileActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        presenter = new ProfilePresenter();
    }
}
