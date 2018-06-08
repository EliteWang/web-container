package com.yanan.webcontainer.complicated;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericServlet implements Servlet {


    private final Logger logger = LoggerFactory.getLogger(GenericServlet.class);

    @Override
    public void doGet() {

    }

    @Override
    public void service() {
        logger.info("generic servlet service ");


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("database operator");

        logger.info("web world");

        logger.info("over...");
    }

    @Override
    public void doPost() {

    }

}
