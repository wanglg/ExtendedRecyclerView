package com.leowong.extendedrecyclerview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * Default Pull-refresh View
 * User: wanglg
 * Date: 2016-01-14
 * Time: 14:07
 * FIXME
 */
class PullToRefreshHandlerView extends SwipeRefreshLayout implements ExtendedRecyclerView.PullToRefreshHandler {
    public PullToRefreshHandlerView(Context context) {
        super(context);
        initView();
    }

    public PullToRefreshHandlerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        setEnabled(false);
        setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
    }

    @Override
    public void swipeRefreshComplete() {
        setRefreshing(false);
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        setEnabled(true);
        super.setOnRefreshListener(listener);
    }
}
