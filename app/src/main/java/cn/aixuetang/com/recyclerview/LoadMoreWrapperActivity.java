package cn.aixuetang.com.recyclerview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.leowong.extendedrecyclerview.ExtendedRecyclerView;
import com.leowong.extendedrecyclerview.adapters.LoadMoreWrapperAdapter;
import com.leowong.extendedrecyclerview.models.ViewItem;

import java.util.ArrayList;
import java.util.List;

import cn.aixuetang.com.recyclerview.widgets.adapters.GridCommonAdapter;

/**
 * User: wanglg
 * Date: 2017-10-27
 * Time: 14:35
 * FIXME
 */
public class LoadMoreWrapperActivity extends Activity implements LoadMoreWrapperAdapter.ILoadMoreCallback {
    private ExtendedRecyclerView mRecycler;
    private GridCommonAdapter gridCommonAdapter;
    private LoadMoreWrapperAdapter loadMoreWrapperAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        mRecycler = (ExtendedRecyclerView) findViewById(R.id.list);
        gridCommonAdapter = new GridCommonAdapter(getList(15));
        loadMoreWrapperAdapter = new LoadMoreWrapperAdapter(gridCommonAdapter);
        loadMoreWrapperAdapter.setLoadMoreView(com.leowong.extendedrecyclerview.R.layout.layout_more_progress);
        loadMoreWrapperAdapter.setLoadMoreCallback(this);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setAdapter(loadMoreWrapperAdapter);
    }

    public List<String> getList(int length) {
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            datas.add(i + " str");
        }
        return datas;
    }

    @Override
    public void loadMore(RecyclerView.ViewHolder holder, int position) {
        final int index = position;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                List<String> data = gridCommonAdapter.getDatas();

                for (int i = index; i < index + 15; i++) {
                    if (i >= 100) {
                        loadMoreWrapperAdapter.setLoadingCompleted(true);
                        break;
                    }
                    data.add(i + " str");
                }
                loadMoreWrapperAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }
}
