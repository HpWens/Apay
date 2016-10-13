package com.lancheng.jneng.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.lancheng.jneng.R;
import com.lancheng.jneng.base.Constant;
import com.lancheng.jneng.utils.Commons;
import com.lancheng.jneng.utils.OkHttpClientManager;
import com.lancheng.jneng.utils.RegularExpression;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by AFLF on 2016/6/6. 注册页面
 */
public class HRegisterActivity extends BaseActivity implements View.OnClickListener {
    private Button btnReturn;//返回按钮
    private TextView btnOk;//确定
    private EditText etName, etPwd, etPwdAgain, etTel, etVerification, shareCode;//姓名，密码，再次输入密码，电话，验证码,邀请码
    private TextView getVerification;//获取验证码
    private Button btnSubmit;//提交
    private CheckBox registerCheckBox;//是否同意
    private String CHECKBOX_TYPE = "false";//是否确定同意协议
    private int TYPE_OBTAIN = 1;//是否是第一次获取
    private String typeText = null;//改变获取验证码的文字

    private String username, passwords, verCode, telephone, friendcode;


    /**
     * 以下为计时器
     */
    private TimeCount time;
    private TextView tvuserRules, tvprivateRules;//使用条款，隐私条款

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        iniview();

    }

    /**
     * 初始化控件
     */
    private void iniview() {
        //返回按钮,确定
        btnReturn = (Button) this.findViewById(R.id.register_return);
        btnOk = (TextView) this.findViewById(R.id.register_ok);
        //姓名，密码，再次输入密码，电话，验证码，邀请码
        etName = (EditText) this.findViewById(R.id.register_edit_name);
        etPwd = (EditText) this.findViewById(R.id.register_edit_pwd);
        etPwdAgain = (EditText) this.findViewById(R.id.register_edit_pwdagain);
        etTel = (EditText) this.findViewById(R.id.register_edit_tel);
        etVerification = (EditText) this.findViewById(R.id.register_edit_verification);
        shareCode = (EditText) this.findViewById(R.id.register_edit_sharecode);
        //获取验证码
        getVerification = (TextView) this.findViewById(R.id.register_tv_verification);
        //是否同意协议
        registerCheckBox = (CheckBox) this.findViewById(R.id.register_checkBox);
        //提交
        btnSubmit = (Button) this.findViewById(R.id.register_submit);
        //使用条款，隐私条款
        tvuserRules = (TextView) this.findViewById(R.id.register_user_rules);
        tvprivateRules = (TextView) this.findViewById(R.id.register_privite_rules);
        intOnclick();
    }

    /**
     * 注册监听
     */
    private void intOnclick() {
        btnReturn.setOnClickListener(this);
        setCheckUser();
        setEditextListener();
        registerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CHECKBOX_TYPE = "true";
                } else {
                    CHECKBOX_TYPE = "false";
                }
            }
        });
        tvuserRules.setOnClickListener(this);
        tvprivateRules.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_return:
                finish();
                break;
            case R.id.register_ok:
                TestRejister();

                break;
            case R.id.register_tv_verification:
                if (!TextUtils.isEmpty(etTel.getText().toString())) {
                    if (Commons.isConnectivity(this)) {
                        closeLoadingDialog();
                        getKeyData(etTel.getText().toString());
                    } else {
                        showText(getString(R.string.hint_intent));
                    }
                }
                break;
            case R.id.register_submit:
                TestRejister();
                break;
            case R.id.register_user_rules:
                if (isConnectivity(HRegisterActivity.this)) {
                    //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
                    Intent intent = new Intent(HRegisterActivity.this, ProtocolActivity.class);
                    //用Bundle携带数据
                    Bundle bundle = new Bundle();
                    //传递name参数为tinyphp
                    bundle.putString("proId", "31");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    //  overridePendingTransition(R.anim.anim_activity_in,R.anim.anim_activity_out);
                } else {
                    showText(getString(R.string.hint_intent));
                }
                break;
            case R.id.register_privite_rules:
                if (isConnectivity(HRegisterActivity.this)) {
                    //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
                    Intent intent = new Intent(HRegisterActivity.this, ProtocolActivity.class);
                    //用Bundle携带数据
                    Bundle bundle = new Bundle();
                    //传递name参数为tinyphp
                    bundle.putString("proId", "32");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    //  overridePendingTransition(R.anim.anim_activity_in,R.anim.anim_activity_out);
                } else {
                    showText(getString(R.string.hint_intent));
                }
                break;
        }
    }


    /**
     * 检查是否可以注册该用户
     */
    private void setCheckUser() {
        etTel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    if (etTel.getText().toString().length() == 11) {
                        if (isConnectivity(HRegisterActivity.this)) {
                            showLoadingDialog();
                            CheckUserData(etTel.getText().toString());
                        } else {
                            showText(getString(R.string.hint_intent));
                        }
                    } else {
                        showText(getString(R.string.himt_register_name));
                    }
                }
            }


        });

    }

    /**
     * 查看用户是否可以进行注册
     */
    private void CheckUserData(String username) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        OkHttpClientManager.postAsyn(Constant.REGISTER_CHECKUSER, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                closeLoadingDialog();
                e.printStackTrace();
                showText(e.getMessage() + "");

            }

            @Override
            public void onResponse(String response) {
                closeLoadingDialog();
                try {
                    // 获取状态
                    String result = new JSONObject(response).getString("result");
                    String detail = new JSONObject(response).getString("detail");
                    if (result.equals("0")) {
                        showText(detail + "");
                        return;
                    } else if (result.equals("1")) {
                        showText(detail + "");
                        return;
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Log.d("aflf", "response" + response.toString());
            }
        }, params);

    }

    /**
     * 发送验证码
     */
    private void getKeyData(String tel) {
        if (tel.length() != 11) {
            closeLoadingDialog();
            showText(getString(R.string.himt_tel));
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("telephone", tel);
        OkHttpClientManager.postAsyn(Constant.REGISTER_SENDCODE, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                closeLoadingDialog();
                e.printStackTrace();
                typeText = "获取验证码";

            }

            @Override
            public void onResponse(String response) {
                closeLoadingDialog();
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
                        showText(detail);
                        Log.d("aflf", "code" + code);
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

    }

    /**
     * 测试访问网络
     */
    private void TestRejister() {
        username = etName.getText().toString();
        passwords = etPwd.getText().toString();
        verCode = etVerification.getText().toString();
        telephone = etTel.getText().toString();
        friendcode = shareCode.getText().toString();
        RegularExpression regular = new RegularExpression();
        boolean TEL_TYPE = regular.PhoneNumber(telephone);
        if (!TEL_TYPE) {
            showText(getString(R.string.himt_tel_errow_format));
            return;
        }
        if (telephone.equals("") || telephone == null) {
            showText(getString(R.string.himt_tel));
            return;
        }
        if (username.equals("") || username == null) {
            showText(getString(R.string.himt_name));
            return;
        }
        if (passwords.equals("") || passwords == null) {
            showText(getString(R.string.himt_pwd_null));
            return;
        }
        if (!passwords.equals(etPwdAgain.getText().toString())) {
            showText(getString(R.string.himt_pwd_inconformity));
            return;
        }
       /*   if (!PWD) {
            showText("你好，密码格式不正确！");
            return;
        }
      if (!USERNAME) {
            showText("你好，登录名格式不正确！");
            return;
        }
        if (!TEL) {
            showText("你好，手机号码格式不正确!");
            return;
        }*/
        if (CHECKBOX_TYPE.equals("false")) {
            showText(getString(R.string.himt_prol));
            return;
        }
        if (isConnectivity(this)) {
            showLoadingDialog();
            GetRejister();
        } else {
            showText(getString(R.string.hint_intent));
        }

    }

    /**
     * 注册
     */
    private void GetRejister() {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("passwords", passwords);
        params.put("telephone", telephone);
        params.put("friendcode", friendcode);
        params.put("codes", verCode);
        Log.d("aflf", "parms" + params);
        OkHttpClientManager.postAsyn(Constant.REGISTER, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.d("response", "response" + e.toString());
                showText(getString(R.string.hint_errow));
                closeLoadingDialog();
            }

            @Override
            public void onResponse(String response) {
                closeLoadingDialog();

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
                Log.d("aflf", "response" + response.toString());
            }
        }, params);
    }

    /**
     * 设置当输入电话后改变button的背景色
     */
    private void setEditextListener() {
        // TODO Auto-generated method stub
        etTel.addTextChangedListener(new MyTextWatcher());
        etPwdAgain.addTextChangedListener(new MyTextWatcher());
        etVerification.addTextChangedListener(new MyTextWatcher());
        etPwd.addTextChangedListener(new MyTextWatcher());
        etName.addTextChangedListener(new MyTextWatcher());
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
            String userName = etName.getText().toString();
            String usertel = etTel.getText().toString();

            if (!TextUtils.isEmpty(usertel)) {
                getVerification.setOnClickListener(HRegisterActivity.this);
                getVerification.setBackgroundResource(R.drawable.border_617b8a_shape);

            } else {
                getVerification.setClickable(false);
                getVerification.setText(getString(R.string.getverification_code));
                getVerification.setBackgroundResource(R.drawable.border_cccccc_shape);
            }

            if (!TextUtils.isEmpty(userPwd) || !TextUtils.isEmpty(userVerification) || !TextUtils.isEmpty(userPwdAgin) || !TextUtils.isEmpty(userName) || !TextUtils.isEmpty(usertel)) {
                btnOk.setOnClickListener(HRegisterActivity.this);
                btnSubmit.setOnClickListener(HRegisterActivity.this);
                btnSubmit.setBackgroundResource(R.drawable.border_ffa200_shape);
            } else {
                btnSubmit.setBackgroundResource(R.drawable.border_cccccc_shape);
            }
        }

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
