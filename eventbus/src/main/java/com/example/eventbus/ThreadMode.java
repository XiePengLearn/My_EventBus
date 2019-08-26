package com.example.eventbus;

public enum ThreadMode {

    /**
     * 随意
     */
    POSTING,

    /**
     * 不管发布者是在主线程还是在子线程 如果开发者选择的是MAIN就代表订闻方法一定要在主线程里面去执行
     */
    MAIN,

    /**
     * 不管发布者是在主线程还是在子线程 如果开发者选择的是BACKGROUND就代表订闻方法一定要在子线程里面去执行
     */
    BACKGROUND,

}
