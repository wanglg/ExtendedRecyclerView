package com.leo.extendedrecyclerview.models;

import java.io.Serializable;

/**
 * Created by wangliugeng on 15/3/31.
 */
public class ViewItem implements Serializable {


    public Object model;
    public int viewType;

    public ViewItem(int viewType, Object model) {
        this.viewType = viewType;
        this.model = model;
    }
}