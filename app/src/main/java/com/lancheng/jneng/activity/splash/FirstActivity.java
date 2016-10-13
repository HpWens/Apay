package com.lancheng.jneng.activity.splash;
import android.os.Bundle;
import com.lancheng.jneng.activity.BaseActivity;

/**
 * 判断是否需要进入引导页面
 */
public class FirstActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String values = this.getSharedValue(this, "splashvalues", "-1");
        if (values.equals("-1")) {
            intentActivity(FirstActivity.this, GuideActivity.class, true);
        } else {
            intentActivity(FirstActivity.this, WelcomActivity.class, true);
        }

    }


}
