package com.lancheng.jneng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lancheng.jneng.R;
import com.lancheng.jneng.adapter.RecyclerInverstmentListAdapter;
import com.lancheng.jneng.base.Constant;
import com.lancheng.jneng.entity.BaseList;
import com.lancheng.jneng.entity.InvestmentTypeInfo;
import com.lancheng.jneng.utils.OkHttpClientManager;
import com.lancheng.jneng.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AFLF on 2016/6/6. 投资列表页面
 */
public class HInvestmentListActivity extends BaseActivity implements View.OnClickListener {
    private TextView topTitle;//标题描述
    private Button btnRetrun;//返回
    private RecyclerInverstmentListAdapter adapter;
    private List<InvestmentTypeInfo> listDatas = new ArrayList<>();
    private PullLoadMoreRecyclerView swipeRefreshLayout;//上啦刷新
    private int page = 1;
    private int pagesize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investmentlist);
        initView();
        showLoadingDialog();
        initData(Constant.LIST_LOAD_FIRST);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        btnRetrun = (Button) this.findViewById(R.id.investmentlist_return);
        swipeRefreshLayout = (PullLoadMoreRecyclerView) this.findViewById(R.id.investementlist_pullLoadMoreRecyclerView);
        topTitle = (TextView) this.findViewById(R.id.investmentlist_title);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        adapter = new RecyclerInverstmentListAdapter(this, listDatas);
        swipeRefreshLayout.setAdapter(adapter);
        intOnclick();
        //设置加载更多背景色
        swipeRefreshLayout.setColorSchemeResources(R.color.titlebackgourndColor);
        swipeRefreshLayout.setLinearLayout();
        swipeRefreshLayout.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                page = 1;
                initData(Constant.LIST_REFRESH);
            }

            @Override
            public void onLoadMore() {
                page++;
                initData(Constant.LIST_LOAD_MORE);
            }
        });
        adapter.setOnItemClickLitener(new RecyclerInverstmentListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isConnectivity(HInvestmentListActivity.this)) {
                    //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
                    Intent intent = new Intent(HInvestmentListActivity.this, HInvestmentDeailsActivity.class);
                    //用Bundle携带数据
                    Bundle bundle = new Bundle();
                    //传递name参数为tinyphp
                    bundle.putString("id", listDatas.get(position).getId());
                    intent.putExtras(bundle);
                    startActivity(intent);
                  //  overridePendingTransition(R.anim.anim_activity_in,R.anim.anim_activity_out);
                } else {
                    showText(getString(R.string.hint_intent));
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    /**
     * 加载数据
     *
     * @param flag link{@Constant}
     */
    private void initData(final int flag) {
        Map<String, String> params = new HashMap<>();
       // params.put("id", getSharedValue(this, "userID", "1")+"");
      //  Log.d("userID", getSharedValue(this, "userID", "1"));
        params.put("id", "1");
        params.put("page", page + "");
        params.put("pagesize", pagesize + "");

        OkHttpClientManager.postAsyn(Constant.Investment_LIST, new OkHttpClientManager.ResultCallback<BaseList<InvestmentTypeInfo>>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.d("response", "onError() called with: " + "request = [" + request + "], e = [" + e + "]");
                closeLoadingDialog();
                if (flag == Constant.LIST_REFRESH) {
                    swipeRefreshLayout.setPullLoadMoreCompleted();
                } else if (flag == Constant.LIST_LOAD_MORE) {
                    swipeRefreshLayout.setPullLoadMoreCompleted();
                } else if (flag == Constant.LIST_LOAD_FIRST) {
                    swipeRefreshLayout.showEmptyView(getString(R.string.hint_errow));
                }


            }

            @Override
            public void onResponse(BaseList<InvestmentTypeInfo> response) {
                closeLoadingDialog();

                if (flag == Constant.LIST_LOAD_FIRST) {
                    Log.d("size", "onResponse: " + response.getList().size());
                    if (response.getList().size() == 0) {
                        swipeRefreshLayout.showEmptyView(getString(R.string.himt_null_string));
                    }
                    listDatas.addAll(response.getList());
                    adapter.notifyDataSetChanged();

                } else if (flag == Constant.LIST_LOAD_MORE) {
                    listDatas.addAll(response.getList());
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setPullLoadMoreCompleted();
                    if (response.getList().size() == 0) {
                        showText(getString(R.string.load_more_null));
                    }
                } else if (flag == Constant.LIST_REFRESH) {
                    listDatas.clear();
                    listDatas.addAll(response.getList());
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setPullLoadMoreCompleted();
                    if (response.getList().size() == 0) {
                        swipeRefreshLayout.showEmptyView(getString(R.string.himt_null_string));
                        Log.d("swiperefreshlayout", "onResponse() called with: " + "response = [" + response + "]");
                    }
                }
            }
        }, params);
    }

    /**
     * 注册监听
     */
    private void intOnclick() {
        btnRetrun.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.investmentlist_return:
                finish();
                break;

        }
    }
}
