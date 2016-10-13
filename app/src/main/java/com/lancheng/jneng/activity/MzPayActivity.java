package com.lancheng.jneng.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.lancheng.jneng.R;
import com.lancheng.jneng.activity.alipayutls.PayResult;
import com.lancheng.jneng.activity.alipayutls.SignUtils;
import com.lancheng.jneng.activity.paywechatutlis.Constants;
import com.lancheng.jneng.activity.paywechatutlis.MD5;
import com.lancheng.jneng.base.Constant;
import com.lancheng.jneng.utils.OkHttpClientManager;
import com.squareup.okhttp.Request;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * 我的账号模块----充值页面
 */
public class MzPayActivity extends BaseActivity implements View.OnClickListener {
    private Button btnReturn, btnOk;//返回按钮,确定
    private TextView payDesc;//支付秒速
    private LinearLayout alipayLayout, wecharLayout;//点击可进行切换
    /**
     * 支付方式 0:支付宝  1:微信
     */
    private String PAY_STATE = "0";
    private ImageView alipayIv, webchatIv;//选择支付方式的图标
    private Button btnSubmit;//点击可以进行提交
    private EditText etMoney;//输入充值金额
    private String PriceDatas = "";//充值金额
    /**
     * 以下为支付宝支付的参数
     */
    // 商户PID
    public static final String PARTNER = "2088121075056414";
    // 商户收款账号
    public static final String SELLER = "huzhaoming212@126.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDEOLuGaYW6VgmmTjq+FdYtC1boj/V9IDcFbP1TMEabEPim9SyVqz2S0GMe+4qmGMi3aeHl99m+WcEfmkc16GBep5lnk2fhRjttisE56o516Asv0yvYPV/hWG/z3gpH7YLjXoegZ+UH/WRaS9RRKJYDHKM+nGFu5qdMMEPuWXe3DYgPyb3fMW0d6iCSaPGgl/ZIbrvPpOHO7rvpCazrDHxFJxFIItqj1uWwuA3SMhIuKNMdTK/UL8LSIWkSFoyECzVuAr6rPX5rjwzc5yRK0JiJjpcbknd940s81kPIJ92VO2KiWBK85BubX4DE2O8V0/e4krLsFi717aX2f5+8HpdRAgMBAAECggEAc3gHMlPKdBrJVupWOyHShXNU00pT5iwc9L5sXJXeFsZn7Kf+cpzwgQLMMaFIt/GBvB085/1wT0m2EgjRuvFiNJFf/smJqdGQiyfKCXD0IIJKgvw7ouaLE6rdRp71Ydps2av5F/XLm2YLte9Fo/TEQlSJ5ABWfdKRxI3qi8puXnM6NbuDdsL3YkPH7NCmeEmDKgMeyAEmSNBZOIDL/Nhz/kEaqhvLAez9r3XhXoosIVpBgwpusRIcBQnRKVosz1hP5PcKFBLy5shNcohVE/FyjxahsQ90MQo47LBRNLjYu4u77MBip+cHhCDABanbYmCeyYD4Cbbyf5w4p8dm3x4oAQKBgQDkt1h6zPOB3dbO1HyuuSU0JX//G+B8OM/EALSugZ0t8BGujxcq3krfz05dhY1TTg6twUY0S6GPFxDehamxmNpJYoG2oYAIitB9jsZqwCqSXqbSWWburWg+7YsUYuyvoX37g/xxpYuSQgOuHDiX5wf5Wzo8cSE0YkJwarFMJ16zkQKBgQDboQzmtW1JWvOWqqfa67Jaj4qJzt0kKa3KbonUK5DjeDe5ck7yyl27eqjxakPkpgs7N2JTyd4n5Oy8EsTVcF4vTj/QI5F4hYfZHSeC6MV/fi1sGpEj4yuhsqfnr2dMkFb7gCV+cGcoPbrMSJNgVA/40+a+f2ylkTii0gWknSxHwQKBgCxD/+/kcsyTrttg7LC0TKsHBvrM3mmsZSm+KToNA+q8J+/s6cL2Ou+OI9Cd5HhXByhB2+CLMr2K7Uzj+a/RsY26bvh10V3V3/wtAeHvR04E26yiePWAwkhLmTtc2goz6zoelWE5dQXdmPC9rYZYxIZSTbcc/hwxWEKBo8blZ8LBAoGAM8NTXRSpez56Zqo2AXzHflySVERzDlDcavd+eIkEDviIeaadQM+9I7CXHL7k3YDc6Mn23LSZLWHhAbqStZNUP2X72DxpzrP5/ovGgi4GTjQY1H+2cXTbFbMbq5lFiWADgCcFrzUiuvNBkWARjASD9ytKGN/DZYpChO1m8xMwGoECgYEAumflL5pSdbbf1C0xtNO4GxPKdfG+9vLNtK3PF+bzf7zTP2Zt4QmTepbDwAht8La+Aqm/hqLJO0El6PSe6kuxsNGjR+twLS/MVw53ES16dBFDIyqdvq5HypM54cKwGBBmV86Ld36EeaBg/9ZAhoLJTIMsKQNTYU0cPHsR8jFN3gg=";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
    private static final int SDK_PAY_FLAG = 1;
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        showText(getString(R.string.pay_state_ok));
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            showText(getString(R.string.pay_state_indeterminate));
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            showText(getString(R.string.pay_state_failed));
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mz_pay);
        api = WXAPIFactory.createWXAPI(this, "wx80f9e0d9f03cbdce");
        boolean reg = api.registerApp(Constants.APP_ID);
        Log.e("---------------", "************" + reg);
        iniview();

    }

    /**
     * 初始化控件
     */
    private void iniview() {
        //返回按钮,确定
        btnReturn = (Button) this.findViewById(R.id.mz_pay_return);
        btnSubmit = (Button) this.findViewById(R.id.pay_alipay_submit);
        payDesc = (TextView) this.findViewById(R.id.pay_payment);
        alipayLayout = (LinearLayout) this.findViewById(R.id.pay_alipay_layout);
        wecharLayout = (LinearLayout) this.findViewById(R.id.pay_wechat_layout);
        alipayIv = (ImageView) this.findViewById(R.id.mz_pay_alipay_iv);
        webchatIv = (ImageView) this.findViewById(R.id.mz_pay_wechat_iv);
        etMoney = (EditText) this.findViewById(R.id.mz_pay_et);
        intOnclick();
    }

    /**
     * 注册监听
     */
    private void intOnclick() {
        btnReturn.setOnClickListener(this);
        alipayLayout.setOnClickListener(this);
        wecharLayout.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mz_pay_return:
                finish();
                break;
            case R.id.pay_alipay_submit:
                PriceDatas = etMoney.getText().toString();
                if (TextUtils.isEmpty(PriceDatas)) {
                    showText(getString(R.string.himt_extract_noeny));
                    return;
                }
                if (PAY_STATE.equals("0")) {
                    String userid = getSharedValue(this, "userID", "-1");
                    showLoadingDialog();
                    getDatas(userid, PriceDatas, PAY_STATE);
                } else {
                    /**
                     * 注意：调试微信支付是，必须的为正式版本也就是要打包，应用签名为正式打包时的前面，必须和官网一直
                     */
                    showLoadingDialog();
                    String userid = getSharedValue(this, "userID", "-1");
                    getDatas(userid, PriceDatas, PAY_STATE);
                    //getWechat("wx80f9e0d9f03cbdce", "1385935502", "dd", "dd", "dd", "Sign=WXPay", "dd");
                }
                break;
            case R.id.pay_alipay_layout:
                PAY_STATE = "0";
                alipayIv.setImageResource(R.mipmap.icon_register_check_pre);
                webchatIv.setImageResource(R.mipmap.icon_register_check_nor);
                payDesc.setText("支付宝");
                break;
            case R.id.pay_wechat_layout:
                PAY_STATE = "1";
                webchatIv.setImageResource(R.mipmap.icon_register_check_pre);
                alipayIv.setImageResource(R.mipmap.icon_register_check_nor);
                payDesc.setText("微信");
                break;

        }
    }


    /**
     * call alipay sdk pay. 调用SDK支付
     */
    private void AliPay(String orderNo, String subject, String body, String price) {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }
        String orderInfo = getOrderInfo(orderNo, subject, body, price);

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(MzPayActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String orderNo, String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + orderNo + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";
        // 服务器异步通知页面路径  http://notify.msp.hk/notify.htm
        orderInfo += "&notify_url=" + "\"" + "http://002.600i.com.cn/pay/alipay_notify" + "\"";
        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);
        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }
    //

    /**
     * 获取支付信息
     *
     * @param id 用户id
     * @param s  金额
     * @param s1 支付方式
     */
    private void getDatas(String id, String s, String s1) {
        Map<String, String> params = new HashMap<>();
        if (id.equals("-1")) {
            showText(getString(R.string.query_userid));
            closeLoadingDialog();
            return;
        }
        params.put("id", id);
        params.put("price", s);
        params.put("way", s1);
        OkHttpClientManager.postAsyn(Constant.SERVICE_WITHDRAW, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.d("responsed", "onError() called with: request = [" + request + "], e = [" + e + "]");
                closeLoadingDialog();

            }

            @Override
            public void onResponse(String response) {
                closeLoadingDialog();
                Log.d("response", "dddddddddddddddd");

                Log.d("response", response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    String result = obj.getString("result");
                    String detail = obj.getString("detail");
                    if (result.equals("0")) {
                        showText(detail + "");
                    } else {
                        if (PAY_STATE.equals("0")) {
                            String orderno = obj.getString("orderno");
                            AliPay(orderno, "巨能合伙人订单-" + orderno, "支付宝充值", PriceDatas);
                        } else {
                            final PayReq req = new PayReq();
                            req.appId = obj.optString("appid");
                            req.partnerId = obj.optString("partnerid");
                            req.prepayId = obj.optString("prepayid");
                            req.packageValue = "Sign=WXPay";
                            req.nonceStr = obj.optString("noncestr");
                            req.timeStamp = obj.optString("timestamp");

                            //String str = obj.getString("timestamp");

                            String str = System.currentTimeMillis() + "";

                            //str = str.substring(str.indexOf("(") + 1, str.indexOf(")"));

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);

                            str = sdf.format(new Date(Long.parseLong(str)));

                            //req.timeStamp = sdf.parse(str).getTime() / 1000 + "";

                            List<NameValuePair> signParams = new LinkedList<NameValuePair>();
                            signParams.add(new BasicNameValuePair("appid", req.appId));
                            signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
                            signParams.add(new BasicNameValuePair("package", req.packageValue));
                            signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
                            signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
                            signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));


                            req.sign = genAppSign(signParams);
                            //req.sign = obj.optString("sign");
                            req.extData = "app data"; // optional

                            Log.e("---------------", "************" + req.nonceStr
                                    + "-------" + req.partnerId + "-----" + req.sign
                                    + "-------" + obj.optString("sign") + "-----" + req.timeStamp);
                            api.sendReq(req);
                            //  getWechat(appId, partnerId, prepayId, nonceStr, formatDate(timestamp), packageValue, sign);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, params);
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);
       // sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("------------------", appSign);
        return appSign;
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 转换获取到的时间错
     *
     * @param dateStr
     * @return
     */
    public static String formatDate(String dateStr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dateStr.length(); i++) {
            char ch = dateStr.charAt(i);
            if (ch >= 48 && ch <= 57) {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    /**
     * 微信发起支付请求
     * String appId, String partnerId,
     * String prepayId, String nonceStr,
     * String timeStamp, String packageValue, String sign
     */
    private void getWechat(String appId, String partnerId, String prepayId, String nonceStr, String timeStamp, String packageValue, String sign) {
        //发起支付请求
        if (api.isWXAppInstalled() == true) {//shifou  anzhuang
            boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;//shifouzhici zhifu
            if (isPaySupported == true) {
                PayReq req = new PayReq();
                req.appId = appId;
                req.partnerId = partnerId;
                req.prepayId = prepayId;
                req.nonceStr = nonceStr;
                req.timeStamp = timeStamp;
                req.packageValue = packageValue;
                req.sign = sign;
                req.extData = "app data"; // optional
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//                api.registerApp(appId);
                Log.d("dddddd", "+" + api.registerApp(appId));

                api.sendReq(req);
                Log.d("ddddd", "+" + api.registerApp(appId));

            } else {
                showText("此微信版本暂不支持支付，请更新最新版本");
            }

        } else {

            showText("你还没安转微信APP");
        }

    }
}
