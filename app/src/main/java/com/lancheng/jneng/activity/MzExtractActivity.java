package com.lancheng.jneng.activity;

import android.os.Bundle;
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
import com.lancheng.jneng.entity.MyzoeType;
import com.lancheng.jneng.entity.MyzoeTypeInfo;
import com.lancheng.jneng.utils.DealSpaceUtils;
import com.lancheng.jneng.utils.OkHttpClientManager;
import com.lancheng.jneng.utils.RegularExpression;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 提现的页面
 */
public class MzExtractActivity extends BaseActivity implements View.OnClickListener {
    private Button btnReturn, btnOk;//返回按钮,确定
    private String hiltValues = "";//可用额度
    private TextView userLimit;//用户额度
    private EditText userName, userIdCard, moneyEdit;//体现金额
    private Button btnSubmit;//提交申请
    // private String USER_NAME = "",USER_CARD="",USER_MONEY="";//用户姓名，卡号，金额

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract);
        initDatas();
        iniview();

    }

    /**
     * 初始化控件
     */
    private void iniview() {
        //返回按钮,确定
        btnReturn = (Button) this.findViewById(R.id.extract_return);
        userLimit = (TextView) this.findViewById(R.id.extract_user_limit);
        userName = (EditText) this.findViewById(R.id.extract_user_name);
        userIdCard = (EditText) this.findViewById(R.id.extract_user_card);
        moneyEdit = (EditText) this.findViewById(R.id.extract_user_money);
        btnSubmit = (Button) this.findViewById(R.id.extract_submit);
        intOnclick();
        DealSpaceUtils.watcherListener(userIdCard, 1);
        setEditextListener();


    }

    /**
     * 注册监听
     */
    private void intOnclick() {
        btnReturn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.extract_return:
                finish();
                break;
            case R.id.extract_submit:
                goSubmit();
                Log.e("moeny", "iniview: " + DealSpaceUtils.handMargin(userIdCard.getText().toString()));
                break;


        }
    }

    /**
     * 判断输入的数据
     */
    private void goSubmit() {
        String name = userName.getText().toString();
        String card = DealSpaceUtils.handMargin(userIdCard.getText().toString());
        String money = moneyEdit.getText().toString();
        RegularExpression regular = new RegularExpression();
        boolean MONEY_TYPE = regular.MoneyType(money);
        boolean BANK_CARD_TYPE = regular.BankCardType(card);
        if (!MONEY_TYPE) {
            showText(getString(R.string.himt_money_format_errow));
            return;
        }
        if (!BANK_CARD_TYPE) {
            showText(getString(R.string.himt_bank_card_type_errow));
            return;
        }
        if (name.equals("") || name == null) {
            showText(getString(R.string.himt_extract_name));
            return;
        }
        if (card.equals("") || card == null) {
            showText(getString(R.string.himt_extract_card));
            return;
        }
        if (money.equals("") || money == null) {
            showText(getString(R.string.himt_extract_noeny));
            return;
        }
        if (Double.valueOf(money) < 0.001) {
            showText(getString(R.string.himt_extract_money_format));
            return;
        }
        if (Double.valueOf(money) - Double.valueOf(userLimit.getText().toString()) > 0.001) {
            showText(getString(R.string.himt_extract_money_limt_errow));
            return;
        }
        String id = getSharedValue(this, "userID", "null");
        if (id.equals("null")) {
            showText(getString(R.string.query_out_login));
            return;
        }
        if (isConnectivity(this)) {
            showLoadingDialog();
            getExtractData(id, name, card, money);
        } else {
            showText(getString(R.string.hint_intent));
        }
    }

    /**
     * 提交体现申请
     */
    private void getExtractData(String id, String name, String cardId, String money) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("name", name);
        params.put("price", money);
        params.put("bankcard", cardId);
        OkHttpClientManager.postAsyn(Constant.SERVICE_RECHARGE, new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        closeLoadingDialog();
                        showText(e.getMessage());
                        Log.d("aflf", "onError() called with: " + "request = [" + request + "], e = [" + e + "]");
                    }

                    @Override
                    public void onResponse(String response) {
                        closeLoadingDialog();
                        Log.d("aflf", "onResponse() called with: " + "response = [" + response + "]");
                        try {
                            // 获取状态
                            String result = new JSONObject(response).getString("result");
                            String detail = new JSONObject(response).getString("detail");
                            if (result.equals("0")) {
                                showText(detail + "");
                                return;
                            } else if (result.equals("1")) {
                                showText(detail + "");
                                finish();
                                return;
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                }

                , params);

    }

    /**
     * 访问网络初始化数据获取额度
     */
    private void initDatas() {
        Map<String, String> params = new HashMap<>();
        params.put("id", getSharedValue(this, "userID", "1"));
        OkHttpClientManager.postAsyn(Constant.MYZOE_INDEX, new OkHttpClientManager.ResultCallback<MyzoeType<MyzoeTypeInfo>>() {
            @Override
            public void onError(Request request, Exception e) {
                showText(e.getMessage());
            }

            @Override
            public void onResponse(MyzoeType<MyzoeTypeInfo> response) {
                Log.d("response", "onResponse() called with: " + "response = [" + response + "]");
                if (response.getResult().equals("0")) {
                    showText(response.getDetail());
                    return;
                } else if (response.getResult().equals("1")) {
                    if (response.getInfor() != null) {
                        hiltValues = response.getInfor().getMoneys();
                        userLimit.setText(hiltValues + "");
                    }
                }

            }
        }, params);
    }

    /**
     * 设置当输入电话后改变button的背景色
     */
    private void setEditextListener() {
        // TODO Auto-generated method stub
        userName.addTextChangedListener(new MyTextWatcher());
        userIdCard.addTextChangedListener(new MyTextWatcher());
        moneyEdit.addTextChangedListener(new MyTextWatcher());
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
            String username = userName.getText().toString();
            String cardd = userIdCard.getText().toString();
            String money = moneyEdit.getText().toString();
            if (!TextUtils.isEmpty(username) || !TextUtils.isEmpty(cardd) || !TextUtils.isEmpty(money)) {
                btnSubmit.setOnClickListener(MzExtractActivity.this);
                btnSubmit.setBackgroundResource(R.drawable.border_ffa200_shape);
            }
        }

    }
}