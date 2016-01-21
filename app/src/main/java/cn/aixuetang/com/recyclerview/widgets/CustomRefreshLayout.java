package cn.aixuetang.com.recyclerview.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.leowong.extendedrecyclerview.ExtendedRecyclerView;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 自定义下来刷新view
 * User: wanglg
 * Date: 2016-01-15
 * Time: 14:37
 * FIXME
 */
public class CustomRefreshLayout extends PtrClassicFrameLayout implements ExtendedRecyclerView.PullToRefreshHandler {
    public CustomRefreshLayout(Context context) {
        super(context);
    }

    public CustomRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        setEnabled(false);
    }

    @Override
    public void swipeRefreshComplete() {
        refreshComplete();
    }

    @Override
    public void setPtrHandler(PtrHandler ptrHandler) {
        setEnabled(true);
        super.setPtrHandler(ptrHandler);
    }
}
