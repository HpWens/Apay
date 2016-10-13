package com.lancheng.jneng.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.lancheng.jneng.view.LoadingDialog;

/**
 *  Fragment基类
 *  @author AFLF
 */
public class BaseFragment extends Fragment {
    private Toast toast;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private OnFragmentInteractionListener mListener;
//
//    public BaseFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment BaseFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static BaseFragment newInstance(String param1, String param2) {
//        BaseFragment fragment = new BaseFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        TextView textView = new TextView(getActivity());
//        textView.setText(R.string.hello_blank_fragment);
//        return textView;
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
    /**
     * 打印提示文字
     * @param text
     */
    public void showText(String text){
        if(toast != null){
            toast.setText(text);
        }else {
            toast = Toast.makeText(getActivity(),text,Toast.LENGTH_SHORT);
        }
        toast.show();
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
    public  PackageInfo getPackageInfo(Context context) {
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
     * 返回当前程序版本名
     */
    public  String getAppVersionName(Context context) {
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

    /*
     * 一下为测试进度对话框
     */
    private LoadingDialog loadingDialog = null;

    /**
     * 测试进度对话框
     */
    public void showLoadingDialog() {
        if (loadingDialog == null) loadingDialog = new LoadingDialog(getContext());
        loadingDialog.show();
    }

    public void closeLoadingDialog() {
        if (loadingDialog != null&&loadingDialog.isShowing()) loadingDialog.dismiss();
    }

    @Override
    public void onDestroy() {
        closeLoadingDialog();
        super.onDestroy();
    }
}