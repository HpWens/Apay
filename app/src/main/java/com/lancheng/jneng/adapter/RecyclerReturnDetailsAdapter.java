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
import com.lancheng.jneng.entity.ReturnDetailsInfo;

import java.util.List;

/**
 * Created by AFLF on 2016/6/6. 回报列表
 */
public class RecyclerReturnDetailsAdapter extends RecyclerView.Adapter<RecyclerReturnDetailsAdapter.ViewHolder> {

    private Context mContext;
    private List<ReturnDetailsInfo> mDatas;

    public List<ReturnDetailsInfo> getDataList() {
        return mDatas;
    }

    public void removeAllDataList() {
        this.mDatas.removeAll(mDatas);
    }

    public RecyclerReturnDetailsAdapter(Context mContext,
                                        List<ReturnDetailsInfo> mDatas) {
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
        public TextView title, tab1, tab2, tab3, tab4;
        public LinearLayout view;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_returndetails_title);
            tab1 = (TextView) itemView.findViewById(R.id.item_returndetails_sell);
            tab2 = (TextView) itemView.findViewById(R.id.item_returndetails_operate);
            tab3 = (TextView) itemView.findViewById(R.id.item_returndetails_manage);
            tab4 = (TextView) itemView.findViewById(R.id.item_returndetails_return);
            view = (LinearLayout) itemView.findViewById(R.id.item_returndetails_view);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_returndetails, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (!TextUtils.isEmpty(mDatas.get(position).getName())){
            holder.title.setText(mDatas.get(position).getName());
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getSales())){
            holder.tab1.setText(mDatas.get(position).getSales());
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getCost())){
            holder.tab2.setText(mDatas.get(position).getCost());
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getRuncost())){
            holder.tab3.setText(mDatas.get(position).getRuncost());
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getProfit())){
            holder.tab4.setText(mDatas.get(position).getProfit());
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