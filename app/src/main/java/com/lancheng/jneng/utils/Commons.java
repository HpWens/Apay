package com.lancheng.jneng.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;

@SuppressLint("SetJavaScriptEnabled")
public class Commons {
	/** 最后一次点击时间 */
	public static long mLastClickTime;
	/** 第一次点击返回的时间 */
	public static long mFirstBackClickTime;

	/**
	 * 跳转activity
	 * 
	 * @param context
	 *            当前activity
	 * @param cls
	 *            跳转activity
	 * @param isfinish
	 *            是否结束当前activity
	 */
	public static void intentActivity(Context context, Class<?> cls,
			boolean isfinish) {
		Intent intent = new Intent(context, cls);
		context.startActivity(intent);
		if (isfinish)
			((Activity) context).finish();
	}

	/**
	 * 
	 * @param fm
	 *            Fragment管理器
	 */
	public static void popBackStackWeb(FragmentManager fm) {
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fm.popBackStack();
		fragmentTransaction.commitAllowingStateLoss();

	}

	/**
	 * 跳转activity
	 * 
	 * @param context
	 *            当前activity
	 * @param cls
	 *            跳转activity
	 * @param bundle
	 *            捎带数据
	 * @param isfinish
	 *            是否结束当前activity
	 */
	public static void intentActivity(Context context, Class<?> cls,
			Bundle bundle, boolean isfinish) {
		Intent intent = new Intent(context, cls);
		intent.putExtra("Bundle", bundle);
		context.startActivity(intent);
		if (isfinish)
			((Activity) context).finish();
	}

	/**
	 * 登录的跳转
	 * 
	 * @param context
	 *            当前activity
	 * 
	 */
	public static void loginSkip(Context context) {
		Intent intent = new Intent(context, null);
		context.startActivity(intent);

	}

	/**
	 * 判断点击有效性，防止重复点击
	 * 
	 * @return false表示重复点击（无效操作）
	 */
	public static boolean clickEffect() {
		if (System.currentTimeMillis() - mLastClickTime < 1500)
			return false;
		mLastClickTime = System.currentTimeMillis();
		return true;
	}

	/**
	 * 判断点击有效性，防止重复点击
	 * 
	 * @return false表示重复点击（无效操作）
	 */
	public static boolean clickEffectmain() {
		if (System.currentTimeMillis() - mLastClickTime < 300)
			return false;
		mLastClickTime = System.currentTimeMillis();
		return true;
	}

	/**
	 * 设置Preference数据存储
	 * 
	 * @param context
	 * @param key
	 * @param value
	 * @throws RuntimeException
	 */
	public static void setSharedPreferences(Context context, String key,
			String value) throws RuntimeException {
		SharedPreferences pre = getSharedPreferences(context);
		SharedPreferences.Editor e = pre.edit();
		e.putString(key, value);
		e.commit();
	}

	/**
	 * 获取Preference数据存储对象
	 *
	 * @param context
	 * @return
	 * @throws RuntimeException
	 */
	public static SharedPreferences getSharedPreferences(Context context)
			throws RuntimeException {

		String edit ="data";
		int type = Context.MODE_PRIVATE;
		return context.getSharedPreferences(edit, type);
	}

	/**
	 * 获取preference 对象中键所对应的值
	 * 
	 * @param context
	 * @param key
	 * @param defvalue
	 * @return
	 */
	public static String getSharedValue(Context context, String key,
			String defvalue) {
		return getSharedPreferences(context).getString(key, defvalue);
	}

	/**
	 * 获取包信息.
	 * 
	 * @param context
	 *            the context
	 */
	public static PackageInfo getPackageInfo(Context context) {
		PackageInfo info = null;
		try {
			String packageName = context.getPackageName();
			info = context.getPackageManager().getPackageInfo(packageName,
					PackageManager.GET_ACTIVITIES);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	/**
	 * Toast 提示
	 * 
	 * @param context
	 * @param message
	 */
	public static void toast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Toadt方法 显示在屏幕上方
	 */
	public static void toastTop(Context context, String text) {
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP, 0, 100);
		toast.show();

	}

	/**
	 * 设置全屏
	 * 
	 * @param
	 */
	public static void setFullScreen(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);// 取消标题栏
		activity.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 全屏
	}


	/**
	 * 
	 * 描述：是否有网络连接.
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnectivity(Context context) {

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return ((connectivityManager.getActiveNetworkInfo() != null && connectivityManager
				.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || telephonyManager
				.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}

	/**
	 * 获取屏幕的宽高
	 * 
	 * @param activity
	 * @return 0 宽 1 高
	 */
	public static int[] getScreenWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screen[] = new int[2];
		screen[0] = dm.widthPixels;
		screen[1] = dm.heightPixels;
		return screen;
	}

	/**
	 * 获取控件的宽高
	 * 
	 * @param view
	 * @return 0 宽 1 高
	 */
	public static int[] getWidgetWidthAndHeight(View view) {
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		int height = view.getMeasuredHeight();
		int width = view.getMeasuredWidth();
		return new int[] { width, height };

	}

	/**
	 * 双击退出程序
	 */
	public static void equitAppWithTwoBackClick(Context context) {
		if (System.currentTimeMillis() - mFirstBackClickTime > 2000) {
			mFirstBackClickTime = System.currentTimeMillis();
			toast(context, "再次点击,退出程序");
		} else {
			((Activity) context).finish();
			// android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	/**
	 * 获取文件夹大小
	 * 
	 * @param file
	 *            File实例
	 * @return long
	 */
	public static long getFolderSize(File file) {

		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);

				} else {
					size = size + fileList[i].length();

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return size/1048576;
		return size;
	}

	/**
	 * 隐藏键盘
	 * 
	 * @param mcontext
	 */
	public static void closeBoard(Context mcontext) {
		InputMethodManager imm = (InputMethodManager) mcontext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		// imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
		if (imm.isActive()) // 一直是true
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
					InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/**
	 * 显示键盘
	 * 
	 * @param mcontext
	 * @param v
	 */
	public static void hideSystemKeyBoard(Context mcontext, View v) {
		InputMethodManager imm = (InputMethodManager) mcontext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}



	/**
	 * 显示自定义的土司
	 * 
	 * @param context
	 *            上下文
	 *            图标的id
	 * @param text
	 *            显示的文本
	 */
	public static void showToast(Context context, String text) {
//		View view = View.inflate(context, R.layout.tostshow, null);
//		TextView tv = (TextView) view.findViewById(R.id.tostshow_tv)tostshow_tv;
		// ImageView iv = (ImageView) view.findViewById(R.id.iv_my_toast);
		// iv.setImageResource(iconid);
//		tv.setText(text);
		Toast toast = new Toast(context);
//		toast.setDuration(0);
//		toast.setView(view);
		toast.show();
	}

	public static int getStatusHeight(Activity activity) {
		int statusHeight = 0;
		Rect localRect = new Rect();
		activity.getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight) {
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass
						.getField("status_bar_height").get(localObject)
						.toString());
				statusHeight = activity.getResources()
						.getDimensionPixelSize(i5);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		return statusHeight;
	}

	/**
	 * 结婚textview 自动换行将字符转换为全角 的
	 */
	public static String ToSBC(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}
	/**
	 * 返回当前程序版本名
	 */
	public  static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;

			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}
}
