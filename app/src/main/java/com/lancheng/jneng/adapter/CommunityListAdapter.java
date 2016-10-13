package com.lancheng.jneng.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lancheng.jneng.R;
import com.lancheng.jneng.utils.Commons;

import java.util.List;

/**
 * Created by AFLF on 2016/6/6. 新闻列表
 */
public class CommunityListAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> BrandDatas;

    public CommunityListAdapter(Context mContext,
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
            view = View.inflate(mContext, R.layout.item_communitylist, null);
            // 减少子孩子的查询次数
            viewHolder = new ViewHolder();
            viewHolder.name= (TextView) view.findViewById(R.id.item_community_name);
            viewHolder.desc = (TextView) view.findViewById(R.id.item_community_desc);
            // 当产生的时候找到XML子引用
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name.setText("新闻题目"+position);
        String s="adddddd投资列表项目你的我的大家的投资列表项目你的我的大家的投资列表项目你的我的大家资列表项目你的我的大资列表项目你的我的大资列表项目你的我的大资列表项目你的我的大dddddddddddddddddddddddddddddddddddddddddddd";
        viewHolder.desc.setText(Commons.ToSBC(s)+position);

        return view;
    }
    /*
	 * View对象的容器，记录子孩子的内存地址 viewholder 相当于一个记事本
	 */
    class ViewHolder {
        /*
         *
         */
        private TextView desc,name;
    }
}
