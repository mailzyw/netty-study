package com.zhangyiwen.study.bio.demo_one_thread_loop;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by zhangyiwen on 17/2/12.
 */
public class SocketClient {
    public static void main(String[] args) {
        try {
            /** 创建Socket*/
            // 创建一个流套接字并将其连接到指定 IP 地址的指定端口号(本处是本机)
            Socket socket = new Socket("127.0.0.1",2017);
            // 60s超时
            socket.setSoTimeout(60*1000);

            /** 发送客户端准备传输的信息,并接收服务端响应 */
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true);
            BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String result ="";
            while(result.indexOf("bye") == -1){
                BufferedReader sysBuff =new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Please input a line of String send to Server...");
                printWriter.println(sysBuff.readLine());
                printWriter.flush();

                result = bufferedReader.readLine();
                System.out.println("Server say : " + result);
            }

            /** 关闭Socket*/
            printWriter.close();
            bufferedReader.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Exception:" + e);
        }
    }
}
