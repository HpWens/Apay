package com.lancheng.jneng.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.lancheng.jneng.R;
import com.lancheng.jneng.base.Constant;

public class ProtocolActivity extends BaseActivity implements View.OnClickListener{
    private Button btnReturn;//返回
    private WebView webView;//webview
    private TextView titleDesc;//标题栏
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol);
        String typeId = "30";
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收传递的值
        if (!bundle.getString("proId").toString().equals("")) {
            typeId = bundle.getString("proId");
        }
        initView(typeId);
    }

    /**
     * 初始化控件
     */
    private void initView(String id) {
        btnReturn = (Button) this.findViewById(R.id.protocol_return);
        webView = (WebView) this.findViewById(R.id.protocol_web);
        titleDesc = (TextView) this.findViewById(R.id.protocol_title);
        if (id.equals("30")){
            titleDesc.setText(getString(R.string.pro_investment));
        }else if(id.equals("31")){
            titleDesc.setText("使用条款");
        }else {
            titleDesc.setText("隐私条款");
        }
        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        initDatas(id);
        initListener();
    }

    /**
     * 注册监听
     */
    private void initListener() {
        btnReturn.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initDatas(String typeid) {
        if (webView != null) {
            showLoadingDialog();
            webView.loadUrl(Constant.SERVER_HOST + "article/detail?id=" + typeid);
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageFinished(WebView view, String url) {
                    closeLoadingDialog();
                }
            });
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
           case  R.id.protocol_return:
            finish();
            break;
        }

    }
}
