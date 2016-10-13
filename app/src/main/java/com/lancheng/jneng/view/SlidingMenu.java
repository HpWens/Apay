package com.lancheng.jneng.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.lancheng.jneng.R;

/**
 * Created by hyh on 16/6/2.
 */
public class SlidingMenu extends HorizontalScrollView {
    private ViewGroup menuLayout, contentLayout;

    private LinearLayout mWapper;

    private boolean isMeasured, isOpen;

    private int mScreenWidth, menuWidth, rightPadding;

    private long startTime, endTime;

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//获取自定义的属性
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SlidingMenu, defStyle, 0);
        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.SlidingMenu_menuRightPadding:
                    rightPadding = typedArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics()));
                    break;
            }
        }

        typedArray.recycle();
        WindowManager vm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        vm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        rightPadding = mScreenWidth / 3;
    }

    /**
     * 打开菜单
     */
    public void openMenu() {
        if (isOpen)
            return;
        this.smoothScrollTo(0, 0);
//        int childCount = menuLayout.getChildCount();
//
//        for (int i=0;i<childCount;i++){
//            menuLayout.getChildAt(i).setClickable(false);
//            menuLayout.getChildAt(i).setFocusable(false);
//            menuLayout.getChildAt(i).setEnabled(false);
//        }
        isOpen = true;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void closeMenu() {
        if (!isOpen) return;
        this.smoothScrollTo(menuWidth, 0);
//        int childCount = menuLayout.getChildCount();
//
//        for (int i=0;i<childCount;i++){
//            menuLayout.getChildAt(i).setClickable(true);
//            menuLayout.getChildAt(i).setFocusable(true);
//            menuLayout.getChildAt(i).setEnabled(true);
//        }
        isOpen = false;
    }

    public void close() {
        scrollTo(menuWidth, 0);
    }

    /**
     * 切换菜单
     */
    public void toggleMenu() {
        if (isOpen) {

            closeMenu();
        } else {

            openMenu();

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isMeasured) {
            mWapper = (LinearLayout) this.getChildAt(0);
            menuLayout = (ViewGroup) mWapper.getChildAt(0);
            contentLayout = (ViewGroup) mWapper.getChildAt(1);
            menuWidth = menuLayout.getLayoutParams().width = mScreenWidth - rightPadding;
            contentLayout.getLayoutParams().width = mScreenWidth;
            isMeasured = true;
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            this.scrollTo(menuWidth, 0);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                Log.d("HYH", "onTouchEvent: down " + startTime);
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                Log.d("HYH", "onTouchEvent: down " + startTime);
                return true;
            case MotionEvent.ACTION_UP:
                endTime = System.currentTimeMillis();
                Log.d("HYH", "onTouchEvent: " + (endTime - startTime));
                if (endTime - startTime < 200) {
                    int scrollX = getScrollX();
                    Log.e("HYH", "onTouchEvent: scrollX "+scrollX + "  menuWith:" + menuWidth);
                    if(!isOpen){
                        if (scrollX >= menuWidth) {
                            this.smoothScrollTo(menuWidth, 0);
                            isOpen = false;
                        } else {
                            this.smoothScrollTo(0, 0);
                            isOpen = true;
                        }
                    }else{
                        if (scrollX > 0) {
                            this.smoothScrollTo(menuWidth, 0);
                            isOpen = false;
                        } else {
                            this.smoothScrollTo(0, 0);
                            isOpen = true;
                        }
                    }
                    return true;
                }

                int scrollX = getScrollX();
                Log.e("HYH", "onTouchEvent: scrollX "+scrollX );
                if (scrollX > menuWidth / 2) {
                    this.smoothScrollTo(menuWidth, 0);
                    isOpen = false;
                } else {

                    this.smoothScrollTo(0, 0);
                    isOpen = true;
                }
                return true;

        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

//        float scale = l * 1.0f / menuWidth;
//        float leftScale = 1 - 0.3f * scale;
//        float rightScale = 0.8f + scale * 0.2f;

        super.onScrollChanged(l, t, oldl, oldt);
//
//        ViewHelper.setScaleX(menuLayout, leftScale);
//        ViewHelper.setScaleY(menuLayout, leftScale);
//        ViewHelper.setAlpha(menuLayout, 0.6f + 0.4f * (1 - scale));
//        ViewHelper.setTranslationX(menuLayout, menuWidth * scale * 0.7f);

//        ViewHelper.setPivotX(contentLayout, 0);
//        ViewHelper.setPivotY(contentLayout, contentLayout.getHeight()/2);
//        ViewHelper.setScaleX(contentLayout, rightScale);
//        ViewHelper.setScaleY(contentLayout, rightScale);

    }


}
