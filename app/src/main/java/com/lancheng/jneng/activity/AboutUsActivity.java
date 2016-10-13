package com.lancheng.jneng.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.lancheng.jneng.R;
import com.lancheng.jneng.base.Constant;

public class AboutUsActivity extends BaseActivity implements View.OnClickListener {
    private Button btnReturn;//返回
    private WebView webView;//webview

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        intview();

    }

    /**
     * 初始化控件
     */
    private void intview() {
        btnReturn = (Button) this.findViewById(R.id.about_us_return);
        webView = (WebView) this.findViewById(R.id.about_web);
        setDatas();
        setLinsener();
    }

    /**
     * 初始化数据
     */
    private void setDatas() {
        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        if (webView != null) {
            showLoadingDialog();
            webView.loadUrl(Constant.SERVER_HOST + "article/detail?id=" + "29");
            webView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageFinished(WebView view, String url) {
                    closeLoadingDialog();
                }
            });
        }
    }

    /**
     * 注册监听
     */
    private void setLinsener() {
        btnReturn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_us_return:
                finish();
                break;

        }

    }


}
