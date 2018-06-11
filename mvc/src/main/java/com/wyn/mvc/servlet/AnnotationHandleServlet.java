package com.wyn.mvc.servlet;

import com.wyn.mvc.util.ScanClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class AnnotationHandleServlet extends HttpServlet {

    private Logger logger = LoggerFactory.getLogger(AnnotationHandleServlet.class);


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        logger.info("begin to initialize...");

        String basePackage = config.getInitParameter("basePackage");
        //如果配置了多个包（,）
        if(basePackage.indexOf(",") != -1) {
            String[] packageNames = basePackage.split(",");
            for(String packageName : packageNames) {
                initRequestMappingMap(packageName);
            }
        } else {
            initRequestMappingMap(basePackage);
        }

        logger.info("initialize over...");
    }

    private void initRequestMappingMap(String packageName) {
        Set<Class<?>> classes = ScanClassUtil.getClasses(packageName);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doPost(request, response);
    }



    private void execute(HttpServletRequest request, HttpServletResponse response) {

    }


    private String parseRequestURI(HttpServletRequest request) {
        String path = request.getContextPath() + "/";
        String requestURI = request.getRequestURI();
        String midUrl = requestURI.replaceFirst(path,"");
        String lastUrl = midUrl.substring(0,midUrl.lastIndexOf("."));
        return lastUrl;
    }


}
