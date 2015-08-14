package cn.aixuetang.com.superrecyclerview.widgets.adapters;


import com.leo.extendedrecyclerview.adapters.LoadMoreAdapter;
import com.leo.extendedrecyclerview.models.ViewItem;

import java.util.List;

import cn.aixuetang.com.superrecyclerview.R;

public class StringListAdapter extends LoadMoreAdapter {
    public StringListAdapter(List<ViewItem> mDatas) {
        super(mDatas);
    }
    public StringListAdapter(List<ViewItem> mDatas,int layoutId) {
        super(mDatas,layoutId);
    }
    @Override
    public int getNormalLayoutId() {
        return R.layout.item_string;
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (mDatas.get(position).viewType != VIEW_TYPE_ITEM_LOAD_MORE) {
            holder.setText(R.id.textView, mDatas.get(position).model.toString());
        }
    }
}
