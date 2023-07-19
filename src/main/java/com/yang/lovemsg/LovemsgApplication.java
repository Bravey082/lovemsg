package com.yang.lovemsg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SpringBootApplication
public class LovemsgApplication {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        SpringApplication.run(LovemsgApplication.class, args);
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
