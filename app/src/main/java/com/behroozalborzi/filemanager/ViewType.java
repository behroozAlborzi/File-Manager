package com.behroozalborzi.filemanager;

/**
 * Created by Behrooz on 7/5/2021.
 * https://behroozalborzi.ir
 * Android Developer
 * Thank you ... :)
 */
public enum ViewType {

    ROW(0),GRID(1);

    private int value;
    ViewType(int value) {

        this.value = value;

    }

    public int getValue() {
        return value;

    }
}
