package com.zhangyiwen.study.nio.simple_exersice;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangyiwen on 16/10/31.
 */
public class NioClient {

    public static void main(String[] args) throws Exception{
        serve();
    }
    /**
     * NIO实现的Client
     */
    public static void serve(){
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("127.0.0.1",8080));

            if(socketChannel.finishConnect()){  //连接完成,可以开始发送数据了
                int i = 0;
                while (true){
                    //停顿1秒
                    TimeUnit.SECONDS.sleep(1);
                    //写入数据到buffer
                    String info = "I'm "+i+++"-th information from client";
                    buffer.clear();
                    buffer.put(info.getBytes());
                    //channel写出buffer中的数据
                    buffer.flip();

                    /*
                     *write()方法无法保证能写多少字节到SocketChannel。
                     * 所以重复调用write()直到Buffer没有要写的字节为止。
                     */
                    while (buffer.hasRemaining()){
                        System.out.println(buffer);
                        socketChannel.write(buffer);
                    }
                }
            }
        } catch (IOException e ) {
            e.printStackTrace();
        } catch (InterruptedException e ) {
            e.printStackTrace();
        }finally {
            if(socketChannel != null){
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
