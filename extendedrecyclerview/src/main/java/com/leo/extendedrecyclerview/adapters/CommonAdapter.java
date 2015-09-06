package com.leo.extendedrecyclerview.adapters;

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
 * Created by wangliugeng on 2015/5/15.
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<CommonAdapter.CommonViewHolder> {
    protected List<T> mDatas;

    public CommonAdapter(List<T> mDatas) {
        if (mDatas == null)
            mDatas = new ArrayList<T>();
        this.mDatas = mDatas;
    }

    @Override
    public CommonAdapter.CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false);
        return new CommonViewHolder(itemView);
    }


    @Override
    public int getItemCount() {
        if (mDatas == null)
            return 0;
        return mDatas.size();
    }

    public void addItem(T viewItem) {
        if (mDatas != null) {
            mDatas.add(viewItem);
            notifyDataSetChanged();
        }
    }

    public void addAll(List<T> newData) {
        if (mDatas != null) {
            int start = mDatas.size();
            mDatas.addAll(newData);
            notifyItemRangeInserted(start, mDatas.size() - 1);
        }
    }

    public void replaceAll(List<T> newData) {
        clearAll();
        if (newData == null) {
            newData = new ArrayList<T>();
        }
        mDatas = newData;
        notifyItemRangeInserted(0, mDatas.size() - 1);
    }

    public void clearAll() {
        if (mDatas == null)
            return;
        int size = this.mDatas.size();
        if (size > 0) {
            mDatas = new ArrayList<T>();
            this.notifyItemRangeRemoved(0, size);
        }
    }

    public abstract int getLayoutId(int viewType);

    public static class CommonViewHolder extends RecyclerView.ViewHolder {
        private final SparseArray<View> mViews;

        public CommonViewHolder(View itemView) {
            super(itemView);
            mViews = new SparseArray<View>();
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
            TextView view = (TextView) getView(viewId);
            view.setText(text);
        }

        public void setImageResource(int viewId, int drawableId) {
            ImageView view = (ImageView) getView(viewId);
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
