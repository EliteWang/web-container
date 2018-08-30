package com.wyn.mvc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 扫描指定包或者jar下面的class
 */
public class ScanClassUtil {


    private static Logger logger = LoggerFactory.getLogger(ScanClassUtil.class);


    //根据包名获取该包下面的所有class文件
    public static Set<Class<?>> getClasses(String packageName) {

        Set<Class<?>> classes = new LinkedHashSet<>();


        String originalPackageName = packageName;
        String packageDirName = packageName.replace('.','/');

        Enumeration<URL> dirs;

        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);

            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();

                String protocol = url.getProtocol();

                if("file".equals(protocol)) {
                    logger.info("scan file type of classes");
                    //获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(),"UTF-8");
                    return getClassesInPackage(originalPackageName,filePath,classes);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Set<Class<?>> getClassesInPackage(String originalPackageName, String filePath, Set<Class<?>> classes) {

        File dir = new File(filePath);

        if(!dir.exists() || !dir.isDirectory()) {
            logger.info(filePath + " is not exists or " + filePath + " is not a directory");
            return null;
        }

        File[] classFiles = dir.listFiles(pathname -> (pathname.isDirectory() || pathname.getName().endsWith(".class")));

        for(File clazz : classFiles) {
            if(clazz.isDirectory()) {
                getClassesInPackage(originalPackageName+"."+clazz.getName(),clazz.getAbsolutePath(),classes);
            } else {
                String className = clazz.getName().substring(0,clazz.getName().length() - 6);
                try {
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(originalPackageName + "." + className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return classes;
    }

}
