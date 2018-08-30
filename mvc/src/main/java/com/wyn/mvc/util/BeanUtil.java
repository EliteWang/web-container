package com.wyn.mvc.util;

import java.lang.reflect.*;

public class BeanUtil {


    /**
     * 获取类中声明的方法
     * @param clazz
     * @return
     */
    public static Method[] findDeclaredMethods(Class clazz) {
        return clazz.getDeclaredMethods();
    }

    /**
     * 实例化
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T instanceClass(Class<T> clazz) {
        if(!clazz.isInterface()) {
            try {
                return clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 使用构造器实例化
     * @param constructor
     * @param args
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    public static <T> T instanceClass(Constructor<T> constructor,Object... args) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        makeAccessible(constructor);
        return constructor.newInstance(args);
    }

    private static <T> void makeAccessible(Constructor<T> constructor) {
        if(!Modifier.isPublic(constructor.getModifiers())
                || !Modifier.isPublic(constructor.getDeclaringClass().getModifiers())
                || !constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
    }


    public static Method findMethod(Class<?> clazz,String methodName,Class<?>... paramTypes) {
        try {
            return clazz.getMethod(methodName,paramTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return findDeclaredMethod(clazz,methodName,paramTypes);
        }
    }

    public static Method findDeclaredMethod(Class<?> clazz,String methodName,Class<?>... paramTypes) {
        try {
            return clazz.getDeclaredMethod(methodName,paramTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            if(clazz.getSuperclass() != null) {
                return findDeclaredMethod(clazz.getSuperclass(),methodName,paramTypes);
            }
            return null;
        }
    }

    public static Field[] findDeclaredFields(Class<?> clazz) {
        return clazz.getDeclaredFields();
    }
}
