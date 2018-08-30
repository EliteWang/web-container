package com.wyn.mvc.util;

import java.util.HashMap;
import java.util.Map;

public class RequestMappingMap {


    private static Map<String,Class<?>> requestMap = new HashMap<>();


    public static Map<String,Class<?>> getRequestMap() {
        return requestMap;
    }

    public static void put(String path,Class<?> className) {
        requestMap.put(path,className);
    }

    public static Class<?> getClassName(String path) {
        return requestMap.get(path);
    }


}
