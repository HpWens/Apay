package com.lancheng.jneng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lancheng.jneng.R;
import com.lancheng.jneng.base.Constant;
import com.lancheng.jneng.entity.BaseType;
import com.lancheng.jneng.entity.InvestmentTypeInfo;
import com.lancheng.jneng.utils.OkHttpClientManager;
import com.lancheng.jneng.view.RoundProgressBar;
import com.squareup.okhttp.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AFLF on 2016/6/6.此页面主要用于显示投资详情的页面
 */
public class HInvestmentDeailsActivity extends BaseActivity implements View.OnClickListener {

    private Button btnRetrun;//返回
    private RoundProgressBar roundProgressBar;//圆环进度
    public TextView tvName, tvMoney, tvRate, tvDuration, tvAward, tvState;//名字，投资金额，期限，利率，状态
    private TextView tv_remark;//项目描述
    private Button btnSubmit;//点击进入我要投标页面
    private String intentId = "";
    private LinearLayout investdetailsParent;//详情整体页面

    /**
     * 访问异常页面
     */
    private LinearLayout errowLayout;//出现错误的页面
    private TextView errowDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investmentdetails);
        showLoadingDialog();
        String typeId = "";
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收传递的值
        if (!bundle.getString("id").toString().equals("")) {
            typeId = bundle.getString("id");
        }
        iniview();
        GetData(typeId);


    }

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
                investdetailsParent.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(BaseType<InvestmentTypeInfo> response) {
                Log.d("aflf", "onResponse() called with: " + "response = [" + response + "]");
                closeLoadingDialog();
                if (response.getResult().equals("0")) {
                    errowLayout.setVisibility(View.VISIBLE);
                    errowDesc.setText(response.getDetail() + "");
                    investdetailsParent.setVisibility(View.GONE);
                } else {

                    if (response.getInfor() == null) {
                        investdetailsParent.setVisibility(View.GONE);
                        errowDesc.setText(getString(R.string.himt_null_string));
                        errowLayout.setVisibility(View.VISIBLE);
                    } else {
                        investdetailsParent.setVisibility(View.VISIBLE);
                        errowLayout.setVisibility(View.GONE);
                        initData(response.getInfor());
                    }

                }
            }
        }, params);
    }

    private void initData(InvestmentTypeInfo info) {
        Log.d("info", "initData() called with: " + "info = [" + info + "]");
        if (!TextUtils.isEmpty(info.getId())) {
            intentId = info.getId();
        }
        //名字，投资金额，期限，利率，状态
        if (!TextUtils.isEmpty(info.getName())) {
            tvName.setText("【担】" + info.getName() + "");
        }
        if (!TextUtils.isEmpty(info.getPrice())) {
            tvMoney.setText("￥" + info.getPrice());
        }
        if (!TextUtils.isEmpty(info.getInterest())) {
            tvRate.setText(info.getInterest() + "");
        }
        if (!TextUtils.isEmpty(info.getDeadline())) {
            tvDuration.setText(info.getDeadline() + "");
        }
        if (!TextUtils.isEmpty(info.getPoint())) {
            tvAward.setText(info.getPoint() + "%");
        }
        if (!TextUtils.isEmpty(info.getStatus())) {
            tvState.setText(info.getStatus() + "");
        }
        Log.d("updateprogress", "updateprogress" + info.getInvestment());
        if ((!TextUtils.isEmpty(info.getInvestment()))) {
            String s = info.getInvestment().toString();
            int progress = Integer.parseInt(s.substring(s.indexOf(".") + 1)) * 100;
            Log.d("updateprogress", "updateprogress" + progress);
            roundProgressBar.setProgress(progress);
        }
        //项目描述
        if (!TextUtils.isEmpty(info.getContent())) {
            tv_remark.setText(info.getContent() + "");
        }
    }

    /**
     * 初始化控件
     */
    private void iniview() {
        //访问异常
        errowLayout = (LinearLayout) this.findViewById(R.id.inverstmentdetails_errow_layout);
        errowDesc = (TextView) this.findViewById(R.id.inverstmentdetails_errow_desc);
        //投资详情整体页面
        investdetailsParent = (LinearLayout) this.findViewById(R.id.inverstmentdetails_parent);
        btnRetrun = (Button) this.findViewById(R.id.investmentdetails_return);
        btnSubmit = (Button) this.findViewById(R.id.investmentdetails_submit);
        roundProgressBar = (RoundProgressBar) this.findViewById(R.id.item_inverstment_roundprogressbar);
        //   roundProgressBar.setProgress(60);
        tvName = (TextView) this.findViewById(R.id.item_inverstment_name);
        tvMoney = (TextView) this.findViewById(R.id.item_inverstment_money);
        tvRate = (TextView) this.findViewById(R.id.item_inverstment_rate);
        tvDuration = (TextView) this.findViewById(R.id.item_inverstment_duration);
        tvAward = (TextView) this.findViewById(R.id.item_inverstment_award);
        tvState = (TextView) this.findViewById(R.id.item_inverstment_state);
        tv_remark = (TextView) findViewById(R.id.tv_remark);
        intOnclick();
    }

    /**
     * 注册监听
     */
    private void intOnclick() {
        btnRetrun.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.investmentdetails_return:
                finish();
                break;
            case R.id.investmentdetails_submit:
                if (isConnectivity(this)) {
                    //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
                    Intent intent = new Intent(HInvestmentDeailsActivity.this, HForInvestmentActivity.class);
                    //用Bundle携带数据
                    Bundle bundle = new Bundle();
                    //传递name参数为tinyphp
                    bundle.putString("id", intentId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    showText(getString(R.string.hint_intent));
                }
                break;

        }
    }
}
