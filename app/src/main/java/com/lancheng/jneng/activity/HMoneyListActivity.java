package com.lancheng.jneng.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lancheng.jneng.R;
import com.lancheng.jneng.adapter.RecyclerMoneyListAdapter;
import com.lancheng.jneng.base.Constant;
import com.lancheng.jneng.entity.MoneyListType;
import com.lancheng.jneng.entity.MoneyListTypeInfo;
import com.lancheng.jneng.utils.OkHttpClientManager;
import com.lancheng.jneng.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * menu页面资金列表详情
 */
public class HMoneyListActivity extends BaseActivity implements View.OnClickListener {
    private TextView topTitle;//标题描述
    private Button btnReturn;//返回
    private PullLoadMoreRecyclerView swiperefreshlayout;//刷新控件
    private RecyclerMoneyListAdapter adapter;
    private List<MoneyListTypeInfo> listDatas = new ArrayList<>();
    //访问网络
    private String  pagesize = "10";//分类id,当前页,页大小
    private int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moneylist);
        initView();
        showLoadingDialog();
        initDatas(Constant.LIST_LOAD_FIRST);

    }

    /**
     * 访问网络获取数据
     */
    private void initDatas(final int flag) {
        Map<String, String> params = new HashMap<>();
        String id=getSharedValue(this, "userID", "null");
        if (id.equals("null")){
            showText(getString(R.string.query_out_login));
            closeLoadingDialog();
            return;
        }
        params.put("id", id);
        params.put("page", page + "");
        params.put("pagesize", pagesize);

        OkHttpClientManager.postAsyn(Constant.MONEY_LIST, new OkHttpClientManager.ResultCallback<MoneyListType<MoneyListTypeInfo>>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.d("onError", "onError() called with: " + "request = [" + request + "], e = [" + e + "]");
                closeLoadingDialog();
                if (flag == Constant.LIST_REFRESH) {
                    swiperefreshlayout.setPullLoadMoreCompleted();
                } else if (flag == Constant.LIST_LOAD_MORE) {
                    swiperefreshlayout.setPullLoadMoreCompleted();
                } else if (flag == Constant.LIST_LOAD_FIRST) {
                    swiperefreshlayout.showEmptyView(getString(R.string.hint_errow));
                }

            }

            @Override
            public void onResponse(MoneyListType<MoneyListTypeInfo> response) {
                Log.d("aflf", "onResponse() called with: " + "response = [" + response + "]");
                closeLoadingDialog();
                if (flag == Constant.LIST_LOAD_FIRST) {
                    if (response.getList().size() == 0) {
                        swiperefreshlayout.showEmptyView(getString(R.string.himt_null_string));
                        Log.d("swiperefreshlayout", "onResponse() called with: " + "response = [" + response + "]");
                    } else {
                        listDatas.addAll(response.getList());
                        adapter.notifyDataSetChanged();
                    }
                } else if (flag == Constant.LIST_LOAD_MORE) {
                    listDatas.addAll(response.getList());
                    adapter.notifyDataSetChanged();
                    swiperefreshlayout.setPullLoadMoreCompleted();
                    if (response.getList().size() == 0) {
                        showText(getString(R.string.load_more_null));
                    }
                } else if (flag == Constant.LIST_REFRESH) {
                    listDatas.clear();
                    listDatas.addAll(response.getList());
                    adapter.notifyDataSetChanged();
                    swiperefreshlayout.setPullLoadMoreCompleted();
                    if (response.getList().size() == 0) {
                        swiperefreshlayout.showEmptyView(getString(R.string.himt_null_string));
                        Log.d("swiperefreshlayout", "onResponse() called with: " + "response = [" + response + "]");
                    }
                }
            }
        }, params);

    }


    /**
     * 初始化控件
     */
    private void initView() {
        btnReturn = (Button) this.findViewById(R.id.moneylist_return);
        swiperefreshlayout = (PullLoadMoreRecyclerView) this.findViewById(R.id.moneylist_pullLoadMoreRecyclerView);
        topTitle = (TextView) this.findViewById(R.id.moneylist_title);
        swiperefreshlayout.setVisibility(View.VISIBLE);
        adapter = new RecyclerMoneyListAdapter(this, listDatas);
        swiperefreshlayout.setAdapter(adapter);
        initListener();
        //设置加载更多背景色
        swiperefreshlayout.setColorSchemeResources(R.color.titlebackgourndColor);
        swiperefreshlayout.setLinearLayout();
        swiperefreshlayout.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                page = 1;
                initDatas(Constant.LIST_REFRESH);
            }

            @Override
            public void onLoadMore() {
                page++;
                initDatas(Constant.LIST_LOAD_MORE);
            }
        });

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
            case R.id.moneylist_return:
                finish();
                break;
        }

    }
}