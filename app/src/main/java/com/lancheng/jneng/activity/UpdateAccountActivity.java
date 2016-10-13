package com.lancheng.jneng.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lancheng.jneng.R;
import com.lancheng.jneng.base.Constant;
import com.lancheng.jneng.entity.AccountTypeInfo;
import com.lancheng.jneng.entity.BaseType;
import com.lancheng.jneng.utils.ImageUtilsView;
import com.lancheng.jneng.utils.OkHttpClientManager;
import com.lancheng.jneng.view.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UpdateAccountActivity extends BaseActivity implements View.OnClickListener {
    private Button btnRetrun;//返回
    private Button btnOk;//确定
    private EditText etMaill, etQQ, etAutograph;//昵称，性别，邮箱，扣扣，签名
    private TextView etSex, etUserName;
    private CircleImageView iconPircture;
    private PopupWindow selectPopuWindow;
    private PopupWindow selectSexPopuwindow;
    private View contentView1;
    private View contentView2;
    private Bitmap head;// 头像Bitmap


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);
        showLoadingDialog();
        getDatas();
        initView();
    }

    /**
     * 定义控件
     */
    private void initView() {
        btnRetrun = (Button) this.findViewById(R.id.update_account_return);
        btnOk = (Button) this.findViewById(R.id.update_account_submit);
        iconPircture = (CircleImageView) this.findViewById(R.id.update_account_icon);
        etUserName = (TextView) this.findViewById(R.id.update_account_et_name);
        etMaill = (EditText) this.findViewById(R.id.update_account_et_mail);
        etQQ = (EditText) this.findViewById(R.id.update_account_et_qq);
        etSex = (TextView) this.findViewById(R.id.update_account_et_sex);
        etAutograph = (EditText) this.findViewById(R.id.update_account_et_signature);
        initListener();
    }

    /**
     * 注册监听
     */
    private void initListener() {
        btnRetrun.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        iconPircture.setOnClickListener(this);
        etSex.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_account_return:
                finish();
                break;
            case R.id.update_account_submit:

                inputData();
                break;
            case R.id.update_account_icon:
                selectPicture(v);
                break;
            case R.id.update_account_et_sex:
                selectSex(v);
                break;
            case R.id.from_camera:
                selectPopuWindow.dismiss();
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                        Environment.getExternalStorageDirectory(), "head.jpg")));
                startActivityForResult(intent2, 2);// 采用ForResult打开
                break;
            case R.id.from_local:
                selectPopuWindow.dismiss();
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                break;
        }

    }


    /**
     * 访问网络获取数据
     */
    private void getDatas() {
        Map<String, String> params = new HashMap<>();
        String id = getSharedValue(this, "userID", "null");
        if (id.equals("null")) {
            showText(getString(R.string.query_out_login));
            closeLoadingDialog();
            return;
        }
        params.put("id", id);
        OkHttpClientManager.postAsyn(Constant.MEMBER_INFO, new OkHttpClientManager.ResultCallback<BaseType<AccountTypeInfo>>() {
            @Override
            public void onError(Request request, Exception e) {
                closeLoadingDialog();
                showText(e.getMessage() + getString(R.string.hint_errow));
            }

            @Override
            public void onResponse(BaseType<AccountTypeInfo> response) {
                closeLoadingDialog();
                Log.d("aflf", "onResponse() called with: " + "response = [" + response + "]");
                if (response.getResult().equals("0")) {
                    showText(response.getDetail() + "");
                    return;
                } else if (response.getResult().equals("1")) {
                    if (response.getInfor() != null) {
                        setDatas(response.getInfor());
                    }
                }
            }
        }, params);
    }

    /**
     * 设置初始化数据
     *
     * @param infor
     */
    private void setDatas(AccountTypeInfo infor) {
        Log.d("infor", "setDatas() called with: " + "infor = [" + infor + "]");
        if (!TextUtils.isEmpty(infor.getAvatar())) {
            ImageLoader.getInstance().displayImage(
                    Constant.SERVER_IMAGE + infor.getAvatar(), iconPircture, ImageUtilsView.imageOption());
        }
        if (!TextUtils.isEmpty(infor.getUsername())) {
            etUserName.setText(infor.getUsername());
        }
        if (!TextUtils.isEmpty(infor.getQq())) {
            etQQ.setHint(infor.getQq());
        }
        if (!TextUtils.isEmpty(infor.getEmail())) {
            etMaill.setHint(infor.getEmail());
        }
        if (!TextUtils.isEmpty(infor.getSix())) {
            etSex.setText(infor.getSix());
        }
        if (!TextUtils.isEmpty(infor.getIntroduction())) {
            etAutograph.setHint(infor.getIntroduction());
        }

    }

    /**
     * 判读输入的修改用户的数据
     */
    private void inputData() {
        String usernameValues = etUserName.getText().toString();
        String maillValues = etMaill.getText().toString();
        String qqValues = etQQ.getText().toString();
        String autographValues = etAutograph.getText().toString();
        String sexValues = etSex.getText().toString();
        if (TextUtils.isEmpty(usernameValues) && TextUtils.isEmpty(maillValues) &&
                TextUtils.isEmpty(qqValues) && TextUtils.isEmpty(autographValues) &&
                TextUtils.isEmpty(sexValues)) {
            showText(getString(R.string.edit_account_desc));
            return;
        } else {
            if (isConnectivity(this)) {
                getEditData(usernameValues, maillValues, qqValues, autographValues, sexValues);
            } else {
                showText(getString(R.string.hint_intent));
            }
        }
    }

    /**
     * 选择用户头像
     */
    private void selectPicture(View parent) {
        if (selectPopuWindow == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(this);
            contentView1 = mLayoutInflater.inflate(
                    R.layout.layout_myzoe_select_popwindow, null);

            selectPopuWindow = new PopupWindow(contentView1,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            selectPopuWindow.getWidth();

        }
        // 下面这2句话要和配套使用才可以让 点击pop之外的地方让pop窗口消失
        selectPopuWindow.setOutsideTouchable(true);
        ColorDrawable cd = new ColorDrawable(0x000000);
        selectPopuWindow.setBackgroundDrawable(cd);
        // 产生背景变暗效果
        WindowManager.LayoutParams lp = getWindow().getAttributes();

        lp.alpha = 0.4f;

        getWindow().setAttributes(lp);
        // popuWindow1.setOutsideTouchable(true);
        selectPopuWindow.setTouchable(true);
        selectPopuWindow.setFocusable(true);

        selectPopuWindow.showAtLocation((View) parent.getParent(),
                Gravity.CENTER_HORIZONTAL, 0, 0);// 设置弹出的位置
        selectPopuWindow.update();
        selectPopuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            // 在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }

        });
        TextView from_camera = (TextView) contentView1
                .findViewById(R.id.from_camera);
        TextView from_local = (TextView) contentView1
                .findViewById(R.id.from_local);
        from_camera.setOnClickListener(this);
        from_local.setOnClickListener(this);
    }

    /**
     * 选择用户性别
     */
    private void selectSex(View parent) {
        if (selectSexPopuwindow == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(this);
            contentView2 = mLayoutInflater.inflate(
                    R.layout.layout_sex_select_popwindow, null);

            selectSexPopuwindow = new PopupWindow(contentView2,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            selectSexPopuwindow.getWidth();

        }
        // 下面这2句话要和配套使用才可以让 点击pop之外的地方让pop窗口消失
        selectSexPopuwindow.setOutsideTouchable(true);
        ColorDrawable cd = new ColorDrawable(0x000000);
        selectSexPopuwindow.setBackgroundDrawable(cd);
        // 产生背景变暗效果
        WindowManager.LayoutParams lp = getWindow().getAttributes();

        lp.alpha = 0.4f;

        getWindow().setAttributes(lp);
        // popuWindow1.setOutsideTouchable(true);
        selectSexPopuwindow.setTouchable(true);
        selectSexPopuwindow.setFocusable(true);

        selectSexPopuwindow.showAtLocation((View) parent.getParent(),
                Gravity.CENTER_HORIZONTAL, 0, 0);// 设置弹出的位置
        selectSexPopuwindow.update();
        selectSexPopuwindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            // 在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }

        });
        final TextView from_man = (TextView) contentView2
                .findViewById(R.id.from_man);
        final TextView from_woman = (TextView) contentView2
                .findViewById(R.id.from_woman);
        from_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSexPopuwindow.dismiss();
                etSex.setText(getString(R.string.edit_account_nam));
            }
        });
        from_woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSexPopuwindow.dismiss();
                etSex.setText(getString(R.string.edit_account_woman));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }

                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory()
                            + "/head.jpg");
                    Log.i("tag", Environment.getExternalStorageDirectory()
                            + "/head.jpg");
                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
                }

                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");

                    if (head != null) {
                        /**
                         * 103. * 上传服务器代码 104.
                         */
                        try {
                            getPictureUrl(bitmapToBase64(head));
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }// 保存在SD卡中
                        //
                    }
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 117. * 调用系统的裁剪 118. * @param uri 119.
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    /**
     * 生成图片地址
     */
    private void getPictureUrl(String base64) {
        Map<String, String> params = new HashMap<>();
        String id = getSharedValue(this, "userID", "null");
        if (id.equals("null")) {
            showText(getString(R.string.query_out_login));
            closeLoadingDialog();
            return;
        }
        params.put("id", id);
        params.put("bit", base64.toString());
        Log.d("aflf", base64);
        OkHttpClientManager.postAsyn(Constant.MEMBER_UPLOADS, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                showText(getString(R.string.hint_errow));
                Log.d("aflf", "onError() called with: " + "request = [" + request + "], e = [" + e + "]");
            }

            @Override
            public void onResponse(String response) {
                Log.d("aflf", "onResponse() called with: " + "response = [" + response + "]");
                try {
                    // 获取状态
                    String result = new JSONObject(response).getString("result");
                    String detail = new JSONObject(response).getString("detail");
                    String src = new JSONObject(response).getString("scr");
                    if (result.equals("0")) {
                        showText(detail + "");
                        return;
                    } else if (result.equals("1")) {
                        if (!TextUtils.isEmpty(src)) {
                            ImageLoader.getInstance().displayImage(
                                    Constant.SERVER_IMAGE + src, iconPircture, ImageUtilsView.imageOption());
                        }
                        showText(detail + "");
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
     * 提交修改信息
     *
     * @param usernameValues
     * @param maillValues
     * @param qqValues
     * @param autographValues
     * @param sexValues
     */
    private void getEditData(String usernameValues, String maillValues, String qqValues, String autographValues, String sexValues) {
        Map<String, String> params = new HashMap<>();
        //   params.put("id", getSharedValue(this, "userID", "1"));
        String id=getSharedValue(this, "userID", "null");
        if (id.equals("null")){
            showText(getString(R.string.query_out_login));
            closeLoadingDialog();
            return;
        }
        params.put("id", id);        //   params.put("name", usernameValues + "");
        if (!TextUtils.isEmpty(qqValues)) {
            params.put("qq", qqValues + "");
        }
        //   params.put("rellname", usernameValues + "");
        if (!TextUtils.isEmpty(maillValues)) {
            params.put("email", maillValues + "");
        }
        //  params.put("telephone", "");
        //   params.put("birthday", "");
        if (!TextUtils.isEmpty(sexValues)) {
            params.put("six", sexValues + "");
        }
        //     params.put("hobby", "");
        if (!TextUtils.isEmpty(autographValues)) {
            params.put("introduction", autographValues + "");
        }
        OkHttpClientManager.postAsyn(Constant.MEMBER_EDINFOR, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.d("aflf", "onError() called with: " + "request = [" + request + "], e = [" + e + "]");
                closeLoadingDialog();
                showText(getString(R.string.hint_errow));
            }

            @Override
            public void onResponse(String response) {
                closeLoadingDialog();
                Log.d("aflf", "onResponse() called with: " + "response = [" + response + "]");
                try {
                    // 获取状态
                    String result = new JSONObject(response).getString("result");
                    String detail = new JSONObject(response).getString("detail");

                    if (result.equals("0")) {
                        showText(detail + "");
                        return;
                    } else if (result.equals("1")) {
                        showText(detail + "");
                        finish();
                        return;
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }, params);


    }

}
