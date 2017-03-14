package com.zhangyiwen.study.nio.simple_exersice;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * Created by zhangyiwen on 16/11/1.
 */
public class DemoOfTransfer {

    public static void main(String[] args) throws Exception {
//        transferFrom();
        transferTo();
    }


    public static void transferFrom(){
        RandomAccessFile fromFile = null;
        RandomAccessFile toFile = null;

        try {
            fromFile = new RandomAccessFile("test_from.txt","rw");
            FileChannel fromChannel = fromFile.getChannel();
            toFile = new RandomAccessFile("test_to.txt","rw");
            FileChannel toChannel = toFile.getChannel();

            long position = 0;
            long count = fromChannel.size();
            System.out.println(count);
            toChannel.transferFrom(fromChannel,position,count);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                if(fromFile != null){
                    fromFile.close();
                }
                if(toFile != null){
                    toFile.close();
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void transferTo(){
        RandomAccessFile fromFile = null;
        RandomAccessFile toFile = null;

        try {
            fromFile = new RandomAccessFile("test_from.txt","rw");
            FileChannel fromChannel = fromFile.getChannel();
            toFile = new RandomAccessFile("test_to.txt","rw");
            FileChannel toChannel = toFile.getChannel();

            long position = 0;
            long count = fromChannel.size();
            System.out.println(count);
            fromChannel.transferTo(position,count,toChannel);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                if(fromFile != null){
                    fromFile.close();
                }
                if(toFile != null){
                    toFile.close();
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }

}
