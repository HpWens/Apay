package com.lancheng.jneng.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lancheng.jneng.R;

/**
 * 资金详情的页面
 */
public class HMoneyDetailsActivity extends BaseActivity implements View.OnClickListener {
    private Button btnReturn;//返回

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_details);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        btnReturn = (Button) this.findViewById(R.id.money_details_return);
        initListener();
    }

    /**
     * 注册控件的监听
     */
    private void initListener() {
        btnReturn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.money_details_return:
                finish();
                break;
        }

    }
}
