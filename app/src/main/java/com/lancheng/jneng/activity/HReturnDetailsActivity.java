package com.lancheng.jneng.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.lancheng.jneng.R;
import com.lancheng.jneng.adapter.RecyclerReturnDetailsAdapter;
import com.lancheng.jneng.base.Constant;
import com.lancheng.jneng.entity.BaseList;
import com.lancheng.jneng.entity.ReturnDetailsInfo;
import com.lancheng.jneng.utils.OkHttpClientManager;
import com.lancheng.jneng.view.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.squareup.okhttp.Request;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 回报详情的页面
 */
public class HReturnDetailsActivity extends BaseActivity implements View.OnClickListener {
    private Button btnReturn;//返回
    private PullLoadMoreRecyclerView swiperefreshlayout;//刷新控件
    private RecyclerReturnDetailsAdapter adapter;
    private List<ReturnDetailsInfo> listDatas = new ArrayList<ReturnDetailsInfo>();
    //访问网络
    private String id = "", pagesize = "10";//分类id,当前页,页大小
    private int page = 1;
    private String begintime = "", endtime = "";
    private TextView tvStartDate, tvEndData;//起始日期
    private TextView btnQury;//查询

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_return_details);
        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收传递的值
        if (!bundle.getString("typeId").toString().equals("")) {
            id = bundle.getString("typeId");
            Log.d("typeId", "typeId:" + id);
        }
        showLoadingDialog();
        getDatas(Constant.LIST_LOAD_FIRST);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        btnReturn = (Button) this.findViewById(R.id.return_details_return);
        tvStartDate = (TextView) this.findViewById(R.id.return_details_start_year);
        tvEndData = (TextView) this.findViewById(R.id.return_details_end_year);
        btnQury = (TextView) this.findViewById(R.id.return_details_end_query);
        swiperefreshlayout = (PullLoadMoreRecyclerView) this.findViewById(R.id.returndetails_pullLoadMoreRecyclerView);
        swiperefreshlayout.setVisibility(View.VISIBLE);
        //设置加载更多背景色
        swiperefreshlayout.setColorSchemeResources(R.color.titlebackgourndColor);
        swiperefreshlayout.setLinearLayout();
        swiperefreshlayout.setPullRefreshEnable(false);
        adapter = new RecyclerReturnDetailsAdapter(this, listDatas);
        swiperefreshlayout.setAdapter(adapter);
        swiperefreshlayout.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getDatas(Constant.LIST_REFRESH);
            }

            @Override
            public void onLoadMore() {
                page++;
                getDatas(Constant.LIST_LOAD_MORE);
            }
        });
        initListener();
    }

    /**
     * 注册控件的监听
     */
    private void initListener() {
        tvEndData.setOnClickListener(this);
        btnReturn.setOnClickListener(this);
        tvStartDate.setOnClickListener(this);
        btnQury.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.return_details_return:
                finish();
                break;
            case R.id.return_details_end_year:
                showDialog(tvEndData);
                //  selectPopWindow(tvStartDate);
                break;
            case R.id.return_details_start_year:
                // selectPopWindow(tvEndData);
                showDialog(tvStartDate);
                break;
            case R.id.return_details_end_query:
                if (!TextUtils.isEmpty(tvEndData.getText().toString())) {
                    endtime = tvEndData.getText().toString();
                }
                if (!TextUtils.isEmpty(tvStartDate.getText().toString())) {
                    begintime = tvStartDate.getText().toString();
                }
                Log.d("aflf", "endtime: " + endtime);
                Log.d("aflf", "begintime: " + begintime);
                showLoadingDialog();
                getDatas(Constant.LIST_REFRSH_SELCT);
                break;

        }

    }

    private Calendar calendar;

    private void showDialog(final TextView tv) {
        //定义显示时间控件
        calendar = Calendar.getInstance();
        //通过自定义控件AlertDialog实现
        final AlertDialog builder = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.layout_select_returndetails_popwindow, null);
        TextView ok = (TextView) view.findViewById(R.id.layout_select_returndetails_ok);
        TextView cancle = (TextView) view.findViewById(R.id.layout_select_returndetails_cancle);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.layout_select_returndetails_datepicker);
        TextView one = (TextView) view.findViewById(R.id.select_returndetails_onemonth);
        TextView two = (TextView) view.findViewById(R.id.select_returndetails_twomonth);
        TextView three = (TextView) view.findViewById(R.id.select_returndetails_threemonth);
        //设置日期简略显示 否则详细显示 包括:星期周
        // datePicker.setCalendarViewShown(false);
        //初始化当前日期
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.setMaxDate(System.currentTimeMillis());//设置最大日期
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), null);
        //设置date布局
        builder.setView(view);
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.set(Calendar.YEAR, datePicker.getYear());
                calendar.set(Calendar.MONTH, datePicker.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String result = df.format(calendar.getTime());
                tvEndData.setText(result);
                tvStartDate.setText(getLastMonth());
                builder.dismiss();
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.set(Calendar.YEAR, datePicker.getYear());
                calendar.set(Calendar.MONTH, datePicker.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String result = df.format(calendar.getTime());
                tvEndData.setText(result);
                tvStartDate.setText(getLastTwoMonth());
                builder.dismiss();
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.set(Calendar.YEAR, datePicker.getYear());
                calendar.set(Calendar.MONTH, datePicker.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String result = df.format(calendar.getTime());
                tvEndData.setText(result);
                tvStartDate.setText(getLastThreeMonth());
                Log.d("aflf", "onClick() called with: " + getLastThreeMonth());
                builder.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.set(Calendar.YEAR, datePicker.getYear());
                calendar.set(Calendar.MONTH, datePicker.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String result = df.format(calendar.getTime());
                tv.setText(result);
                builder.dismiss();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        builder.show();

    }


    /**
     * 获取数据
     */
    private void getDatas(final int flag) {
        Map<String, String> params = new HashMap<>();
       /* String id=getSharedValue(this, "userID", "null");
        if (id.equals("null")){
            showText(getString(R.string.query_out_login));
            closeLoadingDialog();
            return;
        }*/
        params.put("id", id);
        params.put("page", page + "");
        //  params.put("mainid",getSharedValue(this, "userID", "null"));
        params.put("pagesize", pagesize + "");
        if (!TextUtils.isEmpty(begintime)) {
            params.put("begintime", "");
        }
        if (!TextUtils.isEmpty(endtime)) {
            params.put("endtime", "");
        }
        OkHttpClientManager.postAsyn(Constant.RETURN_DETAILS_LIST, new OkHttpClientManager.ResultCallback<BaseList<ReturnDetailsInfo>>() {
            @Override
            public void onError(Request request, Exception e) {
                Log.d("aflf", "onError() called with: " + "request = [" + request + "], e = [" + e + "]");
                if (flag == Constant.LIST_REFRESH) {
                    swiperefreshlayout.setPullLoadMoreCompleted();
                } else if (flag == Constant.LIST_LOAD_MORE) {
                    swiperefreshlayout.setPullLoadMoreCompleted();
                } else if (flag == Constant.LIST_LOAD_FIRST) {
                    swiperefreshlayout.showEmptyView(getString(R.string.hint_errow));
                }
                closeLoadingDialog();
            }

            @Override
            public void onResponse(BaseList<ReturnDetailsInfo> response) {
                closeLoadingDialog();
                Log.d("response", "onResponse() called with: " + response.getList().size() + "]");
                if (flag == Constant.LIST_LOAD_FIRST) {
                    Log.d("size", "onResponse: " + response.getList().size());
                    listDatas.addAll(response.getList());
                    adapter.notifyDataSetChanged();
                    if (response.getList().size() == 0) {
                        swiperefreshlayout.showEmptyView(getString(R.string.himt_null_string));
                    }
                } else if (flag == Constant.LIST_LOAD_MORE) {
                    listDatas.addAll(response.getList());
                    adapter.notifyDataSetChanged();
                    swiperefreshlayout.setPullLoadMoreCompleted();
                    if (response.getList().size() == 0) {
                        showText(getString(R.string.load_more_null));
                    }
                } else if (flag == Constant.LIST_REFRESH) {
                    listDatas.clear();
                    listDatas.addAll(response.getList());
                    adapter.notifyDataSetChanged();
                    swiperefreshlayout.setPullLoadMoreCompleted();
                    if (response.getList().size() == 0) {
                        swiperefreshlayout.showEmptyView(getString(R.string.himt_null_string));
                    }
                }
            }
        }, params);

    }
/*

    private void showDatePicker(final TextView tv_date) {
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog dialog = new DatePickerDialog(this, null, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                calendar.set(Calendar.YEAR, dialog.getDatePicker().getYear());
                calendar.set(Calendar.MONTH, dialog.getDatePicker().getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, dialog.getDatePicker().getDayOfMonth());
                SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
                String result = df.format(calendar.getTime());
                tv_date.setText(result);
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

*/

    /**
     * 获取上一个月
     *
     * @return
     */
    private String getLastMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(cal.MONTH, -1);
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        String lastMonth = dft.format(cal.getTime());
        return lastMonth;
    }

    /**
     * 获取上两个个月
     *
     * @return
     */
    private String getLastTwoMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(cal.MONTH, -2);
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        String lastMonth = dft.format(cal.getTime());
        return lastMonth;
    }

    /**
     * 获取上三个月
     *
     * @return
     */
    private String getLastThreeMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(cal.MONTH, -3);
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        String lastMonth = dft.format(cal.getTime());
        return lastMonth;
    }
}
