package com.lancheng.jneng.activity.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.lancheng.jneng.R;
import com.lancheng.jneng.activity.BaseActivity;
import com.lancheng.jneng.activity.MainActivity;
import com.lancheng.jneng.adapter.GuideViewPagerAdapter;

import java.util.ArrayList;

public class GuideActivity extends BaseActivity implements OnClickListener {

    private ArrayList<View> guideViews;
    private GuideViewPagerAdapter guideViewPagerAdapter;

    private Button guide_start_btn;
    private ImageView[] guide_dot_iv;
    private ViewPager guide_viewpager;
    private View guideView1, guideView2, guideView3;
    // 记录当前的页数
    private int pos = 0;
    private int maxPos = 2;
    private int currentPageScrollStatus;
    private boolean goNextFlag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        setSharedPreferences(this, "splashvalues", "1");
        initViews();
        initValues();
        initListeners();
    }

    @SuppressLint("InflateParams")
    private void initViews() {
        // TODO Auto-generated method stub
        guide_dot_iv = new ImageView[3];
        guide_dot_iv[0] = (ImageView) findViewById(R.id.guide_dot1_iv);
        guide_dot_iv[1] = (ImageView) findViewById(R.id.guide_dot2_iv);
        guide_dot_iv[2] = (ImageView) findViewById(R.id.guide_dot3_iv);
        guide_viewpager = (ViewPager) findViewById(R.id.guide_viewpager);

        guideView1 = LayoutInflater.from(this).inflate(
                R.layout.activity_guide_view_i, null);
        guideView2 = LayoutInflater.from(this).inflate(
                R.layout.activity_guide_view_ii, null);
        guideView3 = LayoutInflater.from(this).inflate(
                R.layout.activity_guide_view_iii, null);
        guide_start_btn = (Button) guideView3
                .findViewById(R.id.guide_start_btn);
    }

    private void initListeners() {
        // TODO Auto-generated method stub
        guide_start_btn.setOnClickListener(this);

        guide_viewpager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub\
                pos = position;
                selectPage(position);

            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                if (pos == 0) {
                    // 如果offsetPixels是0页面也被滑动了，代表在第一页还要往左划
                    if (positionOffsetPixels <= 0
                            && currentPageScrollStatus == 1) {
                        goNextFlag = false;

                    }
                } else if (pos == maxPos) {
                    // 已经在最后一页还想往右划
                    if (positionOffsetPixels == 0
                            && currentPageScrollStatus == 1) {
                        goNextFlag = true;

                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // TODO Auto-generated method stub
                // 记录page滑动状态，如果滑动了state就是1
                currentPageScrollStatus = state;
                if (goNextFlag) {
                    GoToMainActivity();
                }
            }
        });

    }

    private void initValues() {
        // TODO Auto-generated method stub
        guideViews = new ArrayList<View>();
        guideViews.add(guideView1);
        guideViews.add(guideView2);
        guideViews.add(guideView3);
        guideViewPagerAdapter = new GuideViewPagerAdapter(guideViews);
        guide_viewpager.setAdapter(guideViewPagerAdapter);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.guide_start_btn:
                Animation animation = AnimationUtils.loadAnimation(GuideActivity.this, R.anim.anim_scale);
                guide_start_btn.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        GoToMainActivity();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                break;

            default:
                break;
        }
    }

    /**
     * 浮点显示控制
     *
     * @param current
     */
    private void selectPage(int current) {
        for (int i = 0; i < guide_dot_iv.length; i++) {
            guide_dot_iv[current]
                    .setImageResource(R.drawable.guide_dot_pressed);
            if (current != i) {
                guide_dot_iv[i].setImageResource(R.drawable.guide_dot_normal);
            }
        }
    }

    /**
     * 进入主界面
     */
    void GoToMainActivity() {
        Intent i = new Intent(GuideActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

}
