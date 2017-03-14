package com.zhangyiwen.study.bio.demo_one_thread_loop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by zhangyiwen on 17/2/12.
 */
public class SocketServer {

    public static void main(String[] args) {
        try {
            /** 创建ServerSocket*/
            // 创建一个ServerSocket在端口2017监听客户请求
            ServerSocket serverSocket = new ServerSocket(2017);
            serverSocket.setReceiveBufferSize(1024);

            /**
             * 循环监听客户端连接,一次只能处理一个客户端连接
             */
            while (true){
                // 侦听并接受到此Socket的连接,请求到来则产生一个Socket对象，并继续执行
                Socket socket = serverSocket.accept();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                System.out.println("new client connected. " + socket.getRemoteSocketAddress());

                // 不断读取客户端发来的信息并响应,直到客户端主动发来bye为止
                String line = bufferedReader.readLine();
                while (line!= null && !line.equals("bye")) {
                    System.out.println("Client say: " + line);
                    printWriter.println(line.toUpperCase());
                    printWriter.flush();
                    line = bufferedReader.readLine();
                }

                // 客户端退出,服务端主动断连
                printWriter.println("bye, Client!");
                printWriter.flush();
                System.out.println("Client exit!");
                printWriter.close();
                bufferedReader.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //serverSocket.close
        }
    }
}
