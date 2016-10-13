package com.lancheng.jneng.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.lancheng.jneng.R;

public class AboutUsDetailsActivity extends BaseActivity {
    private Button btnReturn;//返回
    private WebView webview;//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_details);
        intview();

    }

    private void intview() {
        btnReturn = (Button) this.findViewById(R.id.about_us_details_return);
        webview = (WebView) this.findViewById(R.id.about_us_details_webview);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        WebSettings settings = webview.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        initDatas();
    }

    private void initDatas() {
        if (webview != null) {
            showLoadingDialog();
            webview.loadUrl("http://www.lcp.dfco.cn/gywm/");
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    closeLoadingDialog();
                }
            });
        }
    }

}
