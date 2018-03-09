package com.exsample.maria.mobi2.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.exsample.maria.mobi2.R;
import com.exsample.maria.mobi2.mvp.present.ProfilePresenter;
import com.exsample.maria.mobi2.mvp.view.ProfileView;

//@InjectViewState
public class ProfileActivity extends MvpAppCompatActivity /*implements ProfileView*/ {

  //  @InjectPresenter
    ProfilePresenter presenter;

    public static Intent start(Context context) {
        return new Intent(context, ProfileActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }
}
