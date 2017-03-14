package com.zhangyiwen.study.nio.simple_exersice;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by zhangyiwen on 16/11/1.
 */
public class DemoOfScatterAndGatter {

    public static void main(String args[]){
        gather();
    }

    public static void gather(){
        ByteBuffer header = ByteBuffer.allocate(10);
        ByteBuffer body = ByteBuffer.allocate(10);

        byte[] b1 = {'0','1'};
        byte[] b2 = {'2','3'};
        header.put(b1);
        body.put(b2);

        ByteBuffer[] buffers = {header,body};

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("scattingAndGather.txt");
            FileChannel channel = fileOutputStream.getChannel();
            channel.write(buffers);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
