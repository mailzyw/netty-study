package com.zhangyiwen.study.nio.reactor_demo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by zhangyiwen on 16/11/8.
 * 定时消息客户端
 */
public class NioClient2 {

    private InetAddress hostAddress;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private ByteBuffer readBuffer = ByteBuffer.allocate(8192);
    private ByteBuffer outBuffer = ByteBuffer.wrap("nice to meet you".getBytes());
    static final Charset charset = Charset.forName("UTF-8");

    public NioClient2(InetAddress hostAddress, int port) throws IOException {
        this.hostAddress = hostAddress;
        this.port = port;
        initSelector();
    }

    public static void main(String[] args) {
        try {
            new NioClient2(InetAddress.getByName("localhost"), 9090).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initSelector() throws IOException {
        // 创建一个selector
        selector = SelectorProvider.provider().openSelector();
        // 打开SocketChannel
        socketChannel = SocketChannel.open();
        // 设置为非阻塞
        socketChannel.configureBlocking(false);
        // 连接指定IP和端口的地址
        socketChannel.connect(new InetSocketAddress(this.hostAddress, this.port));
        // 用selector注册套接字，并返回对应的SelectionKey，同时设置Key的interest set为监听服务端已建立连接的事件
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        // 开启新线程执行
    }

    public void start() {

        while (true) {
            try {
                selector.select();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Iterator<?> selectedKeys = selector.selectedKeys().iterator();
            while (selectedKeys.hasNext()) {
                SelectionKey key = (SelectionKey) selectedKeys.next();
                selectedKeys.remove();
                if (!key.isValid()) {
                    continue;
                }
                dispatch(key);
            }
        }
    }

    /**
     * 事件处理分发
     * @param key 已经ready的selectionKey
     */
    private void dispatch(SelectionKey key){
        try {
            if (key.isConnectable()) {
                System.out.println("[event]connect");
                finishConnection(key);
            } else if (key.isReadable()) {
                System.out.println("[event]read");
                read(key);
            } else if (key.isWritable()) {
                System.out.println("[event]write");
                write(key);
            }
        } catch (Exception e) {
            System.out.println("deal exception begin");
            e.printStackTrace();
            key.channel();
            try {
                if(key.channel()!=null){
                    key.channel().close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.out.println("deal exception end");
        }
    }

    /**
     * 完成与服务端连接
     * @param key
     * @throws IOException
     */
    private void finishConnection(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        try {
            // 判断连接是否建立成功，不成功会抛异常
            socketChannel.finishConnect();
        } catch (IOException e) {
            key.cancel();
            if(socketChannel != null)
            {
                socketChannel.close();
            }
            return;
        }
        // 设置Key的interest set为OP_WRITE事件
        key.interestOps(SelectionKey.OP_READ);
    }

    /**
     * 处理read
     * @param key
     * @throws IOException
     */
    private void read(SelectionKey key) throws IOException {
        // 读取数据
        SocketChannel socketChannel = (SocketChannel) key.channel();
        readBuffer.clear();
        StringBuilder content = new StringBuilder();
        int readNum = socketChannel.read(readBuffer);
        System.out.println("===readNum:"+readNum);
        if(readNum==0){
            return;
        }else if(readNum<0){
            socketChannel.close();
            System.out.println("readNum < 0. "+readNum);
        }else {
            readBuffer.flip();
            content.append(charset.decode(readBuffer)); //decode
        }
        try {
            while(socketChannel.read(readBuffer) > 0)
            {
                readBuffer.flip();
                content.append(charset.decode(readBuffer));
            }
        } catch (IOException e) {
            key.channel();
            if(socketChannel != null)
            {
                socketChannel.close();
            }
        }
        // 处理数据
        process(content.toString(), key);
        // 设置Key的interest set为监听该连接上的write事件
        key.interestOps(SelectionKey.OP_WRITE);
    }

    /**
     * 处理服务端响应数据
     * @param content
     */
    private void process(String content,SelectionKey key){
        System.out.println("[Client receive from server] -> content: " + content);

        outBuffer = ByteBuffer.wrap(content.toLowerCase().getBytes());
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理write
     * @param key
     * @throws IOException
     */
    private void write(SelectionKey key) throws IOException {
        // 写数据
        SocketChannel socketChannel = (SocketChannel) key.channel();
        int writeNum = socketChannel.write(outBuffer);
        System.out.println("==writeNum:"+writeNum);
        if (outBuffer.remaining() > 0) {
            return;
        }
        // 设置Key的interest set为OP_READ事件
        key.interestOps(SelectionKey.OP_READ);
    }

}
