package com.lancheng.jneng.activity.splash;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.lancheng.jneng.R;
import com.lancheng.jneng.activity.BaseActivity;
import com.lancheng.jneng.activity.MainActivity;

public class WelcomActivity extends BaseActivity {
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Intent intent = new Intent(WelcomActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }

        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
        handler.sendEmptyMessageDelayed(0, 1000);
    }

}
