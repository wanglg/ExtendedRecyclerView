package com.leowong.extendedrecyclerview.adapters;

import com.leowong.extendedrecyclerview.R;
import com.leowong.extendedrecyclerview.models.ViewItem;

import java.util.List;

/**
 * Created by wangliugeng on 2015/6/29.
 * default page count 25,可以通过构造函数或setPageCount修改 page count，如果page count不固定
 * 可设置pagecount 为Integer.MAX_VALUE，通过设置setLoadingCompleted(true)来结束加载更多
 */
public abstract class LoadMoreAdapter extends CommonAdapter<ViewItem> {

    public static final int VIEW_TYPE_ITEM_LOAD_MORE = 2015063009;
    private int loadMoreLayoutId = R.layout.layout_more_progress;
    private boolean isLoading;
    private boolean isLoadingCompleted;


    /**
     * one page item count
     */
    private int pageCount = 25;


    private ILoadMoreCallback callback;

    public LoadMoreAdapter(List<ViewItem> mDatas) {
        super(mDatas);
        if (this.mDatas.size() >= pageCount) {
            this.mDatas.add(getLoadMoreItem());
        }

    }

    public LoadMoreAdapter(int pageCount, List<ViewItem> mDatas) {
        super(mDatas);
        this.pageCount = pageCount;
        if (this.mDatas.size() >= pageCount) {
            this.mDatas.add(getLoadMoreItem());
        }
    }

    /**
     * @param mDatas
     * @param loadMoreLayoutId load more layout id
     */
    public LoadMoreAdapter(List<ViewItem> mDatas, int loadMoreLayoutId) {
        super(mDatas);
        this.loadMoreLayoutId = loadMoreLayoutId;
        if (this.mDatas.size() >= pageCount) {
            this.mDatas.add(getLoadMoreItem());
        }
    }

    public LoadMoreAdapter(int pageCount, List<ViewItem> mDatas, int loadMoreLayoutId) {
        super(mDatas);
        this.pageCount = pageCount;
        this.loadMoreLayoutId = loadMoreLayoutId;
        if (this.mDatas.size() >= pageCount) {
            this.mDatas.add(getLoadMoreItem());
        }
    }

    @Override
    public int getLayoutId(int viewType) {
        if (viewType == VIEW_TYPE_ITEM_LOAD_MORE)
            return loadMoreLayoutId;
        return getNormalLayoutId(viewType);
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
        int lastPosition = mDatas.size() - 1;
        if (lastPosition >= 0) {
            ViewItem viewItem = mDatas.get(lastPosition);
            if (viewItem.itemType == VIEW_TYPE_ITEM_LOAD_MORE) {
                mDatas.remove(lastPosition);
            }
        }
        if (this.mDatas.size() >= pageCount) {
            this.mDatas.add(getLoadMoreItem());
        }
    }

    public abstract int getNormalLayoutId(int itemType);

    public abstract void onBindNormalViewHolder(CommonViewHolder holder, int position);

    /**
     * 可重写此方法来自定义点击加载更多
     *
     * @param holder
     * @param position
     */
    public void onBindLoadMoreViewHolder(CommonViewHolder holder, int position) {
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {

        if (position == mDatas.size() - 1 && position >= pageCount - 1 && !isLoading && !isLoadingCompleted) {
            isLoading = true;
            onBindLoadMoreViewHolder(holder, position);
            if (callback != null) {
                callback.loadMore(holder, position);
            }
        }
        if (mDatas.get(position).itemType != VIEW_TYPE_ITEM_LOAD_MORE) {
            onBindNormalViewHolder(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        ViewItem viewItem = mDatas.get(position);
        return viewItem.itemType;
    }

    @Override
    public void addAll(List<ViewItem> newData) {
        hideLoadMore();
        if (newData == null || newData.size() < pageCount) {
            isLoadingCompleted = true;
        } else if (!isLoadingCompleted) {
            newData.add(getLoadMoreItem());
        }
        super.addAll(newData);
        isLoading = false;
    }

    @Override
    public void replaceAll(List<ViewItem> newData) {
        isLoading = false;
        isLoadingCompleted = false;
        if (newData == null || newData.size() < pageCount) {
            isLoadingCompleted = true;
        } else if (!isLoadingCompleted) {
            newData.add(getLoadMoreItem());
        }
        super.replaceAll(newData);
    }

    public void setLoadMoreCallback(ILoadMoreCallback callback) {
        this.callback = callback;
    }

    public boolean getLoadingCompleted() {
        return isLoadingCompleted;
    }

    public void setLoadingCompleted(boolean isLoadingCompleted) {
        this.isLoadingCompleted = isLoadingCompleted;
    }

    private ViewItem getLoadMoreItem() {
        return new ViewItem(VIEW_TYPE_ITEM_LOAD_MORE, null);
    }

    private void hideLoadMore() {
        int lastPosition = mDatas.size() - 1;
        if (lastPosition >= 0) {
            ViewItem viewItem = mDatas.get(lastPosition);
            if (viewItem.itemType == VIEW_TYPE_ITEM_LOAD_MORE) {
                mDatas.remove(lastPosition);
                notifyItemRemoved(lastPosition);
            }
        }
    }

    public interface ILoadMoreCallback {
        void loadMore(CommonViewHolder holder, int position);
    }
}
