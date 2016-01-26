package cn.aixuetang.com.recyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.leowong.extendedrecyclerview.ExtendedRecyclerView;
import com.leowong.extendedrecyclerview.adapters.CommonAdapter;
import com.leowong.extendedrecyclerview.adapters.LoadMoreAdapter;
import com.leowong.extendedrecyclerview.decoration.GridSpacingItemDecoration;
import com.leowong.extendedrecyclerview.models.ViewItem;

import java.util.ArrayList;

import cn.aixuetang.com.recyclerview.widgets.CustomRefreshLayout;
import cn.aixuetang.com.recyclerview.widgets.adapters.GridAdapter;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;


public class CustomSwipeRefreshGridLayoutActivity extends AppCompatActivity implements LoadMoreAdapter.ILoadMoreCallback, PtrHandler {
    private ExtendedRecyclerView mRecycler;
    private GridAdapter mAdapter;
    int position = 0;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_refresh_sample);

        ArrayList<ViewItem> list = new ArrayList<>();
        mAdapter = new GridAdapter(list);

        mRecycler = (ExtendedRecyclerView) findViewById(R.id.list);
        gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mAdapter.getItemViewType(position)) {
                    case LoadMoreAdapter.VIEW_TYPE_ITEM_LOAD_MORE:
                        return gridLayoutManager.getSpanCount();
                    default:
                        return 1;
                }
            }
        });
        mRecycler.setLayoutManager(gridLayoutManager);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRecycler.setAdapter(mAdapter);
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<ViewItem> data = new ArrayList<>();
                        for (int i = 0; i < mAdapter.getPageCount(); i++) {
                            data.add(new ViewItem(0, "default str" + position++));
                        }
                        mAdapter.addAll(data);
                    }
                });
            }
        });
        thread.start();
        mAdapter.setPageCount(20);
        mAdapter.setLoadMoreCallback(this);
        ((CustomRefreshLayout) mRecycler.getSwipeRefreshView()).setPtrHandler(this);
        FadeInAnimator fadeInAnimator = new FadeInAnimator();
        mRecycler.setItemAnimator(fadeInAnimator);
        GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(2, 40, false);
        mRecycler.addItemDecoration(gridSpacingItemDecoration);
    }


    public void onRefresh() {
        Toast.makeText(this, "Refresh", Toast.LENGTH_LONG).show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                position = 0;
                ArrayList<ViewItem> data = new ArrayList<>();
                for (int i = 0; i < mAdapter.getPageCount(); i++) {
                    data.add(new ViewItem(0, "refresh str" + position++));
                }
                mAdapter.replaceAll(data);
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void loadMore(CommonAdapter.CommonViewHolder holder, int position) {
        Toast.makeText(this, "loadmore-->" + position, Toast.LENGTH_LONG).show();
        final int index = position;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                ArrayList<ViewItem> data = new ArrayList<>();

                for (int i = index; i < index + mAdapter.getPageCount(); i++) {
                    if (i >= 100)
                        break;
                    data.add(new ViewItem(0, "loadmore str" + i));
                }
                mAdapter.addAll(data);
            }
        }, 2000);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        onRefresh();
    }
}
