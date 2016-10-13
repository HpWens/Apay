package com.lancheng.jneng.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lancheng.jneng.R;
import com.lancheng.jneng.base.Constant;
import com.lancheng.jneng.utils.OkHttpClientManager;
import com.lancheng.jneng.utils.RegularExpression;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MFeedBackActivity extends BaseActivity implements View.OnClickListener {
    private Button btnRetrun, btnSubmit;//返回,提交
    private EditText etDesc, etTel;//提交意见,联系电话

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_feedback);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        btnRetrun = (Button) this.findViewById(R.id.feedback_return);
        btnSubmit = (Button) this.findViewById(R.id.feedback_submit);
        etDesc = (EditText) this.findViewById(R.id.mzfeedback_desc);
        etTel = (EditText) this.findViewById(R.id.mzfeedback_tel);
        setOnclick();

    }

    /**
     * 注册事件
     */
    private void setOnclick() {
        btnRetrun.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feedback_return:
                finish();
                break;
            case R.id.feedback_submit:
                if (isConnectivity(this)) {
                    getSubmit();
                } else {
                    showText(getString(R.string.hint_intent));
                }
                break;
        }

    }

    /**
     * 访问网络提交意见
     */
    private void getSubmit() {
        String feedtext = etDesc.getText().toString();
        String feedtel = etTel.getText().toString();
        RegularExpression regular = new RegularExpression();
        boolean TEL_TYPE = regular.PhoneNumber(feedtel);
        if (TextUtils.isEmpty(feedtext)) {
            showText(getString(R.string.himt_null_string));
            closeLoadingDialog();
            return;
        }
        if (TextUtils.isEmpty(feedtel)) {
            showText(getString(R.string.himt_tel));
            closeLoadingDialog();
            return;
        }
        if (!TEL_TYPE) {
            showText(getString(R.string.himt_tel_errow_format));
            closeLoadingDialog();
            return;
        }
        //
        Map<String, String> params = new HashMap<>();
        params.put("mainid", getSharedValue(this, "userID", "0"));
        params.put("telephone", feedtel);
        params.put("content", feedtext);
        OkHttpClientManager.postAsyn(Constant.SERVICE_MESSAGES, new OkHttpClientManager.ResultCallback<String>() {
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
