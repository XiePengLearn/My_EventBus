package com.example.eventbus;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * 订阅方法是封装
 */
public class MethodManager {

    //订阅方法本身

    private Method mMethod;
    //接收参数的类型
    private Class<?>   mType;

    //注解里面的枚举类型
    private ThreadMode mThreadMode;

    public MethodManager(Method method, Class<?> type, ThreadMode threadMode) {
        mMethod = method;
        mType = type;
        mThreadMode = threadMode;
    }


    public Method getMethod() {
        return mMethod;
    }

    public Class<?> getType() {
        return mType;
    }

    public ThreadMode getThreadMode() {
        return mThreadMode;
    }
}
