package com.lancheng.jneng.utils;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ImageUtilsView {
	/**
	 * 图片缓存到本地     缓存到内存
	 * @return
	 */
	public static DisplayImageOptions imageOption(){
		DisplayImageOptions op= new DisplayImageOptions.Builder()

		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.imageScaleType(ImageScaleType.EXACTLY)
		.build();
		return op;

	}
	/**
	 * 图片不需要缓存到本地，但要做内存缓存
	 * @return
	 */
	public static DisplayImageOptions imageOnOption(){
		DisplayImageOptions op= new DisplayImageOptions.Builder()

		.cacheInMemory(true)
		.cacheOnDisc(false)//本地
		.bitmapConfig(Bitmap.Config.RGB_565)
		.imageScaleType(ImageScaleType.EXACTLY)
		.build();
		return op;

	}

}
