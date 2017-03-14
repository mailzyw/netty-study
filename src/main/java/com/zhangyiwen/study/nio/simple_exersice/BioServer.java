package com.zhangyiwen.study.nio.simple_exersice;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by zhangyiwen on 16/10/31.
 */
public class BioServer {

    public static void main(String[] args) throws Exception{
        serve();
    }

    /**
     * IO实现的Server
     */
    public static void serve(){
        ServerSocket serverSocket = null;
        InputStream in = null;
        try {
            serverSocket = new ServerSocket(8080);
            int recvMsgSize = 0;
            byte[] recvBuf = new byte[1024];
            while (true){
                //监听客户端连接
                Socket clientSocket = serverSocket.accept();
                SocketAddress clientAddress = clientSocket.getRemoteSocketAddress();
                System.out.println("Handling client at "+clientAddress);
                //
                in = clientSocket.getInputStream();
                while ((recvMsgSize = in.read(recvBuf)) != -1){//不断从inputStream中读取输入流
                    byte[] temp = new byte[recvMsgSize];
                    System.arraycopy(recvBuf, 0, temp, 0, recvMsgSize);
                    System.out.println(new String(temp));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                if(serverSocket != null){
                    serverSocket.close();
                }
                if(in != null){
                    in.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
