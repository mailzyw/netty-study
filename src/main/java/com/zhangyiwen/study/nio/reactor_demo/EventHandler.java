package com.zhangyiwen.study.nio.reactor_demo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhangyiwen on 16/11/7.
 */
public class EventHandler implements Handler{

    ByteBuffer              readBuffer          =   ByteBuffer.allocate(MAXIN);
    ByteBuffer              outBuffer           =   ByteBuffer.allocate(MAXOUT);
    static final int        MAXIN               =   256*1024;
    static final int        MAXOUT              =   256*1024;
    static final Charset    charset             =   Charset.forName("UTF-8");


    public void handle(SocketChannel socketChannel,SelectionKey sk) throws IOException{
        if (sk.isReadable()) {
            System.out.println("[event]read");
            read(socketChannel,sk);
        } else if (sk.isWritable()) {
            System.out.println("[event]write");
            write(socketChannel,sk);
        }
    }

    /**
     * 处理read事件
     * @throws IOException
     */
    private void read(SocketChannel socketChannel,SelectionKey sk) throws IOException{
        // 读取数据
        readBuffer.clear();
        final StringBuilder content = new StringBuilder();
        int readNum = socketChannel.read(readBuffer);
        System.out.println("===readNum:"+readNum);
        if(readNum==0){
            return;
        }else if(readNum<0){
            socketChannel.close();
        }else {
            readBuffer.flip();
            content.append(charset.decode(readBuffer)); //decode
        }
        while(socketChannel.read(readBuffer) > 0)
        {
            readBuffer.flip();
            content.append(charset.decode(readBuffer)); //decode
        }
        // 处理数据
        process(socketChannel,content.toString());
        //
        sk.interestOps(SelectionKey.OP_WRITE);
    }

    /**
     * 处理客户端请求数据
     * @param content
     */
    private void process(SocketChannel socketChannel,String content) throws IOException{
        // process
        System.out.println("[receive from client] -> client:" + socketChannel.getRemoteAddress() + ", content: " + content);
        // encode
        outBuffer = ByteBuffer.wrap(content.toUpperCase().getBytes());
    }

    /**
     * 处理write事件
     * @throws IOException
     */
    private void write(SocketChannel socketChannel,SelectionKey sk) throws IOException {
        // 写数据
        int writeNum = socketChannel.write(outBuffer);
        System.out.println("==writeNum:" + writeNum);
        if (outBuffer.remaining() > 0) {
            return;
        }
        //
        sk.interestOps(SelectionKey.OP_READ);
    }
}
