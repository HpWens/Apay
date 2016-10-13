package com.lancheng.jneng.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.lancheng.jneng.R;
import com.lancheng.jneng.base.Constant;

/**
 * 爹三个模块的新闻详情页面
 *
 * @param
 */
public class CNewsDetailsActivity extends BaseActivity implements View.OnClickListener {
    private Button btnReturn;//返回
    private WebView webView;//webview
    private TextView titleDesc;//标题栏
    private String Id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsdetails);

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收传递的值
        if (!bundle.getString("id").toString().equals("")) {
            Id = bundle.getString("id");
        }
      /*  if (!bundle.getString("keystate").toString().equals("")) {
            KeyState = bundle.getString("keystate");
        }
        if (!bundle.getString("linkurl").toString().equals("")) {
            LinkUrl = bundle.getString("linkurl");
        }*/
        Log.d("aflf", "aflf" + Id);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        btnReturn = (Button) this.findViewById(R.id.newsdetails_return);
        webView = (WebView) this.findViewById(R.id.newsdetails_web);
        titleDesc = (TextView) this.findViewById(R.id.news_details_title);
        titleDesc.setText(getString(R.string.title_comunity));
       /* if (KeyState.equals("news")) {
            titleDesc.setText(getString(R.string.title_comunity));
        } else {
            titleDesc.setText(getString(R.string.banner_title));
        }*/
        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        initDatas();
        initListener();
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        // WebOperation.setWebProgressDialog(this, webView, Constant.SERVER_HOST + "article/detail?id=" + Id);
        if (webView != null) {
            showLoadingDialog();
            webView.loadUrl(Constant.SERVER_HOST + "article/detail?id=" + Id);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    closeLoadingDialog();
                }
            });
        }
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
            case R.id.newsdetails_return:
                finish();
                break;
        }

    }

}
