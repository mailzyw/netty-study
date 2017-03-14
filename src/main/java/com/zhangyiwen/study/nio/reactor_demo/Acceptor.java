package com.zhangyiwen.study.nio.reactor_demo;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * Created by zhangyiwen on 16/11/8.
 */
public class Acceptor {

    static final Charset charset = Charset.forName("UTF-8");

    public void accept(ServerSocketChannel serverChannel, Selector selector) throws IOException{
        System.out.println("[event]connect");
        // 建立连接
        SocketChannel socketChannel = serverChannel.accept();
        socketChannel.configureBlocking(false);
        System.out.println("[new client connected] client:" + socketChannel.getRemoteAddress());

        // 用selector注册套接字，并返回对应的SelectionKey，同时设置Key的interest set为监听该连接上得read事件
        SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);

        // 创建Handler,专门处理该连接后续发生的OP_READ和OP_WRITE事件
        Handler eventHandler = new EventHandler();
        // 绑定Handler到selectionKey上
        selectionKey.attach(eventHandler);

        // 发送欢迎语
//        socketChannel.write(charset.encode("welcome my client."));
    }

}
