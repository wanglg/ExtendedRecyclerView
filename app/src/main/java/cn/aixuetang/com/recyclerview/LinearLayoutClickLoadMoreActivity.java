package cn.aixuetang.com.recyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.leowong.extendedrecyclerview.ExtendedRecyclerView;
import com.leowong.extendedrecyclerview.adapters.CommonAdapter;
import com.leowong.extendedrecyclerview.adapters.LoadMoreAdapter;
import com.leowong.extendedrecyclerview.decoration.DividerItemDecoration;
import com.leowong.extendedrecyclerview.models.ViewItem;

import java.util.ArrayList;

import cn.aixuetang.com.recyclerview.widgets.adapters.ClickLoadListAdapter;
import jp.wasabeef.recyclerview.animators.FadeInAnimator;

public class LinearLayoutClickLoadMoreActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, LoadMoreAdapter.ILoadMoreCallback {
    private ExtendedRecyclerView mRecycler;
    private ClickLoadListAdapter mAdapter;
    int position = 0;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_sample);

        ArrayList<ViewItem> list = new ArrayList<>();
        mAdapter = new ClickLoadListAdapter(list, R.layout.item_click_loadmore);

        mRecycler = (ExtendedRecyclerView) findViewById(R.id.list);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(linearLayoutManager);
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
                        ArrayList<ViewItem> data = new ArrayList<ViewItem>();
                        for (int i = 0; i < mAdapter.getPageCount(); i++) {
                            data.add(new ViewItem(0, "default str" + position++));
                        }
                        mAdapter.addAll(data);
                    }
                });
            }
        });
        thread.start();
        mAdapter.setLoadMoreCallback(this);
        ((SwipeRefreshLayout) mRecycler.getSwipeRefreshView()).setOnRefreshListener(this);
        FadeInAnimator fadeInAnimator = new FadeInAnimator();
        mRecycler.setItemAnimator(fadeInAnimator);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        dividerItemDecoration.setLeftMarign(200);
        dividerItemDecoration.setRightMarign(100);
        mRecycler.addItemDecoration(dividerItemDecoration);
    }


    @Override
    public void onRefresh() {
        Toast.makeText(this, "Refresh", Toast.LENGTH_LONG).show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                position = 0;
                ArrayList<ViewItem> data = new ArrayList<ViewItem>();
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
    public void loadMore(final CommonAdapter.CommonViewHolder holder, int position) {
        Toast.makeText(this, "loadmore-->" + position, Toast.LENGTH_LONG).show();
        final int index = position;
        holder.setOnClickListener(R.id.progressText, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.setVisible(R.id.progressText, View.INVISIBLE);
                holder.setVisible(R.id.progress, View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        ArrayList<ViewItem> data = new ArrayList<ViewItem>();

                        for (int i = index; i < index + mAdapter.getPageCount(); i++) {
                            if (i >= 100)
                                break;
                            data.add(new ViewItem(0, "loadmore str" + i));
                        }
                        mAdapter.addAll(data);
                    }
                }, 2000);
            }
        });
    }
}
