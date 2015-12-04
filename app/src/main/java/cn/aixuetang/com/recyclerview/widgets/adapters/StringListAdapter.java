package cn.aixuetang.com.recyclerview.widgets.adapters;


import com.leowong.extendedrecyclerview.adapters.LoadMoreAdapter;
import com.leowong.extendedrecyclerview.models.ViewItem;

import java.util.List;

import cn.aixuetang.com.recyclerview.R;

public class StringListAdapter extends LoadMoreAdapter {
    public StringListAdapter(List<ViewItem> mDatas) {
        super(mDatas);
    }

    public StringListAdapter(List<ViewItem> mDatas, int layoutId) {
        super(mDatas, layoutId);
    }

    @Override
    public int getNormalLayoutId() {
        return R.layout.item_string;
    }

    @Override
    public void onBindNormalViewHolder(CommonViewHolder holder, int position) {
        holder.setText(R.id.textView, mDatas.get(position).model.toString());
    }


}
