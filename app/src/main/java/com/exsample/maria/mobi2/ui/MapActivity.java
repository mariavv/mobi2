package com.exsample.maria.mobi2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.exsample.maria.mobi2.R;
import com.exsample.maria.mobi2.mvp.present.MapPresenter;
import com.exsample.maria.mobi2.mvp.view.MapView;


public class MapActivity extends MvpAppCompatActivity implements MapView {

    private TextView helloView;

    @InjectPresenter
    MapPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        presenter.onActivityCreate(R.string.hello_world);
    }

    private void initViews() {
        helloView = findViewById(R.id.helloView);
        Button signOutBtn = findViewById(R.id.signOutBtn);
        Button signInBtn = findViewById(R.id.signInBtn);
        Button profileBtn = findViewById(R.id.profileBtn);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.signInBtn:
                        presenter.signInBtnPressed(MapActivity.this);
                        break;
                    case R.id.signOutBtn:
                        presenter.signOutBtnPressed(MapActivity.this);
                        break;
                    case R.id.profileBtn:
                        presenter.profileBtnPressed();
                        break;
                }
            }
        };

        signInBtn.setOnClickListener(onClickListener);
        signOutBtn.setOnClickListener(onClickListener);
        profileBtn.setOnClickListener(onClickListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.activityResult(requestCode, resultCode, data);
    }

    @Override
    public void changeText(String user) {
        helloView.setText(String.format(getString(R.string.hello_user), user));
    }

    @Override
    public void changeText(int greetingRes) {
        helloView.setText(greetingRes);
    }

    @Override
    public void say(String message) {
        Toast.makeText(MapActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startAuthActivity(final int reguestCode) {
        startActivityForResult(AuthActivity.start(this), reguestCode);
    }

    @Override
    public void startProfileActivity() {
        startActivity(ProfileActivity.start(this));
    }
}