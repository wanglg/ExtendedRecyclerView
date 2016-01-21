package com.leowong.extendedrecyclerview.models;

import java.io.Serializable;

public class ViewItem implements Serializable {


    public Object model;
    public int itemType;

    public ViewItem(int itemType, Object model) {
        this.itemType = itemType;
        this.model = model;
    }
}