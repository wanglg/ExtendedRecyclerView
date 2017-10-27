package cn.aixuetang.com.recyclerview.widgets.adapters;

import com.leowong.extendedrecyclerview.adapters.CommonAdapter;

import java.util.List;

import cn.aixuetang.com.recyclerview.R;

/**
 * User: wanglg
 * Date: 2015-12-07
 * Time: 11:46
 * FIXME
 */
public class GridCommonAdapter extends CommonAdapter<String> {
    public GridCommonAdapter(List<String> mDatas) {
        super(mDatas);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_grid_string;
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        holder.setText(R.id.textView, mDatas.get(position));
    }

  /*  public GridCommonAdapter(List<String> mDatas, int layoutId) {
        super(mDatas, layoutId);
    }*/

  /*  @Override
    public int getNormalLayoutId(int itemType) {
        return R.layout.item_grid_string;
    }

    @Override
    public void onBindNormalViewHolder(CommonViewHolder holder, int position) {
        holder.setText(R.id.textView, mDatas.get(position).model.toString());
    }*/
}
