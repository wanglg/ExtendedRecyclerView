package cn.aixuetang.com.recyclerview.widgets.adapters;


import android.view.View;

import com.leowong.extendedrecyclerview.adapters.LoadMoreAdapter;
import com.leowong.extendedrecyclerview.models.ViewItem;

import java.util.List;

import cn.aixuetang.com.recyclerview.R;

public class ClickLoadListAdapter extends LoadMoreAdapter {
    public ClickLoadListAdapter(List<ViewItem> mDatas) {
        super(mDatas);
    }

    public ClickLoadListAdapter(List<ViewItem> mDatas, int layoutId) {
        super(mDatas, layoutId);
    }

    @Override
    public int getNormalLayoutId(int itemType) {
        return R.layout.item_string;
    }

    @Override
    public void onBindNormalViewHolder(CommonViewHolder holder, int position) {
        holder.setText(R.id.textView, mDatas.get(position).model.toString());
    }

    @Override
    public void onBindLoadMoreViewHolder(CommonViewHolder holder, int position) {
        super.onBindLoadMoreViewHolder(holder, position);
        holder.setVisible(R.id.progressText, View.VISIBLE);
        holder.setVisible(R.id.progress, View.INVISIBLE);
    }
}
