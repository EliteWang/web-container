package com.yanan.webcontainer.complicated;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * tomcat核心类
 * @author wangyn-b
 */
public class TomcatCore {


    private static final Logger logger = LoggerFactory.getLogger(TomcatCore.class);


    public static void main(String[] args) throws Exception {

        logger.info("tomcat start to initialize...");
        Thread.sleep(3000);

        logger.info("tomcat have finished the initialization");
        Thread.sleep(1000);

        logger.info("tomcat start the servlet");
        logger.info("read the servlet configuration");

        InputStream inputStream = ClassLoader.getSystemResourceAsStream("tomcat-conf.txt");


        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String path = reader.readLine();
        String className = reader.readLine();

        reader.close();


        File file = new File(path);
        URL url = file.toURI().toURL();
        URLClassLoader loader = new URLClassLoader(new URL[]{url});

        Class<?> clazz = loader.loadClass(className);
        GenericServlet genericServlet = (GenericServlet) clazz.newInstance();
        genericServlet.service();



    }




}
