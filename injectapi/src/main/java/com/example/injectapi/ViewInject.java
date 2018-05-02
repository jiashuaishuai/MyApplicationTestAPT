package com.example.injectapi;

/**
 * Created by JiaShuai on 2018/4/26.
 */

public interface ViewInject<T> {
    void inject(T target, Object source);
}
