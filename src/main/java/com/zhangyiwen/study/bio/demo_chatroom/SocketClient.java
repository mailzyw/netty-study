package com.zhangyiwen.study.bio.demo_chatroom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by zhangyiwen on 17/2/12.
 */
public class SocketClient extends Socket{
    private static final String SERVER_IP ="127.0.0.1";
    private static final int SERVER_PORT =2017;

    private Socket client;
    private PrintWriter out;
    private BufferedReader in;
    private BufferedReader buff;

    /**
     * 与服务器连接，并输入发送消息
     */
    public SocketClient()throws Exception{
        super(SERVER_IP, SERVER_PORT);
        client =this;
        out =new PrintWriter(this.getOutputStream(),true);
        buff =new BufferedReader(new InputStreamReader(client.getInputStream()));
        in =new BufferedReader(new InputStreamReader(System.in));

        // 单独线程处理服务端发来的信息
        new readLineThread().start();

        // 主动向服务端发信息
        while(true){
            String input = in.readLine();
            out.println(input);
            out.flush();
        }
    }

    /**
     * 用于监听服务器端向客户端发送消息线程类
     */
    class readLineThread extends Thread{

        @Override
        public void run() {
            try {
                while(true){
                    String result = buff.readLine();
                    if(result==null || "byeClient".equals(result)){//客户端申请退出，服务端返回确认退出
                        break;
                    }else{//输出服务端发送消息
                        System.out.println("->"+result);
                    }
                }
                in.close();
                out.close();
                client.close();
                System.out.println("server disconnected.");
            }catch (Exception e) {
            }
        }
    }

    public static void main(String[] args) {
        try {
            new SocketClient();//启动客户端
        }catch (Exception e) {
        }
    }
}
