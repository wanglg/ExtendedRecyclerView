package cn.aixuetang.com.recyclerview.widgets.adapters;

import com.leowong.extendedrecyclerview.adapters.LoadMoreAdapter;
import com.leowong.extendedrecyclerview.models.ViewItem;

import java.util.List;

import cn.aixuetang.com.recyclerview.R;

/**
 * User: wanglg
 * Date: 2015-12-07
 * Time: 11:46
 * FIXME
 */
public class GridAdapter extends LoadMoreAdapter {
    public GridAdapter(List<ViewItem> mDatas) {
        super(mDatas);
    }

    public GridAdapter(List<ViewItem> mDatas, int layoutId) {
        super(mDatas, layoutId);
    }

    @Override
    public int getNormalLayoutId(int itemType) {
        return R.layout.item_grid_string;
    }

    @Override
    public void onBindNormalViewHolder(CommonViewHolder holder, int position) {
        holder.setText(R.id.textView, mDatas.get(position).model.toString());
    }
}
