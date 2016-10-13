package com.lancheng.jneng.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lancheng.jneng.R;
import com.lancheng.jneng.base.Constant;
import com.lancheng.jneng.utils.OkHttpClientManager;
import com.lancheng.jneng.utils.RegularExpression;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AFLF on 2016/6/7. 登陆页面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private Button btnReturn;//返回按钮,
    private TextView loginRegister, loginFindPwd, loginSubmit;//立即注册，忘记密码，登陆
    private EditText loginName, loginPwd;//登录名，登陆密码
    //访问网络
    private String userName, userPwd;

    private String resultState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        resultState = getSharedValue(LoginActivity.this, "resultState", "1");
        iniview();
    }

    /**
     * 初始化控件
     */
    private void iniview() {
        //返回按钮,确定
        btnReturn = (Button) this.findViewById(R.id.login_return);
        //立即注册，忘记密码，登陆
        loginRegister = (TextView) this.findViewById(R.id.login_register);
        loginFindPwd = (TextView) this.findViewById(R.id.login_findpwd);
        loginSubmit = (TextView) this.findViewById(R.id.login_submit);
        //登录名，登陆密码
        loginName = (EditText) this.findViewById(R.id.login_edit_name);
        loginPwd = (EditText) this.findViewById(R.id.login_edit_pwd);
        intOnclick();
    }

    /**
     * 注册监听
     */
    private void intOnclick() {
        btnReturn.setOnClickListener(this);
        loginRegister.setOnClickListener(this);
        loginFindPwd.setOnClickListener(this);
        loginSubmit.setOnClickListener(this);
        setEditTextListener();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_return:
                finish();
                break;
            case R.id.login_submit:
               /* ObjectAnimator scaleY = ObjectAnimator.ofFloat(loginSubmit, "scaleY", 1f, 1.4f, 1f);
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(loginSubmit, "scaleX", 1f, 1.4f, 1f);
                AnimatorSet animSet = new AnimatorSet();
                animSet.play(scaleX).with(scaleY);
                animSet.setDuration(1000);
                animSet.start();*/
                forLogin();
                break;
            case R.id.login_register:
                intentActivity(this, HRegisterActivity.class, false);
                break;
            case R.id.login_findpwd:
                intentActivity(this, FindPasswordActivity.class, false);
                break;

        }
    }

    /**
     * 登陆的方法
     */
    private void forLogin() {
        userName = loginName.getText().toString();
        userPwd = loginPwd.getText().toString();
        RegularExpression regular = new RegularExpression();
        boolean teltype = regular.PhoneNumber(userName);
        if (!teltype) {
            showText(getString(R.string.himt_tel_errow_format));
            return;
        }
        if (userName.equals("") || userName == null) {
            showText("你好，手机号不能为空!");
            return;
        }
        if (userPwd.equals("") || userPwd == null) {
            showText("你好，密码不能为空!");
            return;
        }
       /* if (!name || !pw) {
            showText("账号或密码格式不正确！");
            return;
        }*/
        if (isConnectivity(this)) {
            showLoadingDialog();
            getLogin();
        } else {
            showText(getString(R.string.hint_intent));
        }

    }

    /**
     * 访问网络登陆
     */
    private void getLogin() {
        Map<String, String> params = new HashMap<>();
        params.put("username", userName);
        params.put("passwords", userPwd);
        OkHttpClientManager.postAsyn(Constant.LOGIN, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                closeLoadingDialog();
                showText(e.getMessage() + "");
            }

            @Override
            public void onResponse(String response) {
                closeLoadingDialog();
                Log.d("aflf", "response" + response.toString());
                try {
                    // 获取状态
                    String result = new JSONObject(response).getString("result");
                    String detail = new JSONObject(response).getString("detail");
                    String id = new JSONObject(response).getString("id");

                    if (result.equals("0")) {
                        showText(detail + "");
                        return;
                    } else if (result.equals("1")) {
                        showText(detail + "");
                        setSharedPreferences(LoginActivity.this, "userID", id);
                        //   MyApplication.setUser(id);
                        if (!resultState.equals("noMain")) {

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            int index = Integer.parseInt(resultState);
                            intent.putExtra("index", index);
                            startActivity(intent);
                        }
                        finish();
                        return;
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }, params);
    }

    /**
     * 设置文本编辑发生改变触动的事件
     */
    private void setEditTextListener() {
        // TODO Auto-generated method stub

        loginName.addTextChangedListener(new MyTextWatcher());

        loginPwd.addTextChangedListener(new MyTextWatcher());

    }


    class MyTextWatcher implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // 判断是否激活登录 按钮
            String name = loginName.getText().toString();
            String pwd = loginPwd.getText().toString();
            if (name.equals("") || name == null || pwd.equals("") || pwd == null
                    || pwd.length() < 1 || name.length() < 1) {

                loginSubmit.setBackgroundResource(R.drawable.border_cccccc_shape);
                loginSubmit.setClickable(false);

            } else {
                loginSubmit.setClickable(true);
                loginSubmit.setBackgroundResource(R.drawable.border_ffa200_shape);
                loginSubmit.setTextColor(Color.WHITE);

            }
        }

    }
//

}
