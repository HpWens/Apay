package com.lancheng.jneng.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lancheng.jneng.R;
import com.lancheng.jneng.view.RoundProgressBar;

import java.util.List;

/**
 * Created by AFLF on 2016/6/6. 投资列表
 */
public class InverstmentListAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> BrandDatas;

    public InverstmentListAdapter(Context mContext,
                                  List<String> BrandDatas) {
        super();
        this.mContext = mContext;
        this.BrandDatas = BrandDatas;

    }

    @Override
    public int getCount() {
        return BrandDatas.size();
    }

    @Override
    public Object getItem(int position) {
       return BrandDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        // 减少view的创建次数
        View view;
        if (convertView == null) {
            view = View.inflate(mContext, R.layout.item_inverstmentlist, null);
            // 减少子孩子的查询次数
            viewHolder = new ViewHolder();
          //  viewHolder.desc = (TextView) view.findViewById(R.id.item_inverstment_name);
            viewHolder.progressBar= (RoundProgressBar) view.findViewById(R.id.item_inverstment_roundprogressbar);
            // 当产生的时候找到XML子引用
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
      // viewHolder.desc.setText("投资列表项目"+position);
        viewHolder.progressBar.setProgress(60+position);

        return view;
    }
    /*
	 * View对象的容器，记录子孩子的内存地址 viewholder 相当于一个记事本
	 */
    class ViewHolder {
        /*
         *
         */
        private TextView title;
        private RoundProgressBar progressBar;
    }
}
