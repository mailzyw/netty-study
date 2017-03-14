package com.zhangyiwen.study.nio.simple_exersice;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * NIO与IO使用对比
 * Created by zhangyiwen on 16/10/31.
 */
public class DemoOfFileChannel {

    public static void main(String[] args) throws Exception{
        method1();  //NIO使用:采用FileChannel读取文件内容
        method2();  //IO使用:采用FileInputStream读取文件内容
    }

    /**
     * NIO使用:采用FileChannel+ByteBuffer读取文件内容
     */
    public static void method1(){
       RandomAccessFile aFile = null;
        try {
            aFile = new RandomAccessFile("test.txt","rw");
            FileChannel fileChannel = aFile.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            int bytesRead = fileChannel.read(buffer);
            while (bytesRead != -1){
                buffer.flip();
                while (buffer.hasRemaining()){
                    System.out.print((char)buffer.get());
                }
                buffer.compact();
                bytesRead = fileChannel.read(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(aFile != null){
                    aFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * IO使用:采用FileInputStream读取文件内容
     */
    public static void method2(){
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream("test.txt"));
            byte[] buf = new byte[1024];
            int bytesRead = in.read(buf);
            while(bytesRead != -1){
                for(int i=0;i<bytesRead;i++){
                    System.out.print((char)buf[i]);
                }
                bytesRead = in.read(buf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(in != null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
