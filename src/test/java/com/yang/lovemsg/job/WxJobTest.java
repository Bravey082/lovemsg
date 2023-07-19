package com.yang.lovemsg.job;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


class WxJobTest {

    @Test
    void sendMessage() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // 加载类
        Class<?> clazz = Class.forName("com.yang.lovemsg.job.WxJob");
        // 创建对象
        Object obj = clazz.newInstance();
        // 获取方法对象
        Method method = clazz.getMethod("sendMessage");
        // 调用方法
        method.invoke(obj);
    }
}