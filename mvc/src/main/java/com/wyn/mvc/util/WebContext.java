package com.wyn.mvc.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author wangyn-b
 * 当前线程
 */
public class WebContext {


    public static ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();
    public static ThreadLocal<HttpServletResponse> responseHolder = new ThreadLocal<>();


    public HttpServletRequest getRequest() {
        return requestHolder.get();
    }

    public HttpSession getSession() {
        return requestHolder.get().getSession();
    }

    public ServletContext getServletContext() {
        return requestHolder.get().getSession().getServletContext();
    }


    public HttpServletResponse gerResponse() {
        return responseHolder.get();
    }





}
