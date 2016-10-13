package com.lancheng.jneng.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lancheng.jneng.R;
import com.lancheng.jneng.activity.CNewsDetailsActivity;
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
 * Created by aflf on 2016-06-03.
 */
public class CommunityFragment extends BaseFragment implements View.OnClickListener {
    private List<NewsDataTypeListInfo> listDatas = new ArrayList<>();
    private PullLoadMoreRecyclerView swipeRefreshLayout;//刷新
    private RecyclerCommunityListAdapter adapter;
    private String id = "31", pagesize = "10";//分类id,当前页，页大小
    private int page = 1;

    /**
     * tab 选择
     */
    private LinearLayout tabContent;
    private LinearLayout tablayoutLeft, tablayoutRight;
    private TextView tabtextLeft, tabtextRight;
    private View tabviewLeft, tabviewRight;

    private List<CommunityIdTypeInfo> idListdatas = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = View.inflate(getActivity(), R.layout.fragment_community,
                null);

        intview(view);
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(mReceiver, mFilter);
        getCommunityID();
        return view;

    }

    /**
     * 获取新闻分类id
     */
    private void getCommunityID() {
        OkHttpClientManager.postAsyn(Constant.ARTICLE_SORTLIST, new OkHttpClientManager.ResultCallback<CommunityIdType<CommunityIdTypeInfo>>() {
            @Override
            public void onError(Request request, Exception e) {
                showText(getString(R.string.hint_errow));
                if (isConnectivity(getActivity())) {
                    showLoadingDialog();
                    initDatas(Constant.LIST_LOAD_FIRST);
                } else {
                    swipeRefreshLayout.showEmptyView(getString(R.string.hint_errow));
                    showText(getString(R.string.hint_intent));
                }
            }

            @Override
            public void onResponse(CommunityIdType<CommunityIdTypeInfo> response) {
                Log.d("response", "onResponse() called with: " + "response = [" + response + "]");
                if (response.getResult().equals("1")) {
                    if (response.getList().size() != 0) {
                        tabContent.setVisibility(View.VISIBLE);
                        idListdatas.addAll(response.getList());
                        id = idListdatas.get(0).getId();
                        tabtextLeft.setText(idListdatas.get(0).getName());
                        tabtextRight.setText(idListdatas.get(1).getName());
                        if (isConnectivity(getActivity())) {
                            showLoadingDialog();
                            initDatas(Constant.LIST_LOAD_FIRST);
                        } else {
                            swipeRefreshLayout.showEmptyView(getString(R.string.hint_errow));
                            showText(getString(R.string.hint_intent));
                        }
                    }

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
                showText(e.getMessage());
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

    /**
     * 初始化控件
     */
    private void intview(View view) {
        /**
         * tab 选择
         */
        tabContent = (LinearLayout) view.findViewById(R.id.community_tab_content);
        tablayoutLeft = (LinearLayout) view.findViewById(R.id.community_tableft_layout);
        tablayoutRight = (LinearLayout) view.findViewById(R.id.community_tabright_layout);
        tabtextLeft = (TextView) view.findViewById(R.id.community_tableft_text);
        tabtextRight = (TextView) view.findViewById(R.id.community_tabright_text);
        tabviewLeft = view.findViewById(R.id.community_tableft_view);
        tabviewRight = view.findViewById(R.id.community_tabright_view);

        swipeRefreshLayout = (PullLoadMoreRecyclerView) view.findViewById(R.id.community_pullLoadMoreRecyclerView);
        adapter = new RecyclerCommunityListAdapter(getActivity(), listDatas);

        //设置加载更多背景色
        swipeRefreshLayout.setAdapter(adapter);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        //R.color.titlebackgourndColor,
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
                if (isConnectivity(getActivity())) {
                    //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
                    Intent intent = new Intent(getActivity(), CNewsDetailsActivity.class);
                    //用Bundle携带数据
                    Bundle bundle = new Bundle();
                    //传递name参数为tinyphp
                    bundle.putString("id", listDatas.get(position).getId());
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
        setOnclick();
    }

    //
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.d("aflf", "hidden true");
        } else {
            if (listDatas.size() < 1) {
                if (isConnectivity(getActivity())) {
                    showLoadingDialog();
                    initDatas(Constant.LIST_LOAD_FIRST);
                } else {
                    showText(getString(R.string.hint_intent));
                }
            }
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                Log.d("mreceiver", "网络状态已经改变");
                ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeInfo = cm.getActiveNetworkInfo();
                NetworkInfo mobileInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (activeInfo != null && activeInfo.isAvailable()) {
                    String name = activeInfo.getTypeName();
                    Log.d("mreceiver", "当前网络名称：" + name);
                    Log.d("mreceiver", "已接入网络！");
                    showLoadingDialog();
                    getCommunityID();

                } else {
                    Log.d("mreceiver", "没有可用网络:");
                    Log.d("mreceiver", "无网络连接！");
//					无网络后设置islogin为false
//					doctor.setLogin(false);
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

    private void setOnclick() {
        tablayoutLeft.setOnClickListener(this);
        tablayoutRight.setOnClickListener(this);
    }

    /**
     * 初始化点击数据
     */
    private void iniData() {
        tabtextLeft.setTextColor(getResources().getColor(R.color.itemBorderColor));
        tabviewLeft.setBackgroundResource(R.color.itemBorderColor);
        tabtextRight.setTextColor(getResources().getColor(R.color.itemBorderColor));
        tabviewRight.setBackgroundResource(R.color.itemBorderColor);
    }

    @Override
    public void onClick(View v) {
        iniData();
        switch (v.getId()) {
            case R.id.community_tableft_layout:
                tabtextLeft.setTextColor(getResources().getColor(R.color.bottontitlebackgourndColorPre));
                tabviewLeft.setBackgroundResource(R.color.bottontitlebackgourndColorPre);
                id = idListdatas.get(0).getId();
                page = 1;
                if (isConnectivity(getActivity())) {
                    showLoadingDialog();
                    initDatas(Constant.LIST_REFRESH);
                } else {
                    swipeRefreshLayout.showEmptyView(getString(R.string.hint_errow));
                    showText(getString(R.string.hint_intent));
                }
                break;
            case R.id.community_tabright_layout:
                tabtextRight.setTextColor(getResources().getColor(R.color.bottontitlebackgourndColorPre));
                tabviewRight.setBackgroundResource(R.color.bottontitlebackgourndColorPre);
                id = idListdatas.get(1).getId();
                page = 1;
                if (isConnectivity(getActivity())) {
                    showLoadingDialog();
                    initDatas(Constant.LIST_REFRESH);
                } else {
                    swipeRefreshLayout.showEmptyView(getString(R.string.hint_errow));
                    showText(getString(R.string.hint_intent));
                }
                break;
        }
    }
}
