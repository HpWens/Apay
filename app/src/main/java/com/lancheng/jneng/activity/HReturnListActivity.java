package com.lancheng.jneng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lancheng.jneng.R;
import com.lancheng.jneng.adapter.RecyclerReturnListAdapter;
import com.lancheng.jneng.base.Constant;
import com.lancheng.jneng.entity.BaseList;
import com.lancheng.jneng.entity.ReturnlistInfo;
import com.lancheng.jneng.utils.OkHttpClientManager;
import com.lancheng.jneng.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HReturnListActivity extends BaseActivity implements View.OnClickListener {

    private TextView topTitle;//标题描述
    private Button btnReturn;//返回
    private PullLoadMoreRecyclerView swiperefreshlayout;//刷新控件
    private RecyclerReturnListAdapter adapter;
    private List<ReturnlistInfo> listDatas = new ArrayList<ReturnlistInfo>();
    private int page = 1;
    private int pagesize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returnlist);
        initView();
        showLoadingDialog();
        initData(Constant.LIST_LOAD_FIRST);
    }

    /**
     * 初始化控件
     */
    private void initView() {

        topTitle = (TextView) this.findViewById(R.id.returnlist_title);
        btnReturn = (Button) this.findViewById(R.id.returnlist_return);
        swiperefreshlayout = (PullLoadMoreRecyclerView) this.findViewById(R.id.returnlist_pullLoadMoreRecyclerView);
        swiperefreshlayout.setVisibility(View.VISIBLE);
        adapter = new RecyclerReturnListAdapter(this, listDatas);
        swiperefreshlayout.setAdapter(adapter);

        initListener();
        //设置加载更多背景色
        swiperefreshlayout.setColorSchemeResources(R.color.titlebackgourndColor);
        swiperefreshlayout.setLinearLayout();
        swiperefreshlayout.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
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
        adapter.setOnItemClickLitener(new RecyclerReturnListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isConnectivity(HReturnListActivity.this)) {
                    //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
                    Intent intent = new Intent(HReturnListActivity.this, HReturnDetailsActivity.class);
                    //用Bundle携带数据
                    Bundle bundle = new Bundle();
                    //传递name参数为tinyphp
                    bundle.putString("typeId", listDatas.get(position).getId());
                    intent.putExtras(bundle);
                    startActivity(intent);
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
        String id=getSharedValue(this, "userID", "null");
        if (id.equals("null")){
            showText(getString(R.string.query_out_login));
            closeLoadingDialog();
            return;
        }
        params.put("id",id);
        Log.d("id",  getSharedValue(this, "userID", "1"));
    // params.put("page", page + "");
        params.put("pagesize", pagesize + "");

        OkHttpClientManager.postAsyn(Constant.MEMBER_FUNDLIST, new OkHttpClientManager.ResultCallback<BaseList<ReturnlistInfo>>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.d("response", "onError() called with: " + "request = [" + request + "], e = [" + e + "]");
                if (flag == Constant.LIST_REFRESH) {
                    swiperefreshlayout.setPullLoadMoreCompleted();
                } else if (flag == Constant.LIST_LOAD_MORE) {
                    swiperefreshlayout.setPullLoadMoreCompleted();
                } else if (flag == Constant.LIST_LOAD_FIRST) {
                    swiperefreshlayout.showEmptyView(getString(R.string.hint_errow));
                }
                closeLoadingDialog();

            }

            @Override
            public void onResponse(BaseList<ReturnlistInfo> response) {
                closeLoadingDialog();
                Log.d("response", "onResponse() called with: " + "response = [" + response + "]");
                Log.d("info", "onResponse() called with: " + "response = [" + response.getList() + "]");
                closeLoadingDialog();
                if (flag == Constant.LIST_LOAD_FIRST) {
                    Log.d("size", "onResponse: " + response.getList().size());
                    listDatas.addAll(response.getList());
                    adapter.notifyDataSetChanged();
                    if (response.getList().size() == 0) {
                        swiperefreshlayout.showEmptyView(getString(R.string.himt_null_string));
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
                    }
                }
            }
        }, params);
    }


    /**
     * 注册控件的事件
     */
    private void initListener() {
        btnReturn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.returnlist_return:
                finish();
                break;
        }

    }
}
