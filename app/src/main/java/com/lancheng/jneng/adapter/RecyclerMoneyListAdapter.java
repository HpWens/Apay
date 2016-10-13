package com.lancheng.jneng.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lancheng.jneng.R;
import com.lancheng.jneng.entity.MoneyListTypeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AFLF on 2016/6/6. 资产列表
 */
public class RecyclerMoneyListAdapter extends RecyclerView.Adapter<RecyclerMoneyListAdapter.ViewHolder> {

    private Context mContext;
    private List<MoneyListTypeInfo> mDatas;

    public List<MoneyListTypeInfo> getDataList() {
        return mDatas;
    }

    public void removeAllDataList() {
        this.mDatas.removeAll(mDatas);
    }

    public RecyclerMoneyListAdapter(Context mContext,
                                    List<MoneyListTypeInfo> mDatas) {
        super();
        if(mDatas != null){
            this.mDatas = mDatas;
        }else {
            this.mDatas = new ArrayList<>();
        }
        this.mContext = mContext;
    }

    //...点击事件
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, icon, date, mun;
        public LinearLayout view;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = (TextView) itemView.findViewById(R.id.moneylist_icon);
            title = (TextView) itemView.findViewById(R.id.moneylist_title);
            date = (TextView) itemView.findViewById(R.id.moneylist_date);
            mun = (TextView) itemView.findViewById(R.id.moneylist_mun);
            view = (LinearLayout) itemView.findViewById(R.id.item_moneylist_view);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moneylist, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(!TextUtils.isEmpty(mDatas.get(position).getName())){
            holder.title.setText(mDatas.get(position).getName());
        }
        if(!TextUtils.isEmpty(mDatas.get(position).getType())){
            holder.icon.setText(mDatas.get(position).getType());
        }
        if(!TextUtils.isEmpty(mDatas.get(position).getPrice())){
            holder.mun.setText("￥"+mDatas.get(position).getPrice());
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getAddtime())){
            holder.date.setText(mDatas.get(position).getAddtime());
        }

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            //设置背景
            holder.view.setBackgroundResource(R.drawable.selector_list_item_background);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);

                }
            });
            holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

}