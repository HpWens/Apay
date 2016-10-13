package com.lancheng.jneng.Application;

import android.app.Activity;
import android.app.Application;

import com.lancheng.jneng.entity.LoginType;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.Stack;

public class MyApplication extends Application {

    public static LoginType user;
    static MyApplication myApplication;
    Stack<Activity> stack = new Stack<Activity>();//栈管理

    @Override
    public void onCreate() {
        super.onCreate();
        imageLoader();
    }

    public static void setUser(LoginType loginType) {
        user = loginType;
    }

    private void imageLoader() {
        File cacheDir = StorageUtils.getOwnCacheDirectory(this, "imageloader/Cache");
        //创建默认的ImageLoader配置参数
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
                .Builder(this)
                .memoryCacheExtraOptions(480, 800) // maxwidth, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100) //缓存的文件数量
                .discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for releaseapp
                .build();//开始构建


        //Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(configuration);
    }

    public static MyApplication getIntance() {

        if (myApplication == null) {
            myApplication = new MyApplication();
        }
//d
        return myApplication;
    }

//添加acativity

    void AddActivity(Activity activity) {
        if (stack != null) {
            stack.add(activity);
        } else {
            stack = new Stack<Activity>();
            stack.add(activity);
        }
    }

    //
    //delete    diyige activity
    void DeleteActivity() {
        if (stack != null) {
            Activity activity = stack.firstElement();//dibu
            Activity activity1 = stack.lastElement();//dangqian activity
            activity.finish();
        }

    }

    //  shanchu juti moge activity
    void DeleteSpecificActivity(Activity activity) {

        if (stack != null) {
            // activity=stack.lastElement();
            //      activity.finish();
            for (int i = 0; i < stack.size(); i++) {
                Activity mac = stack.get(i);
                if (mac == activity) {
                    mac.finish();
                    //remove
                    stack.remove(i);

                }
            }
        }
    }

    void Back() {
        if (stack != null) {
            for (int i = 0; i < stack.size(); i++) {
                Activity activity = stack.get(i);
                activity.finish();
            }
            System.exit(0);
        }
    }
}

