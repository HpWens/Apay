package com.lancheng.jneng.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lancheng.jneng.Application.MyApplication;
import com.lancheng.jneng.R;
import com.lancheng.jneng.activity.AboutUsActivity;
import com.lancheng.jneng.activity.CAccountActivity;
import com.lancheng.jneng.activity.LoginActivity;
import com.lancheng.jneng.activity.MFeedBackActivity;
import com.lancheng.jneng.activity.UpdatePasswordActivity;

/**
 * Created by aflf on 2016-06-03.
 */
public class MoreFragment extends BaseFragment implements View.OnClickListener {
    private RelativeLayout layoutPwdUpdate, layoutAbout;//修改密码,关于我们
    private TextView layoutLoginout;//注销登录
    private RelativeLayout layoutAccount;//个人信息
    private static final int REQUESTCODE = 1;
    private LinearLayout contactLayout;//联系客服\\
    private RelativeLayout feedbackLayout;//意见反馈

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = View.inflate(getActivity(), R.layout.fragment_more,
                null);
        intview(view);
        return view;

    }

    /**
     * 初始化控件
     *
     * @param view
     */
    private void intview(View view) {
        layoutAbout = (RelativeLayout) view.findViewById(R.id.more_about_layout);
        layoutPwdUpdate = (RelativeLayout) view.findViewById(R.id.more_update_pwd);
        layoutLoginout = (TextView) view.findViewById(R.id.more_login_out);
        layoutAccount = (RelativeLayout) view.findViewById(R.id.more_account);
        contactLayout = (LinearLayout) view.findViewById(R.id.more_us_contact);
        feedbackLayout = (RelativeLayout) view.findViewById(R.id.more_feedback_layout);
        initListener();
    }

    private void initListener() {
        layoutAbout.setOnClickListener(this);
        layoutPwdUpdate.setOnClickListener(this);
        layoutLoginout.setOnClickListener(this);
        layoutAccount.setOnClickListener(this);
        contactLayout.setOnClickListener(this);
        feedbackLayout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_about_layout:
                intentActivity(getActivity(), AboutUsActivity.class, false);
                break;
            case R.id.more_update_pwd:
                isLoginState(UpdatePasswordActivity.class, false);
                break;
            case R.id.more_login_out:
                if (getSharedValue(getActivity(), "userID", "-1").equals("-1")) {
                    showText(getString(R.string.dialog_loginout));
                    return;
                } else {
                    forLoginOut("0");
                }
                break;
            case R.id.more_account:
                isLoginState(CAccountActivity.class, false);
                break;
            case R.id.more_us_contact:
                callPhone();
                break;
            case R.id.more_feedback_layout:
                intentActivity(getActivity(), MFeedBackActivity.class, false);
                break;
        }
    }

    /**
     * 退出登录
     */
    private PopupWindow outPopWindow = null;
    private View contentView1;

    private void forLoginOut(final String state) {
        if (outPopWindow == null) {
            LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());
            contentView1 = mLayoutInflater.inflate(
                    R.layout.dialog_login, null);

            outPopWindow = new PopupWindow(contentView1,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            outPopWindow.getWidth();

        }
        // 下面这2句话要和配套使用才可以让 点击pop之外的地方让pop窗口消失
        outPopWindow.setOutsideTouchable(true);
        ColorDrawable cd = new ColorDrawable(0x000000);
        outPopWindow.setBackgroundDrawable(cd);
        // 产生背景变暗效果
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();

        lp.alpha = 0.4f;

        getActivity().getWindow().setAttributes(lp);
        // popuWindow1.setOutsideTouchable(true);
        outPopWindow.setTouchable(true);
        outPopWindow.setFocusable(true);

        outPopWindow.showAtLocation((View) layoutAbout.getParent(),
                Gravity.CENTER_HORIZONTAL, 0, 0);// 设置弹出的位置
        outPopWindow.update();
        outPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            // 在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }

        });
        TextView desc = (TextView) contentView1.findViewById(R.id.dialog_login_desc);
        if (state.equals("0")) {
            desc.setText(getString(R.string.dialog_login_out_desc));
            Log.d("state", "forLoginOut() called with: " + "state = [" + 8888 + "]");
        } else if (state.equals("1")) {
            desc.setText(getString(R.string.dialog_login_desc));
            Log.d("state", "forLoginOut() called with: " + "state = [" + 8888 + "]");
        }
        TextView ok = (TextView) contentView1.findViewById(R.id.dialog_login_ok);
        TextView cancle = (TextView) contentView1.findViewById(R.id.dialog_login_cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (state.equals("0")) {
                    setSharedPreferences(getActivity(), "userID", "-1");
                    MyApplication.setUser(null);
                } else {
                    startActivity(new Intent(getActivity(),
                            LoginActivity.class));
                    setSharedPreferences(getActivity(), "resultState", "4");
                }
                outPopWindow.dismiss();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outPopWindow.dismiss();
            }
        });

    }

    /**
     * 判断是否登陆状态 如果登陆了进入相关的页面，如果没有登陆进行登陆
     *
     * @param
     */
    private void isLoginState(Class<?> cls, boolean isfinish) {
        String loginState = getSharedValue(getActivity(), "userID", "-1");
        if (loginState.equals("-1")) {
            forLoginOut("1");
        } else {
            Intent intent = new Intent(getActivity(), cls);
            startActivity(intent);
            if (isfinish) {
                getActivity().finish();
            }

        }
    }

    /**
     * 拨打电话
     */
    private void callPhone() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("app需要开启权限才能使用此功能")
                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},
                        REQUESTCODE);
            }

        } else {
            call();
        }

    }

    private void call() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + "4000008888");
        intent.setData(data);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUESTCODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //用户同意了授权
                    call();
                } else {
                    //用户拒绝了授权
                    // Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
