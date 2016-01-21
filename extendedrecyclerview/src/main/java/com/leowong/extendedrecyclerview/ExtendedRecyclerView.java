package com.leowong.extendedrecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.leowong.library.multistatelayout.MultiStateLayout;

/**
 * User: wanglg
 * Date: 2016-01-13
 * Time: 14:34
 * FIXME
 */
public class ExtendedRecyclerView extends MultiStateLayout {

    protected boolean mClipToPadding;
    protected int mPadding;
    protected int mPaddingTop;
    protected int mPaddingBottom;
    protected int mPaddingLeft;
    protected int mPaddingRight;
    protected int mScrollbarStyle;
    private PullToRefreshHandler pullToRefreshHandler;


    /**
     * Pull-refresh View
     */
    private ViewGroup mSwipeRefreshView;
    private RecyclerView mRecyclerView;

    public ExtendedRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(attrs);
        initRecyclerView();
        initSwipeRefreshView();
    }

    public ExtendedRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(attrs);
        initRecyclerView();
        initSwipeRefreshView();
    }

    @Override
    protected int getDefaultContentLayoutId(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ExtendedRecyclerView);
        int layoutId = R.layout.layout_extendrecycler_content;
        if (a != null) {
            try {
                layoutId = a.getResourceId(R.styleable.ExtendedRecyclerView_recyclerSwipe, R.layout.layout_extendrecycler_content);
            } finally {
                a.recycle();
            }
        }
        return layoutId;
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ExtendedRecyclerView);
        if (a != null) {
            try {
                mClipToPadding = a.getBoolean(R.styleable.ExtendedRecyclerView_recyclerClipToPadding, false);
                mPadding = (int) a.getDimension(R.styleable.ExtendedRecyclerView_recyclerPadding, -1.0f);
                mPaddingTop = (int) a.getDimension(R.styleable.ExtendedRecyclerView_recyclerPaddingTop, 0.0f);
                mPaddingBottom = (int) a.getDimension(R.styleable.ExtendedRecyclerView_recyclerPaddingBottom, 0.0f);
                mPaddingLeft = (int) a.getDimension(R.styleable.ExtendedRecyclerView_recyclerPaddingLeft, 0.0f);
                mPaddingRight = (int) a.getDimension(R.styleable.ExtendedRecyclerView_recyclerPaddingRight, 0.0f);
                mScrollbarStyle = a.getInt(R.styleable.ExtendedRecyclerView_scrollbarStyle, -1);
            } finally {
                a.recycle();
            }
        }

    }

    protected void initSwipeRefreshView() {
        mSwipeRefreshView = (ViewGroup) this.findViewById(R.id.swipe_layout);
        if (mSwipeRefreshView == null) {
            throw new IllegalStateException("ExtendedRecyclerView must have a Pull-refresh view ");
        }
        if (mSwipeRefreshView instanceof PullToRefreshHandler) {
            setPullToRefreshHandler((PullToRefreshHandler) mSwipeRefreshView);
        }
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setPullToRefreshHandler(PullToRefreshHandler pullToRefresh) {
        this.pullToRefreshHandler = pullToRefresh;
    }

    public void setHasFixedSize(boolean hasFixedSize) {
        mRecyclerView.setHasFixedSize(hasFixedSize);
    }

    public ViewGroup getSwipeRefreshView() {
        return mSwipeRefreshView;
    }

    private void initRecyclerView() {

        View recyclerView = this.findViewById(android.R.id.list);

        if (recyclerView instanceof RecyclerView)
            mRecyclerView = (RecyclerView) recyclerView;
        else {
            throw new IllegalArgumentException("ExtendedRecyclerView works with a RecyclerView!");
        }
        mRecyclerView.setClipToPadding(mClipToPadding);


        if (mPadding != -1.0f) {
            mRecyclerView.setPadding(mPadding, mPadding, mPadding, mPadding);
        } else {
            mRecyclerView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
        }

        if (mScrollbarStyle != -1) {
            mRecyclerView.setScrollBarStyle(mScrollbarStyle);
        }
    }

    /**
     * Set the layout manager to the recycler
     *
     * @param manager RecyclerView LayoutManager
     */
    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        mRecyclerView.setLayoutManager(manager);
    }

    /**
     * Set the adapter to the recycler
     * Automatically hide the progressbar
     * Set the refresh to false
     * If adapter is empty, then the emptyview is shown
     *
     * @param adapter RecyclerView adapter
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
        if (pullToRefreshHandler != null) {
            pullToRefreshHandler.swipeRefreshComplete();
        }
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
                if (pullToRefreshHandler != null) {
                    pullToRefreshHandler.swipeRefreshComplete();
                }
                if (mRecyclerView.getAdapter().getItemCount() == 0) {
                    switchState(State.STATE_EMPTY);
                } else {
                    switchState(State.STATE_CONTENT);
                }
            }
        });
        if (adapter.getItemCount() == 0) {
            switchState(State.STATE_EMPTY);
        } else {
            switchState(State.STATE_CONTENT);
        }
    }

    /**
     * Set the empty adapter to the recycler
     * show the progressbar
     * hide recycler view ,empty view
     * adapter data should be empty
     *
     * @param adapter RecyclerView adapter
     */
    public void setProgressAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
        switchState(State.STATE_PROGRESS);
        if (pullToRefreshHandler != null) {
            pullToRefreshHandler.swipeRefreshComplete();
        }
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
                if (pullToRefreshHandler != null) {
                    pullToRefreshHandler.swipeRefreshComplete();
                }
                if (mRecyclerView.getAdapter().getItemCount() == 0) {
                    switchState(State.STATE_EMPTY);
                } else {
                    switchState(State.STATE_CONTENT);
                }
            }
        });
    }

    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        mRecyclerView.setItemAnimator(animator);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration, int index) {
        mRecyclerView.addItemDecoration(itemDecoration, index);
    }

    public void removeItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.removeItemDecoration(itemDecoration);
    }

    /**
     * refresh finish callback
     */
    public interface PullToRefreshHandler {
        void swipeRefreshComplete();
    }
}
