package com.example.eventbus;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventBus {
    private static EventBus sEventBus = new EventBus();

    //装载所有订阅方法是 容器
    private Map<Object, List<MethodManager>> methodMap;
    //切换线程的Handler

    private Handler mHandler;

    //创建线程池
    private ExecutorService mExecutorService;


    private EventBus() {
        methodMap = new HashMap<>();
        mHandler = new Handler(Looper.getMainLooper());

        mExecutorService = Executors.newCachedThreadPool();
    }

    public static EventBus getDefault() {

        return sEventBus;
    }

    /**
     * 注册订阅者  把传进来的组件 收取所有的 订阅方法
     *
     * @param object
     */

    public void register(Object object) {
        //收集订阅方法
        List<MethodManager> methodManagers = methodMap.get(object);
        if (methodManagers == null || methodManagers.size() == 0) {

            methodManagers = findMethod(object);
            methodMap.put(object, methodManagers);
        }
    }

    /**
     * 去object里面找订阅方法
     *
     * @param object
     */
    private List<MethodManager> findMethod(Object object) {
        List<MethodManager> methodManagers = new ArrayList<>();
        Class<?> aClass = object.getClass();
        Method[] declaredMethods = aClass.getDeclaredMethods();
        //遍历方法
        for (Method declaredMethod : declaredMethods) {
            //判断这个方法是否符合订阅者的要求
            Subscribe annotation = declaredMethod.getAnnotation(Subscribe.class);
            if (annotation == null) {
                continue;
            }
            //获取该方法的所有接收参数类型
            Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
            if (parameterTypes != null && parameterTypes.length > 1) {

                //不符合我们要求
                throw new RuntimeException("不符合我们的要求");
            }
            //线程类型
            ThreadMode threadMode = annotation.threadMode();
            MethodManager methodManager = new MethodManager(declaredMethod, parameterTypes[0],
                    threadMode);
            methodManagers.add(methodManager);
        }

        return methodManagers;
    }

    /**
     * 发布消息
     *
     * @param setter
     */
    public void post(final Object setter) {

        //拿到map的key的遍历对象
        Iterator<Object> iterator = methodMap.keySet().iterator();
        while (iterator.hasNext()) {

            final Object next = iterator.next();
            List<MethodManager> methodManagers = methodMap.get(next);
            //遍历方法集合
            for (final MethodManager methodManager : methodManagers) {
                //如果类型匹配
                if (setter.getClass().isAssignableFrom(methodManager.getType())) {
                    //线程切换就在这里
                    //                    if(Looper.myLooper() == Looper.getMainLooper()){
                    //
                    //                    }
                    switch (methodManager.getThreadMode()) {
                        case POSTING:
                            invoke(setter, next, methodManager);
                            break;
                        case MAIN:
                            //如果当前线程是主线程的话 直接执行就行了
                            if (Looper.myLooper() == Looper.getMainLooper()) {
                                invoke(setter, next, methodManager);
                            } else {
                                //当前线程不是主线程  切换线程去执行
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(setter, next, methodManager);

                                    }
                                });
                            }

                            break;
                        case BACKGROUND:
                            //如果当前线程是主线程的话 需要切换到子线程执行
                            if (Looper.myLooper() == Looper.getMainLooper()) {

                                mExecutorService.submit(new Runnable() {
                                    @Override
                                    public void run() {
                                        //如果当前线程是主线程的话 需要切换到子线程执行
                                        invoke(setter, next, methodManager);
                                    }
                                });
                            } else {
                                //如果当前线程是子线程的话 直接执行就行了
                                invoke(setter, next, methodManager);
                            }
                            break;

                    }

                }

            }
        }
    }

    private void invoke(Object setter, Object next, MethodManager methodManager) {
        Method method = methodManager.getMethod();
        try {
            method.invoke(next, setter);
        } catch (IllegalAccessException e) {
            e.printStackTrace();

        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消注册的 方法
     *
     * @param object
     */
    public void unRegister(Object object) {

        if (methodMap.get(object) != null) {

            methodMap.remove(object);
        }
    }
}
