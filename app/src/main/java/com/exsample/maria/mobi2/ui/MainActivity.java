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

    TextView helloView;
    Button signInBtn, signOutBtn;

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        presenter = new MainPresenter(MainActivity.this);
        presenter.onCreateActivity();
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
                        presenter.signInBtnPressed();
                        break;
                    case R.id.signOutBtn:
                        presenter.signOutBtnPressed();
                        break;
                }
            }
        };

        signInBtn.setOnClickListener(onClickListener);
        signOutBtn.setOnClickListener(onClickListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.activityResult(requestCode, resultCode, data);
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