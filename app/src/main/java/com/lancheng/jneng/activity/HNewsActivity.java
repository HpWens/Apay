package com.lancheng.jneng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lancheng.jneng.R;
import com.lancheng.jneng.adapter.RecyclerCommunityListAdapter;
import com.lancheng.jneng.base.Constant;
import com.lancheng.jneng.entity.CommunityIdType;
import com.lancheng.jneng.entity.CommunityIdTypeInfo;
import com.lancheng.jneng.entity.NewsDataType;
import com.lancheng.jneng.entity.NewsDataTypeListInfo;
import com.lancheng.jneng.utils.OkHttpClientManager;
import com.lancheng.jneng.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * menu页面最新动态
 */
public class HNewsActivity extends BaseActivity implements View.OnClickListener {
    private LinearLayout errowLayout;//出现错误的页面
    private TextView topTitle;//标题描述
    private Button btnReturn;//返回
    private List<NewsDataTypeListInfo> listDatas = new ArrayList<>();
    private PullLoadMoreRecyclerView swipeRefreshLayout;//刷新
    private RecyclerCommunityListAdapter adapter;
    private String id = "1", pagesize = "10";//分类id,当前页，页大小
    private int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        initView();
        if (isConnectivity(this)) {
            showLoadingDialog();
            getCommunityID();
            initDatas(Constant.LIST_LOAD_FIRST);
        } else {
            swipeRefreshLayout.showEmptyView(getString(R.string.hint_errow));
            showText(getString(R.string.hint_intent));
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        btnReturn = (Button) this.findViewById(R.id.news_return);
        errowLayout = (LinearLayout) this.findViewById(R.id.news_errow_layout);
        topTitle = (TextView) this.findViewById(R.id.news_title);
             swipeRefreshLayout = (PullLoadMoreRecyclerView) this.findViewById(R.id.news_pullLoadMoreRecyclerView);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        adapter = new RecyclerCommunityListAdapter(this, listDatas);
        //设置加载更多背景色
        swipeRefreshLayout.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeResources(R.color.titlebackgourndColor);
        swipeRefreshLayout.setLinearLayout();
        swipeRefreshLayout.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
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
        adapter.setOnItemClickLitener(new RecyclerCommunityListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isConnectivity(HNewsActivity.this)) {
                    //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
                    Intent intent = new Intent(HNewsActivity.this, CNewsDetailsActivity.class);
                    //用Bundle携带数据
                    Bundle bundle = new Bundle();
                    //传递name参数为tinyphp
                    bundle.putString("id", listDatas.get(position).getId());
                 /*   bundle.putString("keystate", "news");
                    bundle.putString("linkurl", "");*/
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
            case R.id.news_return:
                finish();
                break;
        }

    }

    /**
     * 获取新闻分类id
     */
    private void getCommunityID() {
        OkHttpClientManager.postAsyn(Constant.ARTICLE_SORTLIST, new OkHttpClientManager.ResultCallback<CommunityIdType<CommunityIdTypeInfo>>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(CommunityIdType<CommunityIdTypeInfo> response) {
                Log.d("response", "onResponse() called with: " + "response = [" + response + "]");
                if (response.getResult().equals("1")) {


                }
            }
        });
    }

    /**
     * 获取网络数据
     */
    private void initDatas(final int flag) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("page", String.valueOf(page));
        params.put("pagesize", pagesize);
        OkHttpClientManager.postAsyn(Constant.ARTICK_LIST, new OkHttpClientManager.ResultCallback<NewsDataType<NewsDataTypeListInfo>>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
                if (flag == Constant.LIST_REFRESH) {
                    swipeRefreshLayout.setPullLoadMoreCompleted();
                } else if (flag == Constant.LIST_LOAD_MORE) {
                    swipeRefreshLayout.setPullLoadMoreCompleted();
                } else if (flag == Constant.LIST_LOAD_FIRST) {
                    swipeRefreshLayout.showEmptyView(getString(R.string.hint_errow));
                }
                closeLoadingDialog();
            }

            @Override
            public void onResponse(NewsDataType<NewsDataTypeListInfo> response) {
                Log.d("aflf", "success：" + response.toString());
                Log.d("aflf", "count: " + response.getCount());
                Log.d("aflf", "detail: " + response.getDetail());
                Log.d("aflf", "list: " + response.getList());
                Log.d("aflf", ":list.size " + response.getList().size());
                closeLoadingDialog();
                if (flag == Constant.LIST_LOAD_FIRST) {
                    Log.d("size", "onResponse: " + response.getList().size());
                    Log.d("onResponse", "onResponse: " + response.getList().get(1).getContent());
                    listDatas.addAll(response.getList());
                    adapter.notifyDataSetChanged();
                    if (response.getList().size() == 0) {
                        swipeRefreshLayout.showEmptyView(getString(R.string.himt_null_string));
                    }
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
                    }
                }

            }

        }, params);

    }

}