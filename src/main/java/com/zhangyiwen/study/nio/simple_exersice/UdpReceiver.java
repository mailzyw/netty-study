package com.zhangyiwen.study.nio.simple_exersice;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * DUP接收端
 * Created by zhangyiwen on 16/11/1.
 */
public class UdpReceiver {

    public static void main(String[] args) throws Exception {
        receive();
    }

    public static void receive(){
        DatagramChannel channel = null;
        try {
            channel = DatagramChannel.open();
            channel.socket().bind(new InetSocketAddress(8088));

            ByteBuffer buf = ByteBuffer.allocate(1024);
            buf.clear();
            channel.receive(buf);
            buf.flip();
            while (buf.hasRemaining()){
                System.out.print((char)buf.get());
            }
            System.out.println();

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
