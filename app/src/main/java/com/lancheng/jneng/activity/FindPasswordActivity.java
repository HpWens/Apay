package com.lancheng.jneng.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
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
 * 找回密码
 */
public class FindPasswordActivity extends BaseActivity implements View.OnClickListener {
    private Button btnReturn, btnSubmit;//确定,返回
    private EditText etPwd, etPwdAgain, etTel, etVerification;//密码，再次输入密码，电话，验证码
    private TextView getVerification;//获取验证码
    private String typeText = null;//改变获取验证码的文字
    /**
     * 以下为计时器
     */
    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpassword);
        iniview();

    }

    /**
     * 初始化控件
     */


    private void iniview() {
        //返回按钮,确定
        btnReturn = (Button) this.findViewById(R.id.findpwd_return);

        //密码，再次输入密码，电话，验证码
        etPwd = (EditText) this.findViewById(R.id.findpwd_edit_pwd);
        etPwdAgain = (EditText) this.findViewById(R.id.findpwd_edit_pwdagain);
        etTel = (EditText) this.findViewById(R.id.findpwd_edit_tel);
        etVerification = (EditText) this.findViewById(R.id.findpwd_edit_verification);
        //获取验证码
        getVerification = (TextView) this.findViewById(R.id.findpwd_tv_verification);
        //提交
        btnSubmit = (Button) this.findViewById(R.id.findpwd_submit);
        setEditextListener();
        intOnclick();
    }

    /**
     * 注册监听
     */
    private void intOnclick() {
        btnReturn.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.findpwd_return:
                finish();
                break;
            case R.id.findpwd_submit:
                findPwd();
                break;
            case R.id.findpwd_tv_verification:
                getKeyData();
                break;
        }
    }

    //

    /**
     * 设置当输入电话后改变button的背景色
     */
    private void setEditextListener() {
        // TODO Auto-generated method stub
        etTel.addTextChangedListener(new MyTextWatcher());
        etPwdAgain.addTextChangedListener(new MyTextWatcher());
        etVerification.addTextChangedListener(new MyTextWatcher());
        etPwd.addTextChangedListener(new MyTextWatcher());
    }


    //
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
            String userPwd = etPwd.getText().toString();
            String userVerification = etVerification.getText().toString();
            String userPwdAgin = etPwdAgain.getText().toString();
            String usertel = etTel.getText().toString();

            if (usertel.length() == 11) {
                getVerification.setOnClickListener(FindPasswordActivity.this);
                getVerification.setBackgroundResource(R.drawable.border_617b8a_shape);

            } else {
                getVerification.setClickable(false);
                getVerification.setText(getString(R.string.getverification_code));
                getVerification.setBackgroundResource(R.drawable.border_cccccc_shape);
            }

            if (TextUtils.isEmpty(userPwd) || TextUtils.isEmpty(userVerification) || TextUtils.isEmpty(userPwdAgin) || TextUtils.isEmpty(usertel)) {
                btnSubmit.setBackgroundResource(R.drawable.border_cccccc_shape);
            } else {
                btnSubmit.setOnClickListener(FindPasswordActivity.this);
                btnSubmit.setBackgroundResource(R.drawable.border_ffa200_shape);
            }
        }

    }

    /**
     * 发送验证码
     */
    private void getKeyData() {
        String telephone = etTel.getText().toString();
        if (!TextUtils.isEmpty(telephone)) {
            Map<String, String> params = new HashMap<>();
            params.put("telephone", telephone);
            OkHttpClientManager.postAsyn(Constant.REGISTER_SENDCODE, new OkHttpClientManager.ResultCallback<String>() {
                @Override
                public void onError(Request request, Exception e) {
                    e.printStackTrace();
                    typeText = "获取验证码";

                }

                @Override
                public void onResponse(String response) {
                    Log.d("aflf", "onResponse() called with: " + "response = [" + response + "]");
                    try {
                        // 获取状态
                        String result = new JSONObject(response).getString("result");
                        String detail = new JSONObject(response).getString("detail");
                        String code = new JSONObject(response).getString("code");
                        if (result.equals("0")) {
                            typeText = "获取验证码";
                            showText(detail + "");
                            return;
                        } else if (result.equals("1")) {
                            showText(detail + "  code:" + code);
                            myTime();
                            typeText = "再次获取";
                            return;
                        }

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Log.d("aflf", "response" + response.toString());
                }
            }, params);
        } else {
            showText(getString(R.string.himt_tel));
        }
    }

    /**
     * 找回密码
     */
    private void findPwd() {
        String Tel = etTel.getText().toString();
        String Pwd = etPwd.getText().toString();
        String verCode = etVerification.getText().toString();
        RegularExpression regular = new RegularExpression();
        boolean TEL_TYPE = regular.PhoneNumber(Tel);
        if (Tel.equals("") || Tel == null) {
            showText(getString(R.string.himt_tel));
            return;
        }
        if (!TEL_TYPE) {
            showText(getString(R.string.himt_tel_errow_format));
            return;
        }
        if (verCode.equals("") || verCode == null) {
            showText(getString(R.string.himt_verification_null));
            return;
        }
        if (Pwd.equals("") || Pwd == null) {
            showText(getString(R.string.himt_pwd_null));
            return;
        }
        if (!Pwd.equals(etPwdAgain.getText().toString())) {
            showText(getString(R.string.himt_pwd_inconformity));
            return;
        }
        getFindPwd(Tel, Pwd, verCode);
    }

    /**
     * 访问网络修改密码
     *
     * @param tel
     * @param pwd
     */
    private void getFindPwd(String tel, String pwd, String codes) {
        Map<String, String> params = new HashMap<>();
        params.put("telephone", tel);
        params.put("codes", codes);
        params.put("passwords", pwd);
        OkHttpClientManager.postAsyn(Constant.FIND_PASSWORD, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.d("aflf", "onError() called with: " + "request = [" + request + "], e = [" + e + "]");
            }

            @Override
            public void onResponse(String response) {
                Log.d("aflf", "onResponse() called with: " + "response = [" + response + "]");
                try {
                    // 获取状态
                    String result = new JSONObject(response).getString("result");
                    String detail = new JSONObject(response).getString("detail");
                    if (result.equals("0")) {
                        showText(detail + "");
                        return;
                    } else if (result.equals("1")) {
                        showText(detail);
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
    //以下为计时器

    /**
     * 获取验证码的倒计时启动
     */
    private void myTime() {
        time = new TimeCount(Constant.COUNT_DOWN_TIME, 1000);
        getVerification.setBackgroundResource(R.drawable.border_617b8a_shape);
        time.start();
    }

    /**
     * 此类是用于获取验证码按钮的倒计时操作的
     *
     * @author Zeo
     */
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onFinish() {
            // 此方法是计时完毕触发
            getVerification.setText(typeText);
            getVerification
                    .setBackgroundResource(R.drawable.border_cccccc_shape);
            getVerification.setTextColor(Color.rgb(0xfa, 0xf4, 0xe6));
            getVerification.setClickable(true);

        }

        @Override
        public void onTick(long millisUntilFinished) {
            // 计时过程显示

            getVerification.setText(millisUntilFinished / 1000 + "秒");
            getVerification.setClickable(false);

        }

    }

}
