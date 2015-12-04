package com.leowong.extendedrecyclerview.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Simple to use RecyclerView
 * Created by wangliugeng on 2015/5/15.
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<CommonAdapter.CommonViewHolder> {
    protected List<T> mDatas;
    public Context mContext;

    public CommonAdapter(List<T> mDatas) {
        if (mDatas == null)
            mDatas = new ArrayList<>();
        this.mDatas = mDatas;
    }

    @Override
    public CommonAdapter.CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false);
        mContext = itemView.getContext();
        return new CommonViewHolder(itemView);
    }


    @Override
    public int getItemCount() {
        if (mDatas == null)
            return 0;
        return mDatas.size();
    }

    public void addItem(T viewItem) {
        if (viewItem != null && mDatas != null) {
            mDatas.add(viewItem);
            notifyItemInserted(mDatas.size() - 1);
        }
    }

    public void addAll(List<T> newData) {
        if (mDatas != null) {
            int start = mDatas.size();
            mDatas.addAll(newData == null ? new ArrayList<T>() : newData);
            notifyItemRangeInserted(start, newData == null ? 0 : newData.size());
        } else {
            replaceAll(newData);
        }
    }

    public void replaceAll(List<T> newData) {
        clearAll();
        if (newData == null) {
            newData = new ArrayList<>();
        }
        mDatas = newData;
        //会有动画效果
        notifyItemRangeInserted(0, mDatas.size());
    }

    public void clearAll() {
        if (mDatas == null)
            return;
        int size = this.mDatas.size();
        if (size > 0) {
            mDatas = new ArrayList<>();
            this.notifyItemRangeRemoved(0, size);
        }
    }

    /**
     * the layout resource id
     *
     * @param viewType
     * @return
     */
    public abstract int getLayoutId(int viewType);

    public static class CommonViewHolder extends RecyclerView.ViewHolder {
        private final SparseArray<View> mViews;
        public Context mContext;

        public CommonViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mViews = new SparseArray<>();
        }

        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = itemView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        public void setText(int viewId, String text) {
            TextView view = getView(viewId);
            view.setText(text);
        }

        public void setTextColor(int viewId, int textColor) {
            TextView view = getView(viewId);
            view.setTextColor(textColor);
        }

        public void setVisible(int viewId, int visible) {
            View view = getView(viewId);
            view.setVisibility(visible);
        }

        public void setImageResource(int viewId, int drawableId) {
            ImageView view = getView(viewId);
            view.setImageResource(drawableId);
        }

        public void setOnClickListener(int viewId, View.OnClickListener clickListener) {
            getView(viewId).setOnClickListener(clickListener);
        }

        public void setOnItemClickListener(View.OnClickListener clickListener) {
            itemView.setOnClickListener(clickListener);
        }
    }
}
