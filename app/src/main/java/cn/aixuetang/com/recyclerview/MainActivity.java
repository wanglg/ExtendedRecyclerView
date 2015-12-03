package cn.aixuetang.com.recyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.leo.extendedrecyclerview.adapters.LoadMoreAdapter;
import com.leo.extendedrecyclerview.models.ViewItem;
import com.leo.extendedrecyclerview.widgets.ExtendedRecyclerView;

import java.util.ArrayList;

import cn.aixuetang.com.recyclerview.widgets.adapters.StringListAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, LoadMoreAdapter.ILoadMoreCallback {
    private ExtendedRecyclerView mRecycler;
    private StringListAdapter mAdapter;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_sample);

        ArrayList<ViewItem> list = new ArrayList<>();
        mAdapter = new StringListAdapter(list, R.layout.view_more_progress);

        mRecycler = (ExtendedRecyclerView) findViewById(R.id.list);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
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
//                        mAdapter.replaceAll(new ArrayList<ViewItem>());
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
                        ArrayList<ViewItem> data = new ArrayList<ViewItem>();
                        for (int i = 0; i < LoadMoreAdapter.pageCount; i++) {
                            data.add(new ViewItem(0, "default str" + position++));
                        }
                        mAdapter.addAll(data);
                    }
                });
            }
        });
        thread.start();
        mAdapter.setLoadMoreCallback(this);
        mRecycler.setProgressAdapter(mAdapter);
        mRecycler.setRefreshListener(this);
        FadeInAnimator fadeInAnimator = new FadeInAnimator();
        mRecycler.setItemAnimator(fadeInAnimator);
        mRecycler.setRefreshingColorResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_red_light);
    }


    @Override
    public void onRefresh() {
        Toast.makeText(this, "Refresh", Toast.LENGTH_LONG).show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                position = 0;
                ArrayList<ViewItem> data = new ArrayList<ViewItem>();
                for (int i = 0; i < LoadMoreAdapter.pageCount; i++) {
                    data.add(new ViewItem(0, "refresh str" + position++));
                }
//                mAdapter.setLoadingCompleted(false);
                mAdapter.replaceAll(data);
            }
        }, 2000);
    }

    @Override
    public void loadMore(int position) {
        Toast.makeText(this, "loadmore-->" + position, Toast.LENGTH_LONG).show();
        final int index = position;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                ArrayList<ViewItem> data = new ArrayList<ViewItem>();

                for (int i = index; i < index + LoadMoreAdapter.pageCount; i++) {
                    if (i >= 100)
                        break;
                    data.add(new ViewItem(0, "loadmore str" + i));
                }
                mAdapter.addAll(data);
            }
        }, 2000);
    }
}
