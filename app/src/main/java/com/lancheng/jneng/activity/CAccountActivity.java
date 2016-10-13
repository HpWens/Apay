package com.lancheng.jneng.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.Map;

public class CAccountActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvName, tvQq, tvMail, tvSex, tvSignature, tvTel, tvMoney;//昵称，扣扣，邮箱，性别，签名,电话，余额
    private CircleImageView iconPircture;//头像
    private Button btnReturn;//返回
    private TextView btnEdit;//编辑
    private LinearLayout accountParent;//整个布局

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caccount);
        showLoadingDialog();
        getDatas();
        initView();
    }


    /**
     * 初始化控件
     */
    private void initView() {
        accountParent = (LinearLayout) this.findViewById(R.id.account_parent);
        //昵称，扣扣，邮箱，性别，签名，电话，余额
        tvName = (TextView) this.findViewById(R.id.account_name);
        tvQq = (TextView) this.findViewById(R.id.account_qq);
        tvMail = (TextView) this.findViewById(R.id.account_mail);
        tvSex = (TextView) this.findViewById(R.id.account_sex);
        tvSignature = (TextView) this.findViewById(R.id.account_signature);
        tvTel = (TextView) this.findViewById(R.id.account_tel);
        tvMoney = (TextView) this.findViewById(R.id.account_money);
        //头像
        iconPircture = (CircleImageView) this.findViewById(R.id.account_icon);
        //返回
        btnReturn = (Button) this.findViewById(R.id.account_return);
        //编辑
        btnEdit = (TextView) this.findViewById(R.id.account_edit);
        initLinsener();
    }

    /**
     * 注册控件的监听
     */
    private void initLinsener() {
        btnReturn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_return:
                finish();
                break;
            case R.id.account_edit:
//                Intent intent=new Intent(this,UpdateAccountActivity.class);
//                intent.putExtra("accountDatas",);
                intentActivity(this, UpdateAccountActivity.class, false);
                break;
        }

    }

    /**
     * 访问网络获取数据
     */
    private void getDatas() {
        Map<String, String> params = new HashMap<>();
        params.put("id", getSharedValue(this, "userID", "1"));
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
                    accountParent.setVisibility(View.VISIBLE);
                    btnEdit.setOnClickListener(CAccountActivity.this);
                    if (response.getInfor() != null) {
                        setDatas(response.getInfor());
                    }
                }
            }
        }, params);
    }

    /**
     * 设置数据
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
            tvName.setText(infor.getUsername());
        }
        if (!TextUtils.isEmpty(infor.getQq())) {
            tvQq.setText(infor.getQq());
        }
        if (!TextUtils.isEmpty(infor.getEmail())) {
            tvMail.setText(infor.getEmail());
        }
        if (!TextUtils.isEmpty(infor.getMoneys())) {
            tvMoney.setText(infor.getMoneys());
        }
        if (!TextUtils.isEmpty(infor.getSix())) {
            tvSex.setText(infor.getSix());
        }
        if (!TextUtils.isEmpty(infor.getTelephone())) {
            tvTel.setText(infor.getTelephone());
        }
        if (!TextUtils.isEmpty(infor.getIntroduction())) {
            tvSignature.setText(infor.getIntroduction());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        showLoadingDialog();
        getDatas();
    }
}
