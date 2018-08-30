package com.wyn.mvc.servlet;

import com.wyn.mvc.annotation.Controller;
import com.wyn.mvc.annotation.RequestMapping;
import com.wyn.mvc.util.BeanUtil;
import com.wyn.mvc.util.RequestMappingMap;
import com.wyn.mvc.util.ScanClassUtil;
import com.wyn.mvc.util.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        execute(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        execute(request, response);
    }




    //扫描Controller注解类，将有controller和requestMapping注解的类添加到map中
    private void initRequestMappingMap(String packageName) {
        Set<Class<?>> classes = ScanClassUtil.getClasses(packageName);
        for(Class clazz : classes) {
            if(clazz.isAnnotationPresent(Controller.class)) {
                Method[] methods = BeanUtil.findDeclaredMethods(clazz);
                for(Method method : methods) {
                    if(method.isAnnotationPresent(RequestMapping.class)) {
                        String annotation = method.getAnnotation(RequestMapping.class).value();
                        if(annotation != null && !"".equals(annotation.trim())) {
                            if(RequestMappingMap.getRequestMap().containsKey(annotation)) {
                                throw new RuntimeException("duplicate mapping...");
                            }
                            RequestMappingMap.getRequestMap().put(annotation,clazz);
                        }
                    }
                }
            }
        }
    }



    private void execute(HttpServletRequest request, HttpServletResponse response) {
        //将当前线程中request对象添加到ThreadLocal中
        WebContext.requestHolder.set(request);

        WebContext.responseHolder.set(response);

        //获取请求路径
        String  requestPath = parseRequestURI(request);

        Class<?> clazz = RequestMappingMap.getRequestMap().get(requestPath);

        Object classInstance = BeanUtil.instanceClass(clazz);

        Method[] methods = BeanUtil.findDeclaredMethods(clazz);

        Method annotatedMethod = null;
        for(Method method : methods) {
            if(method.isAnnotationPresent(RequestMapping.class)) {
                String annotationValue = method.getAnnotation(RequestMapping.class).value();

                if(annotationValue != null && !"".equals(annotationValue.trim()) && requestPath.equals(annotationValue)) {
                    annotatedMethod = method;
                    break;
                }
            }
        }

        try {
            if(annotatedMethod != null) {
                //执行目标方法处理用户请求
                Object retObj = annotatedMethod.invoke(classInstance);
                if(retObj == null) {
                    return;
                }

                //返回的可能是一个连接，也有可能是结果

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    private String parseRequestURI(HttpServletRequest request) {
        String path = request.getContextPath() + "/";
        String requestURI = request.getRequestURI();
        String midUrl = requestURI.replaceFirst(path,"");
        String lastUrl = midUrl.substring(0,midUrl.lastIndexOf("."));
        return lastUrl;
    }


}
