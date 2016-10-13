package com.lancheng.jneng.view;

import android.app.Activity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 此类是用于进入web页面的时候要显示的进度对话框
 * @author Administrator
 *
 */


	public class WebOperation {
		private static LoadingDialog dialog = null;

		/**
		 *
		 * @param fm
		 *            进度框要显示的页面
		 * @param webview
		 *            Web页面的控件
		 * @param url
		 *            Web页面要加载的网络地址
		 */
		public static void setWebProgressDialog(Activity fm,
												WebView webview, String url) {
			if (webview != null) {
				webview.setWebViewClient(new WebViewClient() {

					@Override
					public void onPageFinished(WebView view, String url) {
						dialog.dismiss();
					}
				});
				if (webview != null) {
					webview.loadUrl(url);
					dialog =new LoadingDialog(fm);
					dialog.show();
					webview.reload();
				}

			}
		}

	}