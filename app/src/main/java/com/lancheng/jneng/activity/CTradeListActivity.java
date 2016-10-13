package com.lancheng.jneng.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lancheng.jneng.R;
import com.lancheng.jneng.adapter.RecyclerTradeListAdapter;
import com.lancheng.jneng.base.Constant;
import com.lancheng.jneng.entity.BaseList;
import com.lancheng.jneng.entity.ReturnlistInfo;
import com.lancheng.jneng.utils.OkHttpClientManager;
import com.lancheng.jneng.utils.RegularExpression;
import com.lancheng.jneng.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人投资了那些店铺
 */
public class CTradeListActivity extends BaseActivity implements View.OnClickListener {
    private Button btnReturn;//返回
    private PullLoadMoreRecyclerView swiperefreshlayout;//刷新控件
    private RecyclerTradeListAdapter adapter;
    private List<ReturnlistInfo> listDatas = new ArrayList<ReturnlistInfo>();
    private int page = 1;
    private int pagesize = 10;
    private PopupWindow selectPopuWindow;
    private View contentView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradelist);
        initView();
        showLoadingDialog();
        initData(Constant.LIST_LOAD_FIRST);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        btnReturn = (Button) this.findViewById(R.id.tradelist_return);
        swiperefreshlayout = (PullLoadMoreRecyclerView) this.findViewById(R.id.tradelist_pullLoadMoreRecyclerView);
        swiperefreshlayout.setVisibility(View.VISIBLE);
        adapter = new RecyclerTradeListAdapter(this, listDatas);
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
        adapter.setOnItemClickLitener(new RecyclerTradeListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isConnectivity(CTradeListActivity.this)) {
                    selectPopWindow(swiperefreshlayout,listDatas.get(position).getId());
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
        String id = getSharedValue(this, "userID", "null");
        if (id.equals("null")) {
            showText(getString(R.string.query_out_login));
            closeLoadingDialog();
            return;
        }
        params.put("id", id);
        Log.d("id", getSharedValue(this, "userID", "1"));
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
     * 注册控件的监听
     */
    private void initListener() {
        btnReturn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tradelist_return:
                finish();
                break;
        }
    }

    /**
     * 选择转让股票
     */
    private void selectPopWindow(View parent, final String typeid) {

        if (selectPopuWindow == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(this);
            contentView1 = mLayoutInflater.inflate(
                    R.layout.layout_myzoe_stock_popwindow, null);

            selectPopuWindow = new PopupWindow(contentView1,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            selectPopuWindow.getWidth();

        }
        // 下面这2句话要和配套使用才可以让 点击pop之外的地方让pop窗口消失
        selectPopuWindow.setOutsideTouchable(true);
        ColorDrawable cd = new ColorDrawable(0x000000);
        selectPopuWindow.setBackgroundDrawable(cd);
        // 产生背景变暗效果
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);
        // popuWindow1.setOutsideTouchable(true);
        selectPopuWindow.setTouchable(true);
        selectPopuWindow.setFocusable(true);

        selectPopuWindow.showAtLocation((View) parent.getParent(),
                Gravity.CENTER_HORIZONTAL, 0, 0);// 设置弹出的位置
        selectPopuWindow.update();
        selectPopuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            // 在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }

        });
        final EditText from_stock = (EditText) contentView1
                .findViewById(R.id.from_stock_money);
        TextView from_ok = (TextView) contentView1
                .findViewById(R.id.from_stock_ok);
        from_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String moneyDatas = from_stock.getText().toString();
                RegularExpression regular = new RegularExpression();
                boolean MONEY_TYPE = regular.MoneyType(moneyDatas);
                if (!MONEY_TYPE) {
                    showText(getString(R.string.himt_money_format_errow));
                    return;
                }
                if (TextUtils.isEmpty(moneyDatas)) {
                    showText(getString(R.string.himt_extract_noeny));
                    return;
                }
                if (isConnectivity(CTradeListActivity.this)) {
                    showLoadingDialog();
                    forTransfer(moneyDatas,typeid);
                } else {
                    showText(getString(R.string.hint_intent));
                }
                from_stock.setText("");
            }
        });
    }

    private void forTransfer(String moneyDatas,String typeid) {
        Map<String, String> params = new HashMap<>();
        String id = getSharedValue(this, "userID", "null");
        if (id.equals("null")) {
            showText(getString(R.string.query_out_login));
            closeLoadingDialog();
            return;
        }
        params.put("id", id);
        params.put("way", typeid);
        params.put("price", moneyDatas);
        OkHttpClientManager.postAsyn(Constant.SERVICE_TRANSFER, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                closeLoadingDialog();
                selectPopuWindow.dismiss();
                Log.d("aflf", "onError() called with: " + "request = [" + request + "], e = [" + e + "]");
            }

            @Override
            public void onResponse(String response) {
                closeLoadingDialog();
                selectPopuWindow.dismiss();
                Log.d("aflf", "onResponse() called with: " + "response = [" + response + "]");
                try {
                    // 获取状态
                    String result = new JSONObject(response).getString("result");
                    String detail = new JSONObject(response).getString("detail");
                    if (result.equals("0")) {
                        showText(detail + "");
                        return;
                    } else if (result.equals("1")) {
                        showText(detail + "");
                        return;
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }, params);

    }
}