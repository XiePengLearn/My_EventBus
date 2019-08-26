package com.example.eventbus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})//声明这个注解是放在什么上面注解作用域
public @interface Subscribe {//声明我们注解的生命周期
    ThreadMode threadMode() default ThreadMode.POSTING;
}
