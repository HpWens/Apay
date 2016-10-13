package com.lancheng.jneng.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lancheng.jneng.R;
import com.lancheng.jneng.base.Constant;
import com.lancheng.jneng.utils.OkHttpClientManager;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 邀请好友页面
 */
public class ShareActivity extends BaseActivity implements View.OnClickListener {
    private Button btnReturn;//返回按钮
    private TextView tvCode;
    private LinearLayout qqShareLayout, wechatShareLayout, sinaShareLayout;
    private String CodeMessage = "";//保存邀请码

    private static final int MSG_TOAST = 1;
    private static final int MSG_ACTION_CCALLBACK = 2;
    private static final int MSG_CANCEL_NOTIFY = 3;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.arg1) {
                case 1:
                    showText("分享成功");
                    break;
                case 2:
                    showText("分享失败");
                    break;
                case 3:
                    showText("分享取消");
                    break;
            }

        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ShareSDK.initSDK(this);
        showLoadingDialog();
        iniCode();
        initView();
    }

    /**
     * 用户邀请码
     */
    private void iniCode() {
        Map<String, String> params = new HashMap<>();
        String id=getSharedValue(this, "userID", "null");
        if (id.equals("null")){
            showText(getString(R.string.query_out_login));
            closeLoadingDialog();
            return;
        }
        params.put("id", id);        //         params.put("id", "1");
        OkHttpClientManager.postAsyn(Constant.MEMBER_MYCODE, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.d("aflf", "onError() called with: " + "request = [" + request + "], e = [" + e + "]");
                closeLoadingDialog();
            }

            @Override
            public void onResponse(String response) {
                Log.d("aflf", "onResponse() called with: " + "response = [" + response + "]");
                closeLoadingDialog();
                try {
                    // 获取状态
                    String result = new JSONObject(response).getString("result");
                    String detail = new JSONObject(response).getString("detail");
                    String code = new JSONObject(response).getString("code");
                    CodeMessage = code;

                    if (result.equals("0")) {
                        showText(detail + "");
                        return;
                    } else if (result.equals("1")) {
                        tvCode.setText(code);
                        return;
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }, params);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        btnReturn = (Button) this.findViewById(R.id.shaer_return);
        qqShareLayout = (LinearLayout) this.findViewById(R.id.share_QQ);
        wechatShareLayout = (LinearLayout) this.findViewById(R.id.share_WeChat);
        sinaShareLayout = (LinearLayout) this.findViewById(R.id.share_Sina);
        tvCode = (TextView) this.findViewById(R.id.mz_invitecode_tv);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/STXIHEI.TTF");
        tvCode.setTypeface(typeface);
        initListener();
    }

    /**
     * 注册监听
     */
    private void initListener() {
        btnReturn.setOnClickListener(this);
        qqShareLayout.setOnClickListener(this);
        wechatShareLayout.setOnClickListener(this);
        sinaShareLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.shaer_return:
                finish();
                break;
            case R.id.share_QQ:
                shareQQ();
                break;
            case R.id.share_Sina:
                shareSina();
                break;
            case R.id.share_WeChat:
                if (!clickEffect()) {
                    return;
                }
                shareWeChat();
                break;
        }

    }

    /**
     * 分享到微信好友
     */
    private void shareWeChat() {
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle("巨能合伙人");
        sp.setText("注册输入我的邀请码" + CodeMessage + "还能获得额外惊喜哦！" + "小伙伴们马上加入巨能一族吧，投资理财快人一步。");
        sp.setUrl("http://fir.im/jneng");
        sp.setImageUrl("http://002.600i.com.cn/uploads/sortpic/4cc5cf77-7b2d-44e7-baa0-48e2048431e2.jpg");
        Platform webchat = ShareSDK.getPlatform(Wechat.NAME);
        webchat.setPlatformActionListener(new MyPlatformActionListener());
        webchat.share(sp);
    }

    /**
     * 分享到新浪
     */
    private void shareSina() {
        SinaWeibo.ShareParams sina_weibo = new SinaWeibo.ShareParams();
        sina_weibo.setText("注册输入我的邀请码" + CodeMessage + "还能获得额外惊喜哦！" + "小伙伴们马上加入巨能一族吧，投资理财快人一步。" + "http://fir.im/jneng");
        sina_weibo.setImageUrl("http://002.600i.com.cn/uploads/sortpic/4cc5cf77-7b2d-44e7-baa0-48e2048431e2.jpg");
        Platform sina_weibo1 = ShareSDK.getPlatform(this,
                SinaWeibo.NAME);
        if (sina_weibo1.isClientValid()) {
            System.out.println("安装了新浪微博");
        } else {
            System.out.println("没有安装了新浪微博");
        }
        if (sina_weibo1.isAuthValid()) {
            sina_weibo1.removeAccount(true);
            ShareSDK.removeCookieOnAuthorize(true);
        }
        sina_weibo1.setPlatformActionListener(new MyPlatformActionListener());
        sina_weibo1.share(sina_weibo);


    }

    /**
     * 分享到QQ
     */
    private void shareQQ() {
        QQ.ShareParams sp = new QQ.ShareParams();
        sp.setTitle("巨能合伙人");
        sp.setText("注册输入我的邀请码" + CodeMessage + "还能获得额外惊喜哦！" + "小伙伴们马上加入巨能一族吧，投资理财快人一步。");
        sp.setTitleUrl("http://fir.im/jneng");
        sp.setShareType(QQ.SHARE_WEBPAGE);
     /*   Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.pic_bg);
        sp.setImageData(icon);*/
        sp.setImageUrl("http://002.600i.com.cn/uploads/sortpic/4cc5cf77-7b2d-44e7-baa0-48e2048431e2.jpg");
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(new MyPlatformActionListener()); // 设置分享事件回调
        // 执行图文分享
        qq.share(sp);
    }
    class MyPlatformActionListener implements PlatformActionListener {
        @Override
        public void onComplete(Platform platform, int action,
                               HashMap<String, Object> arg2) {
            // 成功
            Message msg = new Message();
            msg.what = MSG_TOAST;
            msg.arg1 = 1;
            msg.arg2 = action;
            msg.obj = platform;
            handler.sendMessage(msg);
            System.out.println("arg2======================" + arg2);
        }

        @Override
        public void onError(Platform platform, int action, Throwable t) {
            // 失败
            t.printStackTrace();
            // 错误监听,handle the error msg
            Message msg = new Message();
            msg.what = MSG_ACTION_CCALLBACK;
            msg.arg1 = 2;
            msg.arg2 = action;
            msg.obj = t;
            handler.sendMessage(msg);
        }

        @Override
        public void onCancel(Platform platform, int action) {
            // 取消
            Message msg = new Message();
            msg.what = MSG_CANCEL_NOTIFY;
            msg.arg1 = 3;
            msg.arg2 = action;
            msg.obj = platform;
            handler.sendMessage(msg);
        }
    }
}
