package com.zhangyiwen.study.nio.simple_exersice;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * UDP协议发送端
 * Created by zhangyiwen on 16/11/1.
 */
public class UdpSender {

    public static void main(String[] args) throws Exception{
        send();
    }

    public static void send(){
        DatagramChannel channel = null;
        try {
            channel = DatagramChannel.open();
            String info = "I am the Sender!";
            ByteBuffer buf = ByteBuffer.allocate(1024);
            buf.clear();
            buf.put(info.getBytes());
            buf.flip();

            int bytesSent = channel.send(buf,new InetSocketAddress("127.0.0.1",8088));
            System.out.println("bytesSent:"+bytesSent);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                if(channel!=null){
                    channel.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

}
