package com.exsample.maria.mobi2.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.exsample.maria.mobi2.R;
import com.exsample.maria.mobi2.present.MainPresenter;


public class MainActivity extends AppCompatActivity {

    private TextView helloView;

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        presenter = new MainPresenter();
        presenter.onCreateActivity(MainActivity.this);
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
                        presenter.signInBtnPressed(MainActivity.this);
                        break;
                    case R.id.signOutBtn:
                        presenter.signOutBtnPressed(MainActivity.this);
                        break;
                    case R.id.profileBtn:
                        presenter.profileBtnPressed(MainActivity.this);
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
        presenter.activityResult(MainActivity.this, requestCode, resultCode, data);
    }

    public void sayHi(String greeting) {
        helloView.setText(greeting);
    }

    public void sayHi(int greeting) {
        helloView.setText(greeting);
    }

    public void showError(String errMessage) {
        Toast.makeText(MainActivity.this, errMessage, Toast.LENGTH_SHORT).show();
    }
}