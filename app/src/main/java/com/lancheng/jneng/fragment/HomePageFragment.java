package com.lancheng.jneng.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lancheng.jneng.R;
import com.lancheng.jneng.activity.HInvestmentDeailsActivity;
import com.lancheng.jneng.activity.HInvestmentListActivity;
import com.lancheng.jneng.activity.HRegisterActivity;
import com.lancheng.jneng.activity.LoginActivity;
import com.lancheng.jneng.activity.ShareActivity;
import com.lancheng.jneng.base.Constant;
import com.lancheng.jneng.entity.BaseList;
import com.lancheng.jneng.entity.HomeBannerType;
import com.lancheng.jneng.entity.HomeBannerTypeInfo;
import com.lancheng.jneng.entity.InvestmentTypeInfo;
import com.lancheng.jneng.utils.Commons;
import com.lancheng.jneng.utils.OkHttpClientManager;
import com.lancheng.jneng.view.ADTextView;
import com.lancheng.jneng.view.Banner;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by aflf on 2016-06-03.
 */
public class HomePageFragment extends BaseFragment implements View.OnClickListener {
    //定义三个item选项
    private RelativeLayout homepage_item1, homepage_item2, homepage_item3;
    private Banner banner;
    private List<String> imagesDatas = new ArrayList<>();
    private Integer[] defaultImgs = new Integer[]{R.drawable.pic_banner_special};

    private SwipeRefreshLayout swipeRefreshLayout;
    private ADTextView descMessage;//通知信息
    private List<InvestmentTypeInfo> messageDatas = new ArrayList<>();
    private LinearLayout contentLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = View.inflate(getActivity(), R.layout.fragment_homepage,
                null);

        intview(view);
        if (Commons.isConnectivity(getActivity())) {
            showLoadingDialog();
            initData();
            initMessageData();
        } else {
            contentLayout.setVisibility(View.VISIBLE);
            showText(getString(R.string.hint_intent));
        }

        return view;

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.d("aflf", "hidden true");
        } else {
            Log.d("aflf", "hidden false");
            if (imagesDatas.size() == 0 || messageDatas.size() == 0) {
                if (Commons.isConnectivity(getActivity())) {
                    showLoadingDialog();
                    initData();
                    initMessageData();
                }
            }
        }
    }

    private void initData() {
        Map<String, String> params = new HashMap<>();
        params.put("id", "1");
        params.put("number", "5");
        OkHttpClientManager.postAsyn(Constant.HOME_ADLIST, new OkHttpClientManager.ResultCallback<HomeBannerType<HomeBannerTypeInfo>>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.e("aflf", "exception：" + e.toString());
                closeLoadingDialog();
                swipeRefreshLayout.setRefreshing(false);
                contentLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onResponse(final HomeBannerType<HomeBannerTypeInfo> response) {
                Log.d("aflf", "success：" + response.toString());
                if (response.getList() != null) {
                    imagesDatas.clear();
                    for (int i = 0; i < response.getList().size(); i++) {
                        imagesDatas.add(i, Constant.SERVER_IMAGE + response.getList().get(i).getImgurl());
                        Log.d("url", Constant.SERVER_IMAGE + response.getList().get(i).getImgurl());
//                     /   imagesDatas.add(i, "Constant.SERVER_HOST" + response.getList().get(i).getImgurl));
                    }
 /*  banner.setOnBannerClickListener(new Banner.OnBannerClickListener() {
                        @Override
                        public void OnBannerClick(View view, int position) {
                            Log.d("position", "OnBannerClick() called with: " + "view = [" + view + "], position = [" + position + "]");
                            if (isConnectivity(getActivity())) {
                                //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
                                Intent intent = new Intent(getActivity(), CNewsDetailsActivity.class);
                                //用Bundle携带数据
                                Bundle bundle = new Bundle();
                                //传递name参数为tinyphp
                                bundle.putString("linkurl", response.getList().get(position-1).getLinkurl());
                                bundle.putString("keystate", "banner");
                                bundle.putString("id","");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } else {
                                showText(getString(R.string.hint_intent));
                            }
                        }
                    });*/
                }
                Log.d("size", "size " + imagesDatas.size());

                banner.setImages(imagesDatas);
                swipeRefreshLayout.setRefreshing(false);
                contentLayout.setVisibility(View.VISIBLE);
                closeLoadingDialog();
            }
        }, params);
    }

    /**
     * 加载数据通知数据
     */
    private void initMessageData() {
        Map<String, String> params = new HashMap<>();
        params.put("id", "1");
        params.put("page", "1");
        params.put("pagesize", "10");
        OkHttpClientManager.postAsyn(Constant.Investment_LIST, new OkHttpClientManager.ResultCallback<BaseList<InvestmentTypeInfo>>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.d("response", "onError() called with: " + "request = [" + request + "], e = [" + e + "]");
            }

            @Override
            public void onResponse(BaseList<InvestmentTypeInfo> response) {
                Log.d("response", "onResponse() called with: " + "response = [" + response + "]");
                if (response.getList() != null) {
                    messageDatas.clear();
                    messageDatas.addAll(response.getList());
                    test(messageDatas);
                }
            }
        }, params);
    }

    private void test(final List<InvestmentTypeInfo> messageDatas) {
        descMessage.setmTexts(messageDatas);
        descMessage.setFrontColor(R.color.color4e4e4e);
        descMessage.setBackColor(R.color.color4e4e4e);
        descMessage.setSpeed(3);
        descMessage.setOnClickListener(new ADTextView.onClickListener() {
            @Override
            public void onClick(String mUrl) {
                //showText("mURL:"+mUrl);
                if (isConnectivity(getActivity())) {
                    //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
                    Intent intent = new Intent(getActivity(), HInvestmentDeailsActivity.class);
                    //用Bundle携带数据
                    Bundle bundle = new Bundle();
                    //传递name参数为tinyphp
                    bundle.putString("id", mUrl);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    showText(getString(R.string.hint_intent));
                }
            }
        });
    /*    descMessage.setmFrontTextSize(R.dimen.ytext18);
        descMessage.setmContentTextSize(R.dimen.ytext22);*/

    }


    /**
     * 初始化控件
     */
    private void intview(View view) {
        //主体页面
        contentLayout = (LinearLayout) view.findViewById(R.id.homepage_content_layout);
        banner = (Banner) view.findViewById(R.id.banner);
        descMessage = (ADTextView) view.findViewById(R.id.homepage_message);
        //初始化三个item
        homepage_item1 = (RelativeLayout) view.findViewById(R.id.homepage_item1);
        homepage_item2 = (RelativeLayout) view.findViewById(R.id.homepage_item2);
        homepage_item3 = (RelativeLayout) view.findViewById(R.id.homepage_item3);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.homepage_swip);
        //设置样式
        banner.setBannerStyle(Banner.CIRCLE_INDICATOR);
        banner.setBannerTitle(null);
        banner.setIndicatorGravity(Banner.CENTER);
        banner.setSwipeRefreshLayout(swipeRefreshLayout);
        banner.setDelayTime(5000);//设置轮播间隔时间
        banner.setImages(defaultImgs);
        Log.d("size", "size " + imagesDatas.size());
        // banner.setImages(imagesDatas);//可以选择设置图片网址，或者资源文件，默认用Glide加载\
  /*      banner.setOnBannerClickListener(new Banner.OnBannerClickListener() {
            @Override
            public void OnBannerClick(View view, int position) {
                showText("您点击了" + position);
                intentActivity(getActivity(), LoginActivity.class, false);
            }
        });*/
        swipeRefreshLayout.setColorSchemeResources(R.color.titlebackgourndColor);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Commons.isConnectivity(getActivity())) {
                    showLoadingDialog();
                    initData();
                    if (messageDatas.size()==0){
                        initMessageData();
                    }
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    showText(getString(R.string.hint_intent));
                }
            }
        });
        intOnClickListener();
    }

    /**
     * 初始化控件的点击事件
     */
    private void intOnClickListener() {
        homepage_item1.setOnClickListener(this);
        homepage_item2.setOnClickListener(this);
        homepage_item3.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homepage_item1:
                Commons.intentActivity(getActivity(), HRegisterActivity.class, false);
                break;
            case R.id.homepage_item3:
                if (isConnectivity(getActivity())) {
                    intentActivity(getActivity(), HInvestmentListActivity.class, false);
                } else {
                    showText(getString(R.string.hint_intent));
                }
                break;
            case R.id.homepage_item2:
                if (isConnectivity(getActivity())) {
                    isLoginState(ShareActivity.class, false);

                } else {
                    showText(getString(R.string.hint_intent));
                }
                break;

        }
    }

    /**
     * 判断是否登陆状态 如果登陆了进入相关的页面，如果没有登陆进行登陆
     *
     * @param
     */
    private void isLoginState(Class<?> cls, boolean isfinish) {
        String loginState = getSharedValue(getActivity(), "userID", "-1");
        if (loginState.equals("-1")) {
            startActivity(new Intent(getActivity(),
                    LoginActivity.class));
            setSharedPreferences(getActivity(), "resultState", "1");
        } else {
            Intent intent = new Intent(getActivity(), cls);
            startActivity(intent);
            if (isfinish) {
                getActivity().finish();
            }

        }
    }

    @Override
    public void onStop() {
        Log.d("onstop", "onStop: ");
        banner.isAutoPlay(false);
        super.onStop();
    }
}
