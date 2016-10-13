package com.lancheng.jneng.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lancheng.jneng.R;
import com.lancheng.jneng.activity.CAccountActivity;
import com.lancheng.jneng.activity.CTradeListActivity;
import com.lancheng.jneng.activity.HMoneyListActivity;
import com.lancheng.jneng.activity.HReturnListActivity;
import com.lancheng.jneng.activity.LoginActivity;
import com.lancheng.jneng.activity.MzExtractActivity;
import com.lancheng.jneng.activity.MzPayActivity;
import com.lancheng.jneng.base.Constant;
import com.lancheng.jneng.entity.MyzoeType;
import com.lancheng.jneng.entity.MyzoeTypeInfo;
import com.lancheng.jneng.utils.Commons;
import com.lancheng.jneng.utils.ImageUtilsView;
import com.lancheng.jneng.utils.OkHttpClientManager;
import com.lancheng.jneng.utils.RegularExpression;
import com.lancheng.jneng.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aflf on 2016-06-03.
 */
public class MyZoeFragment extends BaseFragment implements View.OnClickListener {
    private RoundImageView loginIcon;//登陆后的头像
    private TextView btnExtract, btnPay;//充值，提现按钮
    private LinearLayout layoutTab1, layoutTab2, layoutTab3, layoutTab4;//四个tab
    private TextView tvTab1, tvTab2, tvTab3, tvTab4;//显示的四条金额
    private TextView userName, rellName;
    private boolean isEditInfo;
    private PopupWindow selectPopuWindow;
    private View contentView1;
    private SwipeRefreshLayout swipeRefreshLayout;

    //登陆的布局
    private LinearLayout loginContentLayout;
    private TextView btn_login;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.d("aflf", "hidden true");
        } else {
            Log.d("aflf", "hidden false");
            if (getSharedValue(getActivity(), "userID", "-1").equals("-1")) {//未登录
                loginContentLayout.setVisibility(View.VISIBLE);
            } else {//已经登录过
                showLoadingDialog();
                loginContentLayout.setVisibility(View.GONE);
                initDatas();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getSharedValue(getActivity(), "userID", "-1").equals("-1")) {//未登录
            loginContentLayout.setVisibility(View.VISIBLE);
        } else {//已经登录过
            loginContentLayout.setVisibility(View.GONE);
            if (isEditInfo) {
                showText("重新加载页面");
                initDatas();
                isEditInfo = false;
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View view = View.inflate(getActivity(), R.layout.fragment_myzoe,
                null);
        intview(view);
        return view;

    }


    /**
     * 初始化控件
     */
    private void intview(View view) {
        /**
         * 没有登陆时候的页面
         */

        loginContentLayout = (LinearLayout) view.findViewById(R.id.login_content);
        //登陆按钮
        btn_login = (TextView) view.findViewById(R.id.btn_login);
        /**
         * 登陆了之后的页面
         */
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.myzoe_swipe);
        swipeRefreshLayout.setColorSchemeResources(R.color.titlebackgourndColor);
        loginIcon = (RoundImageView) view.findViewById(R.id.myzoe_icon_login);
        userName = (TextView) view.findViewById(R.id.mzoe_username);
        rellName = (TextView) view.findViewById(R.id.mzoe_rellname);
        btnExtract = (TextView) view.findViewById(R.id.myzoe_extract);
        btnPay = (TextView) view.findViewById(R.id.myzoe_pay);
        layoutTab1 = (LinearLayout) view.findViewById(R.id.myzoe_layout_tab1);
        layoutTab2 = (LinearLayout) view.findViewById(R.id.myzoe_layout_tab2);
        layoutTab3 = (LinearLayout) view.findViewById(R.id.myzoe_layout_tab3);
        layoutTab4 = (LinearLayout) view.findViewById(R.id.myzoe_layout_tab4);
        tvTab1 = (TextView) view.findViewById(R.id.myzoe_layout_tab1_tv);
        tvTab2 = (TextView) view.findViewById(R.id.myzoe_layout_tab2_tv);
        tvTab3 = (TextView) view.findViewById(R.id.myzoe_layout_tab3_tv);
        tvTab4 = (TextView) view.findViewById(R.id.myzoe_layout_tab4_tv);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Commons.isConnectivity(getActivity())) {
                    showLoadingDialog();
                    initDatas();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    showText(getString(R.string.hint_intent));
                }
            }
        });

        if (getSharedValue(getActivity(), "userID", "-1").equals("-1")) {//没有登陆
            loginContentLayout.setVisibility(View.VISIBLE);
        } else {//登陆
            loginContentLayout.setVisibility(View.GONE);
            showLoadingDialog();
            initDatas();
        }
        /**
         * 没有登陆
         */
        btn_login.setOnClickListener(this);
    }

    /**
     * 注册控件的监听
     */
    private void initOnclick() {
        loginIcon.setOnClickListener(this);
        btnExtract.setOnClickListener(this);
        btnPay.setOnClickListener(this);
        layoutTab2.setOnClickListener(this);
        layoutTab3.setOnClickListener(this);
        layoutTab4.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        isEditInfo = true;
        switch (v.getId()) {
            case R.id.myzoe_icon_login:
                isEditInfo = true;
                intentActivity(getActivity(), CAccountActivity.class, false);
                //  selectPicture(v);
                break;
            case R.id.myzoe_extract:
                intentActivity(getActivity(), MzExtractActivity.class, false);
                break;
            case R.id.myzoe_pay:
                intentActivity(getActivity(), MzPayActivity.class, false);
                break;
            case R.id.btn_login:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                setSharedPreferences(getActivity(), "resultState", "2");
                startActivity(intent);
                break;
            case R.id.myzoe_layout_tab2:
               // intentActivity(getActivity(), CTradeListActivity.class, false);
                intentActivity(getActivity(), HMoneyListActivity.class,false);
                break;
            case R.id.myzoe_layout_tab3:
                intentActivity(getActivity(), HReturnListActivity.class, false);
                break;
            case R.id.myzoe_layout_tab4:
              intentActivity(getActivity(), CTradeListActivity.class, false);

             /*   String s=tvTab4.getText().toString();
                if (Double.valueOf(s.substring(s.indexOf("￥") + 1)) < 0.01) {
                    showText(getString(R.string.himt_extract_money_limt_errow));
                    return;
                }
                selectPopWindow(v);*/
                break;

        }

    }


    /**
     * 访问网络初始化数据
     */
    private void initDatas() {
        Map<String, String> params = new HashMap<>();
        String id=getSharedValue(getActivity(), "userID", "null");
        if (id.equals("null")){
            showText(getString(R.string.query_out_login));
            closeLoadingDialog();
            return;
        }
        params.put("id", id);
        OkHttpClientManager.postAsyn(Constant.MYZOE_INDEX, new OkHttpClientManager.ResultCallback<MyzoeType<MyzoeTypeInfo>>() {
            @Override
            public void onError(Request request, Exception e) {
                closeLoadingDialog();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onResponse(MyzoeType<MyzoeTypeInfo> response) {
                closeLoadingDialog();
                swipeRefreshLayout.setRefreshing(false);
                Log.d("response", "onResponse() called with: " + "response = [" + response + "]");
                if (response.getResult().equals("0")) {
                    showText(response.getDetail());
                    return;
                } else if (response.getResult().equals("1")) {
                    if (response.getInfor() != null) {
                        setDatas(response.getInfor());
                    }
                }

            }
        }, params);
    }

    /**
     * 设置用户信息
     */
    private void setDatas(MyzoeTypeInfo response) {
        loginContentLayout.setVisibility(View.GONE);
        initOnclick();
        if (!TextUtils.isEmpty(response.getHead())) {
            ImageLoader.getInstance().displayImage(
                    Constant.SERVER_IMAGE + response.getHead(), loginIcon, ImageUtilsView.imageOption());
        }
        if (!TextUtils.isEmpty(response.getRellname())) {
            rellName.setText("欢迎您 " + response.getUsername() + "   ");
        }
        if (!TextUtils.isEmpty(response.getUsername())) {
            userName.setText("账户名： " + response.getRellname() + "");
        }
        if (!TextUtils.isEmpty(response.getMoneys())) {
            setSharedPreferences(getActivity(), "hiltValues", response.getMoneys());
            tvTab1.setText("￥ " + response.getMoneys() + "");
        }
        if (!TextUtils.isEmpty(response.getFreeze())) {
            tvTab2.setText("￥ " + response.getFreeze() + "");
        }
        if (!TextUtils.isEmpty(response.getTurnover())) {
            tvTab3.setText("￥ " + response.getTurnover() + "");
        }
        if (!TextUtils.isEmpty(response.getTransfer())) {
            tvTab4.setText("￥ " + response.getTransfer() + "");
        }

    }

    /**
     * 选择转让股票
     */
    private void selectPopWindow(View parent) {
        if (selectPopuWindow == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
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
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();

        lp.alpha = 0.4f;

        getActivity().getWindow().setAttributes(lp);
        // popuWindow1.setOutsideTouchable(true);
        selectPopuWindow.setTouchable(true);
        selectPopuWindow.setFocusable(true);

        selectPopuWindow.showAtLocation((View) parent.getParent(),
                Gravity.CENTER_HORIZONTAL, 0, 0);// 设置弹出的位置
        selectPopuWindow.update();
        selectPopuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            // 在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
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
                boolean MONEY_TYPE=regular.MoneyType(moneyDatas);
                if (!MONEY_TYPE){
                    showText(getString(R.string.himt_money_format_errow));
                    return;
                }
                if (TextUtils.isEmpty(moneyDatas)) {
                    showText(getString(R.string.himt_extract_noeny));
                    return;
                }
                if (isConnectivity(getActivity())) {
                    showLoadingDialog();
                    forTransfer(moneyDatas);
                } else {
                    showText(getString(R.string.hint_intent));
                }
                from_stock.setText("");
            }
        });
    }

    private void forTransfer(String moneyDatas) {
        Map<String, String> params = new HashMap<>();
        String id=getSharedValue(getActivity(), "userID", "null");
        if (id.equals("null")){
            showText(getString(R.string.query_out_login));
            closeLoadingDialog();
            return;
        }
        params.put("id", id);
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
