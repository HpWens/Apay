package com.lancheng.jneng.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

/**
 * 此adapter是用于滚动新闻的图片
 * 
 * @author Administrator
 * 
 */
public class MyHomeAdapter extends PagerAdapter {
	private ImageView[] imageViews;
	@SuppressWarnings("unused")
	// private FragmentActivity fragge;
	private Context mContext;

	// private List<goodsData> viewPagerData;

	public MyHomeAdapter(Context mContext, ImageView[] imageViews) {
		this.imageViews = imageViews;
		this.mContext = mContext;
		// this.viewPagerData = viewPagerData;
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE / 2;
		// return images.length;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		// ((ViewPager) container).removeView(imageViews[position
		// % imageViews.length]);

	}

	/**
	 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
	 */
	@Override
	public Object instantiateItem(View container, final int position) {

		View view = null;

		if (imageViews.length == 0) {
			return view;
		}
		if (position % imageViews.length < 0) {
			view = imageViews[imageViews.length + position];
		} else {
			view = imageViews[position % imageViews.length];
		}
		ViewParent vp = view.getParent();
		if (vp != null) {
			ViewGroup parent = (ViewGroup) vp;
			parent.removeView(view);
		}

		((ViewPager) container).addView(view);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		return view;
	}

}
