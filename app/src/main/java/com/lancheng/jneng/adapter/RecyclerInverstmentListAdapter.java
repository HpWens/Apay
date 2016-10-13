package com.lancheng.jneng.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lancheng.jneng.R;
import com.lancheng.jneng.entity.InvestmentTypeInfo;
import com.lancheng.jneng.view.RoundProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AFLF on 2016/6/6. 投资列表
 */
public class RecyclerInverstmentListAdapter extends RecyclerView.Adapter<RecyclerInverstmentListAdapter.ViewHolder> {

    private Context mContext;
    private List<InvestmentTypeInfo> mDatas;

    public List<InvestmentTypeInfo> getDataList() {
        return mDatas;
    }

    public void removeAllDataList() {
        this.mDatas.removeAll(mDatas);
    }

    public RecyclerInverstmentListAdapter(Context mContext,
                                          List<InvestmentTypeInfo> mDatas) {
        super();
        if (mDatas != null) {
            this.mDatas = mDatas;
        } else {
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
        public RoundProgressBar progressBar;
        public TextView tvName, tvMoney, tvRate, tvDuration, tvAward, tvState;//名字，投资金额，期限，利率，状态
        public LinearLayout view;
        public View views;

        public ViewHolder(View itemView) {
            super(itemView);
            progressBar = (RoundProgressBar) itemView.findViewById(R.id.item_inverstment_roundprogressbar);
            tvName = (TextView) itemView.findViewById(R.id.item_inverstment_name);
            tvMoney = (TextView) itemView.findViewById(R.id.item_inverstment_money);
            tvRate = (TextView) itemView.findViewById(R.id.item_inverstment_rate);
            tvDuration = (TextView) itemView.findViewById(R.id.item_inverstment_duration);
            tvAward = (TextView) itemView.findViewById(R.id.item_inverstment_award);
            tvState = (TextView) itemView.findViewById(R.id.item_inverstment_state);
            view = (LinearLayout) itemView.findViewById(R.id.item_inverstmentlist_view);
            views = (View) itemView.findViewById(R.id.item_inverstmentlist_views);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inverstmentlist, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.views.setVisibility(View.VISIBLE);
        Log.d("updateprogress", "updateprogress" + mDatas.get(position).getInvestment());
        if ((!TextUtils.isEmpty(mDatas.get(position).getInvestment()))) {
            String s = mDatas.get(position).getInvestment().toString();
            int progress = Integer.parseInt(s.substring(s.indexOf(".") + 1)) * 100;
            Log.d("updateprogress", "updateprogress" + progress);
            holder.progressBar.setProgress(progress);
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getName())) {
            holder.tvName.setText("【担】"+mDatas.get(position).getName());
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getPrice())) {
            holder.tvMoney.setText("￥" + mDatas.get(position).getPrice());
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getInterest())) {
            holder.tvRate.setText(mDatas.get(position).getInterest());
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getDeadline())) {
            holder.tvDuration.setText(mDatas.get(position).getDeadline());
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getPoint())) {
            holder.tvAward.setText(mDatas.get(position).getPoint());
        }
        if (!TextUtils.isEmpty(mDatas.get(position).getStatus())) {
            holder.tvState.setText(mDatas.get(position).getStatus());
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