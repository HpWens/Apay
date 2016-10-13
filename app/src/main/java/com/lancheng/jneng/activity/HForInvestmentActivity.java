package com.lancheng.jneng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lancheng.jneng.R;
import com.lancheng.jneng.base.Constant;
import com.lancheng.jneng.entity.BaseType;
import com.lancheng.jneng.entity.InvestmentTypeInfo;
import com.lancheng.jneng.entity.MyzoeType;
import com.lancheng.jneng.entity.MyzoeTypeInfo;
import com.lancheng.jneng.utils.OkHttpClientManager;
import com.lancheng.jneng.utils.RegularExpression;
import com.lancheng.jneng.view.RoundProgressBar;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 点击投资列表详情中的立刻投资生存的页面
 */
public class HForInvestmentActivity extends BaseActivity implements View.OnClickListener {
    private Button btnRetrun;//返回
    private RoundProgressBar roundProgressBar;//圆环进度
    private TextView tvName, tvMoney, tvRate, tvDuration, tvAward, tvState;//名字，投资金额，期限，利率，状态
    private TextView userLimit, forAll;//用户额度，全投，提交
    private Button btnSubmit;
    private EditText editMoney;//投入金额
    private String typeId = "";//服务id
    private String hiltValues = "";//可用额度
    private LinearLayout forinvestmentParent;//详情整体页面
    private CheckBox investmentCheckBox;//是否同意
    private String CHECKBOX_TYPE = "false";//是否确定同意协议
    private TextView protocolDesc;//服务协议
    /**
     * 访问异常页面
     */
    private LinearLayout errowLayout;//出现错误的页面
    private TextView errowDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forinvestment);
        showLoadingDialog();
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收传递的值
        if (!bundle.getString("id").toString().equals("")) {
            typeId = bundle.getString("id");
        }

        Log.d("aflf", "aflf" + typeId);
        showLoadingDialog();
        initDatas();
        iniview();
        GetData(typeId);


    }


    /**
     * 初始化控件
     */
    private void iniview() {
        btnRetrun = (Button) this.findViewById(R.id.forinvestment_return);
        //访问异常
        errowLayout = (LinearLayout) this.findViewById(R.id.forinvestment_errow_layout);
        errowDesc = (TextView) this.findViewById(R.id.forinvestment_errow_desc);
        //投资详情整体页面
        forinvestmentParent = (LinearLayout) this.findViewById(R.id.forinvestment_parent);
        roundProgressBar = (RoundProgressBar) this.findViewById(R.id.item_inverstment_roundprogressbar);
        tvName = (TextView) this.findViewById(R.id.item_inverstment_name);
        tvMoney = (TextView) this.findViewById(R.id.item_inverstment_money);
        tvRate = (TextView) this.findViewById(R.id.item_inverstment_rate);
        tvDuration = (TextView) this.findViewById(R.id.item_inverstment_duration);
        tvAward = (TextView) this.findViewById(R.id.item_inverstment_award);
        tvState = (TextView) this.findViewById(R.id.item_inverstment_state);

        //用户额度，全投，提交
        userLimit = (TextView) this.findViewById(R.id.forinvestment_limit);
        forAll = (TextView) this.findViewById(R.id.forinvestment_all);
        btnSubmit = (Button) this.findViewById(R.id.forinvestment_submit);
        //投资金额
        editMoney = (EditText) this.findViewById(R.id.forinvestment_editmoney);
        investmentCheckBox = (CheckBox) this.findViewById(R.id.forinvestment_checkBox);
        protocolDesc = (TextView) this.findViewById(R.id.forinvestemtn_protocol_desc);
        // roundProgressBar.setProgress(60);
        intOnclick();
    }


    /**
     * 注册监听
     */
    private void intOnclick() {
        btnRetrun.setOnClickListener(this);
        forAll.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        protocolDesc.setOnClickListener(this);
        investmentCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CHECKBOX_TYPE = "true";
                } else {
                    CHECKBOX_TYPE = "false";
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forinvestment_return:
                finish();
                break;
            case R.id.forinvestment_all:
                editMoney.setText(userLimit.getText().toString());
                break;
            case R.id.forinvestment_submit:
                if (isConnectivity(this)) {
                    showLoadingDialog();
                    ForSubmit();
                } else {
                    showText(getString(R.string.hint_intent));
                }
                break;
            case R.id.forinvestemtn_protocol_desc:
                if (isConnectivity(HForInvestmentActivity.this)) {
                    //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
                    Intent intent = new Intent(HForInvestmentActivity.this, ProtocolActivity.class);
                    //用Bundle携带数据
                    Bundle bundle = new Bundle();
                    //传递name参数为tinyphp
                    bundle.putString("proId", "30");
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
     * 访问网络初始化数据获取额度
     */
    private void initDatas() {
        Map<String, String> params = new HashMap<>();
        String userid = getSharedValue(this, "userID", "-1");
        if (userid.equals("-1")) {
            showText(getString(R.string.query_out_login));
            return;
        }
        params.put("id", userid);
        OkHttpClientManager.postAsyn(Constant.MYZOE_INDEX, new OkHttpClientManager.ResultCallback<MyzoeType<MyzoeTypeInfo>>() {
            @Override
            public void onError(Request request, Exception e) {
                closeLoadingDialog();
            }

            @Override
            public void onResponse(MyzoeType<MyzoeTypeInfo> response) {
                Log.d("response", "onResponse() called with: " + "response = [" + response.getInfor() + "]");
                if (response.getResult().equals("0")) {
                    showText(response.getDetail());
                    return;
                } else if (response.getResult().equals("1")) {

                    if (response.getInfor() != null) {
                        hiltValues = response.getInfor().getMoneys();
                        userLimit.setText(hiltValues + "");
                        return;
                    }
                }

            }
        }, params);
    }

    /**
     * 获取投资详情
     */
    private void GetData(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        OkHttpClientManager.postAsyn(Constant.Investment_INFO, new OkHttpClientManager.ResultCallback<BaseType<InvestmentTypeInfo>>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.d("aflf", "onError() called with: " + "request = [" + request + "], e = [" + e + "]");
                closeLoadingDialog();
                errowLayout.setVisibility(View.VISIBLE);
                errowDesc.setText(getString(R.string.hint_errow));
                forinvestmentParent.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(BaseType<InvestmentTypeInfo> response) {
                Log.d("aflf", "onResponse() called with: " + "response = [" + response + "]");
                closeLoadingDialog();
                if (response.getResult().equals("0")) {
                    errowLayout.setVisibility(View.VISIBLE);
                    errowDesc.setText(response.getDetail() + "");
                    forinvestmentParent.setVisibility(View.GONE);
                    return;
                } else {
                    if (response.getInfor() != null) {
                        errowLayout.setVisibility(View.GONE);
                        forinvestmentParent.setVisibility(View.VISIBLE);
                        setDatas(response.getInfor());
                    }
                }
            }
        }, params);
    }

    /**
     * 设置请求获取的数据
     *
     * @param infor
     */
    private void setDatas(InvestmentTypeInfo infor) {
        Log.d("infor", "setDatas() called with: " + "infor = [" + infor + "]");
        //名字，投资金额，期限，利率，状态
        if (!TextUtils.isEmpty(infor.getName())) {
            tvName.setText("【担】" + infor.getName() + "");
        }
        if (!TextUtils.isEmpty(infor.getPrice())) {
            tvMoney.setText("￥" + infor.getPrice());
        }
        if (!TextUtils.isEmpty(infor.getInterest())) {
            tvRate.setText(infor.getInterest() + "");
        }
        if (!TextUtils.isEmpty(infor.getDeadline())) {
            tvDuration.setText(infor.getDeadline() + "");
        }
        if (!TextUtils.isEmpty(infor.getPoint())) {
            tvAward.setText(infor.getPoint() + "%");
        }
        if (!TextUtils.isEmpty(infor.getStatus())) {
            tvState.setText(infor.getStatus() + "");
        }
        Log.d("updateprogress", "updateprogress" + infor.getInvestment());
        if ((!TextUtils.isEmpty(infor.getInvestment()))) {
            String s = infor.getInvestment().toString();
            int progress = Integer.parseInt(s.substring(s.indexOf(".") + 1)) * 100;
            Log.d("updateprogress", "updateprogress" + progress);
            roundProgressBar.setProgress(progress);
        }
    }

    /**
     * 提交投资申请
     */
    private void ForSubmit() {
        String money = editMoney.getText().toString();
        if (TextUtils.isEmpty(money)) {
            showText("请输入金额！");
            closeLoadingDialog();
            return;
        }
        if (CHECKBOX_TYPE.equals("false")) {
            showText(getString(R.string.himt_prol));
            closeLoadingDialog();
            return;
        }
        RegularExpression regular = new RegularExpression();
        boolean MONEY_TYPE = regular.MoneyType(money);
        if (!MONEY_TYPE) {
            showText(getString(R.string.himt_money_format_errow));
            closeLoadingDialog();
            return;
        }
        if (Double.valueOf(money) < 0.01) {
            showText(getString(R.string.himt_extract_noeny));
            closeLoadingDialog();
            return;
        }
        String userid = getSharedValue(this, "userID", "-1");
        if (userid.equals("-1")) {
            closeLoadingDialog();
            showText(getString(R.string.query_out_login));
            return;
        }
        if (Double.valueOf(money) - Double.valueOf(userLimit.getText().toString()) > 0.001) {
            closeLoadingDialog();
            showText(getString(R.string.himt_extract_money_limt_errow));
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("id", typeId);
        params.put("mainid", getSharedValue(this, "userID", "-1"));
        params.put("price", money);
        OkHttpClientManager.postAsyn(Constant.Investment_SUBINEVST, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.d("aflf", "onError() called with: " + "request = [" + request + "], e = [" + e + "]");
                showText(e.toString() + "");
            }

            @Override
            public void onResponse(String response) {
                closeLoadingDialog();
                Log.d("aflf", "onResponse() called with: " + "response = [" + response + "]");
                try {
                    closeLoadingDialog();
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
        }, params);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
