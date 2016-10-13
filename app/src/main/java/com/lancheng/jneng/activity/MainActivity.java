package com.lancheng.jneng.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lancheng.jneng.R;
import com.lancheng.jneng.fragment.CommunityFragment;
import com.lancheng.jneng.fragment.HomePageFragment;
import com.lancheng.jneng.fragment.MoreFragment;
import com.lancheng.jneng.fragment.MyZoeFragment;
import com.lancheng.jneng.view.YLSlidingLayout;

public class MainActivity extends BaseActivity implements View.OnClickListener, SlidingPaneLayout.PanelSlideListener {
    /**
     * 定义底端四个button页面
     */
    private LinearLayout tab1_xml;
    private LinearLayout tab2_xml;
    private LinearLayout tab3_xml;
    private LinearLayout tab4_xml;

    private ImageView bt1;
    private ImageView bt2;
    private ImageView bt3;
    private ImageView bt4;

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;

    private HomePageFragment hompagefragmet;
    private CommunityFragment communityfragment;
    private MyZoeFragment myzoefragment;
    private MoreFragment morefragment;

    /**
     * c侧滑菜单
     */
    private YLSlidingLayout slidepanel;
    //投资列表，回报详情，交易详情，资金详情，最新动态,平台贷款
    private LinearLayout layoutInvestList, layoutReturnDetails,
            layoutTradeDetails, layoutMoneyDetails, layoutNews, layoutCredit;
    private RelativeLayout menuLayout;
    private Button btn_menuSwitch;//打开侧滑菜单

    /**
     * 标题栏的描述信息
     */
    private TextView titleDesc;

    private long startTime, endTime;

    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniview();
        showTab(i);
    }

    /**
     * 初始化控件
     */
    private void iniview() {
        // button控件
        bt1 = (ImageView) findViewById(R.id.bt1);
        bt2 = (ImageView) findViewById(R.id.bt2);
        bt3 = (ImageView) findViewById(R.id.bt3);
        bt4 = (ImageView) findViewById(R.id.bt4);
        // textview控件
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        // linearlayout
        tab1_xml = (LinearLayout) findViewById(R.id.tab1_xml);
        tab2_xml = (LinearLayout) findViewById(R.id.tab2_xml);
        tab3_xml = (LinearLayout) findViewById(R.id.tab3_xml);
        tab4_xml = (LinearLayout) findViewById(R.id.tab4_xml);

        //侧滑菜单
        slidepanel = (YLSlidingLayout) findViewById(R.id.slidepanel);
        slidepanel.setSliderFadeColor(Color.TRANSPARENT);
        menuLayout = (RelativeLayout) this.findViewById(R.id.id_left);
        btn_menuSwitch = (Button) findViewById(R.id.btn_menuSwitch);

        //投资列表，回报详情，交易详情，资金详情，最新动态
        layoutInvestList = (LinearLayout) findViewById(R.id.menu_layout1);
        layoutReturnDetails = (LinearLayout) findViewById(R.id.menu_layout2);
        layoutTradeDetails = (LinearLayout) findViewById(R.id.menu_layout3);
        layoutMoneyDetails = (LinearLayout) findViewById(R.id.menu_layout4);
        layoutNews = (LinearLayout) findViewById(R.id.menu_layout5);
        layoutCredit = (LinearLayout) findViewById(R.id.menu_layout6);
        menuLayout = (RelativeLayout) this.findViewById(R.id.id_left);
        //标题栏描述信息
        titleDesc = (TextView) findViewById(R.id.layout_titleDesc);
        intOnclick();


    }

    /**
     * 初始化监听
     */
    private void intOnclick() {
        // 控件点击事件
        tab1_xml.setOnClickListener(this);
        tab2_xml.setOnClickListener(this);
        tab3_xml.setOnClickListener(this);
        tab4_xml.setOnClickListener(this);
        btn_menuSwitch.setOnClickListener(this);
        //menu页面的监听事件注册
        layoutInvestList.setOnClickListener(this);
        layoutReturnDetails.setOnClickListener(this);
        layoutTradeDetails.setOnClickListener(this);
        layoutMoneyDetails.setOnClickListener(this);
        layoutNews.setOnClickListener(this);
        layoutCredit.setOnClickListener(this);
        menuLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startTime = System.currentTimeMillis();
                        return true;
                    case MotionEvent.ACTION_UP:
                        endTime = System.currentTimeMillis();
                        if (endTime - startTime > 100) {
                            if (!slidepanel.isOpen()) {
                                slidepanel.openPane();
                            } else {
                                slidepanel.closePane();
                            }
                        }
                        return true;
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (slidepanel.isOpen()) {
            slidepanel.closePane();
        } else {
            finish();
        }
//        super.onBackPressed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int index = intent.getIntExtra("index", 1);
        showTab(index - 1);
    }

    private void showTab(int index) {
        iniData();
        // TODO Auto-generated method stub
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (index) {
            case 0:
                hideAllTab(ft, 0);
                if (hompagefragmet == null) {
                    hompagefragmet = new HomePageFragment();
                    ft.add(R.id.tab_content, hompagefragmet);

                } else {
                    ft.show(hompagefragmet);
                }
                tab1_xml.setBackgroundResource(R.color.bottontitlebackgourndColorPre);
                break;
            case 1:
                hideAllTab(ft, 1);
                if (myzoefragment == null) {
                    myzoefragment = new MyZoeFragment();
                    ft.add(R.id.tab_content, myzoefragment);
                } else {
                    ft.show(myzoefragment);
                }
                tab2_xml.setBackgroundResource(R.color.bottontitlebackgourndColorPre);
                break;
            case 2:
                hideAllTab(ft, 2);
                if (communityfragment == null) {
                    communityfragment = new CommunityFragment();
                    ft.add(R.id.tab_content, communityfragment);
                } else {
                    ft.show(communityfragment);
                }
                tab3_xml.setBackgroundResource(R.color.bottontitlebackgourndColorPre);
                break;
            case 3:
                hideAllTab(ft, 3);
                if (morefragment == null) {
                    morefragment = new MoreFragment();
                    ft.add(R.id.tab_content, morefragment);
                } else {
                    ft.show(morefragment);
                }
                tab4_xml.setBackgroundResource(R.color.bottontitlebackgourndColorPre);
                break;

        }
        ft.commitAllowingStateLoss();
    }

    /**
     * 显示隐藏碎片文件
     *
     * @param ft
     */
    private void hideAllTab(FragmentTransaction ft, int index) {
        // TODO Auto-generated method stub
        if (hompagefragmet != null && index != 0) {
            ft.hide(hompagefragmet);
        }
        if (myzoefragment != null && index != 1) {
            ft.hide(myzoefragment);
        }
        if (communityfragment != null && index != 2) {
            ft.hide(communityfragment);
        }

        if (morefragment != null && index != 3) {
            ft.hide(morefragment);
        }

    }

    /**
     * 初始化点击数据
     */
    private void iniData() {
        tab1_xml.setBackgroundResource(R.color.titlebackgourndColor);
        tab2_xml.setBackgroundResource(R.color.titlebackgourndColor);
        tab3_xml.setBackgroundResource(R.color.titlebackgourndColor);
        tab4_xml.setBackgroundResource(R.color.titlebackgourndColor);
    }

    /**
     * 初始化menu菜单的item的颜色值
     */
    private void initMenuData() {
        layoutInvestList.setBackgroundResource(R.color.menuitembackgrounbColor);
        layoutReturnDetails.setBackgroundResource(R.color.menuitembackgrounbColor);
        layoutTradeDetails.setBackgroundResource(R.color.menuitembackgrounbColor);
        layoutMoneyDetails.setBackgroundResource(R.color.menuitembackgrounbColor);
        layoutNews.setBackgroundResource(R.color.menuitembackgrounbColor);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab1_xml:
                showTab(0);
                titleDesc.setText(getString(R.string.title_one));
                break;
            case R.id.tab2_xml:
                showTab(1);
                titleDesc.setText(getString(R.string.title_myzoe));
                break;
            case R.id.tab3_xml:
                showTab(2);
                titleDesc.setText(getString(R.string.title_comunity));
                break;
            case R.id.tab4_xml:
                showTab(3);
                titleDesc.setText(getString(R.string.title_more));
                break;
            case R.id.btn_menuSwitch:
                if (!slidepanel.isOpen()) {
                    slidepanel.openPane();
                } else {
                    slidepanel.closePane();
                }
                break;
            case R.id.menu_layout1:
                intentActivity(this, HInvestmentListActivity.class, false);
                break;
            case R.id.menu_layout2:
                isLoginState(HReturnListActivity.class, false);
                break;
            case R.id.menu_layout3:
                isLoginState(UpdatePasswordActivity.class, false);
                break;
            case R.id.menu_layout4:
                isLoginState(HMoneyListActivity.class, false);
                break;
            case R.id.menu_layout5:
                intentActivity(this, HNewsActivity.class, false);
                break;
            case R.id.menu_layout6:
                showText("建设中，敬请期待~");
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //  super.onSaveInstanceState(outState);
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {

    }

    @Override
    public void onPanelOpened(View panel) {

    }

    @Override
    public void onPanelClosed(View panel) {

    }

    /**
     * 判断是否登陆状态 如果登陆了进入相关的页面，如果没有登陆进行登陆
     *
     * @param
     */
    private void isLoginState(Class<?> cls, boolean isfinish) {
        String loginState = getSharedValue(this, "userID", "-1");
        if (loginState.equals("-1")) {
            startActivity(new Intent(this,
                    LoginActivity.class));
            setSharedPreferences(this, "resultState", "1");
        } else {
            if (isConnectivity(this)) {
                Intent intent = new Intent(this, cls);
                startActivity(intent);
                if (isfinish) {
                    finish();
                }
            } else {
                showText(getString(R.string.hint_errow));
            }
        }
    }
}


