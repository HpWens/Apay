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
import com.lancheng.jneng.base.Constant;
import com.lancheng.jneng.entity.ReturnlistInfo;
import com.lancheng.jneng.utils.ImageUtilsView;
import com.lancheng.jneng.view.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 *  Created by AFLF on 2016/6/6. 交易列表股权转让
 */
public class RecyclerTradeListAdapter extends RecyclerView.Adapter<RecyclerTradeListAdapter.ViewHolder> {

    private Context mContext;
    private List<ReturnlistInfo> mDatas;

    public List<ReturnlistInfo> getDataList() {
        return mDatas;
    }

    public void removeAllDataList() {
        this.mDatas.removeAll(mDatas);
    }

    public RecyclerTradeListAdapter(Context mContext,
                                    List<ReturnlistInfo> mDatas) {
        super();
        this.mDatas = mDatas;
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
        public TextView title, address;
        public LinearLayout view;
        public RoundImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_tradelist_name);
            address = (TextView) itemView.findViewById(R.id.item_tradelist_address);
            icon = (RoundImageView) itemView.findViewById(R.id.item_tradelist_icon);
            view = (LinearLayout) itemView.findViewById(R.id.item_tradelist_view);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tradelist, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (!TextUtils.isEmpty(mDatas.get(position).getSrc())) {
            ImageLoader.getInstance().displayImage(
                    Constant.SERVER_IMAGE + mDatas.get(position).getSrc(),holder.icon, ImageUtilsView.imageOption());
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getName())) {
            holder.title.setText(mDatas.get(position).getName());
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getPrice())) {
            holder.address.setText("当前投资金额："+mDatas.get(position).getPrice());
        }
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
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