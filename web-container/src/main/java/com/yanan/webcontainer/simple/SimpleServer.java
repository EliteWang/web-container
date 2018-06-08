package com.yanan.webcontainer.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.StringTokenizer;

public class SimpleServer {

    private static Logger logger = LoggerFactory.getLogger(SimpleServer.class);

    private static final int PORT = 8080;

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);

            ServletHandler servletHandler = new ServletHandler(serverSocket);
            servletHandler.start();



        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    private static class ServletHandler extends Thread {

        ServerSocket serverSocket = null;

        public ServletHandler(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }

        @Override
        public void run() {
            while (true) {
                Socket client = null;
                try {
                    client = serverSocket.accept();

                    if(client != null) {
                        logger.info("接收到一个客户端请求");

                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));

                        //请求行
                        String line = bufferedReader.readLine();
                        logger.info("请求行： "+line);

                        //拆分请求路径，

                        String resourcePath = line.substring(line.indexOf("/"),line.lastIndexOf("/") - 5);
                        resourcePath = URLDecoder.decode(resourcePath,"UTF-8");
                        logger.info("请求的资源路径为："+ resourcePath);


                        //获取请求方法类型
                        String method = new StringTokenizer(line).nextElement().toString();
                        logger.info("请求的资源方法为："+ method);


                        while((line = bufferedReader.readLine()) != null) {
                            if("".trim().equals(line)) {
                                break;
                            }
                            logger.info("请求头："+line);
                        }

                    }

                } catch (Exception e) {

                }
            }

        }
    }






}
