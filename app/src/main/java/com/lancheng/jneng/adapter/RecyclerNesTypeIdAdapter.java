package com.lancheng.jneng.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lancheng.jneng.R;
import com.lancheng.jneng.entity.CommunityIdTypeInfo;

import java.util.List;

/**
 * Created by AFLF on 2016/6/6. 新闻列表分类id
 */
public class RecyclerNesTypeIdAdapter extends RecyclerView.Adapter<RecyclerNesTypeIdAdapter.ViewHolder>{

private Context mContext;
private List<CommunityIdTypeInfo>mDatas;

public List<CommunityIdTypeInfo>getDataList(){
        return mDatas;
        }

public void removeAllDataList(){
        this.mDatas.removeAll(mDatas);
        }

public RecyclerNesTypeIdAdapter(Context mContext,
        List<CommunityIdTypeInfo>mDatas){
        super();
        this.mDatas=mDatas;
        this.mContext=mContext;
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
    public Button desc;
    public LinearLayout view;

    public ViewHolder(View itemView) {
        super(itemView);
        desc = (Button) itemView.findViewById(R.id.item_new_typeid_desc);
        view = (LinearLayout) itemView.findViewById(R.id.item_news_id_view);

    }

}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_typeid, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (!TextUtils.isEmpty(mDatas.get(position).getName())) {
            holder.desc.setText(mDatas.get(position).getName());
        }
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
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