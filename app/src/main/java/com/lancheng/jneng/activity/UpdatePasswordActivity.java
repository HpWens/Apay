package com.lancheng.jneng.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.lancheng.jneng.R;
import com.lancheng.jneng.base.Constant;
import com.lancheng.jneng.utils.OkHttpClientManager;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改密码
 */
public class UpdatePasswordActivity extends BaseActivity implements View.OnClickListener {
    private Button btnReturn, btnSubmit;//确定,返回
    private String id = "", oldpasswords = "", passwords = "";
    private EditText etPwd, etPwdAgain, etOldPwd;//密码，再次输入密码，旧密码
    private CheckBox showPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepassword);
        iniview();

    }

    /**
     * 初始化控件
     */


    private void iniview() {
        //返回按钮,确定
        btnReturn = (Button) this.findViewById(R.id.updatepassword_return);

//密码，再次输入密码，旧密码
        etPwd = (EditText) this.findViewById(R.id.update_password_pwd);
        etPwdAgain = (EditText) this.findViewById(R.id.update_password_pwdagain);
        etOldPwd = (EditText) this.findViewById(R.id.update_password_oldpwd);
//显示密码
        showPwd = (CheckBox) this.findViewById(R.id.updatepassword_updatepdw_showpassword);
        //提交
        btnSubmit = (Button) this.findViewById(R.id.update_password_submit);
        intOnclick();
        setEditTextListener();
    }

    /**
     * 注册监听
     */
    private void intOnclick() {
        btnReturn.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        //设置密码显示
        showPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etOldPwd
                            .setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    etPwd
                            .setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    etPwdAgain
                            .setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    etOldPwd.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etPwd.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etPwdAgain.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    /**
     * 设置文本编辑发生改变触动的事件
     */
    private void setEditTextListener() {
        // TODO Auto-generated method stub
        etOldPwd.addTextChangedListener(new MyTextWatcher());
        etPwd.addTextChangedListener(new MyTextWatcher());
        etPwdAgain.addTextChangedListener(new MyTextWatcher());
    }

    class MyTextWatcher implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String pwd = etPwd.getText().toString();
            String oldpw = etOldPwd.getText().toString();
            String aganpw = etPwdAgain.getText().toString();
            if (!TextUtils.isEmpty(pwd)||!TextUtils.isEmpty(oldpw)||!TextUtils.isEmpty(aganpw)) {
                btnSubmit.setBackgroundResource(R.drawable.border_ffa200_shape);
                btnSubmit.setTextColor(Color.WHITE);
            } else {
                btnSubmit.setBackgroundResource(R.drawable.border_cccccc_shape);
                btnSubmit.setTextColor(Color.WHITE);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.updatepassword_return:
                finish();
                break;

            case R.id.update_password_submit:
                isInput();
                break;
        }
    }

    /**
     * 是否已经输入数据
     */
    private void isInput() {
        id = getSharedValue(this, "userID", "-1");
        oldpasswords = etOldPwd.getText().toString();
        passwords = etPwd.getText().toString();
        String passwordsagain = etPwdAgain.getText().toString();
        if (oldpasswords.equals("") || oldpasswords == null) {
            showText(getString(R.string.himt_pwd_null));
            return;
        }
        if (passwords.equals("") || passwords == null) {
            showText(getString(R.string.himt_pwd_null));
            return;
        }
        if (!passwords.equals(passwordsagain)) {
            showText(getString(R.string.himt_pwd_inconformity));
            return;
        }
        if (id.equals("-1")) {
            showText(getString(R.string.query_userid));

            return;
        }
        if (isConnectivity(this)) {
            showLoadingDialog();
            UpdatePwd();
        } else {
            showText(getString(R.string.hint_intent));
        }

    }

    /**
     * 修改密码
     */
    private void UpdatePwd() {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("oldpasswords", oldpasswords);
        params.put("passwords", passwords);
        OkHttpClientManager.postAsyn(Constant.CHANGE_PASSWORD, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                showText(e.toString() + "");
                closeLoadingDialog();
            }

            @Override
            public void onResponse(String response) {
                closeLoadingDialog();
                Log.d("response", "onResponse() called with: " + "response = [" + response + "]");
                try {
                    // 获取状态
                    String result = new JSONObject(response).getString("result");
                    String detail = new JSONObject(response).getString("detail");
                    if (result.equals("0")) {
                        showText(detail + "");
                        return;
                    } else if (result.equals("1")) {
                        finish();
                        showText(detail + "");
                        return;
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, params);
    }
}
