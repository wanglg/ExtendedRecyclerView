package com.leowong.extendedrecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;


public class ExtendedRecyclerView extends FrameLayout {


    protected RecyclerView mRecycler;
    protected ViewStub mProgress;
    protected ViewStub mEmpty;
    protected View mProgressView;
    protected View mEmptyView;

    protected boolean mClipToPadding;
    protected int mPadding;
    protected int mPaddingTop;
    protected int mPaddingBottom;
    protected int mPaddingLeft;
    protected int mPaddingRight;
    protected int mScrollbarStyle;
    protected int mEmptyId;


    protected SwipeRefreshLayout mPtrLayout;

    protected int mRecyclerViewMainLayout;
    private int mProgressId;

    public SwipeRefreshLayout getSwipeToRefresh() {
        return mPtrLayout;
    }

    public RecyclerView getRecyclerView() {
        return mRecycler;
    }

    public ExtendedRecyclerView(Context context) {
        super(context);
        initView();
    }

    public ExtendedRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initView();
    }

    public ExtendedRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(attrs);
        initView();
    }

    protected void initAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.extendedrecyclerview);
        try {
            mRecyclerViewMainLayout = a.getResourceId(R.styleable.extendedrecyclerview_mainLayoutId, R.layout.layout_progress_recyclerview);
            mClipToPadding = a.getBoolean(R.styleable.extendedrecyclerview_recyclerClipToPadding, false);
            mPadding = (int) a.getDimension(R.styleable.extendedrecyclerview_recyclerPadding, -1.0f);
            mPaddingTop = (int) a.getDimension(R.styleable.extendedrecyclerview_recyclerPaddingTop, 0.0f);
            mPaddingBottom = (int) a.getDimension(R.styleable.extendedrecyclerview_recyclerPaddingBottom, 0.0f);
            mPaddingLeft = (int) a.getDimension(R.styleable.extendedrecyclerview_recyclerPaddingLeft, 0.0f);
            mPaddingRight = (int) a.getDimension(R.styleable.extendedrecyclerview_recyclerPaddingRight, 0.0f);
            mScrollbarStyle = a.getInt(R.styleable.extendedrecyclerview_scrollbarStyle, -1);
            mEmptyId = a.getResourceId(R.styleable.extendedrecyclerview_layout_empty, 0);
            mProgressId = a.getResourceId(R.styleable.extendedrecyclerview_layout_progress, R.layout.layout_progress);
        } finally {
            a.recycle();
        }
    }

    private void initView() {
        if (isInEditMode()) {
            return;
        }
        ViewGroup v = (ViewGroup) LayoutInflater.from(getContext()).inflate(mRecyclerViewMainLayout, this);
        initRecyclerView(v);

        mPtrLayout = (SwipeRefreshLayout) v.findViewById(R.id.ptr_layout);
        mPtrLayout.setEnabled(false);
        mPtrLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light,
                android.R.color.holo_green_light, android.R.color.holo_red_light);
        mProgress = (ViewStub) v.findViewById(android.R.id.progress);

        mProgress.setLayoutResource(mProgressId);
        mProgressView = mProgress.inflate();


        mEmpty = (ViewStub) v.findViewById(R.id.empty);
        mEmpty.setLayoutResource(mEmptyId);
        if (mEmptyId != 0)
            mEmptyView = mEmpty.inflate();
        mEmpty.setVisibility(View.GONE);


    }

    /**
     * Implement this method to customize the AbsListView
     */
    protected void initRecyclerView(View view) {
        View recyclerView = view.findViewById(android.R.id.list);

        if (recyclerView instanceof RecyclerView)
            mRecycler = (RecyclerView) recyclerView;
        else {
            throw new IllegalArgumentException("ExtendedRecyclerView works with a RecyclerView!");
        }
        mRecycler.setHasFixedSize(true);
        mRecycler.setClipToPadding(mClipToPadding);


        if (mPadding != -1.0f) {
            mRecycler.setPadding(mPadding, mPadding, mPadding, mPadding);
        } else {
            mRecycler.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
        }

        if (mScrollbarStyle != -1) {
            mRecycler.setScrollBarStyle(mScrollbarStyle);
        }
    }


    /**
     * Set the layout manager to the recycler
     *
     * @param manager
     */
    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        mRecycler.setLayoutManager(manager);
    }

    /**
     * Set the adapter to the recycler
     * Automatically hide the progressbar
     * Set the refresh to false
     * If adapter is empty, then the emptyview is shown
     *
     * @param adapter
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecycler.setAdapter(adapter);
        mProgress.setVisibility(View.GONE);
        mRecycler.setVisibility(View.VISIBLE);
        mPtrLayout.setRefreshing(false);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                update();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                update();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                update();
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                update();
            }

            @Override
            public void onChanged() {
                super.onChanged();
                update();
            }

            private void update() {
                mProgress.setVisibility(View.GONE);
                mPtrLayout.setRefreshing(false);
                if (mRecycler.getAdapter().getItemCount() == 0 && mEmptyId != 0) {
                    mEmpty.setVisibility(View.VISIBLE);
                } else if (mEmptyId != 0) {
                    mEmpty.setVisibility(View.GONE);
                }
            }
        });
        if ((adapter.getItemCount() == 0) && mEmptyId != 0) {
            mEmpty.setVisibility(View.VISIBLE);
        } else if (mEmptyId != 0) {
            mEmpty.setVisibility(View.GONE);
        }
    }

    /**
     * Set the empty adapter to the recycler
     * show the progressbar
     * hide recycler view ,empty view
     * adapter data should be empty
     *
     * @param adapter
     */
    public void setProgressAdapter(RecyclerView.Adapter adapter) {
        mRecycler.setAdapter(adapter);
        mProgress.setVisibility(View.VISIBLE);
        mRecycler.setVisibility(View.GONE);
        mEmpty.setVisibility(View.GONE);
        mPtrLayout.setRefreshing(false);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                update();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                update();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                update();
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                update();
            }

            @Override
            public void onChanged() {
                super.onChanged();
                update();
            }

            private void update() {
                mProgress.setVisibility(View.GONE);
                mPtrLayout.setRefreshing(false);
                mRecycler.setVisibility(View.VISIBLE);
                if (mRecycler.getAdapter().getItemCount() == 0 && mEmptyId != 0) {
                    mEmpty.setVisibility(View.VISIBLE);
                } else if (mEmptyId != 0) {
                    mEmpty.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * Remove the adapter from the recycler
     */
    public void clear() {
        mRecycler.setAdapter(null);
    }

    /**
     * Show the progressbar
     */
    public void showProgress() {
        hideRecycler();
        if (mEmptyId != 0) mEmpty.setVisibility(View.INVISIBLE);
        mProgress.setVisibility(View.VISIBLE);
    }

    /**
     * Return the {@link RecyclerView.LayoutManager} currently responsible for
     * layout policy for this RecyclerView.
     *
     * @return The currently bound LayoutManager
     */
    public RecyclerView.LayoutManager getLayoutManager() {
        return mRecycler.getLayoutManager();
    }

    /**
     * Hide the progressbar and show the recycler
     */
    public void showRecycler() {
        hideProgress();
        mRecycler.setVisibility(View.VISIBLE);
    }


    /**
     * Set the listener when refresh is triggered and enable the SwipeRefreshLayout
     *
     * @param listener
     */
    public void setRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        mPtrLayout.setEnabled(true);
        mPtrLayout.setOnRefreshListener(listener);
    }

    /**
     * Set the colors for the SwipeRefreshLayout states
     *
     * @param colRes1
     * @param colRes2
     * @param colRes3
     * @param colRes4
     */
    public void setRefreshingColorResources(int colRes1, int colRes2, int colRes3, int colRes4) {
        mPtrLayout.setColorSchemeResources(colRes1, colRes2, colRes3, colRes4);
    }

    /**
     * Set the colors for the SwipeRefreshLayout states
     *
     * @param col1
     * @param col2
     * @param col3
     * @param col4
     */
    public void setRefreshingColor(int col1, int col2, int col3, int col4) {
        mPtrLayout.setColorSchemeColors(col1, col2, col3, col4);
    }

    /**
     * Hide the progressbar
     */
    public void hideProgress() {
        mProgress.setVisibility(View.GONE);
    }

    /**
     * Hide the recycler
     */
    public void hideRecycler() {
        mRecycler.setVisibility(View.GONE);
    }

    /**
     * @return the recycler adapter
     */
    public RecyclerView.Adapter getAdapter() {
        return mRecycler.getAdapter();
    }

    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        mRecycler.setItemAnimator(animator);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecycler.addItemDecoration(itemDecoration);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration, int index) {
        mRecycler.addItemDecoration(itemDecoration, index);
    }

    public void removeItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecycler.removeItemDecoration(itemDecoration);
    }

    /**
     * @return inflated progress view or null
     */
    public View getProgressView() {
        return mProgressView;
    }


    /**
     * @return inflated empty view or null
     */
    public View getEmptyView() {
        return mEmptyView;
    }

}
