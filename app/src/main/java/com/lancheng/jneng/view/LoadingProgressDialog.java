package com.lancheng.jneng.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lancheng.jneng.R;


/**
 * @Description:自定义对话框在进入的一个页面的时候显示
 * @author
 */
public class LoadingProgressDialog extends ProgressDialog {

	private AnimationDrawable mAnimation;
	private Context mContext;
	private ImageView mImageView;
	private int count = 0;
	private int mResid;
	private boolean textState;
	private TextView loadingtext;

	public LoadingProgressDialog(Context context,boolean s) {
		super(context);
		this.mContext = context;
	//	this.mResid = id;
		this.textState=s;
		setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		// initData();
	}

	private void initData() {

		mImageView.setBackgroundResource(mResid);
		// 通过ImageView对象拿到背景显示的AnimationDrawable
		mAnimation = (AnimationDrawable) mImageView.getBackground();
		// 为了防止在onCreate方法中只显示第一帧的解决方案之一
		mImageView.post(new Runnable() {
			@Override
			public void run() {
				mAnimation.start();

			}
		});

	}

	private void initView() {
		setContentView(R.layout.loadingprogress_dialog);
		loadingtext=(TextView) findViewById(R.id.otherloadingIv);
		if(textState){
			loadingtext.setVisibility(View.VISIBLE);
		}else{

		}
		// mImageView = (ImageView) findViewById(R.id.loadingIv);

	}

	// @Override
	// public void onWindowFocusChanged(boolean hasFocus) {
	// // TO Auto-generated method stub
	// mAnimation.start();
	// super.onWindowFocusChanged(hasFocus);
	// }
	/*
	 * 点击屏幕其他的地方不退出进度对话框
	 * 
	 * @see android.app.Dialog#setCanceledOnTouchOutside(boolean)
	 */
	@Override
	public void setCanceledOnTouchOutside(boolean cancel) {
		// TODO Auto-generated method stub
		super.setCanceledOnTouchOutside(false);
	}
}
