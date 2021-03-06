package com.leowong.extendedrecyclerview.adapters;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leowong.extendedrecyclerview.utils.WrapperUtils;

/**
 * User: wanglg
 * Date: 2017-10-27
 * Time: 11:46
 * FIXME
 */
public class LoadMoreWrapperAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ITEM_TYPE_LOAD_MORE = Integer.MAX_VALUE - 2;
    private RecyclerView.Adapter mInnerAdapter;
    private View mLoadMoreView;
    private int mLoadMoreLayoutId;
    private boolean isLoadingCompleted;
    private ILoadMoreCallback callback;

    public LoadMoreWrapperAdapter(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_LOAD_MORE) {
            CommonAdapter.CommonViewHolder holder;
            if (mLoadMoreView != null) {
                holder = new CommonAdapter.CommonViewHolder(mLoadMoreView);
            } else {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(mLoadMoreLayoutId, parent,
                        false);
                holder = new CommonAdapter.CommonViewHolder(itemView);
            }
            return holder;
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isShowLoadMore(position)) {
            if (callback != null) {
                callback.loadMore(holder, position);
            }
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position);
    }

    public void setLoadMoreCallback(ILoadMoreCallback callback) {
        this.callback = callback;
    }

    @Override
    public int getItemCount() {
        return isLoadingCompleted ? mInnerAdapter.getItemCount() : mInnerAdapter.getItemCount() + (hasLoadMore() ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowLoadMore(position)) {
            return ITEM_TYPE_LOAD_MORE;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    private boolean isShowLoadMore(int position) {
        return hasLoadMore() && !isLoadingCompleted && (position >= mInnerAdapter.getItemCount());
    }

    private boolean hasLoadMore() {
        return mLoadMoreView != null || mLoadMoreLayoutId != 0;
    }

    public LoadMoreWrapperAdapter setLoadMoreView(View loadMoreView) {
        mLoadMoreView = loadMoreView;
        return this;
    }

    public boolean getLoadingCompleted() {
        return isLoadingCompleted;
    }

    public void setLoadingCompleted(boolean isLoadingCompleted) {
        this.isLoadingCompleted = isLoadingCompleted;
    }

    public LoadMoreWrapperAdapter setLoadMoreView(int layoutId) {
        mLoadMoreLayoutId = layoutId;
        return this;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                if (isShowLoadMore(position)) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null) {
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);

        if (isShowLoadMore(holder.getLayoutPosition())) {
            setFullSpan(holder);
        }
    }

    private void setFullSpan(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;

            p.setFullSpan(true);
        }
    }

    public interface ILoadMoreCallback {
        void loadMore(RecyclerView.ViewHolder holder, int position);
    }
}
